package com.review.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.review.DTO.UserDTO;
import com.review.DTO.UserEditDTO;
import com.review.config.CustomUserDetails;
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
	public String userJoinForm() {
		return "user/user_newjoin";
	}
	
	
	//회원가입
	 @PostMapping("/UserJoin")
	    public String userJoin(UserDTO userDto , RedirectAttributes re) {
		 
		 try {
	        userService.joinUser(userDto);
	        return "redirect:/UserLoginForm";
	        
		  } catch (DataIntegrityViolationException e) {
			  
			  //db에러 메시지 출력
			  e.printStackTrace();
		        
		       return "redirect:/UserJoinForm";
		    }
	   }
	
	
	
	//로그인폼 security에서 자동으로 비교해서 로그인해줌 
	//개편하누~
	@GetMapping("/UserLoginForm")
	public String userLoginForm() {
		return "user/user_login";
	}
	
	
	//마이페이지
	@GetMapping("/UserMypage")
	public String userMypage() {
		return "user/user_mypage";
	}
	
	
	//회원정보수정폼으로 이동
	@GetMapping("/UserEditForm")
	public String userEditForm() {
		return "user/user_edit";
	}
	
	
	//회원정보수정
	@PostMapping("/UserEdit")
	public String userEdit(@AuthenticationPrincipal CustomUserDetails cud, @ModelAttribute UserEditDTO userDto) {
		//userid 가져오기
		Long userid = cud.getUserId();
		userService.updateUser(userid, userDto);
		return "redirect:/UserMypage";
	}
	
	
	//회원정보 삭제
	@PostMapping("/UserDelete")
	//@AuthenticationPrincipal 통해 CustomUserDetails에 잇는 세션정보를 불러옴
	public String userDelete(@AuthenticationPrincipal CustomUserDetails cud) {
		Long userId = cud.getUserId();
		userRepository.deleteById(userId);
		return "redirect:/logout";
		
	}
	
}
