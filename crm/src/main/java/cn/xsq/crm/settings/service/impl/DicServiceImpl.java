package cn.xsq.crm.settings.service.impl;

import cn.xsq.crm.settings.dao.DicTypeDao;
import cn.xsq.crm.settings.dao.DicValueDao;
import cn.xsq.crm.settings.domain.DicType;
import cn.xsq.crm.settings.domain.DicValue;
import cn.xsq.crm.settings.service.DicService;
import cn.xsq.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DicServiceImpl implements DicService {

    private DicTypeDao dicTypeDao = SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);

    private DicValueDao dicValueDao = SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);


    @Override
    public Map<String, List<DicValue>> getDicMap() {

        Map<String, List<DicValue>> map = new HashMap<>();

        List<DicType> dicTypes = dicTypeDao.getAll();

        for (DicType d : dicTypes) {

            List<DicValue> dicValues = dicValueDao.getByCode(d.getCode());

            map.put(d.getCode()+"List" , dicValues);

        }

        return map;
    }
}
