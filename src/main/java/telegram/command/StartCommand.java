package telegram.command;

public class StartCommand implements Command {
    @Override
    public String execute(String message) {
        return """
                Приффки))
                Это бот нереальной легенды сунца Евы Марченковой и просто крутой чиксы Лизы Ант
                Нажми на /help, чтобы узнать, какие есть команды""";
    }

    @Override
    public String getInfo() {
        return "команда, которую нужно использовать, если ты забыл," +
                " кто создатели бота или приветсвие";
    }
}