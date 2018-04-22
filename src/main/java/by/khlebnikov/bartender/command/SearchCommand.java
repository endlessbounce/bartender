package by.khlebnikov.bartender.command;

import by.khlebnikov.bartender.exception.ControllerException;

import javax.servlet.http.HttpServletRequest;

public class SearchCommand implements Command {
    @Override
    public String execute(HttpServletRequest request) throws ControllerException {
        return null;
    }
}
