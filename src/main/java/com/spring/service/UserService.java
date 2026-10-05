package com.spring.service;

import com.spring.entity.User;
import com.spring.form.UserRegisterForm;
import com.spring.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public void register(UserRegisterForm form) {

        User user = new User();

        user.setName(form.getName());
        user.setEmail(form.getEmail());

        String hashedPassword = passwordEncoder.encode(form.getPassword());
        user.setPassword(hashedPassword);

        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        userMapper.insert(user);
    }
}
