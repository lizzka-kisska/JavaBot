package telegram.command;

public interface Command {
    String execute(String message);
    String getInfo();
}
