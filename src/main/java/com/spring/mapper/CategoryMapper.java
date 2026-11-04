package com.spring.mapper;

import com.spring.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CategoryMapper {
  List<Category> findWithLearningDataByMonth(
    @Param("userId") Long userId,
    @Param("month") Integer month
  );
}