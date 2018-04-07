package by.khlebnikov.bartender.command;

import by.khlebnikov.bartender.constant.ConstPage;
import by.khlebnikov.bartender.reader.PropertyReader;

import javax.servlet.http.HttpServletRequest;

public class ProfileCommand implements Command {
    @Override
    public String execute(HttpServletRequest request) {
        return PropertyReader.getConfigProperty(ConstPage.PROFILE);
    }
}
