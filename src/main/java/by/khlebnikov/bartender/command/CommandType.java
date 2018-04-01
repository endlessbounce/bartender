package by.khlebnikov.bartender.command;

public enum CommandType {
    LOGIN(new LoginCommand()),
    LOGIN_ACTION(new LoginActionCommand()),
    LOGOUT(new LogoutCommand()),
    REGISTER(new RegisterCommand()),
    REGISTER_ACTION(new RegisterActionCommand()),
    CONFIRM(new ConfirmEmailCommand()),
    REMIND(new ReminderCommand()),
    REMIND_ACTION(new ReminderActionCommand()),
    LOCALE(new LocaleCommand()),
    CATALOG(new CatalogCommand()),
    PROFILE(new ProfileCommand()),
    SETTINGS(new SettingCommand());

    private Command command;

    CommandType(Command command) {
        this.command = command;
    }

    public Command getCommand() {
        return command;
    }
}
