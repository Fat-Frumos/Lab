package com.epam.esm;

import com.epam.esm.dto.CertificateDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@RequiredArgsConstructor
class CertificateResponse {
    private Embedded _embedded;
    private Map<String, Object> _links;

    @Data
    public static class Embedded {
        private List<CertificateDto> certificateDtoList;
    }
}
