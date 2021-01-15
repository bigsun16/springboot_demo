package com.qihui.sun.security_springboot.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SpringDataUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return User.withUsername("zhangsan").password("$2a$10$sUqmkNtoeZm4EG/tMaBl0OZ8i8MpkqnWH7Z2o8VLCSmBOx2SXhqjS").authorities("p1").build();
    }
}
