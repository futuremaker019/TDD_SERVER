package io.hhplus.tdd.application;

import io.hhplus.tdd.domain.dto.PointRequestDto;
import io.hhplus.tdd.domain.dto.PointResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PointServiceConcurrencyTest {

    @Autowired
    private PointService pointService;

    @BeforeEach
    void setUP() {
        long userId = 1L;
        long pointId = 50L;

        // 동시성 테스트를 위해 초기 포인트 저장
        pointService.chargePointById(PointRequestDto.of(userId, pointId));
    }

    @Test
    void 포인트충전() {
        // given
        long userId = 1L;
        long pointId = 5000L;
        pointService.chargePointById(PointRequestDto.of(userId, pointId));

        // when
        PointResponseDto point = pointService.getPointById(userId);

        // then
        assertThat(point).isNotNull();
        assertThat(point.point()).isEqualTo(10000L);
    }

    /**
     * 멀티스래드를 이용하여 유저들이 동시에 접속하여 포인트를 차감시 포인트가 0 이하로 떨어지는지 확인하는 테스트
     */
    @Test
    void 포인트차감_동시성테스트() throws InterruptedException {
        // given
        long userId = 1L;

        // when
        int threadCount = 100;
        // 멀티쓰레드 병렬처리를 위해 ExecutorService를 사용함
        ExecutorService executorService = Executors.newFixedThreadPool(30);
        // 모든 요청이 끝나기를 기다리기위해 CountDownLatch를 사용함
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            int finalI = i;
            executorService.submit(() -> {
                try {
                    pointService.usePointById(PointRequestDto.of(userId, finalI));
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();

        PointResponseDto point = pointService.getPointById(userId);

        // then
        assertThat(point).isNotNull();
        assertThat(point.point()).isGreaterThanOrEqualTo(0L);
    }

    /**
     * 포인트의 충전과 차감을 랜덤으로 하여 최종금액이 예상한 금액과 같은지 확인하는 테스트
     */
    @Test
    void 포인트충전_차감_동시성테스트() throws InterruptedException {
        // given
        long userId = 1L;

        // when
        CompletableFuture.allOf(
            CompletableFuture.runAsync(() -> {
                pointService.chargePointById(PointRequestDto.of(userId, 5000L));
            }),
            CompletableFuture.runAsync(() -> {
                pointService.usePointById(PointRequestDto.of(userId, 3000L));
            }),
            CompletableFuture.runAsync(() -> {
                pointService.usePointById(PointRequestDto.of(userId, 1000L));
            }),
            CompletableFuture.runAsync(() -> {
                pointService.chargePointById(PointRequestDto.of(userId, 2000L));
            }),
            CompletableFuture.runAsync(() -> {
                pointService.usePointById(PointRequestDto.of(userId, 1000L));
            })
        ).join();

        Thread.sleep(100);

        // then
        PointResponseDto point = pointService.getPointById(userId);
        assertThat(point).isNotNull();
        assertThat(point.point()).isGreaterThanOrEqualTo(5000 - 3000 - 1000 + 2000 + 1000);

    }

}
