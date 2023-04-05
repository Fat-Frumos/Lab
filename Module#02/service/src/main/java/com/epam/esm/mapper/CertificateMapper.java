package com.epam.esm.mapper;

import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Tag;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.CertificateWithoutTagDto;
import com.epam.esm.dto.TagDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Slf4j
@Component
public final class CertificateMapper implements EntityMapper<Certificate, CertificateDto> {
    public static final CertificateMapper mapper = new CertificateMapper();
    public static final TagMapper tagMapper = TagMapper.getInstance();

    public static CertificateMapper getInstance() {
        return mapper;
    }

    private CertificateMapper() {
    }

    @Override
    public CertificateDto toDto(final Certificate certificate) {
        try {
            Set<Tag> tags = certificate.getTags();
            Set<TagDto> tagsDto = new HashSet<>();
            if (tags != null) {
                tagsDto = tags.stream()
                        .map(tagMapper::toDto)
                        .collect(toSet());
            }
            return CertificateDto.builder()
                    .id(certificate.getId())
                    .name(certificate.getName())
                    .description(certificate.getDescription())
                    .price(certificate.getPrice())
                    .createDate(certificate.getCreateDate())
                    .lastUpdateDate(Instant.now())
                    .duration(certificate.getDuration())
                    .tags(tagsDto)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Certificate must not be null");
        }
    }

    @Override
    public Certificate toEntity(final CertificateDto certificateDto) {
        try {
            Set<TagDto> tagsDto = certificateDto.getTags();
            Set<Tag> tags = new HashSet<>();
            if (tagsDto != null) {
                tags = tagsDto.stream()
                        .map(tagMapper::toEntity)
                        .collect(toSet());
            }
            return Certificate.builder()
                    .id(certificateDto.getId())
                    .name(certificateDto.getName())
                    .description(certificateDto.getDescription())
                    .price(certificateDto.getPrice())
                    .createDate(certificateDto.getCreateDate())
                    .lastUpdateDate(certificateDto.getLastUpdateDate())
                    .duration(certificateDto.getDuration())
                    .tags(tags)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Certificate must not be null");
        }
    }

    public CertificateWithoutTagDto toDtoWithoutTags(Certificate certificate) {
        return CertificateWithoutTagDto.builder()
                .id(certificate.getId())
                .name(certificate.getName())
                .description(certificate.getDescription())
                .price(certificate.getPrice())
                .createDate(certificate.getCreateDate())
                .lastUpdateDate(Instant.now())
                .duration(certificate.getDuration())
                .build();
    }
}
