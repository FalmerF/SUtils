package net.smb.sutils.effects;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import net.smb.sutils.utils.Color;

public class Effect1 extends EffectBase {
	private List<ParticleBase> particles = new ArrayList<ParticleBase>();

	public Effect1() {
		super("Космос?", "Этот эффект доступен только для §6VIP");
		vip = true;
	}
	
	public void initEffect(int width, int height) {
		super.initEffect(width, height);
		
		particles.clear();
		int particleCount = random.nextInt(200)+100;
		for(int i = 0; i < particleCount; i++) {
			particles.add(new Particle1(random.nextInt(width), random.nextInt(height), this));
		}
	}
	
	public void update() {
		for(ParticleBase p : particles) p.update();
	}
	
	public void draw(Minecraft mc, int mouseX, int mouseY) {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		List<ParticleBase> activeParticles = new ArrayList<ParticleBase>();
		for(ParticleBase p : particles) {
			if(getDistance(p.posX, p.posY, mouseX, mouseY) <= 100) activeParticles.add(p);
		}
		GL11.glColor4f(0.85F, 0.85F, 0.95F, 0.7F);
		while(activeParticles.size() > 0) {
			ParticleBase particle = activeParticles.remove(0);
			for(ParticleBase p : activeParticles) {
				if(getDistance(p.posX, p.posY, particle.posX, particle.posY) <= 60)
					drawLine(p.posX, p.posY, particle.posX, particle.posY, (float)Math.max(Math.ceil((p.color.colorA+particle.color.colorA)*1.5F), 1));
			}
		}
		
		for(ParticleBase p : particles) p.draw(mc, mouseX, mouseY);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
	}
}

class Particle1 extends ParticleBase {
	private ResourceLocation texture = new ResourceLocation("moderutils", "textures/slider_handle.png");
	private boolean colorMove = false;
	private float colorStep = 0.01F;
	private int size = 2;
	
	public Particle1(int posX, int posY, EffectBase effect) {
		super(posX, posY, effect);
		color = new Color(0.85F, 0.85F, 0.95F);
		color.colorA = effect.random.nextFloat();
		colorStep = effect.random.nextFloat()/30;
		size = effect.random.nextInt(2)+3;
	}
	
	public void update() {
		if(colorMove) {
			color.colorA = moveTo(color.colorA, 1.0F, colorStep);
			if(color.colorA >= 1.0F) colorMove = false;
		}
		else {
			color.colorA = moveTo(color.colorA, 0.0F, colorStep);
			if(color.colorA <= 0.0F) colorMove = true;
		}
	}
	
	public void draw(Minecraft mc, int mouseX, int mouseY) {
		mc.getTextureManager().bindTexture(texture);
		GL11.glColor4f(color.colorR, color.colorG, color.colorB, color.colorA);
        Gui.func_152125_a((int)(this.posX-size/2), (int)(this.posY-size/2), 0, 0, 1, 1, size, size, 1, 1);
	}
}
