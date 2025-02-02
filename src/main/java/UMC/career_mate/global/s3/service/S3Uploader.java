package UMC.career_mate.global.s3.service;

import UMC.career_mate.global.response.exception.GeneralException;
import UMC.career_mate.global.response.exception.code.CommonErrorCode;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S3Uploader {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadImage(MultipartFile file) throws IOException {
        // S3에 저장할 고유한 파일 이름 생성
        String fileName = createFileName(file.getOriginalFilename());

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType("multipart/formed-data");

        System.out.println(file.getContentType());

        // 파일 업로드
        amazonS3.putObject(bucket, fileName, file.getInputStream(), metadata);
        return amazonS3.getUrl(bucket, fileName).toString();
    }

    //파일 이름 생성 로직
    private String createFileName(String originalFileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(originalFileName));
    }

    //파일의 확장자명을 가져오는 로직
    private String getFileExtension(String fileName){
        try{
            return fileName.substring(fileName.lastIndexOf("."));
        }catch(StringIndexOutOfBoundsException e) {
            throw new IllegalArgumentException(String.format("잘못된 형식의 파일 (%s) 입니다.",fileName));
        }
    }
}
