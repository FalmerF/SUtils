package net.smb.sutils.jchat;

public class HoverEvent {

    public final Type type;
    public final String value;

    public HoverEvent(Type type, String value) {
        this.type = type;
        this.value = value;
    }

    @SuppressWarnings("unused")
    public enum Type {
        SHOW_TEXT("show_text"),
        SHOW_ITEM("show_item"),
        SHOW_ACHIEVEMENT("show_achievement"),
        SHOW_ENTITY("show_entity");

        public final String value;

        Type(String value) {

            this.value = value;
        }
    }
}
