package cn.xsq.crm.settings.service;

import cn.xsq.crm.exception.LoginFailureException;
import cn.xsq.crm.settings.domain.User;


public interface UserService {

    User login(String loginAct, String loginPwd, String ip) throws LoginFailureException;
}
