package by.khlebnikov.bartender.command;

import by.khlebnikov.bartender.constant.ConstAttribute;
import by.khlebnikov.bartender.constant.ConstPage;
import by.khlebnikov.bartender.constant.ConstParameter;
import by.khlebnikov.bartender.constant.Constant;
import by.khlebnikov.bartender.entity.ProspectUser;
import by.khlebnikov.bartender.exception.CommandException;
import by.khlebnikov.bartender.exception.ServiceException;
import by.khlebnikov.bartender.mail.LetterType;
import by.khlebnikov.bartender.mail.Mailer;
import by.khlebnikov.bartender.reader.PropertyReader;
import by.khlebnikov.bartender.service.UserService;
import by.khlebnikov.bartender.tag.MessageType;
import by.khlebnikov.bartender.utility.CodeGenerator;
import by.khlebnikov.bartender.utility.HashCoder;
import by.khlebnikov.bartender.utility.TimeGenerator;
import by.khlebnikov.bartender.validator.Validator;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

/**
 * Class used to confirm user's registration via email
 */
public class ConfirmEmailCommand implements Command {

    // Vars ---------------------------------------------------------------------------------------
    private HashCoder hashCoder;
    private UserService service;

    // Constructors -------------------------------------------------------------------------------
    public ConfirmEmailCommand() {
        this.service = new UserService();
        this.hashCoder = new HashCoder();
    }

    // Actions ------------------------------------------------------------------------------------

    /**
     * When a user submits his data with the registration form,
     * method sends a link to user's email, after following which it makes a new record
     * in the database (registers the user) or discards the request for registration if registration
     * time is out.
     *
     * @param request HttpServletRequest request
     * @return result page or registration page if the data filled in the form is not correct
     * @throws CommandException is thrown if an exception on lower levels happens
     */
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        String name = request.getParameter(ConstParameter.NAME);
        String email = request.getParameter(ConstParameter.EMAIL);
        String password = request.getParameter(ConstParameter.PASSWORD);
        String confirmation = request.getParameter(ConstParameter.CONFIRMATION);
        String code = CodeGenerator.uniqueId();
        boolean correctInput = Validator.checkRegistrationData(name, email, password, confirmation, request);
        boolean alreadyRegistered = false;
        boolean awaitingConfirmation = false;

        try {
            /*check if registration data is valid in case front end doesn't check it*/
            alreadyRegistered = service.findUserByEmail(email).isPresent();
            awaitingConfirmation = service.isProspectRegistered(email);

            /*if user is not registered yet and not awaiting confirmation and all input data is correct*/
            if (correctInput && !alreadyRegistered && !awaitingConfirmation) {

                /*create hash-key and salt*/
                byte[] salt = hashCoder.getNextSalt();
                Optional<byte[]> hashOpt = hashCoder.hash(password.toCharArray(), salt);

                if (hashOpt.isPresent()) {
                    byte[] hashKey = hashOpt.get();

                    service.saveProspectUser(new ProspectUser(name, email, hashKey, salt,
                            TimeGenerator.expirationTime(), code));

                    Mailer.sendEmail(email, request, code, LetterType.REGISTRATION);
                    request.setAttribute(ConstAttribute.MESSAGE_TYPE, MessageType.EMAIL_SENT);
                } else {
                    request.setAttribute(ConstAttribute.MESSAGE_TYPE, MessageType.HASH_ERROR);
                }

                return ConstPage.RESULT;
            } else if (alreadyRegistered) {
                request.setAttribute(ConstAttribute.MESSAGE_TYPE, MessageType.ALREADY_REGISTERED);
            } else if (awaitingConfirmation) {
                request.setAttribute(ConstAttribute.MESSAGE_TYPE, MessageType.AWAITING_CONFIRMATION);
            }
        } catch (IOException | MessagingException | ServiceException e) {
            request.setAttribute(ConstAttribute.MESSAGE_TYPE, MessageType.MAIL_ERROR);
            throw new CommandException("Correct input: " + correctInput + ",\n already registered: " + alreadyRegistered +
                    ",\n awaiting confirmation: " + awaitingConfirmation, e);
        }

        request.setAttribute(ConstParameter.NAME, name);
        request.setAttribute(ConstParameter.EMAIL, email);
        request.setAttribute(ConstParameter.PASSWORD, password);
        request.setAttribute(ConstParameter.CONFIRMATION, confirmation);

        return ConstPage.REGISTRATION;
    }
}
