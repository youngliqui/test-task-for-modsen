package by.youngliqui.booktrackerservice.repository;

import by.youngliqui.booktrackerservice.entity.BookStatus;
import by.youngliqui.booktrackerservice.entity.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookStatusRepository extends JpaRepository<BookStatus, Long> {
    Page<BookStatus> findByStatus(Status status, Pageable pageable);

    Optional<BookStatus> findByBookId(Long bookId);

    boolean existsByBookId(Long bookId);
}
