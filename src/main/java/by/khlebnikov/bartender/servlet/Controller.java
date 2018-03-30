package by.khlebnikov.bartender.servlet;

import by.khlebnikov.bartender.command.Command;
import by.khlebnikov.bartender.command.CommandFactory;
import by.khlebnikov.bartender.command.DefaultCommand;
import by.khlebnikov.bartender.manager.PropertyManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet("/jsp/controller")
public class Controller extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Logger logger = LogManager.getLogger();
        String page;

        logger.debug("chosen command: " + request.getParameter("command"));

        CommandFactory client = new CommandFactory();
        Optional<Command> commandOpt = client.defineCommand(request);
        Command command = commandOpt.orElse(new DefaultCommand());
        page = command.execute(request);

        logger.debug("respond page : " + page);

        if (page != null) {
            request.getRequestDispatcher(page).forward(request, response);
        } else {
            logger.debug("Page was not found (null), redirecting to index page");
            page = PropertyManager.getConfigProperty("path.page.index");
            response.sendRedirect(request.getContextPath() + page);
        }
    }
}
