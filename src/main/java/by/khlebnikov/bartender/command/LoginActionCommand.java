package by.khlebnikov.bartender.command;

import by.khlebnikov.bartender.constant.ConstAttribute;
import by.khlebnikov.bartender.constant.ConstParameter;
import by.khlebnikov.bartender.constant.Constant;
import by.khlebnikov.bartender.constant.ConstPage;
import by.khlebnikov.bartender.entity.User;
import by.khlebnikov.bartender.service.UserService;
import by.khlebnikov.bartender.reader.PropertyReader;
import by.khlebnikov.bartender.tag.MessageType;
import by.khlebnikov.bartender.utility.Utility;
import by.khlebnikov.bartender.validator.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public class LoginActionCommand implements Command, CommandWithResponse {
    private Logger logger = LogManager.getLogger();
    private UserService service;
    private HttpServletResponse response;


    public LoginActionCommand() {
        this.service = new UserService();
    }

    @Override
    public String execute(HttpServletRequest request) {
        String email = request.getParameter(ConstParameter.EMAIL);
        String password = request.getParameter(ConstParameter.PASSWORD);
        String logged = request.getParameter(ConstParameter.STAY_LOGGED);

        logger.debug("stay logged: " + logged);

        boolean correctInput = Validator.checkLoginData(email, password, request);

        if(correctInput){
            Optional<User> userOpt = service.checkUser(email, password);

            if(userOpt.isPresent()){
                User user = userOpt.get();
                request.getSession().setAttribute(ConstAttribute.USER_NAME, user.getName());

                //checking stay logged in checkbox
                if(ConstParameter.TRUE.equals(logged)){
                    String id = Utility.uniqueId();
                    user.setUniqueCookie(id);
                    service.updateUser(user);

                    Cookie cookie = Utility.persistingCookie(id);
                    response.addCookie(cookie);

                    Cookie oldSession = new Cookie(Constant.OLD_SESSION, ConstParameter.TRUE);
                    oldSession.setMaxAge(-1);
                    response.addCookie(oldSession);

                    logger.debug("singed in: " + id);
                }

                return PropertyReader.getConfigProperty(ConstPage.HOME);
            } else {
                request.setAttribute(ConstAttribute.MESSAGE_TYPE, MessageType.INCORRECT_EMAIL_OR_PASSWORD);
            }
        }

        request.setAttribute(ConstParameter.EMAIL, email);
        request.setAttribute(ConstParameter.PASSWORD, password);
        return PropertyReader.getConfigProperty(ConstPage.LOGIN);
    }

    @Override
    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }
}
