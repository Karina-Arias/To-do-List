package br.ifsp.tasks_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
public class TasksApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(TasksApiApplication.class, args);
	}
}
