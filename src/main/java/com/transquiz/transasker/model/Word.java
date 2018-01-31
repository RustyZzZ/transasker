package com.transquiz.transasker.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity(name = "words")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String sourceWord;
    private String targetWord;
    @Enumerated(EnumType.STRING)
    private Languages fromLang;
    @Enumerated(EnumType.STRING)
    private Languages toLang;
}
