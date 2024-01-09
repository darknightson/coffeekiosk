package sample.coffeekiosk.api.service.user.response;

import lombok.Builder;
import lombok.Data;

@Data
public class CreateUserResponse {

    private Long id;
    private String username;
    private String password;

    public static CreateUserResponse of(Long id, String username, String password) {
        CreateUserResponse createUserResponse = new CreateUserResponse();
        createUserResponse.id = id;
        createUserResponse.username = username;
        createUserResponse.password = password;
        return createUserResponse;
    }
}
