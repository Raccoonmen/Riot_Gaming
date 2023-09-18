package com.riot_gaming.service;

import com.riot_gaming.exception.NewException;
import com.riot_gaming.model.Auth;
import com.riot_gaming.persist.Entity.MemberEntity;
import com.riot_gaming.persist.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.riot_gaming.type.ErrorCode.ALREADY_EXIST_USER;
import static com.riot_gaming.type.ErrorCode.ID_NOT_FOUND;
import static com.riot_gaming.type.ErrorCode.PASSWORD_UN_MATCH;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@Service
@AllArgsConstructor
public class MemberService implements UserDetailsService {

  private final PasswordEncoder passwordEncoder;
  private final MemberRepository memberRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    log.info("Loading user by username: {}", username);
    return this.memberRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("couldn't find user -> " + username));
  }


  // 회원가입 정보가 맞는지 확인
  public MemberEntity register(Auth.SignUp member) {
    log.info("Registering new member with username: {}", member.getUsername());
    // 이미 아이디가 존재하는 경우 exception 발생
    boolean exists = this.memberRepository.existsByUsername(member.getUsername());
    if (exists) {
      throw new NewException(ALREADY_EXIST_USER,
          BAD_REQUEST);
    }

    // ID 생성 가능한 경우, 멤버 테이블에 저장
    // 비밀번호는 암호화 되어서 저장되어야함
    member.setPassword(this.passwordEncoder.encode(member.getPassword()));
    var result = this.memberRepository.save(member.toEntity());
    log.info("New member registered successfully with username: {}", result.getUsername());

    return result;
  }

  // 로그인 정보가 맞는지 확인
  public MemberEntity authenticate(Auth.SignIn member) {
    log.info("Authenticating member with username: {}", member.getUsername());
    // id 로 멤버 조회
    var user = this.memberRepository.findByUsername(member.getUsername())
        .orElseThrow(() -> new NewException(ID_NOT_FOUND,
            BAD_REQUEST));
    // 패스워드 일치 여부 확인
    //      - 일치하지 않는 경우 400 status 코드와 적합한 에러 메시지 반환
    //      - 일치하는 경우, 해당 멤버 엔티티 반환
    if (!this.passwordEncoder.matches(member.getPassword(), user.getPassword())) {
      log.error("Password does not match for member with username: {}", member.getUsername());
      throw new NewException(PASSWORD_UN_MATCH, BAD_REQUEST);
    }

    return user;
  }


}
