package com.zcq.dao.user;

import com.itmuch.usercenter.domain.entity.user.User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;


public interface UserMapper extends Mapper<User> {
}