package kz.bitlab.minio.fileservice.service;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import kz.bitlab.minio.fileservice.dto.AttachmentFileDto;
import kz.bitlab.minio.fileservice.mapper.AttachmentFileMapper;
import kz.bitlab.minio.fileservice.model.AttachmentFile;
import kz.bitlab.minio.fileservice.repository.AttachmentFileRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService {

    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucket;

    private final AttachmentFileRepository attachmentFileRepository;

    private final AttachmentFileMapper attachmentFileMapper;

    public String uploadFile(MultipartFile file){

        AttachmentFile attachmentFile = new AttachmentFile();
        attachmentFile.setSize(file.getSize());
        attachmentFile.setOriginalName(file.getOriginalFilename());
        attachmentFile.setMimeType(file.getContentType());

        attachmentFile = attachmentFileRepository.save(attachmentFile);
        String fileName = convertToSha1("Ilyas_"+attachmentFile.getId()+"_");

        try{

            minioClient.putObject(
                    PutObjectArgs
                            .builder()
                            .bucket(bucket)
                            .object(fileName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            attachmentFile.setFileName(fileName);
            attachmentFileRepository.save(attachmentFile);

            return attachmentFile.getFileName();

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public ByteArrayResource downloadFile(String fileName){

        AttachmentFile attachmentFile = attachmentFileRepository.findByFileName(fileName);

        if(attachmentFile!=null) {
            try {
                GetObjectArgs getObjectArgs = GetObjectArgs
                        .builder()
                        .bucket(bucket)
                        .object(fileName)
                        .build();

                InputStream stream = minioClient.getObject(getObjectArgs);
                byte[] byteArray = IOUtils.toByteArray(stream);
                stream.close();

                return new ByteArrayResource(byteArray);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private String convertToSha1(String plainText){
        return DigestUtils.sha1Hex(plainText);
    }

    public List<AttachmentFileDto> getAttachments(){
        return attachmentFileMapper.toDtoList(attachmentFileRepository.findAll());
    }

    public AttachmentFileDto getAttachment(Long id){
        return attachmentFileMapper.toDto(attachmentFileRepository.findById(id).orElse(null));
    }

    public AttachmentFileDto getAttachmentByFileName(String fileName){
        return attachmentFileMapper.toDto(attachmentFileRepository.findByFileName(fileName));
    }

}
