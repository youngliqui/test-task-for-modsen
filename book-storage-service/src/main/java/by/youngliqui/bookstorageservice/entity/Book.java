package by.youngliqui.bookstorageservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "books")
public class Book {

    @Id
    @SequenceGenerator(name = "booksIdSeqGen", sequenceName = "books_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "booksIdSeqGen")
    private Long id;

    @Column(unique = true)
    private String ISBN;

    private String title;

    @ManyToOne
    @JoinColumn(name = "genre_id", nullable = false)
    private Genre genre;

    private String description;

    private String author;
}
