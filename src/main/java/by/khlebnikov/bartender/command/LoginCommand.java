package by.khlebnikov.bartender.command;

import by.khlebnikov.bartender.constant.Constant;
import by.khlebnikov.bartender.entity.User;
import by.khlebnikov.bartender.logic.UserService;
import by.khlebnikov.bartender.manager.PropertyManager;
import by.khlebnikov.bartender.tag.MessageType;
import by.khlebnikov.bartender.validator.Validator;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class LoginCommand implements Command {
    private UserService service;

    @Override
    public String execute(HttpServletRequest request) {
        String queryPropertyPath = request.getServletContext().getRealPath(Constant.QUERY_PROPERTY_PATH);
        service = new UserService(queryPropertyPath);

        String email = request.getParameter(Constant.EMAIL);
        String password = request.getParameter(Constant.PASSWORD);

        boolean correctInput = Validator.checkLoginData(email, password, request);

        if(correctInput){
            Optional<User> userOpt = service.checkUser(email, password);

            if(userOpt.isPresent()){
                request.getSession().setAttribute("userName", userOpt.get().getName());

                return PropertyManager.getConfigProperty("path.page.home");
            } else {
                request.setAttribute("MessageType", MessageType.INCORRECT_EMAIL_OR_PASSWORD);
            }
        }

        request.setAttribute("email", email);
        request.setAttribute("password", password);
        return PropertyManager.getConfigProperty("path.page.login");
    }
}
