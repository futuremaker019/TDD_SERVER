package io.hhplus.tdd.domain.dto;

import io.hhplus.tdd.domain.point.UserPoint;

public record PointResponseDto(long id, long point, long updateMillis) {

    public static PointResponseDto of(long id, long point, long updateMillis) {
        return new PointResponseDto(id, point, updateMillis);
    }

    public static PointResponseDto of(long id, long point) {
        return new PointResponseDto(id, point, 0);
    }

    public static PointResponseDto of(long id) {
        return new PointResponseDto(id, 0, 0);
    }

    public static PointResponseDto from(UserPoint userPoint) {
        return PointResponseDto.of(
                userPoint.id(), userPoint.point(), userPoint.updateMillis()
        );
    }
}
