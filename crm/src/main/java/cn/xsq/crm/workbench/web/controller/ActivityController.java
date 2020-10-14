package cn.xsq.crm.workbench.web.controller;

import cn.xsq.crm.exception.DeleteException;
import cn.xsq.crm.exception.SaveException;
import cn.xsq.crm.exception.UpdateException;
import cn.xsq.crm.settings.domain.User;
import cn.xsq.crm.settings.service.UserService;
import cn.xsq.crm.settings.service.impl.UserServiceImpl;
import cn.xsq.crm.utils.DateTimeUtil;
import cn.xsq.crm.utils.PrintJson;
import cn.xsq.crm.utils.ServiceFactory;
import cn.xsq.crm.utils.UUIDUtil;
import cn.xsq.crm.vo.PageVo;
import cn.xsq.crm.workbench.domain.Activity;
import cn.xsq.crm.workbench.domain.ActivityRemark;
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

        if ("/workbench/activity/delete.do".equals(path)){
            deleteById(request, response);
        }

        if("/workbench/activity/findByCheck.do".equals(path)){
            findByCheck(request, response);
        }

        if("/workbench/activity/update.do".equals(path)){
            update(request, response);
        }

        if ("/workbench/activity/detail.do".equals(path)){
            detail(request, response);
        }

        if ("/workbench/activity/getRemarkListByAid.do".equals(path)){
            getRemarkListByAid(request, response);
        }

        if ("/workbench/activity/deleteActivityRemark.do".equals(path)){
            deleteActivityRemark(request, response);
        }

        if ("/workbench/activity/addRemark.do".equals(path)){
            addRemark(request, response);
        }

        if ("/workbench/activity/getRemarkById.do".equals(path)){
            getRemarkById(request, response);
        }

        if ("/workbench/activity/editActivityRemark.do".equals(path)){
            editActivityRemark(request, response);
        }
    }

    private void editActivityRemark(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到editActivityRemark的控制器了！");

        String id = request.getParameter("id");
        String noteContent = request.getParameter("noteContent");
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        String editFlag = "1";

        ActivityRemark ar = new ActivityRemark();
        ar.setId(id);
        ar.setNoteContent(noteContent);
        ar.setEditTime(editTime);
        ar.setEditBy(editBy);
        ar.setEditFlag(editFlag);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = as.editActivityRemark(ar);

        PrintJson.printJsonFlag(response, flag);
    }

    private void getRemarkById(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到getRemarkById的控制器了");

        String id = request.getParameter("id");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        ActivityRemark ar = as.getRemarkById(id);

        PrintJson.printJsonObj(response, ar);

    }

    private void addRemark(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到addRemark的控制器");

        String id = UUIDUtil.getUUID();
        String noteContent = request.getParameter("noteContent");
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User) request.getSession().getAttribute("user")).getName();
        String editFlag = "0";
        String activityId = request.getParameter("activityId");

        ActivityRemark ar = new ActivityRemark();
        ar.setId(id);
        ar.setNoteContent(noteContent);
        ar.setCreateTime(createTime);
        ar.setCreateBy(createBy);
        ar.setEditFlag(editFlag);
        ar.setActivityId(activityId);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = as.addRemark(ar);

        Map<String,Object> m = new HashMap<>();
        m.put("success", flag);
        m.put("id", id);

        PrintJson.printJsonObj(response, m);
    }

    private void deleteActivityRemark(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到deleteActivityRemark的控制器！");

        String activityRemarkId = request.getParameter("activityRemarkId");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = as.deleteActivityRemark(activityRemarkId);

        PrintJson.printJsonFlag(response, flag);
    }

    private void getRemarkListByAid(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到了getRemarkListByAid的控制台");

        String activityId = request.getParameter("activityId");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<ActivityRemark> arList = as.getRemarkListByAid(activityId);

        PrintJson.printJsonObj(response ,arList);
    }


    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到 detail控制台了");

        String aid = request.getParameter("id");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        Activity activity = as.detail(aid);

        request.setAttribute("activity", activity);

        request.getRequestDispatcher("/workbench/activity/detail.jsp").forward(request, response);
    }

    private void update(HttpServletRequest request, HttpServletResponse response) {

        String id = request.getParameter("id");
        String owner = request.getParameter("owner"); //传的就是user的id
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        String editTime = DateTimeUtil.getSysTime(); //修改时间 是当前系统时间
        String editBy = ((User)request.getSession().getAttribute("user")).getName(); //修改人 是当前登录的用户的名字

        Activity a = new Activity();
        a.setId(id);
        a.setOwner(owner);
        a.setName(name);
        a.setStartDate(startDate);
        a.setEndDate(endDate);
        a.setCost(cost);
        a.setDescription(description);
        a.setEditTime(editTime);
        a.setEditBy(editBy);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = false;

        try {

            flag = as.update(a);

            PrintJson.printJsonFlag(response, flag);

        } catch (UpdateException e) {

            e.getMessage();

            PrintJson.printJsonFlag(response, flag);
        }

    }

    private void findByCheck(HttpServletRequest request, HttpServletResponse response) {

        //获取前端传来的id参数
        String id = request.getParameter("id");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        Activity activity = as.findByCheck(id);

        PrintJson.printJsonObj(response, activity);
    }


    private void deleteById(HttpServletRequest request, HttpServletResponse response) {

        String[] ids = request.getParameterValues("id");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = false;

        try {
            flag = as.deleteById(ids);

            PrintJson.printJsonFlag(response, flag);

        }catch (DeleteException e){

            e.getMessage();

            PrintJson.printJsonFlag(response, flag);
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
