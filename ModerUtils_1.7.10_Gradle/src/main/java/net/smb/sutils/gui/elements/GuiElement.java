package net.smb.sutils.gui.elements;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

public class GuiElement extends Gui {
	public int id;
    public int posX, relPosX;
    public int posY, relPosY;
	public int width;
	public int height;
    public boolean enabled;
    public boolean visible;
    public boolean hovered;
    public boolean pressed;
    public boolean selected;
    
    GuiElement(int id, int posX, int posY, int width, int height){
    	this.id = id;
    	this.posX = posX;
    	this.posY = posY;
    	this.relPosX = posX;
    	this.relPosY = posY;
    	this.width = width;
    	this.height = height;
    	this.enabled = true;
    	this.visible = true;
    }
    
    public int getHoverState(boolean hovered)
    {
        byte state = 1;

        if (!this.enabled)
        {
        	state = 0;
        }
        else if (hovered)
        {
        	state = 2;
        }

        return state;
    }
    
    public void draw(Minecraft mc, int positionX, int positionY) {}
    
    protected void dragged(Minecraft mc, int posX, int posY) {}
    
    public void released(int p_146118_1_, int p_146118_2_) {
    	this.pressed = false;
    }
    
    public boolean pressed(Minecraft mc, int posX, int posY)
    {
    	if(this.enabled && this.visible && posX >= this.posX && posY >= this.posY && posX < this.posX + this.width && posY < this.posY + this.height) {
    		return true;
    	}
        return false;
    }
    
    public void clicked() {}
    public void clicked(int id) {}
    
    public boolean isMouseOver()
    {
        return this.hovered;
    }
    
    public void setSelected(boolean selected) {
    	this.selected = selected;
    }
    
    public void keyTyped(char key, int keyId) {}
    
    public void playPressSound(SoundHandler handler)
    {
    	handler.playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
    }
    
    public void glScissor(int x, int y, int width, int height){
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution resolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        int scale = resolution.getScaleFactor();

        int scissorWidth = width * scale;
        int scissorHeight = height * scale;
        int scissorX = x * scale;
        int scissorY = mc.displayHeight - scissorHeight - (y * scale);

        GL11.glScissor(scissorX, scissorY, scissorWidth, scissorHeight);
    }
    
    public float lerp(float a, float b, float f)
    {
        return a + f * (b - a);
    }
    
    public float moveTo(float a, float b, float f) {
    	if(a > b) {
    		f *= -1;
    		return Math.max(a+f, b);
    	}
    	else return Math.min(a+f, b);
    }
    
    public void update() {}
}
