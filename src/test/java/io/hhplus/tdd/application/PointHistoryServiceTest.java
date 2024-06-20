package io.hhplus.tdd.application;

import io.hhplus.tdd.domain.dto.PointHistoryRequestDto;
import io.hhplus.tdd.domain.dto.PointHistoryResponseDto;
import io.hhplus.tdd.domain.point.PointHistory;
import io.hhplus.tdd.domain.point.TransactionType;
import io.hhplus.tdd.infra.repository.PointHistoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class PointHistoryServiceTest {

    @InjectMocks PointHistoryService sut;
    @Mock PointHistoryRepository pointHistoryRepository;

    @DisplayName("충전된_포인트의_히스토리를_저장한다")
    @Test
    public void 사용되거나_충전된_포인트의_히스토리를_저장한다() {
        // given
        long userId = 1L;
        long chargePoint = 100L;

        PointHistoryRequestDto requestDto = PointHistoryRequestDto.of(userId, chargePoint, TransactionType.CHARGE);
        given(pointHistoryRepository.save(requestDto)).willReturn(any(PointHistory.class));

        // when
        sut.savePointHistory(requestDto);

        // then
        then(pointHistoryRepository).should().save(requestDto);
    }

    @DisplayName("포인트_히스토리의_리스트를_조회한다")
    @Test
    public void givenUserId_whenSearchingHistory_thenReturnsHistoryList() {
        // given
        long userId = 1L;
        List<PointHistory> pointHistories = List.of(
                PointHistory.of(1L, userId, 200L, TransactionType.CHARGE, System.currentTimeMillis()),
                PointHistory.of(2L, userId, 50L, TransactionType.USE, System.currentTimeMillis())
        );
        given(pointHistoryRepository.findAllByUserId(userId)).willReturn(pointHistories);

        // when
        List<PointHistoryResponseDto> pointHistoryList = sut.findAllByUserId(userId);

        // then
        assertThat(pointHistoryList).hasSize(2);
        assertThat(pointHistoryList)
                .extracting("userId", "amount", "type")
                .containsExactlyInAnyOrder(
                    tuple(1L, 200L, TransactionType.CHARGE),
                    tuple(1L, 50L, TransactionType.USE)
                );
        then(pointHistoryRepository).should().findAllByUserId(userId);
    }

}