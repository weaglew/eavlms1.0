package com.eagle.eavlms.service;

import com.eagle.eavlms.dao.UserDao;
import com.eagle.eavlms.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    public Page<User> getAll(Pageable pageable) {
        return userDao.findAll(pageable);
    }

    public Page<User> query(User user, Pageable pageable) {
        return userDao.findAll(Example.of(user), pageable);
    }

    public User query(User user) {
        return userDao.findOne(Example.of(user)).orElse(null);
    }

    public User getById(Integer id) {
        return userDao.findById(id).orElse(null);
    }

    public void delete(Integer id) {
        userDao.deleteById(id);
    }

    public User save(User user) {
        return userDao.save(user);
    }
}
