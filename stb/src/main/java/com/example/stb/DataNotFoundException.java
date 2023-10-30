package com.example.stb;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// 예외 클래스 위에 사용되는 애노테이션, 상태 코드와 이유를 설정, 이렇게 하면 404 Not Found를 반환한다.
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "entity not found")
public class DataNotFoundException extends RuntimeException {


  // 직렬화와 연관, 1은 int 데이터형 정수 리터럴, 1L은 long 데이터형 long 정수 리터럴
  private static final long serialVersionUID = 1L;
  public DataNotFoundException(String message) {
    super(message);
  }
}
