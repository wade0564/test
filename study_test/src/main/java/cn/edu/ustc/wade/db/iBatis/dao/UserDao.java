package cn.edu.ustc.wade.db.iBatis.dao;

import java.util.List;

import cn.edu.ustc.wade.db.iBatis.dto.UserDto;

public interface UserDao {
    public List<UserDto> queryUsers(UserDto user) throws Exception;
}

















