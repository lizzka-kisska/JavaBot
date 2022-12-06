package telegram.command;

public class EchoCommand implements Command {
    @Override
    public String execute(String message) {
        if (message == null) {
            return "/echo";
        }
        return message;
    }

    @Override
    public String getInfo() {
        return "команда, которую ты ДОЛЖЕН использовать с каким-то сообщением, " +
                "и бот ответит тебе им же";
    }
}
