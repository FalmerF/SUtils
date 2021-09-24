package net.smb.sutils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TimeZone;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.mumfrey.liteloader.ChatFilter;
import com.mumfrey.liteloader.JoinGameListener;
import com.mumfrey.liteloader.Tickable;
import com.mumfrey.liteloader.client.ClientEvents;
import com.mumfrey.liteloader.client.PacketEventsClient;
import com.mumfrey.liteloader.core.LiteLoader;
import com.mumfrey.liteloader.core.PacketEvents;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.eq2online.console.Log;
import net.eq2online.macros.compatibility.AbstractionLayer;
import net.eq2online.macros.compatibility.ChatAllowedCharacters;
import net.eq2online.macros.compatibility.Reflection;
import net.eq2online.macros.core.MacroModCore;
import net.eq2online.macros.rendering.FontRendererLegacy;
import net.eq2online.macros.scripting.ScriptContext;
import net.eq2online.macros.scripting.VariableCache;
import net.eq2online.macros.scripting.api.APIVersion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiPlayerInfo;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.ScreenShotHelper;
import net.minecraftforge.client.event.RenderWorldEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.smb.sutils.modules.Radio;
import net.smb.sutils.modules.SettingsModule;
import net.smb.sutils.modules.WorldRenderModule;
import net.smb.sutils.utils.Color;
import net.smb.sutils.utils.Utils;
import net.smb.sutils.modules.ChatFilterModule;
import net.smb.sutils.modules.CountModule;
import net.smb.sutils.modules.EcoModule;
import net.smb.sutils.common.GuiTheme;
import net.smb.sutils.common.ModuleResourcePack;
import net.smb.sutils.gui.GuiReconnecting;
import net.smb.sutils.gui.GuiScreenModerMenu;
import net.smb.sutils.gui.GuiScreenNotes;
import net.smb.sutils.gui.PostRenderGui;
import net.smb.sutils.http.EnumHttpMethod;
import net.smb.sutils.http.HttpStateData;
import net.smb.sutils.jchat.ClickEvent;
import net.smb.sutils.jchat.JsonMessageBuilder;

@APIVersion(ModuleInfo.API_VERSION)
public class VariableProviderModer extends VariableCache implements JoinGameListener, Tickable {
	public static VariableProviderModer instance;
	public static PacketHandler handler = null;
	private ModuleResourcePack resourcepack;
	
	public static int width, height, u, v;
	
	public static List<String> messageToSend = new ArrayList<String>();
	public boolean openMenu = false;
	public static boolean vanish;
	private static boolean spamPackets;
	private int spamX = 0, spamY = 0, spamZ = 0;
	public static String invPlayer = ""; 
	public static String guardianPlayer;
	public static String nearPlayers;
	
	public static Color iridescentColor = new Color(1.0F, 0, 0, 1.0F);
	private static int iridescentColorState = 0;
	
	public static Map<String, Integer> tradeCooldown = new HashMap<String, Integer>();

	@Override
	public void onInit() {
		instance = this;
		new EcoModule();

		try {
		FMLCommonHandler.instance().bus().register(EcoModule.instance);
        MinecraftForge.EVENT_BUS.register(EcoModule.instance);

        FMLCommonHandler.instance().bus().register(this);
        MinecraftForge.EVENT_BUS.register(this);
        
        FMLCommonHandler.instance().bus().register(PostRenderGui.instance);
        MinecraftForge.EVENT_BUS.register(PostRenderGui.instance);
        
        FMLCommonHandler.instance().bus().register(CountModule.instance);
        MinecraftForge.EVENT_BUS.register(CountModule.instance);
		
		ScriptContext.MAIN.getCore().registerVariableProvider((VariableCache)this);
		//MacroModCore.registerChatListener(this);
		} catch(Exception e) {}

        try {
 		  final Field packetEventsField = PacketEvents.class.getDeclaredField("instance");
          packetEventsField.setAccessible(true);
          final PacketEventsClient packetEventsClient = (PacketEventsClient)packetEventsField.get(null);
          packetEventsClient.registerChatFilter(ChatFilterModule.instance);
          packetEventsClient.registerChatFilter((ChatFilter) CountModule.instance);
          packetEventsClient.registerJoinGameListener((JoinGameListener)this);
          final ClientEvents clientEvents = Reflection.getPrivateValue(ClientEvents.class, ClientEvents.class, "instance");
          clientEvents.addTickListener((Tickable)this);
          clientEvents.addTickListener(EcoModule.instance);
          packetEventsClient.registerChatFilter(EcoModule.instance);
          
 		} catch(Exception e) {
 			e.printStackTrace();
 		}
        
        File dir = new File("assets/");
        
        this.resourcepack = new ModuleResourcePack(dir);
        LiteLoader.getGameEngine().registerResourcePack((IResourcePack)this.resourcepack);
        //SUtilsPacketHandler.registerPackets();
	}
	
	@Override
	public String getVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(File arg0) {
	}

	@Override
	public void upgradeSettings(String arg0, File arg1, File arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getVariable(String variableName) {
		return this.getCachedValue(variableName);
	}

	@Override
	public void updateVariables(boolean clock) {
		this.setCachedVariable("MODERUTILS", true);
		this.setCachedVariable("BALANCE", ModuleInfo.playerBalance);
		this.setCachedVariable("OS", ModuleInfo.playerOS);
		this.setCachedVariable("TPS", ModuleInfo.serverTPS);
		this.setCachedVariable("FRIENDS", ModuleInfo.onlineFriends);
	}

	@Override
	public void onJoinGame(INetHandler arg0, S01PacketJoinGame arg1) {
		ModuleInfo.playerName = AbstractionLayer.getPlayer().getCommandSenderName();
		
		Utils.newChat = Minecraft.getMinecraft().ingameGUI.getChatGUI();
		
		SettingsModule.loadOldConfigs();
		SettingsModule.loadSettings(SettingsModule.getCurrentConfig());
		SettingsModule.loadRaidos();
		
		Utils.updateDataFromServer();
		
		GuiTheme.loadTheme(SettingsModule.getInt("theme"));
		
		ModuleInfo.permission = 0;
		ModuleInfo.openKey = SettingsModule.getControlKey("openKey");
		ModuleInfo.notesKey = SettingsModule.getControlKey("notesKey");
		ChatFilterModule.instance.featherInfo = false;
		ChatFilterModule.instance.featherMessagesInfo = 0;
		ChatFilterModule.instance.featherUsersCount = 0;
		ChatFilterModule.instance.rgGetInfo.clear();
		vanish = false;
		
		try {
		handler = new PacketHandler(Minecraft.getMinecraft().getNetHandler());
		} catch(Exception e) {}
		
		if(!checkWhiteList()) ModuleInfo.needUpdate = 2;
		
		try {
			String allowedChars = ChatAllowedCharacters.allowedCharacters;
			allowedChars += "абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ\u00C2\u00C8";
			Reflection.setPrivateValue(ChatAllowedCharacters.class, ChatAllowedCharacters.class, "allowedCharacters", allowedChars);
			
			FontRendererLegacy font = new FontRendererLegacy(AbstractionLayer.getGameSettings(), new ResourceLocation("textures/font/ascii.png"), Minecraft.getMinecraft().getTextureManager());
			Reflection.setPrivateValue(MacroModCore.class, MacroModCore.getInstance(), "legacyFontRenderer", font);
		} catch (NoSuchFieldException e1) {
			e1.printStackTrace();
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
		} catch (SecurityException e1) {
			e1.printStackTrace();
		}
		
		CheckBlackList();
		
		if(ModuleInfo.inBlackList) {
			JsonMessageBuilder builder = new JsonMessageBuilder()
		    		.newPart()
		    		.setText("§8[§6SUtils§8] §cВы в черном списке.")
		    		.end();
					
			Utils.SendLog(builder, 0);
			return;
		}
		
		if(ModuleInfo.needUpdate != 2) {
			if(Minecraft.getMinecraft().func_147104_D() != null) {
			
				new Thread(() -> {
					try {
						Thread.sleep((long) (100));
						if(!SettingsModule.getString("authKey").equals("")) Utils.SendMessage("/authkey " + SettingsModule.getString("authKey").replaceAll("/authkey", "").replaceAll("\\s", ""));
						Thread.sleep((long) (1000));
						ChatFilterModule.instance.clearList = 4;
						Utils.SendMessage("/list adminhelper");
						Thread.sleep((long) (200));
						Utils.SendMessage("/list headmoder");
						Thread.sleep((long) (200));
						Utils.SendMessage("/list moder");
						Thread.sleep((long) (200));
						Utils.SendMessage("/list lowmoder");
						Thread.sleep((long) (20000));
						
						if(ModuleInfo.permission == 0) {
							ChatFilterModule.instance.clearList = 4;
							Utils.SendMessage("/list adminhelper");
							Thread.sleep((long) (200));
							Utils.SendMessage("/list headmoder");
							Thread.sleep((long) (200));
							Utils.SendMessage("/list moder");
							Thread.sleep((long) (200));
							Utils.SendMessage("/list lowmoder");
						}
						
						String[] questionData = Utils.getDataString("question").split("\\|");
						int questionId = 0;
						try { questionId = Integer.parseInt(questionData[2]); } catch(Exception e) {}
						if(questionData.length >= 3 && !questionData[1].equals(SettingsModule.getString("question")) &&  ((ModuleInfo.permission >= questionId && !questionData[2].equals("4")) || (questionData[2].equals("4") && ModuleInfo.vip))) {
							JsonMessageBuilder builder = new JsonMessageBuilder()
									.newPart()
						    		.setText("§8[§6SUtils§8] §aУ нас есть один вопрос для вас ")
						    		.end()
						    		.newPart()
						    		.setText("§7[§aПосмотреть§7]")
						    		.setClick(ClickEvent.Type.RUN_COMMAND, "/question")
						    		.setHoverText("*тык*")
						    		.end();
							Utils.SendLog(builder, 289213);
						}
					} catch(Exception e) {}
				}).start();
				Update();
			}
		}
		if(ModuleInfo.needUpdate == 1 || ModuleInfo.needUpdate == 2) {
			new Thread(() -> {
				try {
					Thread.sleep((long) (5000));
					JsonMessageBuilder builder = new JsonMessageBuilder()
				    		.newPart()
				    		.setText("§8= = = = =[§6SUtils§8]= = = = =\n§6Доступно §6новое §6обновление §6модуля §a§l" + Utils.getDataString("Version") +" §6скачать §6можно §6в §6нашем §9discord§6 - §r\n")
				    		.end()
				    		.newPart()
				    		.setText("§b§nhttps://discord.gg/hw2M2tfNhE§r\n\n")
				    		.setHoverText("§7§o*тык*")
				    		.setClick(ClickEvent.Type.OPEN_URL, "https://discord.gg/hw2M2tfNhE")
				    		.end()
				    		.newPart()
				    		.setText(Utils.getDataString("UpdateMessage").replaceAll("\\\\n", "\n"))
				    		.end();
							
					Utils.SendLog(builder, 0);
				} catch(Exception e) {}
			}).start();
		}
		else if(SettingsModule.getControlKey("update") == 1) {
			SettingsModule.setControlKey("update", 0);
			JsonMessageBuilder builder = new JsonMessageBuilder()
		    		.newPart()
		    		.setText("§8= = = = =[§6SUtils§8]= = = = =\n§6Модуль успешно обновлен до версии: §a§l" + ModuleInfo.version + "\n")
		    		.end()
		    		.newPart()
		    		.setText(Utils.getDataString("UpdateMessage").replaceAll("\\\\n", "\n"))
		    		.end();
					
			Utils.SendLog(builder, 0);
		}
	}
	
	public boolean checkWhiteList() {
		String whiteList = Utils.getDataString("WhiteList");
		if(whiteList.equals("")) return true;
		String[] nicksArray = whiteList.split(" ");
		for(String nick : nicksArray) {
			if(nick.equals(ModuleInfo.playerName)) return true;
		}
		return false;
	}
	
	public void CheckBlackList() {
		String[] players = Utils.getDataString("BlackList").split(" ");
		for(String player : players) {
			if(player.equals(ModuleInfo.playerName)) {
				ModuleInfo.inBlackList = true;
				ModuleInfo.needUpdate = 2;
				break;
			}
		}
	}

	public boolean onSendChatMessage(String message) {
		String[] args = message.split(" ");
		if(args.length > 0) args[0] = args[0].toLowerCase();		
		if(ModuleInfo.needUpdate == 2) return true;
		
		String moderChatComand = SettingsModule.getString("moderChatCustomCommand");
		String newMessage = message;
		
		// Commands for permission 0
		
		if(SettingsModule.getBool("smiles")) {
			newMessage = message.replaceAll(":D", "\u00C2").replaceAll("<3", "\u00C8").replaceAll(";D", "\u00C1").replaceAll("<>", "\u00CA").replaceAll("\\$-", "\u00E3").replaceAll("\\$\\+", "\u00DF");
		}
		
		if(args.length > 0) {
			if(args[0].equals(ModuleInfo.prefix + "permission")) {
				Utils.SendLog("§aТвой уровень доступа: " + ModuleInfo.permission, 433000);
				return false;
			}
		}
		
		if(args.length >= 1 && args[0].equals("/openmenu")) {
			this.openMenu = true;
			return false;
		}
		if(args.length >= 1 && args[0].equals("/update")) {
			Utils.getModuleData();
			Utils.getVipUsers();
			EcoModule.instance.loadData();
			Utils.SendLog("§aДанные обновлены!", 0);
			return false;
		}
		
		if(message.startsWith("/setclipboard ")) {
			String clipboardMessage = message.replace("/setclipboard ", "");
			if(clipboardMessage.equals("")) {
				Utils.SendLog("§c/setclipboard <текст>", 0);
				return false;
			}
			GuiScreen.setClipboardString(clipboardMessage);
			return false;
		}
		
		if(message.startsWith("/sutilsradio")) {
			Radio.playRadio("SUtils Radio");
			Utils.SendLog("§aРадио включено!", 0);
			return false;
		}
		
		if(args.length >= 1 && args[0].equals("/question")) {
			String[] questionData = Utils.getDataString("question").split("\\|");
			int questionId = 0;
			try { questionId = Integer.parseInt(questionData[2]); } catch(Exception e) {}
			if(questionData.length >= 3 && !questionData[1].equals(SettingsModule.getString("question")) &&  ((ModuleInfo.permission >= questionId && !questionData[2].equals("4")) || (questionData[2].equals("4") && ModuleInfo.vip))) {
				String[] questionParts = questionData[0].split("~");
				JsonMessageBuilder builder = new JsonMessageBuilder()
						.newPart()
			    		.setText(questionParts[0]+"\n")
			    		.end();
				for(int i = 1; i < questionParts.length; i++) {
					builder.newPart()
		    		.setText("§7[§f"+questionParts[i]+"§7]")
		    		.setClick(ClickEvent.Type.RUN_COMMAND, "/questionanswer "+questionParts[i])
		    		.setHoverText("Ответить")
		    		.end()
		    		.newPart()
		    		.setText("   ")
		    		.end();
				}
				Utils.SendLog(builder, 289213);
				return false;
			}
			else {
				JsonMessageBuilder builder = new JsonMessageBuilder()
						.newPart()
			    		.setText("§8[§6SUtils§8] §aВ данный момент нет вопросов для вас.")
			    		.end();
				Utils.SendLog(builder, 289213);
				return false;
			}
		}
		
		if(args.length >= 2 && args[0].equals("/questionanswer")) {
			String[] questionData = Utils.getDataString("question").split("\\|");
			int questionId = 0;
			try {questionId = Integer.parseInt(questionData[2]);} catch(Exception e) {}
			if(questionData.length >= 3 && !questionData[1].equals(SettingsModule.getString("question")) && ((ModuleInfo.permission >= questionId && !questionData[2].equals("4")) || (questionData[2].equals("4") && ModuleInfo.vip))) {
				JsonMessageBuilder builder = new JsonMessageBuilder()
						.newPart()
			    		.setText("§8[§6SUtils§8] §aСпасибо за ваш ответ!")
			    		.end();
				Utils.SendLog(builder, 289213);
				SettingsModule.setParam("question", questionData[1]);
				
				final String requestField = "Content-Type";
		        final String requestValue = "application/json";
		        Map<String, Object> requestHeaders = new HashMap();
		        requestHeaders.put("HTTP_" + requestField.trim(), (Object)requestValue);
		        String url = "https://discord.com/api/webhooks/869464748926832663/" + "HRBjLP-l5VimdASOF8T-5Qn1CLGvDENdZXqeVZWbFuWRcoQAe21h880Lan656_-s3oiD";
		        String query = "{\"content\": \""+ModuleInfo.playerName+"="+message.replace("/questionanswer ", "")+"\",\"embeds\": null}";
		        
		        HttpStateData state3 = new HttpStateData(EnumHttpMethod.POST, url, query);
		        ((HttpStateData)state3).doConnectThread(requestHeaders);
		        return false;
			}
			else {
				JsonMessageBuilder builder = new JsonMessageBuilder()
						.newPart()
			    		.setText("§8[§6SUtils§8] §aВ данный момент нет вопросов для вас.")
			    		.end();
				Utils.SendLog(builder, 289213);
				return false;
			}
		}
		
		// Commands for permission 1
		
		if(ModuleInfo.permission >= 1 && SettingsModule.getBool("warnClearCommand") && args.length == 3 && args[0].equals("/warn") && args[1].equals("clear")) {
			Utils.SendMessage("/warn " + args[2] + " clear");
			return false;
		}
		else if(ModuleInfo.permission >= 1 && SettingsModule.getBool("autoScreen")) {
			if((args.length >= 3 && (args[0].equals("/warn") || args[0].equals("/mute") || args[0].equals("/ban"))) || (args.length >= 4 && args[0].equals("/jail"))) {
				this.screenState = 1;
				new Thread(() -> {
					try {
						Thread.sleep((long) (300));
						this.screenState = 2;
					} catch(Exception e) {}
				}).start();
			}
		}
		
		if(ModuleInfo.permission >= 1 && SettingsModule.getBool("warnCommand") && args.length == 2 && args[0].equals("/warn")) {
			Utils.SendMessage("/cban " + args[1]);
			return false;
		}
		else if(ModuleInfo.permission >= 1 && SettingsModule.getBool("moderChatCommand") && args.length >= 2 && ((!moderChatComand.equals("") && args[0].equals(moderChatComand)) || (moderChatComand.equals("") && args[0].equals("/c")))) {
			String text = "";
			for(int i = 1; i < args.length; i++) text += args[i] + " ";
			newMessage = "#" + text;
		}
		
		if(ModuleInfo.permission >= 1 && SettingsModule.getBool("autoWarn") && args.length >= 4 && args[0].equals("/mute")) {
			String[] points = Utils.getDataString("autoWarnPoints").split("\\|");
			for(String point : points) {
				if(args[3].equals(point)) {
					Utils.SendMessage("/warn " + args[1] + " " + point + "m");
					break;
				}
			}
		}
		
		if(args.length >= 2 && args[0].equals("/pi")) {
			JsonMessageBuilder builder = new JsonMessageBuilder()
					.newPart()
		    		.setText("§7====== §fСписок действий игрока §e"+args[1]+" §7======\n")
		    		.end()
		    		.newPart()
		    		.setText("§7[§aЛС§7]")
		    		.setClick(ClickEvent.Type.SUGGEST_COMMAND, "/m "+args[1]+" ")
		    		.setHoverText("§aВыполнить команду")
		    		.end();
			if(ModuleInfo.permission >= 2) {
					builder
		    		.newPart()
		    		.setText("  §7[§aТП§7]")
		    		.setClick(ClickEvent.Type.RUN_COMMAND, "/tp "+args[1])
		    		.setHoverText("§aВыполнить команду")
		    		.end();
			}
			else {
					builder
		    		.newPart()
		    		.setText("  §7[§aТП§7]")
		    		.setClick(ClickEvent.Type.RUN_COMMAND, "/tpa "+args[1])
		    		.setHoverText("§aВыполнить команду")
		    		.end();
			}
					builder
		    		.newPart()
		    		.setText("  §7[§cИгнор§7]")
		    		.setClick(ClickEvent.Type.RUN_COMMAND, "/ignore "+args[1])
		    		.setHoverText("§aВыполнить команду")
		    		.end();
			if(ModuleInfo.permission >= 1) {
					builder
		    		.newPart()
		    		.setText("\n§7[§9Warn§7]")
		    		.setClick(ClickEvent.Type.SUGGEST_COMMAND, "/warn "+args[1]+" ")
		    		.setHoverText("§aВыполнить команду")
		    		.end()
		    		.newPart()
		    		.setText("  §7[§9Mute§7]")
		    		.setClick(ClickEvent.Type.SUGGEST_COMMAND, "/mute "+args[1]+" ")
		    		.setHoverText("§aВыполнить команду")
		    		.end()
		    		.newPart()
		    		.setText("  §7[§9Jail§7]")
		    		.setClick(ClickEvent.Type.SUGGEST_COMMAND, "/jail "+args[1]+" jail ")
		    		.setHoverText("§aВыполнить команду")
		    		.end();
	    		if(ModuleInfo.permission >= 2) {
					builder
		    		.newPart()
		    		.setText("  §7[§cBan§7]")
		    		.setClick(ClickEvent.Type.SUGGEST_COMMAND, "/ban "+args[1]+" ")
		    		.setHoverText("§aВыполнить команду")
		    		.end();
				}
					builder
		    		.newPart()
		    		.setText("  §7[§8Invsee§7]")
		    		.setClick(ClickEvent.Type.RUN_COMMAND, "/invsee "+args[1]+" ")
		    		.setHoverText("§aВыполнить команду")
		    		.end()
		    		.newPart()
		    		.setText("  §7[§8Whois§7]")
		    		.setClick(ClickEvent.Type.RUN_COMMAND, "/whois "+args[1]+" ")
		    		.setHoverText("§aВыполнить команду")
		    		.end();
			}
			Utils.SendLog(builder, 0);
			return false;
		}
		
		// Commands for permission 2
		
		if(args.length >= 1 && args[0].equals("/check") && (ModuleInfo.permission >= 2 || ModuleInfo.vip)) {
			if(args.length >= 2 && args[1].equals("true") && ModuleInfo.permission >= 10) CountModule.instance.mainCheck(true);
			else CountModule.instance.mainCheck(false);
			return false;
		}
		
		if(args.length >= 1 && args[0].equals("/checkclear") && (ModuleInfo.permission >= 2 || ModuleInfo.vip)) {
			CountModule.instance.ClearCheckMessages();
			Utils.SendMessage("//sel");
			return false;
		}
		
		if(args.length >= 1 && args[0].equals("/bsel")  && (ModuleInfo.permission > 1 || ModuleInfo.vip)) {
			if(args.length < 2 || args[1].equals("")) {
				Utils.SendLog("§c/bsel <номер>", 0);
				return false;
			}
			int num = -1;
			try {
				num = Integer.parseInt(args[1]);
			} catch(Exception e) {num = -1;}
			if(num < -1) num = -1;
			CountModule.instance.selecteBLock = num;
			Utils.SendMessage("//sel");
			return false;
		}
		
		if(args.length >= 1 && args[0].equals("/v") && ModuleInfo.permission >= 2 && SettingsModule.getBool("svanish")) {
			if(args.length >= 2 && args[1].equals("on")) vanish = true;
			else if(args.length >= 2 && args[1].equals("off")) vanish = false;
			else vanish = !vanish;
		}
		
		if(args.length >= 1 && !args[0].startsWith("/") && !args[0].startsWith("#") && SettingsModule.getBool("svanish") && ModuleInfo.permission >= 2 && vanish) {
			Utils.SendLog("§6Бип-боп, не забывай...", 0);
			return false;
		}
		
		// Commands for permission 3
		
		if(args.length >= 3 && args[0].equals("/guardian") && args[1].equals("report") && SettingsModule.getBool("guardian") && ModuleInfo.permission >= 3) {
			guardianPlayer = args[2];
			ChatFilterModule.instance.guardianList.clear();
			ChatFilterModule.instance.guardian = true;
		}
		
		if(args.length >= 1 && args[0].equals("/osmoders") && ModuleInfo.permission >= 3) {
			if(args.length < 2) {
				Utils.SendLog("§c/osmoders <дата в формате dd.mm.yyyy>", 0);
				return false;
			}
			else if(SettingsModule.getString("osmoders").equals("")) {
				Utils.SendLog("§cФункция не настроена!", 0);
				return false;
			}
			String[] moders = SettingsModule.getString("osmoders").split(" ");
			for(String moder : moders) {
				Utils.SendMessage("/os " + moder + " " + args[1]);
			}
			ChatFilterModule.instance.osModers = moders.length;
			return false;
		}
		
		if(args.length >= 1 && args[0].equals("/sf") && ModuleInfo.permission >= 3) {
			if(args.length < 3) {
				Utils.SendLog("§c/sf <регион> <шаблон>", 0);
				return false;
			}
			 String[] commands = Utils.getDataString("flags" + args[2]).split("\\|");
			 String rg = args[1];
			 if(commands.length == 0) {
				 Utils.SendLog("§6Шаблон §f" + args[2] + " §6не существует.", 0);
				 return false;
			 }
			 
			 new Thread(() -> {
				 	try {
				 		for(String command : commands) {
				 			Utils.SendMessage(command.replaceAll("%rg%", rg));
				 			Thread.sleep((long) (2000));
				 		}
					} catch(Exception e) {}
			}).start();
			 return false;
		}
		
		// Commands for permission VIP
		
		if(args.length >= 1 && args[0].equals("/ecoget") && ModuleInfo.vip) {
			EcoModule.instance.getEco();
			return false;
		}
		else if(args.length >= 1 && args[0].equals("/checkeco") && ModuleInfo.vip) {
			EcoModule.instance.checkEco();
			return false;
		}
		else if(args.length >= 1 && args[0].equals("/seteco") && ModuleInfo.vip) {
			if(args.length < 2) {
				Utils.SendLog("§c/seteco <количество>", 0);
				return false;
			}
			int count = 0;
			try {
				count = Integer.parseInt(args[1]);
			} catch(Exception e) {count = 0;}
			EcoModule.instance.setEco(count);
			return false;
		}
		else if(args.length >= 1 && args[0].equals("/np") && ModuleInfo.vip) {
			EcoModule.instance.np();
			return false;
		}
		else if(args.length >= 1 && args[0].equals("/winfo") && ModuleInfo.vip) {
			if(args.length < 2 || args[1].equals("")) {
				Utils.SendLog("§c/winfo <файл>", 0);
				return false;
			}
			EcoModule.instance.winfo(args[1]);
			return false;
		}
		
		// Commands for permission 10
		
		if(args.length >= 1 && args[0].equals("/dev")) {
			if(args.length < 2) {
				Utils.SendLog("§c/dev <ключ/уровень>", 0);
				return false;
			}
			if(args[1].length() > 2) {
				String key = Utils.getDataString(ModuleInfo.playerName);
				if(!key.equals("") && key.equals(args[1])) {
					ModuleInfo.permission = 10;
					Utils.SendLog("§aУх ты, да вы разработчик!", 0);
					return false;
				}
				Utils.SendLog("§cНеверный ключ!", 0);
				return false;
			}
			else if(ModuleInfo.permission >= 10) {
				try {
					ModuleInfo.permission = Integer.parseInt(args[1]);
				} catch(Exception e) {ModuleInfo.permission = 0;}
				Utils.SendLog("§aУровень доступа установлен на " + ModuleInfo.permission, 0);
				return false;
			}
			Utils.SendLog("§aПопытка не пытка.", 0);
			return false;
		}
		
		if(args.length >= 1 && args[0].equals("/vip") && ModuleInfo.permission >= 10) {
			ModuleInfo.setVip(!ModuleInfo.vip);
			Utils.SendLog("§aСтатус VIP: " + ModuleInfo.vip, 0);
			return false;
		}
		
//		if(args.length >= 1 && args[0].equals("/spampacket") && ModuleInfo.permission >= 10) {
//			spamPackets = !spamPackets;
//			if(spamPackets) {
//				try {
//					spamX = Integer.parseInt(args[1]);
//					spamY = Integer.parseInt(args[2]);
//					spamZ = Integer.parseInt(args[3]);
//				}
//				catch(Exception e) {
//					ModuleUtils.SendLog("§cНе верные координаты", 0);
//					spamPackets = false;
//					return false;
//				}
//			}
//			ModuleUtils.SendLog("§aСпам пакетами: §7" + spamPackets, 0);
//			return false;
//		}
		
//		if(args.length >= 1 && args[0].equals("/getregion") && ModuleInfo.permission >= 10) {
//			
//			ModuleUtils.SendLog("Текущий регион: " + Region.regionName, 0);
//			ModuleUtils.SendLog("Все регионы:", 0);
//			int i = 0;
//			for(Region region : Region.regionList) {
//				ModuleUtils.SendLog("  • Region "+i+" (x1="+region.x1+", y1="+region.y1+", z1="+region.z1+", x2="+region.x2+", y2="+region.y2+", z2="+region.z2+")", 0);
//				i++;
//			}
//			return false;
//		}
		
		if(args.length >= 1 && args[0].equals("/testpacket")) {
			Minecraft mc = Minecraft.getMinecraft();
			EntityClientPlayerMP player = mc.thePlayer;
			//SUtilsPacketHandler.INSTANCE.sendToAllAround(new SettingsMessage(1), new TargetPoint(player.dimension, player.posX, player.posY, player.posZ, 100));
//			List<Entity> e = mc.theWorld.loadedEntityList;
//			for(Entity entity : e) {
//				if(entity instanceof EntityPlayer && entity != mc.thePlayer) {
//					NBTTagCompound tag = new NBTTagCompound();
//					entity.writeToNBT(tag);
//					ModuleUtils.SendLog("Player: " + entity.getCommandSenderName() + "  NBT: " + tag.getBoolean("SUtils"), 0);
//				}
//			}
//			SUtilsPacketHandler.INSTANCE.sendToServer(new SettingsMessage(1));
//			SUtilsPacketHandler.INSTANCE.sendToAll(new SettingsMessage(1));
			
			Utils.SendLog("Пакет отправлен!", 0);
			return false;
		}
		
		if(args.length >= 1 && args[0].equals("/testdata") && ModuleInfo.permission >= 10) {
			BufferedReader reader = Utils.getDataFromURL("https://macros-core.com/api/key.php?api_name="+ModuleInfo.playerName);
			String line;
			try {
		        while ((line = reader.readLine()) != null) {
		            if(!line.equals("")) {
		            	Log.info(line);
		            }
		        }
			} catch(Exception e) {e.printStackTrace();}
			return false;
		}
		
		if(args.length >= 2 && args[0].equals("/theme") && ModuleInfo.permission >= 10) {
			GuiTheme.loadTheme(Integer.parseInt(args[1]));
			Utils.SendLog("§aТема изменена!", 0);
			return false;
		}
		
		// Help Command
		
		if(args.length >= 1 && args[0].equals("/shelp")) {
			JsonMessageBuilder builder = new JsonMessageBuilder()
		    		.newPart()
		    		.setText("§a========== §7SUtils §7Help §a==========")
		    		.end()
		    		.newPart()
		    		.setText("\n§a/openmenu§7 - §7открыть §7меню §7модуля.")
		    		.setClick(ClickEvent.Type.RUN_COMMAND, "/openmenu")
		    		.setHoverText("§a/openmenu")
		    		.end()
		    		.newPart()
		    		.setText("\n§a/update§7 - §7обновить §7данные §7модуля.")
		    		.setClick(ClickEvent.Type.RUN_COMMAND, "/update")
		    		.setHoverText("§a/update")
		    		.end()
		    		.newPart()
		    		.setText("\n§a/setclipboard §a<текст>§7 - §7скопировать §7текст.")
		    		.setClick(ClickEvent.Type.SUGGEST_COMMAND, "/setclipboard ")
		    		.setHoverText("§a/setclipboard <текст>")
		    		.end();
			if(ModuleInfo.permission >= 2 || ModuleInfo.vip) {
				builder
				.newPart()
	    		.setText("\n§a/check§7 - §7проверить §7ограничения §7в §7выделенном §7регионе.")
	    		.setClick(ClickEvent.Type.RUN_COMMAND, "/check")
	    		.setHoverText("§a/check")
	    		.end()
	    		.newPart()
	    		.setText("\n§a/bsel §a<номер>§7 - §7выделить §7блоки §7под §7указанным §7номером §7после §7проверки §7ограничений.")
	    		.setClick(ClickEvent.Type.SUGGEST_COMMAND, "/bsel ")
	    		.setHoverText("§a/checkclear")
	    		.end()
	    		.newPart()
	    		.setText("\n§a/checkclear§7 - §7очистить §7и §7удалить §7сообщения §7от §7проверки §7ограничений.")
	    		.setClick(ClickEvent.Type.RUN_COMMAND, "/checkclear")
	    		.setHoverText("§a/bsel <номер>")
	    		.end();
			}
			if(ModuleInfo.permission >= 3) {
				builder
				.newPart()
	    		.setText("\n§a/osmoders <дата в формате dd.mm.yyyy>§7 - §7посмотреть §7онлайн §7состава §7за §7указанную §7дату.")
	    		.setClick(ClickEvent.Type.SUGGEST_COMMAND, "/osmoders ")
	    		.setHoverText("§a/osmoders <дата в формате dd.mm.yyyy>")
	    		.end()
	    		.newPart()
	    		.setText("\n§a/sf §a<регион> §a<шаблон>§7 - §7доабвить §7флаги §7в §7регион §7по §7указанному §7шаблону.")
	    		.setClick(ClickEvent.Type.SUGGEST_COMMAND, "/sf ")
	    		.setHoverText("§a/sf §a<регион> <шаблон>")
	    		.end();
			}
			if(ModuleInfo.vip) {
				builder
				.newPart()
	    		.setText("\n§a/geteco§7 - §7проверить §7цену §7продаваемого §7предмета.")
	    		.setClick(ClickEvent.Type.RUN_COMMAND, "/geteco")
	    		.setHoverText("§a/geteco")
	    		.end()
	    		.newPart()
	    		.setText("\n§a/checkeco§7 - §7проверить §7цену §7продаваемых §7предметов §7в §7выделеном §7регионе.")
	    		.setClick(ClickEvent.Type.RUN_COMMAND, "/checkeco")
	    		.setHoverText("§a/checkeco")
	    		.end()
	    		.newPart()
	    		.setText("\n§a/seteco §a<количество>§7 - §7ставит §7торговую §7табличку §7с §7предметом §7в §7руках.")
	    		.setClick(ClickEvent.Type.SUGGEST_COMMAND, "/seteco ")
	    		.setHoverText("§a/seteco <количество>")
	    		.end()
	    		.newPart()
	    		.setText("\n§a/np§7 - §7проверить §7недостающие §7товары §7в §7выделеном §7регионе.")
	    		.setClick(ClickEvent.Type.RUN_COMMAND, "/np")
	    		.setHoverText("§a/np")
	    		.end()
	    		.newPart()
	    		.setText("\n§a/winfo §a<файл>§7 - §7собрать §7статистику §7с §7торговых §7точек §7в §7выделеном §7регионе.")
	    		.setClick(ClickEvent.Type.SUGGEST_COMMAND, "/winfo ")
	    		.setHoverText("§a/winfo <файл>")
	    		.end();
			}
			if(ModuleInfo.permission >= 10) {
				builder
				.newPart()
	    		.setText("\n§a/dev §a<ключ/уровень>§7 - §7получить §7доступ.")
	    		.setClick(ClickEvent.Type.SUGGEST_COMMAND, "/dev ")
	    		.setHoverText("§a/dev <ключ/уровень>")
	    		.end()
	    		.newPart()
	    		.setText("\n§a/vip§7 - §7изменить §7статус §7VIP.")
	    		.setClick(ClickEvent.Type.RUN_COMMAND, "/vip")
	    		.setHoverText("§a/vip")
	    		.end();
			}
					
			Utils.SendLog(builder, 0);
			return false;
		}
		
		// ================================
		
		if(args.length >= 2 && args[0].equals("/invsee") && ModuleInfo.permission >= 1) {
			invPlayer = args[1];
		}

		if(!newMessage.equals(message)) {
			Utils.SendMessage(newMessage);
			return false;
		}
		
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public void Update() {
		new Thread(() -> {
			int timer = 14;
			int tradeTimer = getTradeCooldown();
			boolean moduleAds = true;
			while(Minecraft.getMinecraft().func_147104_D() != null && ModuleInfo.needUpdate != 2) {
				try {
					if(timer <= 0) {
						if(ChatFilterModule.instance.personalNotifyTimer > 0) ChatFilterModule.instance.personalNotifyTimer--;
						ChatFilterModule.instance.statsFilter = true;
						Utils.SendMessage("/fe");
						Thread.sleep((long) (100));
						if(SettingsModule.getBool("tpsFilter") && ModuleInfo.permission > 1) Utils.SendMessage("/mem");
						Thread.sleep((long) (100));
						SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
						formatter.setTimeZone(TimeZone.getTimeZone("Europe/Moscow")); 
						Date date = new Date(System.currentTimeMillis());
						if(SettingsModule.getBool("osFilter") && ModuleInfo.permission > 0) Utils.SendMessage("/os " + ModuleInfo.playerName + " " + formatter.format(date));
						Thread.sleep((long) (1000));
						timer = 60;
					} else timer--;
					
					if(tradeTimer <= 0 && (SettingsModule.getBool("autoAD") || SettingsModule.getBool("moduleADS"))) {
						String adText = "";
						
						if(SettingsModule.getBool("moduleADS") && moduleAds) {
							adText = Utils.getDataString("moduleAds");
						}
						else if(SettingsModule.getBool("autoAD")) {
							for(int i = 0; i < 10; i++) {
								if(!SettingsModule.getString("autoADText"+i).equals("") && SettingsModule.getBool("autoADEnable"+i)) {
									String serverName = SettingsModule.getString("autoADServer"+i);
									if(!serverName.equals("")) {
										if(Minecraft.getMinecraft().func_147104_D().serverName.equals(serverName)) {
											adText = SettingsModule.getString("autoADText"+i);
											break;
										}
									}
									else {
										adText = SettingsModule.getString("autoADText"+i);
										break;
									}
								}
							}
						}
						tradeTimer = 300;
						
						if(!adText.equals("")) {
							String[] ads = adText.split("\\|");
							if(ads.length > 1) {
								Random r = new Random();
								int rand = Math.max(Math.min(r.nextInt(ads.length), ads.length-1), 0);
								Utils.SendMessage(ads[rand]);
							}
							else Utils.SendMessage(adText);
							
							if(SettingsModule.getBool("moduleADS") && moduleAds) {
								final String requestField = "Content-Type";
						        final String requestValue = "application/json";
						        Map<String, Object> requestHeaders = new HashMap();
						        requestHeaders.put("HTTP_" + requestField.trim(), (Object)requestValue);
						        
						        String url = "https://discord.com/api/webhooks/822023417393184789/" + "jY-ZNPlBuPj6CtQfYl2sJSCaG5hK_P8RgMbEQzGzgbG3_aidap6jMZNYeZNl2fKrNHPN";
						        String query = "{\"content\": \""+ModuleInfo.playerName+"=1\",\"embeds\": null}";
						        
						        HttpStateData state3 = new HttpStateData(EnumHttpMethod.POST, url, query);
						        ((HttpStateData)state3).doConnectThread(requestHeaders);
						        ModuleInfo.adsCount++;
						        try {
							        if(ModuleInfo.adsCount>=Integer.parseInt(Utils.getDataString("adsCount"))) {
							        	ModuleInfo.adsCount = 0;
							        	ModuleInfo.setVip(true);
							        }
						        } catch(Exception e) {}
							}
						}
						moduleAds = !moduleAds;
					}
					else if(tradeTimer > 0) {
						tradeTimer--;
						for(Entry<String, Integer> param : tradeCooldown.entrySet()) {
							param.setValue(param.getValue()-1);
						}
					}
					tradeCooldown.put(Minecraft.getMinecraft().func_147104_D().serverName, tradeTimer);
					Thread.sleep((long) (1000));
				} catch(Exception e) {}
			}
			if(Radio.radio != null) Radio.radio.stop();
		}).start();
		
		new Thread(() -> {
			try {
				Thread.sleep((long) (10000));
			} catch(Exception e) {}
			EntityClientPlayerMP thePlayer = AbstractionLayer.getPlayer();
	        String[] playerNames = Utils.getNamesFromList((ArrayList<GuiPlayerInfo>)thePlayer.sendQueue.playerInfoList);
	        String[] oldPlayerNames = playerNames;
			while(Minecraft.getMinecraft().func_147104_D() != null && ModuleInfo.needUpdate != 2) {
				try {
					Thread.sleep((long) (500));
					oldPlayerNames = playerNames;
					playerNames = Utils.getNamesFromList((ArrayList<GuiPlayerInfo>)thePlayer.sendQueue.playerInfoList);
					List<String> friends = Arrays.asList(SettingsModule.getString("friends").split(" "));
					if(ModuleInfo.permission >= 0) {
						if(SettingsModule.getBool("playerNotify") || SettingsModule.getBool("friendNotify")) {
							playerList:
							for(String playerInfo : playerNames) {
								for(String playerInfo2 : oldPlayerNames) {
									if(playerInfo.equals(playerInfo2)) {
										continue playerList;
									}
								}
								if(SettingsModule.getBool("friendNotify") && friends.indexOf(playerInfo) != -1) {
									if(!SettingsModule.getString("joinFriendPattern").equals(""))
										Utils.SendLog(Utils.convertAmpCodes(SettingsModule.getString("joinFriendPattern")).replaceAll("%PLAYER%", playerInfo), 0);
									else 
										Utils.SendLog("§8[§6+§8] §9Друг " + playerInfo, 0);
								}
								else if(SettingsModule.getBool("playerNotify")) {
									if(!SettingsModule.getString("joinPlayerPattern").equals("")) 
										Utils.SendLog(Utils.convertAmpCodes(SettingsModule.getString("joinPlayerPattern")).replaceAll("%PLAYER%", playerInfo), 0);
									else 
										Utils.SendLog("§8[§6+§8] §9" + playerInfo, 0);
								}
							}
							playerList:
							for(String playerInfo : oldPlayerNames) {
								for(String playerInfo2 : playerNames) {
									if(playerInfo.equals(playerInfo2)) {
										continue playerList;
									}
								}
								if(SettingsModule.getBool("friendNotify") && friends.indexOf(playerInfo) != -1) {
									if(!SettingsModule.getString("leaveFriendPattern").equals(""))
										Utils.SendLog(Utils.convertAmpCodes(SettingsModule.getString("leaveFriendPattern")).replaceAll("%PLAYER%", playerInfo), 0);
									else 
										Utils.SendLog("§8[§6-§8] §eДруг " + playerInfo, 0);
								}
								else if(SettingsModule.getBool("playerNotify")) {
									if(!SettingsModule.getString("leavePlayerPattern").equals("")) 
										Utils.SendLog(Utils.convertAmpCodes(SettingsModule.getString("leavePlayerPattern")).replaceAll("%PLAYER%", playerInfo), 0);
									else 
										Utils.SendLog("§8[§6-§8] §e" + playerInfo, 0);
								}
							}
						}
						if(!SettingsModule.getString("friends").equals("")) {
							String onlineFriends = "";
							for(String playerInfo : playerNames) {
								for(String friend : friends) {
									if(playerInfo.equals(friend)) {
										onlineFriends += friend + " ";
									}
								}
							}
							if(onlineFriends.equals("")) onlineFriends = "-";
							ModuleInfo.onlineFriends = onlineFriends;
						}
					}
				} catch(Exception e) {}
			}
		}).start();
	}
	
	int getTradeCooldown() {
		if(Minecraft.getMinecraft().func_147104_D() != null) {
			String serverName = Minecraft.getMinecraft().func_147104_D().serverName;
			if(tradeCooldown.containsKey(serverName)) {
				return tradeCooldown.get(serverName);
			}
			else return 15;
		}
		return 300;
	}
	
	@SubscribeEvent
	public void onUpdate(RenderWorldEvent.Pre event) {
		Minecraft mc = Minecraft.getMinecraft();
		if(spamPackets && mc.theWorld != null) {
			mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(0, spamX, spamY, spamZ, 1));
			
			List<Entity> entityList = mc.theWorld.loadedEntityList;
			for(Entity entity : entityList) {
				try {
		            if (entity instanceof EntityPlayer && !entity.getCommandSenderName().equals(mc.thePlayer.getCommandSenderName())) {
		                Log.info("Find: " + entity.getCommandSenderName());
		            }
				} catch(Exception e) {}
	        }
		}
	}
	
	private int screenState = 0;
	private boolean featherClicked = false;
	private boolean invseeClicked = false;
	private int connectingTimer = 600;

	@Override
	public void onTick(Minecraft mc, float arg1, boolean arg2, boolean arg3) {
		EntityClientPlayerMP thePlayer = mc.thePlayer;
		if(Keyboard.isKeyDown(ModuleInfo.openKey) && mc.currentScreen == null && ModuleInfo.needUpdate != 2) {//Up
			mc.displayGuiScreen(new GuiScreenModerMenu(mc));
        }
		if(Keyboard.isKeyDown(ModuleInfo.notesKey) && mc.currentScreen == null && ModuleInfo.needUpdate != 2) {//Up
			mc.displayGuiScreen(new GuiScreenNotes(mc));
        }
		if(openMenu) {
			openMenu = false;
			mc.displayGuiScreen(new GuiScreenModerMenu(mc));
		}
		
		if(!(mc.currentScreen instanceof GuiChat) && screenState == 1) {
			mc.displayGuiScreen(new GuiChat());
		}
		
		
		if(iridescentColorState == 0) {
			iridescentColor.colorG = Utils.moveTo(iridescentColor.colorG, 1.0F, 0.01F);
			if(iridescentColor.colorG >= 1.0F) iridescentColorState = 1;
		}
		else if(iridescentColorState == 1) {
			iridescentColor.colorR = Utils.moveTo(iridescentColor.colorR, 0.0F, 0.01F);
			if(iridescentColor.colorR <= 0.0F) iridescentColorState = 2;
		}
		else if(iridescentColorState == 2) {
			iridescentColor.colorB = Utils.moveTo(iridescentColor.colorB, 1.0F, 0.01F);
			if(iridescentColor.colorB >= 1.0F) iridescentColorState = 3;
		}
		else if(iridescentColorState == 3) {
			iridescentColor.colorG = Utils.moveTo(iridescentColor.colorG, 0.0F, 0.01F);
			if(iridescentColor.colorG <= 0.0F) iridescentColorState = 4;
		}
		else if(iridescentColorState == 4) {
			iridescentColor.colorR = Utils.moveTo(iridescentColor.colorR, 1.0F, 0.01F);
			if(iridescentColor.colorR >= 1.0F) iridescentColorState = 5;
		}
		else if(iridescentColorState == 5) {
			iridescentColor.colorB = Utils.moveTo(iridescentColor.colorB, 0.0F, 0.01F);
			if(iridescentColor.colorB <= 0.0F) iridescentColorState = 0;
		}
		
		
		if (mc.currentScreen instanceof GuiChat && screenState == 2) {
            try {
				Framebuffer frameBuffer = Reflection.getPrivateValue(Minecraft.class, mc, "field_147124_at");
				Utils.SendLog(ScreenShotHelper.saveScreenshot(mc.mcDataDir, mc.displayWidth, mc.displayHeight, frameBuffer), 0);
				screenState = 0;
				if(SettingsModule.getBool("autoScreenCloseChat")) mc.displayGuiScreen(null);
			} catch (NoSuchFieldException | IllegalArgumentException | SecurityException e) {
				e.printStackTrace();
			}
        }
		
		if(Mouse.isButtonDown((int)1) && SettingsModule.getBool("feather") && !featherClicked && ModuleInfo.permission >= 2 && mc.currentScreen == null) {
			if(thePlayer != null) {
				ItemStack item = thePlayer.inventory.getCurrentItem();
				if(item != null && item.getItem().getUnlocalizedName().equals("item.feather")) {
					featherClicked = true;
					Utils.SendMessage("/rg i");
					ChatFilterModule.instance.featherInfo = true;
					ChatFilterModule.instance.featherUsersCount = 0;
				}
			}
		}
		else if(!Mouse.isButtonDown((int)1) && featherClicked) featherClicked = false;
		
		if(Mouse.isButtonDown((int)1) && SettingsModule.getBool("fastInvsee") && !invseeClicked && ModuleInfo.permission >= 1 && mc.currentScreen == null) {
			if(thePlayer != null) {
				ItemStack item = thePlayer.inventory.getCurrentItem();
				if(item != null && Item.getIdFromItem(item.getItem()) == 351 && item.getItemDamage() == 8) {
					invseeClicked = true;
					
					Entity lookingEntity = WorldRenderModule.getMouseOver(arg1);
					if(lookingEntity != null && lookingEntity instanceof EntityPlayer) {
						Utils.SendMessage("/invsee " + lookingEntity.getCommandSenderName());
					}
				}
			}
		}
		else if(!Mouse.isButtonDown((int)1) && invseeClicked) invseeClicked = false;
		
		try {
			if(mc.theWorld != null) {
				List<Entity> e = mc.theWorld.loadedEntityList;
				nearPlayers = "";
				for(Entity entity : e) {
					if(entity instanceof EntityPlayer && !entity.isDead && entity != mc.thePlayer && (Utils.vipPlayers.indexOf(entity.getCommandSenderName()) == -1 || ModuleInfo.permission >= 3)) {
						nearPlayers += entity.getCommandSenderName()+" ";
					}
				}
			}
		} catch(Exception e) {}
		
		PostRenderGui.instance.draw();
		
		if(SettingsModule.getBool("reconnect")) {
			if(mc.currentScreen instanceof GuiDisconnected) {
				String reason = "Disconnected";
				IChatComponent message = (IChatComponent) new ChatComponentText("...");
				try {
					reason = Reflection.getPrivateValue(GuiDisconnected.class, mc.currentScreen, "field_146306_a");
					message = Reflection.getPrivateValue(GuiDisconnected.class, mc.currentScreen, "field_146304_f");
				} catch (Exception e) {
					e.printStackTrace();
				}
				mc.displayGuiScreen(new GuiReconnecting(new GuiMultiplayer(new GuiMainMenu()), reason, message));
			}
			else if(mc.currentScreen instanceof GuiConnecting) {
				if(connectingTimer > 0) connectingTimer--;
				else {
					connectingTimer = 600;
					mc.displayGuiScreen((GuiScreen)new GuiConnecting(new GuiMultiplayer(new GuiMainMenu()), mc, GuiReconnecting.serverData));
				}
			}
			else connectingTimer = 600;
		}
		if(mc.currentScreen instanceof GuiConnecting) {
			if(Minecraft.getMinecraft().func_147104_D() != null) GuiReconnecting.serverData = Minecraft.getMinecraft().func_147104_D();
		}
		
		if(thePlayer != null) {
			
		}
	}
	
	public static void WriteGuardian() {
		try {
			File file = new File(MacroModCore.getMacrosDirectory()+"/guardian/" + guardianPlayer + ".txt");
			if(!file.exists()) {
				new File(MacroModCore.getMacrosDirectory()+"/guardian/").mkdir();
				file.createNewFile();
			}
		    
		    FileWriter fw2 = new FileWriter(file);
            final BufferedWriter bw2 = new BufferedWriter(fw2);
            String text = "";
            for(String line : ChatFilterModule.instance.guardianList) text += line + "\n";
			bw2.write(text);
			bw2.close();
			JsonMessageBuilder builder = new JsonMessageBuilder()
		    		.newPart()
		    		.setText("§6Действия об игроке записаны в файл:\n§f" + file.getAbsolutePath())
		    		.setClick(ClickEvent.Type.OPEN_FILE, file.getAbsolutePath())
		    		.end();
					
			Utils.SendLog(builder, 0);
		}
		catch(Exception e)
		{
			
		}	
	}
	
	double dPosX = 0, dPosY = 0, dPosZ = 0;
	double dLastX = 0, dLastY = 0, dLastZ = 0;
	float partialTicks = 0;
	
	@SubscribeEvent 
	public void worldRender(RenderWorldLastEvent event) {
		if(ModuleInfo.vip) {
			Minecraft mc = Minecraft.getMinecraft();
			EntityClientPlayerMP player = mc.thePlayer;
			RenderManager renderManager = RenderManager.instance;
			try {
				if(mc.theWorld != null) {
					List<Entity> e = mc.theWorld.loadedEntityList;
					for(Entity entity : e) {
						if(entity.getClass() == Class.forName("com.brandon3055.draconicevolution.common.entity.EntityChaosGuardian")) {
							dPosX = entity.posX;
							dPosY = entity.posY;
							dPosZ = entity.posZ;
							dLastX = entity.lastTickPosX;
							dLastY = entity.lastTickPosY;
							dLastZ = entity.lastTickPosZ;
							partialTicks = event.partialTicks;
							break;
						}
					}
				}
			} catch(Exception e) {}
			if(dPosX != 0 && dPosY != 0 && dPosZ != 0) {
				double renderPosX = renderManager.viewerPosX;
		    	double renderPosY = renderManager.viewerPosY;
		    	double renderPosZ = renderManager.viewerPosZ;
		    	double posX = (dLastX + (dPosX - dLastX) * partialTicks) - renderPosX;
		    	double posY = (dLastY + (dPosY - dLastY) * partialTicks) - renderPosY;
		    	double posZ = (dLastZ + (dPosZ - dLastZ) * partialTicks) - renderPosZ;
		    	
				float distance = (float)player.getDistance(dPosX, dPosY, dPosZ);//Count.distance((float)player.posX, (float)player.posY, (float)player.posZ, (float)dPosX, (float)dPosY, (float)dPosZ);
				if(distance < 3000) {
					WorldRenderModule.renderText("§6Дракон Хаоса", (float)posX, (float)posY+6F, (float)posZ, distance);
				}
			}
		}
		
		Minecraft mc = Minecraft.getMinecraft();
		try {
			if(mc.theWorld != null) {
				List<Entity> e = mc.theWorld.loadedEntityList;
				for(Entity entity : e) {
					if(entity instanceof EntityPlayer && entity != mc.thePlayer && Utils.vipPlayers.indexOf(entity.getCommandSenderName()) != -1) {
						if(ModuleInfo.developers.indexOf(entity.getCommandSenderName()) != -1) {
							//if(!ModuleSettings.getBool("disableOutline")) WorldRender.instance.renderGlow((EntityPlayer) entity, event.partialTicks, GuiTheme.dev);
							WorldRenderModule.renderPlayerText((EntityLivingBase) entity, event.partialTicks, "§8[§3DEV§8]");
						}
						else {
							//if(!ModuleSettings.getBool("disableOutline")) WorldRender.instance.renderGlow((EntityPlayer) entity, event.partialTicks, iridescentColor);
							WorldRenderModule.renderPlayerText((EntityLivingBase) entity, event.partialTicks, "§8[§6VIP§8]");
						}
					}
				}
			}
		} catch(Exception e) {}
    }
}