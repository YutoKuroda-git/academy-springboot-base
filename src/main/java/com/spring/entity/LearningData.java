package com.spring.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LearningData {
  private Long id;
  private Long userId;
  private Long categoryId;
  private String itemName;
  private Integer learningTime;
  private Integer learningMonth;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}