package com.eagle.eavlms.dao;

import com.eagle.eavlms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User,Integer> {
}