package net.smb.moderutils.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.FrameType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.toasts.AdvancementToast;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.smb.moderutils.ModuleSettings;

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
	public void initGui()
    {
		//this.posX = (this.field_146294_l/2)-(this.width/2);
		//this.posY = (this.field_146295_m/2)-(this.height/2);
		
		currentHint = ModuleSettings.getControlKey("trainingHint");
		
        GuiHint hint = new GuiHint(2, this.width/2-50, this.height-65, 100, 40, "§fДобавить заметку\n§fВы можете нажав\n§fна эту кнопку.");
        hint.setRegion(this.width/2-55, this.height-25, this.width/2+55, this.height-5);
        this.hints.add(hint);
        
        hint = new GuiHint(3, this.width/2-76, this.height/2-20, 152, 40, "§fВот и подошёл к концу\n§fнаш гайд, теперь Вы умеете\n§fпользоваться SUtils!");
        this.hints.add(hint);
		
		this.guiElements.clear();
		GuiButtonMenu btn = new GuiButtonMenu(0, 15, this.height-25, 110, 20, "Закрыть", "textures/button_close.png");
		btn.imgHeightScale = 0.8F;
		this.guiElements.add(btn);
		btn = new GuiButtonMenu(1, this.width/2-55, this.height-25, 110, 20, "Добавить", "textures/plus.png");
		btn.imgHeightScale = 0.8F;
		this.guiElements.add(btn);
		
		for(int i = 1; i < 11; i++) {
			String title = ModuleSettings.getString("noteTitle" + i);
			String desc = ModuleSettings.getString("noteDesc" + i);
			if(!title.equals("") || !desc.equals("")) {
				this.guiElements.add(new GuiNote(i, ModuleSettings.getInt("notePosX" + i), ModuleSettings.getInt("notePosY" + i), title, desc, true));
				noteCount++;
			}
		}
        
		Keyboard.enableRepeatEvents(true);
		
		if(currentHint-9 < this.hints.size()) this.hints.get(currentHint-9).setVisible(true);
    }
	
	public void nextHint(GuiHint h) {
		this.currentHint++;
		h.setVisible(false);
		if(currentHint-9 < this.hints.size()) this.hints.get(currentHint-9).setVisible(true);
		ModuleSettings.setControlKey("trainingHint", currentHint);
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
						String title = ModuleSettings.getString("noteTitle" + i);
						String desc = ModuleSettings.getString("noteDesc" + i);
						if(title.equals("") && desc.equals("")) {
							ModuleSettings.setParam("notePined" + i, false);
							ModuleSettings.setParam("noteBackground" + i, true);
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
					final DisplayInfo displayInfo = new DisplayInfo(new ItemStack(Item.getItemById(386), 1, 0), new TextComponentString("SUtils головного мозга"), new TextComponentString("Пройти обучение."), (ResourceLocation)null, FrameType.TASK, true, false, false);
			        final Advancement advancement = new Advancement(new ResourceLocation("macros:fake"), (Advancement)null, displayInfo, (AdvancementRewards)null, (Map<String, Criterion>)new HashMap<String, Criterion>(), (String[][])null);
			        final AdvancementToast toast = new AdvancementToast(advancement);
	                Minecraft.getMinecraft().getToastGui().add(toast);
				} catch(Exception e) {}
				
				this.mc.displayGuiScreen((GuiScreen)null);
	            this.mc.setIngameFocus();
				break;
			}
		}
	}
	
	@SuppressWarnings("static-access")
	public void drawScreen(int posX, int posY, float partialTicks)
    {
        this.drawDefaultBackground();
        
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
        try {
			super.handleInput();
		} catch (IOException e) {
			e.printStackTrace();
		}
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

        this.mc.dispatchKeypresses();
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
				ModuleSettings.setParam("notePosX" + ((GuiNote)moveElement).noteId, moveElement.posX);
				ModuleSettings.setParam("notePosY" + ((GuiNote)moveElement).noteId, moveElement.posY);
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
