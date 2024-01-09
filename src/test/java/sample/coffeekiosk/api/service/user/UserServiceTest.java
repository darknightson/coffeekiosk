package sample.coffeekiosk.api.service.user;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import sample.coffeekiosk.api.controller.user.request.CreateUserDto;
import sample.coffeekiosk.api.service.user.response.CreateUserResponse;
import sample.coffeekiosk.domain.user.User;
import sample.coffeekiosk.domain.user.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

// Spring 관련 Bean이 하나도 없는 상태
// 이렇게 하면 테스트가 가벼워 진다.
// 이유는 SpringBootTest를 사용하면 모든 빈을 등록하고 시작하게 되는데 이렇게 되면 클래스가 늘어날 수록 테스트가 무거워진다
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    // 주입 대상 객체를 생성자를 통해 주입받는다.
    @InjectMocks
    private UserService userService;

    // 가짜 객채를 만든다
    // 가짜 객체이기 때문에 테스트를 할때 stub 만들어서 가짜 요청과 결과값을 만들어야 한다.
    @Mock
    private UserRepository userRepository;

    // spy는 가짜 객체를 만들지 않고 실제 객체를 만들어서 사용한다.
//    @Spy
//    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @DisplayName("")
    @Test
    void Test() {

        // given
        CreateUserDto createUserDto = CreateUserDto.builder()
                .username("anthony.son")
                .password("1234")
                .build();

        // when
        // stub1 userRepository.findByUsername(createUserDto.getUsername())
        // 첫번째 수행 시점에는 Optional.empty()를 리턴하고
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
        // stub2 userRepository.save(createUserDto.toEntity())
        // 두번째 수행 시점에는 실제 저장한 객체를 리턴한다.
        User user = User.builder()
                .username("anthony.son")
                .password("1234")
                .build();

        when(userRepository.save(any())).thenReturn(createUserDto.toEntity());

        // 이제 모든 스텁이 완료 되었으니 테스트를 진행한다.
        // 리턴 객체는 알아서 만들어라 그리고 머 결과값으로 쓰던지.
        CreateUserResponse response = userService.createUser(createUserDto);

        // then
        assertThat(response.getUsername()).isEqualTo(createUserDto.getUsername());
    }

}