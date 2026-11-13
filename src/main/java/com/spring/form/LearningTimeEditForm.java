package com.spring.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class LearningTimeEditForm {

  private Integer month;

  @NotBlank(message = "学習時間は必ず入力してください")
  @Pattern(regexp = "^$|^\\d{1,9}$", message = "学習時間は0以上の数字で入力してください")
  private String learningTime;
}