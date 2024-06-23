package io.hhplus.tdd.infra.repository.impl;

import io.hhplus.tdd.domain.dto.PointHistoryRequestDto;
import io.hhplus.tdd.domain.point.PointHistory;
import io.hhplus.tdd.infra.database.PointHistoryTable;
import io.hhplus.tdd.infra.repository.PointHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PointHistoryRepositoryImpl implements PointHistoryRepository {

    private final PointHistoryTable pointHistoryTable;

    @Override
    public PointHistory save(PointHistoryRequestDto requestDto) {
        return pointHistoryTable.insert(
                requestDto.userId(),
                requestDto.amount(),
                requestDto.type(),
                System.currentTimeMillis()
        );
    }

    @Override
    public List<PointHistory> findAllByUserId(long userId) {
        return pointHistoryTable.selectAllByUserId(userId);
    }
}
