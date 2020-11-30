package cn.xsq.crm.workbench.service;

import cn.xsq.crm.exception.DeleteException;
import cn.xsq.crm.exception.SaveException;
import cn.xsq.crm.exception.UpdateException;
import cn.xsq.crm.vo.PageVo;
import cn.xsq.crm.workbench.domain.Activity;
import cn.xsq.crm.workbench.domain.ActivityRemark;

import java.util.List;
import java.util.Map;

public interface ActivityService {

    boolean save(Activity activity) throws SaveException;

    PageVo<Activity> pageList(Map<String, Object> map);

    boolean deleteById(String[] ids) throws DeleteException;

    Activity findByCheck(String id);

    boolean update(Activity a) throws UpdateException;

    Activity detail(String aid);

    List<ActivityRemark> getRemarkListByAid(String activityId);

    boolean deleteActivityRemark(String activityRemarkId);

    boolean saveRemark(ActivityRemark ar);

    boolean editActivityRemark(ActivityRemark ar);

    List<Activity> getActivityListByClueId(String clueId);

    List<Activity> getActivityListByNameAndNotInCar(Map<String, String> map);

    List<Activity> getActivityListByName(String name);
}
