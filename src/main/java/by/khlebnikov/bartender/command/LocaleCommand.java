package by.khlebnikov.bartender.command;

import by.khlebnikov.bartender.constant.Constant;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class LocaleCommand implements Command {
    @Override
    public String execute(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String chosenLocale = request.getParameter(Constant.LOCALE);

        switch (chosenLocale){
            case Constant.EN_US:
                session.setAttribute(Constant.LOCALE, Constant.EN_US);
                session.setAttribute(Constant.CHOSEN_LOCALE, Constant.EN);
                break;
            case Constant.RU_RU:
                session.setAttribute(Constant.LOCALE, Constant.RU_RU);
                session.setAttribute(Constant.CHOSEN_LOCALE, Constant.RU);
        }

        return null;
    }
}
