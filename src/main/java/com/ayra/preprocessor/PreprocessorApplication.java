package com.ayra.preprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {
    DataSourceAutoConfiguration.class
})
public class PreprocessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(PreprocessorApplication.class, args);
	}

}
