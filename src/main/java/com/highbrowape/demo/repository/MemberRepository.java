package com.highbrowape.demo.repository;

import com.highbrowape.demo.entity.Member;
import com.highbrowape.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long> {

}
