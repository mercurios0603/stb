package com.example.stb.comment;

import com.example.stb.DataNotFoundException;
import com.example.stb.article.Article;
import com.example.stb.article.ArticleForm;
import com.example.stb.article.ArticleService;

import com.example.stb.member.Member;
import com.example.stb.member.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.time.LocalDateTime;

@RequestMapping("/comment")
@RequiredArgsConstructor
@Controller
public class CommentController {

  private final ArticleService articleService;
  private final CommentService commentService;
  private final MemberService memberService;

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/create/{id}")
  public String createComment(Model model, @PathVariable("id") Integer id,
                              @Valid CommentForm commentForm, BindingResult bindingResult, Principal principal) {
    // @PathVariable은 URL의 아이디어를 본 메서드의 매개변수 id로 사용하겠다는 이야기

    Article article = this.articleService.getArticle(id);
    Member member = this.memberService.getMember(principal.getName());

    // 카멜 표기법, article_detail에서 사용할수 있는 변수는 괄호안의 "article"이다.
    //

    if (bindingResult.hasErrors()) {
      model.addAttribute("article", article);
      return "article_detail";
    }

    Comment comment = this.commentService.create(article, commentForm.getContent(), member);
    return String.format("redirect:/article/detail/%s#comment_%s", comment.getArticle().getId(), comment.getId());
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/modify/{id}")
  public String commentModify(CommentForm commentForm, @PathVariable("id") Integer id, Principal principal) {
    Comment comment = this.commentService.getComment(id);
    if (!comment.getAuthor().getUsername().equals(principal.getName())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
    }
    commentForm.setContent(comment.getReplyContent());
    return "comment_form";
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/modify/{id}")
  public String commentModify(@Valid CommentForm commentForm, BindingResult bindingResult,
                             @PathVariable("id") Integer id, Principal principal) {
    if (bindingResult.hasErrors()) {
      return "comment_form";
    }
    Comment comment = this.commentService.getComment(id);
    if (!comment.getAuthor().getUsername().equals(principal.getName())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
    }
    this.commentService.modify(comment, commentForm.getContent());
    return String.format("redirect:/article/detail/%s#comment_$s", comment.getArticle().getId(), comment.getId());
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/delete/{id}")
  public String commentDelete(Principal principal, @PathVariable("id") Integer id) {
    Comment comment = this.commentService.getComment(id);
    if (!comment.getAuthor().getUsername().equals(principal.getName())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
    }
    this.commentService.delete(comment);
    return String.format("redirect:/article/detail/%s", comment.getArticle().getId());
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/vote/{id}")
  public String commentVote(Principal principal, @PathVariable("id") Integer id) {
    Comment comment = this.commentService.getComment(id);
    Member member = this.memberService.getMember(principal.getName());
    this.commentService.vote(comment, member);
    return String.format("redirect:/article/detail/%s#comment_%s", comment.getArticle().getId(), comment.getId());
  }
}
