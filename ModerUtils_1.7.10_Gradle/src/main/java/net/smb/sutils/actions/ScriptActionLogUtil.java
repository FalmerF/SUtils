package net.smb.sutils.actions;

import net.eq2online.macros.scripting.ScriptAction;
import net.eq2online.macros.scripting.ScriptContext;
import net.eq2online.macros.scripting.ScriptCore;
import net.eq2online.macros.scripting.api.APIVersion;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IReturnValue;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.smb.sutils.ModuleInfo;
import net.smb.sutils.utils.Utils;
import net.smb.sutils.jchat.ClickEvent;
import net.smb.sutils.jchat.JsonMessageBuilder;

@APIVersion(ModuleInfo.API_VERSION)
public class ScriptActionLogUtil extends ScriptAction {

    public ScriptActionLogUtil() {
        super(ScriptContext.MAIN, "logjson");
    }

    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
    	if(params.length > 0) {
    		String msg = ScriptCore.parseVars(provider, macro, params[0], false);
    		String hoverText = "";
    		String clickCommand = "";
    		if(params.length > 1) {
    			hoverText = ScriptCore.parseVars(provider, macro, params[1], false);
    		}
    		if(params.length > 2) {
    			clickCommand = ScriptCore.parseVars(provider, macro, params[2], false);
    		}
    		
    		msg = Utils.convertAmpCodes(msg);
    		hoverText = Utils.convertAmpCodes(hoverText);
    		
    		final Minecraft mc = Minecraft.getMinecraft();
    		
    		if(!hoverText.equals("") && !clickCommand.equals("")) {
    			((EntityPlayer)mc.thePlayer).addChatComponentMessage(new JsonMessageBuilder()
        	    		.newPart()
        	    		.setText(msg)
        	    		.setHoverText(hoverText)
        	    		.setClick(ClickEvent.Type.RUN_COMMAND, clickCommand)
        	    		.end()
        	    		.build()
        	    		.serialize());
    		}
    		else if(!hoverText.equals("")) {
    			((EntityPlayer)mc.thePlayer).addChatComponentMessage(new JsonMessageBuilder()
        	    		.newPart()
        	    		.setText(msg)
        	    		.setHoverText(hoverText)
        	    		.end()
        	    		.build()
        	    		.serialize());
    		}
    		else if(!clickCommand.equals("")) {
       		 ((EntityPlayer)mc.thePlayer).addChatComponentMessage(new JsonMessageBuilder()
     	    		.newPart()
     	    		.setText(msg)
     	    		.setClick(ClickEvent.Type.RUN_COMMAND, clickCommand)
     	    		.end()
     	    		.build()
     	    		.serialize());
    		}
    		else {
       		 ((EntityPlayer)mc.thePlayer).addChatComponentMessage(new JsonMessageBuilder()
     	    		.newPart()
     	    		.setText(msg)
     	    		.end()
     	    		.build()
     	    		.serialize());
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
