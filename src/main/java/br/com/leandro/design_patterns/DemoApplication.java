package br.com.leandro.design_patterns;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


/*
* Projeto Spring Boot gerado Spring Initializr.
* O seguintes módulos foram selecionados:
* - Spring Data JPA
* - Spring Web
* - H2 database
* - OpenFeign
* */

@EnableFeignClients
@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
