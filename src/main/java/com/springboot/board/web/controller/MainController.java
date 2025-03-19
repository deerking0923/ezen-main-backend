package com.springboot.board.web.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/")
public class MainController {

    @GetMapping("/")
    public String home() {
        return "Ezen Main Backend가 실행 중입니다.";
    }
}
