package command;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import telegram.command.EchoCommand;

public class EchoCommandTest {
    private final EchoCommand echoCommand = new EchoCommand();

    @Test
    void testEmptyInputString() {
        String message = null;
        String result = echoCommand.execute(message);
        String equalResult = "/echo";
        Assertions.assertEquals(result, equalResult);
    }

    @Test
    void testNotEmptyString() {
        String message = "Губы пахнут малиной, девочка-аскорбинка";
        String result = echoCommand.execute(message);
        Assertions.assertEquals(result, message);
    }
}
