package by.khlebnikov.bartender.filter;

import by.khlebnikov.bartender.constant.ConstAttribute;
import by.khlebnikov.bartender.constant.ConstParameter;
import by.khlebnikov.bartender.constant.ConstLocale;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = { "/*" })
public class LocaleFilter implements Filter {

    public void init(FilterConfig config) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain next)
            throws IOException, ServletException {
        HttpSession session = ((HttpServletRequest) request).getSession();
        String locale = (String) session.getAttribute(ConstAttribute.CHOSEN_LANGUAGE);

        if (locale == null){
            session.setAttribute(ConstParameter.LOCALE, ConstLocale.EN_US);
            session.setAttribute(ConstAttribute.CHOSEN_LANGUAGE, ConstLocale.EN);
        }

        next.doFilter(request, response);
    }

    public void destroy() {
    }
}
