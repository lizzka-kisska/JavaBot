package database;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import telegram.Bot;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;


public class ButtonWithDatabaseConnectionTest {
    private final Bot bot = new Bot();

    public Session openSession() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.getTransaction().begin();
        return session;
    }

    public Update setUpdate() {
        Chat chat = new Chat();
        chat.setId(560843844L);
        Message message = new Message();
        message.setChat(chat);
        String data = "get";
        CallbackQuery callbackQuery = new CallbackQuery();
        callbackQuery.setMessage(message);
        callbackQuery.setData(data);
        Update update = new Update();
        update.setCallbackQuery(callbackQuery);
        return update;
    }

    @Test
    void checkButtonsReviewerOption() {
        Update update = setUpdate();
        bot.onUpdateReceived(update);
        Session session = openSession();
        NativeQuery<?> query;
        query = session.createSQLQuery("""
                SELECT chatid_reviewer
                FROM reviewers
                ORDER BY chatid_reviewer DESC
                LIMIT 1;
                """);
        String result = query.getResultStream().findFirst().get().toString();
        String equalResult = "560843844";
        Assertions.assertEquals(result, equalResult);
    }
}
