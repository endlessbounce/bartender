package by.khlebnikov.bartender.command;

import by.khlebnikov.bartender.constant.ConstPage;
import by.khlebnikov.bartender.constant.ConstParameter;
import by.khlebnikov.bartender.reader.PropertyReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

public class ProfileCommand implements Command {
    private Logger logger = LogManager.getLogger();

    /**
     * Returns address of profile page. If user moves there from created
     * cocktail page, checks SECTION parameter to switch the right section on profile
     * page.
     * @param request
     * @return
     */
    @Override
    public String execute(HttpServletRequest request) {
        String section = request.getParameter(ConstParameter.SECTION);
        String editedId = request.getParameter(ConstParameter.EDITED_ID);

        logger.debug("section: " + section + ", edited id: " + editedId);

        if (ConstParameter.CREATED.equals(section)) {
            request.setAttribute(ConstParameter.SECTION, ConstParameter.CREATED);
        } else if (ConstParameter.EDIT.equals(section)){
            request.setAttribute(ConstParameter.SECTION, ConstParameter.EDIT);
            request.setAttribute(ConstParameter.EDITED_ID, editedId);
        }

        return PropertyReader.getConfigProperty(ConstPage.PROFILE);
    }
}
