package by.khlebnikov.bartender.filter;

import by.khlebnikov.bartender.constant.Constant;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LocaleFilter implements Filter {

    public void init(FilterConfig config) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain next)
            throws IOException, ServletException {
        HttpSession session = ((HttpServletRequest) request).getSession();
        String locale = (String) session.getAttribute("ChosenLocale");

        if (locale == null){
            session.setAttribute("locale", Constant.EN_US);
            session.setAttribute("ChosenLocale", Constant.EN);
        }

        next.doFilter(request, response);
    }

    public void destroy() {
    }
}
