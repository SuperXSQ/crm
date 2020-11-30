package cn.xsq.crm.workbench.dao;

import cn.xsq.crm.workbench.domain.ClueActivityRelation;

import java.util.List;


public interface ClueActivityRelationDao {


    int deleteById(String id);

    int save(ClueActivityRelation car);

    List<ClueActivityRelation> getListByclueId(String clueId);
}
