package io.hhplus.tdd.infra.repository;

import io.hhplus.tdd.domain.point.UserPoint;

public interface PointRepository {

    UserPoint save(long id, long point);

    UserPoint findById(Long id);

}
