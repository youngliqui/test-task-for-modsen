package by.youngliqui.booktrackerservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "book_status")
public class BookStatus {

    @Id
    @SequenceGenerator(name = "bookStatusIdSeqGen", sequenceName = "book_status_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bookStatusIdSeqGen")
    private Long id;

    private Long bookId;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime borrowedAt;

    private LocalDateTime returnBy;
}
