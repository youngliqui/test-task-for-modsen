package by.youngliqui.bookstorageservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Builder
@Getter
@Setter
@Entity
@Table(name = "books")
@SQLDelete(sql = "UPDATE books SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @SequenceGenerator(name = "booksIdSeqGen", sequenceName = "books_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "booksIdSeqGen")
    private Long id;

    @Column(unique = true)
    private String ISBN;

    private String title;

    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;

    private String description;

    private String author;

    @Builder.Default
    private boolean isDeleted = false;

}
