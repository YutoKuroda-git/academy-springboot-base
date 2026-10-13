package com.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;

@Controller
public class TopController {
  @GetMapping("/top")
  public String showTop(HttpSession session) {

  if (!Boolean.TRUE.equals(session.getAttribute("loggedIn"))) {
    return "redirect:/users/login";
  }
    return "top";
  }
}
