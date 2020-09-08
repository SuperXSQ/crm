package cn.xsq.crm.workbench.dao;

import cn.xsq.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityDao {
    int save(Activity activity);

    int getTotalByCondition(Map<String, Object> map);

    List<Activity> getActivityByCondition(Map<String, Object> map);
}
