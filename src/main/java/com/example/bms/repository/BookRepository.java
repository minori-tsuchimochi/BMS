package com.example.bms.repository;

import com.example.bms.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByTitleContainingOrAuthorContaining(
            String title, String author
    );
}
