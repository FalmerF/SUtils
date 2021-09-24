package net.smb.sutils.modules;

import java.net.URL;
import java.util.Map;

import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackListener;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.smb.sutils.gui.GuiScreenModerMenu;
import net.smb.sutils.gui.elements.GuiButtonImage;

public class Radio extends PlaybackListener implements Runnable {
	public static Radio radio;
	public static boolean canPlay = true;
	
	private static boolean live = false;
	
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
            if(SettingsModule.settings.containsKey("radioVolume"))
            	setVolume(SettingsModule.getFloat("radioVolume")/100);
            else {
            	setVolume(0.1F);
            	SettingsModule.setParam("radioVolume", "10");
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
	    	if(SettingsModule.radios.get(name) != null) {
	    		SettingsModule.setParam("currentRadio", getRadioByName(name));
	    		if(Minecraft.getMinecraft().currentScreen != null && Minecraft.getMinecraft().currentScreen instanceof GuiScreenModerMenu) {
	    			((GuiScreenModerMenu)Minecraft.getMinecraft().currentScreen).updateRadioText();
	    			((GuiButtonImage)((GuiScreenModerMenu)Minecraft.getMinecraft().currentScreen).getById(41)).buttonTexture = new ResourceLocation("moderutils", "textures/pause.png");
	    		}
	    		Radio.radio = new Radio(SettingsModule.radios.get(name));
	    	}
    	}
    }
    
    public static int getRadioByName(String name) {
    	int i = 0;
    	for(Map.Entry<String, String> param : SettingsModule.radios.entrySet()) {
			if(param.getKey() == name) return i;
				i++;
		}
    	return 0;
    }
}
