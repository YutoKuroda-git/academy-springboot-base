package com.spring.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LearningDataForm {

  private Long categoryId;
  private Integer month;

  @NotBlank(message = "項目名は必ず入力してください")
  @Size(max = 50, message = "項目名は50文字以内で入力してください")
  private String itemName;

  @NotBlank(message = "学習時間は必ず入力してください")
  @Pattern(regexp = "^$|^\\d{1,9}$", message = "学習時間は0以上の数字で入力してください")
  private String learningTime;
}