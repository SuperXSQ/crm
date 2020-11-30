package cn.xsq.crm.workbench.dao;

import cn.xsq.crm.workbench.domain.Contacts;

import java.util.List;

public interface ContactsDao {

    int save(Contacts contacts);

    List<Contacts> getContactsListByFullname(String fullname);
}
