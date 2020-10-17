package cn.xsq.crm.settings.dao;

import cn.xsq.crm.settings.domain.DicValue;

import java.util.List;

public interface DicValueDao {
    List<DicValue> getByCode(String code);
}
