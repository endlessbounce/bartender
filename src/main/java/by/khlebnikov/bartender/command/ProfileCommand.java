package by.khlebnikov.bartender.command;

import by.khlebnikov.bartender.constant.ConstPage;
import by.khlebnikov.bartender.constant.ConstParameter;
import by.khlebnikov.bartender.constant.Constant;
import by.khlebnikov.bartender.reader.PropertyReader;

import javax.servlet.http.HttpServletRequest;

public class ProfileCommand implements Command {
    @Override
    public String execute(HttpServletRequest request) {
        String section = request.getParameter(ConstParameter.SECTION);

        if(ConstParameter.CREATED.equals(section)){
            request.setAttribute(ConstParameter.SECTION, ConstParameter.CREATED);
        }

        return PropertyReader.getConfigProperty(ConstPage.PROFILE);
    }
}
