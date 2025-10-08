package com.review.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.review.entity.userEntity;
import com.review.repository.UserRepository;
import com.review.service.UserService;

@Controller
public class adminController {

	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserService userService;
	
	//관리자 페이지로 이동/ 회원리스트 출력
	@GetMapping("/Admin/AdminPage")
	public String adminPage(Model model) {
		List<userEntity> AllUser = userRepository.findAllByOrderByUserIdAsc();
		model.addAttribute("allUser" , AllUser);
		return "admin/admin_mypage";
	}
	
	
	//권한 없이 접속시 페이지로 이동
	@GetMapping("/access-error")
	public String accessError() {
		return "admin/access-error";
	}
	
	
	
	
	
	
}
