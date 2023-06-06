package com.epam.esm.controller;

import com.epam.esm.assembler.CertificateAssembler;
import com.epam.esm.assembler.OrderAssembler;
import com.epam.esm.assembler.TagAssembler;
import com.epam.esm.assembler.UserAssembler;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @Bean
    public CertificateAssembler certificateAssembler() {
        return new CertificateAssembler();
    }

    @Bean
    public TagAssembler tagAssembler() {
        return new TagAssembler();
    }

    @Bean
    public UserAssembler userAssembler() {
        return new UserAssembler();
    }

    @Bean
    public OrderAssembler orderAssembler() {
        return new OrderAssembler();
    }
}