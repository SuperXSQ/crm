package cn.xsq.crm.workbench.service.impl;

import cn.xsq.crm.utils.DateTimeUtil;
import cn.xsq.crm.utils.SqlSessionUtil;
import cn.xsq.crm.utils.UUIDUtil;
import cn.xsq.crm.vo.PageVo;
import cn.xsq.crm.workbench.dao.CustomerDao;
import cn.xsq.crm.workbench.dao.TranDao;
import cn.xsq.crm.workbench.dao.TranHistoryDao;
import cn.xsq.crm.workbench.domain.Customer;
import cn.xsq.crm.workbench.domain.Tran;
import cn.xsq.crm.workbench.domain.TranHistory;
import cn.xsq.crm.workbench.service.TranService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranServiceImpl implements TranService {

    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);

    @Override
    public PageVo<Tran> pageList(Map<String, Object> map) {

        List<Tran> dataList = tranDao.getTranListByCondition(map);

        int total = tranDao.getTranTotalByCondition(map);


        PageVo<Tran> vo = new PageVo<>();
        vo.setTotal(total);
        vo.setDataList(dataList);

        return vo;
    }

    @Override
    public boolean saveTran(Tran tran, String customerName) {

        boolean flag = true;

        //1.验证客户名称是否存在 不存在则创建客户
        Customer customer = customerDao.getByName(customerName);

        if (customer == null){

            customer = new Customer(); //为空表示不存在客户 则创建一个
            customer.setId(UUIDUtil.getUUID());
            customer.setOwner(tran.getOwner());
            customer.setName(customerName);
            customer.setCreateBy(tran.getCreateBy());
            customer.setCreateTime(DateTimeUtil.getSysTime());
            customer.setContactSummary(tran.getContactSummary());
            customer.setNextContactTime(tran.getNextContactTime());
            customer.setDescription(tran.getDescription());

            int count1 = customerDao.save(customer);

            if (count1 != 1) flag = false;
        }

        //2.保存交易
        tran.setCustomerId(customer.getId());

        int count2 = tranDao.save(tran);

        if (count2 != 1) flag = false;

        //3.创建交易记录
        TranHistory th = new TranHistory();

        th.setId(UUIDUtil.getUUID());
        th.setStage(tran.getStage());
        th.setMoney(tran.getMoney());
        th.setExpectedDate(tran.getExpectedDate());
        th.setCreateBy(tran.getCreateBy());
        th.setCreateTime(DateTimeUtil.getSysTime());
        th.setTranId(tran.getId());

        int count3 = tranHistoryDao.save(th);

        if (count3 != 1) flag = false;

        return flag;
    }

    @Override
    public Tran detail(String id) {

        Tran t = tranDao.detail(id);

        return t;
    }

    @Override
    public List<TranHistory> getTranHistoryList(String tranId) {

        List<TranHistory> thList = tranHistoryDao.getTranHistoryList(tranId);

        return thList;
    }

    @Override
    public boolean changeStage(Tran t) {

        boolean flag = true;

        int count1 = tranDao.changeStage(t);
        if (count1 != 1) flag=false;

        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setStage(t.getStage());
        th.setMoney(t.getMoney());
        th.setExpectedDate(t.getExpectedDate());
        th.setCreateTime(t.getEditTime());
        th.setCreateBy(t.getEditBy());
        th.setTranId(t.getId());

        int count2 = tranHistoryDao.save(th);
        if (count2 != 1) flag=false;

        return flag;
    }

    @Override
    public Map<String, Object> getDataList() {

        int total = tranDao.getTotal();

        List<Map<String,String>> dataList = tranDao.getDataList();

        Map<String, Object> map = new HashMap<>();

        map.put("total", total);
        map.put("dataList", dataList);

        return map;
    }


}
