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
 * Prevents unlogged users from accessing
 * directly profile, settings, and some other commands.
 */
@WebFilter(urlPatterns = {"/controller"})
public class UnloggedUserRedirectFilter implements Filter {

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
     * Redirects unlogged users trying to access resources that are allowed only for logged users
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

        logger.debug("persistentUser user: " + persistentUser + "\ninSession user: " + inSession);

        if (!persistentUser && !inSession) {
            String action = request.getParameter(ConstParameter.COMMAND);
            if (Validator.checkString(action)) {
                try {
                    CommandType commandType = CommandType.valueOf(action.toUpperCase());

                    switch (commandType) {
                        case LOGOUT:
                            prohibitedRequest = true;
                            break;
                        case PROFILE:
                            prohibitedRequest = true;
                            break;
                        case SETTINGS:
                            prohibitedRequest = true;
                            break;
                        case USER_COCKTAIL:
                            prohibitedRequest = true;
                            break;
                        default:
                            prohibitedRequest = false;
                            break;
                    }

                    if (prohibitedRequest) {
                        httpResponse.sendRedirect(httpRequest.getContextPath() + ConstPage.INDEX);
                        logger.debug("redirecting prohibited request from unlogged user");
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
