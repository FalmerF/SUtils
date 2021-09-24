package net.smb.moderutils.effects;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import net.eq2online.console.Log;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;

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
		Tessellator var9 = Tessellator.getInstance();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glLineWidth(width);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        
        BufferBuilder vertexbuffer = var9.getBuffer();
        vertexbuffer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
        vertexbuffer.pos((double)x1, (double)y1, 0.0D).endVertex();
        vertexbuffer.pos((double)x2, (double)y2, 0.0D).endVertex();
        var9.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
}
