package com.riot_gaming.controller;
import com.riot_gaming.model.Auth;
import com.riot_gaming.persist.Entity.MemberEntity;
import com.riot_gaming.security.TokenProvider;
import com.riot_gaming.service.MemberService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final MemberService memberService;

  private final TokenProvider tokenProvider;


  // 회원가입 페이지
  @GetMapping("/signup")
  public String signupForNewPerson() {

    return "signup";
  }

  @PostMapping("/signup")
  public String signup(@RequestParam("username") String username,
      @RequestParam("password") String password,
      @RequestParam("roles") List<String> roles,
      Model model) {

    Auth.SignUp signUpRequest = new Auth.SignUp();
    signUpRequest.setUsername(username);
    signUpRequest.setPassword(password);
    signUpRequest.setRoles(roles);

    MemberEntity result =
        this.memberService.register(signUpRequest);

    model.addAttribute("message", "회원가입에 성공했습니다.");
    model.addAttribute("searchUrl", "/board/list");
    return "message";
  }



  // 로그인 페이지
  @GetMapping("/login")
  public String Login() {

    return "login";
  }

  @PostMapping("/login")
  public String Login2(@RequestParam("username") String username,
      @RequestParam("password") String password,
      Model model, HttpServletRequest request) {

    Auth.SignIn signInRequest = new Auth.SignIn();
    signInRequest.setUsername(username);
    signInRequest.setPassword(password);

    var member = this.memberService.authenticate(signInRequest);
    var token = this.tokenProvider.generateToken(member.getUsername(), member.getRoles());
    System.out.println(token);
    // 현재 인증 정보를 가져옴
    Authentication authentication =
        SecurityContextHolder.getContext().getAuthentication();

    // 헤더에 토큰 추가
    if (authentication != null && authentication.isAuthenticated()) {
      request.setAttribute("Authorization", "Bearer " + token);
    }

    log.info("user login -> " + username);

    model.addAttribute("message", "로그인에 성공했습니다.");
    model.addAttribute("searchUrl", "/board/list");

    return "message";
  }

}

