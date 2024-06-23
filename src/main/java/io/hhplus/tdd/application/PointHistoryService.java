package io.hhplus.tdd.application;

import io.hhplus.tdd.domain.dto.PointHistoryRequestDto;
import io.hhplus.tdd.domain.dto.PointHistoryResponseDto;
import io.hhplus.tdd.infra.repository.PointHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PointHistoryService {

    public PointHistoryRepository pointHistoryRepository;

    public PointHistoryService(@Autowired PointHistoryRepository pointHistoryRepository) {
        this.pointHistoryRepository = pointHistoryRepository;
    }

    public void savePointHistory(PointHistoryRequestDto requestDto) {
        pointHistoryRepository.save(requestDto);
    }

    public List<PointHistoryResponseDto> findAllByUserId(long userId) {
        return pointHistoryRepository.findAllByUserId(userId).stream()
                .map(PointHistoryResponseDto::from)
                .toList();
    }
}
