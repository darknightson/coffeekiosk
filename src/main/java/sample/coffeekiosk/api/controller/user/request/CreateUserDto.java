package sample.coffeekiosk.api.controller.user.request;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sample.coffeekiosk.domain.user.User;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class CreateUserDto {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @Builder
    public CreateUserDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User toEntity() {
        return User.builder()
                .username(username)
                .password(password)
                .build();
    }
}
