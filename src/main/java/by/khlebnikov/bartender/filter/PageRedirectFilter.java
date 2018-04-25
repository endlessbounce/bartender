package by.khlebnikov.bartender.filter;

import by.khlebnikov.bartender.constant.ConstPage;
import by.khlebnikov.bartender.constant.ConstParameter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Class which redirects all users trying to access JSPs directly from the search bar
 */
@WebFilter(urlPatterns = {"/jsp/*"},
        initParams = {@WebInitParam(name = ConstParameter.INDEX, value = ConstPage.INDEX)})
public class PageRedirectFilter implements Filter {

    // Vars ---------------------------------------------------------------------------------------
    private String indexPath;

    // Actions ------------------------------------------------------------------------------------

    /**
     * Is not implemented
     *
     * @param config
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig config) throws ServletException {
        indexPath = config.getInitParameter(ConstParameter.INDEX);
    }

    /**
     * Redirects request to the index page
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

        httpResponse.sendRedirect(httpRequest.getContextPath() + indexPath);
        next.doFilter(request, response);
    }

    /**
     * Is not implemented
     */
    @Override
    public void destroy() {
    }
}
