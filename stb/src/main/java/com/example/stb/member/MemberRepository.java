package com.example.stb.member;

import com.example.stb.article.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
  Optional<Member> findByusername(String username);
}
