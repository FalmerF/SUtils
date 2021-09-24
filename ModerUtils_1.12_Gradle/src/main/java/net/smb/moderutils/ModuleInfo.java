package net.smb.moderutils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.input.Keyboard;

import net.eq2online.console.Log;
import net.smb.moderutils.gui.GuiDescription;
import net.smb.moderutils.gui.PostRenderGui;

/**
 * Shared information for this module
 */
public abstract class ModuleInfo {
    
    /**
     * Target API version
     */
    public static final int API_VERSION = 26;
    public static int sX, sY, sW, sH;
    public static int needUpdate = 0;
    public static int permission = 0;
    public static int openKey = Keyboard.KEY_INSERT;
    public static int notesKey = Keyboard.KEY_HOME;
    public static int adsCount = 0;
    
    public static String version = "v0.5.4", lastVersion = "";
    public static String playerName = "", prefix = "/";
    public static String playerBalance = "0.00", playerOS = "§c0m", serverTPS = "§a20.00", onlineFriends = "", uptime = "0h 0m 0s";
    public static String vipDaysLeft = "0";
    public static List<String> developers =new ArrayList<String>(Arrays.asList("Falmer", "igorek181", "XRayFun_GF"));
    
    public static boolean inBlackList;
    public static boolean scissorEnable;
    public static boolean vip;


    /**
     * Private ctor, no instances 
     */
    public static GuiDescription desc = new GuiDescription();
    
    public ModuleInfo() {}
    
    public static void updateModuleInfo() {
		String lastVersion = ModuleUtils.getDataString("Version");
		needUpdate = 0;
		if(!lastVersion.equals("")) {
			try {
				String[] lastVersionParts = lastVersion.split("\\.");
				String[] versionParts = version.split("\\.");
				if(!lastVersionParts[0].equals(versionParts[0]) || !lastVersionParts[1].equals(versionParts[1])) needUpdate = 2;
				else {
					String lastVersionEndPart = lastVersion.substring(lastVersionParts[0].length() + lastVersionParts[1].length()+2, lastVersion.length());
					String versionEndPart = version.substring(versionParts[0].length() + versionParts[1].length()+2, version.length());
					if(!lastVersionEndPart.equals(versionEndPart)) needUpdate = 1;
				}
			} catch(Exception e) {
				needUpdate = 2;
				e.printStackTrace();
			}
		}
		else needUpdate = 2;
	}
    
    public static void setVip(boolean vip) {
		ModuleInfo.vip = vip;
		
		if(vip && !ModuleSettings.getBool("vip")) {
			PostRenderGui.instance.addTitle("§aПоздравляем, теперь вы §6VIP§a!", "§7Ознакомиться со списком возможностей можно в меню");
		}
		else if(!vip && ModuleSettings.getBool("vip")) {
			PostRenderGui.instance.addTitle("§cВы больше не §6VIP§c!", "§7Приобрести §6VIP§7 можно на нашем discord сервере");
		}
		ModuleSettings.setParam("vip", vip);
	}
}
