package sample.coffeekiosk.api.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sample.coffeekiosk.api.controller.user.request.CreateUserDto;
import sample.coffeekiosk.api.service.user.response.CreateUserResponse;
import sample.coffeekiosk.domain.user.User;
import sample.coffeekiosk.domain.user.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public CreateUserResponse createUser(CreateUserDto createUserDto) {

        userRepository.findByUsername(createUserDto.getUsername())
                .ifPresent(user -> {
                    throw new IllegalArgumentException("이미 존재하는 유저입니다.");
                });

        User save = userRepository.save(createUserDto.toEntity());
        return CreateUserResponse.of(save.getId(), save.getUsername(), save.getPassword());
    }
}
