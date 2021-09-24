package net.smb.moderutils.gui;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.smb.moderutils.Color;
import net.smb.moderutils.GuiTheme;
import net.smb.moderutils.ModuleUtils;
import net.smb.moderutils.RenderUtil;

public class GuiTextString extends GuiElement {
	public String text;
	public String[] lines;
	public float scale = 1.0F;
	public boolean hihighlighted = false;
	public boolean colorCodes = false;
	public boolean positionedRight, centered, shadow = false;
	public int staticMaxWidth = 0;
	
	private int textPosX = 0, textPosY = 0;
	private int pressCursorPos, cursorPos;
	
	GuiTextString(int posX, int posY, float scale, String text){
		super(-1, posX, posY, 0, 0);
		this.text = text;
		this.scale = scale;
		UpdateLines();
	}
	
	@SuppressWarnings("static-access")
	public void draw(Minecraft mc, int positionX, int positionY) {
		if(this.visible) {
//			if(this.selected && this.pressed) {
//				cursorPos = getCharAtPos(positionX, positionY);
//			}
			
			FontRenderer fontRender = mc.fontRenderer;
			int startTextPosX = this.posX;
			int startTextPosY = this.posY;
			if(hihighlighted) {
				startTextPosX += 5;
				startTextPosY += 5;
				int maxWidth = staticMaxWidth;
				if(maxWidth == 0) {
					for(String line : lines) {
						int lineWidth = fontRender.getStringWidth(line);
						if(lineWidth > maxWidth) maxWidth = lineWidth;
					}
					maxWidth += 10;
				}
				GL11.glColor4f(0.7F, 0.7F, 0.7F, 0.8F);
				mc.getTextureManager().bindTexture(GuiTheme.button);
	            this.drawScaledCustomSizeModalRect(this.posX, this.posY, 0, 0, 1, 1, maxWidth, this.height, 1, 1);
	            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			}
			
			for(int i = 0; i < lines.length; i++) {
				textPosX = startTextPosX;
				if(this.positionedRight) {
					textPosX = startTextPosX - (int)(fontRender.getStringWidth(lines[i])*this.scale);
				}
				else if(this.centered) {
					textPosX = startTextPosX - (int)(fontRender.getStringWidth(lines[i])*this.scale)/2;
				}
	        	GL11.glPushMatrix();
	            textPosY = (int)(startTextPosY+((10*this.scale)*i));
	            GL11.glTranslatef(textPosX, textPosY, 0.0F);
	            GL11.glScalef(scale, scale, scale);
	            if(this.shadow) fontRender.drawStringWithShadow(lines[i], 0, 0, GuiTheme.textColor);
	            else fontRender.drawString(lines[i], 0, 0, GuiTheme.textColor);
	        	GL11.glPopMatrix();
	        }
			
//			if(cursorPos != pressCursorPos && this.selected) {
//				int pos1 = pressCursorPos;
//				int pos2 = cursorPos;
//				if(cursorPos < pressCursorPos) {
//					pos1 = cursorPos;
//					pos2 = pressCursorPos;
//				}
//				
//				int lineHeight = (int)(10*scale);
//				
//				int firstCharLine = getCharLine(pos1);
//				int firstCharPosInLine = getCharNumInLine(pos1);
//				int firstPos = (int)(fontRender.getStringWidth(lines[firstCharLine].substring(0, firstCharPosInLine)))+this.posX;
//				int firstSelectedWidth = (int)(fontRender.getStringWidth(lines[firstCharLine].substring(firstCharPosInLine, lines[firstCharLine].length())));
//				
//				int secondCharLine = getCharLine(pos2);
//				int secondCharPosInLine = getCharNumInLine(pos2);
//				int secondPos = (int)(fontRender.getStringWidth(lines[secondCharLine].substring(0, secondCharPosInLine))*scale)+this.posX;
//				
//				
//				GL11.glEnable(GL11.GL_COLOR_LOGIC_OP);
//		        GL11.glLogicOp(GL11.GL_OR_REVERSE);
//		        GL11.glColor4f(0F, 0F, 1.0F, 1.0F);
//				if(firstCharLine == secondCharLine) {
//			        RenderUtil.drawRect(firstPos, lineHeight*firstCharLine-1+this.posY, secondPos, lineHeight*(firstCharLine+1)-2+this.posY, Color.textSelect);
//				}
//				else {
//					for(int i = firstCharLine; i <= secondCharLine; i++) {
//						int linePosY = (lineHeight*i)+this.posY;
//						if(i == firstCharLine) {
//							RenderUtil.drawRect(firstPos, lineHeight*i-1+this.posY, firstPos+firstSelectedWidth, lineHeight*(i+1)-2+this.posY, Color.textSelect);
//						}
//						else if(i == secondCharLine) {
//							RenderUtil.drawRect(this.posX, lineHeight*i-1+this.posY, secondPos, lineHeight*(i+1)-2+this.posY, Color.textSelect);
//						}
//						else {
//							RenderUtil.drawRect(this.posX, lineHeight*i-1+this.posY, (int)(this.posX+fontRender.getStringWidth(lines[i])), lineHeight*(i+1)-2+this.posY, Color.textSelect);
//						}
//					}
//				}
//				GL11.glDisable(GL11.GL_COLOR_LOGIC_OP);
//			}
		}
	}
	
	public String getMultilineString() {
    	String[] words = this.text.replaceAll("\\\\n", " \n").split(" ");
    	String line = "", lastLine = "";
    	int fieldWidth = this.width;
    	for(String word : words) {
    		int newLineWidth = (int)(Minecraft.getMinecraft().fontRenderer.getStringWidth(lastLine + " " + word)*this.scale);
    		if(newLineWidth > fieldWidth || word.startsWith("\n") || word.endsWith("\n")) {
    			if(!line.equals("")) line += "\n";
    			line += word;
    			lastLine = word;
    		}
    		else {
    			if(line.equals("")) {
    				line += word;
	    			lastLine = word;
    			}
    			else {
	    			line += " " + word;
	    			lastLine += " " + word;
    			}
    		}
    	}
    	
    	return line;
    }
	
	public void UpdateLines() {
		String newText = this.text;
		if(this.width != 0) newText = this.getMultilineString();
		if(colorCodes) this.lines = ModuleUtils.convertAmpCodes(newText).split("(\\n)+");
		else this.lines = newText.split("\\n");
		this.height = (int) (lines.length * (10 * this.scale));
		if(hihighlighted) this.height += 10;
	}
	
	public void SetText(String text) {
		this.text = text;
		this.UpdateLines();
	}
	
	public void setHihighlighted(boolean b) {
		this.hihighlighted = b;
		UpdateLines();
	}
	
	public void setColorFormating(boolean b) {
		this.colorCodes = b;
		UpdateLines();
	}
	
	public void setScale(float scale) {
		this.scale = scale;
		this.UpdateLines();
	}
	
//	public boolean pressed(Minecraft mc, int posX, int posY)
//    {
//    	if(this.enabled && this.visible && posX >= this.posX && posY >= this.posY && posX < this.posX + this.width && posY < this.posY + this.height) {
//    		this.pressed = true;
//    		this.selected = true;
//    		if(mc.currentScreen instanceof GuiScreenModerMenu) ((GuiScreenModerMenu)mc.currentScreen).selectedElement = this;
//    		else if(mc.currentScreen instanceof GuiScreenNotes) ((GuiScreenNotes)mc.currentScreen).selectedElement = this;
//    		else if(mc.currentScreen instanceof GuiScreenRules) ((GuiScreenRules)mc.currentScreen).selectedElement = this;
//    		pressCursorPos = getCharAtPos(posX, posY);
//    		cursorPos = pressCursorPos;
//    		return true;
//    	}
//        return false;
//    }
	
	public int getCharAtPos(int posX, int posY) {
		int needWidth = posX-this.posX;
		posY = posY-this.posY;
		int charLine = (int)(Math.max(posY/(10*scale), 0));
		if(charLine > lines.length-1) return this.text.length();
		int numToLine = 0;
		for(int i = 0; i < charLine; i++) {
			numToLine += lines[i].length();
		}
		if(needWidth >= 0) {
			String text = Minecraft.getMinecraft().fontRenderer.trimStringToWidth(lines[charLine], needWidth, false);
			int charPos = numToLine+text.length();
			if(text.endsWith(String.valueOf((char)10))) charPos--;
			return charPos;
		}
		else return numToLine;
	}
	
	public int getCharLine(int num) {
		int t = 0;
		for(int i = 0; i < lines.length; i++) {
			t += lines[i].length();
			if(t > num) return i;
		}
		return Math.max(lines.length-1, 0);
	}
	
	public int getCharNumInLine(int num) {
		int t = 0;
		for(int i = 0; i < lines.length; i++) {
			int lineLength = lines[i].length();
			if(t + lineLength > num) return num-t;
			else t += lineLength;
		}
		return Math.max(lines[lines.length-1].length(), 0);
	}
	
	public void keyTyped(char key, int keyId) {
		if(selected && this.enabled) {
			if(key == 3) {
				if(pressCursorPos != cursorPos) GuiScreen.setClipboardString(getSelectedText());
    		}
    	}
	}
	
	public String getSelectedText() {
		int pos1 = pressCursorPos;
		int pos2 = cursorPos;
		if(cursorPos < pressCursorPos) {
			pos1 = cursorPos;
			pos2 = pressCursorPos;
		}
		
		return text.substring(pos1, pos2);
	}
}
