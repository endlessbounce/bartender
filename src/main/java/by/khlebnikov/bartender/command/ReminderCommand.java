package by.khlebnikov.bartender.command;

import by.khlebnikov.bartender.constant.Constant;
import by.khlebnikov.bartender.reader.PropertyReader;

import javax.servlet.http.HttpServletRequest;

public class ReminderCommand implements Command {
    @Override
    public String execute(HttpServletRequest request) {
        return PropertyReader.getConfigProperty(Constant.PAGE_REMINDER);
    }
}
