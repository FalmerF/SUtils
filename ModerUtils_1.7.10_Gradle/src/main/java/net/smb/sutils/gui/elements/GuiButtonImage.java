package net.smb.sutils.gui.elements;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.smb.sutils.common.GuiTheme;

public class GuiButtonImage extends GuiElement{
    public ResourceLocation buttonTexture = GuiTheme.button;
    public float colorR = 1.0F, colorG = 1.0F, colorB = 1.0F;
    public float colorRStandart = 1.0F, colorGStandart = 1.0F, colorBStandart = 1.0F;
    private float currentR = 1.0F, currentG = 1.0F, currentB = 1.0F;
    
    public GuiButtonImage(int id, int posX, int posY, ResourceLocation buttonTexture)
    {
        this(id, posX, posY, 200, 20, buttonTexture);
    }

    public GuiButtonImage(int id, int posX, int posY, int width, int height, ResourceLocation buttonTexture)
    {
    	super(id, posX, posY, width, height);
        this.buttonTexture = buttonTexture;
    }
    
    public void setColor(float R, float G, float B) {
    	this.colorR = R;
    	this.colorG = G;
    	this.colorB = B;
    }
    
    public void setColorStandart(float R, float G, float B) {
    	this.colorRStandart = R;
    	this.colorGStandart = G;
    	this.colorBStandart = B;
    }
    
    @SuppressWarnings("static-access")
	public void draw(Minecraft mc, int positionX, int positionY)
    {
        if (this.visible)
        {
            this.hovered = positionX >= this.posX && positionY >= this.posY && positionX < this.posX + this.width && positionY < this.posY + this.height;
            
            if(hovered) {
            	currentR = this.lerp(currentR, colorR, 0.2F);
            	currentG = this.lerp(currentG, colorG, 0.2F);
            	currentB = this.lerp(currentB, colorB, 0.2F);
            }
            else {
            	currentR = this.lerp(currentR, colorRStandart, 0.2F);
            	currentG = this.lerp(currentG, colorGStandart, 0.2F);
            	currentB = this.lerp(currentB, colorBStandart, 0.2F);
            }
            
            GL11.glColor4f(currentR, currentG, currentB, 1.0F);
            
            mc.getTextureManager().bindTexture(this.buttonTexture);
            this.func_152125_a(this.posX, this.posY, 0, 0, 1, 1, this.width, this.height, 1, 1);
        }
    }
}
