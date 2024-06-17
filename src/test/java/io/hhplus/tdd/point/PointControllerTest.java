package io.hhplus.tdd.point;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("포인트 컨트롤러 테스트")
@SpringBootTest
@AutoConfigureMockMvc   // service, controller 를 모두 메모리에 올려 테스트할 때 사용한다.
class PointControllerTest {

    private static final Logger log = LoggerFactory.getLogger(PointControllerTest.class);

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;


    @Test
    void givenUserId_whenCallingUserPoint_thenReturnUserPoint() throws Exception {
        // given
        long userId = 1L;

        // when
        mockMvc.perform(get("/point/%d".formatted(userId)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(
                        content().bytes(objectMapper.writeValueAsBytes(new UserPoint(0, 0, 0)))
                );

        // then
    }

    @Test
    void givenUserId_whenCallingPointHistory_thenReturnListPointHistory() {
        //given

        // when

        // then
    }



}