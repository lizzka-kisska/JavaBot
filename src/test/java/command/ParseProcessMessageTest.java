package command;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import telegram.Bot;

public class ParseProcessMessageTest {
    private final Bot bot = new Bot();

    @Test
    void checkAboutCommand() {
        String textMsg = "/about";
        String result = bot.parseProcessMessage(textMsg);
        String equalResult = """
                Это крутой бот свэг
                Ты можешь помочь другим и/или попросить помощи других с выбором шмоток, лука, прически, мэкапа, итд""";
        Assertions.assertEquals(result, equalResult);
    }

    @Test
    void checkAboutCommandNotEmptyMessage() {
        String textMsg = "/about А в рязани пряники с глазами";
        String result = bot.parseProcessMessage(textMsg);
        String equalResult = "нажми /help, чтобы узнать команды >:(";
        Assertions.assertEquals(result, equalResult);
    }

    @Test
    void checkStartCommand() {
        String textMsg = "/start";
        String result = bot.parseProcessMessage(textMsg);
        String equalResult = """
                Приффки))
                Это бот нереальной легенды сунца Евы Марченковой и просто крутой чиксы Лизы Ант
                Нажми на /help, чтобы узнать, какие есть команды""";
        Assertions.assertEquals(result, equalResult);
    }

    @Test
    void checkStartCommandNotEmptyMessage() {
        String textMsg = "/start Пациент без электрофореза - что пацаны без майонеза";
        String result = bot.parseProcessMessage(textMsg);
        String equalResult = "нажми /help, чтобы узнать команды >:(";
        Assertions.assertEquals(result, equalResult);
    }

    @Test
    void checkEchoCommandEmptyMessage() {
        String textMsg = "/echo";
        String result = bot.parseProcessMessage(textMsg);
        String equalResult = "/echo";
        Assertions.assertEquals(result, equalResult);
    }

    @Test
    void checkEchoCommandNotEmptyMessage() {
        String textMsg = "/echo Водила чувствует мой вайб";
        String result = bot.parseProcessMessage(textMsg);
        String equalResult = "Водила чувствует мой вайб";
        Assertions.assertEquals(result, equalResult);
    }

    @Test
    void checkHelpCommandEmptyMessage() {
        String textMsg = "/help";
        String result = bot.parseProcessMessage(textMsg);
        String equalResult = """
                /start — команда, которую нужно использовать, если ты забыл, кто создатели бота или хочешь поздороваться ещё раз
                /about — команда, с помощью которой ты узнаешь для чего этот бот, кроме как для зачета по ооп
                /echo — команда, которую ты ДОЛЖЕН использовать с каким-то сообщением, и бот ответит тебе им же
                /help — команда, с помощью которой ты узнаешь другие команды бота
                /who_am_i — команда, для того, чтобы ты выбрал, кем ты хочешь быть

                также ты можешь написать /help + команда, чтобы узнать про эту команду""";
        Assertions.assertEquals(result, equalResult);
    }

    @Test
    void checkHelpCommandIncorrectMessage() {
        String textMsg = "/help Собака писала, собака какала, собака села и заплакала";
        String result = bot.parseProcessMessage(textMsg);
        String equalResult = "нажми /help, чтобы узнать команды >:(";
        Assertions.assertEquals(result, equalResult);
    }

    @Test
    void checkCommandHelpSlashStart() {
        String textMsg = "/help /start";
        String result = bot.parseProcessMessage(textMsg);
        String equalResult = "команда, которую нужно использовать, если ты забыл," +
                " кто создатели бота или хочешь поздороваться ещё раз";
        Assertions.assertEquals(result, equalResult);
    }

    @Test
    void checkCommandHelpStart() {
        String textMsg = "/help start";
        String result = bot.parseProcessMessage(textMsg);
        String equalResult = "команда, которую нужно использовать, если ты забыл, кто создатели бота или приветсвие";
        Assertions.assertEquals(result, equalResult);
    }

    @Test
    void checkCommandHelpSlashAbout() {
        String textMsg = "/help /about";
        String result = bot.parseProcessMessage(textMsg);
        String equalResult = "команда, с помощью которой ты узнаешь для чего этот бот, кроме как для зачета по ооп";
        Assertions.assertEquals(result, equalResult);
    }

    @Test
    void checkCommandHelpAbout() {
        String textMsg = "/help about";
        String result = bot.parseProcessMessage(textMsg);
        String equalResult = "команда, с помощью которой ты узнаешь для чего этот бот, кроме как для зачета по ооп";
        Assertions.assertEquals(result, equalResult);
    }

    @Test
    void checkCommandHelpSlashEcho() {
        String textMsg = "/help /echo";
        String result = bot.parseProcessMessage(textMsg);
        String equalResult = "команда, которую ты ДОЛЖЕН использовать с каким-то сообщением, и бот ответит тебе им же";
        Assertions.assertEquals(result, equalResult);
    }

    @Test
    void checkCommandHelpEcho() {
        String textMsg = "/help echo";
        String result = bot.parseProcessMessage(textMsg);
        String equalResult = "команда, которую ты ДОЛЖЕН использовать с каким-то сообщением, и бот ответит тебе им же";
        Assertions.assertEquals(result, equalResult);
    }

    @Test
    void checkCommandHelpSlashHelp() {
        String textMsg = "/help /help";
        String result = bot.parseProcessMessage(textMsg);
        String equalResult = "команда, с помощью которой ты узнаешь другие команды бота";
        Assertions.assertEquals(result, equalResult);
    }

    @Test
    void checkCommandHelpHelp() {
        String textMsg = "/help help";
        String result = bot.parseProcessMessage(textMsg);
        String equalResult = "команда, с помощью которой ты узнаешь другие команды бота";
        Assertions.assertEquals(result, equalResult);
    }

    @Test
    void checkIncorrectMessage() {
        String textMsg = "Всем привет, я Андрей Созыкин, это курс компьютерные сети и системы телекоммуникаций";
        String result = bot.parseProcessMessage(textMsg);
        String equalResult = "нажми /help, чтобы узнать команды >:(";
        Assertions.assertEquals(result, equalResult);
    }
}
