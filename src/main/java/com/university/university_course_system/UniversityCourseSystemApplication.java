// UniversityCourseSystemApplication.java
package com.university.university_course_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.university.university_course_system")
public class UniversityCourseSystemApplication {
	public static void main(String[] args) {
		SpringApplication.run(UniversityCourseSystemApplication.class, args);
	}
}
