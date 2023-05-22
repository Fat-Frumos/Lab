package com.epam.esm.service;

import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.User;
import com.epam.esm.exception.UserNotFoundException;
import com.epam.esm.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final UserMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public UserDto getById(final Long id) {
        return mapper.toDto(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(final Long id) {
        return userDao.getById(id)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("User not found with id %d", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDto> getAll(final Pageable pageable) {
        List<UserDto> dtos = mapper.toDtoList(userDao.getAll(pageable));
        return new PageImpl<>(dtos, pageable, dtos.size());
    }
}
