package org.example.common.seeder;


import org.example.user.UserEntity;
import org.example.user.UserRepository;
import org.example.user.UserRoleEnum;
import org.example.user.UserStatusEnum;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserSeeder implements CommandLineRunner {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public UserSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public void run(String... args) {

        if (userRepository.existsByEmail("admin@appmarket.com")
                || userRepository.existsByEmail("daviviana2602@gmail.com")) {

            return;

        }


        UserEntity admin = UserEntity.builder()
                .name("Administrator")
                .email("admin@appmarket.com")
                .password(passwordEncoder.encode("admin"))
                .role(UserRoleEnum.ADMIN)
                .status(UserStatusEnum.ACTIVE)
                .emailVerified(true)
                .emailSent(true)
                .build();


        UserEntity davi = UserEntity.builder()
                .name("davi")
                .email("daviviana2602@gmail.com")
                .password(passwordEncoder.encode("user"))
                .role(UserRoleEnum.USER)
                .status(UserStatusEnum.ACTIVE)
                .emailVerified(true)
                .emailSent(true)
                .build();



        userRepository.save(admin);
        userRepository.save(davi);

        System.out.println("Users seeded successfully!");

    }
}