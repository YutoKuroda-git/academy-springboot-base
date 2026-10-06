package com.spring.mapper;

import com.spring.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
  void insert(User user);
}
