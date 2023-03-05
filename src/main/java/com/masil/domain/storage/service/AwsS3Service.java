package com.masil.domain.storage.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.masil.domain.storage.dto.FileInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

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
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");
        }

        // s3에 저장된 파일 path 얻어옴.
        String path = amazonS3.getUrl(bucket, fileName).getPath();

        return FileInfoDto.of(path, createUrlName(path));
    }

    /**
     * cdn 호스트 붙인 url 반환
     * ex) https://dmpoeindaa9de.cloudfront.net + /post-image/filename.jpg
     */
    public String createUrlName(String path) {
        return cdnDomain + path;
    }

    // 먼저 파일 업로드시, 파일명을 난수화하기 위해 UUID 를 활용하여 난수를 돌린다.
    public String createFileName(String fileName, String dirName){
        return dirName + "/" + UUID.randomUUID() + fileName;
    }

    // file 형식이 잘못된 경우를 확인하기 위해 만들어진 로직이며, 파일 타입과 상관없이 업로드할 수 있게 하기위해, "."의 존재 유무만 판단하였습니다.
//    private String getFileExtension(String fileName){
//        try{
//            return fileName.substring(fileName.lastIndexOf("."));
//        } catch (StringIndexOutOfBoundsException e){
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 형식의 파일" + fileName + ") 입니다.");
//        }
//    }

}