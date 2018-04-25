package by.khlebnikov.bartender.validator;

import by.khlebnikov.bartender.constant.ConstAttribute;
import by.khlebnikov.bartender.constant.Constant;
import by.khlebnikov.bartender.tag.MessageType;

import javax.servlet.http.HttpServletRequest;

/**
 * Class validating submitted by a user data
 */
public class Validator {

    // Actions ---------------------------------------------------------------------------------------

    /**
     * Validates if the string is not null nor empty
     *
     * @param string submitted string
     * @return true if the string is valid, false otherwise
     */
    public static boolean checkString(String string) {
        return string != null && !string.isEmpty();
    }

    /**
     * Checks submitted by a user registration
     *
     * @param name         submitted name
     * @param email        submitted email
     * @param password     submitted password
     * @param confirmation submitted confirmation
     * @param request      HttpServletRequest
     * @return true if all fields are valid, false if any is invalid
     */
    public static boolean checkRegistrationData(String name,
                                                String email,
                                                String password,
                                                String confirmation,
                                                HttpServletRequest request) {
        boolean valid = true;
        MessageType message = null;

        if (!checkString(name)) {
            message = MessageType.INCORRECT_NAME;
            valid = false;
        } else if (!checkString(email)) {
            message = MessageType.INCORRECT_EMAIL;
            valid = false;
        } else if (!checkString(password)) {
            message = MessageType.INCORRECT_PASSWORD;
            valid = false;
        } else if (!checkString(confirmation)) {
            message = MessageType.INCORRECT_CONFIRMATION;
            valid = false;
        } else if (password.length() < Constant.MIN_PASSWORD_LENGTH
                || password.length() > Constant.MAX_PASSWORD_LENGTH) {
            message = MessageType.INCORRECT_PASSWORD_LENGTH;
            valid = false;
        } else if (!confirmation.equals(password)) {
            message = MessageType.PASSWORD_MISMATCH;
            valid = false;
        }

        request.setAttribute(ConstAttribute.MESSAGE_TYPE, message);
        return valid;
    }

    /**
     * Checks submitted by a user login data
     *
     * @param email    submitted email
     * @param password submitted password
     * @param request  HttpServletRequest
     * @return true if all fields are valid, false if any is invalid
     */
    public static boolean checkLoginData(String email, String password, HttpServletRequest request) {
        boolean valid = true;
        MessageType message = null;

        if (!checkString(email)) {
            message = MessageType.INCORRECT_EMAIL;
            valid = false;
        } else if (!checkString(password)) {
            message = MessageType.INCORRECT_PASSWORD;
            valid = false;
        }

        request.setAttribute(ConstAttribute.MESSAGE_TYPE, message);
        return valid;
    }

}
