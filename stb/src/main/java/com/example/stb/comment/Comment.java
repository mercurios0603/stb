package com.example.stb.comment;

import com.example.stb.article.Article;
import com.example.stb.member.Member;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
public class Comment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(columnDefinition = "TEXT")
  private String replyContent;

  private LocalDateTime createDate;

  private LocalDateTime modifyDate;

  // question 속성은 답변 엔티티에서 질문 엔티티를 참조하기 위해 추가했다.
  // 예를 들어 답변 객체(예:answer)를 통해 질문 객체의 제목을 알고 싶다면 answer.getQuestion().getSubject()처럼 접근할 수 있다.
  // 하지만 이렇게 속성만 추가하면 안되고 질문 엔티티와 연결된 속성이라는 것을 명시적으로 표시해야 한다.

  @ManyToOne
  private Article article;

  // 여러 개의 답글이 한명의 사용자에 의해 작성될 수 있으므로 @ManyToOne 관계가 성립
  @ManyToOne
  private Member author;

  @ManyToMany
  Set<Member> voter;
}
