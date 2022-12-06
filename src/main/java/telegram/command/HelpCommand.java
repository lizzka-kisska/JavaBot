package telegram.command;


import java.util.Map;

public class HelpCommand implements Command {
    private final Map<String, Command> commands;

    public HelpCommand(Map<String, Command> commands) {
        this.commands = commands;
    }

    @Override
    public String execute(String message) {
        if (message == null) {
            StringBuilder sb = new StringBuilder();
            commands.forEach((name, cmd) -> {
                sb.append(name).append(" — ").append(cmd.getInfo()).append("\n");
            });
            sb.append("\nтакже ты можешь написать /help + / + команда, чтобы узнать про эту команду");
            return sb.toString();
        }
        return "";
    }

    @Override
    public String getInfo() {
        return "команда, с помощью которой ты узнаешь другие команды бота";
    }
}
