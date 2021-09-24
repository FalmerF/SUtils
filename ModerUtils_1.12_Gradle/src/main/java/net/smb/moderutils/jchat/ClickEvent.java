package net.smb.moderutils.jchat;

/**
 * Created by JustAGod on 17.03.2018.
 */
public class ClickEvent {

    public final Type type;
    public final String value;

    public ClickEvent(Type type, String value) {
        this.type = type;
        this.value = value;
    }

    @SuppressWarnings("unused")
    public enum Type {

        /**
         * При нажатии открывает заданую ссылку
         */
        OPEN_URL("open_url"),
        /**
         * Открывает файл. Путь может быть относительным
         */
        OPEN_FILE("open_file"),
        TWITCH_USER("twitch_user_info"),
        RUN_COMMAND("run_command"),
        SUGGEST_COMMAND("suggest_command");

        public final String value;

        Type(String value) {

            this.value = value;
        }
    }
}
