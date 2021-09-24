package net.smb.moderutils.jchat;

/**
 * Created by JustAGod on 17.03.2018.
 */
@SuppressWarnings("unused")
public enum PartColor {
    BLACK("black"),
    DARK_BLUE("dark_blue"),
    DARK_GREEN("dark_green"),
    DARK_AQUA("dark_aqua"),
    DARK_RED("dark_red"),
    DARK_PURPLE("dark_purple"),
    GOLD("gold"),
    GRAY("gray"),
    DARK_GRAY("dark_gray"),
    BLUE("blue"),
    GREEN("green"),
    AQUA("aqua"),
    RED("red"),
    LIGHT_PURPLE("light_purple"),
    YELLOW("yellow"),
    WHITE("white"),
    RESET("reset");

    public final String value;

    PartColor(String value) {
        this.value = value;
    }
}
