package com.example.stb.article;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// JpaRepository를 상속할 때는 제네릭스 타입으로 <Question, Integer> 처럼 리포지터리의 대상이 되는
// 엔티티의 타입(Question)과 해당 엔티티의 PK의 속성 타입(Integer)을 지정해야 한다.
// 이것은 JpaRepository를 생성하기 위한 규칙이다.
public interface ArticleRepository extends JpaRepository<Article, Integer> {

  // 아래의 기능은 JpaRepository에서 자체 지원하기 때문에 아래의 메서드는 그냥 형태만 알아두어라.

  // 제목으로 데이터 조회
  Article findByTitle(String title);

  // 제목과 내용으로 데이터 조회 AND
  Article findByTitleAndContent(String title, String content);

  // 제목에 특정 문자열이 포함되어 있는 데이터 조회
  List<Article> findByTitleLike(String title);

  // 페이징 관련
  Page<Article> findAll(Pageable pageable);

  //  검색 관련
  Page<Article> findAll(Specification<Article> spec, Pageable pageable);
}
