package com.example.demo.User;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User,Integer>  {
    boolean existByUserName(String userName);
    User findByUserName(String userName);
}
