package by.khlebnikov.bartender.servlet;

import by.khlebnikov.bartender.command.*;
import by.khlebnikov.bartender.constant.ConstAttribute;
import by.khlebnikov.bartender.constant.ConstParameter;
import by.khlebnikov.bartender.constant.ConstPage;
import by.khlebnikov.bartender.exception.ControllerException;
import by.khlebnikov.bartender.reader.PropertyReader;
import by.khlebnikov.bartender.tag.MessageType;
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Logger logger = LogManager.getLogger();
        String page = PropertyReader.getConfigProperty(ConstPage.INDEX);

        /*LoggedUserRedirectFilter restricts access for logged users to such commands as
        * login, registration, etc.*/
        String prohibitedRequest = (String)request.getAttribute(ConstAttribute.PROHIBITED);
        if(prohibitedRequest != null){
            response.sendRedirect(request.getContextPath() + page);
            return;
        }

        logger.debug("chosen command: " + request.getParameter(ConstParameter.COMMAND));

        CommandFactory client = new CommandFactory();

        Optional<Command> commandOpt = client.defineCommand(request);
        Command command = commandOpt.orElse(new DefaultCommand());

            /*We need http response to attach cookie while logging in*/
        if(command instanceof LoginActionCommand ||
                command instanceof LogoutCommand){
            ((CommandWithResponse) command).setResponse(response);
        }

        try{
            page = command.execute(request);
        } catch (ControllerException e) {
            /*there seems to be no solid reasons to show users internal error descriptions
             * so we simply will display a message that error happened and make a log in JSP*/
            logger.catching(e);
            page = PropertyReader.getConfigProperty(ConstPage.RESULT);
        }

        logger.debug("respond page : " + page);

        if (page != null) {
            request.getRequestDispatcher(page).forward(request, response);
        } else {
            page = PropertyReader.getConfigProperty(ConstPage.INDEX);
            response.sendRedirect(request.getContextPath() + page);
        }
    }
}
