package com.example.stb.article;

import com.example.stb.comment.CommentForm;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

import org.springframework.ui.Model;

import java.util.List;


@RequestMapping("/article")
@RequiredArgsConstructor
@Controller
public class ArticleController {

  private final ArticleService articleService;

  // Model model은 View를 보여줄 부분이 있을 때, html에서 사용하기 위해 작성하는 것이다. (프레임워크 ui.Model)
  // (페이징 적용이후) 스프링부트의 페이징은 첫페이지 번호가 1이 아닌 0이다.
  //  paging.isEmpty	페이지 존재 여부 (게시물이 있으면 false, 없으면 true)
  //  paging.totalElements	전체 게시물 개수
  //  paging.totalPages	전체 페이지 개수
  //  paging.size	페이지당 보여줄 게시물 개수
  //  paging.number	현재 페이지 번호
  //  paging.hasPrevious	이전 페이지 존재 여부
  //  paging.hasNext	다음 페이지 존재 여부

  @GetMapping("/list")
  public String list(Model model, @RequestParam(value="page", defaultValue = "0") int page) {
    Page<Article> paging = this.articleService.getList(page);
    model.addAttribute("paging", paging);
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
  @PostMapping("/create")
  public String articleCreate(@Valid ArticleForm articleForm, BindingResult bindingResult) {
    // 오류가 있을 경우에는 기사글을 작성하는 화면을 다시 렌더링 한 것
    if (bindingResult.hasErrors()) {
      return "article_form";
    }
    this.articleService.create(articleForm.getTitle(), articleForm.getContent());
    return "redirect:/article/list";
  }
}
