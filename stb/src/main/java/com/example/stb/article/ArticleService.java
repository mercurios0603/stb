package com.example.stb.article;
import com.example.stb.member.Member;
import com.example.stb.comment.Comment;

import com.example.stb.DataNotFoundException;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ArticleService {

  private final ArticleRepository articleRepository;


// 모든 기사 조회 (페이징 미사용)
//  public List<Article> getList() {
//    return this.articleRepository.findAll();
//    // findALl()은 Spring Data JPA의 JpaRepository 인터페이스에서 제공하는 기본 메서드 중 하나
//    // 현재 ArticleRepository는 인터페이스를 통해 JpaRepository의 메서드를 사용할 수 있는 상태임.
//  }

  // 모든 기사 조회 (페이징 사용)
  // pageSize 10은 한 페이지에 보여줄 게시물 갯수
  public Page<Article> getList(int page, String kw) {
    List<Sort.Order> sorts = new ArrayList<>();
    sorts.add(Sort.Order.desc("createDate"));
    Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
    Specification<Article> spec = search(kw);
    return this.articleRepository.findAll(spec, pageable);
  }

  // 기사 id에 맞는 개별 기사 조회
  public Article getArticle(Integer id) {
    // JpaRepository의 기능을 이용하여 기사번호 id에 맞는 기사를 찾아 가져오는 것임
    Optional<Article> article = this.articleRepository.findById(id);
    if (article.isPresent()) {
      return article.get();
    } else {
      throw new DataNotFoundException("article not found");
    }
  }

  public void create(String title, String content, Member member) {
    Article article = new Article();
    article.setTitle(title);
    article.setContent(content);
    article.setCreateDate(LocalDateTime.now());
    article.setAuthor(member);
    this.articleRepository.save(article);
  }

  public void modify(Article article, String subject, String content) {
    article.setTitle(subject);
    article.setContent(content);
    article.setModifyDate(LocalDateTime.now());
    this.articleRepository.save(article);
  }
  public void delete(Article article) {
    this.articleRepository.delete(article);
  }

  public void vote(Article article, Member member) {
    article.getVoter().add(member);
    this.articleRepository.save(article);
  }

  private Specification<Article> search(String kw) {
    return new Specification<>() {
      private static final long serialVersionUID = 1L;
      @Override
      public Predicate toPredicate(Root<Article> a, CriteriaQuery<?> query, CriteriaBuilder cb) {
        query.distinct(true);  // 중복을 제거
        Join<Article, Member> u1 = a.join("author", JoinType.LEFT);
        Join<Article, Comment> c = a.join("commentList", JoinType.LEFT);
        Join<Comment, Member> u2 = c.join("author", JoinType.LEFT);
        return cb.or(cb.like(a.get("title"), "%" + kw + "%"), // 제목
                cb.like(a.get("content"), "%" + kw + "%"),      // 내용
                cb.like(u1.get("username"), "%" + kw + "%"),    // 질문 작성자
                cb.like(c.get("replyContent"), "%" + kw + "%"),      // 답변 내용
                cb.like(u2.get("username"), "%" + kw + "%"));   // 답변 작성자
      }
    };
  }
}
