package net.smb.sutils.gui;

import java.awt.Desktop;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.smb.sutils.common.GuiTheme;
import net.smb.sutils.gui.elements.GuiButtonImage;
import net.smb.sutils.gui.elements.GuiButtonMenu;
import net.smb.sutils.gui.elements.GuiButtonStandart;
import net.smb.sutils.gui.elements.GuiElement;
import net.smb.sutils.gui.elements.GuiField;
import net.smb.sutils.gui.elements.GuiFunction;
import net.smb.sutils.gui.elements.GuiScrollView;
import net.smb.sutils.gui.elements.GuiTextString;
import net.smb.sutils.utils.Utils;

public class GuiScreenRules extends GuiScreen {
    public List<GuiElement> guiElements = new ArrayList<GuiElement>();
    private final Minecraft mc;
    
    private int posX;
    private int posY;
    private int windowWidth = 350;
    private int windowHeight = 350;
    
    private int minHeight = 64, minWidth = 110;
    
    private GuiScrollView rulesMain, rulesEco, rulesModer;
    private GuiScrollView selectedCategory = null;
    private GuiButtonMenu selectedCategoryButton;
    public GuiElement selectedElement;
    public GuiElement pressedElement;
    
    public GuiField searchField;
    
    public GuiScreenRules(Minecraft mc)
    {
        this.mc = mc;
    }

	@SuppressWarnings("unchecked")
	public void initGui()
    {
		this.posX = (this.width/2)-(windowWidth/2);
		this.posY = (this.height/2)-(windowHeight/2);
	
		this.guiElements.clear();
		GuiButtonImage btnImage = new GuiButtonImage(0, 325, 21, 7, 7, new ResourceLocation("moderutils", "textures/button_close.png"));
        btnImage.setColor(1.0F, 0.0F, 0.0F);
        this.guiElements.add(btnImage);
		
        GuiButtonMenu btn = new GuiButtonMenu(1, 20, 25, 100, 20, "Основные", "textures/scroll.png");
        btn.selected = true;
        selectedCategoryButton = btn;
        this.guiElements.add(btn);
        btn = new GuiButtonMenu(2, 120, 25, 100, 20, "Экономика", "textures/money_icon.png");
        btn.imgWidthScale = 0.85F;
        this.guiElements.add(btn);
        this.guiElements.add(new GuiButtonMenu(3, 220, 25, 100, 20, "Модераторы", "textures/player_icon.png"));
        
        searchField = new GuiField(-1, 220, 55, 100, "Поиск...", "", GuiField.FieldType.STANDART);
        this.guiElements.add(searchField);
        
        rulesMain = new GuiScrollView(-1, 15, 70, 310, 260);
        
        GuiFunction func = new GuiFunction(-1, 0, -10, 70, "Ссылки");
		func.width = 310;
		func.elements.add(new GuiButtonStandart(4, 20, 35, 70, 13, "Правила"));
		func.elements.add(new GuiButtonStandart(5, 100, 35, 100, 13, "Спорные выражения"));
		func.elements.add(new GuiButtonStandart(6, 210, 35, 70, 13, "Ограничения"));
		
		rulesMain.elements.add(func);
        
        addElementsByText(rulesMain, Utils.getDataString("MainRules"), 40);
        selectedCategory = rulesMain;
        this.guiElements.add(rulesMain);
        
        rulesEco = new GuiScrollView(-1, 15, 70, 310, 260);
        
        func = new GuiFunction(-1, 0, -10, 70, "Ссылки");
		func.width = 310;
		func.elements.add(new GuiButtonStandart(7, 20, 35, 70, 13, "Экономика"));
        
		rulesEco.elements.add(func);
		
		addElementsByText(rulesEco, Utils.getDataString("EcoRules"), 40);
		
        rulesEco.setVisible(false);
        this.guiElements.add(rulesEco);
        
        rulesModer = new GuiScrollView(-1, 15, 70, 310, 260);
        
        addElementsByText(rulesModer, Utils.getDataString("ModerRules"), -10);
        
        rulesModer.setVisible(false);
        this.guiElements.add(rulesModer);
        
		Keyboard.enableRepeatEvents(true);
		this.setPosAll();
    }
	
	public void addElementsByText(GuiScrollView scroll, String text, int lastPosY) {
		String[] functions = text.split("function:");
		int functionPosY = lastPosY;
		for(String function : functions) {
			try {
				String[] textArray = function.split("/");
				String title = Utils.convertAmpCodes(replaceFormatingText(textArray[0]));
				String description = Utils.convertAmpCodes(replaceFormatingText(textArray[1]));
				String hDescription = "";
				if(textArray.length >= 3) hDescription = Utils.convertAmpCodes(replaceFormatingText(textArray[2]));
				
				int funcHeight = 65;
				
				GuiTextString descriptionText = new GuiTextString(20, 35, 0.8F, description);
				GuiTextString hDescriptionText = null;
				if(!hDescription.equals("")) {
					hDescriptionText = new GuiTextString(20, 45 + descriptionText.height, 0.8F, hDescription);
					hDescriptionText.setHihighlighted(true);
					hDescriptionText.staticMaxWidth = 270;
					
					funcHeight += descriptionText.height + hDescriptionText.height;
				}
				else funcHeight += descriptionText.height;
				
				GuiFunction func = new GuiFunction(-1, 0, functionPosY, funcHeight, title);
				func.width = 310;
				func.elements.add(descriptionText);
				if(hDescriptionText != null) func.elements.add(hDescriptionText);
				
				scroll.elements.add(func);
				
				functionPosY += funcHeight - 15;
			}
			catch(Exception e) {}
		}
	}
	
	public String replaceFormatingText(String text) {
		return text.replaceAll("\\\\n", "\n");
	}
	
	public void updateScreen() {
		super.updateScreen();
		for(GuiElement element : guiElements) element.update();
	}
	
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}
	
	public void filterBySearch(String search) {
		search = search.toLowerCase();
		filterScrollBySearch(rulesMain, search);
		filterScrollBySearch(rulesEco, search);
		filterScrollBySearch(rulesModer, search);
		setPosAll();
	}
	
	public void filterScrollBySearch(GuiScrollView scroll, String search) {
		int fPosY = -10;
		for(GuiElement element : scroll.elements) {
			if(element instanceof GuiFunction) {
				GuiFunction func = (GuiFunction)element;
				boolean hasSearch = false;
				if(search.equals("")) hasSearch = true;
				else if(func.displayString.toLowerCase().contains(search)) hasSearch = true;
				else {
					for(GuiElement e : func.elements) {
						if(e instanceof GuiTextString && ((GuiTextString)e).text.toLowerCase().contains(search)) {
							hasSearch = true;
							break;
						}
					}
				}
				
				if(hasSearch) {
					func.visible = true;
					func.relPosY = fPosY;
					fPosY += func.height-15;
				}
				else {
					func.visible = false;
				}
			}
		}
	}
	
	protected void actionPerfomed(GuiElement element) {
		switch(element.id) {
			case 0:
				this.mc.displayGuiScreen(new GuiScreenModerMenu(mc));
				break;
			case 1:
				SetCategory((GuiButtonMenu)element, this.rulesMain);
				this.filterBySearch(searchField.getText());
				break;
			case 2:
				SetCategory((GuiButtonMenu)element, this.rulesEco);
				this.filterBySearch(searchField.getText());
				break;
			case 3:
				SetCategory((GuiButtonMenu)element, this.rulesModer);
				this.filterBySearch(searchField.getText());
				break;
			case 4:
				try {
					Desktop.getDesktop().browse(new URI("https://simpleminecraft.ru/rules.html"));
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 5:
				try {
					Desktop.getDesktop().browse(new URI("https://docs.google.com/document/d/1kVCmPBd0T27LrV-1dYlMfiXdK_jd5SfYcYOa5dhy3Yc/"));
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 6:
				try {
					Desktop.getDesktop().browse(new URI("https://f.simpleminecraft.ru/index.php?/topic/5037-ogranichenija-na-serverah-simpleminecraft/"));
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 7:
				try {
					Desktop.getDesktop().browse(new URI("https://f.simpleminecraft.ru/index.php?/forum/49-ekonomika/"));
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
		}
	}
	
	public void SetCategory(GuiButtonMenu button, GuiScrollView category) {
		if(this.selectedCategory != null) this.selectedCategory.setVisible(false);
		if(this.selectedCategoryButton != null) this.selectedCategoryButton.selected = false;
		
		category.setVisible(true);
		this.selectedCategory = category;
		
		this.selectedCategoryButton = button;
		this.selectedCategoryButton.selected = true;
	}
	
	@SuppressWarnings("static-access")
	public void drawScreen(int posX, int posY, float partialTicks)
    {
        this.drawDefaultBackground();
        
        mc.getTextureManager().bindTexture(GuiTheme.tooltipBackground);
        
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_ALPHA_TEST);

        this.func_152125_a(this.posX, this.posY, 0, 0, 1, 1, this.minWidth/2, this.minHeight/2, 3, 3);
        this.func_152125_a(this.posX+this.windowWidth-this.minWidth/2, this.posY, 2, 0, 1, 1, this.minWidth/2, this.minHeight/2, 3, 3);
        this.func_152125_a(this.posX+this.windowWidth-this.minWidth/2, this.posY+this.windowHeight-this.minHeight/2, 2, 2, 1, 1, this.minWidth/2, this.minHeight/2, 3, 3);
        this.func_152125_a(this.posX, this.posY+this.windowHeight-this.minHeight/2, 0, 2, 1, 1, this.minWidth/2, this.minHeight/2, 3, 3);
        
        this.func_152125_a(this.posX, this.posY+this.minHeight/2, 0, 1, 1, 1, this.minWidth/2, this.windowHeight-this.minHeight, 3, 3);
        this.func_152125_a(this.posX+this.windowWidth-this.minWidth/2, this.posY+this.minHeight/2, 2, 1, 1, 1, this.minWidth/2, this.windowHeight-this.minHeight, 3, 3);
        this.func_152125_a(this.posX+this.minWidth/2, this.posY, 1, 0, 1, 1, this.windowWidth-this.minWidth, this.minHeight/2, 3, 3);
        this.func_152125_a(this.posX+this.minWidth/2, this.posY+this.windowHeight-this.minHeight/2, 1, 2, 1, 1, this.windowWidth-this.minWidth, this.minHeight/2, 3, 3);
        
        this.func_152125_a(this.posX+this.minWidth/2, this.posY+this.minHeight/2, 1, 1, 1, 1, this.windowWidth-this.minWidth, this.windowHeight-this.minHeight, 3, 3);
    	
    	GL11.glDepthMask(true);
    	GL11.glEnable(GL11.GL_ALPHA_TEST);
        
    	for(GuiElement element : guiElements) {
    		element.draw(mc, posX, posY);
    	}
    }
	
	public void setPosAll() {
		for(GuiElement element : guiElements) {
    		element.posX = element.relPosX+this.posX;
    		element.posY = element.relPosY+this.posY;
    		
    		if(element instanceof GuiScrollView) ((GuiScrollView)element).setPos();
    		else if(element instanceof GuiFunction) ((GuiFunction)element).setPos();
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
				if(selectedElement == this.searchField) this.filterBySearch(searchField.getText());
			}
		}
		if (keyId == 1)
        {
        	this.mc.displayGuiScreen(new GuiScreenModerMenu(mc));
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
        
        
        if(var4 != 0 && selectedCategory != null) {
        	this.selectedCategory.mouseScroll(var4);
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
        	this.CheckPressElements(this.guiElements, posX, posY, mouseButton);
        }
    }
	
	protected void mouseMovedOrUp(int posX, int posY, int button) {
		if (this.pressedElement != null && button == 0)
        {
            this.pressedElement.released(posX, posY);
            this.pressedElement = null;
        }
	}
	
	public boolean CheckPressElements(List<GuiElement> elements, int posX, int posY, int mouseButton) {
		try {
			for(GuiElement element : elements) {
				if(element instanceof GuiFunction) {
					CheckPressElements(((GuiFunction)element).elements, posX, posY, mouseButton);
				}
				else if(element.pressed(mc, posX, posY)) {
	    			if(mouseButton == 0) {
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
				else if(element instanceof GuiScrollView) {
					CheckPressElements(((GuiScrollView)element).elements, posX, posY, mouseButton);
				}
	    	}
		} catch(Exception e) {}
		return false;
	}
}
