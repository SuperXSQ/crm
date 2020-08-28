package cn.xsq.crm.settings.dao;

import cn.xsq.crm.settings.domain.User;

import java.util.Map;

public interface UserDao {
    User login(Map<String,String> userMap);
}
