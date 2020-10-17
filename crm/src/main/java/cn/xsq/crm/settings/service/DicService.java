package cn.xsq.crm.settings.service;

import cn.xsq.crm.settings.domain.DicValue;

import java.util.List;
import java.util.Map;

public interface DicService {
    Map<String, List<DicValue>> getDicMap();
}
