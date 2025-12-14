package com.example.bms.repository;

import com.example.bms.entity.LoanHistory;
import com.example.bms.entity.Book;
import com.example.bms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LoanHistoryRepository extends JpaRepository<LoanHistory, Long> {
    List<LoanHistory> findByBookAndReturnDateIsNull(Book book);
}
