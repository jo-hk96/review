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
	
	//관리자 페이지로 이동
	@GetMapping("/Admin/AdminPage")
	public String adminPage() {
		return "admin/admin_mypage";
	}
	
	
	//권한 없이 접속시 페이지로 이동
	@GetMapping("/access-error")
	public String accessError() {
		return "admin/access-error";
	}
	
	
}
