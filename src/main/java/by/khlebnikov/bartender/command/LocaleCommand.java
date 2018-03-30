package by.khlebnikov.bartender.command;

import by.khlebnikov.bartender.constant.Constant;
import by.khlebnikov.bartender.manager.PropertyManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class LocaleCommand implements Command {
    @Override
    public String execute(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String chosenLocale = request.getParameter(Constant.LOCALE);

        switch (chosenLocale){
            case Constant.EN_US:
                session.setAttribute("locale", Constant.EN_US);
                session.setAttribute("ChosenLocale", Constant.EN);
                break;
            case Constant.RU_RU:
                session.setAttribute("locale", Constant.RU_RU);
                session.setAttribute("ChosenLocale", Constant.RU);
        }

        return PropertyManager.getConfigProperty("path.page.index");
    }
}
