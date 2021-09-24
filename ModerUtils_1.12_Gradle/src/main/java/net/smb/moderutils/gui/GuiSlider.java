package net.smb.moderutils.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import net.smb.moderutils.GuiTheme;
import net.smb.moderutils.ModuleSettings;

public class GuiSlider extends GuiElement{
    protected ResourceLocation handleTexture = new ResourceLocation("moderutils", "textures/slider_handle.png");
    public float value, maxVal, minVal;
    public String option;

    public GuiSlider(int id, int posX, int posY, int width, float minVal, float maxVal, String option)
    {
    	super(id, posX, posY, width, 10);
        
        this.maxVal = maxVal;
        this.minVal = minVal;
        
        this.option = option;
        try {
        if(!this.option.equals("")) this.value = Float.parseFloat(ModuleSettings.getString(option));
        } catch(Exception e) {
        	this.value = 0.8F;
        	ModuleSettings.setParam(option, (float)(this.maxVal-this.minVal)*this.value);
        	e.printStackTrace();
        }
    	this.value = value/(maxVal-minVal);
    }
    
    @SuppressWarnings("static-access")
	public void draw(Minecraft mc, int positionX, int positionY)
    {
        if (this.visible)
        {
            this.hovered = positionX >= this.posX && positionY >= this.posY && positionX < this.posX + this.width && positionY < this.posY + this.height;
            int state = getHoverState(hovered);
            int color = GuiTheme.textColor;
            
            if(state == 2) {
            	GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            }
            else if(state == 1) {
            	GL11.glColor4f(0.9F, 0.9F, 0.9F, 1.0F);
            }
            else if(state == 0) {
            	GL11.glColor4f(0.5F, 0.5F, 0.5F, 1.0F);
            	color = GuiTheme.textDisabledColor;
            }
            
            // Render Line
            mc.getTextureManager().bindTexture(GuiTheme.sliderLine);
            this.drawScaledCustomSizeModalRect(this.posX, this.posY+4, 0, 0, 1, 1, this.width, 2, 1, 1);
            
            // Render Handle
            mc.getTextureManager().bindTexture(handleTexture);
            this.drawScaledCustomSizeModalRect((int)(this.posX+((this.width-6)*this.value)), this.posY+2, 0, 0, 1, 1, 6, 6, 1, 1);
            
            FontRenderer fontRender = mc.fontRenderer;
            
            // Render Value Field
            mc.getTextureManager().bindTexture(GuiTheme.sliderValue);
            this.drawScaledCustomSizeModalRect((int)(this.posX+this.width+4), this.posY, 0, 0, 1, 1, 20, 10, 1, 1);
            // Render Value Text
            String displayString = String.format("%.1f",(float)(this.maxVal-this.minVal)*this.value);
            fontRender.drawString(displayString, (int)(this.posX+this.width+14)-fontRender.getStringWidth(displayString)/2, this.posY + 1, color);
            
            this.dragged(mc, positionX, positionY);
        }
    }
    
    protected void dragged(Minecraft mc, int posX, int posY) {
    	if(this.enabled && this.visible && this.pressed) {
    		this.value = (float)(posX-this.posX-3)/(float)(this.width-6);
    		this.value = Math.max(Math.min(this.value, 1), 0);
    	}
    }
    
    public void released(int p_146118_1_, int p_146118_2_) {
    	super.released(p_146118_1_, p_146118_2_);
    	if(!this.option.equals("")) ModuleSettings.setParam(option, (float)(this.maxVal-this.minVal)*this.value);
    }
}
