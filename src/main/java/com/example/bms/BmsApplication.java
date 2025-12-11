package com.example.bms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class BmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BmsApplication.class, args);
	}

    @Bean
    public org.springframework.boot.CommandLineRunner run(BCryptPasswordEncoder encoder) {
        return args -> {
            System.out.println("ADMINパスワード (admin1234)のハッシュ：");
            System.out.println(encoder.encode("admin1234"));
        };
    }

}
