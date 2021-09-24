package net.smb.moderutils.actions;

import net.eq2online.macros.scripting.api.ReturnValue;
import net.eq2online.macros.scripting.parser.ScriptAction;
import net.eq2online.macros.scripting.parser.ScriptContext;
import net.minecraft.client.gui.GuiScreen;
import net.smb.moderutils.ModuleInfo;
import net.eq2online.macros.scripting.api.IReturnValue;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.eq2online.macros.scripting.api.APIVersion;

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
            final String clipboard = provider.expand(macro, params[0], false);
            GuiScreen.setClipboardString(clipboard);
        }
        return null;
    }
}
