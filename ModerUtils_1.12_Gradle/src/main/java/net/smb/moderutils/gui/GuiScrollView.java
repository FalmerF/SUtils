package net.smb.moderutils.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import net.eq2online.console.Log;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.smb.moderutils.GuiTheme;
import net.smb.moderutils.ModuleInfo;

public class GuiScrollView extends GuiElement {
    public int minHeight = 12, barWidth = 5;
    public float value;
    public boolean scrollHovered;
    public float goToValue = -1;
    
    public String displayString;
    List<GuiElement> elements = new ArrayList<GuiElement>();

    public GuiScrollView(int id, int posX, int posY, int width, int height)
    {
    	super(id, posX, posY, width, height);
        
        setPos();
    }
    
    public void update() {
		for(GuiElement element : elements) element.update();
		
		if(goToValue >= 0) {
			value = moveTo(value, goToValue, 0.02f);
			setPos();
			if(value == goToValue) goToValue = -1;
		}
	}
    
    @SuppressWarnings("static-access")
	public void draw(Minecraft mc, int posX, int posY)
    {
        if (this.visible)
        {
        	this.hovered = posX >= this.posX+this.width-this.barWidth && posY >= this.posY && posX < this.posX + this.width && posY < this.posY + this.height;
        	
			if(this.hovered || this.pressed) GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			else GL11.glColor4f(0.9F, 0.9F, 0.9F, 1.0F);
            
            // Draw Scroll Bar
            mc.getTextureManager().bindTexture(GuiTheme.scrollBar);
            this.drawScaledCustomSizeModalRect(this.posX+this.width-this.barWidth, this.posY, 0, 0, 1, 1, this.barWidth, this.minHeight, 1, 3);
            this.drawScaledCustomSizeModalRect(this.posX+this.width-this.barWidth, this.posY+this.height-this.minHeight, 0, 2, 1, 1, this.barWidth, this.minHeight, 1, 3);
            this.drawScaledCustomSizeModalRect(this.posX+this.width-this.barWidth, this.posY+this.minHeight, 0, 1, 1, 1, this.barWidth, this.height-(this.minHeight*2), 1, 3);
            
            // Draw Scroll Handle
            mc.getTextureManager().bindTexture(GuiTheme.scrollHandle);
            this.drawScaledCustomSizeModalRect(this.posX+this.width-this.barWidth, (int)(this.posY+((this.height-(this.barWidth*4))*value)), 0, 0, 1, 1, this.barWidth, this.barWidth*4, 1, 1);
            
            this.dragged(mc, posX, posY);
        
	        GL11.glEnable(GL11.GL_SCISSOR_TEST);
	        boolean scissorPreEnable = ModuleInfo.scissorEnable;
	    	int preSPosX = ModuleInfo.sX;
	    	int preSPosY = ModuleInfo.sY;
	    	int preSWidth = ModuleInfo.sW;
	    	int preSHeight = ModuleInfo.sH;
	        if(scissorPreEnable) {
	            int sPosX = Math.max(Math.min(this.posX, ModuleInfo.sX + ModuleInfo.sW), ModuleInfo.sX);
	            int sPosY = Math.max(Math.min(this.posY, ModuleInfo.sY + ModuleInfo.sH), ModuleInfo.sY);
	            int sWidth = 0;
	            int sHeight = 0;
	            if(this.posX < sPosX) sWidth = Math.max(Math.min(this.width, this.posX+this.width-sPosX), 0);
	            else sWidth = Math.max(Math.min(this.width, ModuleInfo.sX + ModuleInfo.sW - sPosX), 0);
	            if(this.posY < sPosY) sHeight = Math.max(Math.min(this.height, this.posY+this.height-sPosY), 0);
	            else sHeight = Math.max(Math.min(this.height, ModuleInfo.sY + ModuleInfo.sH - sPosY), 0);
	            
	            ModuleInfo.sX = sPosX;
	            ModuleInfo.sY = sPosY;
	            ModuleInfo.sW = sWidth;
	            ModuleInfo.sH = sHeight;
	            
	            this.glScissor(sPosX, sPosY, sWidth, sHeight);
	        }
	        else {
	        	ModuleInfo.scissorEnable = true;
	            ModuleInfo.sX = this.posX;
	            ModuleInfo.sY = this.posY;
	            ModuleInfo.sW = this.width;
	            ModuleInfo.sH = this.height;
	            this.glScissor(this.posX, this.posY, this.width, this.height);
	        }
	        
	        boolean elementHovered = false;
	        
	        for(GuiElement element : this.elements) {
	    		element.draw(mc, posX, posY);
	    		if((element instanceof GuiScrollView) && ((GuiScrollView)element).scrollHovered) elementHovered = true;
	    		else if(element instanceof GuiFunction) {
	    			for(GuiElement element2 : ((GuiFunction)element).elements) {
	    				if((element2 instanceof GuiScrollView) && ((GuiScrollView)element2).scrollHovered) elementHovered = true;
	    			}
	    		}
	        }
	        if(scissorPreEnable) {
	        	 ModuleInfo.sX = preSPosX;
	             ModuleInfo.sY = preSPosY;
	             ModuleInfo.sW = preSWidth;
	             ModuleInfo.sH = preSHeight;
	             this.glScissor(preSPosX, preSPosY, preSWidth, preSHeight);
	        }
	        else {
		        GL11.glDisable(GL11.GL_SCISSOR_TEST);
		        ModuleInfo.scissorEnable = false;
	        }
	        
	        this.scrollHovered = !elementHovered && posX >= this.posX && posY >= this.posY && posX < this.posX + this.width && posY < this.posY + this.height;
        }
    }
    
    public int getmaxHeight() {
    	int height = 0;
    	for(GuiElement element : this.elements) {
    		if(element.relPosY + element.height > height) {
    			height = element.relPosY + element.height;
    		}
        }
    	height = Math.max(height-this.height, 0);
    	return height;
    }
    
    public void mouseScroll(int id) {
    	for(GuiElement element : this.elements) {
    		if((element instanceof GuiScrollView)) ((GuiScrollView)element).mouseScroll(id);
    		else if(element instanceof GuiFunction) {
    			for(GuiElement element2 : ((GuiFunction)element).elements) {
    				if((element2 instanceof GuiScrollView)) ((GuiScrollView)element2).mouseScroll(id);
    			}
    		}
        }
    	if(this.scrollHovered) {
    		int maxHeight = getmaxHeight();
    		this.value = this.value-(1/(maxHeight/20.0F))*id;
    		this.value = Math.max(Math.min(this.value, 1), 0);
    		setPos();
    	}
    }
    
    public boolean pressed(Minecraft mc, int posX, int posY)
    {
    	if(this.enabled && this.visible && posX >= this.posX+this.width-this.barWidth && posY >= this.posY && posX < this.posX + this.width && posY < this.posY + this.height) {
    		return true;
    	}
        return false;
    }
    
    protected void dragged(Minecraft mc, int posX, int posY) {
    	if(this.enabled && this.visible && this.pressed) {
    		this.value = (float)(posY-this.posY-this.barWidth*2)/(float)(this.height-this.barWidth*4);
    		this.value = Math.max(Math.min(this.value, 1), 0);
    		
    		this.setPos();
    	}
    }
    
    public void setEnable(boolean enabled) {
    	this.enabled = enabled;
    	for(GuiElement element : this.elements) {
    		element.enabled = enabled;
    		if(element instanceof GuiFunction) {
        		((GuiFunction)element).setEnable(enabled);
        	}
    		else if(element instanceof GuiScrollView) {
        		((GuiScrollView)element).setEnable(visible);
        	}
        }
    }
    
    public void setVisible(boolean visible) {
    	this.visible = visible;
    	for(GuiElement element : this.elements) {
    		element.visible = visible;
    		if(element instanceof GuiFunction) {
        		((GuiFunction)element).setVisible(visible);
        	}
    		else if(element instanceof GuiScrollView) {
        		((GuiScrollView)element).setVisible(visible);
        	}
        }
    }
    
    public void setPos() {
    	int maxHeight = getmaxHeight();
    	for(GuiElement element : this.elements) {
        	element.posX = element.relPosX+this.posX;
        	element.posY = (int)(element.relPosY+this.posY-(maxHeight*value));
        	if(element instanceof GuiFunction) {
        		((GuiFunction)element).setPos();
        	}
        	else if(element instanceof GuiScrollView) ((GuiScrollView)element).setPos();
        	else if(element instanceof GuiToggle) {
        		((GuiToggle)element).SetNeedPos();
        	}
        }
    }
    
    public GuiElement getById(int id) {
    	for(GuiElement element : this.elements) {
    		if(element.id == id) return element;
    		else if(element instanceof GuiFunction) {
    			GuiElement e = ((GuiFunction)element).getById(id);
    			if(e != null) return e;
    		}
    		else if(element instanceof GuiScrollView) {
    			GuiElement e = ((GuiScrollView)element).getById(id);
    			if(e != null) return e;
    		}
        }
    	return null;
    }
}
