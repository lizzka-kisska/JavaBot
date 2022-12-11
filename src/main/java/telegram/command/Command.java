package telegram.command;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface Command {
    String execute(String message);

    String getInfo();

    SendMessage buttons(String chatId);
}
