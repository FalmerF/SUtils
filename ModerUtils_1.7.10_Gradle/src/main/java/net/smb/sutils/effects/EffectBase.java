package net.smb.sutils.effects;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;

public class EffectBase {
	public static EffectBase[] effects = new EffectBase[] {null, new Effect1(), new Effect2(), new Effect3(), new Effect4(), new Effect5(), new Effect6()};
	
	protected String name, description;
	public int width, height;
	protected Random random = new Random();
	public int lastMouseX = 0, lastMouseY = 0;
	public boolean vip = false;
	
	public EffectBase(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	public void initEffect(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public void update() {}
	
	public void draw(Minecraft mc, int mouseX, int mouseY) {}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public int getDistance(float x1, float y1, float x2, float y2) {
		return (int)Math.sqrt(Math.pow(x1-x2, 2)+Math.pow(y1-y2, 2));
	}
	
	public void drawLine(float x1, float y1, float x2, float y2, float width) {
		Tessellator var9 = Tessellator.instance;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glLineWidth(width);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        var9.startDrawing(GL11.GL_LINE_STRIP);
        var9.addVertex((double)x1, (double)y1, 0.0D);
        var9.addVertex((double)x2, (double)y2, 0.0D);
        var9.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
}
