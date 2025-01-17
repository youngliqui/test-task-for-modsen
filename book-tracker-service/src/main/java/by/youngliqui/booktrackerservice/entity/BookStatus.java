package by.youngliqui.booktrackerservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@Entity
@Table(name = "book_status")
@SQLDelete(sql = "UPDATE book_status SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
@NoArgsConstructor
@AllArgsConstructor
public class BookStatus {

    @Id
    @SequenceGenerator(name = "bookStatusIdSeqGen", sequenceName = "book_status_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bookStatusIdSeqGen")
    private Long id;

    @Column(unique = true)
    private Long bookId;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime borrowedAt;

    private LocalDateTime returnBy;

    @Builder.Default
    private boolean isDeleted = false;

}
