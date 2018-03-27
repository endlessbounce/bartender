package by.khlebnikov.bartender.command;

import javax.servlet.http.HttpServletRequest;

public class LogoutCommand implements Command {
    @Override
    public String execute(HttpServletRequest content) {
        return null;
    }
}
