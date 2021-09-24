package net.smb.sutils.actions;

import net.eq2online.macros.scripting.ScriptAction;
import net.eq2online.macros.scripting.ScriptContext;
import net.eq2online.macros.scripting.ScriptCore;
import net.eq2online.macros.scripting.api.APIVersion;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IReturnValue;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.minecraft.client.gui.GuiScreen;
import net.smb.sutils.ModuleInfo;

@APIVersion(ModuleInfo.API_VERSION)
public class ScriptActionSetClipboard extends ScriptAction
{
	public ScriptActionSetClipboard() {
    	super(ScriptContext.MAIN, "setclipboard");
	}
    
    @Override
    public void onInit() {
    	ScriptContext.CHATFILTER.getCore().registerScriptAction(this);
        ScriptContext.MAIN.getCore().registerScriptAction(this);
    }
    
    @Override
    public IReturnValue execute(final IScriptActionProvider provider, final IMacro macro, final IMacroAction instance, final String rawParams, final String[] params) {
    	if (params.length == 1) {
            final String clipboard = ScriptCore.parseVars(provider, macro, params[0], false);
            GuiScreen.setClipboardString(clipboard);
        }
        return null;
    }
}
