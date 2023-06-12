package org.example.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RandomCatDTO {
    List<String> tags;
    String createdAt;
    String updateAt;
    boolean validated;
    String owner;
    String file;
    String mimetype;
    Long size;
    String _id;
    String url;
}
