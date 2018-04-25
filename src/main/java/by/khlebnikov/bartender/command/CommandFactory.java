package by.khlebnikov.bartender.command;

import by.khlebnikov.bartender.constant.ConstParameter;
import by.khlebnikov.bartender.exception.CommandException;
import by.khlebnikov.bartender.validator.Validator;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * Class with a method to define chosen by a user action command
 */
public class CommandFactory {

    // Actions ------------------------------------------------------------------------------------

    /**
     * Defines chosen by a user command
     *
     * @param request HttpServletRequest request
     * @return a command if there's a match, or an empty Optional
     */
    public Optional<Command> defineCommand(HttpServletRequest request) throws CommandException {
        Optional<Command> current = Optional.empty();
        String action = request.getParameter(ConstParameter.COMMAND);

        if (Validator.checkString(action)) {
            try {
                CommandType chosenCommand = CommandType.valueOf(action.toUpperCase());
                current = Optional.of(chosenCommand.getCommand());
            } catch (IllegalArgumentException e) {
                throw new CommandException("Action doesn't exist: " + action, e);
            }
        }

        return current;
    }

}
