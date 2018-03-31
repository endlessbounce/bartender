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
        String locale = (String) session.getAttribute(Constant.CHOSEN_LOCALE);

        if (locale == null){
            session.setAttribute(Constant.LOCALE, Constant.EN_US);
            session.setAttribute(Constant.CHOSEN_LOCALE, Constant.EN);
        }

        next.doFilter(request, response);
    }

    public void destroy() {
    }
}
