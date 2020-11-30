package cn.xsq.crm.workbench.service.impl;

import cn.xsq.crm.utils.SqlSessionUtil;
import cn.xsq.crm.workbench.dao.ContactsDao;
import cn.xsq.crm.workbench.domain.Contacts;
import cn.xsq.crm.workbench.service.ContactsService;

import java.util.List;

public class ContactsServiceImpl implements ContactsService {

    private ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);


    public List<Contacts> getContactsList(String fullname) {

        List<Contacts> cList = contactsDao.getContactsListByFullname(fullname);

        return cList;
    }
}
