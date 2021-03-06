package cn.xsq.crm.workbench.dao;

import cn.xsq.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityDao {
    int save(Activity activity);

    int getTotalByCondition(Map<String, Object> map);

    List<Activity> getActivityByCondition(Map<String, Object> map);

    int deleteById(String[] ids);

    Activity findByCheck(String id);

    int update(Activity a);

    Activity detail(String aid);

    List<Activity> getActivityListByClueId(String clueId);

    List<Activity> getActivityListByNameAndNotInCar(Map<String, String> map);

    List<Activity> getActivityListByName(String name);
}
