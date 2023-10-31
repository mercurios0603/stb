package com.example.stb.member;

import com.example.stb.article.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

  // findByUsername 메서드는 Spring Data JPA가 제공하는 기능 중 하나입니다.
  // Spring Data JPA는 리포지토리 인터페이스를 정의할 때 메서드 이름 규칙에 따라 자동으로 쿼리 메서드를 생성해줍니다.
  // 따라서 findByUsername 메서드가 별도의 구현이 없어도, Spring Data JPA가 자동으로 해당 메서드를 구현합니다.
  Optional<Member> findByusername(String username);
}
