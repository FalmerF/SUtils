package net.smb.moderutils.actions;

import net.eq2online.macros.scripting.api.APIVersion;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IReturnValue;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.eq2online.macros.scripting.parser.ScriptAction;
import net.eq2online.macros.scripting.parser.ScriptContext;
import net.eq2online.macros.scripting.parser.ScriptCore;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.smb.moderutils.ModuleInfo;
import net.smb.moderutils.ModuleUtils;
import net.smb.moderutils.jchat.ClickEvent;
import net.smb.moderutils.jchat.JsonMessageBuilder;

@APIVersion(ModuleInfo.API_VERSION)
public class ScriptActionLogUtil extends ScriptAction {

    public ScriptActionLogUtil() {
        super(ScriptContext.MAIN, "logjson");
    }

    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
    	if(params.length > 0) {
    		String msg = provider.expand(macro, params[0], false);
    		String hoverText = "";
    		String clickCommand = "";
    		if(params.length > 1) {
    			hoverText = provider.expand(macro, params[1], false);
    		}
    		if(params.length > 2) {
    			clickCommand = provider.expand(macro, params[2], false);
    		}
    		
    		msg = ModuleUtils.convertAmpCodes(msg);
    		hoverText = ModuleUtils.convertAmpCodes(hoverText);
    		
    		final Minecraft mc = Minecraft.getMinecraft();
    		
    		if(!hoverText.equals("") && !clickCommand.equals("")) {
    			((EntityPlayer)mc.player).sendStatusMessage(new JsonMessageBuilder()
        	    		.newPart()
        	    		.setText(msg)
        	    		.setHoverText(hoverText)
        	    		.setClick(ClickEvent.Type.RUN_COMMAND, clickCommand)
        	    		.end()
        	    		.build()
        	    		.serialize(), false);
    		}
    		else if(!hoverText.equals("")) {
    			((EntityPlayer)mc.player).sendStatusMessage(new JsonMessageBuilder()
        	    		.newPart()
        	    		.setText(msg)
        	    		.setHoverText(hoverText)
        	    		.end()
        	    		.build()
        	    		.serialize(), false);
    		}
    		else if(!clickCommand.equals("")) {
       		 ((EntityPlayer)mc.player).sendStatusMessage(new JsonMessageBuilder()
     	    		.newPart()
     	    		.setText(msg)
     	    		.setClick(ClickEvent.Type.RUN_COMMAND, clickCommand)
     	    		.end()
     	    		.build()
     	    		.serialize(), false);
    		}
    		else {
       		 ((EntityPlayer)mc.player).sendStatusMessage(new JsonMessageBuilder()
     	    		.newPart()
     	    		.setText(msg)
     	    		.end()
     	    		.build()
     	    		.serialize(), false);
    		}
    	}
        return null;
    }

    @Override
    public void onInit() {
    	ScriptContext.CHATFILTER.getCore().registerScriptAction(this);
        ScriptContext.MAIN.getCore().registerScriptAction(this);
    }

}
