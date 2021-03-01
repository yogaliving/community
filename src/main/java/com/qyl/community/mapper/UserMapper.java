package com.qyl.community.mapper;

import com.qyl.community.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    void insert(User user);

    User findByToken(@Param("token") String token);
}
