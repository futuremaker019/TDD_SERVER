package io.hhplus.tdd.application;

import io.hhplus.tdd.domain.point.TransactionType;
import io.hhplus.tdd.domain.point.UserPoint;
import io.hhplus.tdd.infra.repository.UserPointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPointService {

    private final UserPointRepository userPointRepository;

    public UserPoint getUserPoint(Long id) {
        return userPointRepository.findById(id);
    }

    public UserPoint chargeUserPoint(long id, long point) {
        return saveUserPointTransactionType(id, point, TransactionType.CHARGE);
    }

    public UserPoint useUserPoint(long id, long point) {
        return saveUserPointTransactionType(id, point, TransactionType.USE);
    }

    public UserPoint saveUserPointTransactionType(long id, long point, TransactionType transactionType) {
        long savePoint = 0L;
        UserPoint userPoint = userPointRepository.findById(id);;

        if (transactionType == TransactionType.CHARGE) {
            savePoint = plusPoint(userPoint.point(), point);
        } else if (transactionType == TransactionType.USE) {
            savePoint = subtractPoint(userPoint.point(), point);
        }
        
        return userPointRepository.save(UserPoint.of(id, savePoint));
    }

    public long plusPoint(long currentPoint, long usedPoint) {
        return currentPoint + usedPoint;
    }

    public long subtractPoint(long currentPoint, long usedPoint) {
        long result = currentPoint - usedPoint;
        if (result < 0) {
            throw new RuntimeException("포인트를 차감할 수 없습니다.");
        }
        return result;
    }

}
