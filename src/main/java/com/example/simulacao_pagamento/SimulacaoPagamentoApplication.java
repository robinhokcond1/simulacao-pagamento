package com.example.simulacao_pagamento;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.simulacao_pagamento")
public class SimulacaoPagamentoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimulacaoPagamentoApplication.class, args);
	}

}
