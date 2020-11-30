package cn.xsq.crm.workbench.dao;

import cn.xsq.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueDao {


    int save(Clue c);

    List<Clue> getClueListByCondition(Map<String, Object> map);

    int getClueTotalByCondition(Map<String, Object> map);

    Clue detail(String id);

    Clue getById(String clueId);

    int deleteById(String clueId);
}
