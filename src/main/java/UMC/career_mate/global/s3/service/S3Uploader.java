package UMC.career_mate.global.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
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
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        // 파일 업로드
        amazonS3.putObject(bucket, fileName, file.getInputStream(), metadata);
        return amazonS3.getUrl(bucket, fileName).toString();
    }
}
