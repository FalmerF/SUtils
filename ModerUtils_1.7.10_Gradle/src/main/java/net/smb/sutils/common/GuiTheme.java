package net.smb.sutils.common;

import net.minecraft.util.ResourceLocation;
import net.smb.sutils.modules.SettingsModule;
import net.smb.sutils.utils.Color;

public class GuiTheme {
	public static int theme = 0;
	public static String themeName = "Стандартная";
	
	public static Color textSelect = new Color(0F, 0F, 1.0F, 1.0F);
	public static Color tab = new Color(0.15F, 0.15F, 0.15F, 0.5F);
	public static Color tabInfo = new Color(0.0F, 0.0F, 0.0F, 0.8F);
	public static Color tabPlayer = new Color(0.0F, 0.0F, 0.0F, 0.6F);
	public static Color white = new Color(1.0F, 1.0F, 1.0F, 1.0F);
	public static Color hints = new Color(0.22F, 0.4F, 0.74F, 1.0F);
	public static Color vip = new Color(1.0F, 0.67F, 0, 1.0F);
	public static Color dev = new Color(0F, 0.67F, 0.67F, 1.0F);
	public static int textColor = 14737632;
	public static int textDisabledColor = 7895160;
	public static int fieldTextColor = 14737632;
	public static int fieldDisabledTextColor = 9868950;
	
	public static ResourceLocation background = new ResourceLocation("moderutils", "textures/standart_theme/menu_background.png");
	public static ResourceLocation buttonStandart = new ResourceLocation("moderutils", "textures/standart_theme/button_standart.png");
	public static ResourceLocation button = new ResourceLocation("moderutils", "textures/standart_theme/button.png");
	public static ResourceLocation buttonHover = new ResourceLocation("moderutils", "textures/standart_theme/button_hover.png");
	public static ResourceLocation buttonSelected = new ResourceLocation("moderutils", "textures/standart_theme/button_select.png");
	public static ResourceLocation infoIcon = new ResourceLocation("moderutils", "textures/standart_theme/info.png");
	public static ResourceLocation tooltipBackground = new ResourceLocation("moderutils", "textures/standart_theme/description_window.png");
	public static ResourceLocation functionBackground = new ResourceLocation("moderutils", "textures/standart_theme/function_window.png");
	public static ResourceLocation fieldBackground = new ResourceLocation("moderutils", "textures/standart_theme/field.png");
	public static ResourceLocation scrollBar = new ResourceLocation("moderutils", "textures/standart_theme/scroll_bar.png");
	public static ResourceLocation scrollHandle = new ResourceLocation("moderutils", "textures/standart_theme/scroll_handle.png");
	public static ResourceLocation sliderLine = new ResourceLocation("moderutils", "textures/standart_theme/slider_line.png");
    public static ResourceLocation sliderValue = new ResourceLocation("moderutils", "textures/standart_theme/slider_value.png");
    public static ResourceLocation toggleActive = new ResourceLocation("moderutils", "textures/standart_theme/toggle_active.png");
    public static ResourceLocation toggleDisactive = new ResourceLocation("moderutils", "textures/standart_theme/toggle_disactive.png");
    public static ResourceLocation toggleHandle = new ResourceLocation("moderutils", "textures/standart_theme/toggle_handle.png");
    
    private static String[] themes = new String[] {"standart_theme", "light_theme"};
    private static String[] themeNames = new String[] {"Стандартная", "Светлая"};
    
    public static void loadTheme(int themeId) {
    	if(themeId >= themes.length) themeId = 0;
    	else if(themeId < 0) themeId = themes.length-1;
    	theme = themeId;
    	themeName = themeNames[theme];
    	SettingsModule.setParam("theme", theme);
    	
    	String theme = themes[themeId];
    	
    	background = new ResourceLocation("moderutils", "textures/"+theme+"/menu_background.png");
    	buttonStandart = new ResourceLocation("moderutils", "textures/"+theme+"/button_standart.png");
    	button = new ResourceLocation("moderutils", "textures/"+theme+"/button.png");
    	buttonHover = new ResourceLocation("moderutils", "textures/"+theme+"/button_hover.png");
    	buttonSelected = new ResourceLocation("moderutils", "textures/"+theme+"/button_select.png");
    	infoIcon = new ResourceLocation("moderutils", "textures/"+theme+"/info.png");
    	tooltipBackground = new ResourceLocation("moderutils", "textures/"+theme+"/description_window.png");
    	functionBackground = new ResourceLocation("moderutils", "textures/"+theme+"/function_window.png");
    	fieldBackground = new ResourceLocation("moderutils", "textures/"+theme+"/field.png");
    	scrollBar = new ResourceLocation("moderutils", "textures/"+theme+"/scroll_bar.png");
    	scrollHandle = new ResourceLocation("moderutils", "textures/"+theme+"/scroll_handle.png");
    	sliderLine = new ResourceLocation("moderutils", "textures/"+theme+"/slider_line.png");
        sliderValue = new ResourceLocation("moderutils", "textures/"+theme+"/slider_value.png");
        toggleActive = new ResourceLocation("moderutils", "textures/"+theme+"/toggle_active.png");
        toggleDisactive = new ResourceLocation("moderutils", "textures/"+theme+"/toggle_disactive.png");
        toggleHandle = new ResourceLocation("moderutils", "textures/"+theme+"/toggle_handle.png");
        
        switch(themeId) {
	        case 0:{
	        	textColor = 14737632;
	        	textDisabledColor = 7895160;
	        	fieldTextColor = 14737632;
	        	fieldDisabledTextColor = 9868950;
	        	break;
	        }
	        case 1:{
//	        	textColor = 3158064;
//	        	textDisabledColor = 5066061;
//	        	fieldTextColor = 14737632;
//	        	fieldDisabledTextColor = 9868950;
//	        	break;
	        	textColor = 14737632;
	        	textDisabledColor = 7895160;
	        	fieldTextColor = 14737632;
	        	fieldDisabledTextColor = 9868950;
	        	break;
	        }
        }
    }
}
