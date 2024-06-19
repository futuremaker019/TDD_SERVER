package io.hhplus.tdd.application;

import io.hhplus.tdd.domain.point.UserPoint;
import io.hhplus.tdd.infra.repository.UserPointRepository;
import io.hhplus.tdd.infra.repository.UserPointRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("유저 포인트 서비스")
@ExtendWith(MockitoExtension.class)
class UserPointServiceTest {

    @InjectMocks
    private UserPointService sut;

    @Mock
    private UserPointRepository userPointRepository;


    @Test
    void givenId_when_then() {
        // given
        long userId = 1L;
        when(userPointRepository.findById(userId)).thenReturn(UserPoint.of(userId));

        // when
        UserPoint userPoint = userPointRepository.findById(userId);

        // then
        assertThat(userPoint).isNotNull();
        assertThat(userPoint.id()).isEqualTo(1L);
    }

    @Test
    void givenId_whenChargePoint_thenReturnPoint() {
        // given
        long userId = 1L;
        long point = 100L;

        UserPoint currentPoint = UserPoint.of(userId);
        given(userPointRepository.findById(userId)).willReturn(currentPoint);

        UserPoint plusUserPoint = UserPoint.of(userId, currentPoint.point() + point);
//        given(userPointRepository.save(any(UserPoint.class))).willReturn(plusUserPoint);
        given(userPointRepository.save(plusUserPoint)).willReturn(plusUserPoint);

        // when
        UserPoint userPoint = sut.chargeUserPoint(userId, point);

        // then
        assertThat(userPoint.id()).isEqualTo(userId);
        assertThat(userPoint.point()).isEqualTo(point);

        then(userPointRepository).should().findById(userId);
//        then(userPointRepository).should().save(any(UserPoint.class));
        then(userPointRepository).should().save(plusUserPoint);
    }

    @Test
    void givenIdAndPoint_whenUsePoint_thenReturnPoint() {
        // given
        long userId = 1L;
        long chargedPoint = 80L;
        long usedPoint = 50L;

        UserPoint currentPoint = UserPoint.of(userId);
        given(userPointRepository.findById(userId)).willReturn(currentPoint);

        UserPoint chargedUserPoint = sut.chargeUserPoint(userId, chargedPoint);

        // when
        UserPoint returnedUserPoint = sut.useUserPoint(userId, chargedUserPoint.point() - usedPoint);

        // then
        assertThat(returnedUserPoint.id()).isEqualTo(userId);
        assertThat(returnedUserPoint.point()).isEqualTo(chargedPoint - usedPoint);

        then(userPointRepository).should().save(any(UserPoint.class));
    }

}