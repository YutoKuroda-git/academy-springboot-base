package com.spring.controller;

import com.spring.form.UserRegisterForm;
import com.spring.service.UserService;
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

@Controller
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping("/users/register")
  public String showRegisterForm(Model model) {
    model.addAttribute("userRegisterForm", new UserRegisterForm());
    return "users/register";
  }

  @GetMapping("/users/login")
  public String showLoginForm() {
    return "users/login";
  }

  
  @PostMapping("/users/login")
  public String login(
    @RequestParam String email,
    @RequestParam String password,
    RedirectAttributes redirectAttributes) {

    if (userService.login(email, password)) {
      return "redirect:/top";
    }

    redirectAttributes.addFlashAttribute(
      "errorMessage",
      "メールアドレス、もしくはパスワードが間違っています"
    );

    return "redirect:/users/login";
  }

  @PostMapping("/users/register")
  public String register(
    @Valid @ModelAttribute("userRegisterForm") UserRegisterForm form,
    BindingResult bindingResult) {

    if (bindingResult.hasErrors()) {
      return "users/register";
    }

    userService.register(form);
    return "redirect:/top";
  }
}
