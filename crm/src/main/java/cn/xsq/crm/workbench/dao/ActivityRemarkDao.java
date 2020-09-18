package cn.xsq.crm.workbench.dao;

public interface ActivityRemarkDao {

    int getCountByAid(String[] ids);

    int deleteByAid(String[] ids);
}
