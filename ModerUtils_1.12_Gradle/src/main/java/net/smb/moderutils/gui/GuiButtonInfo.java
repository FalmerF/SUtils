package net.smb.moderutils.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import net.smb.moderutils.GuiTheme;
import net.smb.moderutils.ModuleInfo;

public class GuiButtonInfo extends GuiElement{
    public float scale;
    public String description;
    public String example;

    public GuiButtonInfo(int id, float scale, String description, String example)
    {
    	super(id, 18, 18, 8, 8);
        this.scale = scale;
        this.description = description;
        this.example = example;
    }
    
    @SuppressWarnings("static-access")
	public void draw(Minecraft mc, int positionX, int positionY)
    {
        if (this.visible)
        {
            this.hovered = positionX >= this.posX && positionY >= this.posY && positionX < this.posX + this.width*this.scale && positionY < this.posY + this.height*this.scale;
            
            if(hovered) {
            	GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.9F);
            }
            else {
            	GL11.glColor4f(0.9F, 0.9F, 0.9F, 0.5F);
            }
            
            // Draw Button
            mc.getTextureManager().bindTexture(GuiTheme.infoIcon);
            this.drawScaledCustomSizeModalRect(this.posX, this.posY, 0, 0, 1, 1, (int)(this.width*this.scale), (int)(this.height*this.scale), 1, 1);
            
            // Draw Tooltip
            if(hovered)
            	ModuleInfo.desc.setParams(this.description, this.example);
        }
    }
}
