package cn.xsq.crm.workbench.service.impl;

import cn.xsq.crm.exception.SaveException;
import cn.xsq.crm.utils.SqlSessionUtil;
import cn.xsq.crm.vo.PageVo;
import cn.xsq.crm.workbench.dao.ActivityDao;
import cn.xsq.crm.workbench.domain.Activity;
import cn.xsq.crm.workbench.service.ActivityService;

import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {

    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);

    @Override
    public Boolean save(Activity activity) throws SaveException {

        int count = activityDao.save(activity);

        if (count == 1){

            return true;
        }

        throw new SaveException("保存失败！");

    }

    @Override
    public PageVo<Activity> pageList(Map<String, Object> map) {

        //调dao层
        int total = activityDao.getTotalByCondition(map);

        List<Activity> dataList = activityDao.getActivityByCondition(map);

        PageVo vo = new PageVo();

        vo.setTotal(total);

        vo.setDataList(dataList);

        return vo;
    }
}
