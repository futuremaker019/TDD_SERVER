package io.hhplus.tdd.application;

import io.hhplus.tdd.domain.dto.PointHistoryRequestDto;
import io.hhplus.tdd.domain.dto.PointRequestDto;
import io.hhplus.tdd.domain.dto.PointResponseDto;
import io.hhplus.tdd.domain.point.TransactionType;
import io.hhplus.tdd.domain.point.UserPoint;
import io.hhplus.tdd.infra.repository.impl.PointRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
class PointServiceTest {

    @InjectMocks private PointService sut;
    @Mock private PointRepositoryImpl userPointRepository;
    @Mock private PointHistoryService pointHistoryService;

    private UserPoint userPoint;

    /**
     * 포인트 차감 테스트를 위해 초기값을 설정하는 작업으로 setUp을 만들었습니다.
     */
    @BeforeEach
    public void setUp() {
        long userId = 1L;
        long defaultPoint = 10L;
        UserPoint initalUserPoint = new UserPoint(userId, defaultPoint, System.currentTimeMillis());
        given(userPointRepository.save(userId, defaultPoint)).willReturn(initalUserPoint);
//        given(userPointRepository.findById(userId)).willAnswer(invocation -> userPoint);

        userPoint = userPointRepository.save(userId, defaultPoint);
    }

    @DisplayName("포인를 조회한다.")
    @Test
    public void 포인트를_조회한다() {
        // given
        long userId = 1L;
        UserPoint userPoint = UserPoint.of(userId, 0L, System.currentTimeMillis());
        given(userPointRepository.findById(userId)).willReturn(userPoint);

        // when
        PointResponseDto responseDto = sut.getPointById(userId);

        // then
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.id()).isEqualTo(userId);

        then(userPointRepository).should().findById(userId);
    }

    @DisplayName("포인트를 충전한다.")
    @Test
    public void 포인트를_충전한다() {
        // given
        long userId = 1L;
        long chargePoint = 100L;

        UserPoint findUserPoint = UserPoint.of(userId, 0, System.currentTimeMillis());
        given(userPointRepository.findById(userId)).willReturn(findUserPoint);
        doNothing().when(pointHistoryService).savePointHistory(
                PointHistoryRequestDto.of(userId, chargePoint, TransactionType.CHARGE)
        );

        long resultPoint = findUserPoint.point() + chargePoint;
        UserPoint chargedUserPoint = UserPoint.of(userId, resultPoint);
        given(userPointRepository.save(findUserPoint.id(), resultPoint)).willReturn(chargedUserPoint);

        // when
        PointResponseDto responseDto = sut.chargePointById(
                PointRequestDto.of(userId, resultPoint)
        );

        // then
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.id()).isEqualTo(userId);
        assertThat(responseDto.point()).isEqualTo(chargePoint);

        then(userPointRepository).should().findById(userId);
        then(userPointRepository).should().save(chargedUserPoint.id(), chargedUserPoint.point());
    }

    @DisplayName("충전하려고_하는_포인트가_5만_이상일때_예외를_발생시킨다")
    @Test
    public void 충전하려고_하는_포인트가_5만_이상일때_예외를_발생시킨다() {
        // given
        long userId = 1L;
        long chargePoint = 50000L;

        given(userPointRepository.findById(userId)).willReturn(userPoint);

        // when
        Throwable t = catchThrowable(() -> sut.chargePointById(PointRequestDto.of(userId, chargePoint)));

        // then
        assertThat(t)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("5만점 이상의 포인트를 충전할 수 없습니다. 잔액 %d".formatted(userPoint.point()));
    }

    @DisplayName("포인트를 차감한다.")
    @Test
    public void 포인트를_차감한다() {
        // given
        long userId = 1L;
        long usePoint = 5L;

        given(userPointRepository.findById(userId)).willReturn(userPoint);
        long resultPoint = userPoint.point() - usePoint;
        UserPoint chargedUserPoint = UserPoint.of(userId, resultPoint);
        given(userPointRepository.save(userId, resultPoint)).willReturn(chargedUserPoint);
        doNothing().when(pointHistoryService).savePointHistory(
                PointHistoryRequestDto.of(userId, resultPoint, TransactionType.USE)
        );

        // when
        PointResponseDto responseDto = sut.usePointById(
                PointRequestDto.of(userId, usePoint)
        );

        // then
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.id()).isEqualTo(userId);
        assertThat(responseDto.point()).isEqualTo(resultPoint);

        then(userPointRepository).should().findById(userId);
        then(userPointRepository).should().save(userId, resultPoint);
    }

    @DisplayName("포인트_차감시_금액이_마이너스면_예외를_처리한다")
    @Test
    public void 포인트_차감시_금액이_마이너스면_예외처리한다() {
        // given
        long userId = 1L;
        long usePoint = 300L;

        given(userPointRepository.findById(userId)).willReturn(userPoint);

        // when
        Throwable t = catchThrowable(() -> sut.usePointById(PointRequestDto.of(userId, usePoint)));

        // then
        assertThat(t)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("포인트를 차감할 수 없습니다. 잔액 %d".formatted(userPoint.point()));
    }

    @Test
    void 동시성_테스트_포인트_차감() throws InterruptedException {
        // given
        long userId = 1L;
        given(userPointRepository.findById(userId)).willReturn(userPoint);

        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        doAnswer(invocation -> {
            long id = invocation.getArgument(0);
            long point = invocation.getArgument(1);
            userPoint = new UserPoint(id, point, System.currentTimeMillis());
            return userPoint;
        }).when(userPointRepository).save(anyLong(), anyLong());

        // when
        for (int i = 0; i < threadCount; i++) {
            int finalI = i;
            executorService.submit(() -> {
                try {
                    sut.usePointById(PointRequestDto.of(userId, finalI));
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();
        executorService.shutdown();

        // then
        assertThat(userPoint).isNotNull();
        assertThat(userPoint.point()).isGreaterThanOrEqualTo(0L);
    }
}