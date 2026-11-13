package com.spring.mapper;

import com.spring.entity.LearningData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LearningDataMapper {
  int countByItemName(
    @Param("userId") Long userId,
    @Param("month") Integer month,
    @Param("itemName") String itemName
  );

  void insert(LearningData learningData);

  LearningData findByIdAndUserId(
    @Param("id") Long id,
    @Param("userId") Long userId
  );

  void updateLearningTime(
    @Param("id") Long id,
    @Param("userId") Long userId,
    @Param("learningTime") Integer learningTime
  );
}