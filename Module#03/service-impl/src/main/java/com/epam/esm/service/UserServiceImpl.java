package com.epam.esm.service;

import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.User;
import com.epam.esm.exception.UserNotFoundException;
import com.epam.esm.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Validated
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final UserMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public UserDto getById(Long id) {
        return mapper.toDto(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userDao.getById(id)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("User not found with id %d", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAll() {
        return userDao.getAll().stream()
                .map(mapper::toDto)
                .collect(toList());
    }
}
