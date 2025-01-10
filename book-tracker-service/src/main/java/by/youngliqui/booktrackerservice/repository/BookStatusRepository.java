package by.youngliqui.booktrackerservice.repository;

import by.youngliqui.booktrackerservice.entity.BookStatus;
import by.youngliqui.booktrackerservice.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookStatusRepository extends JpaRepository<BookStatus, Long> {
    List<BookStatus> findByStatus(Status status);
}
