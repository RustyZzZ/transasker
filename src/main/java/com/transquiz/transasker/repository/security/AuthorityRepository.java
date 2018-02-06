package com.transquiz.transasker.repository.security;

import com.transquiz.transasker.model.security.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Authority getAuthorityByName(String string);


}
