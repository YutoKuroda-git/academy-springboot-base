package com.spring.controller;

import com.spring.entity.User;
import com.spring.mapper.UserMapper;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class TopController {

  private final UserMapper userMapper;

  @GetMapping("/top")
  public String showTop(HttpSession session, Model model) {

    if (!Boolean.TRUE.equals(session.getAttribute("loggedIn"))) {
      return "redirect:/users/login";
    }

    Long userId = (Long) session.getAttribute("userId");
    User user = userMapper.findById(userId);

    if (user == null) {
      session.invalidate();
      return "redirect:/users/login";
    }

    model.addAttribute("user", user);

    return "top";
  }
}