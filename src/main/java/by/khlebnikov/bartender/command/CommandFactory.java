package by.khlebnikov.bartender.command;

import by.khlebnikov.bartender.manager.MessageManager;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class CommandFactory {
    public Optional<Command> defineCommand(HttpServletRequest request){
        Optional<Command> current = Optional.of(new DefaultCommand());

        String action = request.getParameter("command");
        if (action == null || action.isEmpty()) {
            return current;
        }

        try {
            CommandType currentEnum = CommandType.valueOf(action.toUpperCase());
            current = Optional.of(currentEnum.getCommand());
        } catch (IllegalArgumentException e) {
            request.setAttribute("wrongAction", action
                    + MessageManager.getProperty("message.wrongaction"));
        }
        return current;
    }
}
