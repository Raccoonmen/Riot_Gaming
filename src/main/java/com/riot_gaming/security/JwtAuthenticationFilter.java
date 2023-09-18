package com.riot_gaming.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 실제로 로그인한 사람에게만 API 접근 해줘야한다.
// OncePerRequestFilter를 상속받아서 dofFilterInternal 메서드 사용.
// 필터에서 요청이 들어올 때마다 토큰이 포함되어 있는지 보고, 유효한지 판단.

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  public static final String TOKEN_HEADER = "Authorization";
  public static final String TOKEN_PREFIX = "Bearer ";

  private final TokenProvider tokenProvider;

  // 해당 인증을 사용하고, 응답으로 나온 토큰으로, token 이 있다면,
  // TokenProvider를 가져와서 validateToekn으로 검사한다.
  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain
  ) throws ServletException, IOException {
    String token = this.resolveTokenFromRequest(request);

    // 요청이 들어올 때마다 토큰을 꺼내서 필터를 실행시켜서 유효하면 인증정보를
    // Context에 담는다.
    // SecurityContextHolder를 통해 Context에 인증 정보를 넣어준다.
    // 그리고 filterChain 이라는 스프링 기능을 통해서 연속적으로 실행되게 한다.
    if (StringUtils.hasText(token) && this.tokenProvider.validateToken(token)) {
      Authentication auth = this.tokenProvider.getAuthentication(token);
      SecurityContextHolder.getContext().setAuthentication(auth);

      log.info(String.format("[%s] -> %s",
          this.tokenProvider.getUsername(token), request.getRequestURI()));
    }

    filterChain.doFilter(request, response);
  }

  // request 토큰을 꺼내는 메서드
  // request를 받아서 헤더를 가져온다.
  private String resolveTokenFromRequest(HttpServletRequest request) {
    String token = request.getHeader(TOKEN_HEADER);

    // token이 존재하고, TOEKN_PRIFIX라면 TOKEN_PREFIX를 제외한 실제 토큰을
    // 리턴한다.
    if (!ObjectUtils.isEmpty(token) && token.startsWith(TOKEN_PREFIX)) {
      return token.substring(TOKEN_PREFIX.length());
    }

    return null;
  }
}
