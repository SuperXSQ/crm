package cn.xsq.crm.workbench.service.impl;

import cn.xsq.crm.exception.DeleteException;
import cn.xsq.crm.exception.SaveException;
import cn.xsq.crm.exception.UpdateException;
import cn.xsq.crm.utils.SqlSessionUtil;
import cn.xsq.crm.vo.PageVo;
import cn.xsq.crm.workbench.dao.ActivityDao;
import cn.xsq.crm.workbench.dao.ActivityRemarkDao;
import cn.xsq.crm.workbench.domain.Activity;
import cn.xsq.crm.workbench.domain.ActivityRemark;
import cn.xsq.crm.workbench.service.ActivityService;

import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {

    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    private ActivityRemarkDao activityRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);

    @Override
    public boolean save(Activity activity) throws SaveException {

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

    @Override
    public boolean deleteById(String[] ids) throws DeleteException {

        //删除Activity表记录时 会关联ActivityRemark（备注）表中的记录
        //所以要先删除关联表的备注记录
        //判断备注表是否删除成功 先根据id查询条数 然后判断是否与删除返回的条数相等
        int count1 = activityRemarkDao.getCountByAid(ids);

        int count2 = activityRemarkDao.deleteByAid(ids);

        if (count1 != count2){
            throw new DeleteException("删除失败 删除备注条数失败");
        }


        //然后删除Activity表的记录
        //判断是否删除成功 删除返回的条数 是否与ids数组的长度相等
        int count3 = activityDao.deleteById(ids);

        if (count3 != ids.length){
            throw new DeleteException("删除失败 删除条数与提交条数不相等");
        }

        return true;
    }

    @Override
    public Activity findByCheck(String id) {

        Activity activity = activityDao.findByCheck(id);

        return activity;
    }

    @Override
    public boolean update(Activity a) throws UpdateException{

        int count = activityDao.update(a);

        if (count != 1){
            throw new UpdateException("修改失败！");
        }

        return true;
    }

    @Override
    public Activity detail(String aid) {

        return activityDao.detail(aid);

    }

    @Override
    public List<ActivityRemark> getRemarkListByAid(String activityId) {

        return activityRemarkDao.getRemarkListByAid(activityId);

    }

    @Override
    public boolean deleteActivityRemark(String activityRemarkId) {

        boolean flag = false;

        int count = activityRemarkDao.deleteById(activityRemarkId);

        if (count == 1){
            flag = true;
        }

        return flag;
    }

    @Override
    public boolean saveRemark(ActivityRemark ar) {

        boolean flag = false;

        int count = activityRemarkDao.saveRemark(ar);

        if (count == 1){
            flag = true;
        }

        return flag;
    }


    @Override
    public boolean editActivityRemark(ActivityRemark ar) {

        boolean flag = false;

        int count = activityRemarkDao.updateRemark(ar);

        if (count == 1){
            flag = true;
        }

        return  flag;
    }
}
