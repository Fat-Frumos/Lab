package com.epam.esm.service;

import com.epam.esm.criteria.Criteria;
import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.UserWithoutOrderDto;
import com.epam.esm.entity.User;

import java.util.List;

public interface UserService {
    User findById(Long id);

    UserDto getById(Long id);

    List<UserWithoutOrderDto> getAll(Criteria criteria);
}
