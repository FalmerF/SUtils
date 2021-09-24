package net.smb.sutils.gui.elements;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ResourceLocation;
import net.smb.sutils.ModuleInfo;
import net.smb.sutils.common.GuiTheme;
import net.smb.sutils.modules.SettingsModule;
import net.smb.sutils.utils.RenderUtils;

public class GuiField extends GuiElement {
	private String text = "";
	public String description = "";
	public String option = "";
	
	public int fieldLimit = 0;
	private int cursorPos = 0;
	private int selectionPos = 0;
	private int timer;
	private int textAnchorX = 0, textAnchorY = 0;
	public int lineSpace = 2;
	public int lineHeight;
	private int maxWidthLine;
	
	private int minWidth = 20, minHeight = 6;
	
	private int cursorLine;
	private int cursorPosInLine;
	private int widthToCursor;
	
	private int firstCharLine;
	private int firstCharPosInLine;
	private int firstPos;
	private int firstSelectedWidth;
	
	private int secondCharLine;
	private int secondCharPosInLine;
	private int secondPos;
	
	private int pressedTimer = 10;
	
	private boolean displayEditPlace;
	private boolean moveToCursor;
	public boolean saved = true;
	
	public boolean selected;
    public boolean multiline;
    
    public int minLines = 2;
	
	private FontRenderer fontRender;
	
	public FieldType fieldType;
	public enum FieldType{
    	STANDART,
    	NUMBERS,
    	PASSWORD,
    }
	
	public GuiField(int id, int posX, int posY, int width, String description, String option, FieldType fieldType){
		super(id, posX, posY, width, 15);
		this.option = option;
        if(!this.option.equals("")) this.text = SettingsModule.getString(this.option);
        this.fieldType = fieldType;
		this.description = description;
		this.fontRender = Minecraft.getMinecraft().fontRenderer;
		this.lineHeight = fontRender.FONT_HEIGHT+this.lineSpace;
	}
	
	public void update() {
		timer--;
		if(timer <= 0) {
			timer = 5;
			displayEditPlace = !displayEditPlace;
		}
		
		if(this.pressed && pressedTimer > 0) pressedTimer--;
	}
	
	public void draw(Minecraft mc, int positionX, int positionY) {
		if(this.visible) {
			this.hovered = positionX >= this.posX && positionY >= this.posY && positionX < this.posX + this.width && positionY < this.posY + this.height;
			
			int color = GuiTheme.fieldTextColor;
        	if(!this.enabled) {
        		GL11.glColor4f(0.5F, 0.5F, 0.5F, 1.0F);
        		color = GuiTheme.fieldDisabledTextColor;
        	}
        	else if(this.selected) {
            	GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            }
            else GL11.glColor4f(0.8F, 0.8F, 0.8F, 1.0F);
			
        	if(this.multiline) {
        		this.height = 10*this.minLines+5;
        	}
        	
			mc.getTextureManager().bindTexture(GuiTheme.fieldBackground);
            Gui.func_152125_a(this.posX, this.posY, 0, 0, 1, 1, this.minWidth, this.minHeight, 3, 3);
            Gui.func_152125_a(this.posX+this.width-this.minWidth, this.posY, 2, 0, 1, 1, this.minWidth, this.minHeight, 3, 3);
            Gui.func_152125_a(this.posX, this.posY+this.height-this.minHeight, 0, 2, 1, 1, this.minWidth, this.minHeight, 3, 3);
            Gui.func_152125_a(this.posX+this.width-this.minWidth, this.posY+this.height-this.minHeight, 2, 2, 1, 1, this.minWidth, this.minHeight, 3, 3);
            
            Gui.func_152125_a(this.posX+this.minWidth, this.posY, 1, 0, 1, 1, this.width-(this.minWidth*2), this.minHeight, 3, 3);
            Gui.func_152125_a(this.posX+this.minWidth, this.posY+this.height-this.minHeight, 1, 2, 1, 1, this.width-(this.minWidth*2), this.minHeight, 3, 3);
            Gui.func_152125_a(this.posX, this.posY+this.minHeight, 0, 1, 1, 1, this.minWidth, this.height-(this.minHeight*2), 3, 3);
            Gui.func_152125_a(this.posX+this.width-this.minWidth, this.posY+this.minHeight, 2, 1, 1, 1, this.minWidth, this.height-(this.minHeight*2), 3, 3);
            
            Gui.func_152125_a(this.posX+this.minWidth, this.posY+this.minHeight, 1, 1, 1, 1, this.width-(this.minWidth*2), this.height-(this.minHeight*2), 3, 3);
			
			boolean scissorPreEnable = ModuleInfo.scissorEnable;
            
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            if(scissorPreEnable) {
	            int sPosX = Math.max(Math.min(this.posX+3, ModuleInfo.sX + ModuleInfo.sW), ModuleInfo.sX);
	            int sPosY = Math.max(Math.min(this.posY+3, ModuleInfo.sY + ModuleInfo.sH), ModuleInfo.sY);
	            int sWidth = Math.max(Math.min(this.width-8, ModuleInfo.sX + ModuleInfo.sW - sPosX), 0);
	            int sHeight = Math.max(Math.min(this.height-6, ModuleInfo.sY + ModuleInfo.sH - sPosY), 0);
	            this.glScissor(sPosX, sPosY, sWidth, sHeight);
            }
            else this.glScissor(this.posX+3, this.posY+3, this.width-8, this.height-6);
			
			String[] lines = getLines(text);
			
			if(moveToCursor && lines.length > 0) {
				try {
				int widthToCursor = fontRender.getStringWidth(lines[cursorLine].substring(0, cursorPosInLine));
				if(this.posY+10+textAnchorY+(lineHeight*cursorLine) > this.height+this.posY-3) {
					textAnchorY = Math.min(textAnchorY-10, 0);
				}
				else if(this.posY+textAnchorY+(lineHeight*cursorLine) < this.posY) {
					textAnchorY = Math.min(textAnchorY+10, 0);
				}
				else if(this.posX+10+widthToCursor+textAnchorX > this.posX+this.width-3 && !this.multiline) {
					textAnchorX = Math.min(textAnchorX-10, 0);
				}
				else if(this.posX+10+widthToCursor+textAnchorX < this.posX+6 && !this.multiline) {
					textAnchorX = Math.min(textAnchorX+10, 0);
				}
				else moveToCursor = false;
				
				if(this.multiline) textAnchorX = 0;
				} catch(Exception e) {widthToCursor = 0;}
			}
			
			if(text.equals("") && !this.selected) {
				fontRender.drawString(description, this.posX+3, this.posY+3, GuiTheme.fieldDisabledTextColor);
			}
			else {
				for(int i = 0; i < lines.length; i++) {
					int linePosY = this.posY+textAnchorY+3+lineHeight*i;
					if(linePosY>this.posY+3-this.lineHeight && linePosY < this.posY+this.height) {
						fontRender.drawString(lines[i].replaceAll(String.valueOf((char)10), ""), this.posX+3+textAnchorX, linePosY, color);
					}
				}
			}
			
			if(this.selected) {
				int currentCharPosX = this.posX+3;
				int currentCharPosY = this.posY+3;
				if(!text.equals("")) {
					currentCharPosX = this.posX+3+widthToCursor+textAnchorX;
					currentCharPosY = this.posY+3+textAnchorY+lineHeight*cursorLine;
				}
				if(displayEditPlace) {
					fontRender.drawString("|", currentCharPosX, currentCharPosY, color);
				}
				
				if(this.pressed && pressedTimer <= 0) {
					pressedTimer = 2;
					setCursorPos(getCharAtPos(positionX, positionY), false);
				}
			
				if(cursorPos != selectionPos) {
					GL11.glColor4f(0F, 0F, 1.0F, 1.0F);
					GL11.glEnable(GL11.GL_COLOR_LOGIC_OP);
			        GL11.glLogicOp(GL11.GL_OR_REVERSE);
					if(firstCharLine == secondCharLine) {
				        RenderUtils.drawRect(firstPos, lineHeight*firstCharLine-1+this.posY+3+textAnchorY, secondPos, lineHeight*(firstCharLine+1)-2+this.posY+3+textAnchorY, GuiTheme.textSelect);
					}
					else {
						for(int i = firstCharLine; i <= secondCharLine; i++) {
							int linePosY = (this.lineHeight*i)+this.textAnchorY+this.posY;
							if(this.posY-10 < linePosY && this.posY+this.height-3 > linePosY) {
								if(i == firstCharLine) {
									RenderUtils.drawRect(firstPos, lineHeight*i-1+this.posY+3+textAnchorY, firstPos+firstSelectedWidth, lineHeight*(i+1)-2+this.posY+3+textAnchorY, GuiTheme.textSelect);
								}
								else if(i == secondCharLine) {
									RenderUtils.drawRect(this.posX+3, lineHeight*i-1+this.posY+3+textAnchorY, secondPos, lineHeight*(i+1)-2+this.posY+3+textAnchorY, GuiTheme.textSelect);
								}
								else {
									RenderUtils.drawRect(this.posX+3, lineHeight*i-1+this.posY+3+textAnchorY, this.posX+3+fontRender.getStringWidth(lines[i]), lineHeight*(i+1)-2+this.posY+3+textAnchorY, GuiTheme.textSelect);
								}
							}
						}
					}
					GL11.glDisable(GL11.GL_COLOR_LOGIC_OP);
				}
				
			}
			
			GL11.glDisable(GL11.GL_SCISSOR_TEST);
            if(scissorPreEnable) {
            	GL11.glEnable(GL11.GL_SCISSOR_TEST);
            	this.glScissor(ModuleInfo.sX, ModuleInfo.sY, ModuleInfo.sW, ModuleInfo.sH);
            }
		}
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
		this.setSelected(false);
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		this.setSelected(false);
	}
	
	public boolean pressed(Minecraft mc, int posX, int posY)
    {
    	if(this.enabled && this.visible && posX >= this.posX && posY >= this.posY && posX < this.posX + this.width && posY < this.posY + this.height) {
    		setSelected(true);
    		pressedTimer = 5;
    		this.pressed = true;
    		
    		setCursorPos(getCharAtPos(posX, posY), true);
    		return true;
    	}
        return false;
    }
	
	public void keyTyped(char key, int keyId) {
		if(selected && this.enabled) {
			if(keyId == Keyboard.KEY_BACK && text.length()>0) delete();
			else if(key == 3) {
				if(selectionPos != cursorPos) GuiScreen.setClipboardString(getSelectedText());
    		}
    		else if(key == 22) addText(GuiScreen.getClipboardString());
    		else if(key == 24) {
    			if(selectionPos != cursorPos) {
					GuiScreen.setClipboardString(getSelectedText());
					delete(true);
				}
    		}
    		else if(keyId == Keyboard.KEY_LEFT) {
    			if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) moveCursor(-1, false);
    			else if(selectionPos != cursorPos) {
    				int pos1 = selectionPos;
    				if(cursorPos < selectionPos) {
    					pos1 = cursorPos;
    				}
    				setCursorPos(pos1, true);
    			}
    			else moveCursor(-1);
    		}
    		else if(keyId == Keyboard.KEY_RIGHT) {
    			if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) moveCursor(1, false);
    			else if(selectionPos != cursorPos) {
    				int pos2 = cursorPos;
    				if(cursorPos < selectionPos) {
    					pos2 = selectionPos;
    				}
    				setCursorPos(pos2, true);
    			}
    			else moveCursor(1);
    		}
    		else if(keyId == Keyboard.KEY_UP && this.multiline) {
    			moveCursorVertical(-1);
    		}
    		else if(keyId == Keyboard.KEY_DOWN && this.multiline) {
    			moveCursorVertical(1);
    		}
    		else if(keyId == Keyboard.KEY_ESCAPE) setSelected(false);
    		else if(keyId == Keyboard.KEY_A && Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
    			this.selectionPos = 0;
    			setCursorPos(text.length(), false);
    		}
    		else if(keyId == Keyboard.KEY_RETURN && this.multiline) {
    			addText(String.valueOf((char)10));
    		}
    		else addText(String.valueOf(key));
    	}
	}
	
	public void addText(String add) {
		add = filerAllowedCharacters(add);
		if(!add.equals("")) {
			saved = false;
			delete(true);
			if(!text.equals("")) {
				String beforeText = text.substring(0, cursorPos);
				String afterText = text.substring(cursorPos, text.length());
				text = beforeText + add + afterText;
			}
			else {
				text = add;
			}
			
			int addedSize = add.length();
			
			if(addedSize != 0) moveCursor(addedSize);
			
			String[] lines = getLines(text);
			maxWidthLine = 0;
			for(String line : lines) {
				int lineWidth = fontRender.getStringWidth(line);
				if(lineWidth > maxWidthLine) maxWidthLine = lineWidth;
			}
		}
	}
	public void delete() {
		delete(false);
	}
	
	public void delete(boolean onlySelection) {
		if(selectionPos != cursorPos) {
			saved = false;
			int pos1 = selectionPos;
			int pos2 = cursorPos;
			if(cursorPos < selectionPos) {
				pos1 = cursorPos;
				pos2 = selectionPos;
			}
			
			String beforeText = text.substring(0, pos1);
			String afterText = text.substring(pos2, text.length());
			
			setCursorPos(pos1, true);
			text = beforeText + afterText;
		}
		else if(!onlySelection) {
			saved = false;
			String beforeText = text.substring(0, cursorPos);
			String afterText = text.substring(cursorPos, text.length());
			if(beforeText.length() > 0) {
				moveCursor(-1);
				beforeText = beforeText.substring(0, beforeText.length()-1);
			}
			text = beforeText + afterText;
		}
	}
	
	public void setSelected(boolean selected) {
    	this.selected = selected;
    	if(selected) {
    	}
    	else {
    		if(!this.option.equals("")) {
    			SettingsModule.setParam(option, this.text);
    		}
    	}
    }
	
	public void moveCursorVertical(int move) {
		String[] lines = getLines(text);
		int cursorLine = getCharLine(lines, cursorPos);
		int cursorNumInLine = getCharNumInLine(lines, cursorPos);
		int widthToCursor = fontRender.getStringWidth(lines[cursorLine].substring(0, cursorNumInLine));
		
		int newLine = Math.max(Math.min(cursorLine+move, lines.length-1), 0);
		int newCursorPos = 0;
		for(int i = 0; i < newLine; i++) newCursorPos += lines[i].length();
		String newLineString =  fontRender.trimStringToWidth(lines[newLine], widthToCursor+2);
		newCursorPos += newLineString.length();
		
		if(newLineString.endsWith(String.valueOf((char)10))) newCursorPos--;
		
		setCursorPos(newCursorPos, true);
	}
	
	public void moveCursor(int move) {
		moveCursor(move, true);
	}
	
	public void moveCursor(int move, boolean moveSelected) {
		setCursorPos(cursorPos+move, moveSelected);
	}
	
	public void setCursorPos(int num, boolean moveSelected) {
		cursorPos = Math.max(Math.min(num, text.length()), 0);
		timer = 5;
		displayEditPlace = true;
		String[] lines = getLines(text);
		if(moveSelected) {
			this.selectionPos = cursorPos;
		}
		else {
			int pos1 = selectionPos;
			int pos2 = cursorPos;
			if(cursorPos < selectionPos) {
				pos1 = cursorPos;
				pos2 = selectionPos;
			}
			
			try {
			firstCharLine = getCharLine(lines, pos1);
			firstCharPosInLine = getCharNumInLine(lines, pos1);
			firstPos = (int)(fontRender.getStringWidth(lines[firstCharLine].substring(0, firstCharPosInLine)))+this.posX+3+textAnchorX;
			firstSelectedWidth = (int)(fontRender.getStringWidth(lines[firstCharLine].substring(firstCharPosInLine, lines[firstCharLine].length())));
			
			secondCharLine = getCharLine(lines, pos2);
			secondCharPosInLine = getCharNumInLine(lines, pos2);
			secondPos = (int)(fontRender.getStringWidth(lines[secondCharLine].substring(0, secondCharPosInLine)))+this.posX+3+textAnchorX;
			} catch(Exception e) {
				firstCharLine = 0;
				firstCharPosInLine = 0;
				firstPos = 0;
				firstSelectedWidth = 0;
				
				secondCharLine = 0;
				secondCharPosInLine = 0;
				secondPos = 0;
				
				cursorLine = 0;
				cursorPosInLine = 0;
				widthToCursor = 0;
				return;
			}
		}
		
		cursorLine = getCharLine(lines, cursorPos);
		cursorPosInLine = getCharNumInLine(lines, cursorPos);
		widthToCursor = fontRender.getStringWidth(lines[cursorLine].substring(0, cursorPosInLine));

		if(this.posY+10+textAnchorY+(lineHeight*cursorLine) > this.height+this.posY-3) {
			this.moveToCursor = true;
		}
		else if(this.posY+textAnchorY+(lineHeight*cursorLine) < this.posY) {
			this.moveToCursor = true;
		}
		
		if(this.posX+10+widthToCursor+textAnchorX > this.posX+this.width-3) {
			this.moveToCursor = true;
		}
		else if(this.posX+10+widthToCursor+textAnchorX < this.posX+3) {
			this.moveToCursor = true;
		}
	}
	
	public int getCharAtPos(int posX, int posY) {
		int needWidth = posX-this.posX-3-textAnchorX;
		posY = posY-this.posY-3-this.textAnchorY;
		int charLine = Math.max(posY/lineHeight, 0);
		String[] lines = getLines(this.text);
		if(charLine > lines.length-1) return this.text.length();
		int numToLine = 0;
		for(int i = 0; i < charLine; i++) {
			numToLine += lines[i].length();
		}
		if(needWidth >= 0) {
			String text = fontRender.trimStringToWidth(lines[charLine], needWidth, false);
			int charPos = numToLine+text.length();
			if(text.endsWith(String.valueOf((char)10))) charPos--;
			return charPos;
		}
		else return numToLine;
	}
	
	public String getSelectedText() {
		int pos1 = selectionPos;
		int pos2 = cursorPos;
		if(cursorPos < selectionPos) {
			pos1 = cursorPos;
			pos2 = selectionPos;
		}
		
		return text.substring(pos1, pos2);
	}
	
	public String[] getLines(String text) {
		if(text != null && !text.equals("")) {
			String[] lines = text.split(String.valueOf((char)10));
			if(this.multiline) {
				String line = "";
				List<String> linesList = new ArrayList<String>();
				for(int i = 0; i < lines.length; i++) {
					if(this.fieldType == FieldType.PASSWORD) lines[i] = lines[i].replaceAll(".", "*");
					lines[i] += (char)10;
					
					line = "";
					String[] words = lines[i].split(" ");
					for(String word : words) {
						if(line.equals("") && fontRender.getStringWidth(line + word) < this.width-6) line += word;
						else if(!line.equals("") && fontRender.getStringWidth(line + " " + word) < this.width-6) line += " " + word;
						else {
							linesList.add(line + " ");
							line = word;
						}
					}
					if(!line.equals("")) linesList.add(line);
				}
				if(linesList.size()> 0)linesList.set(linesList.size()-1, linesList.get(linesList.size()-1) + " ");
				else return new String[] {" "};
				return linesList.toArray(new String[0]);
			}
			else {
				for(int i = 0; i < lines.length; i++) {
					if(this.fieldType == FieldType.PASSWORD) lines[i] = lines[i].replaceAll(".", "*");
					lines[i] += (char)10;
				}
			}
			return lines;
		}
		else {
			return new String[] {""};
		}
	}
	
	public int getCharLine(String[] lines, int num) {
		int t = 0;
		for(int i = 0; i < lines.length; i++) {
			t += lines[i].length();
			if(t > num) return i;
		}
		return Math.max(lines.length-1, 0);
	}
	
	public int getCharNumInLine(String[] lines, int num) {
		int t = 0;
		for(int i = 0; i < lines.length; i++) {
			int lineLength = lines[i].length();
			if(t + lineLength > num) return num-t;
			else t += lineLength;
		}
		if(lines.length > 0) return Math.max(lines[lines.length-1].length(), 0);
		else return 0;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
		this.setCursorPos(0, true);
		this.moveToCursor = false;
	}
	
	public String filerAllowedCharacters(String text)
    {
        StringBuilder var1 = new StringBuilder();
        char[] var2 = text.toCharArray();
        int var3 = var2.length;
        
        if(this.fieldType == FieldType.NUMBERS) {
        	for (int var4 = 0; var4 < var3; ++var4)
            {
                char var5 = var2[var4];

                if ("0123456789".indexOf(var5) != -1)
                {
                    var1.append(var5);
                }
            }
        }
        else {
        	if(this.multiline) {
        		for (int var4 = 0; var4 < var3; ++var4)
    	        {
    	            char var5 = var2[var4];
    	
    	            if (isAllowedCharacterMultiline(var5))
    	            {
    	                var1.append(var5);
    	            }
    	        }
        	}
        	else {
		        for (int var4 = 0; var4 < var3; ++var4)
		        {
		            char var5 = var2[var4];
		
		            if (isAllowedCharacter(var5))
		            {
		                var1.append(var5);
		            }
		        }
        	}
        }

        return var1.toString();
    }
	
	public static boolean isAllowedCharacter(char c)
    {
        return c != 167 && c >= 32 && c != 127;
    }
	public static boolean isAllowedCharacterMultiline(char c)
    {
        return c == 10 || (c != 167 && c >= 32 && c != 127);
    }
}
