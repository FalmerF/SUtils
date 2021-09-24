package net.smb.sutils.effects;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import net.smb.sutils.utils.Color;

public class Effect6 extends EffectBase {
	private List<ParticleBase> particles = new ArrayList<ParticleBase>();

	public Effect6() {
		super("Короны", "Этот эффект доступен только для §6VIP");
		vip = true;
	}
	
	public void initEffect(int width, int height) {
		super.initEffect(width, height);
		
		particles.clear();
		int particleCount = random.nextInt(70)+30;
		for(int i = 0; i < particleCount; i++) {
			particles.add(new Particle6(random.nextInt(width), random.nextInt(height), this));
		}
	}
	
	public void update() {
		for(ParticleBase p : particles) p.update();
	}
	
	public void draw(Minecraft mc, int mouseX, int mouseY) {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		for(ParticleBase p : particles) p.draw(mc, mouseX, mouseY);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		lastMouseX = mouseX;
		lastMouseY = mouseY;
	}
}

class Particle6 extends ParticleBase {
	private ResourceLocation texture = new ResourceLocation("moderutils", "textures/crown.png");
	private float fallSpeed = 0.01F;
	private int size = 2;
	private float rotate = 0.0F;
	private float rotateSpeed = 1.0F;
	
	public Particle6(int posX, int posY, EffectBase effect) {
		super(posX, posY, effect);
		color = new Color(1.0F, 1.0F, 1.0F, 1.0F);
		color.colorA = effect.random.nextFloat()*0.3F+0.7F;
		fallSpeed = effect.random.nextFloat()*2+2;
		size = effect.random.nextInt(10)+10;
		rotate = effect.random.nextInt(360);
		rotateSpeed = effect.random.nextFloat()*5-2.5F;
	}
	
	public void update() {
		posY += fallSpeed;
		rotate += rotateSpeed;

		if(posY >= effect.height+size) {
			posY = -size*2;
			posX = effect.random.nextInt(effect.width);
		}
	}
	
	public void draw(Minecraft mc, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(texture);
		GL11.glColor4f(color.colorR, color.colorG, color.colorB, color.colorA);
		GL11.glPushMatrix();
		GL11.glTranslatef((int)(this.posX-size/2), (int)(this.posY-size/2), 0);
		GL11.glRotatef(rotate, 0, 0, 1);
        Gui.func_152125_a(0, 0, 0, 0, 1, 1, size, size, 1, 1);
        GL11.glPopMatrix();
	}
}
