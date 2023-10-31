package com.example.stb.member;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberSecurityService implements UserDetailsService {

  private final MemberRepository memberRepository;

  // 스프링 부트 또는 스프링 프레임워크에서 throws 키워드가 사용되면,
  // 해당 메서드에서 특정 예외(예외 클래스)가 발생할 수 있다는 것을 나타냅니다.
  // 이 예외는 메서드를 호출한 코드로 전파되며, 호출한 코드에서 이 예외를 처리하도록 유도합니다.
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<Member> _siteUser = this.memberRepository.findByusername(username);
    if (_siteUser.isEmpty()) {
      throw new UsernameNotFoundException("사용자를 찾을수 없습니다.");
    }
    Member member = _siteUser.get();
    List<GrantedAuthority> authorities = new ArrayList<>();
    //MemberRole.ADMIN과 MemberRole.USER는 enum 자료형 때문에 가능한 것.
    if ("admin".equals(username)) {
      authorities.add(new SimpleGrantedAuthority(MemberRole.ADMIN.getValue()));
    } else {
      authorities.add(new SimpleGrantedAuthority(MemberRole.USER.getValue()));
    }
    // 사용자명, 비밀번호, 권한을 입력으로 스프링 시큐리티의 User 객체를 생성하여 리턴했다.
    // 스프링 시큐리티는 loadUserByUsername 메서드에 의해 리턴된 User 객체의 비밀번호가
    // 화면으로부터 입력 받은 비밀번호와 일치하는지를 검사하는 로직을 내부적으로 가지고 있다.
    return new User(member.getUsername(), member.getPassword(), authorities);
  }
}
