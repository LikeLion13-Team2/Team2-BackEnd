package com.poco.poco_backend.domain.member.repository;

import com.poco.poco_backend.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query("SELECT m FROM Member m WHERE m.email = :email")
    Optional<Member> findByEmail(@Param("email") String email);

    @Query("SELECT m.memberId FROM Member m WHERE m.email = :email")
    Optional<Long> findIdByEmail(@Param("email") String email);

    @Modifying
    @Query("DELETE FROM Member m WHERE m.email = :email")
    int deleteMemberByEmail(@Param("email") String email);

    @Modifying
    @Query("UPDATE Member m SET m.name = :newName WHERE m.email = :email")
    int updateMemberByEmail(@Param("email") String email,
                            @Param("newName") String newName);

}
