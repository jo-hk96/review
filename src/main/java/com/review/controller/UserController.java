package com.review.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.review.DTO.UserDTO;
import com.review.repository.UserRepository;
import com.review.service.UserService;

@Controller
public class UserController {

	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserService userService;
	
	//회원가입 폼으로 이동
	@GetMapping("/UserJoinForm")
	public String userjoinForm() {
		return "user/user_newjoin";
	}
	
	
	//회원가입
	@PostMapping("/UserJoin")
	public String userjoin(UserDTO userDto) {
		userService.join(userDto);
		
		return "redirect:/home";
	}
	
	
	
	//로그인폼
	@GetMapping("/UserLoginForm")
	public String userloginform() {
		return "user/user_login";
	}
	
	
	
	//로그인하기
	@GetMapping("/UserLogin")
	public String userlogin() {
		return "redirect:/home";
	}
	
	
	
	
	//내정보
	@GetMapping("/UserMypage")
	public String userMypage() {
		return "user/user_mypage";
	}
	
}
