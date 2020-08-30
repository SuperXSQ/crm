package cn.xsq.crm.web.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;

        Object user = request.getSession().getAttribute("user");

        String path = request.getServletPath();

        if ("/login.jsp".equals(path) || "/settings/user/login.do".equals(path)){
            chain.doFilter(req , resp);
        }else {

            if (user != null){
                chain.doFilter(req , resp);
            }else {
                //"/crm/login.jsp"  动态的 "/项目名"
                ((HttpServletResponse)resp).sendRedirect(request.getContextPath() + "/login.jsp");
            }
        }

    }
}
