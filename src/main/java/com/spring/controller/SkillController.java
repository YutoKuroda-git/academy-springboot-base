package com.spring.controller;

import com.spring.mapper.CategoryMapper;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.YearMonth;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class SkillController {

  private final CategoryMapper categoryMapper;

  @GetMapping("/skills")
  public String list(
    @RequestParam(required = false) String month,
    HttpSession session,
    Model model) {

    if (!Boolean.TRUE.equals(session.getAttribute("loggedIn"))) {
      return "redirect:/users/login";
    }

    Long userId = (Long) session.getAttribute("userId");
    if (userId == null) {
      session.invalidate();
      return "redirect:/users/login";
    }

    YearMonth now = YearMonth.now();
    int currentMonth = now.getMonthValue();

    // 当月を含む過去3ヶ月(例: 11, 10, 9 / 1月なら 1, 12, 11)
    List<Integer> months = List.of(
      currentMonth,
      now.minusMonths(1).getMonthValue(),
      now.minusMonths(2).getMonthValue());

    int selectedMonth = parseMonth(month, currentMonth);
    if (!months.contains(selectedMonth)) {
      selectedMonth = currentMonth;
    }

    model.addAttribute("months", months);
    model.addAttribute("selectedMonth", selectedMonth);
    model.addAttribute(
      "categories",
      categoryMapper.findWithLearningDataByMonth(userId, selectedMonth));

    return "skill/list";
  }

  private int parseMonth(String month, int fallback) {
    if (month == null) {
      return fallback;
    }
    try {
      return Integer.parseInt(month);
    } catch (NumberFormatException e) {
      return fallback;
    }
  }
}