package cn.xsq.crm.workbench.web.controller;

import cn.xsq.crm.exception.SaveException;
import cn.xsq.crm.settings.domain.User;
import cn.xsq.crm.settings.service.UserService;
import cn.xsq.crm.settings.service.impl.UserServiceImpl;
import cn.xsq.crm.utils.DateTimeUtil;
import cn.xsq.crm.utils.PrintJson;
import cn.xsq.crm.utils.ServiceFactory;
import cn.xsq.crm.utils.UUIDUtil;
import cn.xsq.crm.vo.PageVo;
import cn.xsq.crm.workbench.domain.Activity;
import cn.xsq.crm.workbench.service.ActivityService;
import cn.xsq.crm.workbench.service.impl.ActivityServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入ActivityController了！");

        String path = request.getServletPath();

        if ("/workbench/activity/findAll.do".equals(path)){

            findAll(request, response);
        }

        if ("/workbench/activity/save.do".equals(path)){
            save(request, response);
        }

        if ("/workbench/activity/pageList.do".equals(path)){
            pageList(request, response);
        }
    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {

        String name = request.getParameter("name");
        String owner = request.getParameter("owner"); //这一项是所有者的名字，在Activity表中owner字段应该是用户的id，所以不能用set到domain
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        int pageNo = Integer.parseInt(request.getParameter("pageNo"));
        int pageSize = Integer.parseInt(request.getParameter("pageSize")); //每页显示的条数
        int pageSkip = (pageNo-1)*pageSize; //略过的条数

        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("owner", owner);
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        map.put("pageSkip", pageSkip);
        map.put("pageSize", pageSize);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        PageVo<Activity> pageVo = as.pageList(map);

        PrintJson.printJsonObj(response, pageVo);

    }

    private void save(HttpServletRequest request, HttpServletResponse response) {

        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();

        Activity activity = new Activity();

        activity.setId(id);
        activity.setOwner(owner);
        activity.setName(name);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setCost(cost);
        activity.setDescription(description);
        activity.setCreateTime(createTime);
        activity.setCreateBy(createBy);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        try{

            Boolean flag = as.save(activity);

            PrintJson.printJsonFlag(response, flag);

        }catch (SaveException e){

            String msg = e.getMessage();

            Map<String , Object> sMap = new HashMap<>();
            sMap.put("success" , false);
            sMap.put("s" , msg);

            PrintJson.printJsonObj(response, sMap);
        }
    }


    private void findAll(HttpServletRequest request, HttpServletResponse response) {

        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> users = us.findAll();

        PrintJson.printJsonObj(response , users);
    }
}
