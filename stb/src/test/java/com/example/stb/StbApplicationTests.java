package com.example.stb;

import com.example.stb.article.ArticleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StbApplicationTests {

	@Autowired
	private ArticleService articleService;

	@Test
	void testJpa() {
		for (int i = 1; i <= 300; i++) {
			String title = String.format("테스트 데이터입니다:[%03d]", i);
			String content = "내용무";
			this.articleService.create(title, content);
		}
	}

}
