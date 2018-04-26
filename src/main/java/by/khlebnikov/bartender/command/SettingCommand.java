package by.khlebnikov.bartender.command;

import by.khlebnikov.bartender.constant.ConstPage;
import by.khlebnikov.bartender.reader.PropertyReader;

import javax.servlet.http.HttpServletRequest;

/**
 * Class to render settings page to users
 */
public class SettingCommand implements Command {

    // Actions ------------------------------------------------------------------------------------

    /**
     * Renders the page with user settings
     * @param request HttpServletRequest request
     * @return settings page
     */
    @Override
    public String execute(HttpServletRequest request) {
        return ConstPage.SETTINGS;
    }
}
