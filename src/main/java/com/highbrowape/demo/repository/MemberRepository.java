package com.highbrowape.demo.repository;

import com.highbrowape.demo.entity.Member;
import com.highbrowape.demo.entity.Project;
import com.highbrowape.demo.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Transactional
public interface MemberRepository extends JpaRepository<Member,Long> {


    List<Member> findByProjectProjectId(String id) ;


    int countByProjectIdAndUserEmail(String projectId, String loggedInEmail);

    Object countByProject(Project p);

    Optional<Member> findByProjectAndUserEmail(Project project, String loggedInEmail);

    double countDistinctByUserEmail(String loggedInEmail);

    List<Member> findDistinctByUserEmail(String loggedInEmail, Pageable pageable);

    double countByUserEmail(String loggedInEmail);

    List<Member> findByUserEmail(String loggedInEmail, Pageable pageable);


    void deleteAllByProjectId(long id);

    Optional<Member> findByProjectProjectIdAndUserEmail(String projectId, String email);
}
