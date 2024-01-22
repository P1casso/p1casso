package com.p1casso;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
@MapperScan("com.p1casso.**.mapper")
public class P1cassoApplication {

	public static void main(String[] args) {
		SpringApplication.run(P1cassoApplication.class, args);
	}

}
