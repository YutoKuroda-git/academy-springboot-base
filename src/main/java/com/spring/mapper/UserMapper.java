package com.spring.mapper;

import com.spring.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
  void insert(User user);
  User findByEmail(@Param("email") String email);
}
