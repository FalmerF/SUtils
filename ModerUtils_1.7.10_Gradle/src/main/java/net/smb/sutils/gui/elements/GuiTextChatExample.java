package net.smb.sutils.gui.elements;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.smb.sutils.ModuleInfo;
import net.smb.sutils.common.GuiTheme;
import net.smb.sutils.modules.SettingsModule;
import net.smb.sutils.utils.Utils;

public class GuiTextChatExample extends GuiElement {
	public String text;
	public int type;
	
	public GuiTextChatExample(int posX, int posY, int type){
		super(-1, posX, posY, 0, 0);
		this.type = type;
	}
	
    @SuppressWarnings("static-access")
	public void draw(Minecraft mc, int positionX, int positionY)
    {
        if (this.visible)
        {
            FontRenderer fontRender = mc.fontRenderer;
            
            String displayString = "";
            
            if(type == 0) {
            	if(SettingsModule.getBool("chatFilter")) {
            		String color = Utils.stringToChatCodes(SettingsModule.getString("chatFilterColor"));
            		if(color.equals("")) color = "§f";
            		displayString = "§8[§aG§8] §7Player §f§l»" + color +" Пример текста в глобальном чате.";
            	}
            	else {
            		displayString = "§a[§2G§a] §7Player §f§l»§f Пример текста в глоабльном чате.";
            	}
            }
            else if(type == 1) {
            	if(SettingsModule.getBool("chatFilter")) {
            		String color = Utils.stringToChatCodes(SettingsModule.getString("chatFilterTColor"));
            		if(color.equals("")) color = "§f";
            		displayString = "§8[§6T§8] §7Player §f§l»" + color +" Пример текста в торговом чате.";
            	}
            	else {
            		displayString = "§e[§6T§e] §7Player §f§l»§f Пример текста в торговом чате.";
            	}
            }
            else if(type == 2) {
            	if(SettingsModule.getBool("chatFilter")) {
            		String color = Utils.stringToChatCodes(SettingsModule.getString("chatFilterLoaclColor"));
            		if(color.equals("")) color = "§f";
            		displayString = "§7Player §f§l»" + color +" Пример текста в локальном чате.";
            	}
            	else {
            		displayString = "§7Player §f§l»§f Пример текста в локальном чате.";
            	}
            }
            else if(type == 3) {
            	if(SettingsModule.getBool("chatFilter")) {
            		String color = Utils.stringToChatCodes(SettingsModule.getString("highlightedWordsColor"));
            		String colorGlobal = Utils.stringToChatCodes(SettingsModule.getString("chatFilterColor"));
            		if(color.equals("")) color = "§f";
            		displayString = "§8[§aG§8] §8[§2Grand§8] §7Player §f§l»" + colorGlobal +" Пример текста с " + color + "выделяемым" + colorGlobal + " словом.";
            	}
            	else {
            		displayString = "§a[§2G§a] §8[§2Grand§8] §7Player §f§l»§f Пример текста с выделяемым словом.";
            	}
            }
            
            else if(type == 4) {
            	if(SettingsModule.getBool("chatFilter")) {
            		String color = "§6";
            		if(!SettingsModule.getString("chatFilterModerColor").equals("")) color = Utils.stringToChatCodes(SettingsModule.getString("chatFilterModerColor"));
            		displayString = "§8[§aG§8] §8[§9Moder§8] §ePlayer §f§l»" + color +" Пример текста модератора.";
            	}
            	else {
            		displayString = "§a[§2G§a] §8[§9Moder§8] §ePlayer §f§l»§6 Пример текста модератора.";
            	}
            }
            
            else if(type == 5) {
            	if(!SettingsModule.getString("joinPlayerPattern").equals("")) {
            		displayString = Utils.convertAmpCodes(SettingsModule.getString("joinPlayerPattern")).replaceAll("%PLAYER%", ModuleInfo.playerName);
            	}
            	else {
            		displayString = "§8[§6+§8] §9" + ModuleInfo.playerName;
            	}
            	if(displayString.length() > 30)
            		displayString = displayString.substring(0, 30) + "...";
            }
            
            else if(type == 6) {
            	if(!SettingsModule.getString("leavePlayerPattern").equals("")) {
            		displayString = Utils.convertAmpCodes(SettingsModule.getString("leavePlayerPattern")).replaceAll("%PLAYER%", ModuleInfo.playerName);
            	}
            	else {
            		displayString = "§8[§6-§8] §e" + ModuleInfo.playerName;
            	}
            	if(displayString.length() > 30)
            		displayString = displayString.substring(0, 30) + "...";
            }
            
            else if(type == 7) {
            	if(SettingsModule.getBool("chatFilter")) {
            		String color = "§f";
            		if(!SettingsModule.getString("chatFilterModerChatColor").equals("")) color = Utils.stringToChatCodes(SettingsModule.getString("chatFilterModerChatColor"));
            		displayString = "§8[§9M§8] §8[§9Moder§8] §ePlayer §f§l»" + color +" Пример текста модер чата.";
            	}
            	else {
            		displayString = "§8[§9M§8] §8[§9Moder§8] §ePlayer §f§l»§f Пример текста модер чата.";
            	}
            }
            
            else if(type == 8) {
            	if(!SettingsModule.getString("joinFriendPattern").equals("")) {
            		displayString = Utils.convertAmpCodes(SettingsModule.getString("joinFriendPattern")).replaceAll("%PLAYER%", ModuleInfo.playerName);
            	}
            	else {
            		displayString = "§8[§6+§8] §9Друг " + ModuleInfo.playerName;
            	}
            	if(displayString.length() > 30)
            		displayString = displayString.substring(0, 30) + "...";
            }
            
            else if(type == 9) {
            	if(!SettingsModule.getString("leaveFriendPattern").equals("")) {
            		displayString = Utils.convertAmpCodes(SettingsModule.getString("leaveFriendPattern")).replaceAll("%PLAYER%", ModuleInfo.playerName);
            	}
            	else {
            		displayString = "§8[§6-§8] §eДруг " + ModuleInfo.playerName;
            	}
            	if(displayString.length() > 30)
            		displayString = displayString.substring(0, 30) + "...";
            }
            
            // Draw Title
            GL11.glPushMatrix();
        	GL11.glTranslatef(this.posX, this.posY, 0.0F);
            GL11.glScalef(0.8F, 0.8F, 0.8F);
            fontRender.drawString(displayString, 0, 0, GuiTheme.textColor);
            GL11.glPopMatrix();
        }
    }
}
