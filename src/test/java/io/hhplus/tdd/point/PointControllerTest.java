package io.hhplus.tdd.point;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.hhplus.tdd.application.UserPointService;
import io.hhplus.tdd.domain.point.UserPoint;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 *  case 1
 *      1.1. 유저의 id(key)를 이용하여 유저의 포인트를 단건조회한다.
 *      1.2. 유저의 id(key)를 이용하여 포인트를 조회하지만 포인트가 없어 조회되지 않는다.
 *
 */
@DisplayName("포인트 컨트롤러 테스트")
@SpringBootTest
@AutoConfigureMockMvc   // service, controller 를 모두 메모리에 올려 테스트할 때 사용한다.
class PointControllerTest {

    private static final Logger log = LoggerFactory.getLogger(PointControllerTest.class);

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private UserPointService userPointService;

    @Test
    void givenId_whenRequestingUserPoint_thenReturnUserPoint() throws Exception {
        // given
        long userId = 1L;

        // when
        mockMvc.perform(get("/point/%d".formatted(userId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().bytes(
                        objectMapper.writeValueAsBytes(new UserPoint(0, 0, 0)))
                );
    }

    @Test
    void givenNotExistedId_whenRequestingUserPoint_thenThrowsException() throws Exception {
        // given
        long id = 1L;

        // when
        doThrow(new Exception()).when(userPointService).getUserPoint(id);
        mockMvc.perform(get("/point/%d".formatted(id)))
                .andDo(print())
                .andExpect(status().is5xxServerError());

    }

    @Test
    void givenUserId_whenCallingPointHistory_thenReturnListPointHistory() {
        // given


        // when

        // then
    }



}