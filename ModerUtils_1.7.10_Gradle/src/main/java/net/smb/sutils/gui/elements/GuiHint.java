package net.smb.sutils.gui.elements;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.smb.sutils.common.GuiTheme;
import net.smb.sutils.utils.Color;
import net.smb.sutils.utils.RenderUtils;

public class GuiHint extends GuiElement {
    protected static final ResourceLocation whiteBackground = new ResourceLocation("moderutils", "textures/white_window.png");
    public int minHeight = 50, minWidth = 50;
    GuiTextString text;
    boolean selectRegion = false, skiped = false;
    int s1, s2, s3, s4;
    int animState = 0;
    float scale = 0;

    public GuiHint(int id, int posX, int posY, int width, int height, String text)
    {
    	super(id, posX, posY, width, height);
    	this.text = new GuiTextString(posX+5, posY+5, 1.0F, text);
    	this.text.shadow = false;
    	this.visible = false;
    }
    
    public void setRegion(int s1, int s2, int s3, int s4) {
    	this.s1 = s1-posX;
    	this.s2 = s2-posY;
    	this.s3 = s3-posX;
    	this.s4 = s4-posY;
    	selectRegion = true;
    }
    
    public void update() {
    	if(this.animState == 1) {
    		scale += 0.2f;
    		if(scale >= 1.2f) this.animState = 2;
    	}
    	else if(this.animState == 2) {
    		scale -= 0.05f;
    		if(scale <= 1f) this.animState = 0;
    	}
    	else if(this.animState == 3) {
    		scale += 0.05f;
    		if(scale >= 1.2f) this.animState = 4;
    	}
    	else if(this.animState == 4) {
    		scale -= 0.2f;
    		if(scale <= 0f) {
    			this.animState = 0;
    			this.visible = false;
    		}
    	}
    }
    
    @SuppressWarnings("static-access")
	public void draw(Minecraft mc, int positionX, int positionY)
    {
        if (this.visible)
        {
        	if(selectRegion) {
        		GL11.glPushMatrix();
        		GL11.glTranslatef(posX, posY, 0);
        		Color col = new Color(GuiTheme.hints, scale);
        		RenderUtils.drawRect(s1-1, s2, s1, s4, col);
        		RenderUtils.drawRect(s3+1, s2, s3, s4, col);
        		RenderUtils.drawRect(s1-1, s2-1, s3+1, s2, col);
        		RenderUtils.drawRect(s1-1, s4+1, s3+1, s4, col);
        		GL11.glPopMatrix();
        	}
        	GL11.glColor4f(GuiTheme.hints.colorR, GuiTheme.hints.colorG, GuiTheme.hints.colorB, 1.0F);
        	
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDepthMask(false);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            
            int scaledWidth = (int)(this.width*this.scale);
            int scaledHeight = (int)(this.height*this.scale);
            int scaledX = (this.width-scaledWidth)/2+this.posX;
            int scaledY = (this.height-scaledHeight)/2+this.posY;
            
            GL11.glPushMatrix();
            GL11.glTranslatef(scaledX, scaledY, 0.0F);
            GL11.glScalef(scale, scale, scale);
            scaledX = 0;
            scaledY = 0;

            // Draw Background
            mc.getTextureManager().bindTexture(whiteBackground);
            this.func_152125_a(scaledX, scaledY, 0, 0, 1, 1, this.minWidth/2, this.minHeight/2, 3, 3);
            this.func_152125_a(scaledX+width-this.minWidth/2, scaledY, 2, 0, 1, 1, this.minWidth/2, this.minHeight/2, 3, 3);
            this.func_152125_a(scaledX+width-this.minWidth/2, scaledY+height-this.minHeight/2, 2, 2, 1, 1, this.minWidth/2, this.minHeight/2, 3, 3);
            this.func_152125_a(scaledX, scaledY+height-this.minHeight/2, 0, 2, 1, 1, this.minWidth/2, this.minHeight/2, 3, 3);
            
            this.func_152125_a(scaledX, scaledY+this.minHeight/2, 0, 1, 1, 1, this.minWidth/2, height-this.minHeight, 3, 3);
            this.func_152125_a(scaledX+width-this.minWidth/2, scaledY+this.minHeight/2, 2, 1, 1, 1, this.minWidth/2, height-this.minHeight, 3, 3);
            this.func_152125_a(scaledX+this.minWidth/2, scaledY, 1, 0, 1, 1, width-this.minWidth, this.minHeight/2, 3, 3);
            this.func_152125_a(scaledX+this.minWidth/2, scaledY+height-this.minHeight/2, 1, 2, 1, 1, width-this.minWidth, this.minHeight/2, 3, 3);
            
            this.func_152125_a(scaledX+this.minWidth/2, scaledY+this.minHeight/2, 1, 1, 1, 1, width-this.minWidth, height-this.minHeight, 3, 3);
        	
            GL11.glDepthMask(true);
        	GL11.glEnable(GL11.GL_ALPHA_TEST);
        	text.posX = 5;
        	text.posY = 5;
        	text.draw(mc, positionX, positionY);
        	GL11.glPopMatrix();
        }
    }
    
    public boolean pressed(Minecraft mc, int posX, int posY)
    {
    	if(this.enabled && this.visible && !this.skiped && posX >= this.posX && posY >= this.posY && posX < this.posX + this.width && posY < this.posY + this.height) {
    		this.skiped = true;
    		return true;
    	}
        return false;
    }
    
    public void setVisible(boolean visible) {
    	if(visible) {
    		this.scale = 0;
    		this.animState = 1;
    		this.visible = visible;
    	}
    	else {
    		this.scale = 1;
    		this.animState = 3;
    	}
    }
}
