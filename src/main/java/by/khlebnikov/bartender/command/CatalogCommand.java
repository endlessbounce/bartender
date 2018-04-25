package by.khlebnikov.bartender.command;

import by.khlebnikov.bartender.constant.ConstPage;

import javax.servlet.http.HttpServletRequest;

/**
 * Class representing command, returning path to the catalog page.
 */
public class CatalogCommand implements Command {

    // Actions ------------------------------------------------------------------------------------

    /**
     * Returns the path to the catalog page
     *
     * @param request HttpServletRequest request
     * @return path to the catalog page
     */
    @Override
    public String execute(HttpServletRequest request) {
        return ConstPage.CATALOG;
    }
}

