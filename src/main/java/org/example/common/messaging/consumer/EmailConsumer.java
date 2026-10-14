package org.example.common.messaging.consumer;


import org.example.common.exception.ErrorCodeEnum;
import org.example.common.messaging.RabbitConfig;
import org.example.common.messaging.event.UserRegisteredEvent;
import org.example.email.EmailService;
import org.example.user.UserEntity;
import org.example.user.UserRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;


@Component
public class EmailConsumer {

    private final EmailService emailService;
    private final UserRepository userRepository;


    public EmailConsumer(EmailService emailService, UserRepository userRepository) {
        this.emailService = emailService;
        this.userRepository = userRepository;
    }

    private UserEntity findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new org.example.common.exception.NotFoundException(ErrorCodeEnum.USER_NOT_FOUND, "User not found"));
    }



    @RabbitListener(queues = RabbitConfig.USER_REGISTERED_QUEUE)
    public void consume(UserRegisteredEvent event) {

        UserEntity user = findUserById(event.getUserId());

        String token;


        if (user.getEmailVerificationToken() == null
                || user.getEmailVerificationExpiresIn() == null
                || user.getEmailVerificationExpiresIn().isBefore(LocalDateTime.now())) {

            token = UUID.randomUUID().toString();

            user.setEmailVerified(false);
            user.setEmailVerificationToken(token);
            user.setEmailVerificationExpiresIn(LocalDateTime.now().plusHours(1));

            userRepository.save(user);

        } else {
            token = user.getEmailVerificationToken();
        }

        emailService.sendVerificationEmail(
                event.getEmail(),
                token
        );

        user.setEmailSent(true);
        userRepository.save(user);

    }

}