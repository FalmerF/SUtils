package net.smb.moderutils.gui;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.GameType;
import net.smb.moderutils.FunctionDescriptions;
import net.smb.moderutils.GuiTheme;
import net.smb.moderutils.ModuleInfo;
import net.smb.moderutils.ModuleSettings;
import net.smb.moderutils.ModuleUtils;
import net.smb.moderutils.VariableProviderModer;
import net.smb.moderutils.Radio.Radio;
import net.smb.moderutils.effects.EffectBase;

public class GuiScreenModerMenu extends GuiScreen {

    private final Minecraft mc;
    protected static final ResourceLocation coin = new ResourceLocation("moderutils", "textures/money_icon.png");
    protected static final ResourceLocation time = new ResourceLocation("moderutils", "textures/time_icon.png");
    protected static final ResourceLocation tps = new ResourceLocation("moderutils", "textures/tps_icon.png");
    protected static final ResourceLocation ribbon = new ResourceLocation("moderutils", "textures/ribbon.png");
    protected static final ResourceLocation vip = new ResourceLocation("moderutils", "textures/vip.png");
    protected static final ResourceLocation logo = new ResourceLocation("moderutils", "textures/smb_logo.png");
    public GuiElement pressedElement;
    public GuiElement selectedElement;
    public GuiScrollView playerCategory, lowModerCategory, moderCategory, headCategory, settingsCategory, mainCategory, devCategory, vipCategory, vipSaleCategory, radioCategory;
    public GuiButtonMenu selectedCategoryButton;
    public GuiScrollView selectedCategory;
    
    public int posX, posY;
    public List<GuiElement> guiElements = new ArrayList<GuiElement>();
    
    public boolean moving;
    public int relPosX, relPosY;
    
    public String buttonConfigName = "";
    
    private int headPosX, headPosY;
    
    public int holiday = 0;
    
    public List<GuiHint> hints = new ArrayList<GuiHint>();
    public int currentHint = 0;
    
    public EffectBase effect;
    public int effectNum = 0;
	
    public GuiScreenModerMenu(Minecraft mc)
    {
        this.mc = mc;
    }

	@SuppressWarnings("unchecked")
	public void initGui()
    {
		if(ModuleSettings.getInt("guiPosX") == 0) {
			this.posX = (this.width/2)-231;
			this.posY = (this.height/2)-140;
			ModuleSettings.setParam("guiPosX", this.posX);
			ModuleSettings.setParam("guiPosY", this.posY);
		}
		else {
			this.posX = ModuleSettings.getInt("guiPosX");
			this.posY = ModuleSettings.getInt("guiPosY");
		}
		
        this.guiElements.clear();
        GuiButtonMenu btn;
        GuiHint hint;
        int buttonPos = 80;
        if(ModuleInfo.vip) {
	        btn = new GuiButtonMenu(38, 15, buttonPos, 110, 20, "VIP", "textures/vip_icon.png");
	        buttonPos += 25;
	        btn.imgHeightScale = 1.2F;
	        btn.imgWidthScale = 1.2F;
	        this.guiElements.add(btn);
        }
        
        currentHint = ModuleSettings.getControlKey("trainingHint");
        
        if(currentHint <= 9) {
	        // Start Hint
	        hint = new GuiHint(49, 156, 102, 150, 65, "§fДобро пожаловать в SUtils!\n§fДанный гайд поможет Вам\n§fосвоиться в нашем модуле\n\n§e(Кликните по подсказке)");
	        this.hints.add(hint);
	        
	        // Player Category Hint
	        hint = new GuiHint(50, 125, buttonPos-10, 150, 50, "§fВсе функции распределены\n§fпо категориям.\n§fВ §ePlayer§f находятся\n§fфункции для игроков.");
	        hint.setRegion(15, buttonPos, 125, buttonPos+20);
	        this.hints.add(hint);
	        
	        // Category region Hint
	        hint = new GuiHint(51, 24, buttonPos-10, 114, 40, "§fВнутри категорий\n§fВы сможете найти\n§fвсе нужные функции.");
	        hint.setRegion(135, 50, 425, 255);
	        this.hints.add(hint);
	        
	        // Info Hint
	        hint = new GuiHint(49, 147, 41, 150, 50, "§fУ каждой функции есть\n§fсвоё описание, для его\n§fотображения наведите\n§fкурсор на §6(i)§f.");
	        hint.setRegion(138, 51, 146, 59);
	        this.hints.add(hint);
	        
	        // Formating Codes Hint
	        hint = new GuiHint(49, 29, 225, 172, 40, "§fНекоторые функции используют\n§fкоды форматирования,\n§fпосмотреть их Вы можете тут.");
	        hint.setRegion(20, 253, 28, 261);
	        this.hints.add(hint);
	        
	        // Settings Hint
	        hint = new GuiHint(49, 279, 30, 155, 50, "§fВ настройках Вы можете\n§fизменить клавиши открытия\n§fменю и заметок, а также\n§fработа с конфигами.");
	        hint.setRegion(415, 21, 424, 30);
	        this.hints.add(hint);
	        
	        // Radio Hint
	        hint = new GuiHint(52, 305, 31, 132, 30, "§fТебе скучно и одиноко?\n§fЕ***шь українский рок!");
	        hint.setRegion(395, 19, 407, 31);
	        this.hints.add(hint);
	        
	        // Radio Hint 2
	        hint = new GuiHint(49, 130, 75, 145, 40, "§fДля добавления своего\n§fрадио Вам нужна прямая\n§fссылка на поток.");
	        hint.setRegion(140, 115, 425, 130);
	        this.hints.add(hint);
	        
	        // Notes Hint
	        hint = new GuiHint(53, 156, 102, 150, 40, "§fОткрыть меню §eЗаметок§f\n§fВы можете нажав клавишу\n§eHome§f (по стандарту).");
	        this.hints.add(hint);
        }

        this.guiElements.add(new GuiButtonMenu(1, 15, buttonPos, 110, 20, "Player", "textures/player_icon.png"));
        buttonPos += 25;
        if(ModuleInfo.permission >= 1) {
	        btn = new GuiButtonMenu(2, 15, buttonPos, 110, 20, "LowModer", "textures/lowmoder_icon.png");
	        buttonPos += 25;
	        this.guiElements.add(btn);
        }
        
        if(ModuleInfo.permission >= 2) {
	        btn = new GuiButtonMenu(3, 15, buttonPos, 110, 20, "Moder", "textures/moder_icon.png");
	        buttonPos += 25;
	        this.guiElements.add(btn);
        }
        
        if(ModuleInfo.permission >= 3) {
	        btn = new GuiButtonMenu(4, 15, buttonPos, 110, 20, "HeadModer", "textures/headmoder_icon.png");
	        buttonPos += 25;
	        this.guiElements.add(btn);
        }
        
        btn = new GuiButtonMenu(31, 15, buttonPos, 110, 20, "Правила", "textures/Scroll.png");
        buttonPos += 25;
        this.guiElements.add(btn);
        
        if(ModuleInfo.permission >= 10) {
	        btn = new GuiButtonMenu(32, 15, buttonPos, 110, 20, "Dev", "textures/Tools.png");
	        btn.imgWidthScale = 1.3F;
	        buttonPos += 25;
	        this.guiElements.add(btn);
        }
       
        GuiButtonImage btnImage = new GuiButtonImage(0, 433, 22, 7, 7, new ResourceLocation("moderutils", "textures/button_close.png"));
        btnImage.setColor(1.0F, 0.0F, 0.0F);
        this.guiElements.add(btnImage);
        
        if(!ModuleInfo.vip) {
	        btnImage = new GuiButtonImage(39, 360, 20, 24, 13, new ResourceLocation("moderutils", "textures/vip.png"));
	        btnImage.setColorStandart(0.8F, 0.8F, 0.8F);
	        btnImage.setColor(1.0F, 1.0F, 1.0F);
	        this.guiElements.add(btnImage);
        }
        
        btnImage = new GuiButtonImage(25, 415, 21, 9, 9, new ResourceLocation("moderutils", "textures/Gear.png"));
        btnImage.setColor(0.4F, 0.4F, 1.0F);
        btnImage.setColorStandart(1.0F, 1.0F, 1.0F);
        this.guiElements.add(btnImage);
        
        btnImage = new GuiButtonImage(40, 395, 19, 12, 12, new ResourceLocation("moderutils", "textures/radio.png"));
        btnImage.setColor(0.4F, 0.4F, 1.0F);
        btnImage.setColorStandart(1.0F, 1.0F, 1.0F);
        this.guiElements.add(btnImage);
        
        btnImage = new GuiButtonImage(56, 110, 250, 12, 13, new ResourceLocation("moderutils", "textures/discord_logo.png"));
        btnImage.setColor(0.4F, 0.4F, 1.0F);
        btnImage.setColorStandart(1.0F, 1.0F, 1.0F);
        this.guiElements.add(btnImage);
        
        GuiButtonInfo info = new GuiButtonInfo(-1, 1, "Коды форматирования", FunctionDescriptions.color_codes);
        info.relPosX = 20;
        info.relPosY = 253;
        this.guiElements.add(info);
        
        
        // Add Category
        
        GuiFunction func = null;
        GuiToggle toggle = null, toggle2 = null;
        GuiField field = null;
        
        playerCategory = new GuiScrollView(-1, 120, 45, 323, 210);
        this.guiElements.add(playerCategory);
        
        boolean chatFilterEnabled = ModuleSettings.getBool("chatFilter");
        
        func = new GuiFunction(-1, 0, -10, 360, "Фильтр чата");
        func.width = 320;
        toggle = new GuiToggle(5, 35, 35, "chatFilter");
        func.elements.add(toggle);
        func.elements.add(new GuiTextString(55, 35, 0.8F, "-  Включить фильтр"));
        
        field = new GuiField(6, 30, 54, 70, "Код цвета...", "chatFilterColor", GuiField.FieldType.STANDART);
        field.enabled = chatFilterEnabled;
        func.elements.add(field);
        func.elements.add(new GuiTextString(110, 58, 0.8F, "-  Цвет глобального чата"));
        
        field = new GuiField(7, 30, 74, 70, "Код цвета...", "chatFilterTColor", GuiField.FieldType.STANDART);
        field.enabled = chatFilterEnabled;
        func.elements.add(field);
        func.elements.add(new GuiTextString(110, 78, 0.8F, "-  Цвет торгового чата"));
        
        field = new GuiField(8, 30, 94, 70, "Код цвета...", "chatFilterLoaclColor", GuiField.FieldType.STANDART);
        field.enabled = chatFilterEnabled;
        func.elements.add(field);
        func.elements.add(new GuiTextString(110, 98, 0.8F, "-  Цвет локального чата"));
        
        field = new GuiField(9, 30, 114, 70, "Код цвета...", "chatFilterModerColor", GuiField.FieldType.STANDART);
        field.enabled = chatFilterEnabled;
        func.elements.add(field);
        func.elements.add(new GuiTextString(110, 118, 0.8F, "-  Цвет модераторов"));
        
        if(ModuleInfo.permission >= 1) {
        	field = new GuiField(65, 30, 134, 70, "Код цвета...", "chatFilterModerChatColor", GuiField.FieldType.STANDART);
            field.enabled = chatFilterEnabled;
            func.elements.add(field);
	        func.elements.add(new GuiTextString(110, 138, 0.8F, "-  Цвет чата модераторов"));
		}
        
        func.elements.add(new GuiTextString(20, 160, 0.8F, "Слова"));
        func.elements.add(new GuiTextString(100, 160, 0.8F, "Цвет"));
        func.elements.add(new GuiTextString(180, 160, 0.8F, "Аудио"));
        func.elements.add(new GuiTextString(260, 160, 0.8F, "Регистр"));
        GuiScrollView scroll = new GuiScrollView(10, 20, 170, 280, 80);
        
        for(int i = 0; i < 10; i++) {
        	if(!ModuleSettings.getString("highlightedWords" + i).equals("")) {
        		int y = 5+20*(scroll.elements.size()/4);
	        	scroll.elements.add(new GuiField(-1, 0, y, 70, "Слова...", "highlightedWords" + i, GuiField.FieldType.STANDART));
	        	scroll.elements.add(new GuiField(-1, 80, y, 70, "Код цвета...", "highlightedWordsColor" + i, GuiField.FieldType.STANDART));
	        	scroll.elements.add(new GuiButtonSound(-1, 160, y+1, 70, 13, "highlightedWordsSound" + i));
	        	scroll.elements.add(new GuiToggle(-1, 240, y+5, "highlightedWordsRegister" + i));
        	}
        }
        scroll.elements.add(new GuiButtonStandart(11, 105, 5+20*(scroll.elements.size()/4), 70, 13, "Добавить"));
        func.elements.add(scroll);
        
        func.elements.add(new GuiTextString(20, 265, 0.8F, "Как это будет выглядеть:"));
        
        func.elements.add(new GuiTextChatExample(30, 280, 0));
        func.elements.add(new GuiTextChatExample(30, 290, 1));
        func.elements.add(new GuiTextChatExample(30, 300, 2));
        func.elements.add(new GuiTextChatExample(30, 310, 3));
        func.elements.add(new GuiTextChatExample(30, 320, 4));
        if(ModuleInfo.permission >= 1)
        	func.elements.add(new GuiTextChatExample(30, 330, 7));
        
        func.elements.add(new GuiButtonInfo(-1, 1, "Фильтрация чата по вашим настройкам\n ыf-ыf", FunctionDescriptions.chatfilter));
        
        playerCategory.elements.add(func);
        
        func = new GuiFunction(-1, 0, 335, 170, "Авто-реклама");
        func.width = 320;
        
        func.elements.add(new GuiToggle(5, 35, 35, "autoAD"));
        func.elements.add(new GuiTextString(55, 35, 0.8F, "-  Включить/Выключить"));
        
        func.elements.add(new GuiTextString(20, 55, 0.8F, "Реклама"));
        func.elements.add(new GuiTextString(150, 55, 0.8F, "Сервер"));
        func.elements.add(new GuiTextString(250, 55, 0.8F, "Вкл./Выкл."));
        
        scroll = new GuiScrollView(47, 20, 65, 280, 80);
        
        for(int i = 0; i < 10; i++) {
        	if(!ModuleSettings.getString("autoADText" + i).equals("")) {
        		int y = 5+20*(scroll.elements.size()/3);
	        	scroll.elements.add(new GuiField(-1, 0, y, 120, "Реклама...", "autoADText" + i, GuiField.FieldType.STANDART));
	        	scroll.elements.add(new GuiField(-1, 130, y, 100, "Сервер...", "autoADServer" + i, GuiField.FieldType.STANDART));
	        	scroll.elements.add(new GuiToggle(-1, 240, y+5, "autoADEnable" + i));
        	}
        }
        scroll.elements.add(new GuiButtonStandart(48, 105, 5+20*(scroll.elements.size()/3), 70, 13, "Добавить"));
        
        func.elements.add(scroll);
        
        func.elements.add(new GuiButtonInfo(-1, 1, "Автоматическая отправка сообщений\nв чат раз в 5 мин.\n\n§rВ поле §c§lСервер§r напишите полное название\n§rсервера, например: §7TechnoMagic #2\n§7(Не обязательно)\n\n§rДля отправки разных сообщений\n§rзапишите их через §c§l|", FunctionDescriptions.autoAd));
        
        playerCategory.elements.add(func);
        
        func = new GuiFunction(-1, 0, 490, 150, "Бесплатный §6VIP§r за рекламу модуля!");
        func.width = 320;
        
        func.elements.add(new GuiToggle(5, 35, 35, "moduleADS"));
        func.elements.add(new GuiTextString(55, 35, 0.8F, "-  Включить/Выключить"));
        func.elements.add(new GuiTextString(200, 35, 0.8F, "§rСчетчик реклам: §l"+ModuleInfo.adsCount));
        
        func.elements.add(new GuiTextString(20, 55, 0.8F, "Рекламируйте модуль и получите §6VIP§r бесплатно!\n"
        		+ "\n"
        		+ "Как это работает?\n"
        		+ " • Включив данную функцию Вы будите автоматически отправлять\nрекламу модуля в торговый чат.\n"
        		+ " • Отправив рекламу §l"+ModuleUtils.getDataString("adsCount")+" раз§r Вы получите §6SUtils VIP§r на §l2д§r.\n\n"
        		+ "§7Мы выражаем огромную благодарность за рекламу нашего модуля \u00C8"));
        
        playerCategory.elements.add(func);
        
        func = new GuiFunction(-1, 0, 625, 200, "Статистика");
        
        func.elements.add(new GuiToggle(37, 25, 35, "stats"));
        func.elements.add(new GuiTextString(45, 35, 0.8F, "-  Включить/Выключить"));
        func.elements.add(new GuiTextString(25, 55, 0.8F, "Расположение"));
        GuiButtonStandart buttonStandart = new GuiButtonStandart(36, 25, 65, 110, 13, "Слева Сверху");
        int posId = ModuleSettings.getInt("statsPos");
        if(posId == 1) buttonStandart.displayString = "Слева Снизу";
        else if(posId == 2) buttonStandart.displayString = "Справа Сверху";
        else if(posId == 3) buttonStandart.displayString = "Справа Снизу";
        else buttonStandart.displayString = "Слева Сверху";
        
        if(!ModuleSettings.getBool("stats")) buttonStandart.enabled = false;
        
        func.elements.add(buttonStandart);
        
        func.elements.add(new GuiSlider(-1, 25, 85, 50, 0.1F, 3.1F, "statsScale"));
        func.elements.add(new GuiTextString(100, 87, 0.8F, " - Размер"));
        
        if(ModuleSettings.getString("statsPattern").equals("")) {
        	ModuleSettings.setParam("statsPattern", "Баланс: &6%money%");
        }
        
        field = new GuiField(-1, 20, 100, 120, "Шаблон...", "statsPattern", GuiField.FieldType.STANDART);
        field.multiline = true;
        field.minLines = 3;
        
        func.elements.add(field);
        
        func.elements.add(new GuiTextString(20, 140, 0.8F, "Значения: §7%money%, %friends%,\n§7%os%, %tps%, %uptime%"));
        
        playerCategory.elements.add(func);
        
        func = (new GuiFunction(-1, 150, 625, 90, "Смайлики"));
        func.width = 170;
        func.elements.add(new GuiToggle(-1, 21, 35, "smiles"));
        func.elements.add(new GuiTextString(42, 35, 0.8F, "- Включить/Выключить"));
        func.elements.add(new GuiTextString(21, 55, 0.8F, ";D - \u00C1;  :D - \u00C2;§r  <3 - \u00C8; <> - \u00CA;\n$+ - \u00E3; $- - \u00DF"));
        
        func.elements.add(new GuiButtonInfo(-1, 1, "Заменяет символы на смайлики. \u00C2", FunctionDescriptions.smiles));
        
        playerCategory.elements.add(func);
        
        func = new GuiFunction(-1, 150, 695, 80, "Tab");
        func.width = 170;
        toggle = new GuiToggle(-1, 21, 35, "tab");
        func.elements.add(toggle);
        func.elements.add(new GuiTextString(42, 35, 0.8f, "- Включить/Выключить"));
        toggle = new GuiToggle(-1, 21, 50, "tabStats");
        if (ModuleInfo.permission == 0) {
            toggle.enabled = false;
        }
        func.elements.add(toggle);
        func.elements.add(new GuiTextString(42, 50, 0.8f, "- Статистика в Tab"));
        func.elements.add(new GuiButtonInfo(-1, 1.0f, "Заменяет Tab на новый.", ""));
        this.playerCategory.elements.add(func);
        
        func = (new GuiFunction(-1, 150, 755, 70, "Радар"));
        func.width = 170;
        
        func.elements.add(new GuiToggle(-1, 21, 35, "radar"));
        func.elements.add(new GuiTextString(42, 35, 0.8F, "- Включить/Выключить"));
        
        func.elements.add(new GuiButtonInfo(-1, 1, "Отображает ближайших игроков.", ""));
        
        playerCategory.elements.add(func);
        
        func = (new GuiFunction(-1, 0, 810, 255, "Вход/Выход игроков"));
        func.elements.add(new GuiToggle(22, 21, 35, "playerNotify"));
        func.elements.add(new GuiTextString(40, 35, 0.8F, "- Включить уведомления"));
        
        field = new GuiField(23, 20, 45, 125, "[+] %PLAYER%", "joinPlayerPattern", GuiField.FieldType.STANDART);
        field.enabled = ModuleSettings.getBool("playerNotify");
        func.elements.add(field);
        func.elements.add(new GuiTextChatExample(25, 65, 5));
        
        field = new GuiField(24, 20, 80, 125, "[-] %PLAYER%", "leavePlayerPattern", GuiField.FieldType.STANDART);
        field.enabled = ModuleSettings.getBool("playerNotify");
        func.elements.add(field);
        func.elements.add(new GuiTextChatExample(25, 100, 6));
        
        func.elements.add(new GuiTextString(30, 120, 1.0F, "Вход/Выход друзей"));
        func.elements.add(new GuiToggle(62, 21, 135, "friendNotify"));
        func.elements.add(new GuiTextString(40, 135, 0.8F, "- Включить уведомления"));
        
        func.elements.add(new GuiField(-1, 20, 145, 125, "Список друзей...", "friends", GuiField.FieldType.STANDART));
        
        field = new GuiField(63, 20, 165, 125, "[+] Друг %PLAYER%", "joinFriendPattern", GuiField.FieldType.STANDART);
        field.enabled = ModuleSettings.getBool("friendNotify");
        func.elements.add(field);
        func.elements.add(new GuiTextChatExample(25, 185, 8));
        
        field = new GuiField(64, 20, 200, 125, "[-] Друг %PLAYER%", "leaveFriendPattern", GuiField.FieldType.STANDART);
        field.enabled = ModuleSettings.getBool("friendNotify");
        func.elements.add(field);
        func.elements.add(new GuiTextChatExample(25, 220, 9));
        
        func.elements.add(new GuiButtonInfo(-1, 1, "Уведомляет о входе/выходе игроков\nна сервер."
        		+ "\n\n§6%PLAYER% §r- ник игрока.", FunctionDescriptions.join));
        
        playerCategory.elements.add(func);
        
        func = (new GuiFunction(-1, 150, 810, 70, "Auto-Reconnect"));
        func.width = 170;
        
        func.elements.add(new GuiToggle(-1, 21, 35, "reconnect"));
        func.elements.add(new GuiTextString(40, 35, 0.8F, "- Включить/Выключить"));
        
        playerCategory.elements.add(func);
        
        func = (new GuiFunction(-1, 150, 865, 200, "Пусто..."));
        func.width = 170;
        
        func.elements.add(new GuiTextString(20, 35, 0.8F, "Здесь скоро что-то будет."));
        
        playerCategory.elements.add(func);
        
        func = new GuiFunction(-1, 0, 1050, 400, "Удаление не нужных сообщений");
        func.width = 320;
        
        func.elements.add(new GuiToggle(-1, 25, 40, "noPermissionFilter"));
        func.elements.add(new GuiTextString(45, 35, 0.8F, "§4У Вас нет прав для выполнения данной команды.\n§cУ Вас недостаточно прав."));
        
        func.elements.add(new GuiToggle(-1, 25, 70, "noRgPermissionFilter"));
        func.elements.add(new GuiTextString(45, 60, 0.8F, "§4Вам запрещено взаимодействовать с блоками в этом регионе!\n"
        		+ "§4Вы не можете разбивать блоки на этой территории.\n"
        		+ "§cВы не можете выбрасывать предметы на данной территории."));
        
        func.elements.add(new GuiToggle(-1, 25, 100, "pvpFilter"));
        func.elements.add(new GuiTextString(45, 95, 0.8F, "§6У Вас выключен PvP.\n§6Включить можно командой: §c/pvp"));
        
        func.elements.add(new GuiToggle(-1, 25, 120, "knowledgeFilter"));
        func.elements.add(new GuiTextString(45, 120, 0.8F, "§fВам нехватает знаний для изучения этого."));
        
        func.elements.add(new GuiToggle(-1, 25, 140, "criticalDamageFilter"));
        func.elements.add(new GuiTextString(45, 140, 0.8F, "§4Предупреждение: критический урон !!!"));
        
        func.elements.add(new GuiToggle(-1, 25, 160, "clearingFilter"));
        func.elements.add(new GuiTextString(45, 155, 0.8F, "§a[Чистка] §7Чистка выброшенных предметов через §63 минуты.\n"
        		+ "§a[Чистка] §7Чистка выброшенных предметов через §61 минуту."));
        
        func.elements.add(new GuiToggle(-1, 25, 185, "noHitBlockFilter"));
        func.elements.add(new GuiTextString(45, 180, 0.8F, "§cНа указателе нет блока (или он слишком далеко)!\n§cТам ничего нет!"));
        
        func.elements.add(new GuiToggle(-1, 25, 215, "ifsFilter"));
        func.elements.add(new GuiTextString(45, 205, 0.8F, "§e[Магазин] §fПредмет будет установлен при создании...\n"
        		+ "§8Название: §7§oItem\n"
        		+ "§8Данные предмета: §7§oотсутствуют."));
        
        func.elements.add(new GuiToggle(-1, 25, 245, "tradingSetFilter"));
        func.elements.add(new GuiTextString(45, 240, 0.8F, "§e[Магазин] §fТорговая точка успешно создана!\n"
        		+ "§8*Налог* §7§oУплачена пошлина за создание торговой точки."));
        
        func.elements.add(new GuiToggle(-1, 25, 280, "transactionFilter"));
        func.elements.add(new GuiTextString(45, 270, 0.8F, "§e[Магазин] §fУведомление о транзакции:\n"
        		+ "§8*Продажа* §7§oИгрок PLAYER приобрел [ITEM x 1].\n"
        		+ "§8*Покупка* §7§oПредмет [ITEM x 1]."));
        
        func.elements.add(new GuiToggle(-1, 25, 310, "hintsFilter"));
        func.elements.add(new GuiTextString(45, 310, 0.8F, " §e§l>>> §7§oПодсказки"));
        
        func.elements.add(new GuiToggle(-1, 25, 330, "bedrokFilter"));
        func.elements.add(new GuiTextString(45, 330, 0.8F, "§cБедрок запрещён."));
        
        func.elements.add(new GuiToggle(-1, 25, 350, "spawnerFilter"));
        func.elements.add(new GuiTextString(45, 350, 0.8F, "§cИнвентарь спавнера переполнен! Координаты блока:"));
        
        func.elements.add(new GuiToggle(-1, 25, 370, "compassFilter"));
        func.elements.add(new GuiTextString(45, 370, 0.8F, "§cНичего нет, чтобы пройти через него!"));
        
        func.elements.add(new GuiButtonInfo(-1, 1, "Удаляет выбранные вами сообщения.", FunctionDescriptions.clear_messages));
        
        playerCategory.elements.add(func);
        		
        playerCategory.setVisible(false);
        actionPerfomed(toggle);
        
        //=========================
        
        if(ModuleInfo.permission >= 1) {
	        lowModerCategory = new GuiScrollView(-1, 120, 45, 323, 210);
	        this.guiElements.add(lowModerCategory);
	        
	        func = new GuiFunction(-1, 0, -10, 65, "Важно!");
	        func.width = 320;
	        func.elements.add(new GuiTextString(25, 30, 0.8F, "§cИмейте ввиду что распространение функций §bLowModer§c, §9Moder\n§cи §6HeadModer§c - является нарушением пункта §c§l10.5§c."));
	        
	        lowModerCategory.elements.add(func);
	        
	        func = (new GuiFunction(-1, 0, 40, 95, "Auth-Key"));
	        func.elements.add(new GuiField(14, 20, 35, 125, "Ключ...", "authKey", GuiField.FieldType.PASSWORD));
	        func.elements.add(new GuiToggle(15, 26, 59, ""));
	        func.elements.add(new GuiTextString(45, 59, 0.8F, "- Показать ключ"));
	        func.elements.add(new GuiButtonInfo(-1, 1, "Автоматический ввод ключа при\nвходе в игру.", FunctionDescriptions.authKey));
	        lowModerCategory.elements.add(func);
	        
	        func = new GuiFunction(-1, 150, 40, 95, "Цвет наказаний");
	        func.elements.add(new GuiToggle(-1, 21, 35, "chatFilterModer"));
	        func.elements.add(new GuiTextString(40, 35, 0.8F, "- Включить фильтр"));
	        func.elements.add(new GuiButtonInfo(-1, 1, "Изменяет стандартный цвет\nвсех наказаний на §6Gold§r & §cRed§r.", FunctionDescriptions.chatFilterModer));
	        lowModerCategory.elements.add(func);
	        
	        func = new GuiFunction(-1, 0, 120, 105, "TPS & OS");
	        func.elements.add(new GuiToggle(-1, 21, 35, "osFilter"));
	        func.elements.add(new GuiTextString(40, 35, 0.8F, "- Включить отображение\n  /os"));
	        toggle = new GuiToggle(-1, 21, 60, "tpsFilter");
	        if(ModuleInfo.permission < 2) toggle.enabled = false;
	        func.elements.add(toggle);
	        func.elements.add(new GuiTextString(40, 60, 0.8F, "- Включить отображение\n  /mem"));
	        func.elements.add(new GuiButtonInfo(-1, 1, "Отображает твой баланс и онлайн за\nтекущий день.\n§8(Обновляется 1 раз в минуту)", FunctionDescriptions.os_tps));
	        lowModerCategory.elements.add(func);
	        
	        func = new GuiFunction(-1, 150, 120, 215, "Уведомления");
	        toggle = new GuiToggle(16, 21, 35, "allNotify");
	        func.elements.add(toggle);
	        func.elements.add(new GuiTextString(40, 35, 0.8F, "- Уведомления на все\n  сообщения"));
	        
	        toggle2 = new GuiToggle(17, 21, 60, "privateNotify");
	        toggle2.enabled = !toggle.active;
	        func.elements.add(toggle2);
	        func.elements.add(new GuiTextString(40, 60, 0.8F, "- Уведомления на личные\n  сообщения"));
	        
	        toggle2 = new GuiToggle(18, 21, 85, "globalNotify");
	        toggle2.enabled = !toggle.active;
	        func.elements.add(toggle2);
	        func.elements.add(new GuiTextString(40, 85, 0.8F, "- Уведомления на\n  сообщения общего\n  чата"));
	        
	        toggle2 = new GuiToggle(19, 21, 125, "personalNotify");
	        toggle2.enabled = !toggle.active;
	        func.elements.add(toggle2);
	        func.elements.add(new GuiTextString(25, 125, 0.8F, "     - Личные уведомления\n"
	        		+ "Любой пользователь может\n"
	        		+ "упомянуть вас написав\n"
	        		+ "в чат §6§l@" + ModuleInfo.playerName
	        		+ "\nВремя между уведомлениями\n"
	        		+ "5 мин."));
	        func.elements.add(new GuiButtonInfo(-1, 1, "Отправляет системное уведомление\nна указанные сообщения.", FunctionDescriptions.windows_Notify));
	        
	        lowModerCategory.elements.add(func);
	        
	        
	        
	        func = (new GuiFunction(-1, 0, 340, 95, "Auto-Screen"));
	        func.elements.add(new GuiToggle(20, 21, 35, "autoScreen"));
	        func.elements.add(new GuiTextString(40, 35, 0.8F, "- Включить/Выключить"));
	        
	        toggle = new GuiToggle(21, 21, 55, "autoScreenCloseChat");
	        toggle.enabled = ModuleSettings.getBool("autoScreen");
	        func.elements.add(toggle);
	        func.elements.add(new GuiTextString(40, 55, 0.8F, "- Закрывать чат после\n  создания скриншота"));
	        func.elements.add(new GuiButtonInfo(-1, 1, "Автоматически делает скриншот\nпосле выдачи наказания.", FunctionDescriptions.auto_screen));
	        
	        lowModerCategory.elements.add(func);
	        
	        func = (new GuiFunction(-1, 0, 210, 145, "Авто-Замена команд"));
	        func.elements.add(new GuiToggle(-1, 21, 35, "warnCommand"));
	        func.elements.add(new GuiTextString(42, 35, 0.8F, "- /warn -> /cban"));
	        
	        func.elements.add(new GuiToggle(-1, 21, 55, "warnClearCommand"));
	        func.elements.add(new GuiTextString(42, 55, 0.8F, "- /warn clear player\n-> /warn player clear"));
	        
	        func.elements.add(new GuiToggle(-1, 21, 85, "moderChatCommand"));
	        func.elements.add(new GuiTextString(42, 85, 0.8F, "- #text -> /c text"));
	        if(ModuleSettings.getString("moderChatCustomCommand").equals("")) ModuleSettings.setParam("moderChatCustomCommand", "/c");
	        func.elements.add(new GuiField(-1, 20, 100, 125, "Команда...", "moderChatCustomCommand", GuiField.FieldType.STANDART));
	        
	        func.elements.add(new GuiButtonInfo(-1, 1, "При отправке команды /warn с\nуказазанным только ником заменяет\nна команду /cban.\n/warn clear player заменяет на\n/warn player clear\n/c text заменяет на #text", FunctionDescriptions.auto_warn));
	        
	        lowModerCategory.elements.add(func);
	        
	        func = (new GuiFunction(-1, 150, 320, 115, "Подсказки §8(Тест)"));
	        func.elements.add(new GuiToggle(-1, 21, 35, "hints1"));
	        func.elements.add(new GuiTextString(42, 35, 0.8F, "- Больше 5 повторяемых\n  символов §7(1.2)"));
	        
	        func.elements.add(new GuiButtonInfo(-1, 1, "Отправляет в чат подсказки,\nесли появляется нарушение.", FunctionDescriptions.hints));
	        
	        lowModerCategory.elements.add(func);
	        
	        func = (new GuiFunction(-1, 150, 420, 75, "Быстрый invsee"));
	        func.elements.add(new GuiToggle(-1, 21, 35, "fastInvsee"));
	        func.elements.add(new GuiTextString(42, 35, 0.8F, "- Включить/Выключить"));
	        
	        func.elements.add(new GuiButtonInfo(-1, 1, "Тыкните на игрока серым красителем\nчтобы быстро посмотреть его инвентарь.", ""));
	        
	        lowModerCategory.elements.add(func);
	        
	        func = (new GuiFunction(-1, 0, 420, 75, "Авто-варн §8(Тест)"));
	        func.elements.add(new GuiToggle(-1, 21, 35, "autoWarn"));
	        func.elements.add(new GuiTextString(42, 35, 0.8F, "- Включить/Выключить"));
	        
	        func.elements.add(new GuiButtonInfo(-1, 1, "Автоматически выдает варн после мута.\n\n§8(Функция находится в тестовом режиме\n§8и может работать не во всех случаях.)", ""));
	        
	        lowModerCategory.elements.add(func);
	        
	        lowModerCategory.setVisible(false);
        }
        //=========================
        
        if(ModuleInfo.permission >= 2) {
	        moderCategory = new GuiScrollView(-1, 120, 45, 323, 210);
	        this.guiElements.add(moderCategory);
	        
	        func = (new GuiFunction(-1, 0, -10, 95, "Auto-Seen"));
	        func.elements.add(new GuiToggle(-1, 21, 35, "autoseen"));
	        func.elements.add(new GuiTextString(42, 35, 0.8F, "- Включить/Выключить"));
	        
	        func.elements.add(new GuiButtonInfo(-1, 1, "Автоматически проверяет твинки нового\nигрока.", FunctionDescriptions.autoseen));
	        
	        moderCategory.elements.add(func);
	        
	        func = (new GuiFunction(-1, 150, -10, 95, "Перо"));
	        func.elements.add(new GuiToggle(-1, 21, 35, "feather"));
	        func.elements.add(new GuiTextString(42, 35, 0.8F, "- Включить/Выключить"));
	        
	        func.elements.add(new GuiButtonInfo(-1, 1, "Возвращает в работу старое перо.\nПКМ пером = /rg i", FunctionDescriptions.feather));
	        
	        moderCategory.elements.add(func);
	        
	        func = (new GuiFunction(-1, 0, 70, 95, "Safe-Vanish"));
	        func.elements.add(new GuiToggle(34, 21, 35, "svanish"));
	        func.elements.add(new GuiTextString(42, 35, 0.8F, "- Включить/Выключить"));
	        
	        func.elements.add(new GuiButtonInfo(-1, 1, "Не даст вам писать в чат когда вы\nнаходитесь в ванише.\n§8Исключения: модер-чат, лс", FunctionDescriptions.svanish));
	        
	        moderCategory.elements.add(func);
	        
	        moderCategory.setVisible(false);
        }
        
        //=========================
        
        if(ModuleInfo.permission >= 3) {
	        headCategory = new GuiScrollView(-1, 120, 45, 323, 210);
	        this.guiElements.add(headCategory);
	        
	        func = (new GuiFunction(-1, 0, -10, 95, "Local Chat"));
	        func.elements.add(new GuiToggle(-1, 21, 35, "lchat"));
	        func.elements.add(new GuiTextString(42, 35, 0.8F, "- Включить/Выключить"));
	        
	        func.elements.add(new GuiButtonInfo(-1, 1, "Перемещает §c§mеб***е§r локальные сообщения\nв отдельную вкладку §3Tabby Chat§r.", FunctionDescriptions.lChat));
	        
	        headCategory.elements.add(func);
	        
	        func = (new GuiFunction(-1, 150, -10, 95, "SP"));
	        func.elements.add(new GuiToggle(-1, 21, 35, "spchat"));
	        func.elements.add(new GuiTextString(42, 35, 0.8F, "- Включить/Выключить"));
	        
	        func.elements.add(new GuiButtonInfo(-1, 1, "Перемещает сообщения sp\nв отдельную вкладку §3Tabby Chat§r.", FunctionDescriptions.spChat));
	        
	        headCategory.elements.add(func);
	        
	        func = (new GuiFunction(-1, 0, 70, 95, "Guardian"));
	        func.elements.add(new GuiToggle(-1, 21, 35, "guardian"));
	        func.elements.add(new GuiTextString(42, 35, 0.8F, "- Включить/Выключить"));
	        
	        func.elements.add(new GuiButtonInfo(-1, 1, "Записывает действия игрока в\nотдельный файл.", FunctionDescriptions.guardian));
	        
	        headCategory.elements.add(func);
	        
	        func = (new GuiFunction(-1, 150, 70, 95, "OS Состава"));
	        field = new GuiField(-1, 20, 35, 120, "Ники...", "osmoders", GuiField.FieldType.STANDART);
	        field.multiline = true;
	        field.minLines = 3;
	        
	        func.elements.add(field);
	        
	        func.elements.add(new GuiButtonInfo(-1, 1, "Выводит онлайн всего состава.\nНеобходимо записать ники модераторов\nчерез пробел.", FunctionDescriptions.osmoders));
	        
	        headCategory.elements.add(func);
	        
	        func = (new GuiFunction(-1, 0, 150, 95, "/sf <регион> <шаблон>"));
	        
	        String patterns = "Шаблоны: §7";
	        String line = "";
	        
	        for(Entry<String, String> param : ModuleUtils.data.entrySet()) {
	    		String value = param.getKey();
	    		if(value.startsWith("flags")) {
	    			value = value.replace("flags", "");
	    			if(mc.fontRenderer.getStringWidth(line + value) > 110) {
	    				patterns += "\n" + line;
	    				line = value;
	    			}
	    			else {
	    				if(line.equals("")) line = "§7" + value;
	    				else line += ", §7" + value;
	    			}
	    		}
			}
	        if(!line.equals("")) patterns += "\n" + line;
	        
	        func.elements.add(new GuiTextString(20, 35, 0.8F, patterns));
	        
	        func.elements.add(new GuiButtonInfo(-1, 1, "Добавляет флаги в регион\nпо указанному шаблону.", ""));
	        
	        headCategory.elements.add(func);
	        
	        headCategory.setVisible(false);
        }
        
        //=========================
        
        settingsCategory = new GuiScrollView(-1, 120, 45, 323, 210);
        this.guiElements.add(settingsCategory);
        
        func = new GuiFunction(-1, 0, -10, 65, "Важно!");
        func.width = 320;
        func.elements.add(new GuiTextString(25, 30, 0.8F, "§cПожалуйста не передавайте свои конфиги другим пользователям.\n§cВ них может хранится важная информация!"));
        
        settingsCategory.elements.add(func);
        
        int functionPosY = 40;
        
        if(ModuleInfo.vip) {
	        func = new GuiFunction(-1, 0, functionPosY, 65, "Статус §6VIP");
	        func.width = 320;
	        functionPosY += 45;
	        func.elements.add(new GuiTextString(30, 30, 1.0F, "У вас осталось " + ModuleInfo.vipDaysLeft + " д."));
	        settingsCategory.elements.add(func);
        }
        
        func = new GuiFunction(-1, 0, functionPosY, 100, "Конфиги");
        func.width = 320;
        functionPosY += 85;
        
        buttonConfigName = ModuleSettings.config.getName();
        func.elements.add(new GuiButtonStandart(26, 30, 30, 130, 13, buttonConfigName));
        func.elements.add(new GuiButtonStandart(27, 165, 30, 70, 13, "Применить"));
        func.elements.add(new GuiButtonStandart(30, 240, 30, 50, 13, "Удалить"));
        
        func.elements.add(new GuiField(28, 30, 50, 130, "Название...", "", GuiField.FieldType.STANDART));
        func.elements.add(new GuiButtonStandart(29, 165, 51, 70, 13, "Создать"));
        
        settingsCategory.elements.add(func);
        
        func = new GuiFunction(-1, 0, functionPosY, 85, "Управление");
        func.width = 320;
        functionPosY += 70;
        
        func.elements.add(new GuiOptionKey(-1, 30, 30, 60, 13, "openKey"));
        func.elements.add(new GuiTextString(95, 33, 0.8F, "- Открыть меню"));
        
        func.elements.add(new GuiOptionKey(-1, 30, 50, 60, 13, "notesKey"));
        func.elements.add(new GuiTextString(95, 53, 0.8F, "- Открыть меню заметок"));
        
        settingsCategory.elements.add(func);
        
        func = new GuiFunction(-1, 0, functionPosY, 115, "Кастомизация");
        func.width = 320;
        
        func.elements.add(new GuiButtonStandart(58, 30, 30, 130, 13, "Без эффекта"));
        func.elements.add(new GuiTextString(165, 33, 0.8F, "- Эффект в меню"));
        GuiTextString guiText = new GuiTextString(35, 50, 0.8F, "");
        guiText.id = 59;
        func.elements.add(guiText);
        
        func.elements.add(new GuiButtonStandart(60, 30, 80, 130, 13, GuiTheme.themeName));
        func.elements.add(new GuiTextString(165, 83, 0.8F, "- Тема"));
        
        settingsCategory.elements.add(func);
        
        settingsCategory.setVisible(false);
        
        //=========================
        
        mainCategory = new GuiScrollView(-1, 120, 45, 323, 210);
        this.guiElements.add(mainCategory);
        this.selectedCategory = mainCategory;
        
        String text = ModuleUtils.getDataString("StartScreen").replaceAll("\\\\n", "\n");
        mainCategory.elements.add(new GuiTextString(20, 10, 1.0F, text));
        
        mainCategory.setVisible(true);
        
        //=========================
        
        if(ModuleInfo.vip) {
	        vipCategory = new GuiScrollView(-1, 120, 45, 323, 210);
	        this.guiElements.add(vipCategory);
	        
	        text = ModuleUtils.getDataString("VipScreen").replaceAll("\\\\n", "\n");
	        vipCategory.elements.add(new GuiTextString(20, 10, 1.0F, text));
	        
	        vipCategory.setVisible(false);
        }
        
        //=========================
        
        if(!ModuleInfo.vip) {
	        vipSaleCategory = new GuiScrollView(-1, 120, 45, 323, 210);
	        this.guiElements.add(vipSaleCategory);
	        
	        text = ModuleUtils.getDataString("VipSaleScreen").replaceAll("\\\\n", "\n");
	        vipSaleCategory.elements.add(new GuiTextString(20, 10, 1.0F, text));
	        
	        vipSaleCategory.setVisible(false);
        }
        
        //=========================
        
        if(ModuleInfo.permission >= 10) {
	        devCategory = new GuiScrollView(-1, 120, 45, 323, 210);
	        this.guiElements.add(devCategory);
	        
	        func = new GuiFunction(-1, 0, -10, 65, "Dev Func 1");
	        
	        func.elements.add(new GuiToggle(33, 21, 35, "devFunc1"));
	        func.elements.add(new GuiTextString(35, 35, 0.8F, " - Включить/Выключить"));
	        
	        devCategory.elements.add(func);
	        
	        this.guiElements.add(devCategory);
	        
	        func = new GuiFunction(-1, 150, -10, 65, "Dev Func 2");
	        
	        func.elements.add(new GuiToggle(57, 21, 35, "devFunc2"));
	        func.elements.add(new GuiTextString(35, 35, 0.8F, " - Включить/Выключить"));
	        
	        devCategory.elements.add(func);
	        
	        devCategory.setVisible(false);
        }
        
        //=========================
        
        radioCategory = new GuiScrollView(-1, 120, 45, 323, 210);
        this.guiElements.add(radioCategory);
        
        func = new GuiFunction(-1, 0, -10, 120, "Радио!");
        func.width = 325;
        
        guiText = new GuiTextString(150, 35, 0.8F, String.valueOf(ModuleSettings.radios.keySet().toArray()[ModuleSettings.getInt("currentRadio")]));
        guiText.centered = true;
        guiText.id = 49;
        func.elements.add(guiText);
        
        btnImage = new GuiButtonImage(41, 145, 60, 9, 9, new ResourceLocation("moderutils", "textures/play.png"));
        btnImage.setColor(0.4F, 0.4F, 1.0F);
        btnImage.setColorStandart(1.0F, 1.0F, 1.0F);
        if(Radio.radio != null && Radio.radio.isPlaying()) btnImage.buttonTexture = new ResourceLocation("moderutils", "textures/pause.png");
        func.elements.add(btnImage);
        
        btnImage = new GuiButtonImage(44, 125, 58, 13, 13, new ResourceLocation("moderutils", "textures/arrow_left.png"));
        btnImage.setColor(0.4F, 0.4F, 1.0F);
        btnImage.setColorStandart(1.0F, 1.0F, 1.0F);
        func.elements.add(btnImage);
        btnImage = new GuiButtonImage(45, 160, 58, 13, 13, new ResourceLocation("moderutils", "textures/arrow_right.png"));
        btnImage.setColor(0.4F, 0.4F, 1.0F);
        btnImage.setColorStandart(1.0F, 1.0F, 1.0F);
        func.elements.add(btnImage);
        
        func.elements.add(new GuiSlider(43, 195, 60, 85, 0F, 100.0F, "radioVolume"));
        
        btnImage = new GuiButtonImage(-1, 180, 58, 14, 14, new ResourceLocation("moderutils", "textures/volume.png"));
        func.elements.add(btnImage);
        
        func.elements.add(new GuiField(54, 20, 80, 100, "Название...", "", GuiField.FieldType.STANDART));
        func.elements.add(new GuiField(55, 125, 80, 100, "Ссылка...", "", GuiField.FieldType.STANDART));
        
        func.elements.add(new GuiButtonStandart(46, 235, 81, 70, 13, "Добавить"));
        
        radioCategory.elements.add(func);
        
        radioCategory.setVisible(false);
        
        //=========================
        
        if(currentHint < this.hints.size()) this.hints.get(currentHint).setVisible(true);
        
        LocalDate currentdate = LocalDate.now();
        int day = currentdate.getDayOfMonth();
        int month = currentdate.getMonthValue();
        
        if(day == 9 && month == 5) this.holiday = 1;
        
        this.setPosAll();
        setEffect(ModuleSettings.getInt("effect"));
        
        Keyboard.enableRepeatEvents(true);
        
        this.mc.setIngameNotInFocus();
    }
	
	public void updateScreen() {
		super.updateScreen();
		for(GuiElement element : guiElements) element.update();
		for(GuiHint h : hints) h.update();
		if(effect != null) effect.update();
	}
	
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}
	
	protected void actionPerfomed(GuiElement element) {
		int num = 0;
		switch (element.id)
        {
			case 0:
				this.mc.displayGuiScreen((GuiScreen)null);
                this.mc.setIngameFocus();
                break;
			case 1:
				SwitchCategory((GuiButtonMenu)element, this.playerCategory);
                break;
			case 2:
				SwitchCategory((GuiButtonMenu)element, this.lowModerCategory);
                break;
			case 3:
				SwitchCategory((GuiButtonMenu)element, this.moderCategory);
                break;
			case 4:
				SwitchCategory((GuiButtonMenu)element, this.headCategory);
                break;
			case 5:
				boolean active = ((GuiToggle)element).active;
				getById(6).enabled = active;
				getById(7).enabled = active;
				getById(8).enabled = active;
				getById(9).enabled = active;
				getById(10).enabled = active;
				getById(65).enabled = active;
				break;
			case 11:
			{
				GuiScrollView scroll = (GuiScrollView) getById(10);
				if(scroll.elements.size() < 41) {
					addElement:
					for(int i = 0; i < 10; i++) {
						for(GuiElement el : scroll.elements) {
							if((el instanceof GuiField) && ((GuiField)el).option.equals("highlightedWords" + i)) {
								continue addElement;
							}
						}
			        	if(ModuleSettings.getString("highlightedWords" + i).equals("")) {
			        		int y = 5+20*(scroll.elements.size()/4);
				        	scroll.elements.add(new GuiField(-1, 0, y, 70, "Слова...", "highlightedWords" + i, GuiField.FieldType.STANDART));
				        	scroll.elements.add(new GuiField(-1, 80, y, 70, "Код цвета...", "highlightedWordsColor" + i, GuiField.FieldType.STANDART));
				        	scroll.elements.add(new GuiButtonSound(-1, 160, y+1, 70, 13, "highlightedWordsSound" + i));
				        	scroll.elements.add(new GuiToggle(-1, 240, y+5, "highlightedWordsRegister" + i));
				        	element.relPosY = 5+20*(scroll.elements.size()/4);
				        	scroll.setPos();
				        	break;
			        	}
		        	}
				}
				break;
			}
			case 15:
				GuiElement e = getById(14);
				if(e != null && ((GuiToggle)element).active) ((GuiField)e).fieldType = GuiField.FieldType.STANDART;
				else if(e != null && !((GuiToggle)element).active) ((GuiField)e).fieldType = GuiField.FieldType.PASSWORD;
                break;
			case 16:
				this.getById(17).enabled = !((GuiToggle)element).active;
				this.getById(18).enabled = !((GuiToggle)element).active;
				this.getById(19).enabled = !((GuiToggle)element).active;
				break;
			case 20:
				this.getById(21).enabled = ((GuiToggle)element).active;
				break;
			case 22:
				this.getById(23).enabled = ((GuiToggle)element).active;
				this.getById(24).enabled = ((GuiToggle)element).active;
				break;
			case 25:
				SwitchCategory(null, this.settingsCategory);
				break;
			case 26:
				List<String> configs = ModuleSettings.GetAllConfigs();
				int currentConfigIndex = configs.indexOf(buttonConfigName);
				currentConfigIndex++;
				if(currentConfigIndex >= configs.size()) currentConfigIndex = 0;
				else if(currentConfigIndex < 0) currentConfigIndex = configs.size()-1;
				buttonConfigName = configs.get(currentConfigIndex);
				((GuiButtonStandart)element).displayString = buttonConfigName;
				break;
			case 27:
				ModuleSettings.SetCurrentConfig(buttonConfigName.replaceAll("\\.config", ""));
				this.initGui();
				break;
			case 29:
				String name = ((GuiField)this.getById(28)).getText();
				name = ModuleSettings.createConfig(name.replaceAll("\\.config", ""));
				ModuleSettings.SetCurrentConfig(name.replaceAll("\\.config", ""));
				this.initGui();
				break;
			case 30:
				if(ModuleSettings.deleteConfig(buttonConfigName.replaceAll("\\.config", ""))) {
					this.initGui();
				}
				else {
					buttonConfigName = "main.config";
					((GuiButtonStandart)this.getById(26)).displayString = buttonConfigName;
				}
				break;
			case 31:
				this.mc.displayGuiScreen(new GuiScreenRules(mc));
                break;
			case 32:
				SwitchCategory((GuiButtonMenu)element, this.devCategory);
                break;
			case 33:
				break;
			case 34:
				if(!((GuiToggle) element).active) VariableProviderModer.vanish = false;
				break;
			case 36:
				int posId = ModuleSettings.getInt("statsPos");
				posId++;
				if(posId > 3) posId = 0;
				else if(posId < 0) posId = 3;
				GuiButtonStandart buttonStandart = (GuiButtonStandart)element;
		        if(posId == 1) buttonStandart.displayString = "Слева Снизу";
		        else if(posId == 2) buttonStandart.displayString = "Справа Сверху";
		        else if(posId == 3) buttonStandart.displayString = "Справа Снизу";
		        else buttonStandart.displayString = "Слева Сверху";
		        ModuleSettings.setParam("statsPos", posId);
				break;
			case 37:
				this.getById(36).enabled = ((GuiToggle)element).active;
				break;
			case 38:
				SwitchCategory((GuiButtonMenu)element, this.vipCategory);
                break;
			case 39:
				SwitchCategory(null, this.vipSaleCategory);
                break;
			case 40:
				SwitchCategory(null, this.radioCategory);
                break;
			case 41:
				if(Radio.canPlay) {
					GuiButtonImage btnImage = (GuiButtonImage) getById(41);
			        if(Radio.radio == null || !Radio.radio.isPlaying()) {
			        	btnImage.buttonTexture = new ResourceLocation("moderutils", "textures/pause.png");
	
			        	Radio.playRadio(String.valueOf(ModuleSettings.radios.keySet().toArray()[ModuleSettings.getInt("currentRadio")]));
			        }
			        else {
			        	btnImage.buttonTexture = new ResourceLocation("moderutils", "textures/play.png");
			        	if (Radio.radio != null) Radio.radio.stop();
			        }
				}
		        break;
			case 44:
				if(Radio.canPlay) {
					num = ModuleSettings.getInt("currentRadio");
					num = num-1 < 0 ? ModuleSettings.radios.size()-1 : num-1;
					Radio.playRadio(String.valueOf(ModuleSettings.radios.keySet().toArray()[num]));
				}
				break;
			case 45:
				if(Radio.canPlay) {
					num = ModuleSettings.getInt("currentRadio");
					num = num+1 > ModuleSettings.radios.size()-1 ? 0 : num+1;
					Radio.playRadio(String.valueOf(ModuleSettings.radios.keySet().toArray()[num]));
				}
				break;
			case 46:
				if(Radio.canPlay) {
					GuiField fieldName = (GuiField)getById(54);
					GuiField fieldLink = (GuiField)getById(55);
					if(!fieldName.getText().equals("") && !fieldLink.getText().equals("")) {
						ModuleSettings.radios.put(fieldName.getText(), fieldLink.getText());
						ModuleSettings.saveRadios();
						Radio.playRadio(fieldName.getText());
						fieldName.setText("");
						fieldLink.setText("");
					}
				}
				break;
			case 48:
			{
				GuiScrollView scroll = (GuiScrollView) getById(47);
				if(scroll.elements.size() < 41) {
					addElement:
					for(int i = 0; i < 10; i++) {
						for(GuiElement el : scroll.elements) {
							if((el instanceof GuiField) && ((GuiField)el).option.equals("autoADText" + i)) {
								continue addElement;
							}
						}
			        	if(ModuleSettings.getString("autoADText" + i).equals("")) {
			        		int y = 5+20*(scroll.elements.size()/3);
			        		scroll.elements.add(new GuiField(-1, 0, y, 120, "Реклама...", "autoADText" + i, GuiField.FieldType.STANDART));
				        	scroll.elements.add(new GuiField(-1, 130, y, 100, "Сервер...", "autoADServer" + i, GuiField.FieldType.STANDART));
				        	scroll.elements.add(new GuiToggle(-1, 240, y+5, "autoADEnable" + i));
				        	element.relPosY = 5+20*(scroll.elements.size()/3);
				        	scroll.setPos();
				        	break;
			        	}
		        	}
				}
				break;
			}
			case 49:
			{
				nextHint((GuiHint)element);
				break;
			}
			case 50:
			{
				nextHint((GuiHint)element);
				SwitchCategory((GuiButtonMenu)getById(1), this.playerCategory);
				break;
			}
			case 51:
			{
				nextHint((GuiHint)element);
				this.playerCategory.goToValue = 0.2615f;
				break;
			}
			case 52:
			{
				nextHint((GuiHint)element);
				SwitchCategory(null, this.radioCategory);
				break;
			}
			case 53:
			{
				nextHint((GuiHint)element);
				this.mc.displayGuiScreen(new GuiScreenNotes(mc));
				break;
			}
			case 56:
			{
				try {
					Desktop.getDesktop().browse(new URI("https://discord.gg/hw2M2tfNhE"));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				break;
			}
			case 57:
			{
				if(((GuiToggle)element).active) Minecraft.getMinecraft().playerController.setGameType(GameType.CREATIVE);
				else Minecraft.getMinecraft().playerController.setGameType(GameType.SURVIVAL);
				break;
			}
			case 58:
			{
		        setEffect(this.effectNum+1);
				break;
			}
			case 60:
			{
		        GuiTheme.loadTheme(GuiTheme.theme+1);
		        ((GuiButtonStandart)element).displayString = GuiTheme.themeName;
				break;
			}
        }
	}
	
	public void nextHint(GuiHint h) {
		this.currentHint++;
		h.setVisible(false);
		if(currentHint < this.hints.size()) this.hints.get(currentHint).setVisible(true);
		ModuleSettings.setControlKey("trainingHint", currentHint);
	}
	
	public void updateRadioText() {
		((GuiTextString)getById(49)).SetText(String.valueOf(ModuleSettings.radios.keySet().toArray()[ModuleSettings.getInt("currentRadio")]));
	}
	
	protected void actionPerfomed2(GuiElement element) {
		switch (element.id)
        {
			case 26:
				List<String> configs = ModuleSettings.GetAllConfigs();
				int currentConfigIndex = configs.indexOf(buttonConfigName);
				currentConfigIndex--;
				if(currentConfigIndex >= configs.size()) currentConfigIndex = 0;
				else if(currentConfigIndex < 0) currentConfigIndex = configs.size()-1;
				buttonConfigName = configs.get(currentConfigIndex);
				((GuiButtonStandart)element).displayString = buttonConfigName;
				break;
			case 36:
				int posId = ModuleSettings.getInt("statsPos");
				posId--;
				if(posId > 3) posId = 0;
				else if(posId < 0) posId = 3;
				GuiButtonStandart buttonStandart = (GuiButtonStandart)element;
		        if(posId == 1) buttonStandart.displayString = "Слева Снизу";
		        else if(posId == 2) buttonStandart.displayString = "Справа Сверху";
		        else if(posId == 3) buttonStandart.displayString = "Справа Снизу";
		        else buttonStandart.displayString = "Слева Сверху";
		        ModuleSettings.setParam("statsPos", posId);
				break;
			case 58:
			{
		        setEffect(this.effectNum-1);
				break;
			}
			case 60:
			{
		        GuiTheme.loadTheme(GuiTheme.theme-1);
		        ((GuiButtonStandart)element).displayString = GuiTheme.themeName;
				break;
			}
        }
	}
	
	protected void actionReleased(GuiElement element) {
		if(element.id == 43) {
			if(Radio.radio != null) Radio.radio.setVolume(ModuleSettings.getFloat("radioVolume")/100);
		}
	}
	
	public void setEffect(int effectNum) {
        if(effectNum >= EffectBase.effects.length) effectNum = 0;
        else if(effectNum < 0) effectNum = EffectBase.effects.length-1;
        
        if(effectNum != 0) {
        	((GuiButtonStandart)getById(58)).displayString = EffectBase.effects[effectNum].getName();
        	((GuiTextString)getById(59)).SetText(EffectBase.effects[effectNum].getDescription());
        }
        else {
        	((GuiButtonStandart)getById(58)).displayString = "Без эффекта";
        	((GuiTextString)getById(59)).SetText("");
        }
        
        this.effectNum = effectNum;
		
		effect = EffectBase.effects[effectNum];
        if(effect != null) {
        	effect.initEffect(width, height);
        	
        	if(!effect.vip || (effect.vip && ModuleInfo.vip)) {
        		ModuleSettings.setParam("effect", effectNum);
        	}
        	else if(ModuleSettings.getInt("effect") == effectNum) ModuleSettings.setParam("effect", 0);
        }
        else ModuleSettings.setParam("effect", 0);
	}
	
	public GuiElement getById(int id) {
    	for(GuiElement element : this.guiElements) {
    		if(element.id == id) return element;
    		else if(element instanceof GuiScrollView) {
    			GuiElement e = ((GuiScrollView)element).getById(id);
    			if(e != null) return e;
    		}
    		else if(element instanceof GuiFunction) {
    			GuiElement e = ((GuiFunction)element).getById(id);
    			if(e != null) return e;
    		}
        }
    	return null;
    }
	
	public void SwitchCategory(GuiButtonMenu button, GuiScrollView category) {
		if(this.selectedCategoryButton != null) this.selectedCategoryButton.selected = false;
		if(this.selectedCategory != null) this.selectedCategory.setVisible(false);
		
		if(button != null) {
			this.selectedCategoryButton = button;
			button.selected = true;
		}
		category.setVisible(true);
		this.selectedCategory = category;
		
		if(effect != null && effect.vip && !ModuleInfo.vip) {
			setEffect(ModuleSettings.getInt("effect"));
    	}
	}
	
	@SuppressWarnings("static-access")
	public void drawScreen(int posX, int posY, float partialTicks)
    {
        this.drawDefaultBackground();
        if(effect != null) effect.draw(mc, posX, posY);
        
        mc.getTextureManager().bindTexture(GuiTheme.background);
        
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        
    	this.drawScaledCustomSizeModalRect(this.posX, this.posY, 0, 0, 1, 1, (int)(420*1.1F), (int)(255*1.1F), 1, 1);
    	
        mc.getTextureManager().bindTexture(mc.player.getLocationSkin());
        this.headPosX = (int)this.lerp(this.headPosX, this.posX+25, 0.4F);
        this.headPosY = (int)this.lerp(this.headPosY, this.posY+40, 0.4F);
    	this.drawScaledCustomSizeModalRect(this.headPosX, this.headPosY, 1, 1, 1, 1, 20, 20, 8, 8);
    	
    	if(this.holiday == 1) {
	    	mc.getTextureManager().bindTexture(this.ribbon);
	    	this.drawScaledCustomSizeModalRect(this.headPosX-10, this.headPosY+9, 0, 0, 1, 1, 25, 25, 1, 1);
    	}
    	if(ModuleInfo.vip) {
	    	mc.getTextureManager().bindTexture(this.vip);
	    	this.drawScaledCustomSizeModalRect(this.posX+25, this.posY+61, 0, 0, 1, 1, 20, 11, 1, 1);
    	}
    	
    	FontRenderer fontRender = mc.fontRenderer;
    	
    	fontRender.drawString("SUtils " + ModuleInfo.version, this.posX + 36, this.posY + 21, GuiTheme.textColor);
    	mc.getTextureManager().bindTexture(logo);
    	this.drawScaledCustomSizeModalRect(this.posX + 15, this.posY + 15, 1, 1, 1, 1, 20, 20, 1, 1);
    	
    	if(ModuleInfo.playerName.length() > 13) {
    		GL11.glPushMatrix();
        	GL11.glTranslatef(this.posX + 50, this.posY + 41, 0.0F);
            GL11.glScalef(0.8F, 0.8F, 0.8F);
            fontRender.drawString(ModuleInfo.playerName, 0, 0, 14737632);
        	GL11.glPopMatrix();
    	}
    	else fontRender.drawString(ModuleInfo.playerName, this.posX + 50, this.posY + 40, GuiTheme.textColor);
    	
    	switch(ModuleInfo.permission) {
    		case 0:
    			fontRender.drawString("Player", this.posX + 50, this.posY + 52, 10526880);
    			break;
    		case 1:
    			fontRender.drawString("LowModer", this.posX + 50, this.posY + 52, 5636095);
    			break;
    		case 2:
    			fontRender.drawString("Moder", this.posX + 50, this.posY + 52, 5592575);
    			break;
    		case 3:
    			fontRender.drawString("HeadModer", this.posX + 50, this.posY + 52, 16755200);
    			break;
    		case 10:
    			fontRender.drawString("§3Developer", this.posX + 50, this.posY + 52, 10526880);
    			break;
    	}
    	
    	fontRender.drawString("§6" + ModuleInfo.playerBalance, this.posX + 145, this.posY + 21, GuiTheme.textColor);
    	
    	mc.getTextureManager().bindTexture(this.coin);
    	GL11.glColor4f(0.32F, 0.69F, 0.43F, 1.0F);
    	this.drawScaledCustomSizeModalRect(this.posX + 137, this.posY + 20, 0, 0, 1, 1, 6, 10, 1, 1);
    	
    	if(ModuleSettings.getBool("osFilter") && ModuleInfo.permission > 0) {
    		fontRender.drawString(ModuleInfo.playerOS, this.posX + 230, this.posY + 21, GuiTheme.textColor);
    		
    		mc.getTextureManager().bindTexture(this.time);
    		GL11.glColor4f(0.82F, 0.83F, 0.34F, 1.0F);
        	this.drawScaledCustomSizeModalRect(this.posX + 222, this.posY + 20, 0, 0, 1, 1, 6, 10, 1, 1);
    	}
    	
		if(ModuleSettings.getBool("tpsFilter") && ModuleInfo.permission > 1) {
			fontRender.drawString(ModuleInfo.serverTPS, this.posX + 287, this.posY + 21, GuiTheme.textColor);
			
			mc.getTextureManager().bindTexture(this.tps);
			GL11.glColor4f(0.44F, 0.71F, 0.90F, 1.0F);
        	this.drawScaledCustomSizeModalRect(this.posX + 275, this.posY + 20, 0, 0, 1, 1, 10, 8, 1, 1);
		}
		
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_BLEND);
    	GL11.glDepthMask(true);
    	GL11.glEnable(GL11.GL_ALPHA_TEST);
        
    	for(GuiElement element : guiElements) {
    		element.draw(mc, posX, posY);
    	}
    	for(GuiHint h : hints) h.draw(mc, posX, posY);
    	
    	ModuleInfo.desc.draw(mc, posX, posY);
    	
    	if(moving) {
    		this.posX = Math.max(Math.min(posX-this.relPosX, this.width-(int)(420*1.1F)+10), -10);
    		this.posY = Math.max(Math.min(posY-this.relPosY, this.height-(int)(255*1.1F)+10), -10);
    		this.setPosAll();
		}
    }
	
	public void setPosAll() {
		for(GuiElement element : guiElements) {
    		element.posX = element.relPosX+this.posX;
    		element.posY = element.relPosY+this.posY;
    		
    		if(element instanceof GuiScrollView) ((GuiScrollView)element).setPos();
    		else if(element instanceof GuiFunction) ((GuiFunction)element).setPos();
    	}
		for(GuiHint h : hints) {
			h.posX = h.relPosX+this.posX;
    		h.posY = h.relPosY+this.posY;
		}
        this.headPosX = this.posX + 25;
        this.headPosY = this.posY + 40;
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
	
	protected void mouseClicked(int posX, int posY, int mouseButton)
    {
        if (mouseButton == 0)
        {
        	if(this.selectedElement != null) {
        		this.selectedElement.setSelected(false);
	        	this.selectedElement = null;
        	}
        	this.CheckPressElements(this.guiElements, posX, posY, mouseButton);
        	
        	if(this.pressedElement == null) {
        		if(posX > this.posX+10 && posX < this.posX+410 && posY > this.posY+10 && posY < this.posY+30) {
        			this.moving = true;
        			this.relPosX = posX - this.posX;
        			this.relPosY = posY - this.posY;
        		}
        		else if(posX > this.posX+25 && posX < this.posX+45 && posY > this.posY+40 && posY < this.posY+60) {
        			this.headPosY += 5;
        		}
        	}
        }
        else if(mouseButton == 1) this.CheckPressElements(this.guiElements, posX, posY, mouseButton);
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
	    		if(element.pressed(mc, posX, posY) && !(element instanceof GuiFunction)) {
	    			if(mouseButton == 0) {
		    			element.playPressSound(this.mc.getSoundHandler());
		    			if(this.pressedElement == null) {
			    			this.pressedElement = element;
			    			this.pressedElement.pressed = true;
		    			}
		    			element.clicked();
		    			this.actionPerfomed(element);
		    			if(element.enabled && element.visible) {
		    	        	this.selectedElement = element;
		    	        	this.selectedElement.setSelected(true);
		    			}
	    			}
	    			else if(mouseButton == 1) {
	    				this.actionPerfomed2(element);
	    				element.clicked(mouseButton);
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
	
	protected void mouseMovedOrUp(int posX, int posY, int button)
    {
        if (this.pressedElement != null && button == 0)
        {
        	this.pressedElement.released(posX, posY);
        	this.actionReleased(pressedElement);
            this.pressedElement = null;
        }
        if(button == 0) {
        	this.moving = false;
        	ModuleSettings.setParam("guiPosX", this.posX);
			ModuleSettings.setParam("guiPosY", this.posY);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void handleMouseInput()
    {
        int var1 = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int var2 = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
        int var3 = Mouse.getEventButton();
        int var4 = Math.max(Math.min(Mouse.getEventDWheel(),1),-1);

        if (var3 != -1)
        {
            this.mouseMovedOrUp(var1, var2, var3);
        }
        if (Mouse.getEventButtonState())
        {
            this.mouseClicked(var1, var2, var3);
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

        this.mc.dispatchKeypresses();
    }
    
    public float lerp(float a, float b, float f)
    {
        return a + f * (b - a);
    }
}
