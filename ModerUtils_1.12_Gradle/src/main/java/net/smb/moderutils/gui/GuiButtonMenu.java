package net.smb.moderutils.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import net.smb.moderutils.GuiTheme;

public class GuiButtonMenu extends GuiElement{
    protected ResourceLocation icon = null;

    public String displayString;
    public boolean selected;
    
    public float imgWidthScale = 1.0F;
    public float imgHeightScale = 1.0F;
    
    private float textureAlpha = 0.0F, textureHoverAlpha = 0.0F, textureSelectedAlpha = 0.0F;
    
    public GuiButtonMenu(int id, int posX, int posY, String displayString)
    {
        this(id, posX, posY, 200, 20, displayString, "textures/player_icon.png");
    }

    public GuiButtonMenu(int id, int posX, int posY, int width, int height, String displayString, String iconDir)
    {
    	super(id, posX, posY, width, height);
        this.displayString = displayString;
        this.icon = new ResourceLocation("moderutils", iconDir);
    }
    
    @SuppressWarnings("static-access")
	public void draw(Minecraft mc, int positionX, int positionY)
    {
        if (this.visible)
        {
            FontRenderer fontRender = mc.fontRenderer;
            this.hovered = positionX >= this.posX && positionY >= this.posY && positionX < this.posX + this.width && positionY < this.posY + this.height;
            int hoverState = this.getHoverState(this.hovered);
            
            if(selected) {
            	textureSelectedAlpha = this.lerp(textureSelectedAlpha, 1.0F, 0.3F);
            	textureHoverAlpha = this.lerp(textureHoverAlpha, 0.0F, 0.3F);
            	textureAlpha = this.lerp(textureAlpha, 0.0F, 0.3F);
            }
            else if(hoverState == 2) {
            	textureSelectedAlpha = this.lerp(textureSelectedAlpha, 0.0F, 0.3F);
            	textureHoverAlpha = this.lerp(textureHoverAlpha, 1.0F, 0.3F);
            	textureAlpha = this.lerp(textureAlpha, 0.0F, 0.3F);
        	}
            else {
            	textureSelectedAlpha = this.lerp(textureSelectedAlpha, 0.0F, 0.3F);
            	textureHoverAlpha = this.lerp(textureHoverAlpha, 0.0F, 0.3F);
            	textureAlpha = this.lerp(textureAlpha, 1.0F, 0.3F);
            }
            
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDepthMask(false);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            
            GL11.glColor4f(1.0F, 1.0F, 1.0F, textureSelectedAlpha);
            mc.getTextureManager().bindTexture(GuiTheme.buttonSelected);
            this.drawScaledCustomSizeModalRect(this.posX, this.posY, 0, 0, 1, 1, this.width, this.height, 1, 1);
            
            GL11.glColor4f(1.0F, 1.0F, 1.0F, textureHoverAlpha);
            mc.getTextureManager().bindTexture(GuiTheme.buttonHover);
            this.drawScaledCustomSizeModalRect(this.posX, this.posY, 0, 0, 1, 1, this.width, this.height, 1, 1);
            
            GL11.glColor4f(1.0F, 1.0F, 1.0F, textureAlpha);
            mc.getTextureManager().bindTexture(GuiTheme.button);
            this.drawScaledCustomSizeModalRect(this.posX, this.posY, 0, 0, 1, 1, this.width, this.height, 1, 1);
            
            if(selected) GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            else if(hoverState == 2) GL11.glColor4f(0.9F, 0.9F, 0.9F, 1.0F);
            else GL11.glColor4f(0.5F, 0.5F, 0.5F, 1.0F);
            
            mc.getTextureManager().bindTexture(icon);
            this.drawScaledCustomSizeModalRect(this.posX+this.height/4, this.posY+this.height/4, 0, 0, 1, 1, (int)(this.height/2.5F*this.imgWidthScale), (int)(this.height/2*this.imgHeightScale), 1, 1);
            
            int color = GuiTheme.textColor;
            if (!this.enabled)
            {
            	color = GuiTheme.textDisabledColor;
            }

            fontRender.drawString(this.displayString, this.posX + 20, this.posY + (this.height - 8) / 2, color);
            
    		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(GL11.GL_BLEND);
        	GL11.glDepthMask(true);
        	GL11.glEnable(GL11.GL_ALPHA_TEST);
        }
    }
}
