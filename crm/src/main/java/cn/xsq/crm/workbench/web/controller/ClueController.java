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
import cn.xsq.crm.workbench.domain.Clue;
import cn.xsq.crm.workbench.domain.Tran;
import cn.xsq.crm.workbench.service.ActivityService;
import cn.xsq.crm.workbench.service.ClueService;
import cn.xsq.crm.workbench.service.impl.ActivityServiceImpl;
import cn.xsq.crm.workbench.service.impl.ClueServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入ClueController了！");

        String path = request.getServletPath();

        if ("/workbench/clue/findAll.do".equals(path)){
            findAll(request, response);
        }

        if ("/workbench/clue/save.do".equals(path)){
            save(request, response);
        }

        if ("/workbench/clue/pageList.do".equals(path)){
            pageList(request, response);
        }

        if ("/workbench/clue/detail.do".equals(path)){
            detail(request, response);
        }

        if ("/workbench/clue/getActivityListByClueId.do".equals(path)){
            getActivityListByClueId(request, response);
        }

        if ("/workbench/clue/unbund.do".equals(path)){
            unbund(request, response);
        }

        if ("/workbench/clue/getActivityListByNameAndNotInCar.do".equals(path)){
            getActivityListByNameAndNotInCar(request,response);
        }

        if ("/workbench/clue/bund.do".equals(path)){
            bund(request, response);
        }

        if ("/workbench/clue/getActivityListByName.do".equals(path)){
            getActivityListByName(request, response);
        }

        if ("/workbench/clue/convert.do".equals(path)){
            convert(request, response);
        }

    }

    private void convert(HttpServletRequest request, HttpServletResponse response) throws IOException {

        System.out.println("进入到convert线索转换的控制器");

        String flag = request.getParameter("flag");
        String clueId = request.getParameter("clueId");
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        Tran tran = null;

        if ("y".equals(flag)){
            //此为创建交易的请求
            String id = UUIDUtil.getUUID();
            String money = request.getParameter("money");
            String name = request.getParameter("name");
            String expectedDate = request.getParameter("expectedDate");
            String stage = request.getParameter("stage");
            String activityId = request.getParameter("activityId");
            String createTime = DateTimeUtil.getSysTime();

            tran = new Tran();
            tran.setId(id);
            tran.setMoney(money);
            tran.setName(name);
            tran.setExpectedDate(expectedDate);
            tran.setStage(stage);
            tran.setActivityId(activityId);
            tran.setCreateBy(createBy);
            tran.setCreateTime(createTime);
        }

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean outFlag = cs.convert(clueId, createBy, tran);

        if (outFlag)
        response.sendRedirect(request.getContextPath()+"/workbench/clue/index.jsp");

    }

    private void getActivityListByName(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入根据市场活动名称查询列表的控制器！");

        String name = request.getParameter("name");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<Activity> list = as.getActivityListByName(name);

        PrintJson.printJsonObj(response, list);
    }

    private void bund(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("关联操作的后台");

        String cid = request.getParameter("cid");
        String aids[] = request.getParameterValues("aid");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = cs.bund(cid, aids);

        PrintJson.printJsonFlag(response, flag);
    }

    private void getActivityListByNameAndNotInCar(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("展示 添加关联市场活动模态窗口中的 未关联的市场活动列表");

        String name = request.getParameter("name");
        String clueId = request.getParameter("clueId");

        Map<String,String> map = new HashMap<>();
        map.put("name", name);
        map.put("clueId", clueId);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<Activity> list = as.getActivityListByNameAndNotInCar(map);

        PrintJson.printJsonObj(response, list);

    }

    private void unbund(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("解除关联");

        String id = request.getParameter("id");

        ClueService c = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = c.unbund(id);

        PrintJson.printJsonFlag(response, flag);

    }

    private void getActivityListByClueId(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到根据线索的id查询展示市场活动列表");

        String clueId = request.getParameter("clueId");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<Activity> aList = as.getActivityListByClueId(clueId);

        PrintJson.printJsonObj(response, aList);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入detail线索详细信息页面的控制器");

        String id = request.getParameter("id");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        Clue clue = cs.detail(id);

        request.setAttribute("clue", clue);

        request.getRequestDispatcher("/workbench/clue/detail.jsp").forward(request, response);
    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到刷新线索列表的方法控制器");

        String fullname = request.getParameter("fullname");
        String owner = request.getParameter("owner");
        String company = request.getParameter("company");
        String phone = request.getParameter("phone");
        String mphone = request.getParameter("mphone");
        String state = request.getParameter("state");
        String source = request.getParameter("source");
        int pageNo = Integer.parseInt(request.getParameter("pageNo"));
        int pageSize = Integer.parseInt(request.getParameter("pageSize"));
        int pageSkip = (pageNo-1)*pageSize;

        Map<String , Object> map = new HashMap<>();

        map.put("fullname", fullname);
        map.put("owner", owner);
        map.put("company", company);
        map.put("phone", phone);
        map.put("mphone", mphone);
        map.put("state", state);
        map.put("source", source);
        map.put("pageSize", pageSize);
        map.put("pageSkip", pageSkip);

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        PageVo<Clue> vo = cs.pageList(map);

        PrintJson.printJsonObj(response, vo);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到 保存的控制器");

        String id = UUIDUtil.getUUID();
        String fullname = request.getParameter("fullname");
        String appellation = request.getParameter("appellation");
        String owner = request.getParameter("owner");
        String company = request.getParameter("company");
        String job = request.getParameter("job");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String website = request.getParameter("website");
        String mphone = request.getParameter("mphone");
        String state = request.getParameter("state");
        String source = request.getParameter("source");
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        String address = request.getParameter("address");

        Clue c = new Clue();

        c.setId(id);
        c.setFullname(fullname);
        c.setAppellation(appellation);
        c.setOwner(owner);
        c.setCompany(company);
        c.setJob(job);
        c.setEmail(email);
        c.setPhone(phone);
        c.setWebsite(website);
        c.setMphone(mphone);
        c.setState(state);
        c.setSource(source);
        c.setCreateBy(createBy);
        c.setCreateTime(createTime);
        c.setDescription(description);
        c.setContactSummary(contactSummary);
        c.setNextContactTime(nextContactTime);
        c.setAddress(address);

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = cs.save(c);

        PrintJson.printJsonFlag(response, flag);

    }

    private void findAll(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到findAll，获取所有者列表");

        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> users = us.findAll();

        PrintJson.printJsonObj(response, users);
    }

}
