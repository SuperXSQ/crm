package cn.xsq.crm.workbench.dao;

import cn.xsq.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkDao {

    List<ClueRemark> getListByClueId(String clueId);

    int deleteById(String id);
}
