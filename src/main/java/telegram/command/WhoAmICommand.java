package telegram.command;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class WhoAmICommand implements Command {
    @Override
    public String execute(String message) {
        return null;
    }

    @Override
    public String getInfo() {
        return "команда, для того, чтобы ты выбрал, кем ты хочешь быть";
    }

    @Override
    public SendMessage buttons(String chatId) {
        Buttons buttons = new Buttons();
        int id = Integer.parseInt(chatId);
        return buttons.inlineKeyBoardMessage(id);
    }
}
