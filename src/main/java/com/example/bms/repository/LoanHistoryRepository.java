package com.example.bms.repository;

import com.example.bms.entity.LoanHistory;
import com.example.bms.entity.Book;
import com.example.bms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface LoanHistoryRepository extends JpaRepository<LoanHistory, Long> {
    Optional<LoanHistory> findByBookAndReturnDateIsNull(Book book);
    List<LoanHistory> findByUser(User user);
}
