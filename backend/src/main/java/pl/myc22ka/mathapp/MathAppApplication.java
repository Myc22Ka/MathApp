package pl.myc22ka.mathapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MathAppApplication {
	public static void main(String[] args) {
		SpringApplication.run(MathAppApplication.class, args);
	}
}
