package com.transquiz.transasker.model;

import com.transquiz.transasker.model.security.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "profiles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    private String tgUsername;

    private Integer tgPrivateChatId;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "profile_words", joinColumns = @JoinColumn(name = "profile_id"), inverseJoinColumns = @JoinColumn(name = "word_id"))
    private Set<Word> words;

    private String mode;


}
