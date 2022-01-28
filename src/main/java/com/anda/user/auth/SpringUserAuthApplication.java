package com.anda.user.auth;

import com.anda.user.auth.model.Role;
import com.anda.user.auth.model.RoleEnum;
import com.anda.user.auth.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.Arrays;

@SpringBootApplication
public class SpringUserAuthApplication implements CommandLineRunner {

	@Autowired
	RoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(SpringUserAuthApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var roles = new ArrayList<Role>();
		Arrays.asList(RoleEnum.values()).forEach(roleEnum -> roles.add(new Role(roleEnum)));
		roleRepository.saveAll(roles);

		roleRepository.findAll().forEach(System.out::println);
	}
}
