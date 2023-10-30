package com.example.stb.comment;

import com.example.stb.article.Article;
import com.example.stb.article.ArticleService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/comment")
@RequiredArgsConstructor
@Controller
public class CommentController {

  private final ArticleService articleService;
  private final CommentService commentService;

  @PostMapping("/create/{id}")
  public String createComment(Model model, @PathVariable("id") Integer id,
                              @Valid CommentForm commentForm, BindingResult bindingResult) {
    // @PathVariable은 URL의 아이디어를 본 메서드의 매개변수 id로 사용하겠다는 이야기

    Article article = this.articleService.getArticle(id);

    // 카멜 표기법, article_detail에서 사용할수 있는 변수는 괄호안의 "article"이다.
    //

    if (bindingResult.hasErrors()) {
      model.addAttribute("article", article);
      return "article_detail";
    }

    this.commentService.create(article, commentForm.getContent());
    return String.format("redirect:/article/detail/%s", id);
  }
}
