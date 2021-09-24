package net.smb.sutils.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.eq2online.console.Log;
import net.eq2online.macros.compatibility.AbstractionLayer;
import net.eq2online.macros.compatibility.Reflection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.GuiPlayerInfo;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.inventory.InventoryBasic;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.smb.sutils.ModuleInfo;
import net.smb.sutils.VariableProviderModer;
import net.smb.sutils.common.GuiTheme;
import net.smb.sutils.gui.elements.GuiNote;
import net.smb.sutils.gui.elements.GuiTextString;
import net.smb.sutils.modules.SettingsModule;
import net.smb.sutils.utils.RenderUtils;
import net.smb.sutils.utils.Utils;

public class PostRenderGui {
	public static PostRenderGui instance = new PostRenderGui();
	public List<GuiNote> notes = new ArrayList<GuiNote>();
	public GuiTextString statsElement = new GuiTextString(0, 0, 0.8F, "");
	private List<String[]> titles = new ArrayList<String[]>();
	private static GuiTextString titleText, descriptionText;
	
	public PostRenderGui() {
		statsElement.shadow = true;
	}
	
	static {
		titleText = new GuiTextString(0, 0, 2, "");
		titleText.centered = true;
		
		descriptionText = new GuiTextString(0, 0, 1.5F, "");
		descriptionText.centered = true;
	}

	public void draw() {
		if(ModuleInfo.needUpdate == 2) return;
		Minecraft mc = Minecraft.getMinecraft();
		
		if(mc.currentScreen == null) {
			ScaledResolution var2 = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
			int screenWidth = var2.getScaledWidth();
            int screenHeight = var2.getScaledHeight();
            
			if(notes.size() == 0) {
				for(int i = 1; i < 11; i++) {
					String title = SettingsModule.getString("noteTitle" + i);
					String desc = SettingsModule.getString("noteDesc" + i);
					if((!title.equals("") || !desc.equals("")) && SettingsModule.getBool("notePined" + i)) {
						this.notes.add(new GuiNote(i, SettingsModule.getInt("notePosX" + i), SettingsModule.getInt("notePosY" + i), title, desc, false));
					}
				}
			}
			for(GuiNote note : notes) {
				note.draw(mc, 0, 0);
			}
		
			if(SettingsModule.getBool("stats") && ModuleInfo.needUpdate != 2) {
				int posId = SettingsModule.getInt("statsPos");
				
				String statsText = SettingsModule.getString("statsPattern");
				statsText = statsText.replaceAll("%money%", ModuleInfo.playerBalance)
				.replaceAll("%friends%", ModuleInfo.onlineFriends)
				.replaceAll("%os%", ModuleInfo.playerOS)
				.replaceAll("%uptime%", ModuleInfo.uptime)
				.replaceAll("%tps%", ModuleInfo.serverTPS);
				statsText = Utils.convertAmpCodes(statsText);
				
				statsElement.SetText(statsText);
				try {
					statsElement.setScale(Float.parseFloat(SettingsModule.getString("statsScale")));
				} catch(Exception e) {
					statsElement.setScale(0.8F);
				}
				if(posId == 0) {
					statsElement.posX = 5;
					statsElement.posY = 5;
					statsElement.positionedRight = false;
				}
				else if(posId == 1) {
					statsElement.posX = 5;
					statsElement.posY = screenHeight - statsElement.height - 5;
					statsElement.positionedRight = false;
				}
				else if(posId == 2) {
					statsElement.posX = screenWidth - 5;
					statsElement.posY = 5;
					statsElement.positionedRight = true;
				}
				else if(posId == 3) {
					statsElement.posX = screenWidth - 5;
					statsElement.posY = screenHeight - statsElement.height - 5;
					statsElement.positionedRight = true;
				}
				
				statsElement.draw(mc, 0, 0);
			}
			if(SettingsModule.getBool("radar") && !VariableProviderModer.nearPlayers.equals("")) {
				int stringWidth = mc.fontRenderer.getStringWidth(VariableProviderModer.nearPlayers);
				if(stringWidth > screenWidth/2-100) {
					VariableProviderModer.nearPlayers = mc.fontRenderer.trimStringToWidth(VariableProviderModer.nearPlayers, screenWidth/2-60)+"...";
					stringWidth = screenWidth/2-100;
				}
				RenderUtils.drawRect(screenWidth-7-stringWidth, screenHeight-3, screenWidth-5, screenHeight-15, GuiTheme.tabInfo);
				mc.fontRenderer.drawString(VariableProviderModer.nearPlayers, screenWidth-5-stringWidth, screenHeight-13, 14737632);
			}
			titleText.draw(mc, 0, 0);
			descriptionText.draw(mc, 0, 0);
		}
		else {
			if(notes.size() != 0) {
				notes.clear();
			}
			
			if(mc.currentScreen instanceof GuiChest && !VariableProviderModer.invPlayer.equals("")) {
				try {
					InventoryBasic inv = (InventoryBasic)Reflection.getPrivateValue(GuiChest.class, mc.currentScreen, "field_147015_w");
					if(inv.getInventoryName().equals("container.inventory")) {
						((InventoryBasic)Reflection.getPrivateValue(GuiChest.class, mc.currentScreen, "field_147015_w")).func_110133_a("Инвентарь: "+VariableProviderModer.invPlayer);
					}
					else if(inv.getInventoryName().equals("Equipped")) {
						((InventoryBasic)Reflection.getPrivateValue(GuiChest.class, mc.currentScreen, "field_147015_w")).func_110133_a("Экипировка: "+VariableProviderModer.invPlayer);
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
				VariableProviderModer.invPlayer = "";
			}
		}
	}
	
	@SubscribeEvent
	public void onRenderGameOverlay(RenderGameOverlayEvent.Pre event) {
		if(ModuleInfo.needUpdate != 2 && SettingsModule.getBool("tab")) KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindPlayerList.getKeyCode(), false);
	}
	
	@SubscribeEvent
	public void onRenderGameOverlay(RenderGameOverlayEvent.Text event) { //event.phase == TickEvent.Phase.END &&
		if(ModuleInfo.needUpdate != 2 && SettingsModule.getBool("tab")) {
			Minecraft mc = Minecraft.getMinecraft();
			EntityClientPlayerMP thePlayer = AbstractionLayer.getPlayer();
			if(Keyboard.isKeyDown(mc.gameSettings.keyBindPlayerList.getKeyCode()) && mc.currentScreen == null && thePlayer != null) {
				ScaledResolution var2 = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
				int width = var2.getScaledWidth();
				int height = var2.getScaledHeight();
				
				boolean stats = SettingsModule.getBool("tabStats");
				boolean moderStats = stats && ModuleInfo.permission >= 1;
				int pingNum = 0;
				
				int panelPosX = width/2-200;
				int panelPosY = moderStats ? 34 : 24;
				int statsPanelHeight = moderStats ? 24 : 14;
				
				RenderUtils.drawRect(panelPosX, 10, panelPosX+400, statsPanelHeight+10, GuiTheme.tabInfo);
				
				RenderUtils.drawRect(panelPosX, panelPosY, panelPosX+400, panelPosY+250, GuiTheme.tab);
				List<String> friends = Arrays.asList(SettingsModule.getString("friends").split(" "));
				
				ArrayList<GuiPlayerInfo> players = (ArrayList<GuiPlayerInfo>)thePlayer.sendQueue.playerInfoList;
				int playerNum = 0;
				for(GuiPlayerInfo playerInfo : players) {
					if(playerNum >= 100) break;
					int posX = (playerNum%4)*100+panelPosX;
					int posY = (playerNum/4)*10+panelPosY;
					GL11.glColor4f(0, 0, 0, 0.5F);
					RenderUtils.drawRect(posX, posY, posX+100, posY+10, GuiTheme.tabPlayer);
					
					if(stats && friends.indexOf(playerInfo.name) != -1) {
						mc.fontRenderer.drawString("§9"+playerInfo.name, posX+1, posY+1, 14737632);
					}
					else if(ModuleInfo.developers.indexOf(playerInfo.name) != -1) {
						mc.fontRenderer.drawString("§3"+playerInfo.name, posX+1, posY+1, 14737632);
					}
					else if(Utils.vipPlayers.indexOf(playerInfo.name) != -1) {
						mc.fontRenderer.drawString("§6"+playerInfo.name, posX+1, posY+1, 14737632);
					}
					else mc.fontRenderer.drawString(playerInfo.name, posX+1, posY+1, 14737632);
					
					if(playerInfo.name.equals(ModuleInfo.playerName)) pingNum = playerInfo.responseTime;
					
					playerNum++;
				}
				
				String serverName = "Single Player";
				String ping = "0";
				String onlineInfo = " §6[1/1]";
				
				ServerData serverData = mc.func_147104_D();
				
				if(serverData != null) {
					serverName = serverData.serverName;
					try {
						onlineInfo = " §6["+players.size()+"/"+Utils.clearAmpCodes(serverData.populationInfo).split("/")[1]+"]";
					} catch(Exception e) {onlineInfo = "1/1";}
					if(pingNum > 500) ping = "§c"+pingNum;
					else if(pingNum > 150) ping = "§e"+pingNum;
					else ping = "§a"+pingNum;
				}
				
				mc.fontRenderer.drawString(serverName+onlineInfo, panelPosX+3, 13, 14737632);
				mc.fontRenderer.drawString("Баланс: §6" + ModuleInfo.playerBalance, panelPosX+397-mc.fontRenderer.getStringWidth("Баланс: §6" + ModuleInfo.playerBalance), 13, 14737632);
				if(moderStats) {
					String tabInfo = "";
					if(ModuleInfo.permission >= 2 && SettingsModule.getBool("tpsFilter")) tabInfo+="§rTPS: " + ModuleInfo.serverTPS;
					if(ModuleInfo.permission >= 1 && SettingsModule.getBool("osFilter")) tabInfo+="  §rOS: " + ModuleInfo.playerOS;
					if(ModuleInfo.permission >= 2 && SettingsModule.getBool("tpsFilter")) tabInfo+="  §rUpTime: §6" + ModuleInfo.uptime;
					mc.fontRenderer.drawString(tabInfo, panelPosX+3, 23, 14737632);
				}
			}
		}
	}
	
	private int animState = 0;
	
	@SubscribeEvent
    public void onClientTick(ClientTickEvent event)
    {
		if(Minecraft.getMinecraft().currentScreen == null) {
			if(animState == 1) {
				titleText.scale = moveTo(titleText.scale, 2.4f, 0.05f);
				descriptionText.scale = moveTo(descriptionText.scale, 1.9f, 0.05f);
				if(titleText.scale >= 2.4f && descriptionText.scale >= 1.9f) animState = 2;
			}
			else if(animState == 2) {
				titleText.scale = moveTo(titleText.scale, 2f, 0.05f);
				descriptionText.scale = moveTo(descriptionText.scale, 1.5f, 0.05f);
				if(titleText.scale <= 2f && descriptionText.scale <= 1.5f) animState = 3;
			}
			else if(animState == 203) {
				titleText.scale = moveTo(titleText.scale, 2.4f, 0.05f);
				descriptionText.scale = moveTo(descriptionText.scale, 1.9f, 0.05f);
				if(titleText.scale >= 2.4f && descriptionText.scale >= 1.9f) animState = 204;
			}
			else if(animState == 204) {
				titleText.scale = moveTo(titleText.scale, 0f, 0.05f);
				descriptionText.scale = moveTo(descriptionText.scale, 0f, 0.05f);
				if(titleText.scale <= 0f && descriptionText.scale <= 0f) animState = 0;
			}
			else if(animState != 0) animState++;
		}
    }
	
	public void addTitle(String title, String description) {
		titles.add(new String[]{title, description});
		nextTitle();
	}
	
	public void nextTitle() {
		if(titles.size() <= 0) return;
		String[] t = titles.remove(0);
		titleText.SetText(t[0]);
		descriptionText.SetText(t[1]);
		titleText.scale = 0;
		descriptionText.scale = 0;
		animState = 1;
		
		Minecraft mc = Minecraft.getMinecraft();
		ScaledResolution scaled = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		titleText.posX = scaled.getScaledWidth()/2;
		titleText.posY = scaled.getScaledHeight()/2-75;
		
		descriptionText.posX = scaled.getScaledWidth()/2;
		descriptionText.posY = scaled.getScaledHeight()/2-50;
	}
	
	public float moveTo(float a, float b, float f) {
    	if(a > b) {
    		f *= -1;
    		return Math.max(a+f, b);
    	}
    	else return Math.min(a+f, b);
    }
}
