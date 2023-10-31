package com.example.stb.article;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.example.stb.comment.Comment;
import com.example.stb.member.Member;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

@Getter
@Setter
@Entity
public class Article {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

//  int memberid; (private SiteUser author;
// 이후 별도의 클래스로 생성됨 (ManyToOne)
// 현재는 회원이름 엔티티 작성.

  @Column(length = 200)
  private String title;

  @Column(columnDefinition = "TEXT")
  private String content;

  private LocalDateTime createDate;

  private LocalDateTime modifyDate;

  @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE)
  private List<Comment> commentList;

  // 여러개의 기사가 한 명의 사용자로부터 작성될 수 있으므로 @ManyToOne 관계가 성립
  @ManyToOne
  private Member author;

  @ManyToMany
  Set<Member> voter;
}
