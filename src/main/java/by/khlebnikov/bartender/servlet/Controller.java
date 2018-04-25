package by.khlebnikov.bartender.servlet;

import by.khlebnikov.bartender.command.*;
import by.khlebnikov.bartender.constant.ConstAttribute;
import by.khlebnikov.bartender.constant.ConstParameter;
import by.khlebnikov.bartender.constant.ConstPage;
import by.khlebnikov.bartender.exception.CommandException;
import by.khlebnikov.bartender.reader.PropertyReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet("/controller")
public class Controller extends HttpServlet {

    // Vars ---------------------------------------------------------------------------------------
    private static Logger logger = LogManager.getLogger();

    // Actions ------------------------------------------------------------------------------------
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Processes incoming request
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String page;
        CommandFactory client = new CommandFactory();

        try {
            Optional<Command> commandOpt = client.defineCommand(request);
            Command command = commandOpt.orElse(new DefaultCommand());

            /*We need http response to attach cookies in some commands*/
            if (command instanceof LoginActionCommand ||
                    command instanceof LogoutCommand) {
                ((CommandWithResponse) command).setResponse(response);
            }

            page = command.execute(request);
        } catch (CommandException e) {
            logger.catching(e);
            page = ConstPage.RESULT;
        }

        logger.debug("chosen command: " + request.getParameter(ConstParameter.COMMAND) +
                       "\nrespond page : " + page);

        if (page != null) {
            request.getRequestDispatcher(page).forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + ConstPage.INDEX);
        }
    }
}
