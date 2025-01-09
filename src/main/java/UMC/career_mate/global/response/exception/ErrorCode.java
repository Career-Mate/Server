package UMC.career_mate.global.response.exception;

public interface ErrorCode {
    int getStatus();
    String getCode();
    String getMessage();
}
