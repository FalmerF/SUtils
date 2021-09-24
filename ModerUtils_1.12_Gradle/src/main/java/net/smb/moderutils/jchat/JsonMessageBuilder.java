package net.smb.moderutils.jchat;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JustAGod on 17.03.2018.
 */
public class JsonMessageBuilder {

    private static final Gson gson = new Gson();
    private List<MessagePart> parts = new ArrayList<MessagePart>();

    public JsonPartBuilder newPart() {
        return new JsonPartBuilder();
    }

    public JsonMessage build() {
        return new JsonMessage(parts);
    }

    public class JsonPartBuilder {
        private boolean bold;
        private boolean underlined;
        private boolean italic;
        private boolean strikethrough;
        private boolean obfuscated;
        private PartText text;
        private PartColor color;
        private HoverEvent hover;
        private ClickEvent click;

        public JsonPartBuilder setText(String text) {
            this.text = new PartText.SimpleText(text);
            return this;
        }

        public JsonPartBuilder setTranslatedText(String format, String... arguments) {
            this.text = new PartText.TranslatedText(format, arguments);
            return this;
        }

        public JsonPartBuilder setBold(boolean bold) {
            this.bold = bold;
            return this;
        }

        public JsonPartBuilder setColor(PartColor color) {
            this.color = color;
            return this;
        }

        public JsonPartBuilder setUnderlined(boolean underlined) {
            this.underlined = underlined;
            return this;
        }

        public JsonPartBuilder setItalic(boolean italic) {
            this.italic = italic;
            return this;
        }

        public JsonPartBuilder setStrikethrough(boolean strikethrough) {
            this.strikethrough = strikethrough;
            return this;
        }

        public JsonPartBuilder setObfuscated(boolean obfuscated) {
            this.obfuscated = obfuscated;
            return this;
        }

        public JsonPartBuilder setHoverText(String text) {
            this.hover = text == null ? null : new HoverEvent(HoverEvent.Type.SHOW_TEXT, text);
            return this;
        }

        public JsonPartBuilder setHoverStack(ItemStack stack) {
            if (stack != null) {
                NBTTagCompound compound = new NBTTagCompound();
                stack.writeToNBT(compound);
                this.hover = new HoverEvent(HoverEvent.Type.SHOW_ITEM, compound.toString());
            } else this.hover = null;
            return this;
        }

        public JsonPartBuilder setHoverAchievemnt(StatBase achievemnt) {
            this.hover = achievemnt == null ? null : new HoverEvent(HoverEvent.Type.SHOW_ACHIEVEMENT, achievemnt.statId);
            return this;
        }

        public JsonPartBuilder setClick(ClickEvent.Type action, String value) {
            if (action == null || value == null) {
                this.click = null;
            } else {
                this.click = new ClickEvent(action, value);
            }
            return this;
        }

        public JsonMessageBuilder end() {
            Preconditions.checkArgument(text != null, "You must to specify text of part");
            parts.add(new MessagePart(text, color, bold, underlined, italic, strikethrough, obfuscated, hover, click));
            return JsonMessageBuilder.this;
        }
    }
}
