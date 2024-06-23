package io.hhplus.tdd.domain.dto;

import io.hhplus.tdd.domain.point.PointHistory;
import io.hhplus.tdd.domain.point.TransactionType;

public record PointHistoryRequestDto(long userId,
                                     long amount,
                                     TransactionType type,
                                     long updateMillis) {

    public static PointHistoryRequestDto of(long userId, long amount, TransactionType type, long updateMillis) {
        return new PointHistoryRequestDto(userId, amount, type, updateMillis);
    }

    public static PointHistoryRequestDto of(long userId, long amount, TransactionType type) {
        return new PointHistoryRequestDto(userId, amount, type, 0);
    }

    // 포인트 히스트로 등록용
    public PointHistory to(PointHistory pointHistory) {
        return PointHistory.of(
                pointHistory.userId(),
                pointHistory.amount(),
                pointHistory.type(),
                pointHistory.updateMillis()
        );
    }
}
