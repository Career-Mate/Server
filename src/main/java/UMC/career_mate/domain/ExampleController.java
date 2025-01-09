package UMC.career_mate.domain;

import UMC.career_mate.global.response.ApiResponse;
import UMC.career_mate.global.response.result.code.ExampleResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExampleController {

    @GetMapping("/example")
    public ApiResponse<String> exampleController() {
        String data = "api 반환형식 예시 입니다.";
        return ApiResponse.onSuccess(ExampleResultCode.EXAMPLE_RESULT_CODE, data);
    }
}

/*
예시용이므로 도메인마다 패키지 만들어지면 삭제하겠습니다.
 */
