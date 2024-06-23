package io.hhplus.tdd.infra.repository.impl;

import io.hhplus.tdd.domain.point.UserPoint;
import io.hhplus.tdd.infra.database.UserPointTable;
import io.hhplus.tdd.infra.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PointRepositoryImpl implements PointRepository {

    private final UserPointTable userPointTable;

    @Override
    public UserPoint save(long id, long point) {
        return userPointTable.insertOrUpdate(id, point);
    }

    @Override
    public UserPoint findById(Long id) {
        return userPointTable.selectById(id);
    }
}
