package by.khlebnikov.bartender.command;

import by.khlebnikov.bartender.manager.PropertyManager;

import javax.servlet.http.HttpServletRequest;

public class DefaultCommand implements Command {
    @Override
    public String execute(HttpServletRequest request) {
        return PropertyManager.getConfigProperty("path.page.home");
    }
}
