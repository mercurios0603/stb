package com.example.stb.comment;

import com.example.stb.article.ArticleService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
