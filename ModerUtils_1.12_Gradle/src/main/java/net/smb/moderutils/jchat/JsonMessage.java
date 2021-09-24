package net.smb.moderutils.jchat;

import com.google.gson.stream.JsonWriter;
import net.minecraft.util.text.ITextComponent;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by JustAGod on 17.03.2018.
 */
public class JsonMessage {

    public final List<MessagePart> parts;

    public JsonMessage(List<MessagePart> parts) {
        this.parts = parts;
    }


    public ITextComponent serialize()  {
        return ITextComponent.Serializer.jsonToComponent(toJson());
    }

    public String toJson() {
        try {
            CharArrayWriter buffer = new CharArrayWriter();
            JsonWriter writer = new JsonWriter(buffer);
            writer.beginObject();
            writer.name("text");
            writer.value("");

            if (parts.size() > 0) {
                writer.name("extra");
                writer.beginArray();
                for (MessagePart part : parts) {
                    serializePart(part, writer);
                }
                writer.endArray();
            }
            writer.endObject();
            writer.close();
            return buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void serializePart(MessagePart part, JsonWriter writer) throws IOException {
        writer.beginObject();
        if (part.text instanceof PartText.SimpleText) {
            writer.name("text");
            writer.value(((PartText.SimpleText) part.text).text);
        } else {
            PartText.TranslatedText text = ((PartText.TranslatedText) part.text);
            writer.name("translate");
            writer.value(text.format);
            if (text.arguments.length > 0) {
                writer.name("with");
                writer.beginArray();
                for (String argument : text.arguments) {
                    writer.value(argument);
                }
                writer.endArray();
            }


        }
        if (part.color != null) {
            writer.name("color");
            writer.value(part.color.value);
        }
        if (part.bold) {
            writer.name("bold");
            writer.value(true);
        }
        if (part.underlined) {
            writer.name("underlined");
            writer.value(true);
        }
        if (part.italic) {
            writer.name("italic");
            writer.value(true);
        }
        if (part.strikethrough) {
            writer.name("strikethrough");
            writer.value(true);
        }
        if (part.obfuscated) {
            writer.name("obfuscated");
            writer.value(true);
        }
        if (part.click != null) {
            writer.name("clickEvent");
            writer.beginObject();
            writer.name("action");
            writer.value(part.click.type.value);
            writer.name("value");
            writer.value(part.click.value);
            writer.endObject();
        }
        if (part.hover != null) {
            writer.name("hoverEvent");
            writer.beginObject();
            writer.name("action");
            writer.value(part.hover.type.value);
            writer.name("value");
            writer.value(part.hover.value);
            writer.endObject();
        }
        writer.endObject();
    }
}
