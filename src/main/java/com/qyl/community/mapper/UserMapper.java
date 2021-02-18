package com.qyl.community.mapper;

import com.qyl.community.model.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    void insert(User user);
}
