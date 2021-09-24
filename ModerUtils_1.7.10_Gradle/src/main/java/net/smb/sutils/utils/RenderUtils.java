package net.smb.sutils.utils;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;

public class RenderUtils {
	
	
    public static void drawRect(int pos1, int pos2, int pos3, int pos4, Color color)
    {
        int var5;

        if (pos1 < pos3)
        {
            var5 = pos1;
            pos1 = pos3;
            pos3 = var5;
        }

        if (pos2 < pos4)
        {
            var5 = pos2;
            pos2 = pos4;
            pos4 = var5;
        }

        Tessellator var9 = Tessellator.instance;
		GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(color.colorR, color.colorG, color.colorB, color.colorA);
        var9.startDrawingQuads();
        var9.addVertex((double)pos1, (double)pos4, 0);
        var9.addVertex((double)pos3, (double)pos4, 0);
        var9.addVertex((double)pos3, (double)pos2, 0);
        var9.addVertex((double)pos1, (double)pos2, 0);
        var9.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }
    
    public static void drawGradientRect(int pos1, int pos2, int pos3, int pos4, int startColor, int endColor)
    {
        float var7 = (float)(startColor >> 24 & 255) / 255.0F;
        float var8 = (float)(startColor >> 16 & 255) / 255.0F;
        float var9 = (float)(startColor >> 8 & 255) / 255.0F;
        float var10 = (float)(startColor & 255) / 255.0F;
        float var11 = (float)(endColor >> 24 & 255) / 255.0F;
        float var12 = (float)(endColor >> 16 & 255) / 255.0F;
        float var13 = (float)(endColor >> 8 & 255) / 255.0F;
        float var14 = (float)(endColor & 255) / 255.0F;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        Tessellator var15 = Tessellator.instance;
        var15.startDrawingQuads();
        var15.setColorRGBA_F(var8, var9, var10, var7);
        var15.addVertex((double)pos3, (double)pos2, 0);
        var15.addVertex((double)pos1, (double)pos2, 0);
        var15.setColorRGBA_F(var12, var13, var14, var11);
        var15.addVertex((double)pos1, (double)pos4, 0);
        var15.addVertex((double)pos3, (double)pos4, 0);
        var15.draw();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }
}
