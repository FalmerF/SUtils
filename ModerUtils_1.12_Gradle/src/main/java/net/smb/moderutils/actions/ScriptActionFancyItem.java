package net.smb.moderutils.actions;

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
import net.eq2online.macros.scripting.parser.ScriptCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.RayTraceResult;
import net.smb.moderutils.ModuleInfo;

@APIVersion(ModuleInfo.API_VERSION)
public class ScriptActionFancyItem extends ScriptAction
{
    public ScriptActionFancyItem() {
        super(ScriptContext.MAIN, "fancyitem");
    }
    
    public void onInit() {
        this.context.getCore().registerScriptAction((IScriptAction)this);
    }
    
    public IReturnValue execute(final IScriptActionProvider provider, final IMacro macro, final IMacroAction instance, final String rawParams, final String[] params) {	
        if(!ModuleInfo.vip) return null;
    	if(params.length >= 3) {
	    	final Minecraft mc = Minecraft.getMinecraft();
	    	
	    	final Object theWorld = Minecraft.getMinecraft().world;
	    	RayTraceResult movingObj = mc.objectMouseOver;
	    	TileEntity tileBlock =  ((WorldClient)theWorld).getTileEntity(movingObj.getBlockPos());
	    	
	    	if(tileBlock == null || !(tileBlock instanceof TileEntityFancySign)) {
	    		provider.actionAddChatMessage("§8[§6Sign§8] §6На указателе нет красивой таблички.");
	    		return (IReturnValue)new ReturnValue("");
	    	}
	    	
	    	TileEntityFancySign tile = (TileEntityFancySign)tileBlock;
	    	
	    	int itemnum = 0;
	    	int posX = 0;
	    	int posY = 0;
	    	
	    	try {
	    		itemnum = (int)clamp(Integer.parseInt(provider.expand(macro, params[0], false))-1,0,1);
	    		posX = (int)clamp(Integer.parseInt(provider.expand(macro, params[1], false)),-750,750);
	    		posY = (int)clamp(Integer.parseInt(provider.expand(macro, params[2], false)),-750,750);
	    	} catch(Exception e) {
	    		return null;
	    	}
	    	
	    	if(itemnum == 0) {
	    		tile.slot1X = posX;
	    		tile.slot1Y = posY;
	    	}
	    	else if(itemnum == 1) {
	    		tile.slot2X = posX;
	    		tile.slot2Y = posY;
	    	}
	    	
	    	if(params.length >= 4) {
	    		try {
	    			int scale = (int)clamp(Integer.parseInt(provider.expand(macro, params[3], false))-1,0,8);
	    			if(itemnum == 0) {
	    	    		tile.slot1Scale = scale;
	    	    	}
	    	    	else if(itemnum == 1) {
	    	    		tile.slot2Scale = scale;
	    	    	}
	    		}catch(Exception e) {
	    			provider.actionAddChatMessage("§c" + e.getLocalizedMessage());
	    		}
	    	}
    	}
    	
    	return (IReturnValue)new ReturnValue("");
	}
    
    public static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }
}
