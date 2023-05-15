package com.epam.esm.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
public class OrderDto implements Linkable {
    private Long id;
    private UserDto user;
    private BigDecimal cost;
    private Timestamp orderDate;
    private List<CertificateDto> certificatesDtos;
}
