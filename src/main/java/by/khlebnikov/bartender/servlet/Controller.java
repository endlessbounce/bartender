package by.khlebnikov.bartender.servlet;

import by.khlebnikov.bartender.command.Command;
import by.khlebnikov.bartender.command.CommandFactory;
import by.khlebnikov.bartender.command.DefaultCommand;
import by.khlebnikov.bartender.command.LoginActionCommand;
import by.khlebnikov.bartender.constant.Constant;
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Logger logger = LogManager.getLogger();
        String page;

        logger.debug("chosen command: " + request.getParameter(Constant.COMMAND));

        CommandFactory client = new CommandFactory();
        Optional<Command> commandOpt = client.defineCommand(request);
        Command command = commandOpt.orElse(new DefaultCommand());

        /*We need http response to attach cookie while logging in*/
        if(command instanceof LoginActionCommand){
            ((LoginActionCommand) command).setResponse(response);
        }

        page = command.execute(request);

        logger.debug("respond page : " + page);

        if (page != null) {
            request.getRequestDispatcher(page).forward(request, response);
        } else {
            page = PropertyReader.getConfigProperty(Constant.PAGE_INDEX);
            response.sendRedirect(request.getContextPath() + page);
        }
    }
}
