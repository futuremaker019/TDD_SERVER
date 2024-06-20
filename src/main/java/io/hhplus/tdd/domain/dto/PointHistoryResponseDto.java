package io.hhplus.tdd.domain.dto;

import io.hhplus.tdd.domain.point.PointHistory;
import io.hhplus.tdd.domain.point.TransactionType;

public record PointHistoryResponseDto(long id,
                                      long userId,
                                      long amount,
                                      TransactionType type,
                                      long updateMillis) {

    public static PointHistoryResponseDto of(long id, long userId, long amount, TransactionType type, long updateMillis) {
        return new PointHistoryResponseDto(id, userId, amount, type, updateMillis);
    }

    public static PointHistoryResponseDto from(PointHistory pointHistory) {
        return PointHistoryResponseDto.of(
                pointHistory.id(),
                pointHistory.userId(),
                pointHistory.amount(),
                pointHistory.type(),
                pointHistory.updateMillis()
        );
    }

}
