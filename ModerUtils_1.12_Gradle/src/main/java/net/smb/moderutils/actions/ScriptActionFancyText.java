package net.smb.moderutils.actions;

import java.util.ArrayList;
import java.util.List;

import jds.bibliocraft.tileentities.TileEntityFancySign;
import net.eq2online.macros.scripting.api.APIVersion;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IReturnValue;
import net.eq2online.macros.scripting.api.IScriptAction;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.eq2online.macros.scripting.api.ReturnValue;
import net.eq2online.macros.scripting.parser.ScriptAction;
import net.eq2online.macros.scripting.parser.ScriptContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.RayTraceResult;
import net.smb.moderutils.ModuleInfo;

@APIVersion(ModuleInfo.API_VERSION)
public class ScriptActionFancyText extends ScriptAction
{
    public ScriptActionFancyText() {
        super(ScriptContext.MAIN, "fancytext");
    }
    
    public void onInit() {
        this.context.getCore().registerScriptAction((IScriptAction)this);
    }
    
    int[] charsLine = {40, 29, 21, 14, 10, 8};
    
    public IReturnValue execute(final IScriptActionProvider provider, final IMacro macro, final IMacroAction instance, final String rawParams, final String[] params) {
        if(!ModuleInfo.vip) return null;
    	if(params.length > 0) {
	    	final Minecraft mc = Minecraft.getMinecraft();
	    	
	    	final Object theWorld = Minecraft.getMinecraft().world;
	    	RayTraceResult movingObj = mc.objectMouseOver;
	    	TileEntity tileBlock =  ((WorldClient)theWorld).getTileEntity(movingObj.getBlockPos());
	    	
	    	if(tileBlock == null || !(tileBlock instanceof TileEntityFancySign)) {
	    		provider.actionAddChatMessage("§8[§6Sign§8] §6На указателе нет красивой таблички.");
	    		return (IReturnValue)new ReturnValue("");
	    	}
	    	
	    	TileEntityFancySign tile = (TileEntityFancySign)tileBlock;
	    	
	    	String[] text = provider.expand(macro, params[0], false).split("\\|");
	    	
	    	for(int i = 0; i < 15; i++) {
	    		tile.text[i] = "";
	    		tile.textScale[i] = 1;
	    	}
	    	
	    	if(params.length >= 2) {
	    		String[] textScale = provider.expand(macro, params[1], false).split("\\|");
	    		for(int i = 0; i < textScale.length/2; i++) {
	    			try {
	    				int linenum = (int)clamp(Integer.parseInt(textScale[i*2])-1,0,14);
	    				
	    				tile.textScale[linenum] = (int) clamp(Integer.parseInt(textScale[i*2+1])-1,0,5);
	    			}
	    			catch (Exception e){
	    				provider.actionAddChatMessage("§c" + e.getLocalizedMessage());
	    			}
		    	}
	    	}
	    	
	    	for(int i = 0; i < text.length / 2; i++) {
	    		try {
	    			int linenum = (int)clamp(Integer.parseInt(text[i*2])-1,0,14);
	    		
	    			text[i*2+1] = convertAmpCodes(text[i*2+1]);
	    			List<String> list = new ArrayList<String>();
	    			if(text[i*2+1].length() >= charsLine[tile.textScale[linenum]]) {
	    				String[] words = text[i*2+1].split(" ");
	    				String s = "";
	    				for(int n = 0; n < words.length; n++) {
	    					if(String.valueOf(s + words[n]).length()>=charsLine[tile.textScale[linenum]]) {
	    						if(s == "") {
	    							s = words[n];
	    							list.add(s);
	    							s = "";
	    						}
	    						else {
	    							list.add(s);
	    							s = words[n] + " ";
	    						}
	    					}
	    					else {
	    						s += words[n] + " ";
	    					}
	    				}
	    				if(s != "") {
	    					list.add(s);
	    				}
	    				
	    				for(int n = 0; n < list.size();n++) {
	    					if(linenum+n < 15) {
	    						tile.textScale[linenum+n] = tile.textScale[linenum];
	    						tile.text[linenum+n] = list.get(n);
	    					}
	    					else {
	    						break;
	    					}
	    				}
	    			}
	    			else {
	    				tile.text[linenum] = text[i*2+1];
	    			}
				} catch(Exception e) {
					provider.actionAddChatMessage(e.getMessage());
				}
	    	}
    	}
    	
    	return (IReturnValue)new ReturnValue("");
	}
    
    public static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }
    
    public static String convertAmpCodes(final String text) {
        return text.replaceAll("(?<!&)&([0-9a-fklmnor])", "§$1").replaceAll("&&", "&");
    }
    
}
