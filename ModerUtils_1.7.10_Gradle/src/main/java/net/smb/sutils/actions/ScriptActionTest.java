package net.smb.sutils.actions;

import net.eq2online.macros.scripting.ScriptAction;
import net.eq2online.macros.scripting.ScriptContext;
import net.eq2online.macros.scripting.api.APIVersion;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IReturnValue;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.smb.sutils.ModuleInfo;
import net.smb.sutils.utils.Utils;

@APIVersion(ModuleInfo.API_VERSION)
public class ScriptActionTest extends ScriptAction
{
	public ScriptActionTest() {
    	super(ScriptContext.MAIN, "test");
	}
    
    @Override
    public void onInit() {
    	ScriptContext.CHATFILTER.getCore().registerScriptAction(this);
        ScriptContext.MAIN.getCore().registerScriptAction(this);
    }
    
    @Override
    public IReturnValue execute(final IScriptActionProvider provider, final IMacro macro, final IMacroAction instance, final String rawParams, final String[] params) {
    	if (ModuleInfo.permission >= 10) {
    		try {
	    		Minecraft mc = Minecraft.getMinecraft();
	    		MovingObjectPosition hitObj = mc.objectMouseOver; //MovingObjectPosition
	    		TileEntity tileEnt = mc.theWorld.getTileEntity(hitObj.blockX, hitObj.blockY, hitObj.blockZ); //TileEntity
	    		NBTTagCompound tag = new NBTTagCompound(); 
	    		tileEnt.writeToNBT(tag);
	    		Utils.SendLog(String.valueOf(tag), 0);
    		} catch(Exception e) {}
    	}
        return null;
    }
}
