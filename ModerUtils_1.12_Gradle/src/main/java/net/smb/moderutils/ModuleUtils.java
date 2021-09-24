package net.smb.moderutils;

import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.eq2online.console.Log;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.smb.moderutils.http.EnumHttpMethod;
import net.smb.moderutils.http.HttpStateData;
import net.smb.moderutils.http.ReturnValueArrayImplodeScalar;
import net.smb.moderutils.jchat.JsonMessageBuilder;

public class ModuleUtils {
	public static String[] soundName = {"Без звука","Опыт", "Наковальня", "Стрела", "Сломал", "Клик", "Стекло",
			"Басс", "Арфа", "Нота", "Кот", "Взрыв", "Всплеск", "Спрут", "Летучая мышь", "Ифрит",
			"Курица", "Корова", "Дракон", "Эндерман", "Гаст", "Свинья", "Волк"};
	public static String[] soundDir = {"", "minecraft:entity.experience_orb.pickup", "minecraft:block.anvil.land", "minecraft:entity.arrow.hit", "minecraft:block.stone.break", "minecraft:ui.button.click", 
			"minecraft:block.glass.break", "minecraft:block.note.bass", "minecraft:block.note.harp", "minecraft:block.note.pling", "minecraft:entity.cat.purreow", "minecraft:entity.firework.blast", "minecraft:entity.player.swim",
			"entity.squid.hurt", "minecraft:entity.bat.hurt", "minecraft:entity.blaze.hurt", "minecraft:entity.chicken.hurt", "minecraft:entity.cow.hurt", "minecraft:entity.enderdragon.hurt",
			"minecraft:entity.endermen.hurt", "minecraft:entity.ghast.scream", "minecraft:entity.pig.hurt", "minecraft:entity.wolf.hurt"};
	
	private static TrayIcon trayIcon;
	protected static JsonParser parser = new JsonParser();
	
	public static Map<String, String> data = new HashMap<String, String>();
	public static List<String> vipPlayers = new ArrayList<String>();
	
	public static void SendMessage(String message) {
		Minecraft.getMinecraft().player.sendChatMessage(message);
	}
	
	public static void SendLog(String message, int id) {
		SendLog((ITextComponent) new TextComponentString(message), id);
	}
	
	public static void SendLog(ITextComponent message, int id) {
		final GuiNewChat chatGUI = Minecraft.getMinecraft().ingameGUI.getChatGUI();
        chatGUI.printChatMessageWithOptionalDeletion(message, id);
	}
	
	public static void SendLog(JsonMessageBuilder builder, int id) {
		SendLog(builder.build().serialize(), id);
	}
	
	public void JsonAdd(JsonMessageBuilder builder, String text) {
		builder
		.newPart()
		.setText(text)
		.end();
	}
	
	public static String convertAmpCodes(final String text) {
        return text.replaceAll("(?<!&)&([0-9a-fklmnor])", "§$1").replaceAll("&&", "&");
    }
	
	public static String clearAmpCodes(String text) {
        return text.replaceAll("(?<!&)§([0-9a-fklmnor])", "");
    }
	public static boolean hasAmpCodes(String text) {
		return Pattern.compile("(?<!&)§([0-57-9a-fklmno])").matcher(text).find();
	}
	
	public static String setAmpCodesToAllText(String text) {
		String newText = "";
		String[] textArray = text.split(" ");
		String lastCode = "§6";
		for(String s : textArray) {
			String code = getAllCodes(s);
			if(!code.equals("")) lastCode = code;
			newText += lastCode + s + " ";
		}
		return newText;
	}
	
	public static String getAllCodes(String text) {
		String codesInText = "";
		String[] codes = {"§0", "§1", "§2", "§3", "§4", "§5", "§6", "§7", "§8", "§9", "§a", "§b", "§c", "§d", "§e", "§f", "§l", "§m", "§n", "§o", "§k"};
		for(String code : codes) {
			if(Pattern.compile(code).matcher(text).find()) codesInText += code;
		}
		return codesInText;
	}
	
	public static String stringToChatCodes(String codes) {
		if(codes.equals("")) return "§f";
		String newText = "";
		for(char c : codes.toCharArray()) newText += "§" + c;
		return newText;
	}
	
	public static String replaceHighWords(String text, String chatColor) {
		for(int i = 0; i < 10; i++) {
			if(!ModuleSettings.getString("highlightedWords" + i).equals("")) {
				String color = ModuleSettings.getString("highlightedWordsColor" + i);
				String words = ModuleSettings.getString("highlightedWords" + i);
				if(color.equals("") || words.equals("")) return text;
				boolean register = ModuleSettings.getBool("highlightedWordsRegister" + i);
				color = convertAmpCodes(color);
				String[] wordsArray = words.split(" ");
				String[] textWords = text.split(" ");
				text = "";
				boolean wordFound = false;
				for(String wordText : textWords) {
					for(String word : wordsArray) {
						if(!register) {
							if(wordText.replaceAll("[^a-zA-Zа-яА-ЯёЁ0-9_]", "").toLowerCase().equals(word.toLowerCase())) {
								wordText = color + wordText + chatColor;
								wordFound = true;
							}
						}
						else {
							if(wordText.replaceAll("[^a-zA-Zа-яА-ЯёЁ0-9_]", "").equals(word)) {
								wordText = color + wordText + chatColor;
								wordFound = true;
							}
						}
					}
					text += wordText + " ";
				}
				int sound = Math.max(Math.min(ModuleSettings.getInt("highlightedWordsSound" + i), soundName.length-1), 0);
				if(wordFound && sound != 0)
					Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvent.REGISTRY.getObject(new ResourceLocation(soundDir[sound])), 1.0F));
			
			}
		}
		return text;
	}
	
	public static void sendNotify(String message) {
		try {
			if (trayIcon == null) {
	            SystemTray tray = SystemTray.getSystemTray();
	            
	            URL url = new URL("https://image.prntscr.com/image/uXRHCdMBQNO7u8ZeGzHq3A.png");
	            Image image = Toolkit.getDefaultToolkit().getImage(url);
	     
	            trayIcon = new TrayIcon(image);
	            trayIcon.setImageAutoSize(true);
	            trayIcon.setToolTip("Notifications");
	            tray.add(trayIcon);
	        }
	 
	        trayIcon.displayMessage("Notifications", message, TrayIcon.MessageType.INFO);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String[] getNamesFromList(Collection<NetworkPlayerInfo> playerList) {
		Object[] playerArray = playerList.toArray();
		String[] playerNames = new String[playerList.size()];
		for(int i = 0; i < playerNames.length; i++)  playerNames[i] = ((NetworkPlayerInfo)playerArray[i]).getGameProfile().getName();
		return playerNames;
	}
	
	public static BufferedReader getDataFromURL(String urlName) {
	    try {
	        URL url = new URL(urlName);
	        HttpURLConnection connection = setRequestPropertys((HttpURLConnection) url.openConnection());
	        connection.connect();
	        for(int i = 0; i < 5; i++) {
	        	if(connection.getResponseCode() == 404) connection.connect();
	        	else break;
	        }
	        
	        InputStream stream = connection.getErrorStream();
	        if (stream == null) {
	            stream = connection.getInputStream();
	            return new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8")));
	        }
        } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return null;
	}
	
	public static HttpURLConnection setRequestPropertys(HttpURLConnection connection) {
		connection.setRequestProperty("User-Agent", "SMB-Super-Secret-Agent-Esli-Ti-Eto-Nawel-Idi-Naxyu");
		connection.setRequestProperty("Cookie", "");
		return connection;
	}
	
	public static void updateDataFromServer() {
		getModuleData();
		getModuleAds();
		getVipUsers();
		EcoModule.instance.loadData();
	}
	
	public static void getModuleData() {
		String url  = "https://macros-core.com/data/info.txt";
		BufferedReader reader = getDataFromURL(url);
		String line;
		try {
	        while ((line = reader.readLine()) != null) {
	            if(!line.equals("")) {
	            	String[] lineData = line.split("=", 2);
	            	data.put(lineData[0], lineData[1]);
	            }
	        }
		} catch(Exception e) {e.printStackTrace();}
        ModuleInfo.updateModuleInfo();
	}
	
	public static void getVipUsers() {
		String url  = "https://macros-core.com/data/vipInfo.txt";
		BufferedReader reader = getDataFromURL(url);
		String line;
		try {
	        while ((line = reader.readLine()) != null) {
	            if(!line.equals("")) {
	            	String[] lineData = line.split("=", 2);
	            	vipPlayers.add(lineData[0]);
	            	if(lineData[0].equals(ModuleInfo.playerName))
	            		ModuleInfo.vipDaysLeft = lineData[1].split("\\|")[2];
	            }
	        }
		} catch(Exception e) {e.printStackTrace();}
		if(vipPlayers.indexOf(ModuleInfo.playerName) != -1) ModuleInfo.setVip(true);
        else ModuleInfo.setVip(false);
	}
	
	public static void getModuleAds() {
		String url  = "https://macros-core.com/data/adsData.txt";
		BufferedReader reader = getDataFromURL(url);
		String line;
		try {
	        while ((line = reader.readLine()) != null) {
	            if(!line.equals("")) {
	            	String[] lineData = line.split("=", 2);
	            	if(lineData[0].equals(ModuleInfo.playerName))
	            		ModuleInfo.adsCount = Integer.parseInt(lineData[1]);
	            }
	        }
		} catch(Exception e) {e.printStackTrace();}
	}
	
	public static String jsonGet(final String[] params) {
        String valueString = "";
        if (params.length == 2 || params.length == 3) {
            final String json = params[0];
            final String key = params[1];
            final JsonElement input = parser.parse(json);
            try {
                final JsonObject inputObject = input.getAsJsonObject();
                final JsonElement value = inputObject.get(key);
                valueString = value.toString();
            }
            catch (IllegalStateException e) {}
            catch (ClassCastException ex) {}
        }
        return valueString;
    }
    
    public static int jsonArraySize(final String param) {
    	int size = 0;
        final String json = param;
        final JsonElement input = parser.parse(json);
        try {
            final JsonArray inputArray = input.getAsJsonArray();
            size = inputArray.size();
        }
        catch (IllegalStateException e) {}
        catch (ClassCastException ex) {}
        return size;
    }
    
    public static List<String> getJsonAsArray(final String param) {
        final List<String> elements = new LinkedList<String>();
        final String json = param;
        final JsonElement input = parser.parse(json);
        try {
            final JsonArray inputArray = input.getAsJsonArray();
            final Iterator<JsonElement> inputIter = (Iterator<JsonElement>)inputArray.iterator();
            while (inputIter.hasNext()) {
                elements.add(inputIter.next().toString());
            }
        }
        catch (IllegalStateException e) {}
        catch (ClassCastException ex) {}
        return elements;
    }
    
	public static String getDataString(String key) {
		String value = data.get(key);
		if(value == null) return "";
		else return value;
	}
	
	public static float moveTo(float value, float to, float step) {
    	if(value > to) {
    		step *= -1;
    		return Math.max(value+step, to);
    	}
    	else return Math.min(value+step, to);
    }
}
