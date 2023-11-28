package kz.bitlab.minio.fileservice.mapper;

import kz.bitlab.minio.fileservice.dto.AttachmentFileDto;
import kz.bitlab.minio.fileservice.model.AttachmentFile;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AttachmentFileMapper {

    AttachmentFileDto toDto(AttachmentFile file);
    AttachmentFile toEntity(AttachmentFileDto dto);
    List<AttachmentFileDto> toDtoList(List<AttachmentFile> fileList);

}
