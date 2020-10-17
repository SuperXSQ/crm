package cn.xsq.crm.web.listener;

import cn.xsq.crm.settings.domain.DicValue;
import cn.xsq.crm.settings.service.DicService;
import cn.xsq.crm.settings.service.impl.DicServiceImpl;
import cn.xsq.crm.utils.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;
import java.util.Map;
import java.util.Set;


/*
    SysInitListener 系统初始化监听器

    ServletContextListener 要监听上下文域对象 就实现上下文监听器接口
 */

public class SysInitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {

        //event 参数可以获取监听的对象
        ServletContext application = event.getServletContext();

        //调service层获取数据字典
        DicService ds = (DicService) ServiceFactory.getService(new DicServiceImpl());


        /*
            在业务层中  应该将多个数据字典的值保存到一个map中
            Map<String , List<DicValue>> m = new HashMap<>;
            m.put("appellationList",DicValue1);
            m.put("clueStateList",DicValue2);
            ...
            ...
         */
        Map<String, List<DicValue>> map = ds.getDicMap();

        /*
            拿到数据字典的map之后 还要把map拆解 然后保存到上下文域对象中去
         */
        //取出map所有的key
        Set<String> set = map.keySet();
        //然后通过set取出每一个key，然后通过key取得map的value
        for (String key : set) {

            application.setAttribute(key, map.get(key));
        }
    }
}
