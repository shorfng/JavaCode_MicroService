package com.loto.service;

import com.loto.dao.UsersRepository;
import com.loto.pojo.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class JdbcUserDetailsService implements UserDetailsService {
    @Autowired
    private UsersRepository usersRepository;

    /**
     * 根据 username 查询出该用户的所有信息，封装成 UserDetails 类型的对象返回，至于密码，框架会自动匹配
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = usersRepository.findByUsername(username);
        return new User(users.getUsername(), users.getPassword(), new ArrayList<>());
    }
}
