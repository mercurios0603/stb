package com.example.stb.article;

import java.time.LocalDateTime;
import java.util.List;

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

// private LocalDataTime modifyDate;

  @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE)
  private List<Comment> commentList;

  @ManyToOne
  private Member author;
}
