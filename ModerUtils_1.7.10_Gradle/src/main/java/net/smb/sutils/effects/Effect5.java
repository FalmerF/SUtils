package net.smb.sutils.effects;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.smb.sutils.utils.Color;
import net.smb.sutils.utils.RenderUtils;

public class Effect5 extends EffectBase {
	private List<ParticleBase> particles = new ArrayList<ParticleBase>();

	public Effect5() {
		super("Пустота", "");
	}
	
	public void initEffect(int width, int height) {
		super.initEffect(width, height);
		
		particles.clear();
		int particleCount = random.nextInt(130)+30;
		for(int i = 0; i < particleCount; i++) {
			particles.add(new Particle5(random.nextInt(width), random.nextInt(height), this));
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

class Particle5 extends ParticleBase {
	private float colorMove = 0.01f;
	private int size = 2;
	private float moveX, moveY;
	
	public Particle5(int posX, int posY, EffectBase effect) {
		super(posX, posY, effect);
		color = new Color(0.4F, 0.4F, 0.4F, 1.0F);
		colorMove = effect.random.nextFloat()/30+0.02F;
		moveX = effect.random.nextFloat()-0.5F;
		moveY = effect.random.nextFloat()-0.5F;
		size = effect.random.nextInt(2)+1;
	}
	
	public void update() {
		color.colorA -= colorMove;
		posX += moveX;
		posY += moveY;
		
		if(color.colorA <= 0) {
			posX = effect.random.nextInt(effect.width);
			posY = effect.random.nextInt(effect.height);
			colorMove = effect.random.nextFloat()/30+0.02F;
			color.colorA = 1.0F;
			moveX = effect.random.nextFloat()-0.5F;
			moveY = effect.random.nextFloat()-0.5F;
		}
	}
	
	public void draw(Minecraft mc, int mouseX, int mouseY) {
        RenderUtils.drawRect((int)(posX-size), (int)(posY-size), (int)(posX+size), (int)(posY+size), color);
	}
}
