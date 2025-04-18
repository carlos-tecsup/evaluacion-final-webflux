package com.mitocode;

import com.mitocode.model.Role;
import com.mitocode.model.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class MatriculaApplication {


	public static void main(String[] args) {
		SpringApplication.run(MatriculaApplication.class, args);
	}


}
