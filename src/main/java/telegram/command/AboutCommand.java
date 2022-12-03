package telegram.command;

public class AboutCommand implements Command {
    @Override
    public String execute(String message) {
        return """
                Это крутой бот свэг
                Ты можешь помочь другим и/или попросить помощи других с выбором шмоток, лука, прически, мэкапа, стиля музыки итд""";
    }

    @Override
    public String getInfo() {
        return "команда, с помощью которой ты узнаешь для чего этот бот, " +
                "кроме как для зачета по ооп";
    }
}
