package net.smb.sutils.gui;

import java.awt.Desktop;
import java.net.URI;
import java.util.Iterator;
import java.util.List;

import net.eq2online.console.Log;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.IChatComponent;
import net.smb.sutils.ModuleInfo;
import net.smb.sutils.gui.elements.GuiSliderReconnect;
import net.smb.sutils.modules.SettingsModule;

public class GuiReconnecting extends GuiScreen {

	private String reason;
    private IChatComponent message;
    private List multilineMessage;
    private final GuiScreen parentScreen;
    public static boolean reconnect = false;
    public static int timer = 0;
    public static ServerData serverData;
	
    public GuiReconnecting(GuiScreen parent, String reason, IChatComponent message)
    {
        this.parentScreen = parent;
        this.reason = reason;
        this.message = message;
    }

	@SuppressWarnings("unchecked")
	public void initGui()
    {
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 150 + 12, I18n.format("gui.toMenu", new Object[0])));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 12, "Reconnect"));
        
        if(ModuleInfo.needUpdate == 2) {
	        GuiButton discordButton = new GuiButton(2, this.width / 2 - 100, this.height / 4 + 180 + 12, "Discord SMB");
	        this.buttonList.add(discordButton);
        }
        else {
	        GuiSliderReconnect timerSlider = new GuiSliderReconnect(3, this.width / 2 - 100, this.height / 4 + 180 + 12);
	        this.buttonList.add(timerSlider);
        }
        this.multilineMessage = this.fontRendererObj.listFormattedStringToWidth(this.message.getFormattedText(), this.width - 50);
        timer = SettingsModule.getInt("reconnectTime");
        if(timer == 0) {
        	timer = 1200;
        	SettingsModule.setParam("reconnectTime", timer);
        }
        reconnect = SettingsModule.getBool("reconnect2");
    }
	
	public void updateScreen() {
		if(ModuleInfo.needUpdate == 2) {
			((GuiButton) this.buttonList.get(1)).displayString = "§cТребуется обновление";
		}
		else if(reconnect) {
			((GuiButton) this.buttonList.get(1)).displayString = "Reconnect: §aON  §fВремя до подключения: §a" + (int)(Math.ceil(timer/20));
		}
		else {
			((GuiButton) this.buttonList.get(1)).displayString = "Reconnect: §cOFF";
		}
		
		if(ModuleInfo.needUpdate != 2 && reconnect) {
	        
	        if(timer > 0) {
	        	timer--;
	        }
		}
	}
	
	protected void actionPerformed(GuiButton button)
    {
        if (button.id == 0)
        {
            Minecraft.getMinecraft().displayGuiScreen(parentScreen);
        }
        else if (button.id == 1 && ModuleInfo.needUpdate != 2)
        {
        	reconnect = !reconnect;
        	SettingsModule.setParam("reconnect2", reconnect);
        }
        else if (button.id == 2)
        {
        	try {
				Desktop.getDesktop().browse(new URI("https://discord.gg/hw2M2tfNhE"));
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
    }
	
	protected void keyTyped(char p_73869_1_, int p_73869_2_) {}
	
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
		if(timer <= 0) {
        	timer = SettingsModule.getInt("reconnectTime");
        	
        	if(serverData != null)	
            mc.displayGuiScreen((GuiScreen)new GuiConnecting(new GuiMultiplayer(new GuiMainMenu()), mc, serverData));
        }
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, this.reason, this.width / 2, this.height / 2 - 50, 11184810);
        this.drawCenteredString(this.fontRendererObj, "Auto-Reconnect by SMB " + ModuleInfo.version, this.width / 2, this.height - 10, 11184810);
        int var4 = this.height / 2 - 30;

        if (this.multilineMessage != null)
        {
            for (Iterator var5 = this.multilineMessage.iterator(); var5.hasNext(); var4 += this.fontRendererObj.FONT_HEIGHT)
            {
                String var6 = (String)var5.next();
                this.drawCenteredString(this.fontRendererObj, var6, this.width / 2, var4, 16777215);
            }
        }
        super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
    }
}
