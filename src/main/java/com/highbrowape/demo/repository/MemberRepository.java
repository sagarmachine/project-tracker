package com.highbrowape.demo.repository;

import com.highbrowape.demo.entity.Member;
import com.highbrowape.demo.entity.Project;
import com.highbrowape.demo.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {

    Optional<Member> findByProjectAndEmail(Project project, String loggedInEmail);

    List<Member> findDistinctByEmail(String loggedInEmail, Pageable pageable);

    List<Member> findDistinctByEmail(String loggedInEmail);


}
