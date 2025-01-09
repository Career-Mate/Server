package UMC.career_mate.domain;

import UMC.career_mate.global.response.exception.GeneralException;
import UMC.career_mate.global.response.exception.code.ExampleErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExampleErrorService {

    public String exampleErrorCode() {
        throw new GeneralException(ExampleErrorCode.EXAMPLE_ERROR_CODE);
    }
}

/*
예시용이므로 도메인마다 패키지 만들어지면 삭제하겠습니다.
 */