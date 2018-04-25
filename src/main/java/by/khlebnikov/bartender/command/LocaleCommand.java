package by.khlebnikov.bartender.command;

import by.khlebnikov.bartender.constant.ConstAttribute;
import by.khlebnikov.bartender.constant.ConstParameter;
import by.khlebnikov.bartender.constant.ConstLocale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Class allowing to switch between RU and EN locations of users
 */
public class LocaleCommand implements Command {

    // Actions ------------------------------------------------------------------------------------

    /**
     * Attaches to the session chosen locale and language strings
     *
     * @param request HttpServletRequest request
     * @return null so controller redirects the request to the index page
     */
    @Override
    public String execute(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String chosenLocale = request.getParameter(ConstParameter.LOCALE);

        switch (chosenLocale) {
            case ConstLocale.EN_US:
                session.setAttribute(ConstParameter.LOCALE, ConstLocale.EN_US);
                session.setAttribute(ConstAttribute.CHOSEN_LANGUAGE, ConstLocale.EN);
                break;
            case ConstLocale.RU_RU:
                session.setAttribute(ConstParameter.LOCALE, ConstLocale.RU_RU);
                session.setAttribute(ConstAttribute.CHOSEN_LANGUAGE, ConstLocale.RU);
        }

        return null;
    }
}
