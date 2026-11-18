package com.fady.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fady.users.entities.Role;
import com.fady.users.entities.User;
import com.fady.users.repos.RoleRepository;
import com.fady.users.repos.UserRepository;
import com.fady.users.service.UserService;
import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class UsersMicroserviceApplication {

	@Autowired
	UserService userService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(UsersMicroserviceApplication.class, args);
	}

	@PostConstruct
	void init_users() {
		// Initialiser les rôles s'ils n'existent pas
		if (roleRepository.findByRole("ADMIN") == null) {
			userService.addRole(new Role(null, "ADMIN"));
		}
		if (roleRepository.findByRole("USER") == null) {
			userService.addRole(new Role(null, "USER"));
		}

		// Initialiser l'administrateur par défaut
		if (userRepository.findByUsername("admin") == null) {
			userService.saveUser(new User(null, "admin", "123", true, "admin@gmail.com", null));
			userService.addRoleToUser("admin", "ADMIN");
			userService.addRoleToUser("admin", "USER");
		}

		// Initialiser un utilisateur standard par défaut (fadi)
		if (userRepository.findByUsername("fadi") == null) {
			userService.saveUser(new User(null, "fadi", "123", true, "fadi@gmail.com", null));
			userService.addRoleToUser("fadi", "USER");
		}
	}
}
