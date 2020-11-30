package cn.xsq.crm.workbench.web.controller;


import cn.xsq.crm.settings.domain.User;
import cn.xsq.crm.settings.service.UserService;
import cn.xsq.crm.settings.service.impl.UserServiceImpl;
import cn.xsq.crm.utils.DateTimeUtil;
import cn.xsq.crm.utils.PrintJson;
import cn.xsq.crm.utils.ServiceFactory;
import cn.xsq.crm.utils.UUIDUtil;
import cn.xsq.crm.vo.PageVo;
import cn.xsq.crm.workbench.domain.Activity;
import cn.xsq.crm.workbench.domain.Contacts;
import cn.xsq.crm.workbench.domain.Tran;
import cn.xsq.crm.workbench.domain.TranHistory;
import cn.xsq.crm.workbench.service.ActivityService;
import cn.xsq.crm.workbench.service.ContactsService;
import cn.xsq.crm.workbench.service.CustomerService;
import cn.xsq.crm.workbench.service.TranService;
import cn.xsq.crm.workbench.service.impl.ActivityServiceImpl;
import cn.xsq.crm.workbench.service.impl.ContactsServiceImpl;
import cn.xsq.crm.workbench.service.impl.CustomerServiceImpl;
import cn.xsq.crm.workbench.service.impl.TranServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入TranController了！");

        String path = request.getServletPath();

        if ("/workbench/tran/pageList.do".equals(path)) {
            pageList(request, response);
        }

        if ("/workbench/tran/save.do".equals(path)){
            save(request, response);
        }

        if ("/workbench/tran/getActivityList.do".equals(path)){
            getActivityList(request, response);
        }

        if ("/workbench/tran/getContactsList.do".equals(path)){
            getContactsList(request, response);
        }

        if ("/workbench/tran/getCustomerName.do".equals(path)){
            getCustomerName(request, response);
        }

        if ("/workbench/tran/saveTran.do".equals(path)){
            saveTran(request, response);
        }

        if ("/workbench/tran/detail.do".equals(path)){
            detail(request, response);
        }

        if ("/workbench/tran/getTranHistoryList.do".equals(path)){
            getTranHistoryList(request, response);
        }

        if ("/workbench/tran/changeStage.do".equals(path)){
            changeStage(request, response);
        }

        if ("/workbench/tran/getDataList.do".equals(path)){
            getDataList(request, response);
        }
    }

    private void getDataList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("取得统计图标所需的数据");

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        Map<String, Object> map = ts.getDataList();

        PrintJson.printJsonObj(response, map);
    }

    private void changeStage(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("点击图标更改交易阶段");

        String id = request.getParameter("id");
        String stage = request.getParameter("stage");
        String money = request.getParameter("money");
        String expectedDate = request.getParameter("expectedDate");
        Tran t = new Tran();
        t.setId(id);
        t.setStage(stage);
        t.setEditBy(((User)request.getSession().getAttribute("user")).getName());
        t.setEditTime(DateTimeUtil.getSysTime());
        t.setMoney(money);
        t.setExpectedDate(expectedDate);

        Map<String,String> pMap = (Map<String, String>) request.getServletContext().getAttribute("pMap");
        String possibility = pMap.get(stage);
        t.setPossibility(possibility);

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
        boolean flag = ts.changeStage(t);

        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("tran",t);

        PrintJson.printJsonObj(response, map);
    }

    private void getTranHistoryList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("展现交易详情页中的交易历史记录");

        String tranId = request.getParameter("tranId");

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        List<TranHistory> thList = ts.getTranHistoryList(tranId);

        Map<String,String> pMap = (Map<String, String>) request.getServletContext().getAttribute("pMap");

        for (TranHistory th:thList) {
            String stage = th.getStage();
            String possibility = pMap.get(stage);
            th.setPossibility(possibility);
        }

        PrintJson.printJsonObj(response, thList);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入交易详情页");

        String id = request.getParameter("id");

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        Tran t = ts.detail(id);


        String stage = t.getStage();
        Map<String,String> pMap = (Map<String, String>) request.getServletContext().getAttribute("pMap");
        String possibility = pMap.get(stage);
        t.setPossibility(possibility);

        request.setAttribute("t", t);

        request.getRequestDispatcher("/workbench/transaction/detail.jsp").forward(request, response);

    }

    private void saveTran(HttpServletRequest request, HttpServletResponse response) throws IOException {

        System.out.println("提交表单 保存交易");

        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String money = request.getParameter("money");
        String name = request.getParameter("name");
        String expectedDate = request.getParameter("expectedDate");
        String customerName = request.getParameter("customerName");
        String stage = request.getParameter("stage");
        String type = request.getParameter("type");
        String source = request.getParameter("source");
        String activityId = request.getParameter("activityId");
        String contactsId = request.getParameter("contactsId");
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");

        Tran tran = new Tran();

        tran.setId(id);
        tran.setOwner(owner);
        tran.setMoney(money);
        tran.setName(name);
        tran.setExpectedDate(expectedDate);
        tran.setStage(stage);
        tran.setType(type);
        tran.setSource(source);
        tran.setActivityId(activityId);
        tran.setContactsId(contactsId);
        tran.setCreateBy(createBy);
        tran.setCreateTime(createTime);
        tran.setDescription(description);
        tran.setContactSummary(contactSummary);
        tran.setNextContactTime(nextContactTime);

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        boolean flag = ts.saveTran(tran, customerName);

        if (flag) response.sendRedirect(request.getContextPath() + "/workbench/transaction/index.jsp");
    }

    private void getCustomerName(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到客户名称自动补全");

        String name = request.getParameter("name");

        CustomerService ts = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        List<String> sList = ts.getCustomerName(name);

        PrintJson.printJsonObj(response, sList);
    }

    private void getContactsList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到展现联系人列表");

        String fullname = request.getParameter("fullname");

        ContactsService cs = (ContactsService) ServiceFactory.getService(new ContactsServiceImpl());

        List<Contacts> cList = cs.getContactsList(fullname);

        PrintJson.printJsonObj(response, cList);
    }

    private void getActivityList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到展现市场活动列表");

        String name = request.getParameter("name");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<Activity> aList = as.getActivityListByName(name);

        PrintJson.printJsonObj(response, aList);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("创建交易之前过后台取得所有者列表");

        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> userList = us.findAll();

        request.getSession().setAttribute("userList", userList);

        request.getRequestDispatcher("/workbench/transaction/save.jsp").forward(request, response);

    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到展现交易列表的控制器");

        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String customerName = request.getParameter("customerName");
        String stage = request.getParameter("stage");
        String transactionType = request.getParameter("transactionType");
        String source = request.getParameter("source");
        String contactsName = request.getParameter("contactsName");
        int pageNo = Integer.parseInt(request.getParameter("pageNo"));
        int pageSize = Integer.parseInt(request.getParameter("pageSize"));
        int pageSkip = (pageNo - 1) * pageSize;

        Map<String, Object> map = new HashMap<>();
        map.put("owner", owner);
        map.put("name", name);
        map.put("customerName", customerName);
        map.put("stage", stage);
        map.put("transactionType", transactionType);
        map.put("source", source);
        map.put("contactsName", contactsName);
        map.put("pageSize", pageSize);
        map.put("pageSkip", pageSkip);

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        PageVo<Tran> vo = ts.pageList(map);

        PrintJson.printJsonObj(response, vo);
    }
}