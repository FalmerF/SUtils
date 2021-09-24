package net.smb.sutils.gui.elements;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import net.smb.sutils.ModuleInfo;
import net.smb.sutils.common.GuiTheme;

public class GuiFunction extends GuiElement {
    public int minHeight = 64, minWidth = 110;
    
    public String displayString;
    public List<GuiElement> elements = new ArrayList<GuiElement>();

    public GuiFunction(int id, int posX, int posY, int height, String displayString)
    {
    	super(id, posX, posY, 165, height);
        this.displayString = displayString;
        setPos();
    }
    
    @SuppressWarnings("static-access")
	public void draw(Minecraft mc, int positionX, int positionY)
    {
        if (this.visible)
        {
        	if(ModuleInfo.scissorEnable)
        		if(this.posY+this.height < ModuleInfo.sY || this.posY > ModuleInfo.sY+ModuleInfo.sH || this.posX+this.width < ModuleInfo.sX || this.posX > ModuleInfo.sX+ModuleInfo.sW) return;
        	
            FontRenderer fontRender = mc.fontRenderer;
            
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDepthMask(false);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            
            // Draw Background
            mc.getTextureManager().bindTexture(GuiTheme.functionBackground);
            this.func_152125_a(this.posX, this.posY, 0, 0, 1, 1, this.minWidth/2, this.minHeight/2, 3, 3);
            this.func_152125_a(this.posX+this.width-this.minWidth/2, this.posY, 2, 0, 1, 1, this.minWidth/2, this.minHeight/2, 3, 3);
            this.func_152125_a(this.posX+this.width-this.minWidth/2, this.posY+this.height-this.minHeight/2, 2, 2, 1, 1, this.minWidth/2, this.minHeight/2, 3, 3);
            this.func_152125_a(this.posX, this.posY+this.height-this.minHeight/2, 0, 2, 1, 1, this.minWidth/2, this.minHeight/2, 3, 3);
            
            this.func_152125_a(this.posX, this.posY+this.minHeight/2, 0, 1, 1, 1, this.minWidth/2, this.height-this.minHeight, 3, 3);
            this.func_152125_a(this.posX+this.width-this.minWidth/2, this.posY+this.minHeight/2, 2, 1, 1, 1, this.minWidth/2, this.height-this.minHeight, 3, 3);
            this.func_152125_a(this.posX+this.minWidth/2, this.posY, 1, 0, 1, 1, this.width-this.minWidth, this.minHeight/2, 3, 3);
            this.func_152125_a(this.posX+this.minWidth/2, this.posY+this.height-this.minHeight/2, 1, 2, 1, 1, this.width-this.minWidth, this.minHeight/2, 3, 3);
            
            this.func_152125_a(this.posX+this.minWidth/2, this.posY+this.minHeight/2, 1, 1, 1, 1, this.width-this.minWidth, this.height-this.minHeight, 3, 3);
        	
            GL11.glDepthMask(true);
        	GL11.glEnable(GL11.GL_ALPHA_TEST);
            
            // Draw Title
        	fontRender.drawString(this.displayString, this.posX + 30, this.posY+18, GuiTheme.textColor);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            for(GuiElement element : this.elements) {
        		element.draw(mc, positionX, positionY);
            }
        }
    }
    
    public void setEnable(boolean enabled) {
    	this.enabled = enabled;
    	for(GuiElement element : this.elements) {
    		element.enabled = enabled;
    		if(element instanceof GuiScrollView) {
        		((GuiScrollView)element).setEnable(visible);
        	}
        }
    }
    
    public void setVisible(boolean visible) {
    	this.visible = visible;
    	for(GuiElement element : this.elements) {
    		element.visible = visible;
    		if(element instanceof GuiScrollView) {
        		((GuiScrollView)element).setVisible(visible);
        	}
        }
    }
    
    public void setPos() {
    	for(GuiElement element : this.elements) {
    		element.posX = element.relPosX + this.posX;
    		element.posY = element.relPosY + this.posY;
    		if(element instanceof GuiToggle) {
        		((GuiToggle)element).SetNeedPos();
        	}
    		else if(element instanceof GuiScrollView) ((GuiScrollView)element).setPos();
        }
    }
    
    public GuiElement getById(int id) {
    	for(GuiElement element : this.elements) {
    		if(element.id == id) return element;
    		else if(element instanceof GuiScrollView) {
    			GuiElement e = ((GuiScrollView)element).getById(id);
    			if(e != null) return e;
    		}
        }
    	return null;
    }
    
    public void update() {
		for(GuiElement element : elements) element.update();
	}
}
