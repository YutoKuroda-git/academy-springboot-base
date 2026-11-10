package com.spring.service;

import com.spring.entity.LearningData;
import com.spring.form.LearningDataForm;
import com.spring.mapper.LearningDataMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LearningDataService {

  private final LearningDataMapper learningDataMapper;

  public boolean existsItemName(Long userId, Integer month, String itemName) {
    return learningDataMapper.countByItemName(userId, month, itemName) > 0;
  }

  public void register(Long userId, LearningDataForm form) {
    LearningData data = new LearningData();
    data.setUserId(userId);
    data.setCategoryId(form.getCategoryId());
    data.setItemName(form.getItemName().trim());
    data.setLearningTime(Integer.parseInt(form.getLearningTime()));
    data.setLearningMonth(form.getMonth());
    learningDataMapper.insert(data);
  }
}