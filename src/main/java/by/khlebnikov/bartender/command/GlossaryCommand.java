package by.khlebnikov.bartender.command;

import by.khlebnikov.bartender.constant.ConstPage;
import by.khlebnikov.bartender.reader.PropertyReader;

import javax.servlet.http.HttpServletRequest;

/**
 * Class to render glossary page to the user
 */
public class GlossaryCommand implements Command {

    // Actions ------------------------------------------------------------------------------------

    /**
     * Returns the glossary page to a user
     *
     * @param request HttpServletRequest request
     * @return the path to the glossary page
     */
    @Override
    public String execute(HttpServletRequest request) {
        return ConstPage.GLOSSARY;
    }
}
