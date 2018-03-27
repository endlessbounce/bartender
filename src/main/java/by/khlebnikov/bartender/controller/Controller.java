package by.khlebnikov.bartender.controller;

import by.khlebnikov.bartender.command.Command;
import by.khlebnikov.bartender.command.CommandFactory;
import by.khlebnikov.bartender.command.DefaultCommand;
import by.khlebnikov.bartender.manager.MessageManager;
import by.khlebnikov.bartender.manager.PagePathManager;

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
        String page;
        CommandFactory client = new CommandFactory();
        Optional<Command> commandOpt = client.defineCommand(request);
        Command command = commandOpt.orElse(new DefaultCommand());
        page = command.execute(request);

        if (page != null) {
            request.getRequestDispatcher(page).forward(request, response);
        } else {
            page = PagePathManager.getProperty("path.page.index");
            request.getSession().setAttribute("nullPage",
                    MessageManager.getProperty("message.nullpage"));
            response.sendRedirect(request.getContextPath() + page);
        }
    }
}
