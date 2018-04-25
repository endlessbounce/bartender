package by.khlebnikov.bartender.filter;

import by.khlebnikov.bartender.constant.Constant;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import java.io.IOException;

/**
 * WebFilter for encoding monitoring
 */
@WebFilter(urlPatterns = {"/*"},
        initParams = {@WebInitParam(name = Constant.ENCODING, value = Constant.UTF8)})
public class CharsetFilter implements Filter {

    // Vars ---------------------------------------------------------------------------------------
    private String encoding;


    // Actions ------------------------------------------------------------------------------------

    /**
     * Sets encoding to UTF8 if it's not set
     *
     * @param config
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig config) throws ServletException {
        encoding = config.getInitParameter(Constant.ENCODING);
        if (encoding == null) encoding = Constant.UTF8;
    }

    /**
     * Sets default character encoding and content type
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
        // Respect the client-specified character encoding
        // (see HTTP specification section 3.4.1)
        if (request.getCharacterEncoding() == null) {
            request.setCharacterEncoding(encoding);
        }

        // Set the default response content type and encoding
        response.setContentType(Constant.CONTENT_TYPE);
        response.setCharacterEncoding(Constant.UTF8);

        next.doFilter(request, response);
    }

    /**
     * Is not implemented
     */
    @Override
    public void destroy() {
    }
}
