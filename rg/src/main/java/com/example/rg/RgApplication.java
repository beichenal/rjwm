package com.example.rg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class RgApplication {

	public static void main(String[] args) {
		SpringApplication.run(RgApplication.class, args);
		log.info("项目启动成功");
	}

}
