package io.hhplus.tdd.infra.repository;

import io.hhplus.tdd.domain.point.UserPoint;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PointRepositoryImplTest {

    @Autowired
    private PointRepository pointRepository;

    @Test
    void test() {
        // given


        // when
        UserPoint userPoint = pointRepository.findById(1L);

        // then
        assertThat(userPoint).isNotNull();
        assertThat(userPoint.id()).isEqualTo(1L);
    }

    @Test
    public void test2() {
        // given
        long userId = 1L;
        long point = 2000L;

        // when
        UserPoint saved = pointRepository.save(userId, point);

        // then
        assertThat(saved.id()).isEqualTo(userId);
        assertThat(saved.point()).isEqualTo(point);
    }

}