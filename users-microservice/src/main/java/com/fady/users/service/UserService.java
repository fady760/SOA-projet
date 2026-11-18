package com.fady.users.service;

import java.util.List;
import com.fady.users.entities.Role;
import com.fady.users.entities.User;
import com.fady.users.register.RegistationRequest;

public interface UserService {
	User saveUser(User user);
	User findUserByUsername(String username);
	Role addRole(Role role);
	User addRoleToUser(String username, String rolename);
	List<User> findAllUsers();

	User registerUser(RegistationRequest request);
	public User validateToken(String code);
}
