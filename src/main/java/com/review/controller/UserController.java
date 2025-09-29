package com.review.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	
	
	 // 이메일 중복 체크 API
    @GetMapping("/check/email")
    public ResponseEntity<Boolean> checkEmail(@RequestParam String email) {
        boolean isDuplicated = userService.checkEmailDuplication(email);
        // isDuplicated가 true면 중복, false면 사용 가능
        return ResponseEntity.ok(isDuplicated);
    }

    // 닉네임 중복 체크 API
    @GetMapping("/check/nickname")
    public ResponseEntity<Boolean> checkNickname(@RequestParam String nickname) {
        boolean isDuplicated = userService.checkNicknameDuplication(nickname);
        return ResponseEntity.ok(isDuplicated);
    }
	
	
	//회원가입 폼으로 이동
	@GetMapping("/UserJoinForm")
	public String userJoinForm() {
		return "user/user_newjoin";
	}
	
	
	//회원가입
	 @PostMapping("/UserJoin")
	    public String userJoin(UserDTO userDto , RedirectAttributes re) {
		 
		   // 1. 이메일 중복 검사
	        if (userService.checkEmailDuplication(userDto.getEmail())) {
	            re.addFlashAttribute("errorMessage", "이미 사용 중인 이메일입니다.");
	            return "redirect:/UserJoinForm"; // 가입 폼으로 리다이렉트
	        }
	        
	        // 2. 닉네임 중복 검사
	        if (userService.checkNicknameDuplication(userDto.getNickname())) {
	            re.addFlashAttribute("errorMessage", "이미 사용 중인 닉네임입니다.");
	            return "redirect:/UserJoinForm"; // 가입 폼으로 리다이렉트
	        }
	        
	        // 3. 중복이 없으면 회원가입 진행
	        userService.joinUser(userDto);
	        
	        return "redirect:/login"; // 성공 후 로그인 페이지로 이동
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
