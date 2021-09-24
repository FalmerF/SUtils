package net.smb.sutils.gui.elements;

import net.eq2online.console.Log;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.settings.GameSettings;
import net.smb.sutils.gui.GuiReconnecting;
import net.smb.sutils.modules.SettingsModule;

import org.lwjgl.opengl.GL11;

public class GuiSliderReconnect extends GuiButton
{
    private float value;
    public boolean pressed;
    private final float minValue;
    private final float maxValue;
    private static final String __OBFID = "CL_00000680";
    private final String name = "Reconnect Time: ";

    public GuiSliderReconnect(int id, int posX, int posY)
    {
        this(id, posX, posY, 0.0F, 1.0F);
    }

    public GuiSliderReconnect(int id, int posX, int posY, float minValue, float maxValue)
    {
        super(id, posX, posY, 200, 20, "");
        this.value = 1.0F;
        this.minValue = minValue;
        this.maxValue = maxValue;
        Minecraft var7 = Minecraft.getMinecraft();
        this.value = SettingsModule.getInt("reconnectTime");
        if(this.value == 0) this.value = 1200;
        
        value = (float)((value/20.00F)-10)/290.00F;

        this.displayString = name + SettingsModule.getInt("reconnectTime")/20; //displayString
    }

    public int getHoverState(boolean p_146114_1_) //GetHoverState
    {
        return 0;
    }

    /**
     * Fired when the mouse button is dragged. Equivalent of MouseListener.mouseDragged(MouseEvent e).
     */
    protected void mouseDragged(Minecraft p_146119_1_, int p_146119_2_, int p_146119_3_) //mouseDragged
    {
        if (this.visible) //visible
        {
            if (this.pressed)
            {
                this.value = (float)(p_146119_2_ - (this.xPosition + 4)) / (float)(this.width - 8);

                if (this.value < 0.0F)
                {
                    this.value = 0.0F;
                }

                if (this.value > 1.0F)
                {
                    this.value = 1.0F;
                }
                
                GuiReconnecting.timer = (int) (((int)(29.00F*value)+1)*10)*20;
                value = ((int)(value/0.034F))/29.00F;

                this.displayString = name + GuiReconnecting.timer/20; //displayString
            }
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.drawTexturedModalRect(this.xPosition + (int)(this.value * (float)(this.width - 8)), this.yPosition, 0, 66, 4, 20);
            this.drawTexturedModalRect(this.xPosition + (int)(this.value * (float)(this.width - 8)) + 4, this.yPosition, 196, 66, 4, 20);
        }
    }

    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
     * e).
     */
    public boolean mousePressed(Minecraft p_146116_1_, int p_146116_2_, int p_146116_3_) //mousePressed
    {
        if (super.mousePressed(p_146116_1_, p_146116_2_, p_146116_3_))
        {
            this.value = (float)(p_146116_2_ - (this.xPosition + 4)) / (float)(this.width - 8);

            if (this.value < 0.0F)
            {
                this.value = 0.0F;
            }

            if (this.value > 1.0F)
            {
                this.value = 1.0F;
            }
            this.pressed = true;
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Fired when the mouse button is released. Equivalent of MouseListener.mouseReleased(MouseEvent e).
     */
    public void mouseReleased(int p_146118_1_, int p_146118_2_) //mouseReleased
    {
        this.pressed = false;
        SettingsModule.setParam("reconnectTime", GuiReconnecting.timer);
    }
}

