package io.hhplus.tdd.domain.point;

public record PointHistory(
        long id,
        long userId,
        long amount,
        TransactionType type,
        long updateMillis
) {

    public static PointHistory of(long id, long userId, long amount, TransactionType type, long updateMillis) {
        return new PointHistory(id, userId, amount, type, updateMillis);
    }

    public static PointHistory of(long userId, long amount, TransactionType type, long updateMillis) {
        return new PointHistory(0, userId, amount, type, updateMillis);
    }

}
