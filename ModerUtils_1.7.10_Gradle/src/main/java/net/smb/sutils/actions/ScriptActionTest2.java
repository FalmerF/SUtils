package net.smb.sutils.actions;

import net.eq2online.macros.scripting.ScriptAction;
import net.eq2online.macros.scripting.ScriptContext;
import net.eq2online.macros.scripting.api.APIVersion;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IReturnValue;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.MovingObjectPosition;
import net.smb.sutils.ModuleInfo;

@APIVersion(ModuleInfo.API_VERSION)
public class ScriptActionTest2 extends ScriptAction
{
	public ScriptActionTest2() {
    	super(ScriptContext.MAIN, "test2");
	}
    
    @Override
    public void onInit() {
    	ScriptContext.CHATFILTER.getCore().registerScriptAction(this);
        ScriptContext.MAIN.getCore().registerScriptAction(this);
    }
    
    @Override
    public IReturnValue execute(final IScriptActionProvider provider, final IMacro macro, final IMacroAction instance, final String rawParams, final String[] params) {
    	if (ModuleInfo.permission >= 10) {
    		Minecraft mc = Minecraft.getMinecraft();
    		MovingObjectPosition object = mc.objectMouseOver;
    		mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(1, object.blockX, object.blockY, object.blockZ, 1));
    	}
        return null;
    }
}
