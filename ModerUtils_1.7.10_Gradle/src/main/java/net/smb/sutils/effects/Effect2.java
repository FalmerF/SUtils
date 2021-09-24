package net.smb.sutils.effects;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.smb.sutils.utils.Color;
import net.smb.sutils.utils.RenderUtils;

public class Effect2 extends EffectBase {
	private List<ParticleBase> particles = new ArrayList<ParticleBase>();

	public Effect2() {
		super("Капли воды", "");
	}
	
	public void initEffect(int width, int height) {
		super.initEffect(width, height);
		
		particles.clear();
		int particleCount = random.nextInt(170)+70;
		for(int i = 0; i < particleCount; i++) {
			particles.add(new Particle2(random.nextInt(width), random.nextInt(height), this));
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

class Particle2 extends ParticleBase {
	private float fallSpeed = 0.01F;
	private int size = 2;
	
	public Particle2(int posX, int posY, EffectBase effect) {
		super(posX, posY, effect);
		color = new Color(0.4F, 0.44F, 0.63F, 0.5F);
		color.colorA = effect.random.nextFloat()*0.4F+0.5F;
		fallSpeed = effect.random.nextFloat()*2+2;
		size = effect.random.nextInt(2)+1;
	}
	
	public void update() {
		posY += fallSpeed;
		if(effect.getDistance(posX, posY, effect.lastMouseX, effect.lastMouseY) <= 50) {
			try {
				
				
				float d = posX-effect.lastMouseX;
				if(d > 0) posX = lerp(posX, effect.lastMouseX+65, 0.1F);
				else posX = lerp(posX, effect.lastMouseX-65, 0.1F);
				
				d = posY-effect.lastMouseY;
				if(d > 0) posY = lerp(posY, effect.lastMouseY+65, 0.1F);
				else posY = lerp(posY, effect.lastMouseY-65, 0.1F);
			} catch(Exception e) {}
		}

		if(posY >= effect.height+size) {
			posY = -size*2;
			posX = effect.random.nextInt(effect.width);
		}
	}
	
	public void draw(Minecraft mc, int mouseX, int mouseY) {
        RenderUtils.drawRect((int)(posX-size), (int)(posY-size), (int)(posX+size), (int)(posY+size), color);
	}
}
