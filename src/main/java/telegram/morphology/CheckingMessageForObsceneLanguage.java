package telegram.morphology;

import ru.stachek66.nlp.mystem.holding.Factory;
import ru.stachek66.nlp.mystem.holding.MyStem;
import ru.stachek66.nlp.mystem.holding.MyStemApplicationException;
import ru.stachek66.nlp.mystem.holding.Request;
import ru.stachek66.nlp.mystem.model.Info;
import scala.Option;
import scala.collection.JavaConversions;

import java.io.*;

public class CheckingMessageForObsceneLanguage {


    public static boolean tokenInList(String token) {
        try {
            File file = new File("src/main/java/telegram/morphology/banned_words.txt");
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            String line = reader.readLine();
            while (line != null) {
                if (token.equals(line)) {
                    return true;
                }
                line = reader.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private final static MyStem mystemAnalyzer =
            new Factory("--format json")
                    .newMyStem("3.0", Option.<File>empty()).get();

    public static boolean wordShouldBeCensored(final String word) throws MyStemApplicationException {
        final Iterable<Info> result =
                JavaConversions.asJavaIterable(
                        mystemAnalyzer
                                .analyze(Request.apply(word))
                                .info()
                                .toIterable());

        for (final Info info : result) {
            String token = "";
            if (info.lex().isDefined()) {
                token = info.lex().get();
            }
            return tokenInList(token);
        }
        return false;
    }

    public static String creatingCensoredMessage(final String message) throws MyStemApplicationException {
        StringBuilder censoredMessage = new StringBuilder();
        StringBuilder word = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            char symbol = message.charAt(i);
            if (!Character.isLetter(symbol)) {
                if (wordShouldBeCensored(String.valueOf(word))) {
                    censoredMessage.append("||");
                    censoredMessage.append(word);
                    censoredMessage.append("||");
                } else {
                    censoredMessage.append(word);
                }
                word.setLength(0);
                censoredMessage.append(symbol);
            } else {
                word.append(symbol);
            }
        }
        if (wordShouldBeCensored(String.valueOf(word))) {
            censoredMessage.append("||");
            censoredMessage.append(word);
            censoredMessage.append("||");
        } else {
            censoredMessage.append(word);
        }

        return censoredMessage.toString();
    }
}
