package com.spring.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data 
public class User {
  private Long id;
  private String name;
  private String email;
  private String password;
  private String selfIntroduction;
  private String avatarImage;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
