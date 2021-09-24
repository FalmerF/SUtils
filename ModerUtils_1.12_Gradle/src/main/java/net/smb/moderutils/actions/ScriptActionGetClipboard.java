package net.smb.moderutils.actions;

import net.eq2online.macros.scripting.api.ReturnValue;
import net.eq2online.macros.scripting.parser.ScriptAction;
import net.eq2online.macros.scripting.parser.ScriptContext;
import net.eq2online.macros.scripting.parser.ScriptCore;
import net.minecraft.client.gui.GuiScreen;
import net.smb.moderutils.ModuleInfo;
import net.eq2online.macros.scripting.api.IReturnValue;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.eq2online.macros.scripting.api.APIVersion;

@APIVersion(ModuleInfo.API_VERSION)
public class ScriptActionGetClipboard extends ScriptAction
{
    public ScriptActionGetClipboard() {
    	super(ScriptContext.MAIN, "getclipboard");
	}
    
    @Override
    public void onInit() {
    	ScriptContext.CHATFILTER.getCore().registerScriptAction(this);
        ScriptContext.MAIN.getCore().registerScriptAction(this);
    }
    
    @Override
    public IReturnValue execute(final IScriptActionProvider provider, final IMacro macro, final IMacroAction instance, final String rawParams, final String[] params) {
        String clipboard = "";
        if (params.length == 0 || params.length == 1) {
            clipboard = GuiScreen.getClipboardString();
            if (params.length == 1) {
                provider.setVariable(macro, params[0], clipboard);
            }
        }
        return (IReturnValue)new ReturnValue(clipboard);
    }
}
