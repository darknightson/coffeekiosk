package sample.coffeekiosk.api.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import sample.coffeekiosk.api.controller.user.request.CreateUserDto;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc // 해당 어노테이션은 MockMvc 를 자동으로 등록해준다 어노테이션을 설정하지 않는다면 mockMvc는 new 로 인스턴스를 생성해야 한다.
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @DisplayName("회원가입 성공 테스트를 진행한다.")
    @Test
    void createUserTest() throws Exception {

        // given
        CreateUserDto createUserDto = CreateUserDto.builder()
                .username("anthony.son")
                .password("1234")
                .build();

        // when
        mockMvc.perform(post("/api/v1/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserDto))
                )
                .andDo(print())
                .andExpect(jsonPath("$.data.username").value("anthony.son"))
                .andExpect(jsonPath("$.data.password").value("1234"))
                .andExpect(jsonPath("$.code").value("201"))
        ;
        // then
    }


}