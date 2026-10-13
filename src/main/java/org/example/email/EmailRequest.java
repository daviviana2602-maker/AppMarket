package org.example.email;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor


public class EmailRequest {

    private String from;
    private String to;
    private String subject;
    private String html;

}
