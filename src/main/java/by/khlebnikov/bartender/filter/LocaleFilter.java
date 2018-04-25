package by.khlebnikov.bartender.filter;

import by.khlebnikov.bartender.constant.ConstAttribute;
import by.khlebnikov.bartender.constant.ConstParameter;
import by.khlebnikov.bartender.constant.ConstLocale;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * WebFilter for setting default language of the application
 */
@WebFilter(urlPatterns = {"/*"})
public class LocaleFilter implements Filter {

    // Actions ------------------------------------------------------------------------------------

    /**
     * Not implemented
     *
     * @param config
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    /**
     * Sets default English language and locale of the application
     *
     * @param request
     * @param response
     * @param next
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain next)
            throws IOException, ServletException {
        HttpSession session = ((HttpServletRequest) request).getSession();
        String language = (String) session.getAttribute(ConstAttribute.CHOSEN_LANGUAGE);

        if (language == null) {
            session.setAttribute(ConstParameter.LOCALE, ConstLocale.EN_US);
            session.setAttribute(ConstAttribute.CHOSEN_LANGUAGE, ConstLocale.EN);
        }

        next.doFilter(request, response);
    }

    /**
     * Is not implemented
     */
    @Override
    public void destroy() {
    }
}
