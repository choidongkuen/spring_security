package com.example.spring_security.domain.repository;

import com.example.spring_security.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long> {
}
