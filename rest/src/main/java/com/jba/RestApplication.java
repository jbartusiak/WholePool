package com.jba;

import com.jba.dao2.DAOConfig;
import com.jba.dao2.blocked.dao.BlockedDAO;
import com.jba.dao2.blocked.entity.BlockStatus;
import com.jba.dao2.blocked.entity.BlockedUsers;
import com.jba.dao2.user.enitity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(exclude = HibernateJpaAutoConfiguration.class)
@Configuration
@Import(DAOConfig.class)
@RestController
public class RestApplication {

    @Autowired
    BlockedDAO blockedDAO;

	public static void main(String[] args) {
		SpringApplication.run(RestApplication.class, args);
	}

	@RequestMapping("/")
	public String hello(){
		return "hello";
	}

	@RequestMapping("/BU/{id}")
	public BlockedUsers blockedUsers(@PathVariable("id") int id){
		return blockedDAO.getUserBlockedStatus(User.of(id));
	}
}
