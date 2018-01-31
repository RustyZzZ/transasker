package com.transquiz.transasker.repository;

import com.transquiz.transasker.model.Profile;
import com.transquiz.transasker.model.security.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Profile getProfileByUser(User user);
}
