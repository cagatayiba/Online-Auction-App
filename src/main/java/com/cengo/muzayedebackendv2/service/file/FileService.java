package com.cengo.muzayedebackendv2.service.file;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.cengo.muzayedebackendv2.exception.FileNotUploadedException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FileService {
    private final AmazonS3 awsS3Client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    public void uploadFile(MultipartFile file, String fileName) {
        try {
            var fis = file.getInputStream();
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(file.getContentType());
            objectMetadata.setContentLength(file.getSize());

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, fis, objectMetadata);

            awsS3Client.putObject(putObjectRequest);
        }catch (IOException e){
            throw new FileNotUploadedException();
        }

    }

    public void deleteFile(String fileName) {
        awsS3Client.deleteObject(bucketName, fileName);
    }
}
