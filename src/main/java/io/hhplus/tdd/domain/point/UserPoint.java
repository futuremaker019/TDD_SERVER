package io.hhplus.tdd.domain.point;

public record UserPoint(
        long id,
        long point,
        long updateMillis
) {

    public static UserPoint empty(long id) {
        return new UserPoint(id, 0, System.currentTimeMillis());
    }

    public static UserPoint of(long id, long point, long updateMillis) {
        return new UserPoint(id, point, updateMillis);
    }

    public static UserPoint of(long id, long point) {
        return new UserPoint(id, point, 0L);
    }

    public static UserPoint of(long id) {
        return new UserPoint(id, 0L, 0L);
    }
}
