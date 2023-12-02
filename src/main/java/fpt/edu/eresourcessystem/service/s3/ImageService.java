package fpt.edu.eresourcessystem.service.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
public class ImageService {

    @Value("${application.bucket.name}")
    private String bucketName;

    private final AmazonS3 s3Client;

    private String localLocation = System.getProperty("user.dir");

    public ImageService(AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadImage(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String ext = fileName.substring(fileName.indexOf("."));
        String uuidFileName = "ckeditor_image" + UUID.randomUUID() + ext;
        String localPath = localLocation + "/uploads/" + uuidFileName;
        File localFile = new File(localPath);
        file.transferTo(localFile);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, localFile);
        s3Client.putObject(putObjectRequest);
        String s3Url = s3Client.getUrl(bucketName, fileName).toString();
        localFile.delete();
        return s3Url;
    }


    public byte[] downloadFile(String fileName) {
        S3Object s3Object = s3Client.getObject(bucketName, fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            byte[] content = IOUtils.toByteArray(inputStream);
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public String deleteFile(String fileName) {
        s3Client.deleteObject(bucketName, fileName);
        return fileName + " removed ...";
    }


    private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            log.error("Error converting multipartFile to file", e);
        }
        return convertedFile;
    }
}