package net.smb.sutils.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import net.eq2online.macros.compatibility.Reflection;
import net.eq2online.macros.scripting.ItemID;
import net.eq2online.macros.scripting.actions.ScriptActionAchievementGet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.StatBase;
import net.minecraft.util.ChatComponentText;
import net.smb.sutils.gui.elements.GuiButtonMenu;
import net.smb.sutils.gui.elements.GuiElement;
import net.smb.sutils.gui.elements.GuiFunction;
import net.smb.sutils.gui.elements.GuiHint;
import net.smb.sutils.gui.elements.GuiNote;
import net.smb.sutils.gui.elements.GuiScrollView;
import net.smb.sutils.modules.SettingsModule;

public class GuiScreenNotes extends GuiScreen {
    public List<GuiElement> guiElements = new ArrayList<GuiElement>();
    private final Minecraft mc;

    public GuiElement selectedElement;
    public GuiElement pressedElement;
    public GuiElement moveElement;
    
    public int relPosX, relPosY;
    
    public int noteCount = 0;
    
    public List<GuiHint> hints = new ArrayList<GuiHint>();
    public int currentHint = 0;
    
    public GuiScreenNotes(Minecraft mc)
    {
        this.mc = mc;
    }

	@SuppressWarnings("unchecked")
	public void initGui() // initGui
    {
		//this.posX = (this.field_146294_l/2)-(this.width/2);
		//this.posY = (this.field_146295_m/2)-(this.height/2);
		
		currentHint = SettingsModule.getControlKey("trainingHint");
		
        GuiHint hint = new GuiHint(2, this.width/2-50, this.height-65, 100, 40, "Добавить заметку\nВы можете нажав\nна эту кнопку.");
        hint.setRegion(this.width/2-55, this.height-25, this.width/2+55, this.height-5);
        this.hints.add(hint);
        
        hint = new GuiHint(3, this.width/2-76, this.height/2-20, 152, 40, "Вот и подошёл к концу\nнаш гайд, теперь Вы умеете\nпользоваться SUtils!");
        this.hints.add(hint);
		
		this.guiElements.clear();
		GuiButtonMenu btn = new GuiButtonMenu(0, 15, this.height-25, 110, 20, "Закрыть", "textures/button_close.png");
		btn.imgHeightScale = 0.8F;
		this.guiElements.add(btn);
		btn = new GuiButtonMenu(1, this.width/2-55, this.height-25, 110, 20, "Добавить", "textures/plus.png");
		btn.imgHeightScale = 0.8F;
		this.guiElements.add(btn);
		
		for(int i = 1; i < 11; i++) {
			String title = SettingsModule.getString("noteTitle" + i);
			String desc = SettingsModule.getString("noteDesc" + i);
			if(!title.equals("") || !desc.equals("")) {
				this.guiElements.add(new GuiNote(i, SettingsModule.getInt("notePosX" + i), SettingsModule.getInt("notePosY" + i), title, desc, true));
				noteCount++;
			}
		}
        
		if(currentHint-9 < this.hints.size() && currentHint-9 >= 0) this.hints.get(currentHint-9).setVisible(true);
		
		Keyboard.enableRepeatEvents(true);
    }
	
	public void nextHint(GuiHint h) {
		this.currentHint++;
		h.setVisible(false);
		if(currentHint-9 < this.hints.size() && currentHint-9 >= 0) this.hints.get(currentHint-9).setVisible(true);
		SettingsModule.setControlKey("trainingHint", currentHint);
	}
	
	public void updateScreen() {
		super.updateScreen();
		for(GuiElement element : guiElements) element.update();
		for(GuiHint h : hints) h.update();
	}
	
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}
	
	protected void actionPerfomed(GuiElement element) {
		switch(element.id) {
			case 0:
				this.mc.displayGuiScreen((GuiScreen)null);
                this.mc.setIngameFocus();
				break;
			case 1:
				if(noteCount < 10) {
					for(int i = 1; i < 11; i++) {
						String title = SettingsModule.getString("noteTitle" + i);
						String desc = SettingsModule.getString("noteDesc" + i);
						if(title.equals("") && desc.equals("")) {
							SettingsModule.setParam("notePined" + i, false);
							SettingsModule.setParam("noteBackground" + i, true);
							this.guiElements.add(new GuiNote(i, this.width/2-85, this.height/2-30, "Заголовок", "Описание", true));
							noteCount++;
							break;
						}
					}
				}
				break;
			case 2:
			{
				nextHint((GuiHint)element);
				break;
			}
			case 3:
			{
				nextHint((GuiHint)element);
				
				try {
					Achievement c = new Achievement("fake", "fake", 0, 0, new ItemStack(Item.getItemById(386), 1, 0), null);
					Reflection.setPrivateValue(StatBase.class, c, "field_75978_a", new ChatComponentText("SUtils головного мозга"));
	                Minecraft.getMinecraft().guiAchievement.func_146256_a(c);
				} catch(Exception e) {}
				
				this.mc.displayGuiScreen((GuiScreen)null);
	            this.mc.setIngameFocus();
				break;
			}
		}
	}
	
	@SuppressWarnings("static-access")
	public void drawScreen(int posX, int posY, float partialTicks) // Draw Screen
    {
        this.drawDefaultBackground();
        
        GL11.glEnable(GL11.GL_BLEND);
    	GL11.glDepthMask(true);
    	GL11.glEnable(GL11.GL_ALPHA_TEST);
        
    	for(GuiElement element : guiElements) {
    		element.draw(mc, posX, posY);
    	}
    	for(GuiHint h : hints) h.draw(mc, posX, posY);
    	
    	if(moveElement != null) {
    		if(moveElement instanceof GuiNote) {
    			GuiNote note = (GuiNote)moveElement;
    			note.setPos();
        		moveElement.posX = Math.max(Math.min(posX-this.relPosX, this.width-moveElement.width+5), -10);
        		moveElement.posY = Math.max(Math.min(posY-this.relPosY, this.height-note.getHeight()+5), -10);
    		}
    	}
    }
	
	protected void keyTyped(char key, int keyId)
    {
		if(this.selectedElement != null) {
			if(keyId == 1) {
				this.selectedElement.setSelected(false);
        		this.selectedElement = null;
			}
			else {
				this.selectedElement.keyTyped(key, keyId);
			}
		}
		if (keyId == 1)
        {
			this.mc.displayGuiScreen((GuiScreen)null);
            this.mc.setIngameFocus();
        }
    }
	
    public void handleInput()
    {
        if (Mouse.isCreated())
        {
            while (Mouse.next())
            {
                this.handleMouseInput();
            }
        }

        if (Keyboard.isCreated())
        {
            while (Keyboard.next())
            {
                this.handleKeyboardInput();
            }
        }
        super.handleInput();
    }
    
    public void handleMouseInput()
    {
        int var1 = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int var2 = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
        int var3 = Mouse.getEventButton();
        int var4 = Math.max(Math.min(Mouse.getEventDWheel(),1),-1);

        
        if (Mouse.getEventButtonState())
        {
            this.mouseClicked(var1, var2, var3);
        }
        else if (var3 != -1)
        {
            this.mouseMovedOrUp(var1, var2, var3);
        }
    }
    
    public void handleKeyboardInput()
    {
        if (Keyboard.getEventKeyState())
        {
            this.keyTyped(Keyboard.getEventCharacter(), Keyboard.getEventKey());
        }

        this.mc.func_152348_aa();
    }
    
	protected void mouseClicked(int posX, int posY, int mouseButton)
    {
        if (mouseButton == 0)
        {
        	if(this.selectedElement != null) {
        		this.selectedElement.setSelected(false);
	        	this.selectedElement = null;
        	}
        	moveElement = null;
        	this.CheckPressElements(this.guiElements, posX, posY, mouseButton);
        }
    }
	
	protected void mouseMovedOrUp(int posX, int posY, int button) {
		if (this.pressedElement != null && button == 0)
        {
            this.pressedElement.released(posX, posY);
            this.pressedElement = null;
        }
		if(this.moveElement != null) {
			if(moveElement instanceof GuiNote) {
				SettingsModule.setParam("notePosX" + ((GuiNote)moveElement).noteId, moveElement.posX);
				SettingsModule.setParam("notePosY" + ((GuiNote)moveElement).noteId, moveElement.posY);
			}
			this.moveElement = null;
		}
	}
	
	public void CheckPressElements(List<GuiElement> elements, int posX, int posY, int mouseButton) {
		try {
			for(GuiHint h : hints) {
				if(h.pressed(mc, posX, posY)) {
					this.actionPerfomed(h);
					return;
				}
			}
			
			for(GuiElement element : elements) {
	    		if(element.pressed(mc, posX, posY)) {
	    			if(mouseButton == 0) {
	    				if(element instanceof GuiNote) {
	    					if(!((GuiNote)element).ClickElement(posX, posY, mouseButton)) {
		    					this.relPosX = posX - element.posX;
		    					this.relPosY = posY - element.posY;
		    					this.moveElement = element;
	    					}
	    				}
	    				else {
			    			element.playPressSound(this.mc.getSoundHandler());
			    			element.clicked();
			    			if(this.pressedElement == null) {
				    			this.pressedElement = element;
				    			this.pressedElement.pressed = true;
			    			}
			    			this.actionPerfomed(element);
			    			if(element.enabled && element.visible) {
			    	        	this.selectedElement = element;
			    	        	this.selectedElement.setSelected(true);
			    			}
	    				}
	    			}
	    		}
				if(element instanceof GuiFunction) {
					this.CheckPressElements(((GuiFunction)element).elements, posX, posY, mouseButton);
				}
				else if(element instanceof GuiScrollView) {
					this.CheckPressElements(((GuiScrollView)element).elements, posX, posY, mouseButton);
				}
	    	}
		} catch(Exception e) {}
	}
}
