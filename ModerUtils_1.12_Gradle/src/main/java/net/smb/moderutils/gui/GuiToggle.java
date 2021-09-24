package net.smb.moderutils.gui;

import org.lwjgl.opengl.GL11;

import net.eq2online.console.Log;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.smb.moderutils.GuiTheme;
import net.smb.moderutils.ModuleSettings;

public class GuiToggle extends GuiElement{
    public boolean active;
    public float currentPosX, needPosX;
    public String option = "";

    public GuiToggle(int id, int posX, int posY, String option)
    {
    	super(id, posX, posY, 15, 5);
    	this.currentPosX = posX-2;
    	this.needPosX = posX-2;
    	this.option = option;
    	if(!this.option.equals("")) this.active = ModuleSettings.getBool(this.option);
    }
    
    @SuppressWarnings("static-access")
	public void draw(Minecraft mc, int positionX, int positionY)
    {
        if (this.visible)
        {
            this.hovered = positionX >= this.posX && positionY >= this.posY && positionX < this.posX + this.width && positionY < this.posY + this.height;
            int state = this.getHoverState(hovered);
            
            if(state == 2) {
            	GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            }
            else if(state == 1) {
            	GL11.glColor4f(0.9F, 0.9F, 0.9F, 1.0F);
            }
            else if(state == 0) {
            	GL11.glColor4f(0.5F, 0.5F, 0.5F, 1.0F);
            }
            
            // Render Main Texture
            if(this.active) {
            	mc.getTextureManager().bindTexture(GuiTheme.toggleActive);
            }
            else {
            	mc.getTextureManager().bindTexture(GuiTheme.toggleDisactive);
            }
            this.drawScaledCustomSizeModalRect(this.posX, this.posY, 0, 0, 1, 1, this.width, this.height, 1, 1);
            
            // Render Handle
            mc.getTextureManager().bindTexture(GuiTheme.toggleHandle);
            
            if(this.active) {
            	this.needPosX = (float)(this.posX+this.width-6);
            }
            else {
            	this.needPosX = this.posX-2;
            }
            
            this.currentPosX = lerp(this.currentPosX, this.needPosX, 0.3F);
            this.drawScaledCustomSizeModalRect((int)this.currentPosX, this.posY-1, 0, 0, 1, 1, 7, 7, 1, 1);
        }
    }
    
    public void SetNeedPos() {
    	if(this.active) {
        	this.currentPosX = (float)(this.posX+this.width-6);
        }
        else {
        	this.currentPosX = this.posX-2;
        }
    }
    
    public void clicked() {
    	if(this.enabled && this.visible) {
    		active = !active;
    		if(!this.option.equals("")) ModuleSettings.setParam(option, this.active);
    	}
    }
}
