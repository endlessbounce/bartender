package by.khlebnikov.bartender.command;

import javax.servlet.http.HttpServletResponse;

public interface CommandWithResponse {
    void setResponse(HttpServletResponse response);
}
