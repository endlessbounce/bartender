package by.khlebnikov.bartender.command;

import by.khlebnikov.bartender.constant.ConstPage;
import by.khlebnikov.bartender.constant.ConstParameter;
import by.khlebnikov.bartender.reader.PropertyReader;

import javax.servlet.http.HttpServletRequest;

public class ProfileCommand implements Command {

    /**
     * Returns address of profile page. If user moves there from created
     * cocktail context, checks CREATED enum value to switch the section on profile
     * page to "your cocktails"
     * @param request
     * @return
     */
    @Override
    public String execute(HttpServletRequest request) {
        String section = request.getParameter(ConstParameter.SECTION);

        if (ConstParameter.CREATED.equals(section)) {
            request.setAttribute(ConstParameter.SECTION, ConstParameter.CREATED);
        }

        return PropertyReader.getConfigProperty(ConstPage.PROFILE);
    }
}
