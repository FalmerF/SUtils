package net.smb.sutils.effects;

import net.minecraft.client.Minecraft;
import net.smb.sutils.utils.Color;

public class ParticleBase {
	public Color color = new Color(1.0F, 1.0F, 1.0F, 1.0F);
	public float posX, posY;
	protected EffectBase effect;
	
	public ParticleBase(int posX, int posY, EffectBase effect) {
		this.posX = posX;
		this.posY = posY;
		this.effect = effect;
	}
	
	public void update() {}
	
	public void draw(Minecraft mc, int mouseX, int mouseY) {}
	
	public float moveTo(float a, float b, float f) {
    	if(a > b) {
    		f *= -1;
    		return Math.max(a+f, b);
    	}
    	else return Math.min(a+f, b);
    }
	
	public float lerp(float a, float b, float f)
    {
        return a + f * (b - a);
    }
}
