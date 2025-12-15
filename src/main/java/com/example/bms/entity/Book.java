package com.example.bms.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.*;

@Getter
@Setter
@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "書名は必須です")
    private String title;

    @Column(nullable = false)
    @NotBlank(message = "著者は必須です")
    private String author;

    @Column
    private String publisher;

    @Column(nullable = false)
    @NotBlank(message = "ISBNは必須です")
    private String isbn;

    @Column(nullable = false)
    @NotNull(message = "在庫数は必須です")
    @Min(value = 0, message = "在庫数は0以上で入力してください")
    private Integer stock;

    @Transient
    private boolean userHasLoaned;
}
