package io.hhplus.tdd.application;

import io.hhplus.tdd.domain.dto.PointRequestDto;
import io.hhplus.tdd.domain.dto.PointResponseDto;
import io.hhplus.tdd.domain.point.UserPoint;
import io.hhplus.tdd.infra.repository.PointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PointService {

    public PointRepository pointRepository;

    public PointService(@Autowired PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    public PointResponseDto getPointById(long userId) {
        UserPoint userPoint = pointRepository.findById(userId);
        return PointResponseDto.from(userPoint);
    }

    public PointResponseDto chargePointById(PointRequestDto requestDto) {
        UserPoint userPoint = pointRepository.findById(requestDto.id());
        long resultPoint = userPoint.point() + requestDto.point();
        UserPoint saved = pointRepository.save(requestDto.id(), resultPoint);
        return PointResponseDto.from(saved);
    }

    public PointResponseDto usePointById(PointRequestDto requestDto) {
        UserPoint userPoint = pointRepository.findById(requestDto.id());
        long resultPoint = userPoint.point() - requestDto.point();
        if (resultPoint < 0) {
            throw new RuntimeException("포인트를 차감할 수 없습니다. 잔액 %d".formatted(userPoint.point()));
        }
        UserPoint saved = pointRepository.save(requestDto.id(), userPoint.point());
        return PointResponseDto.from(saved);
    }
}
