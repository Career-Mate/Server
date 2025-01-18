package UMC.career_mate.global.response;

import UMC.career_mate.global.response.exception.ErrorCode;
import UMC.career_mate.global.response.result.ResultCode;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiResponse<T> {
    private final int status;
    private final String code;
    private final String message;
    private final T data;

    public static final ApiResponse<Void> OK =
            new ApiResponse<>(200, "SC000", "성공입니다.", null);

    public static <T> ApiResponse<T> onSuccess(ResultCode resultCode, T data) {
        return ApiResponse.<T>builder()
                .status(resultCode.getStatus())
                .code(resultCode.getCode())
                .message(resultCode.getMessage())
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> onSuccess(ResultCode resultCode) {
        return ApiResponse.<T>builder()
                .status(resultCode.getStatus())
                .code(resultCode.getCode())
                .message(resultCode.getMessage())
                .data(null)
                .build();
    }

    public static <T> ApiResponse<T> onSuccess(T data) {
        return ApiResponse.<T>builder()
            .status(OK.getStatus())
            .code(OK.getCode())
            .message(OK.getMessage())
            .data(null)
            .build();
    }

    public static <T> ApiResponse<T> onFailure(ErrorCode errorCode, T data) {
        return ApiResponse.<T>builder()
                .status(errorCode.getStatus())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> onFailure(ErrorCode errorCode) {
        return ApiResponse.<T>builder()
                .status(errorCode.getStatus())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .data(null)
                .build();
    }
}
