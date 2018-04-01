package by.khlebnikov.bartender.filter;

import by.khlebnikov.bartender.constant.Constant;
import by.khlebnikov.bartender.entity.User;
import by.khlebnikov.bartender.logic.UserService;
import by.khlebnikov.bartender.utility.Utility;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebFilter(urlPatterns = { "/*" })
public class LoggedCookieFilter implements Filter {

    public void init(FilterConfig config) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain next)
            throws IOException, ServletException {
        HttpServletRequest req = ((HttpServletRequest) request);
        HttpServletResponse resp = ((HttpServletResponse) response);

        Cookie[] cookieArr = req.getCookies();

        if (cookieArr != null) {
            Optional<Cookie> loggedCookieOpt = Utility.getCookie(cookieArr, Constant.STAY_LOGGED);
            Optional<Cookie> oldSessionCookieOpt = Utility.getCookie(cookieArr, Constant.OLD_SESSION);

            //if user is logged and he visits the app for the first time - fetch data from the database
            if (loggedCookieOpt.isPresent() && !oldSessionCookieOpt.isPresent()) {
                List<User> userList = new UserService().findUserByCookie(loggedCookieOpt.get().getValue());

                if (!userList.isEmpty()) {
                    String name = userList.get(0).getName();
                    req.getSession().setAttribute(Constant.USER_NAME, name);
                }

                resp.addCookie(new Cookie(Constant.OLD_SESSION, Constant.TRUE));
            }
        }

        next.doFilter(request, response);
    }

    public void destroy() {
    }
}