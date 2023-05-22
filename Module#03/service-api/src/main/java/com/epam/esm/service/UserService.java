package com.epam.esm.service;

import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    User findById(Long id);

    UserDto getById(Long id);

    Page<UserDto> getAll(Pageable pageable);
}
