package io.hhplus.tdd.application;

import io.hhplus.tdd.domain.dto.PointHistoryRequestDto;
import io.hhplus.tdd.domain.dto.PointRequestDto;
import io.hhplus.tdd.domain.dto.PointResponseDto;
import io.hhplus.tdd.domain.point.TransactionType;
import io.hhplus.tdd.domain.point.UserPoint;
import io.hhplus.tdd.infra.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointService {

    public final PointRepository pointRepository;
    public final PointHistoryService pointHistoryService;

    public PointResponseDto getPointById(long userId) {
        UserPoint userPoint = pointRepository.findById(userId);
        return PointResponseDto.from(userPoint);
    }

    /**
     * 포인트 충전 메서드
     *      - 특정 userId의 포인트 검색
     *      - 포인트 충전(검색된 기존의 포인트와 충전하고자 하는 포인트를 더함)
     *      - 포인트가 5만보다 큰지 확인 (5만은 충전제한을 두기위한 임의로 정한 수입니다.)
     *      - 포인트 저장
     *      - pointHistory 에 저장
     */
    public synchronized PointResponseDto chargePointById(PointRequestDto requestDto) {
        UserPoint userPoint = pointRepository.findById(requestDto.id());
        long resultPoint = userPoint.point() + requestDto.point();
        if (resultPoint > 50000) {
            throw new RuntimeException("5만점 이상의 포인트를 충전할 수 없습니다. 잔액 %d".formatted(userPoint.point()));
        }
        UserPoint saved = pointRepository.save(requestDto.id(), resultPoint);
        pointHistoryService.savePointHistory(
                PointHistoryRequestDto.of(requestDto.id(), resultPoint, TransactionType.CHARGE)
        );
        return PointResponseDto.from(saved);
    }

    /**
     *  포인트 차감 메서드
     *      - 특정 userId의 포인트 검색
     *      - 포인트 차감 후 포인트가 0L보다 작은지 확인
     *      - 포인트 저장
     *      - pointHistory에 저장
     */
    public synchronized PointResponseDto usePointById(PointRequestDto requestDto) {
        UserPoint userPoint = pointRepository.findById(requestDto.id());
        long resultPoint = userPoint.point() - requestDto.point();
        if (resultPoint < 0) {
            throw new RuntimeException("포인트를 차감할 수 없습니다. 잔액 %d".formatted(userPoint.point()));
        }
        UserPoint saved = pointRepository.save(requestDto.id(), resultPoint);
        pointHistoryService.savePointHistory(
                PointHistoryRequestDto.of(requestDto.id(), resultPoint, TransactionType.USE)
        );
        return PointResponseDto.from(saved);
    }
}
