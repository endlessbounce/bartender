package by.khlebnikov.bartender.command;

import by.khlebnikov.bartender.logic.Service;
import by.khlebnikov.bartender.manager.MessageManager;
import by.khlebnikov.bartender.manager.PagePathManager;

import javax.servlet.http.HttpServletRequest;

public class RegisterCommand implements Command {
    private Service service;

    public RegisterCommand() {
        this.service = new Service();
    }

    @Override
    public String execute(HttpServletRequest request) {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String pass = request.getParameter("password");
        boolean success = service.registerUser(name, email, pass);
        String msg;
        if(success){
            msg = MessageManager.getProperty("message.registrationSuccess");
        }else{
            msg = MessageManager.getProperty("message.registrationerror");
        }
        request.setAttribute("reg_result", msg);
        return PagePathManager.getProperty("path.page.home");
    }
}
