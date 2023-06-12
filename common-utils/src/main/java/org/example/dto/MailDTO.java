package org.example.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MailDTO {
    private String id;
    private String emailTo;
}
