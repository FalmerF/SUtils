package net.smb.moderutils.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.smb.moderutils.FunctionDescriptions;
import net.smb.moderutils.GuiTheme;
import net.smb.moderutils.ModuleInfo;

public class GuiDescription extends GuiElement {
	public GuiDescription() {
		super(-1, 0, 0, 210, 0);
		this.visible = false;
	}

	public String description = "";
	public String example = "";
	
    private int tooltipMH = 70, tooltipWidth = 60;
    
    private GuiScreen currentScreen;
	
	public void setParams(String description, String example) {
		this.description = description;
		this.example = example;
		this.visible = true;
	}
	
    @SuppressWarnings("static-access")
	public void draw(Minecraft mc, int positionX, int positionY)
    {
        if (this.visible)
        {
            // Draw Tooltip
        	
        	GL11.glEnable(GL11.GL_BLEND);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDepthMask(false);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            
            // Draw Background
        	mc.getTextureManager().bindTexture(GuiTheme.tooltipBackground);
        	int tooltipPosX = positionX - 7;
        	int tooltipPosY = positionY - 10;
        	String[] descLines = this.description.split("\\n");
        	String[] exampleLines = this.example.split("\\n");
        	int exampleHeight = 8+(7*exampleLines.length);
        	int tooltipHeight = 50 + exampleHeight + (descLines.length*10);
        	
        	if(currentScreen == null) currentScreen = Minecraft.getMinecraft().currentScreen;
        	
        	if(tooltipPosX + tooltipWidth + this.width > currentScreen.width) {
        		tooltipPosX = positionX + 10 - tooltipWidth - this.width;
        	}
        	if(tooltipPosY + tooltipHeight > currentScreen.height) {
        		tooltipPosY = positionY + 15 - tooltipHeight;
        	}
        	
            this.drawScaledCustomSizeModalRect(tooltipPosX, tooltipPosY, 0, 0, 1, 1, tooltipWidth + this.width, tooltipMH/2, 1, 3);
            this.drawScaledCustomSizeModalRect(tooltipPosX, tooltipPosY+tooltipHeight-(tooltipMH/2), 0, 2, 1, 1, tooltipWidth + this.width, tooltipMH/2, 1, 3);
            this.drawScaledCustomSizeModalRect(tooltipPosX, tooltipPosY+this.tooltipMH/2, 0, 1, 1, 1, tooltipWidth + this.width, tooltipHeight-tooltipMH+1, 1, 3);
            
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.8F);
            mc.getTextureManager().bindTexture(GuiTheme.button);
            this.drawScaledCustomSizeModalRect(tooltipPosX+30, tooltipPosY+25, 0, 0, 1, 1, this.width, exampleHeight, 1, 1);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            
            FontRenderer fontRender = mc.fontRenderer;
            // Draw Example
            for(int i = 0; i < exampleLines.length; i++) {
            	GL11.glPushMatrix();
                int textPosX = tooltipPosX+35;
                int textPosY = tooltipPosY+30+(7*i);
                GL11.glTranslatef(textPosX, textPosY, 0.0F);
                GL11.glScalef(0.7F, 0.7F, 0.7F);
                fontRender.drawString(exampleLines[i],0, 0, GuiTheme.textColor);
            	GL11.glPopMatrix();
            }
            
            // Draw Description
            for(int i = 0; i < descLines.length; i++) {
            	fontRender.drawString(descLines[i],tooltipPosX+30, tooltipPosY+exampleHeight+30+(10*i), GuiTheme.textColor);
            }
            
            GL11.glDepthMask(true);
        	GL11.glEnable(GL11.GL_ALPHA_TEST);
        	GL11.glDisable(GL11.GL_BLEND);
        	this.visible = false;
        }
    }
}
