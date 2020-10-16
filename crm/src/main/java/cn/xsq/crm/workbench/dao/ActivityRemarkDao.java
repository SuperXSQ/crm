package cn.xsq.crm.workbench.dao;

import cn.xsq.crm.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkDao {

    List<ActivityRemark> getRemarkListByAid(String activityId);

    int getCountByAid(String[] ids);

    int deleteByAid(String[] ids);

    int deleteById(String id);

    int saveRemark(ActivityRemark ar);

    int updateRemark(ActivityRemark ar);
}
