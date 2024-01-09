package sample.coffeekiosk.api;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ApiResponse<T> {

    private int code;
    private HttpStatus httpStatus;
    private String message;
    private T data;

    public ApiResponse(HttpStatus httpStatus, String message, T data) {
        this.code = httpStatus.value();
        this.httpStatus = httpStatus;
        this.message = message;
        this.data = data;
    }


    public static <T> ApiResponse<T> of(HttpStatus httpStatus, String message, T data) {
        return new ApiResponse<>(httpStatus, message, data);
    }

    public static <T> ApiResponse<T> of(HttpStatus httpStatus, T data) {
        return ApiResponse.of(httpStatus, httpStatus.name(), data);
    }

    public static <T> ApiResponse<T> of(T data) {
        return ApiResponse.of(HttpStatus.OK, data);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(HttpStatus.BAD_REQUEST, message, null);
    }

    public static <T> ApiResponse<T> bindError(String message, T data) {
        return new ApiResponse<>(HttpStatus.BAD_REQUEST, message, data);
    }




}
