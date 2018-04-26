package by.khlebnikov.bartender.command;

import by.khlebnikov.bartender.constant.ConstPage;
import by.khlebnikov.bartender.reader.PropertyReader;

import javax.servlet.http.HttpServletRequest;

//TODO
public class SettingCommand implements Command {
    @Override
    public String execute(HttpServletRequest request) {
        return ConstPage.SETTINGS;
    }
}
