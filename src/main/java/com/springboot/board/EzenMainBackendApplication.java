package com.springboot.board;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class EzenMainBackendApplication {

	static {
		// .env 파일 읽기
		Dotenv dotenv = Dotenv.configure()
				.directory("./") // .env 파일 경로
				.load();
		dotenv.entries().forEach(entry -> {
			System.setProperty(entry.getKey(), entry.getValue());
		});
	}

	public static void main(String[] args) {
		SpringApplication.run(EzenMainBackendApplication.class, args);
	}

}
