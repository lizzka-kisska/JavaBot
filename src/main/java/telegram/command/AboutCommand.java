package telegram.command;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class AboutCommand implements Command {
    @Override
    public String execute(String message) {
        return """
                Это крутой бот свэг
                Ты можешь помочь другим и/или попросить помощи других с выбором шмоток, лука, прически, мэкапа, итд""";
    }

    @Override
    public String getInfo() {
        return "команда, с помощью которой ты узнаешь для чего этот бот, " +
                "кроме как для зачета по ооп";
    }

    @Override
    public SendMessage buttons(String chatId) {
        return null;
    }
}
