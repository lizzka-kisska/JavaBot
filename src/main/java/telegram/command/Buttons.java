package telegram.command;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class Buttons {
    public SendMessage inlineKeyBoardMessage(long chatId) {
//        Создаем обьект разметки клавиатуры
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
//        Создаем обьект InlineKeyboardButton, у него есть 2 параметра: текст на кнопке и то, что отсылается на
//        сервер при нажатии
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("я фоток отправлятель");
        button1.setCallbackData("send");
        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText("я фоток получатель");
        button2.setCallbackData("get");
//        Добавляем его в список, таким образом создавая ряд
        List<InlineKeyboardButton> buttonsRow1 = new ArrayList<>();
        List<InlineKeyboardButton> buttonsRow2 = new ArrayList<>();
        buttonsRow1.add(button1);
        buttonsRow2.add(button2);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(buttonsRow1);
        rowList.add(buttonsRow2);
        inlineKeyboardMarkup.setKeyboard(rowList);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("выбирай!!!\nTы можешь быть и тем, и тем, для этого достаточно выбрать 2 кнопку");
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        return sendMessage;
    }
}
