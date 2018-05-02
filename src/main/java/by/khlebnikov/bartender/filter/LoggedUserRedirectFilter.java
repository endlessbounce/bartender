package by.khlebnikov.bartender.filter;

import by.khlebnikov.bartender.command.CommandType;
import by.khlebnikov.bartender.constant.ConstAttribute;
import by.khlebnikov.bartender.constant.ConstPage;
import by.khlebnikov.bartender.constant.ConstParameter;
import by.khlebnikov.bartender.utility.CookieHandler;
import by.khlebnikov.bartender.validator.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Class which redirects logged users trying to access prohibited pages
 */
@WebFilter(urlPatterns = {"/controller"})
public class LoggedUserRedirectFilter implements Filter {

    // Vars ---------------------------------------------------------------------------------------
    private static Logger logger = LogManager.getLogger();

    // Actions ------------------------------------------------------------------------------------

    /**
     * Is not implemented
     *
     * @param config
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    /**
     * Prevents logged users from accessing
     * directly registration, login, and some other commands.
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
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        boolean persistentUser = CookieHandler.isLoggedUser(httpRequest);
        boolean inSession = ((HttpServletRequest) request).getSession().getAttribute(ConstAttribute.USER_NAME) != null;
        boolean prohibitedRequest;

        logger.debug("persistent user: " + persistentUser + "\nin-session user: " + inSession);

        if (persistentUser || inSession) {
            String action = request.getParameter(ConstParameter.COMMAND);
            if (Validator.checkString(action)) {
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
                        httpResponse.sendRedirect(httpRequest.getContextPath() + ConstPage.INDEX);
                        logger.debug("redirecting prohibited request from logged user");
                        return;
                    }

                } catch (IllegalArgumentException e) {
                    logger.catching(e);
                }
            }
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