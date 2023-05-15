package com.epam.esm.controller;

import com.epam.esm.assembler.UserAssembler;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "users")
public class UserController {

    private final UserAssembler assembler;

    private final UserService userService;
    private final CertificateService certificateService;

    @GetMapping("/{id}")
    public EntityModel<UserDto> getUser(
            @PathVariable final Long id) {
        return assembler.toModel(
                userService.getById(id));
    }

    @GetMapping()
    public List<UserDto> getUsers() { // TODO criteria
        return userService.getAll();
    }

    @GetMapping(value = "/{id}/certificates", produces = APPLICATION_JSON_VALUE)
    public List<CertificateDto> getUserCertificates(
            @PathVariable final Long id) {
        return certificateService.getCertificatesByUserId(id);
    }
}
