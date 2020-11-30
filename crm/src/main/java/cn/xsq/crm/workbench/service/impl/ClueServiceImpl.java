package cn.xsq.crm.workbench.service.impl;

import cn.xsq.crm.utils.DateTimeUtil;
import cn.xsq.crm.utils.SqlSessionUtil;
import cn.xsq.crm.utils.UUIDUtil;
import cn.xsq.crm.vo.PageVo;
import cn.xsq.crm.workbench.dao.*;
import cn.xsq.crm.workbench.domain.*;
import cn.xsq.crm.workbench.service.ClueService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueServiceImpl implements ClueService {

    private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    private ClueActivityRelationDao clueActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    private ClueRemarkDao clueRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);
    private CustomerRemarkDao customerRemarkDao = SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);
    private ContactsRemarkDao contactsRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
    private ContactsActivityRelationDao contactsActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);
    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);

    @Override
    public boolean save(Clue c) {

        boolean flag = false;

        int count = clueDao.save(c);

        if (count == 1){
            flag = true;
        }

        return flag;
    }

    @Override
    public PageVo<Clue> pageList(Map<String, Object> map) {

        List<Clue> list = clueDao.getClueListByCondition(map);

        int total = clueDao.getClueTotalByCondition(map);

        PageVo<Clue> vo = new PageVo<>();

        vo.setDataList(list);
        vo.setTotal(total);

        return vo;
    }

    @Override
    public Clue detail(String id) {

        Clue clue = clueDao.detail(id);

        return clue;
    }

    @Override
    public boolean unbund(String id) {

        boolean flag = false;

        int count = clueActivityRelationDao.deleteById(id);

        if (count == 1){
            flag = true;
        }

        return flag;
    }

    @Override
    public boolean bund(String cid, String[] aids) {

        boolean flag = true;

        for (String aid : aids) {

            ClueActivityRelation car = new ClueActivityRelation();
            car.setId(UUIDUtil.getUUID());
            car.setClueId(cid);
            car.setActivityId(aid);

            int count = clueActivityRelationDao.save(car);

            if (count != 1){
                return false;
            }
        }

        return flag;
    }

    @Override
    public boolean convert(String clueId, String createBy, Tran tran) {

        boolean flag = true;

        //(1) 获取到线索id，通过线索id获取线索对象（线索对象当中封装了线索的信息）
        Clue clue = clueDao.getById(clueId);

        //(2) 通过线索对象提取客户信息，当该客户不存在的时候，新建客户（根据公司的名称精确匹配，判断该客户是否存在！）
        String company = clue.getCompany();

        Customer customer = customerDao.getByName(company);

        if (customer == null){
            customer = new Customer();

            customer.setId(UUIDUtil.getUUID());
            customer.setOwner(clue.getOwner());
            customer.setName(company);
            customer.setWebsite(clue.getWebsite());
            customer.setPhone(clue.getPhone());
            customer.setCreateBy(createBy);
            customer.setCreateTime(DateTimeUtil.getSysTime());
            customer.setContactSummary(clue.getContactSummary());
            customer.setNextContactTime(clue.getNextContactTime());
            customer.setDescription(clue.getDescription());
            customer.setAddress(clue.getAddress());

            int count = customerDao.save(customer);

            if (count != 1) flag = false;
        }


        //(3) 通过线索对象提取联系人信息，保存联系人
        Contacts contacts = new Contacts();

        contacts.setId(UUIDUtil.getUUID());
        contacts.setOwner(clue.getOwner());
        contacts.setSource(clue.getSource());
        contacts.setCustomerId(customer.getId());
        contacts.setFullname(clue.getFullname());
        contacts.setAppellation(clue.getAppellation());
        contacts.setEmail(clue.getEmail());
        contacts.setMphone(clue.getMphone());
        contacts.setJob(clue.getJob());
        contacts.setCreateBy(createBy);
        contacts.setCreateTime(DateTimeUtil.getSysTime());
        contacts.setDescription(clue.getDescription());
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setAddress(clue.getAddress());

        int count = contactsDao.save(contacts);

        if (count != 1) flag = false;


        //(4) 线索备注转换到客户备注以及联系人备注
        List<ClueRemark> clueRemarkList = clueRemarkDao.getListByClueId(clueId);
        //取得list中的noteContent备注信息

        for (ClueRemark cr : clueRemarkList) {

            String noteContent = cr.getNoteContent();

            CustomerRemark customerRemark = new CustomerRemark();
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setNoteContent(noteContent);
            customerRemark.setCreateTime(DateTimeUtil.getSysTime());
            customerRemark.setCreateBy(createBy);
            customerRemark.setEditFlag("0");
            customerRemark.setCustomerId(customer.getId());

            int count2 = customerRemarkDao.save(customerRemark);

            if (count2 != 1) flag = false;


            ContactsRemark contactsRemark = new ContactsRemark();
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setNoteContent(noteContent);
            contactsRemark.setCreateBy(createBy);
            contactsRemark.setCreateTime(DateTimeUtil.getSysTime());
            contactsRemark.setEditFlag("0");
            contactsRemark.setContactsId(contacts.getId());

            int count3 = contactsRemarkDao.save(contactsRemark);

            if (count3 != 1) flag = false;
        }


        //(5) “线索和市场活动”的关系转换到“联系人和市场活动”的关系
        //这里也是返回一个list  然后取list中的activityId
        List<ClueActivityRelation> ClueActivityRelationList = clueActivityRelationDao.getListByclueId(clueId);

        for (ClueActivityRelation c : ClueActivityRelationList) {

            ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setActivityId(c.getActivityId());
            contactsActivityRelation.setContactsId(contacts.getId());

            int count4 = contactsActivityRelationDao.save(contactsActivityRelation);

            if (count4 != 1) flag = false;
        }



        //(6) 如果有创建交易需求，创建一条交易
        if (tran != null){

            //tran在controller中已经封装了一些信息
            tran.setOwner(clue.getOwner());
            tran.setCustomerId(customer.getId());
            tran.setSource(clue.getSource());
            tran.setContactsId(contacts.getId());
            tran.setDescription(clue.getDescription());
            tran.setContactSummary(clue.getContactSummary());
            tran.setNextContactTime(clue.getNextContactTime());

            int count5 = tranDao.save(tran);

            if (count5 != 1) flag = false;


            //(7) 如果创建了交易，则创建一条该交易下的交易历史
            TranHistory tranHistory = new TranHistory();
            tranHistory.setId(UUIDUtil.getUUID());
            tranHistory.setStage(tran.getStage());
            tranHistory.setMoney(tran.getMoney());
            tranHistory.setExpectedDate(tran.getExpectedDate());
            tranHistory.setCreateTime(DateTimeUtil.getSysTime());
            tranHistory.setCreateBy(createBy);
            tranHistory.setTranId(tran.getId());

            int count6 = tranHistoryDao.save(tranHistory);

            if (count6 != 1) flag = false;
        }


        //(8) 删除线索备注
        for (ClueRemark cr : clueRemarkList) {

            int count7 = clueRemarkDao.deleteById(cr.getId());

            if (count7 != 1) flag = false;
        }


        //(9) 删除线索和市场活动的关系

        for (ClueActivityRelation c : ClueActivityRelationList) {

            int count8 = clueActivityRelationDao.deleteById(c.getId());

            if (count8 != 1) flag = false;

        }



        //(10) 删除线索
        int count9 = clueDao.deleteById(clueId);

        if (count9 != 1) flag = false;

        return flag;
    }
}
