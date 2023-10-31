package com.example.stb.article;

import com.example.stb.comment.CommentForm;
import com.example.stb.member.Member;
import com.example.stb.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

import org.springframework.ui.Model;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;


@RequestMapping("/article")
@RequiredArgsConstructor
@Controller
public class ArticleController {

  private final ArticleService articleService;
  private final MemberService memberService;

  // Model model은 View를 보여줄 부분이 있을 때, html에서 사용하기 위해 작성하는 것이다. (프레임워크 ui.Model)
  // (페이징 적용이후) 스프링부트의 페이징은 첫페이지 번호가 1이 아닌 0이다.
  //  paging.isEmpty	페이지 존재 여부 (게시물이 있으면 false, 없으면 true)
  //  paging.totalElements	전체 게시물 개수
  //  paging.totalPages	전체 페이지 개수
  //  paging.size	페이지당 보여줄 게시물 개수
  //  paging.number	현재 페이지 번호
  //  paging.hasPrevious	이전 페이지 존재 여부
  //  paging.hasNext	다음 페이지 존재 여부

  // 리스트 불러오기 기능 자체가 결국 모든 게시물을 검색하는 것과 같다. 따라서 해당 메서드에 키워드를 공백으로 추가하여
  // 기능을 보완하는 것
  @GetMapping("/list")
  public String list(Model model, @RequestParam(value="page", defaultValue = "0") int page, @RequestParam(value = "kw", defaultValue = "") String kw) {
    Page<Article> paging = this.articleService.getList(page, kw);
    model.addAttribute("paging", paging);
    model.addAttribute("kw", kw);
    return "article_list";
  }

  @GetMapping("/detail/{id}")
  //해당 URL에 있는 {id}가 @PathVariable로 가져와져서 Integer id에 들어가고,
  // 이 id가 다시 articleService의 getArticle 메서드의 매개변수로 들어간다.
  public String detail(Model model, @PathVariable("id") Integer id, CommentForm commentForm) {
    Article article = this.articleService.getArticle(id);
    model.addAttribute("article", article);
    return "article_detail";
  }

  // @PostMapping의 메서드명은 @GetMapping시 사용했던 articleCreate 메서드명과 동일하게 사용할 수 있다.
  // 단, 매개변수의 형태가 다른 경우에 가능하다. - 메서드 오버로딩 (create는 메서드 오버로딩을 사용한 것이라고 볼 수 있다)
  @PreAuthorize("isAuthenticated()")
  @GetMapping("/create")
  // ArticleForm과 같이 매개변수로 바인딩한 객체는 Model 객체로 전달하지 않아도 템플릿(article_form)에서 사용이 가능하다
  // 이 ArticleForm 객체의 articleForm은 템플릿의 article_form의 th:object="${articleForm}"로 사용된다.
  public String articleCreate(ArticleForm articleForm) {
    return "article_form";
  }

  // @Valid 어노테이션을 사용하면 데이터 모델 객체에 적용된 유효성 검사 어노테이션을 검사한다.
  // 이때 제약조건은 ArticleForm에 기사 제목과 내용에 대한 사항이 명시되어 있다.
  // 데이터 모델 객체가 제약 조건을 위반하는 경우, 해당 검사 오류는 BindingResult에 기록된다.
  // BindingResult 객체는 스프링 MVC 컨트롤러 메서드에서 @Valid 어노테이션과 함께 사용되며,
  // 데이터 모델 객체의 유효성 검사를 수행한 후 발생한 오류를 수집하고 제어한다.
  // 오류가 발생한 경우, 해당 오류 정보를 처리하고 적절한 응답을 생성하거나 리디렉션할 수 있다.


  //  Principal은 현재 사용자를 나타내는 인터페이스입니다. 이 인터페이스는 Spring Security와 관련이 깊으며,
  //  인증된 사용자의 정보를 가져오는 데 사용됩니다. 주로 웹 애플리케이션에서 사용자 정보를 얻어오는 데 활용됩니다.
  @PreAuthorize("isAuthenticated()")
  @PostMapping("/create")
  public String articleCreate(@Valid ArticleForm articleForm, BindingResult bindingResult, Principal principal) {
    // 오류가 있을 경우에는 기사글을 작성하는 화면을 다시 렌더링 한 것
    if (bindingResult.hasErrors()) {
      return "article_form";
    }
    Member member = this.memberService.getMember(principal.getName());
    this.articleService.create(articleForm.getTitle(), articleForm.getContent(), member);
    return "redirect:/article/list";
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/modify/{id}")
  public String articleModify (ArticleForm articleForm, @PathVariable("id") Integer id, Principal principal) {
    Article article = this.articleService.getArticle(id);
    if (!article.getAuthor().getUsername().equals(principal.getName())){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
    }
    articleForm.setTitle(article.getTitle());
    articleForm.setContent(article.getContent());
    return "article_form";
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/modify/{id}")
  public String articleModify(@Valid ArticleForm articleForm, BindingResult bindingResult,
                               Principal principal, @PathVariable("id") Integer id) {
    if (bindingResult.hasErrors()) {
      return "article_form";
    }
    Article article = this.articleService.getArticle(id);
    if (!article.getAuthor().getUsername().equals(principal.getName())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
    }
    this.articleService.modify(article, articleForm.getTitle(), articleForm.getContent());
    return String.format("redirect:/article/detail/%s", id);
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/delete/{id}")
  public String articleDelete(Principal principal, @PathVariable("id") Integer id) {
    Article article = this.articleService.getArticle(id);
    if (!article.getAuthor().getUsername().equals(principal.getName())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
    }
    this.articleService.delete(article);
    return "redirect:/";
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/vote/{id}")
  public String articleVote(Principal principal, @PathVariable("id") Integer id) {
    Article article = this.articleService.getArticle(id);
    Member member = this.memberService.getMember(principal.getName());
    this.articleService.vote(article, member);
    return String.format("redirect:/article/detail/%s", id);
  }
}
