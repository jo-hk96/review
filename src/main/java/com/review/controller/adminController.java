package com.review.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.review.DTO.UserDTO;
import com.review.repository.UserRepository;
import com.review.service.UserService;

@Controller
public class adminController {

	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserService userService;
	
	//회원가입 폼으로 이동
	@GetMapping("/AdminPage")
	public String adminPage() {
		return "admin/admin_mypage";
	}
	
	
	
}
