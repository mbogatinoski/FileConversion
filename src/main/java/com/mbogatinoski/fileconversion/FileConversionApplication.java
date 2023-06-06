package com.mbogatinoski.fileconversion;

import org.apache.fop.render.awt.viewer.Command;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootApplication
public class FileConversionApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileConversionApplication.class, args);
	}

//	@Bean
//	CommandLineRunner commandLineRunner(KafkaTemplate<String, String> kafkaTemplate) {
//		System.out.println("Testing");
//		return args -> {
//			for(int i = 0; i < 100; i++) {
//				kafkaTemplate.send("file-conversion", "hello kafka :D " + i);
//			}
//		};
//	}
}
