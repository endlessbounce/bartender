package by.khlebnikov.bartender.filter;

import by.khlebnikov.bartender.command.CommandType;
import by.khlebnikov.bartender.constant.ConstAttribute;
import by.khlebnikov.bartender.constant.ConstParameter;
import by.khlebnikov.bartender.utility.Utility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Prevents logged users from accessing
 * directly registration, login, and some other commands.
 */
@WebFilter(urlPatterns = {"/controller"})
public class LoggedUserRedirectFilter implements Filter {
    private Logger logger = LogManager.getLogger();

    public void init(FilterConfig config) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain next)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        boolean persistentUser = Utility.isLoggedUser(httpRequest);
        boolean inSession = ((HttpServletRequest) request).getSession().getAttribute(ConstAttribute.USER_NAME) != null;
        boolean prohibitedRequest;

        logger.debug("persistent user: " + persistentUser);
        logger.debug("in-session user: " + inSession);

        if (persistentUser || inSession) {
            String action = request.getParameter(ConstParameter.COMMAND);
            if (action != null && !action.isEmpty()) {
                try {
                    CommandType commandType = CommandType.valueOf(action.toUpperCase());

                    switch (commandType) {
                        case LOGIN:
                            prohibitedRequest = true;
                            break;
                        case LOGIN_ACTION:
                            prohibitedRequest = true;
                            break;
                        case RESET:
                            prohibitedRequest = true;
                            break;
                        case RESET_ACTION:
                            prohibitedRequest = true;
                            break;
                        case REGISTER:
                            prohibitedRequest = true;
                            break;
                        case REGISTER_ACTION:
                            prohibitedRequest = true;
                            break;
                        case CONFIRM:
                            prohibitedRequest = true;
                            break;
                        case SET_NEW:
                            prohibitedRequest = true;
                            break;
                        default:
                            prohibitedRequest = false;
                            break;
                    }

                    if (prohibitedRequest) {
                        httpRequest.setAttribute(ConstAttribute.PROHIBITED, ConstParameter.TRUE);
                    }

                } catch (IllegalArgumentException e) {
                    logger.catching(e);
                }
            }
        }

        next.doFilter(request, response);
    }

    public void destroy() {
    }
}