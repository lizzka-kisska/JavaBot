package morphology;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import telegram.morphology.CheckingMessageForObsceneLanguage;

public class TokenInListTest {
    @Test
    void checkWithEmptyToken() {
        String token = "";
        Boolean result = CheckingMessageForObsceneLanguage.tokenInList(token);
        Boolean equalResult = false;
        Assertions.assertEquals(result, equalResult);
    }

    @Test
    void checkWithTokenContainedInList() {
        String token = "жопа";
        Boolean result = CheckingMessageForObsceneLanguage.tokenInList(token);
        Boolean equalResult = true;
        Assertions.assertEquals(result, equalResult);
    }

    @Test
    void checkWithTokenNotContainedInList() {
        String token = "бабочка";
        Boolean result = CheckingMessageForObsceneLanguage.tokenInList(token);
        Boolean equalResult = false;
        Assertions.assertEquals(result, equalResult);
    }
}