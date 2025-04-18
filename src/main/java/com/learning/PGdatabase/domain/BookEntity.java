package com.learning.PGdatabase.domain;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table (name = "books")
public class BookEntity {
    @Id
    private String isbn;

    @NonNull
    private String title;

    @ManyToOne (cascade = CascadeType.ALL)
    @JoinColumn (name = "author_id")
    private AuthorEntity author;
}
