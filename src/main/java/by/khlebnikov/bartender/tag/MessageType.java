package by.khlebnikov.bartender.tag;

/**
 * Enum containing keys of message properties to display via custom MessageTag
 */
public enum MessageType {

    // Constants ----------------------------------------------------------------------------------
    REGISTRATION_ERROR("message.registrationerror"),
    REGISTRATION_SUCCESS("message.registrationSuccess"),
    INCORRECT_CONFIRMATION("message.incorrectconfirmation"),
    EMAIL_SENT("message.emailsent"),
    INCORRECT_NAME("message.incorrectname"),
    INCORRECT_EMAIL("message.incorrectemail"),
    INCORRECT_PASSWORD("message.incorrectpassword"),
    INCORRECT_EMAIL_OR_PASSWORD("message.notregistered"),
    PASSWORD_MISMATCH("message.passwordmismatch"),
    INCORRECT_PASSWORD_LENGTH("message.passwordlength"),
    ALREADY_REGISTERED("message.alreadyregistered"),
    AWAITING_CONFIRMATION("message.awaitingconfirmation"),
    USER_NOT_REGISTERED("message.invalidemail"),
    RESET_LINK_SENT("message.resetlinksent"),
    MAIL_ERROR("message.mailerror"),
    HASH_ERROR("message.hasherror"),
    INCORRECT_USER("message.incorrectuser"),
    PASSWORD_CHANGED("message.passwordchanged"),
    COCKTAIL_NOT_FOUND("message.cocktailnotfound"),
    ERROR("message.error");

    // Vars ---------------------------------------------------------------------------------------
    private String messageKey;

    // Constructors -------------------------------------------------------------------------------
    MessageType(String messageKey) {
        this.messageKey = messageKey;
    }

    // Getters ------------------------------------------------------------------------------------
    public String getMessageKey() {
        return messageKey;
    }
}
