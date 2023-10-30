package com.example.stb;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

  @GetMapping("/index")
  @ResponseBody
  public String index() {
    System.out.println("Hello Spring Boot"); // 콘솔 출력 명령어
    return "hello spring boot!!";
  }

  // redirect와 forward 두 종류가 있다.
  @GetMapping("/")
  public String root() {
    return "redirect:/article/list";
  }
}
