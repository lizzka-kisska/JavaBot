package telegram;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;

public class Bot extends TelegramLongPollingBot {
    @Override
    public String getBotUsername() {
        return "dripNdrainBot";
    }

    @Override
    public String getBotToken() {
        return "5652087564:AAFMNUwqTISFuKEr9_ilOt-mWxhqX_xsjzI";
    }

    @Override
    public void onUpdateReceived(Update update) {
        //update - объект, из которого мы можем получить сообщение,
        // текст и id чата, необходмые для отправки ответного сообщения
        try{
            if(update.hasMessage() && update.getMessage().hasText()) {
                //Извлекаем из объекта сообщение пользователя
                Message message = update.getMessage();
                //Достаем из inMess id чата пользователя
                String chatId = message.getChatId().toString();
                //Получаем текст сообщения пользователя, отправляем в написанный нами обработчик
                String response = parseMessage(message.getText());
                //Создаем объект класса SendMessage - наш будущий ответ пользователю
                SendMessage sendResponse = new SendMessage();

                //Добавляем в наше сообщение id чата а также наш ответ
                sendResponse.setChatId(chatId);
                sendResponse.setText(response);

                //Отправляем в чат
                execute(sendResponse);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public String parseMessage(String textMsg) {
        String errorMes = "нажми /help, чтобы узнать команды >:(";

        HashMap<String, String> commandsMap = new HashMap<>();

        int spaceIndex = textMsg.indexOf(" ");
        String command = spaceIndex != -1 ? textMsg.substring(0, spaceIndex) : textMsg;
        String message = spaceIndex != -1 ? textMsg.substring(spaceIndex+1) : null;

        commandsMap.put("/start", """
                Приффки))
                Это бот нереальной легенды сунца Евы Марченковой и просто крутой чиксы Лизы Ант
                Нажми на /help, чтобы узнать, какие есть команды""");

        commandsMap.put("/about", """
                Это крутой бот свэг
                Ты можешь помочь другим и/или попросить помощи других с выбором шмоток, лука, прически, мэкапа, стиля музыки итд""");

        commandsMap.put("/echo", message != null ? message : "/echo");

        commandsMap.put("/help", """
                Наши команды:
                /start - привествие
                /help - все команды
                /about - все о крутом боте свэге
                /echo + что-то - если тебе так нужно, бот повторит твое что-то
                /help + команда - все о команде
                Пожалуйста, пиши допслова после команд через пробел:P""");

        HashMap<String, String> commandHelp = new HashMap<>();
        commandHelp.put("/start", "команда, которую нужно использовать, если ты забыл," +
                " кто создатели бота или приветсвие");
        commandHelp.put("/help", "команда, с помощью которой ты узнаешь другие команды бота");
        commandHelp.put("/about", "команда, с помощью которой ты узнаешь для чего этот бот, " +
                "кроме как для зачета по ооп");
        commandHelp.put("/echo", "команда, которую ты ДОЛЖЕН использовать с каким-то сообщением, " +
                "и бот ответит тебе им же");

        if(command.startsWith("/")){
            if(command.equals("/help") && message != null){
                if(message.startsWith("/")){
                    return commandHelp.getOrDefault(message, errorMes);
                }
                else{
                    return commandHelp.getOrDefault("/" + message, errorMes);
                }
            }
            if(message == null && !command.equals("/echo"))
                return commandsMap.getOrDefault(command, errorMes);
            else if (command.equals("/echo")) {
                return commandsMap.getOrDefault(command, errorMes);
            }
        }
        return errorMes;
    }
}
