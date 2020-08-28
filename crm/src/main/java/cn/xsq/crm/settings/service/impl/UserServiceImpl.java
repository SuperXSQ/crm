package cn.xsq.crm.settings.service.impl;

import cn.xsq.crm.exception.LoginFailureException;
import cn.xsq.crm.settings.dao.UserDao;
import cn.xsq.crm.settings.domain.User;
import cn.xsq.crm.settings.service.UserService;
import cn.xsq.crm.utils.DateTimeUtil;
import cn.xsq.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.Map;

public class UserServiceImpl implements UserService {

    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);



    public User login(String loginAct, String loginPwd, String ip) throws LoginFailureException {

        Map<String,String> userMap = new HashMap<>();
        userMap.put("loginAct", loginAct);
        userMap.put("loginPwd", loginPwd);

        User user = userDao.login(userMap);

        //如果没查到数据
        if (user == null){
            throw new LoginFailureException("您输入的用户名或密码错误！");
        }

        //到此处说明不为空，那么验证失效时间，锁定状态和访问ip
        String expireTime = user.getExpireTime();//失效时间
        String lockState = user.getLockState();//锁定状态
        String allowIps = user.getAllowIps();//访问ip

        //系统当前时间
        String nowTime = DateTimeUtil.getSysTime();

        if (expireTime.compareTo(nowTime) < 0){
            throw new LoginFailureException("您的账号已失效！");
        }else if ("0".equals(lockState)){
            throw new LoginFailureException("您的账号已锁定");
        }else if (! allowIps.contains(ip)){
            throw new LoginFailureException("您的ip地址不合法！");
        }

        return user;
    }
}
