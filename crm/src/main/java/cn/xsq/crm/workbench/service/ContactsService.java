package cn.xsq.crm.workbench.service;

import cn.xsq.crm.workbench.domain.Contacts;

import java.util.List;

public interface ContactsService {

    List<Contacts> getContactsList(String fullname);
}
