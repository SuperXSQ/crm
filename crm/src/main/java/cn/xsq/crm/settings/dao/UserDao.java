package cn.xsq.crm.settings.dao;

import cn.xsq.crm.settings.domain.User;

import java.util.List;
import java.util.Map;

public interface UserDao {
    User login(Map<String,String> userMap);

    List<User> findAll();
}
