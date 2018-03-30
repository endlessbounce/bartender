package by.khlebnikov.bartender.tag;

public enum MessageType {
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
    PASSWORD_SENT("message.passwordsent");

    private String messageKey;

    MessageType(String messageKey) {
        this.messageKey = messageKey;
    }

    public String getMessageKey() {
        return messageKey;
    }
}
