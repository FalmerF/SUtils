package net.smb.sutils.gui.elements;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import net.smb.sutils.common.GuiTheme;

public class GuiButtonStandart extends GuiElement {
    public String displayString;
    public boolean selected;
    public int minWidth = 20;
    
    public GuiButtonStandart(int id, int posX, int posY, String displayString)
    {
        this(id, posX, posY, 200, 20, displayString);
    }

    public GuiButtonStandart(int id, int posX, int posY, int width, int height, String displayString)
    {
    	super(id, posX, posY, width, height);
        this.displayString = displayString;
    }
    
    @SuppressWarnings("static-access")
	public void draw(Minecraft mc, int positionX, int positionY)
    {
        if (this.visible)
        {
            FontRenderer fontRender = mc.fontRenderer;
            this.hovered = positionX >= this.posX && positionY >= this.posY && positionX < this.posX + this.width && positionY < this.posY + this.height;
            int hoverState = this.getHoverState(this.hovered);
            
            mc.getTextureManager().bindTexture(GuiTheme.buttonStandart);
            
            if(hoverState == 2) GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            else if(hoverState == 1) GL11.glColor4f(0.9F, 0.9F, 0.9F, 1.0F);
            else GL11.glColor4f(0.5F, 0.5F, 0.5F, 1.0F);

            this.func_152125_a(this.posX, this.posY, 0, 0, 1, 1, this.minWidth, this.height, 3, 1);
            this.func_152125_a(this.posX+this.width-this.minWidth, this.posY, 2, 0, 1, 1, this.minWidth, this.height, 3, 1);
            this.func_152125_a(this.posX+this.minWidth, this.posY, 1, 0, 1, 1, this.width-this.minWidth*2, this.height, 3, 1);
            
            int color = GuiTheme.textColor;
            if (!this.enabled)
            {
            	color = GuiTheme.textDisabledColor;
            }
            
            GL11.glPushMatrix();
        	GL11.glTranslatef(this.posX + this.width/2, this.posY + (this.height - 6) / 2, 0.0F);
            GL11.glScalef(0.8F, 0.8F, 0.8F);
            fontRender.drawString(this.displayString, -fontRender.getStringWidth(displayString)/2, 0, color);
            GL11.glPopMatrix();
        }
    }
}
