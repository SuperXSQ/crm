package cn.xsq.crm.workbench.service.impl;

import cn.xsq.crm.utils.SqlSessionUtil;
import cn.xsq.crm.workbench.dao.CustomerDao;
import cn.xsq.crm.workbench.service.CustomerService;

import java.util.List;

public class CustomerServiceImpl implements CustomerService {


    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);


    public List<String> getCustomerName(String name) {

        List<String> sList = customerDao.getCustomerName(name);

        return sList;
    }
}
