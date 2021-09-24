package net.smb.sutils.gui.elements;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.smb.sutils.common.GuiTheme;
import net.smb.sutils.gui.GuiScreenNotes;
import net.smb.sutils.modules.SettingsModule;

public class GuiNote extends GuiElement {
    public int minHeight = 64/2, minWidth = 110/2+1;
    public int noteId = 0;
    public int needHeight;
    
    public String title;
    public String description;
    public boolean canEdit;
    
    public boolean editing = false;
    public boolean pined = false;
    public boolean background = true;
    
    public GuiTextString descriptionElement;
    public GuiTextString titleElement;
    
    public GuiField titleField;
    public GuiField descriptionField;
    
    List<GuiElement> elements = new ArrayList<GuiElement>();

    public GuiNote(int noteId, int posX, int posY, String title, String description, boolean canEdit)
    {
    	super(-1, posX, posY, 170, 30);
    	this.needHeight = 100;
    	this.noteId = noteId;
        this.title = title;
        this.description = description;
        this.canEdit = canEdit;
        
        titleElement = new GuiTextString(13, 15, 1.0F, title);
        titleElement.width = 140;
        titleElement.setColorFormating(true);
        this.elements.add(titleElement);
        
        descriptionElement = new GuiTextString(13, 27, 0.8F, description);
        descriptionElement.width = 140;
        descriptionElement.setColorFormating(true);
        this.elements.add(descriptionElement);
        
        this.pined = SettingsModule.getBool("notePined" + this.noteId);
        this.background = SettingsModule.getBool("noteBackground" + this.noteId);
        
        if(this.canEdit) {
	        GuiButtonImage btnImage = new GuiButtonImage(0, 150, 12, 6, 7, new ResourceLocation("moderutils", "textures/rubbish.png"));
	        btnImage.setColor(1.0F, 0.0F, 0.0F);
	        this.elements.add(btnImage);
	        
	        btnImage = new GuiButtonImage(1, 135, 12, 7, 7, new ResourceLocation("moderutils", "textures/pencil.png"));
	        btnImage.setColor(1.0F, 1.0F, 0.0F);
	        this.elements.add(btnImage);
	        
	        btnImage = new GuiButtonImage(2, 125, 12, 5, 8, new ResourceLocation("moderutils", "textures/pin.png"));
	        btnImage.setColor(1.0F, 1.0F, 0.0F);
	        this.SetButtonColor(btnImage, this.pined);
	        this.elements.add(btnImage);
	        
	        btnImage = new GuiButtonImage(3, 115, 12, 7, 7, new ResourceLocation("moderutils", "textures/layers.png"));
	        btnImage.setColor(1.0F, 1.0F, 0.0F);
	        this.SetButtonColor(btnImage, this.background);
	        this.elements.add(btnImage);
	        
	        titleField = new GuiField(4, 13, 30, 146, "Заголовок...", "noteTitle" + this.noteId, GuiField.FieldType.STANDART);
	        titleField.visible = false;
	        titleField.multiline = true;
	        titleField.fieldLimit = 50;
	        this.elements.add(titleField);
	        
	        descriptionField = new GuiField(5, 13, 50, 146, "Описание...", "noteDesc" + this.noteId, GuiField.FieldType.STANDART);
	        descriptionField.visible = false;
	        descriptionField.multiline = true;
	        descriptionField.minLines = 3;
	        this.elements.add(descriptionField);
        }
        
        SettingsModule.setParam("notePosX" + this.noteId, this.posX);
        SettingsModule.setParam("notePosY" + this.noteId, this.posY);
        SettingsModule.setParam("noteTitle" + this.noteId, this.title);
        SettingsModule.setParam("noteDesc" + this.noteId, this.description);

        setPos();
    }
    
    public void update() {
		for(GuiElement element : elements) element.update();
	}
    
    @SuppressWarnings("static-access")
	public void draw(Minecraft mc, int positionX, int positionY)
    {
        if (this.visible)
        {
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDepthMask(false);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            
            if(this.background) {
	            this.needHeight = 35;
	            if(this.editing) {
	            	this.needHeight += this.titleField.height;
	            	this.needHeight += this.descriptionField.height;
	            	this.needHeight += 7;
	            	
	            	this.descriptionField.posY = this.titleField.posY + this.titleField.height + 5;
	            	this.descriptionField.relPosY = this.posY + this.titleField.relPosY + this.titleField.height + 5;
	            }
	            else {
		            this.needHeight += titleElement.height;
		            this.needHeight += descriptionElement.height;
		            
		            if(this.canEdit) {
		            	titleElement.relPosY = 30;
		        		titleElement.posY = this.posY+30;
		        		
		        		descriptionElement.posY = titleElement.posY + titleElement.height + 2;
		        		descriptionElement.relPosY = this.posY + titleElement.relPosY + titleElement.height + 2;
		        	}
		        	else {
		        		titleElement.relPosY = 15;
		        		titleElement.posY = this.posY+15;
		        		
		        		descriptionElement.posY = titleElement.posY + titleElement.height + 2;
		        		descriptionElement.relPosY = this.posY + titleElement.relPosY + titleElement.height + 2;
		        	}
	            }
	            if(this.canEdit) this.needHeight += 10;
            }	
            else {
            	this.needHeight = 30;
            	
            	if(this.canEdit) {
	            	titleElement.relPosY = 30;
	        		titleElement.posY = this.posY+30;
	        		
	        		descriptionElement.posY = titleElement.posY + titleElement.height + 2;
	        		descriptionElement.relPosY = this.posY + titleElement.relPosY + titleElement.height + 2;
	        	}
	        	else {
	        		titleElement.relPosY = 15;
	        		titleElement.posY = this.posY+15;
	        		
	        		descriptionElement.posY = titleElement.posY + titleElement.height + 2;
	        		descriptionElement.relPosY = this.posY + titleElement.relPosY + titleElement.height + 2;
	        	}
            }
            
            this.height = (int)this.lerp(this.height, this.needHeight, 0.4F);
            
            // Draw Background
            if(this.canEdit || this.background) {
	            mc.getTextureManager().bindTexture(GuiTheme.functionBackground);
	            this.func_152125_a(this.posX, this.posY, 0, 0, 1, 1, this.minWidth/2, this.minHeight/2, 3, 3);
	            this.func_152125_a(this.posX+this.width-this.minWidth/2, this.posY, 2, 0, 1, 1, this.minWidth/2, this.minHeight/2, 3, 3);
	            this.func_152125_a(this.posX+this.width-this.minWidth/2, this.posY+this.height-this.minHeight/2, 2, 2, 1, 1, this.minWidth/2, this.minHeight/2, 3, 3);
	            this.func_152125_a(this.posX, this.posY+this.height-this.minHeight/2, 0, 2, 1, 1, this.minWidth/2, this.minHeight/2, 3, 3);
	            
	            this.func_152125_a(this.posX, this.posY+this.minHeight/2, 0, 1, 1, 1, this.minWidth/2, this.height-this.minHeight, 3, 3);
	            this.func_152125_a(this.posX+this.width-this.minWidth/2, this.posY+this.minHeight/2, 2, 1, 1, 1, this.minWidth/2, this.height-this.minHeight, 3, 3);
	            this.func_152125_a(this.posX+this.minWidth/2, this.posY, 1, 0, 1, 1, this.width-this.minWidth, this.minHeight/2, 3, 3);
	            this.func_152125_a(this.posX+this.minWidth/2, this.posY+this.height-this.minHeight/2, 1, 2, 1, 1, this.width-this.minWidth, this.minHeight/2, 3, 3);
	            
	            this.func_152125_a(this.posX+this.minWidth/2, this.posY+this.minHeight/2, 1, 1, 1, 1, this.width-this.minWidth, this.height-this.minHeight, 3, 3);
            }
            
            GL11.glDepthMask(true);
        	GL11.glEnable(GL11.GL_ALPHA_TEST);
        }
        for(GuiElement element : this.elements) {
    		element.draw(mc, positionX, positionY);
        }
    }
    
    public void setEnable(boolean enabled) {
    	this.enabled = enabled;
    	for(GuiElement element : this.elements) {
    		element.enabled = enabled;
    		if(element instanceof GuiScrollView) {
        		((GuiScrollView)element).setEnable(visible);
        	}
        }
    }
    
    public void setVisible(boolean visible) {
    	this.visible = visible;
    	for(GuiElement element : this.elements) {
    		element.visible = visible;
    		if(element instanceof GuiScrollView) {
        		((GuiScrollView)element).setVisible(visible);
        	}
        }
    }
    
    public void setPos() {
    	for(GuiElement element : this.elements) {
    		element.posX = element.relPosX + this.posX;
    		element.posY = element.relPosY + this.posY;
    		if(element instanceof GuiToggle) {
        		((GuiToggle)element).SetNeedPos();
        	}
    		else if(element instanceof GuiScrollView) ((GuiScrollView)element).setPos();
        }
    }
    
    public GuiElement getById(int id) {
    	for(GuiElement element : this.elements) {
    		if(element.id == id) return element;
    		else if(element instanceof GuiScrollView) {
    			GuiElement e = ((GuiScrollView)element).getById(id);
    			if(e != null) return e;
    		}
        }
    	return null;
    }
    
    public boolean ClickElement(int posX, int posY, int mouseButton) {
    	for(GuiElement element : elements) {
    		if(element.pressed(Minecraft.getMinecraft(), posX, posY)) {
    			if(mouseButton == 0) {
	    			element.playPressSound(Minecraft.getMinecraft().getSoundHandler());
	    			element.clicked();
	    			this.actionPerfomed(element);
	    			if(Minecraft.getMinecraft().currentScreen instanceof GuiScreenNotes)((GuiScreenNotes)Minecraft.getMinecraft().currentScreen).pressedElement = element;
	    			return true;
				}
			}
		}
    	return false;
    }
    
    public void actionPerfomed(GuiElement element) {
    	Minecraft mc = Minecraft.getMinecraft();
    	switch(element.id) {
			case 0:
				if(mc.currentScreen != null && (mc.currentScreen instanceof GuiScreenNotes)) {
					SettingsModule.setParam("noteTitle" + this.noteId, "");
					SettingsModule.setParam("noteDesc" + this.noteId, "");
					((GuiScreenNotes)mc.currentScreen).noteCount--;
					((GuiScreenNotes)mc.currentScreen).guiElements.remove(this);
				}
				break;
			case 1:
				this.editing = !this.editing;
				this.SetButtonColor((GuiButtonImage) element, this.editing);
				
				this.titleElement.visible = !editing;
				this.descriptionElement.visible = !editing;
				
				this.titleField.visible = editing;
				this.descriptionField.visible = editing;
				
				if(editing) {
					this.titleField.setText(this.title);
					this.descriptionField.setText(this.description);;
				}
				else {
					this.title = this.titleField.getText();
					this.description = this.descriptionField.getText();
					
					this.titleElement.SetText(this.title);
					this.descriptionElement.SetText(this.description);
				}
				break;
			case 2:
				this.pined = !this.pined;
				this.SetButtonColor((GuiButtonImage) element, this.pined);
				
				SettingsModule.setParam("notePined" + this.noteId, this.pined);
				break;
			case 3:
				this.background = !this.background;
				this.SetButtonColor((GuiButtonImage) element, this.background);
				
				SettingsModule.setParam("noteBackground" + this.noteId, this.background);
				break;
			case 4:
				if(mc.currentScreen != null && (mc.currentScreen instanceof GuiScreenNotes)) {
					((GuiScreenNotes)mc.currentScreen).selectedElement = element;
					((GuiField)element).setSelected(true);
				}
				break;
			case 5:
				if(mc.currentScreen != null && (mc.currentScreen instanceof GuiScreenNotes)) {
					((GuiScreenNotes)mc.currentScreen).selectedElement = element;
					((GuiField)element).setSelected(true);
				}
				break;
    	}
    }
    
    public void SetButtonColor(GuiButtonImage btn, boolean active) {
    	if(active) btn.setColorStandart(0.22F, 0.64F, 0.94F);
    	else btn.setColorStandart(1.0F, 1.0F, 1.0F);
    }
    
    public int getHeight() {
    	int currentHeight = 35;
    	if(this.editing) {
    		currentHeight += this.titleField.height;
    		currentHeight += this.descriptionField.height;
    		currentHeight += 7;
    	}
    	else {
    		currentHeight += titleElement.height;
    		currentHeight += descriptionElement.height;
    	}
    	if(this.canEdit) currentHeight += 10;
    	//return this.height;
    	return currentHeight;
    }
    
    public boolean pressed(Minecraft mc, int posX, int posY)
    {
    	if(this.enabled && this.visible && posX >= this.posX && posY >= this.posY && posX < this.posX + this.width && posY < this.posY + this.getHeight()) {
    		return true;
    	}
        return false;
    }
}
