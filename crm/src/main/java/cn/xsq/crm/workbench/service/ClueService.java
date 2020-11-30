package cn.xsq.crm.workbench.service;

import cn.xsq.crm.vo.PageVo;
import cn.xsq.crm.workbench.domain.Clue;
import cn.xsq.crm.workbench.domain.Tran;

import java.util.Map;

public interface ClueService {
    boolean save(Clue c);

    PageVo<Clue> pageList(Map<String, Object> map);

    Clue detail(String id);

    boolean unbund(String id);

    boolean bund(String cid, String[] aids);

    boolean convert(String clueId, String createBy, Tran tran);
}
