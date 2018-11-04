package com.jba;

import com.jba.dao2.Dao2Application;
import com.jba.dao2.blocked.dao.BlockedDAOMySQLRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(exclude = HibernateJpaAutoConfiguration.class)
@Configuration
@Import(Dao2Application.class)
@RestController
public class RestApplication {

	@Autowired
    BlockedDAOMySQLRepository blockedDAOMySQLRepository;

	public static void main(String[] args) {
		SpringApplication.run(RestApplication.class, args);
	}

	@RequestMapping("/")
	public String hello(){
		return "hello";
	}
}
