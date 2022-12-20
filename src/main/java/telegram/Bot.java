package telegram;

import database.CollaborationDatabase;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import telegram.command.*;

import java.sql.SQLException;
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
                        sendMessage(chatId, response);
                    }
                } else {
                    //если не команда, значит, нужно переслать
                    CollaborationDatabase data = new CollaborationDatabase();
                    String reviewer = data.checkSender(chatId);
                    if (reviewer != null) {
                        sendMessage(chatId, "отзыв отправился! спасибо что были с нами");
                        sendMessage(reviewer, "получите и распишитесь! отзыв:)");
                        sendMessage(reviewer, message.getText());
                    } else {
                        String newChatId = data.forwardToReviewer(chatId);

                        sendMessage(newChatId, "тебе пришло новое сообщение, " +
                                "посмотри, вдруг там что-то интересное");
                        sendMessage(newChatId, message.getText());

                        sendMessage(chatId, "твое смс успешно отправлено крутому юзеру #swag");
                    }
                }
//            }else if (update.hasMessage() && update.getMessage().hasPhoto()) {
//                Message message = update.getMessage();
//                String chatId = message.getChatId().toString();
//                List<PhotoSize> photo = message.getPhoto();
//                String photoId = String.valueOf(photo.get(0));
////                если фото, то нужно переслать
//                CollaborationDatabase data = new CollaborationDatabase();
//                String newChatId = data.forwardToReviewer(chatId);
//                SendMessage send = new SendMessage();
//                send.setChatId(newChatId);
//                SendPhoto sendPhoto = new SendPhoto();
//                sendPhoto.setChatId(newChatId);
//                sendPhoto.setPhoto(photoId);
            } else if (update.hasCallbackQuery()) {
//                если есть отклик, то нужно добавить в бд
                String chatId = update.getCallbackQuery().getMessage().getChatId().toString();

                if (update.getCallbackQuery().getData().equals("send")) {
                    sendMessage(chatId, "у у у ты фоток отправлятель");
                } else if (update.getCallbackQuery().getData().equals("get")) {
                    sendMessage(chatId,"у у у ты фоток получатель");
                }

                if (update.getCallbackQuery().getData().equals("get")) {
                    CollaborationDatabase data = new CollaborationDatabase();
                    data.insertToDatabase(chatId);
                }
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
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

    public void sendMessage(String chatId, String message) throws TelegramApiException {
        SendMessage send = new SendMessage();
        send.setChatId(chatId);
        send.setText(message);
        execute(send);
    }
}


