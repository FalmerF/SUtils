package net.smb.sutils.effects;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.smb.sutils.utils.Color;
import net.smb.sutils.utils.RenderUtils;

public class Effect4 extends EffectBase {
	private List<ParticleBase> particles = new ArrayList<ParticleBase>();

	public Effect4() {
		super("Эндер частицы", "");
	}
	
	public void initEffect(int width, int height) {
		super.initEffect(width, height);
		
		particles.clear();
		int particleCount = random.nextInt(130)+30;
		for(int i = 0; i < particleCount; i++) {
			particles.add(new Particle4(random.nextInt(width), random.nextInt(height), this));
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

class Particle4 extends ParticleBase {
	private float upSpeed = 0.01F;
	private float colorMove = 0.01f;
	private int size = 2;
	private int canMove = 10;
	
	public Particle4(int posX, int posY, EffectBase effect) {
		super(posX, posY, effect);
		color = new Color(0.75F, 0.22F, 0.83F, 1.0F);
		colorMove = effect.random.nextFloat()/30+0.03F;
		upSpeed = effect.random.nextFloat()+1;
		size = effect.random.nextInt(2)+1;
	}
	
	public void update() {
		if(canMove <= 0) posY -= upSpeed;
		else canMove--;
		color.colorA -= colorMove;
		
		if(color.colorA <= 0) {
			posX = effect.random.nextInt(effect.width);
			posY = effect.random.nextInt(effect.height);
			colorMove = effect.random.nextFloat()/30+0.03F;
			upSpeed = effect.random.nextFloat()+1;
			color.colorA = 1.0F;
			canMove = 10;
		}
	}
	
	public void draw(Minecraft mc, int mouseX, int mouseY) {
        RenderUtils.drawRect((int)(posX-size), (int)(posY-size), (int)(posX+size), (int)(posY+size), color);
	}
}
