package morphology;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.stachek66.nlp.mystem.holding.MyStemApplicationException;
import telegram.morphology.CheckingMessageForObsceneLanguage;

public class WordShouldBeCensoredTest {
    @Test
    void checkWithEmptyWord() throws MyStemApplicationException {
        String word = "";
        Boolean result = CheckingMessageForObsceneLanguage.wordShouldBeCensored(word);
        Boolean equalResult = false;
        Assertions.assertEquals(result, equalResult);
    }

    @Test
    void checkWithCensoredWord() throws MyStemApplicationException {
        String word = "дружба";
        Boolean result = CheckingMessageForObsceneLanguage.wordShouldBeCensored(word);
        Boolean equalResult = false;
        Assertions.assertEquals(result, equalResult);
    }

    @Test
    void checkWithObsceneWordFromList() throws MyStemApplicationException {
        String word = "жопа";
        Boolean result = CheckingMessageForObsceneLanguage.wordShouldBeCensored(word);
        Boolean equalResult = true;
        Assertions.assertEquals(result, equalResult);
    }

    @Test
    void checkWithObsceneWordNotFromList() throws MyStemApplicationException {
        String word = "жопе";
        Boolean result = CheckingMessageForObsceneLanguage.wordShouldBeCensored(word);
        Boolean equalResult = true;
        Assertions.assertEquals(result, equalResult);
    }

    @Test
    void checkWithModifiedObsceneWord() throws MyStemApplicationException {
        String word = "жoпa";
        Boolean result = CheckingMessageForObsceneLanguage.wordShouldBeCensored(word);
        Boolean equalResult = true;
        Assertions.assertEquals(result, equalResult);
    }

    @Test
    void checkWithWordInDifferentRegisters() throws MyStemApplicationException {
        String word = "жОпА";
        Boolean result = CheckingMessageForObsceneLanguage.wordShouldBeCensored(word);
        Boolean equalResult = true;
        Assertions.assertEquals(result, equalResult);
    }
}