package cn.xsq.crm.workbench.service;

import cn.xsq.crm.exception.SaveException;
import cn.xsq.crm.vo.PageVo;
import cn.xsq.crm.workbench.domain.Activity;

import java.util.Map;

public interface ActivityService {

    Boolean save(Activity activity) throws SaveException;

    PageVo<Activity> pageList(Map<String, Object> map);
}
