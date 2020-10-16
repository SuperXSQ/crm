package cn.xsq.crm.settings.service.impl;

import cn.xsq.crm.settings.domain.DicType;
import cn.xsq.crm.settings.domain.DicValue;
import cn.xsq.crm.settings.service.DicService;
import cn.xsq.crm.utils.SqlSessionUtil;

public class DicServiceImpl implements DicService {

    private DicType dicType = SqlSessionUtil.getSqlSession().getMapper(DicType.class);

    private DicValue dicValue = SqlSessionUtil.getSqlSession().getMapper(DicValue.class);


}
