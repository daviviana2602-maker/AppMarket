package org.example.common.messaging.event;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor


public class UserChangedPasswordEvent {

    String email;
    String token;

}