package by.khlebnikov.bartender.command;

import by.khlebnikov.bartender.constant.ConstPage;
import by.khlebnikov.bartender.constant.ConstParameter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

/**
 * Class rendering profile page to a logged user
 */
public class ProfileCommand implements Command {

    // Vars ---------------------------------------------------------------------------------------
    private static Logger logger = LogManager.getLogger();

    // Actions ------------------------------------------------------------------------------------

    /**
     * Returns the address of the profile page. If user moves there from created
     * cocktail page, checks SECTION parameter to switch to the right section on the profile
     * page (either 'edit' cocktail or 'created cocktails').
     *
     * @param request HttpServletRequest request
     * @return the profile page path
     */
    @Override
    public String execute(HttpServletRequest request) {
        String section = request.getParameter(ConstParameter.SECTION);
        String editedId = request.getParameter(ConstParameter.EDITED_ID);

        logger.debug("section: " + section + ", edited id: " + editedId);

        if (ConstParameter.CREATED.equals(section)) {
            request.setAttribute(ConstParameter.SECTION, ConstParameter.CREATED);
        } else if (ConstParameter.EDIT.equals(section)) {
            request.setAttribute(ConstParameter.SECTION, ConstParameter.EDIT);
            request.setAttribute(ConstParameter.EDITED_ID, editedId);
        }

        return ConstPage.PROFILE;
    }
}
