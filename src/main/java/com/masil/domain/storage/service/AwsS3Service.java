package com.masil.domain.storage.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.masil.domain.storage.dto.FileInfoDto;
import com.masil.domain.storage.exception.FileNotFoundException;
import com.masil.domain.storage.exception.InvalidImageExtensionException;
import com.masil.domain.storage.exception.InvalidImageFileException;
import com.masil.global.error.exception.BusinessException;
import com.masil.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AwsS3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cdn.domain}")
    private String cdnDomain;

    private final AmazonS3 amazonS3;

    /**
     * AWS S3에 이미지 파일 업로드
     * @param multipartFile : 파일
     * @param dirName : 폴더 이름
     * @return : FileInfoDto (path, cdn URL)
     */
    public FileInfoDto upload(MultipartFile multipartFile, String dirName){

        // 이미지 파일 유효성 체크 후 bufferedImage 반환
        BufferedImage bufferedImage = getValidBufferedImage(multipartFile);

        String fileName = createFileName(multipartFile.getOriginalFilename(), dirName);

        // 메타데이터 지정, TODO : 필요여부 체크
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());

        // s3에 이미지 저장
        try(InputStream inputStream = multipartFile.getInputStream()){
            amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata));
        } catch (IOException e){
            // TODO : 추주 예외 변경
            throw new BusinessException("파일 업로드에 실패하였습니다.", ErrorCode.SERVER_ERROR);
        }

        // s3에 저장된 파일 path 얻어옴.
        String path = amazonS3.getUrl(bucket, fileName).getPath();

        return FileInfoDto.of(path, createUrlName(path), bufferedImage.getWidth(), bufferedImage.getHeight());
    }

    /**
     * 이미지 유효성 체크 후 BufferedImage 반환
     */
    public BufferedImage getValidBufferedImage(MultipartFile multipartFile) {
        try {
            // 업로드된 파일의 BufferedImage 를 가져온다.
            BufferedImage read = ImageIO.read(multipartFile.getInputStream());

            // 업로드된 파일이 이미지가 아닐 경우를 체크한다.
            if (read == null) {
                throw new InvalidImageFileException();
            }

            // 파일 확장자 체크 [jpeg, jpg, png, gif]
            if (!isValidImageExtension(multipartFile.getContentType())) {
                throw new InvalidImageExtensionException();
            }

            return read;
        } catch (IOException e) {
            throw new BusinessException("파일 업로드에 실패하였습니다.", ErrorCode.SERVER_ERROR);
        } catch (NullPointerException e) {
            throw new FileNotFoundException();
        }
    }

    /**
     * cdn 호스트 붙인 url 반환
     * ex) https://dmpoeindaa9de.cloudfront.net + /post-image/filename.jpg
     */
    private String createUrlName(String path) {
        return cdnDomain + path;
    }

    private String createFileName(String fileName, String dirName){
        // 파일명을 다르게 하기 위해 UUID 를 붙임
        return dirName + "/" + UUID.randomUUID() + fileName;
    }

    private boolean isValidImageExtension(String contentType) {
        // 유효한 이미지 확장자.
        return contentType.matches("^image/(jpeg|jpg|png|gif)$");
    }

}