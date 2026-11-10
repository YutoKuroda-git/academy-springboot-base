package com.spring.controller;

import com.spring.entity.Category;
import com.spring.form.LearningDataForm;
import com.spring.mapper.CategoryMapper;
import com.spring.service.LearningDataService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.YearMonth;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class SkillController {

  private final CategoryMapper categoryMapper;
  private final LearningDataService learningDataService;

  @GetMapping("/skills")
  public String list(
    @RequestParam(required = false) String month,
    HttpSession session,
    Model model) {

    Long userId = getLoginUserId(session);
    if (userId == null) {
      return "redirect:/users/login";
    }

    List<Integer> months = availableMonths();
    int selectedMonth = parseMonth(month, months.get(0));
    if (!months.contains(selectedMonth)) {
      selectedMonth = months.get(0);
    }

    model.addAttribute("months", months);
    model.addAttribute("selectedMonth", selectedMonth);
    model.addAttribute(
      "categories",
      categoryMapper.findWithLearningDataByMonth(userId, selectedMonth));

    return "skill/list";
  }

  @GetMapping("/skills/new")
  public String showNewForm(
    @RequestParam(required = false) Long categoryId,
    @RequestParam(required = false) String month,
    HttpSession session,
    Model model) {

    Long userId = getLoginUserId(session);
    if (userId == null) {
      return "redirect:/users/login";
    }

    List<Integer> months = availableMonths();
    int selectedMonth = parseMonth(month, months.get(0));
    Category category = categoryId == null ? null : categoryMapper.findById(categoryId);

    if (category == null || !months.contains(selectedMonth)) {
      return "redirect:/skills";
    }

    LearningDataForm form = new LearningDataForm();
    form.setCategoryId(categoryId);
    form.setMonth(selectedMonth);

    model.addAttribute("learningDataForm", form);
    model.addAttribute("categoryName", category.getName());
    return "skill/new";
  }

  @PostMapping("/skills")
  public String create(
    @Valid @ModelAttribute LearningDataForm form,
    BindingResult result,
    HttpSession session,
    Model model,
    RedirectAttributes redirectAttributes) {

    Long userId = getLoginUserId(session);
    if (userId == null) {
      return "redirect:/users/login";
    }

    Category category = form.getCategoryId() == null
      ? null
      : categoryMapper.findById(form.getCategoryId());

    if (category == null
      || form.getMonth() == null
      || !availableMonths().contains(form.getMonth())) {
      return "redirect:/skills";
    }

    if (!result.hasFieldErrors("itemName")
      && learningDataService.existsItemName(
        userId, form.getMonth(), form.getItemName().trim())) {
      result.rejectValue(
        "itemName",
        "duplicate",
        form.getItemName().trim() + "は既に登録されています");
    }

    if (result.hasErrors()) {
      model.addAttribute("categoryName", category.getName());
      return "skill/new";
    }

    learningDataService.register(userId, form);

    redirectAttributes.addAttribute("categoryId", form.getCategoryId());
    redirectAttributes.addAttribute("month", form.getMonth());
    redirectAttributes.addFlashAttribute("success", true);
    redirectAttributes.addFlashAttribute("addedItemName", form.getItemName().trim());
    redirectAttributes.addFlashAttribute(
      "addedLearningTime", Integer.parseInt(form.getLearningTime()));
    return "redirect:/skills/new";
  }

  // ログイン中のユーザーIDを返す(未ログインなら null)
  private Long getLoginUserId(HttpSession session) {
    if (!Boolean.TRUE.equals(session.getAttribute("loggedIn"))) {
      return null;
    }
    Long userId = (Long) session.getAttribute("userId");
    if (userId == null) {
      session.invalidate();
    }
    return userId;
  }

  // 当月を含む過去3ヶ月(例: 11, 10, 9 / 1月なら 1, 12, 11)
  private List<Integer> availableMonths() {
    YearMonth now = YearMonth.now();
    return List.of(
      now.getMonthValue(),
      now.minusMonths(1).getMonthValue(),
      now.minusMonths(2).getMonthValue());
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