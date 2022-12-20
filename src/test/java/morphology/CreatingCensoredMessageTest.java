package morphology;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.stachek66.nlp.mystem.holding.MyStemApplicationException;
import telegram.morphology.CheckingMessageForObsceneLanguage;

public class CreatingCensoredMessageTest {
    @Test
    void checkWithEmptyMessage() throws MyStemApplicationException {
        String message = "";
        String result = CheckingMessageForObsceneLanguage.creatingCensoredMessage(message);
        String equalResult = "";
        Assertions.assertEquals(result, equalResult);
    }

    @Test
    void checkWithMessageConsistingOnlyOneCensoredWord() throws MyStemApplicationException {
        String message = "Дружба";
        String result = CheckingMessageForObsceneLanguage.creatingCensoredMessage(message);
        String equalResult = "Дружба";
        Assertions.assertEquals(result, equalResult);
    }

    @Test
    void checkWithMessageConsistingOnlyOneObsceneWord() throws MyStemApplicationException {
        String message = "Жопа";
        String result = CheckingMessageForObsceneLanguage.creatingCensoredMessage(message);
        String equalResult = "||Жопа||";
        Assertions.assertEquals(result, equalResult);
    }

    @Test
    void checkWithMessageConsistingOnlySymbols() throws MyStemApplicationException {
        String message = """
                (*≧∀≦*)
                （；＿；）""";
        String result = CheckingMessageForObsceneLanguage.creatingCensoredMessage(message);
        String equalResult = """
                (*≧∀≦*)
                （；＿；）""";
        Assertions.assertEquals(result, equalResult);
    }

    @Test
    void checkWithMessageConsistingSymbolsAndCensoredWords() throws MyStemApplicationException {
        String message = "(*≧∀≦*) Мир Дружба Жвачка";
        String result = CheckingMessageForObsceneLanguage.creatingCensoredMessage(message);
        String equalResult = "(*≧∀≦*) Мир Дружба Жвачка";
        Assertions.assertEquals(result, equalResult);
    }

    @Test
    void checkWithMessageConsistingSymbolsAndObsceneWords() throws MyStemApplicationException {
        String message = ">:( Жопа";
        String result = CheckingMessageForObsceneLanguage.creatingCensoredMessage(message);
        String equalResult = ">:( ||Жопа||";
        Assertions.assertEquals(result, equalResult);
    }

    @Test
    void checkWithMessageConsistingSymbolsAndAnyWords() throws MyStemApplicationException {
        String message = """
                Анекдот:
                — Вовочка, не смей произносить это слово - «жопа»!
                — Почему?
                — Потому что НЕТ такого слова!
                — Как это — жопа есть, а слова нету???""";
        String result = CheckingMessageForObsceneLanguage.creatingCensoredMessage(message);
        String equalResult = """
                Анекдот:
                — Вовочка, не смей произносить это слово - «||жопа||»!
                — Почему?
                — Потому что НЕТ такого слова!
                — Как это — ||жопа|| есть, а слова нету???""";
        Assertions.assertEquals(result, equalResult);
    }
}
