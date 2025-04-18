package com.learning.PGdatabase.domain;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table (name = "authors")
@EqualsAndHashCode (exclude = "id")
public class AuthorEntity {
    @Id
    @GeneratedValue (strategy = GenerationType.SEQUENCE, generator = "authors_seq")
    private Long id;

    private String name;

    private Integer age;
}
