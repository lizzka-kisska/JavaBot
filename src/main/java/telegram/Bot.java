package telegram;

import database.CollaborationDatabase;
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
                //если это команда, либо парсим, либо выдаем кнопки
                if (message.getText().startsWith("/")) {
                    if (message.getText().equals("/who_am_i")) {
                        Command executeButton = commands.get("/who_am_i");
                        execute(executeButton.buttons(chatId));
                    } else {
                        String response = parseProcessMessage(message.getText());
                        SendMessage sendResponse = new SendMessage();
                        sendResponse.setChatId(chatId);
                        sendResponse.setText(response);

                        execute(sendResponse);
                    }
                } else {
                    //если не команда, значит, нужно переслать
                    CollaborationDatabase data = new CollaborationDatabase();
                    String newChatId = data.forwardToReviewer(chatId);

                    SendMessage sendResponseToReviewer = new SendMessage();
                    sendResponseToReviewer.setChatId(newChatId);
                    sendResponseToReviewer.setText(message.getText());

                    execute(sendResponseToReviewer);

                    SendMessage sendResponseToSender = new SendMessage();
                    sendResponseToSender.setText("твое смс успешно отправлено крутому юзеру свэг #hype");
                    sendResponseToSender.setChatId(chatId);
                    execute(sendResponseToSender);

                }
            } else if (update.hasCallbackQuery()) {
//                если есть отклик, то нужно добавить в бд
                SendMessage sendResponse = new SendMessage();
                long chatId = update.getCallbackQuery().getMessage().getChatId();

                if (update.getCallbackQuery().getData().equals("send")) {
                    sendResponse.setText("у у у ты фоток отправлятель");
                } else if (update.getCallbackQuery().getData().equals("get")) {
                    sendResponse.setText("у у у ты фоток получатель");
                }
                sendResponse.setChatId(chatId);
                execute(sendResponse);

                if (update.getCallbackQuery().getData().equals("get")) {
                    CollaborationDatabase data = new CollaborationDatabase();
                    data.insertToDatabase(String.valueOf(chatId));
                }
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


