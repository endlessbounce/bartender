package by.khlebnikov.bartender.command;

import by.khlebnikov.bartender.manager.PagePathManager;

import javax.servlet.http.HttpServletRequest;

public class DefaultCommand implements Command {
    @Override
    public String execute(HttpServletRequest request) {
        return PagePathManager.getProperty("path.page.home");
    }
}
