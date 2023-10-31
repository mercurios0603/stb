package com.example.stb.member;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@RequiredArgsConstructor
@Controller
@RequestMapping("/member")
public class MemberController {

  private final MemberService memberService;

  @GetMapping("/signup")
  public String signup(MemberCreateForm memberCreateForm) {
    return "signup_form";
  }

  @PostMapping("/signup")
  public String signup(@Valid MemberCreateForm memberCreateForm, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return "signup_form";
    }

    // bindingResult.rejectValue의 각 파라미터는 bindingResult.rejectValue(필드명, 오류코드, 에러메시지)를 의미함.
    // 대형 프로젝트에서는 번역과 관리를 위해 오류코드를 잘 정의하여 사용해야 한다.
    if (!memberCreateForm.getPassword1().equals(memberCreateForm.getPassword2())) {
      bindingResult.rejectValue("password2", "passwordInCorrect",
              "2개의 패스워드가 일치하지 않습니다.");
      return "signup_form";
    }

    try {
    this.memberService.create(memberCreateForm.getUsername(), memberCreateForm.getEmail(), memberCreateForm.getPassword1());
    }catch(
      DataIntegrityViolationException e) {
      e.printStackTrace();
      bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
      return "signup_form";
    }catch(Exception e) {
      e.printStackTrace();
      bindingResult.reject("signupFailed", e.getMessage());
      return "signup_form";
    }

    return "redirect:/";
  }

  // 실제 로그인을 진행하는 @PostMapping 방식의 메서드는 스프링 시큐리티가 대신 처리하므로 직접 구현할 필요가 없다.
  // 로그아웃은 스프링 시큐리티 상에서 처리될 뿐더러 별도의 로그아웃 페이지는 없다. 네비게이션바의 UI만 변경.
  @GetMapping("/login")
  public String login() {
    return "login_form";
  }
}
