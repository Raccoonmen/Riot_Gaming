package com.riot_gaming.security;


import com.riot_gaming.service.MemberService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TokenProvider {

  private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60; // 1 hour
  private static final String KEY_ROLES = "roles";

  private final MemberService memberService;

  @Value("{spring.jwt.secret}")
  private String secretKey;

  /**
   * 토큰 생성(발급)
   * @param username
   * @param roles
   * @return
   */
  public String generateToken(String username, List<String> roles) {
    // 다음 정보들을 포함한 claims 생성
    //      - username
    Claims claims = Jwts.claims().setSubject(username);
    //      - roles
    claims.put(KEY_ROLES, roles);
    //      - 생성 시간
    var now = new Date();
    //      - 만료 시간
    var expiredDate = new Date(now.getTime() + TOKEN_EXPIRE_TIME);

    //      - signature
    // jwt 발급
    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now) // 토큰 생성 시간
        .setExpiration(expiredDate) // 토큰 만료 시간
        .signWith(SignatureAlgorithm.HS512, this.secretKey) // 사용한 암호화 알고리즘, 비밀키
        .compact();
  }

  // Jwt토큰으로 부터 인증정보를 가져온다.
  // memberService를 통해서 가져오고, UserDetails 형태로 return 한다.
  public Authentication getAuthentication(String jwt) {
    UserDetails userDetails = this.memberService.loadUserByUsername(this.getUsername(jwt));
    return new UsernamePasswordAuthenticationToken(
        userDetails, "", userDetails.getAuthorities());
  }

  // 해당 토큰 유효한지 확인하기 위해서 토큰을 받아서 parseClaims를 넣어서
  // username을 가져온다.
  public String getUsername(String token) {
    return this.parseClaims(token).getSubject();
  }

  // 해당 토큰이 빈갑싱면 false를 리턴한다.
  public boolean validateToken(String token) {
    if (!StringUtils.hasText(token)) return false;

    var claims = this.parseClaims(token);
    return !claims.getExpiration().before(new Date()); // 토큰 만료시간이 현재 시간보다 이전인지 확인
  }

  // 토큰을 통해서, 내 secretKey를 파싱해서 claim에서 정보를 가져온다.
  private Claims parseClaims(String token) {
    try {
      return Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(token).getBody();
    } catch (ExpiredJwtException e) {
      return e.getClaims();
    }
  }

}
