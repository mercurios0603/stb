package com.example.stb.article;

import com.example.stb.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
  public Page<Article> getList(int page) {
    List<Sort.Order> sorts = new ArrayList<>();
    sorts.add(Sort.Order.desc("createDate"));
    Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
    return this.articleRepository.findAll(pageable);
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

  public void create(String title, String content) {
    Article article = new Article();
    article.setTitle(title);
    article.setContent(content);
    article.setCreateDate(LocalDateTime.now());
    this.articleRepository.save(article);
  }
}
