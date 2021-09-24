package net.smb.sutils.jchat;

public class ClickEvent {

    public final Type type;
    public final String value;

    public ClickEvent(Type type, String value) {
        this.type = type;
        this.value = value;
    }

    @SuppressWarnings("unused")
    public enum Type {
    	
        OPEN_URL("open_url"),
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
