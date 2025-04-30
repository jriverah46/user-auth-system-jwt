package com.jr.security_no_guide;

import com.jr.security_no_guide.persistence.entity.PermissionEntity;
import com.jr.security_no_guide.persistence.entity.RoleEntity;
import com.jr.security_no_guide.persistence.entity.RoleEnum;
import com.jr.security_no_guide.persistence.entity.UserEntity;
import com.jr.security_no_guide.persistence.repository.PermissionRepository;
import com.jr.security_no_guide.persistence.repository.RoleRepository;
import com.jr.security_no_guide.persistence.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Set;

@SpringBootApplication
public class SecurityNoGuideApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurityNoGuideApplication.class, args);
	}


	@Bean
	CommandLineRunner init(UserRepository userRepository,RoleRepository roleRepository,PermissionRepository permissionRepository) {
		return args -> {
			if (userRepository.count() > 0 || roleRepository.count() > 0 || permissionRepository.count() > 0) {
				System.out.println("Los datos ya existen en la base de datos. No se insertarán nuevamente.");
				return;
			}
			//creacion de permisos
			PermissionEntity createPermission = PermissionEntity.builder()
					.name("CREATE")
					.build();
			PermissionEntity updatePermission = PermissionEntity.builder()
					.name("UPDATE")
					.build();
			PermissionEntity deletePermission = PermissionEntity.builder()
					.name("DELETE")
					.build();
			PermissionEntity readPermission = PermissionEntity.builder()
					.name("READ")
					.build();

			//creacion de roles

			RoleEntity roleAdmin = RoleEntity.builder()
					.roleName(RoleEnum.ADMIN)
					.permissions(Set.of(createPermission, updatePermission, deletePermission, readPermission))
					.build();
			RoleEntity roleUser = RoleEntity.builder()
					.roleName(RoleEnum.USER)
					.permissions(Set.of(createPermission, readPermission))
					.build();

			RoleEntity roleDev = RoleEntity.builder()
					.roleName(RoleEnum.DEVELOPER)
					.permissions(Set.of(createPermission, updatePermission, deletePermission, readPermission))
					.build();

			//creacion de usuarios

			UserEntity user1 = UserEntity.builder()
					.username("user1")
					.password("1234")
					.accountNoLocked(true)
					.credentialNoExpired(true)
					.accountNoExpired(true)
					.isEnabled(true)
					.roles(Set.of(roleAdmin))
					.build();
			UserEntity user2 = UserEntity.builder()
					.username("user2")
					.password("1234")
					.accountNoLocked(true)
					.credentialNoExpired(true)
					.accountNoExpired(true)
					.isEnabled(true)
					.roles(Set.of(roleDev))
					.build();
			UserEntity user3 = UserEntity.builder()
					.username("user3")
					.password("1234")
					.accountNoLocked(true)
					.credentialNoExpired(true)
					.accountNoExpired(true)
					.isEnabled(true)
					.roles(Set.of(roleUser))
					.build();


			//guardando registros en bd
			userRepository.saveAll(List.of(user1, user2, user3));
		};
	}}