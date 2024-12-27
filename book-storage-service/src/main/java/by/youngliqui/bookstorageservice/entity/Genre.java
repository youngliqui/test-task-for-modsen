package by.youngliqui.bookstorageservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "genres")
public class Genre {
    @Id
    @SequenceGenerator(name = "genresIdSeqGen", sequenceName = "genres_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genresIdSeqGen")
    private Long id;

    private String name;
}
