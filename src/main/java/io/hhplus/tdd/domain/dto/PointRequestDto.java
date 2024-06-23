package io.hhplus.tdd.domain.dto;

import io.hhplus.tdd.domain.point.UserPoint;

public record PointRequestDto(long id, long point, long updateMillis) {

    public static PointRequestDto of(long id, long point, long updateMillis) {
        return new PointRequestDto(id, point, updateMillis);
    }

    public static PointRequestDto of(long id, long point) {
        return new PointRequestDto(id, point, 0);
    }

    public UserPoint to(UserPoint userPoint) {
        return UserPoint.of(
                userPoint.id(),
                userPoint.point(),
                userPoint.updateMillis()
        );
    }
}