package io.hhplus.tdd.infra.repository;

import io.hhplus.tdd.domain.dto.PointHistoryRequestDto;
import io.hhplus.tdd.domain.point.PointHistory;

import java.util.List;

public interface PointHistoryRepository {

    PointHistory save(PointHistoryRequestDto requestDto);

    List<PointHistory> findAllByUserId(long userId);

}
