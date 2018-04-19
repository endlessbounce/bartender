package by.khlebnikov.bartender.command;

import by.khlebnikov.bartender.exception.ControllerException;

import javax.servlet.http.HttpServletRequest;

public interface Command {
    String execute(HttpServletRequest request) throws ControllerException;
}
