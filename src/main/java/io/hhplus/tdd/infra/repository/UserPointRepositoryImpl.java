package io.hhplus.tdd.infra.repository;

import io.hhplus.tdd.domain.point.UserPoint;
import io.hhplus.tdd.infra.database.UserPointTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserPointRepositoryImpl implements UserPointRepository {

    private final UserPointTable userPointTable;

    @Override
    public UserPoint save(UserPoint userPoint) {
        System.out.println("userPoint.id() = " + userPoint.id());
        System.out.println("userPoint = " + userPoint.point());
        return userPointTable.insertOrUpdate(userPoint.id(), userPoint.point());
    }

    @Override
    public UserPoint findById(Long id) {
        return userPointTable.selectById(id);
    }
}
