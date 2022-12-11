package telegram;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import telegram.command.*;

import java.util.*;

public class Bot extends TelegramLongPollingBot {
    private static final Map<String, Command> commands = new LinkedHashMap<>();

    {
        commands.put("/start", new StartCommand());
        commands.put("/about", new AboutCommand());
        commands.put("/echo", new EchoCommand());
        commands.put("/help", new HelpCommand(commands));
        commands.put("/who_am_i", new WhoAmICommand());
    }

    @Override
    public String getBotUsername() {
        return "dripNdrainBot";
    }

    @Override
    public String getBotToken() {
        TokenReader token = new TokenReader();
        try {
            return token.tokenReader("constant.txt");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        //update - объект, из которого мы можем получить сообщение,
        // текст и id чата, необходмые для отправки ответного сообщения
        try {
            if (update.hasMessage() && update.getMessage().hasText()) {
                //Извлекаем из объекта сообщение пользователя
                Message message = update.getMessage();
                //Достаем из inMess id чата пользователя
                String chatId = message.getChatId().toString();

                if (message.getText().equals("/who_am_i")) {
                    Command executeButton = commands.get("/who_am_i");
                    execute(executeButton.buttons(chatId));
                } else {
                    //Получаем текст сообщения пользователя, отправляем в написанный нами обработчик
                    String response = parseProcessMessage(message.getText());
                    //Создаем объект класса SendMessage - наш будущий ответ пользователю
                    SendMessage sendResponse = new SendMessage();

                    //Добавляем в наше сообщение id чата а также наш ответ
                    sendResponse.setChatId(chatId);
                    sendResponse.setText(response);

                    //Отправляем в чат
                    execute(sendResponse);
                }
            } else if (update.hasCallbackQuery()) {
                SendMessage sendResponse = new SendMessage();

                if (update.getCallbackQuery().getData().equals("send")) {
                    sendResponse.setText("у у у ты фоток отправлятель");
                } else if (update.getCallbackQuery().getData().equals("get")) {
                    sendResponse.setText("у у у ты фоток получатель");
                } else if (update.getCallbackQuery().getData().equals("send&get")) {
                    sendResponse.setText("у у у ты фоток отправитель и фоток получатель");
                }
                sendResponse.setChatId(update.getCallbackQuery().getMessage().getChatId());
                execute(sendResponse);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public String parseProcessMessage(String textMsg) {
        String errorMes = "нажми /help, чтобы узнать команды >:(";

        int spaceIndex = textMsg.indexOf(" ");
        String command = spaceIndex != -1 ? textMsg.substring(0, spaceIndex) : textMsg;
        String message = spaceIndex != -1 ? textMsg.substring(spaceIndex + 1) : null;

        Command commandOutput = commands.get(command);
        // если такой команды не существует(те нет значения по ключу) - ошибка
        if (commandOutput == null) {
            return errorMes;
        }
        // если хелп - выводим инфу
        if (command.equals("/help") && message != null) {
            if (commands.containsKey(message)) {
                Command msgInfo = commands.get(message);
                return msgInfo.getInfo();
            } else if (commands.containsKey("/" + message)) {
                Command msgInfo = commands.get("/" + message);
                return msgInfo.getInfo();
            } else {
                return errorMes;
            }
        } else if (!command.equals("/help") && !command.equals("/echo") && message != null) {
            return errorMes;
        }
        return commandOutput.execute(message);
    }
}


