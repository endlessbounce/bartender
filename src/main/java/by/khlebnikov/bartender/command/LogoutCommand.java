package by.khlebnikov.bartender.command;

import by.khlebnikov.bartender.manager.PropertyManager;

import javax.servlet.http.HttpServletRequest;

public class LogoutCommand implements Command {
    @Override
    public String execute(HttpServletRequest request) {
        request.getSession().invalidate();
        return PropertyManager.getConfigProperty("path.page.home");

    }
}
