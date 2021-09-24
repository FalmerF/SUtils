package net.smb.moderutils.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.mojang.authlib.GameProfile;

import net.eq2online.console.Log;
import net.eq2online.macros.compatibility.Reflection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.smb.moderutils.Color;
import net.smb.moderutils.GuiTheme;
import net.smb.moderutils.ModuleInfo;
import net.smb.moderutils.ModuleSettings;
import net.smb.moderutils.ModuleUtils;
import net.smb.moderutils.RenderUtil;
import net.smb.moderutils.VariableProviderModer;

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
			ScaledResolution var2 = new ScaledResolution(mc);
			int screenWidth = var2.getScaledWidth();
            int screenHeight = var2.getScaledHeight();
            
			if(notes.size() == 0) {
				for(int i = 1; i < 11; i++) {
					String title = ModuleSettings.getString("noteTitle" + i);
					String desc = ModuleSettings.getString("noteDesc" + i);
					if((!title.equals("") || !desc.equals("")) && ModuleSettings.getBool("notePined" + i)) {
						this.notes.add(new GuiNote(i, ModuleSettings.getInt("notePosX" + i), ModuleSettings.getInt("notePosY" + i), title, desc, false));
					}
				}
			}
			for(GuiNote note : notes) {
				note.draw(mc, 0, 0);
			}
		
			if(ModuleSettings.getBool("stats") && ModuleInfo.needUpdate != 2) {
				int posId = ModuleSettings.getInt("statsPos");
				
				String statsText = ModuleSettings.getString("statsPattern");
				statsText = statsText.replaceAll("%money%", ModuleInfo.playerBalance)
				.replaceAll("%friends%", ModuleInfo.onlineFriends)
				.replaceAll("%os%", ModuleInfo.playerOS)
				.replaceAll("%uptime%", ModuleInfo.uptime)
				.replaceAll("%tps%", ModuleInfo.serverTPS);
				statsText = ModuleUtils.convertAmpCodes(statsText);
				
				statsElement.SetText(statsText);
				try {
					statsElement.setScale(Float.parseFloat(ModuleSettings.getString("statsScale")));
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
			if(ModuleSettings.getBool("radar") && !VariableProviderModer.nearPlayers.equals("")) {
				int stringWidth = mc.fontRenderer.getStringWidth(VariableProviderModer.nearPlayers);
				if(stringWidth > screenWidth/2-100) {
					VariableProviderModer.nearPlayers = mc.fontRenderer.trimStringToWidth(VariableProviderModer.nearPlayers, screenWidth/2-60)+"...";
					stringWidth = screenWidth/2-100;
				}
				RenderUtil.drawRect(screenWidth-7-stringWidth, screenHeight-3, screenWidth-5, screenHeight-15, GuiTheme.tabInfo);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				mc.fontRenderer.drawString("§f"+ModuleUtils.clearAmpCodes(VariableProviderModer.nearPlayers), screenWidth-5-stringWidth, screenHeight-13, 14737632);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
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
					if(inv.getName().equals("Инвентарь")) {
						((InventoryBasic)Reflection.getPrivateValue(GuiChest.class, mc.currentScreen, "field_147015_w")).setCustomName(VariableProviderModer.invPlayer);
					}
					else if(inv.getName().equals("Equipped")) {
						((InventoryBasic)Reflection.getPrivateValue(GuiChest.class, mc.currentScreen, "field_147015_w")).setCustomName(VariableProviderModer.invPlayer);
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
		if(ModuleInfo.needUpdate != 2 && ModuleSettings.getBool("tab")) KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindPlayerList.getKeyCode(), false);
	}
	
	@SubscribeEvent
	public void onRenderGameOverlay(RenderGameOverlayEvent.Text event) {
		if(ModuleInfo.needUpdate != 2 && ModuleSettings.getBool("tab")) {
			Minecraft mc = Minecraft.getMinecraft();
			EntityPlayerSP thePlayer = mc.player;
			if(Keyboard.isKeyDown(mc.gameSettings.keyBindPlayerList.getKeyCode()) && mc.currentScreen == null && thePlayer != null) {
				ScaledResolution var2 = new ScaledResolution(mc);
				int width = var2.getScaledWidth();
				int height = var2.getScaledHeight();
				
				boolean stats = ModuleSettings.getBool("tabStats");
				boolean moderStats = stats && ModuleInfo.permission >= 1;
				int pingNum = 0;
				
				int panelPosX = width/2-200;
				int panelPosY = moderStats ? 34 : 24;
				int statsPanelHeight = moderStats ? 24 : 14;
				
				RenderUtil.drawRect(panelPosX, 10, panelPosX+400, statsPanelHeight+10, GuiTheme.tabInfo);
				
				RenderUtil.drawRect(panelPosX, panelPosY, panelPosX+400, panelPosY+250, GuiTheme.tab);
				List<String> friends = Arrays.asList(ModuleSettings.getString("friends").split(" "));
				
				Collection<NetworkPlayerInfo> players = (Collection<NetworkPlayerInfo>)thePlayer.connection.getPlayerInfoMap();
				int playerNum = 0;
				for(NetworkPlayerInfo playerInfo : players) {
					try {
					if(playerNum >= 100) break;
					int posX = (playerNum%4)*100+panelPosX;
					int posY = (playerNum/4)*10+panelPosY;
					GL11.glColor4f(0, 0, 0, 0.5F);
					RenderUtil.drawRect(posX, posY, posX+100, posY+10, GuiTheme.tabPlayer);
					String playerName = ModuleUtils.clearAmpCodes(getPlayerName(playerInfo).replaceAll(" ", ""));
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					
					if(stats && friends.indexOf(playerName) != -1) {
						mc.fontRenderer.drawString("§9"+playerName, posX+11, posY+1, 14737632);
					}
					else if(ModuleInfo.developers.indexOf(playerName) != -1) {
						mc.fontRenderer.drawString("§3"+playerName, posX+11, posY+1, 14737632);
					}
					else if(ModuleUtils.vipPlayers.indexOf(playerName) != -1) {
						mc.fontRenderer.drawString("§6"+playerName, posX+11, posY+1, 14737632);
					}
					else mc.fontRenderer.drawString("§f"+playerName, posX+11, posY+1, 14737632);
					
					if(playerName.equals(ModuleInfo.playerName)) pingNum = playerInfo.getResponseTime();
					
					GameProfile gameprofile = playerInfo.getGameProfile();
					EntityPlayer entityplayer = mc.world.getPlayerEntityByUUID(gameprofile.getId());
                    boolean flag1 = entityplayer != null && entityplayer.isWearing(EnumPlayerModelParts.CAPE) && (gameprofile.getName().equals("Dinnerbone") || gameprofile.getName().equals("Grumm"));
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    mc.getTextureManager().bindTexture(playerInfo.getLocationSkin());
                    int l2 = 8 + (flag1 ? 8 : 0);
                    int i3 = 8 * (flag1 ? -1 : 1);
                    Gui.drawScaledCustomSizeModalRect(posX+1, posY+1, 8.0F, (float)l2, 8, i3, 8, 8, 64.0F, 64.0F);
                    if (entityplayer != null && entityplayer.isWearing(EnumPlayerModelParts.HAT))
                    {
                        int j3 = 8 + (flag1 ? 8 : 0);
                        int k3 = 8 * (flag1 ? -1 : 1);
                        Gui.drawScaledCustomSizeModalRect(posX+1, posY+1, 40.0F, (float)j3, 8, k3, 8, 8, 64.0F, 64.0F);
                    }
					
					playerNum++;
					} catch(Exception e) {}
				}
				
				String serverName = "Single Player";
				String ping = "0";
				String onlineInfo = " §6[1/1]";
				
				ServerData serverData = mc.getCurrentServerData();
				
				if(serverData != null) {
					serverName = serverData.serverName;
					try {
						onlineInfo = " §6["+players.size()+"/"+ModuleUtils.clearAmpCodes(serverData.populationInfo).split("/")[1]+"]";
					} catch(Exception e) {onlineInfo = "1/1";}
					if(pingNum > 500) ping = "§c"+pingNum;
					else if(pingNum > 150) ping = "§e"+pingNum;
					else ping = "§a"+pingNum;
				}
				
				mc.fontRenderer.drawString(serverName+onlineInfo, panelPosX+3, 13, 14737632);
				mc.fontRenderer.drawString("Баланс: §6" + ModuleInfo.playerBalance, panelPosX+397-mc.fontRenderer.getStringWidth("Баланс: §6" + ModuleInfo.playerBalance), 13, 14737632);
				if(moderStats) {
					String tabInfo = "";
					if(ModuleInfo.permission >= 2 && ModuleSettings.getBool("tpsFilter")) tabInfo+="§rTPS: " + ModuleInfo.serverTPS;
					if(ModuleInfo.permission >= 1 && ModuleSettings.getBool("osFilter")) tabInfo+="  §rOS: " + ModuleInfo.playerOS;
					if(ModuleInfo.permission >= 2 && ModuleSettings.getBool("tpsFilter")) tabInfo+="  §rUpTime: §6" + ModuleInfo.uptime;
					mc.fontRenderer.drawString(tabInfo, panelPosX+3, 23, 14737632);
				}
			}
		}
	}
	
	public String getPlayerName(NetworkPlayerInfo networkPlayerInfoIn)
    {
        return networkPlayerInfoIn.getDisplayName() != null ? networkPlayerInfoIn.getDisplayName().getFormattedText() : ScorePlayerTeam.formatPlayerName(networkPlayerInfoIn.getPlayerTeam(), networkPlayerInfoIn.getGameProfile().getName());
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
		ScaledResolution scaled = new ScaledResolution(mc);
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
