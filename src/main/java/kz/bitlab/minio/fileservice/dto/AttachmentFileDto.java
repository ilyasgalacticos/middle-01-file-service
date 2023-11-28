package kz.bitlab.minio.fileservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class AttachmentFileDto {

    private Long id;
    private String fileName;
    private String originalName;
    private long size;
    private LocalDateTime addedTime;
    private String mimeType;

}
