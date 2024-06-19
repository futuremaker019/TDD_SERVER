package io.hhplus.tdd.infra.repository;

import io.hhplus.tdd.domain.point.UserPoint;

import java.util.Optional;

public interface UserPointRepository {

    UserPoint save(UserPoint userPoint);

    UserPoint findById(Long id);

}
