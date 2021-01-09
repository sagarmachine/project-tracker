package com.highbrowape.demo.repository;

import com.highbrowape.demo.dto.output.UserList;
import com.highbrowape.demo.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);


    @Query(value="select new com.highbrowape.demo.dto.output.UserList(email,firstName,lastName,addedOn,imageUrl,thumbnailUrl) from User where email like %:search% OR firstName like %:search% OR lastName like %:search% ")
    List<UserList> findEmailBySearch(String search, Pageable pageable);
    @Query(value="select count(email) from User where email like %:search% OR firstName like %:search% OR lastName like %:search% ")
    Integer countEmailBySearch(String search );


    @Query( value="select new com.highbrowape.demo.dto.output.UserList(email,firstName,lastName,addedOn,imageUrl,thumbnailUrl) from User where email like %:firstName% OR firstName like %:firstName% OR lastName like %:lastName% ")
    List<UserList> findEmailByName(String firstName,String lastName, Pageable pageable);
    @Query(value="select count(email) from User where email like %:firstName% OR (firstName like %:firstName% AND lastName like %:lastName%) ")
    Integer countEmailByName(String firstName,String lastName);

}
