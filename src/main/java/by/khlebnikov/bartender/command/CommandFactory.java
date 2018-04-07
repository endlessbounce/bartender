package by.khlebnikov.bartender.command;

import by.khlebnikov.bartender.constant.ConstParameter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class CommandFactory {
    private Logger logger = LogManager.getLogger();

    public Optional<Command> defineCommand(HttpServletRequest request){
        Optional<Command> current = Optional.empty();
        String action = request.getParameter(ConstParameter.COMMAND);

        if (action == null || action.isEmpty()) {
            return current;
        }

        try {
            CommandType currentEnum = CommandType.valueOf(action.toUpperCase());
            current = Optional.of(currentEnum.getCommand());
        } catch (IllegalArgumentException e) {
            logger.catching(e);
        }

        return current;
    }
}
