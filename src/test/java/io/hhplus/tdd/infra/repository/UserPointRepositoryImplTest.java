package io.hhplus.tdd.infra.repository;

import io.hhplus.tdd.domain.point.UserPoint;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserPointRepositoryImplTest {

    @Autowired
    private UserPointRepository userPointRepository;

    @Test
    void test() {
        // given


        // when
        UserPoint userPoint = userPointRepository.findById(1L);

        // then
        assertThat(userPoint).isNotNull();
        assertThat(userPoint.id()).isEqualTo(2L);
    }

}