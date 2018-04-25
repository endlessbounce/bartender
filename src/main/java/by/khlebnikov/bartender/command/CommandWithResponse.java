package by.khlebnikov.bartender.command;

import javax.servlet.http.HttpServletResponse;

/**
 * Interface providing a way for a class to get an instance of response.
 */
public interface CommandWithResponse {

    /**
     * A set method to inject an instance of response to a command class.
     * This might be necessary to work with cookies.
     *
     * @param response
     */
    void setResponse(HttpServletResponse response);
}
