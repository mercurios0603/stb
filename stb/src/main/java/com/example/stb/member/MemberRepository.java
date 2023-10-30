package com.example.stb.member;

import com.example.stb.article.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
