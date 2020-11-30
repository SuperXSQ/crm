package cn.xsq.crm.workbench.dao;

import cn.xsq.crm.workbench.domain.Contacts;
import cn.xsq.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface TranDao {

    int save(Tran tran);

    List<Tran> getTranListByCondition(Map<String, Object> map);

    int getTranTotalByCondition(Map<String, Object> map);

    Tran detail(String id);

    int changeStage(Tran t);

    int getTotal();

    List<Map<String, String>> getDataList();
}
