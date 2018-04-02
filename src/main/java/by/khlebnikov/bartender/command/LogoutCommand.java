package by.khlebnikov.bartender.command;

import by.khlebnikov.bartender.constant.Constant;
import by.khlebnikov.bartender.entity.User;
import by.khlebnikov.bartender.logic.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class LogoutCommand implements Command, CommandWithResponse {
    private Logger logger = LogManager.getLogger();
    private UserService service;
    private HttpServletResponse response;

    public LogoutCommand() {
        this.service = new UserService();
    }

    @Override
    public String execute(HttpServletRequest request) {
        Cookie[] cookieArr = request.getCookies();

        // A negative value means that the cookie is not stored persistently and will
        // be deleted when the Web browser exits. A zero value causes the cookie to be deleted.
        for (Cookie cookie : cookieArr) {
            switch (cookie.getName()){
                case Constant.STAY_LOGGED:
                    List<User> userList = service.findUserByCookie(cookie.getValue());
                    User user;

                    if(!userList.isEmpty()){
                        user = userList.get(0);
                        user.setUniqueCookie(null);
                        service.updateUser(user);
                    }

                    // To delete a cookie, we need to create a cookie that have the same
                    // name with the cookie that we want to delete. We also need to set
                    // the max age of the cookie to 0 and then add it to the Servlet's
                    // response method.
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                    logger.debug("deleted cookie: " + cookie.getValue());
                    break;
                case Constant.OLD_SESSION:
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                    logger.debug("deleted cookie: " + cookie.getValue());
                    break;
            }
        }

        request.getSession().invalidate();

        return null;
    }

    @Override
    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }
}
