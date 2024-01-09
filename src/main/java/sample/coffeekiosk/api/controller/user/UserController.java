package sample.coffeekiosk.api.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sample.coffeekiosk.api.ApiResponse;
import sample.coffeekiosk.api.controller.user.request.CreateUserDto;
import sample.coffeekiosk.api.service.user.UserService;
import sample.coffeekiosk.api.service.user.response.CreateUserResponse;
import sample.coffeekiosk.config.aop.AutoValidator;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/api/v1/user/create/test")
    public ApiResponse<Object> createUsertest(@RequestBody @Validated CreateUserDto createUserDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors() ) {
            log.error("{}", bindingResult);
            return ApiResponse.bindError("잘못된 요청입니다.", bindingResult);
        }

        return ApiResponse.of(HttpStatus.CREATED, userService.createUser(createUserDto));
    }

    @AutoValidator
    @PostMapping("/api/v1/user/create")
    public ApiResponse<Object> createUser(@RequestBody @Validated CreateUserDto createUserDto, BindingResult bindingResult) {
        CreateUserResponse response = userService.createUser(createUserDto);
        return ApiResponse.of(HttpStatus.CREATED, response);
    }
}
