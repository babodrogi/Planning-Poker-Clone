package com.bb.abrishw.controllers;

import com.bb.abrishw.model.User;
import com.bb.abrishw.dtos.UserDto;
import com.bb.abrishw.services.UserService;
import com.bb.abrishw.utilities.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UserController {

  private UserService userService;
  private JwtTokenUtil jwtTokenUtil;

  @Autowired
  public UserController(UserService userService,JwtTokenUtil jwtTokenUtil) {
    this.userService = userService;
    this.jwtTokenUtil = jwtTokenUtil;
  }

  @GetMapping("/login")
  public String displayLogin(@RequestParam(required = false) String errorMessage ,Model model){
    model.addAttribute("errorMessage",errorMessage);
    return "login";
  }

  @PostMapping("/login")
    public String login(@ModelAttribute()UserDto userDto, HttpServletResponse response){
    User user = userService.findByUsername(userDto.getUsername());
    if(user == null){
      String errorMessage = "User Doesn't Exist";
      return "redirect:/login?errorMessage=" + errorMessage;
    }else if(!user.getPassword().equals(userDto.getPassword())){
      String errorMessage = "Wrong Password";
      return "redirect:/login?errorMessage=" + errorMessage;
    }else{
      userService.setCookieForUser(jwtTokenUtil.generateToken(user),response);
      return "redirect:/";
    }
  }

  @GetMapping("/logout")
  public String logout(HttpServletResponse response){
    userService.logoutUser(response);
    return "redirect:/login";
  }
}
