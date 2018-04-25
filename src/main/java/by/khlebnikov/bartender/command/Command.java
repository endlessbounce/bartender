package by.khlebnikov.bartender.command;

import by.khlebnikov.bartender.exception.CommandException;

import javax.servlet.http.HttpServletRequest;

/**
 * Interface to implement the Command design pattern
 */
public interface Command {

    /**
     * Processes data attached to request and returns required page.
     *
     * @param request HttpServletRequest request
     * @return String path to a chosen page
     * @throws CommandException if an exception on lower levels happens
     */
    String execute(HttpServletRequest request) throws CommandException;

}
