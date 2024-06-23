package io.hhplus.tdd.dummy;

import io.hhplus.tdd.domain.point.PointHistory;
import io.hhplus.tdd.domain.point.TransactionType;
import io.hhplus.tdd.domain.point.UserPoint;

import java.util.List;

public class Dummy {

    public static List<PointHistory> createPointHistory() {
        return List.of(
                PointHistory.of(1L, 1L,  100, TransactionType.CHARGE, System.currentTimeMillis()),
                PointHistory.of(2L, 1L,  100, TransactionType.CHARGE, System.currentTimeMillis()),
                PointHistory.of(3L, 1L,  50, TransactionType.USE, System.currentTimeMillis()),
                PointHistory.of(4L, 2L,  100, TransactionType.CHARGE, System.currentTimeMillis())
        );
    }

    public static UserPoint createUserPoint() {
        return UserPoint.of(1L, 150L, 1L);
    }

}
