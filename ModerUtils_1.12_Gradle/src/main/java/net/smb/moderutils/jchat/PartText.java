package net.smb.moderutils.jchat;

/**
 * Created by JustAGod on 17.03.2018.
 */
public class PartText {

    protected PartText() {
    }

    public static final class SimpleText extends PartText {

        public final String text;

        public SimpleText(String text) {
            this.text = text;
        }
    }

    public static final class TranslatedText extends PartText {

        public final String format;
        public final String[] arguments;

        public TranslatedText(String format, String[] arguments) {
            this.format = format;
            this.arguments = arguments;
        }
    }
}
