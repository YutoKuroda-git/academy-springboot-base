package com.spring.controller;

import com.spring.entity.User;
import com.spring.mapper.UserMapper;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class UserProfileController {
  
  private final UserMapper userMapper;
  
  @GetMapping("/users/profile/edit")
  public String showEditForm(HttpSession session, Model model) {
    
    if (!Boolean.TRUE.equals(session.getAttribute("loggedIn"))) {
      return "redirect:/users/login";
    }
    
    Long userId = (Long) session.getAttribute("userId");
    
    if (userId == null) {
      session.invalidate();
      return "redirect:/users/login";
    }
    
    User user = userMapper.findById(userId);
    
    if (user == null) {
      session.invalidate();
      return "redirect:/users/login";
    }
    
    model.addAttribute("user", user);
    return "users/profile/edit";
  }
  
  @PostMapping("/users/profile/edit")
  public String updateProfile(
    @RequestParam(required = false) String selfIntroduction,
    @RequestParam(required = false) MultipartFile avatarImage,
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
      
      if (selfIntroduction == null
        || selfIntroduction.trim().length() < 50
        || selfIntroduction.length() > 200) {
          
        User user = userMapper.findById(userId);
          
        if (user == null) {
          session.invalidate();
          return "redirect:/users/login";
        }
        
        user.setSelfIntroduction(selfIntroduction);
        model.addAttribute("user", user);
        model.addAttribute(
          "errorMessage",
          "50文字以上200文字以下で入力してください"
        );
        
        return "users/profile/edit";
      }
        
      if (avatarImage != null && !avatarImage.isEmpty()) {
        try {

          Path uploadDir = Paths.get(System.getProperty("user.dir"))
          .resolve("uploads")
          .toAbsolutePath()
          .normalize();
          
          Files.createDirectories(uploadDir);
          
          String originalFilename = avatarImage.getOriginalFilename();
          String extension = "";
          
          if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(
              originalFilename.lastIndexOf(".")
            ).toLowerCase();
          }
          
          if (!extension.matches("\\.(jpg|jpeg|png|gif|webp)")) {
            model.addAttribute("user", userMapper.findById(userId));
            model.addAttribute("avatarErrorMessage", "JPG、PNG、GIF、WEBP形式の画像を選択してください");
            return "users/profile/edit";
          }
          
          String filename = UUID.randomUUID() + extension;
          Path filePath = uploadDir.resolve(filename);
          
          avatarImage.transferTo(filePath.toFile());
          
          String imagePath = "/uploads/" + filename;
          userMapper.updateAvatarImage(userId, imagePath);
          
        } catch (IOException e) {
          e.printStackTrace();
          
          model.addAttribute("user", userMapper.findById(userId));
          model.addAttribute("avatarErrorMessage", "画像の保存に失敗しました");
          return "users/profile/edit";
        }
      }
        
    userMapper.updateSelfIntroduction(userId, selfIntroduction);
    return "redirect:/top";
  }
}