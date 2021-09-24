package net.smb.sutils.modules;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.smb.sutils.ModuleInfo;

public class SettingsModule {
	public static Map<String, String> settings = new HashMap<String, String>();
	public static File configDir = new File(Minecraft.getMinecraft().mcDataDir, "../SUtilsConfigs");
	public static File config;
	public static Map<String, String> radios = new HashMap<String, String>();
	
	public static void SetCurrentConfig(String name) {
		try {
			File mainConfig = new File(configDir+"/main.config");
			if(!mainConfig.exists()) {
				createConfig("main");
			}
	    	Properties prop = new Properties();
	    	prop.load(new FileInputStream(mainConfig));
	    	prop.setProperty("config", name);
			prop.store(new FileOutputStream(mainConfig),null);
			
			loadSettings(name);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getCurrentConfig() {
		try {
			File mainConfig = new File(configDir+"/main.config");
			if(!mainConfig.exists()) {
				createConfig("main");
				return "main";
			}
			else {
		    	Properties prop = new Properties();
		    	prop.load(new FileInputStream(mainConfig));
	
		    	String name =  prop.getProperty("config");
		    	if(name.equals("")) return "main";
		    	
		    	return name;
			}
		} catch (Exception e) {
		}
		return "main";
	}
	
	public static int getControlKey(String key) {
		try {
			File mainConfig = new File(configDir+"/main.config");
			if(!mainConfig.exists()) {
				createConfig("main");
				if(key.equals("openKey")) return Keyboard.KEY_INSERT;
				else if(key.equals("notesKey")) return Keyboard.KEY_HOME;
				else return 0;
			}
			else {
		    	Properties prop = new Properties();
		    	prop.load(new FileInputStream(mainConfig));
	
		    	return Integer.parseInt(prop.getProperty(key));
			}
		} catch (Exception e) {
		}
		if(key.equals("openKey")) return Keyboard.KEY_INSERT;
		else if(key.equals("notesKey")) return Keyboard.KEY_HOME;
		else return 0;
	}
	
	public static void setControlKey(String key, int value) {
		try {
			File mainConfig = new File(configDir+"/main.config");
			if(!mainConfig.exists()) {
				createConfig("main");
			}
	    	Properties prop = new Properties();
	    	prop.load(new FileInputStream(mainConfig));
	    	prop.setProperty(key, String.valueOf(value));
			prop.store(new FileOutputStream(mainConfig),null);
			if(key.equals("openKey")) ModuleInfo.openKey = value;
			else if (key.equals("notesKey")) ModuleInfo.notesKey = value;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void saveParam(String key) {
		try {
			if(!config.exists()) {
				createConfig(config.getName());
			}
	    	Properties prop = new Properties();
	    	prop.load(new FileInputStream(config));
	    	prop.setProperty(key, settings.get(key));
			prop.store(new FileOutputStream(config),null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String loadParam(String key) {
		try {
			if(!config.exists()) {
				createConfig(config.getName());
			}
	    	Properties prop = new Properties();
	    	prop.load(new FileInputStream(config));

	    	return prop.getProperty(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static void loadSettings(String name) {
		try {
			config = new File(configDir+"/" + name + ".config");
			if(!config.exists()) {
				createConfig(name);
			}
			else {
		    	Properties prop = new Properties();
		    	prop.load(new FileInputStream(config));
		    	settings.clear();
		    	
		    	for(Entry<Object, Object> param : prop.entrySet()) {
		    		settings.put(String.valueOf(param.getKey()), String.valueOf(param.getValue()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void loadOldConfigs() {
		configDir.mkdir();
		File oldConfigDir = new File(Minecraft.getMinecraft().mcDataDir, "liteconfig/common/macros/configs");
		if(oldConfigDir.exists()) {
			for(File file : oldConfigDir.listFiles()) {
				if(file.getName().endsWith(".config")) {
					file.renameTo(new File(configDir, "/"+file.getName()));
				}
				else if(file.getName().equals("radio.txt")) {
					file.renameTo(new File(configDir, "/radio.txt"));
				}
			}
		}
	}
	
	public static void saveConfig() {
		try {
			if(!config.exists()) {
				createConfig(config.getName());
			}
			else {
		    	Properties prop = new Properties();
		    	prop.load(new FileInputStream(config));
				for(Map.Entry<String, String> param : settings.entrySet()) {
					prop.setProperty(param.getKey(), param.getValue());
				}
				prop.store(new FileOutputStream(config),null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String createConfig(String name) {
		try {
			configDir.mkdirs();
			File newConfig = new File(configDir.getPath()+"/" + name + ".config");
			int i = 1;
			while(newConfig.exists()) {
				newConfig = new File(configDir.getPath()+"/" + name + "_" + i + ".config");
				i++;
			}
			newConfig.createNewFile();
			return newConfig.getName();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "main.config";
	}
	
	public static boolean deleteConfig(String name) {
		try {
			File newConfig = new File(configDir.getPath()+"/" + name + ".config");
			if(newConfig.exists()) {
				if(newConfig.equals(config)) {
					SetCurrentConfig("main");
					new Thread(() -> {
						try {
							Thread.sleep((long) (3000));
							newConfig.delete();
						} catch(Exception e) {}
					}).start();
					return true;
				}
				else {
					new Thread(() -> {
						try {
							Thread.sleep((long) (3000));
							newConfig.delete();
						} catch(Exception e) {}
					}).start();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static String getString(String key) {
		String value = settings.get(key);
		if(value == null) return "";
		else return value;
	}
	
	public static int getInt(String key) {
		String value = settings.get(key);
		if(value == null) return 0;
		else { 
			try {
				return Integer.parseInt(value);
			} catch(Exception e) {
				return 0;
			}
		}
	}
	
	public static float getFloat(String key) {
		String value = settings.get(key);
		if(value == null) return 0;
		else { 
			try {
				return Float.parseFloat(value);
			} catch(Exception e) {
				return 0;
			}
		}
	}
	
	public static boolean getBool(String key) {
		String value = settings.get(key);
		if(value == null) return false;
		else { 
			try {
				return Boolean.parseBoolean(value);
			} catch(Exception e) {
				return false;
			}
		}
	}
	
	public static void setParam(String key, Object value) {
		settings.put(key, String.valueOf(value));
		saveParam(key);
	}
	
	public static List<String> GetAllConfigs() {
		List<String> configs = new ArrayList<String>();
		if(configDir.exists()) {
			for(File file : configDir.listFiles()) {
				if(file.getName().endsWith(".config")) configs.add(file.getName());
			}
		}
		return configs;
	}
	
	public static void loadRaidos() {
		try {
			radios.clear();
			File file = new File(configDir, "/radio.txt");
			if(!file.exists()) {
				configDir.mkdir();
				file.createNewFile();
				
				radios.put("Radio Record", "http://radio-srv1.11one.ru/record192k.mp3");
				radios.put("Европа +", "http://ep128.hostingradio.ru:8030/ep128"); 
				radios.put("Супердискотека 90-х", "https://radiorecord.hostingradio.ru/sd9096.aacp");
				radios.put("Trancemission", "https://radiorecord.hostingradio.ru/tm96.aacp");
				radios.put("Russian Mix", "https://radiorecord.hostingradio.ru/rus96.aacp");
				radios.put("Russian Hits", "https://radiorecord.hostingradio.ru/russianhits96.aacp");
				radios.put("Медляк FM", "https://radiorecord.hostingradio.ru/mdl96.aacp");
				radios.put("Гоп FM", "https://radiorecord.hostingradio.ru/gop96.aacp");
				radios.put("VIP House", "https://radiorecord.hostingradio.ru/vip96.aacp");
				radios.put("Український рок", "https://online.radioroks.ua/RadioROKS_Ukr");
				radios.put("Black Rap", "https://radiorecord.hostingradio.ru/yo96.aacp");
				radios.put("Old School", "https://radiorecord.hostingradio.ru/pump96.aacp");
				radios.put("Hardstyle", "https://radiorecord.hostingradio.ru/teo96.aacp");
				radios.put("Chill-Out", "https://radiorecord.hostingradio.ru/chil96.aacp");
				radios.put("EDM", "https://radiorecord.hostingradio.ru/club96.aacp");
				radios.put("Deep", "https://radiorecord.hostingradio.ru/deep96.aacp");
				radios.put("Breaks", "https://radiorecord.hostingradio.ru/brks96.aacp");
				radios.put("Dancecore", "https://radiorecord.hostingradio.ru/dc96.aacp");
				radios.put("Dubstep", "https://radiorecord.hostingradio.ru/dub96.aacp");
				radios.put("Trap", "https://radiorecord.hostingradio.ru/trap96.aacp");
				radios.put("Techno", "https://radiorecord.hostingradio.ru/techno96.aacp");
				radios.put("Technopop", "https://radiorecord.hostingradio.ru/technopop96.aacp");
				radios.put("Future House", "https://radiorecord.hostingradio.ru/fut96.aacp");
				radios.put("Rock", "https://radiorecord.hostingradio.ru/rock96.aacp");
				saveRadios();
				radios.put("SUtils Radio", "http://92.53.65.14:8000/live");
				
				return;
			}
			  
			BufferedReader br = new BufferedReader(new FileReader(file));
		  
		    String st;
		    while ((st = br.readLine()) != null) {
		    	try {
			    	String[] r = st.split("=", 2);
			    	radios.put(r[0], r[1]);
		    	} catch(Exception e) {}
		    }
		    radios.put("SUtils Radio", "http://92.53.65.14:8000/live");
		} catch(Exception e) {e.printStackTrace();}
	}
	
	public static void saveRadios() {
		try {
			File file = new File(configDir, "/radio.txt");
			if(!file.exists()) {
				configDir.mkdir();
				file.createNewFile();
			}
			String radiosAll = "";
			
			for(Map.Entry<String, String> param : radios.entrySet()) {
				if(!param.getValue().equals("http://92.53.65.14:8000/live"))
					radiosAll += param.getKey()+"="+param.getValue()+"\n";
			}
			
        	final FileWriter fw2 = new FileWriter(file);
            final BufferedWriter bw2 = new BufferedWriter(fw2);
			bw2.write(radiosAll);
			bw2.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
