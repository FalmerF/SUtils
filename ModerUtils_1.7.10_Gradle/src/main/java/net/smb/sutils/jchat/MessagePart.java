package net.smb.sutils.jchat;


import com.google.common.base.Preconditions;

public class MessagePart {

    public final PartText text;
    public final PartColor color;
    public final boolean bold;
    public final boolean underlined;
    public final boolean italic;
    public final boolean strikethrough;
    public final boolean obfuscated;
    public final HoverEvent hover;
    public final ClickEvent click;

    public MessagePart(
            PartText text,
            PartColor color,
            boolean bold,
            boolean underlined,
            boolean italic,
            boolean strikethrough,
            boolean obfuscated,
            HoverEvent hover,
            ClickEvent click
    ) {
        Preconditions.checkArgument(
                text instanceof PartText.SimpleText || text instanceof PartText.TranslatedText,
                "You haven't to override PartText");
        this.text = text;
        this.color = color;
        this.bold = bold;
        this.underlined = underlined;
        this.italic = italic;
        this.strikethrough = strikethrough;
        this.obfuscated = obfuscated;
        this.hover = hover;
        this.click = click;
    }
}
