package cn.xsq.crm.workbench.service;

import cn.xsq.crm.vo.PageVo;
import cn.xsq.crm.workbench.domain.Tran;
import cn.xsq.crm.workbench.domain.TranHistory;

import java.util.List;
import java.util.Map;

public interface TranService {

    PageVo<Tran> pageList(Map<String, Object> map);

    boolean saveTran(Tran tran, String customerName);

    Tran detail(String id);

    List<TranHistory> getTranHistoryList(String tranId);

    boolean changeStage(Tran t);

    Map<String, Object> getDataList();
}
