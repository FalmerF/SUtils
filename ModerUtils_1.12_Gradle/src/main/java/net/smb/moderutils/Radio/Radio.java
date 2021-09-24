package net.smb.moderutils.Radio;

import java.net.URL;
import java.util.Map;

import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackListener;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.smb.moderutils.CFilter;
import net.smb.moderutils.ModuleInfo;
import net.smb.moderutils.ModuleSettings;
import net.smb.moderutils.ModuleUtils;
import net.smb.moderutils.gui.GuiButtonImage;
import net.smb.moderutils.gui.GuiScreenModerMenu;

public class Radio extends PlaybackListener implements Runnable {
	public static Radio radio;
	public static boolean canPlay = true;
	
	private Thread pThread;
    boolean ThreadStarted;
    AdvancedPlayer player;
    String link = "";
    
    public Radio(String link) {
        this.ThreadStarted = false;
        this.link = link;
        (this.pThread = new Thread(this)).start();
        canPlay = false;
        
        new Thread(() -> {
			try {
				Thread.sleep((long) (2000));
				canPlay = true;
			} catch(Exception e) {}
		}).start();
    }
    
    @Override
    public void run() {
        try {
            (this.player = new AdvancedPlayer(new URL(link).openStream())).setPlayBackListener(this);
            if(ModuleSettings.settings.containsKey("radioVolume"))
            	setVolume(ModuleSettings.getFloat("radioVolume")/100);
            else {
            	setVolume(0.1F);
            	ModuleSettings.setParam("radioVolume", "10");
            }
            this.player.play();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void stop() {
        if (this.player != null && this.isPlaying()) {
            this.player.stop();
            if(Minecraft.getMinecraft().currentScreen != null && Minecraft.getMinecraft().currentScreen instanceof GuiScreenModerMenu) {
    			((GuiButtonImage)((GuiScreenModerMenu)Minecraft.getMinecraft().currentScreen).getById(41)).buttonTexture = new ResourceLocation("moderutils", "textures/play.png");;
    		}
        }
    }
    
    public boolean isPlaying() {
        return this.pThread.isAlive();
    }
    
    public void setVolume(final float f) {
        if (this.player != null) {
            this.player.setVolume(f/2);
        }
    }
    
    public static void playRadio(String name) {
    	if(canPlay) {
	    	if (Radio.radio != null) Radio.radio.stop();
	    	if(ModuleSettings.radios.get(name) != null) {
	    		ModuleSettings.setParam("currentRadio", getRadioByName(name));
	    		if(Minecraft.getMinecraft().currentScreen != null && Minecraft.getMinecraft().currentScreen instanceof GuiScreenModerMenu) {
	    			((GuiScreenModerMenu)Minecraft.getMinecraft().currentScreen).updateRadioText();
	    			((GuiButtonImage)((GuiScreenModerMenu)Minecraft.getMinecraft().currentScreen).getById(41)).buttonTexture = new ResourceLocation("moderutils", "textures/pause.png");
	    		}
	    		Radio.radio = new Radio(ModuleSettings.radios.get(name));
	    	}
    	}
    }
    
    public static int getRadioByName(String name) {
    	int i = 0;
    	for(Map.Entry<String, String> param : ModuleSettings.radios.entrySet()) {
			if(param.getKey() == name) return i;
				i++;
		}
    	return 0;
    }
}
