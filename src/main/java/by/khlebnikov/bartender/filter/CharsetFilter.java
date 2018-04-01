package by.khlebnikov.bartender.filter;

import by.khlebnikov.bartender.constant.Constant;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import java.io.IOException;

@WebFilter(urlPatterns = { "/*" },
           initParams = {@WebInitParam(name = Constant.ENCODING,
                                       value = Constant.UTF8) })
public class CharsetFilter implements Filter {
    private String encoding;

    public void init(FilterConfig config) throws ServletException {
        encoding = config.getInitParameter(Constant.ENCODING);
        if (encoding == null) encoding = Constant.UTF8;
    }

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

    public void destroy() {
    }
}
