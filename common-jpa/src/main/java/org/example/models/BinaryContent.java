package org.example.models;

import lombok.*;
import org.example.models.enums.TypeFile;

import javax.persistence.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "binary_content")
public class BinaryContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long fileId;
    @Enumerated(EnumType.STRING)
    private TypeFile typeFile;
    private byte[] fileAsArrayOfBytes;
}
