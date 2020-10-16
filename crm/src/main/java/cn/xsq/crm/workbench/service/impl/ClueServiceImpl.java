package cn.xsq.crm.workbench.service.impl;

import cn.xsq.crm.utils.SqlSessionUtil;
import cn.xsq.crm.workbench.dao.ClueDao;
import cn.xsq.crm.workbench.service.ClueService;

public class ClueServiceImpl implements ClueService {

    private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
}
