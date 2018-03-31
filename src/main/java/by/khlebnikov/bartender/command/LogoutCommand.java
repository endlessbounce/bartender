package by.khlebnikov.bartender.command;

import by.khlebnikov.bartender.constant.Constant;
import by.khlebnikov.bartender.entity.User;
import by.khlebnikov.bartender.logic.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class LogoutCommand implements Command {
    private UserService service;

    public LogoutCommand() {
        this.service = new UserService();
    }

    @Override
    public String execute(HttpServletRequest request) {
        Cookie[] cookieArr = request.getCookies();

        // A negative value means that the cookie is not stored persistently and will
        // be deleted when the Web browser exits. A zero value causes the cookie to be deleted.
        for (Cookie cookie : cookieArr) {
            if(Constant.STAY_LOGGED.equals(cookie.getName())){
                List<User> userList = service.findUserByCookie(cookie.getValue());
                User user;

                if(!userList.isEmpty()){
                    user = userList.get(0);
                    user.setUniqueCookie(null);
                    service.updateUser(user);
                }

                cookie.setMaxAge(0);
            }
        }

        request.getSession().invalidate();

        return null;
    }
}
