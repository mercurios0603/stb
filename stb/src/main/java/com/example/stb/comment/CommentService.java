package com.example.stb.comment;


import com.example.stb.DataNotFoundException;
import com.example.stb.article.Article;
import com.example.stb.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentService {

  private final CommentRepository commentRepository;

  public Comment create(Article article, String content, Member author) {

    Comment comment = new Comment();
    comment.setReplyContent(content);
    comment.setCreateDate(LocalDateTime.now());
    comment.setArticle(article);
    comment.setAuthor(author);
    this.commentRepository.save(comment);
    return comment;
  }

  public Comment getComment(Integer id) {
    Optional<Comment> comment = this.commentRepository.findById(id);
    if (comment.isPresent()) {
      return comment.get();
    } else {
      throw new DataNotFoundException("comment not found");
    }
  }

  public void modify(Comment comment, String content) {
    comment.setReplyContent(content);
    comment.setModifyDate(LocalDateTime.now());
    this.commentRepository.save(comment);
  }

  public void delete(Comment comment) {
    this.commentRepository.delete(comment);
  }

  public void vote(Comment comment, Member member) {
    comment.getVoter().add(member);
    this.commentRepository.save(comment);
  }
}
