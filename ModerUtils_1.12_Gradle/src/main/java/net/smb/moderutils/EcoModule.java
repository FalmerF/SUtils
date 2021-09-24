package net.smb.moderutils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.lwjgl.input.Keyboard;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mumfrey.liteloader.ChatFilter;
import com.mumfrey.liteloader.Tickable;
import com.mumfrey.liteloader.core.LiteLoaderEventBroker.ReturnValue;

import net.eq2online.console.Log;
import net.eq2online.macros.compatibility.Reflection;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.smb.moderutils.EcoModule.ItemInfo;
import net.smb.moderutils.http.EnumHttpMethod;
import net.smb.moderutils.http.HttpStateData;
import net.smb.moderutils.http.ReturnValueArrayImplodeScalar;
import net.smb.moderutils.jchat.ClickEvent.Type;
import net.smb.moderutils.jchat.JsonMessageBuilder;

public class EcoModule implements Tickable, ChatFilter {
	public static EcoModule instance;
	
	public static String TMTable = "1e27qzWs7PwY-5ihDbUl61Sc6CDt7eH2NrXdsTivXjPw";
    public static String MagicRPGTable = "1bewMMsYnt32d-_9VHO4V2ZZ8GqD_0kvZrq1L9r0UWcg";
    public static String FantasyTable = "10a9tuKGTG4NWveuAcyTCmLGyylLIwQUaGxEU3R5N_7k";
    public static String SFTable = "11Ca0nRE4-sNLD4y5xvg8s6gFfAi3EqgiEf-fOygNdWs";
    public static String HiTechTable = "12b3BLGWez7ssYeW-VWfrRjNLZqulypq1376ejSZHCAM";
    public static String PixelmonTable = "1cSkRZ5AXVGQZlawaYYP2fE4Cga1H7IL5dCS0bCGOiEg";
    public static String GalaxyTable = "1nrfEidYdtGcyGkSewX-MlEZbbv8i4-wnod3KJUfn5go";
    public static String WizardTable = "15orX5Iv9arHDTNSiX_rZNv1I3s3idD2iiueEumeGqpQ";
    public static String NanoTechTable = "1864HGcrTp1FO79OZsM7ViakfkKZWOJTOdLiYgkNEiRY";
    
    protected static JsonParser parser = new JsonParser();
    
    private List<ItemInfo> items = new ArrayList<ItemInfo>();
    
    private static Pattern itemIdPattern = Pattern.compile("^([0-9]+):?([0-9]*)$");
    private static Pattern tableDataPattern = Pattern.compile("^(id:\\s([0-9]*[:]*[0-9]*))?,?\\s?(cost:\\s([0-9]*))?,?\\s?(charge:\\s([0-9]*))?$");
	
    public boolean tradeStation = false;
    
    Minecraft mc;
    
    public int[] pos1 = new int[3];
    public int[] pos2 = new int[3];
    
	public EcoModule(){
		instance = this;
		mc = Minecraft.getMinecraft();
	}
	
	public void loadData() {
		if(ModuleInfo.vip) {
	        final Minecraft mc = Minecraft.getMinecraft();
	        final ServerData serverData = mc.getCurrentServerData();
	        String serverName = "";
	        String sheetId = TMTable;
	        tradeStation = false;
	        if(serverData != null) {
	        	serverName = serverData.serverName;
	        }
	        String dataName = "TM";
	        
	        if(serverName.equals("MagicRPG #1") || serverName.equals("MagicRPG #2") || serverName.equals("MagicRPG")) dataName = "MagicRPG";
	        else if(serverName.equals("Fantasy")) dataName = "Fantasy";
	        else if(serverName.equals("SkyFactory")) dataName = "SF";
	        else if(serverName.equals("HiTech")) dataName = "HiTech";
	        else if(serverName.equals("Pixelmon #1") || serverName.equals("Pixelmon #2") || serverName.equals("Pixelmon")) dataName = "Pixelmon";
	        else if(serverName.equals("Galaxy")) dataName = "Galaxy";
	        else if(serverName.equals("Wizard #1") || serverName.equals("Wizard #2") || serverName.equals("Wizard")) dataName = "Wizard";
	        else if(serverName.equals("NanoTech #1") || serverName.equals("NanoTech #2") || serverName.equals("NanoTech")) dataName = "NanoTech";
	        
	        String[] tradeStationServers = ModuleUtils.getDataString("tradeStationServers").split(",");
	        for(String server : tradeStationServers) {
	        	if(server.equals(serverName)) {
	        		tradeStation = true;
	        		break;
	        	}
	        }

			String url  = "https://macros-core.com/data/eco/" + dataName + ".txt";
			BufferedReader reader = ModuleUtils.getDataFromURL(url);
			String line;
			try {
		        while ((line = reader.readLine()) != null) {
		            if(!line.equals("")) {
		            	try {
		            		String[] lineData = line.split("=");
		            		String id = lineData[0].replaceAll(" ", "");
				        	int cost = Integer.parseInt(lineData[1]);
				        	boolean charge = lineData[2].equals("1") ? true : false;
				        	items.add(new ItemInfo(id, cost, charge));
		            	} catch(Exception e) {}
		            }
		        }
			} catch(Exception e) {e.printStackTrace();}
		}
	}
	
	public ItemInfo getItemById(int id, int meta) {
		for(ItemInfo item : items) {
			if(item.equals(id, meta)) return item;
		}
		return null;
	}
	
	public ItemInfo getItemById(String id) {
		for(ItemInfo item : items) {
			if(item.equals(id)) return item;
		}
		return null;
	}
	
	public ItemInfo getItemByStack(ItemStack itemStack) {
		int id = Item.getIdFromItem(itemStack.getItem());
		int meta = itemStack.getItemDamage();
		for(ItemInfo item : items) {
			if(item.equals(id, meta)) return item;
		}
		return null;
	}
	
	public ItemStack getItemStackById(String fullid) {
		String[] data = fullid.split(":");
		int meta = 0;
		int id = 0;
		try {
			id = Integer.parseInt(data[0]);
			if(data.length >= 2 && !data[1].equals("")) meta = Integer.parseInt(data[1]);
		} catch(Exception e) {meta = 0;}
		return new ItemStack(Item.getItemById(id), 1, meta);
	}
	
	public String getFullId(ItemStack itemStack) {
		String id = String.valueOf( Item.getIdFromItem(itemStack.getItem()));
		if(itemStack.getItemDamage() != 0) id += ":" + itemStack.getItemDamage();
		return id;
	}
	
	private boolean placeSign = false;
	private int signTick = 0;
	private String[] signText;
	
	public void setEco(int count) {
		EntityPlayerSP player = mc.player;
		if(player == null) return;
		new Thread(() -> {
			try {
				ItemStack itemStack = player.inventory.getCurrentItem().copy();
				itemStack.setCount(count);
				
				if(itemStack == null) {
		        	ModuleUtils.SendLog("§8[§6Eco§8] §6Предмет не найден.", 0);
		        	return;
		        }
		        ItemInfo item = getItemByStack(itemStack);
		        if(item == null) {
		        	ModuleUtils.SendLog("§8[§6Eco§8] §6id: " + getFullId(itemStack) + " §6не §6найден, §6возможно §6данный §6предмет §6ещё §6не §6добавлен §6в §6экономику.", 0);
		        	sendToDiscord(itemStack.getDisplayName(), getFullId(itemStack));
		        	return;
		        }
		        
		        if(this.tradeStation) {
		        	final TileEntity tileEnt = mc.world.getTileEntity(mc.objectMouseOver.getBlockPos());
		        	Class tileEntityTradeStationClass = Class.forName("ru.simplemc.simplecore.mod.common.tileentity.TileEntityTradeStation");
		        	if(tileEnt == null || tileEnt.getClass() != tileEntityTradeStationClass) {
		        		ModuleUtils.SendLog("§cНет торговой станции на курсоре.", 0);
		        		return;
		        	}
		        	
		        	Class tradeDataClass = Class.forName("ru.simplemc.simplecore.mod.common.data.TradeData");
		        	Class tradeTypeClass = Class.forName("ru.simplemc.simplecore.mod.common.data.TradeType");
		        	Class networkHandlerClass = Class.forName("ru.simplemc.simplecore.mod.handler.network.NetworkHandler");
		        	Class tradeSettingsApplyMessageClass = Class.forName("ru.simplemc.simplecore.mod.handler.network.message.TradeSettingsApplyMessage");
		        	
		        	Object tradeDataInstance = tradeDataClass.getMethod("copy").invoke(tileEntityTradeStationClass.getMethod("getData").invoke(tileEnt));
		        	tradeDataClass.getMethod("setItemStack", ItemStack.class).invoke(tradeDataInstance, itemStack);
		        	tradeDataClass.getMethod("setPrice", BigDecimal.class).invoke(tradeDataInstance, new BigDecimal(count*item.cost));
		        	// tradeDataClass.getMethod("setType", tradeTypeClass).invoke(tradeDataInstance, Enum.valueOf((Class<Enum>) tradeTypeClass, "BUY"));
		        	
		        	Object networkWrapper = networkHandlerClass.getField("NETWORK_WRAPPER").get(networkHandlerClass);	
		  
		        	Object tradeSettingsApplyMessageInstance = tradeSettingsApplyMessageClass.getConstructor(tradeDataClass).newInstance(tradeDataInstance);
		        	networkWrapper.getClass().getMethod("sendToServer", IMessage.class).invoke(networkWrapper, tradeSettingsApplyMessageInstance);
		        	ModuleUtils.SendLog("§8[§6Eco§8] §6§l" + itemStack.getDisplayName() + " §a" + itemStack.getCount() + " §aшт. = §a" + (count*item.cost) + " §aэк.", 0);
		        }
		        else {
			        Minecraft.getMinecraft().displayGuiScreen(null);
			        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), true);
			        ModuleUtils.SendMessage("/ifs");
			        Thread.sleep((long) (500));
			        
			        InventoryPlayer inventory = player.inventory;
			        int signSlot = -1;
			        for (int i = 0; i < 9; ++i) {
		                if (inventory.mainInventory.get(i) == null || inventory.mainInventory.get(i).getItem() != Items.SIGN) continue;
		                signSlot = i;
		                break;
		            }
			        if(signSlot != -1) {
			        	ItemStack sign = inventory.mainInventory.get(signSlot);
			        	signText = new String[4];
			        	signText[0] = "";
			        	signText[1] = String.valueOf(count);
			        	signText[2] = String.valueOf(count*item.cost);
			        	signText[3] = "";
			        	actionUseItem(mc, player, sign, signSlot);
			        	signTick = 0;
			        	placeSign = true;
			        	ModuleUtils.SendLog("§8[§6Eco§8] §6§l" + itemStack.getDisplayName() + " §a" + itemStack.getCount() + " §aшт. = §a" + (count*item.cost) + " §aэк.", 0);
			        }
			        else {
			        	ModuleUtils.SendLog("§cУ Вас нет таблички в хотбаре.", 0);
			        }
		        }
		} catch(Exception e) {e.printStackTrace();}
		}).start();
	}
	
	// From MacroKeybind
	public void actionUseItem(final Minecraft minecraft, final EntityPlayerSP thePlayer, final ItemStack itemstack, final int slotID) {
        final int oldItem = thePlayer.inventory.currentItem;
        thePlayer.inventory.currentItem = slotID;
        for (final EnumHand enumhand : EnumHand.values()) {
            final BlockPos blockpos = minecraft.objectMouseOver.getBlockPos();
            if (minecraft.world.getBlockState(blockpos).getMaterial() != Material.AIR) {
                final int i = (itemstack != null) ? itemstack.getCount() : 0;
                final EnumActionResult result = minecraft.playerController.processRightClickBlock(minecraft.player, minecraft.world, blockpos, minecraft.objectMouseOver.sideHit, minecraft.objectMouseOver.hitVec, enumhand);
                if (result == EnumActionResult.SUCCESS) {
                    if (itemstack == null) {
                        break;
                    }
                    if (itemstack.getCount() == 0) {
                        minecraft.player.setHeldItem(enumhand, (ItemStack)null);
                        break;
                    }
                    if (itemstack.getCount() != i || minecraft.playerController.isInCreativeMode()) {
                        minecraft.entityRenderer.itemRenderer.resetEquippedProgress(enumhand);
                        break;
                    }
                    break;
                }
            }
        }
        thePlayer.inventory.currentItem = oldItem;
    }
	
	public void getEco() {
		if(this.tradeStation) {
			int price = 0;
	        ItemStack itemStack = null;
	        final RayTraceResult hitObj = mc.objectMouseOver;
	        if(hitObj != null && hitObj.typeOfHit == RayTraceResult.Type.BLOCK) {
	        	try {
		        	final TileEntity tileEnt = mc.world.getTileEntity(hitObj.getBlockPos());
		        	Class tileEntityTradeStation = Class.forName("ru.simplemc.senergetics.common.tile.TileEntityTradeStation");
		        	if(tileEnt.getClass() == tileEntityTradeStation) {
		        		Object data = tileEnt.getClass().getField("data").get(tileEnt);
		        		itemStack = (ItemStack) data.getClass().getMethod("getTradeItemStack").invoke(data, null);
		        	    price = ((BigDecimal) data.getClass().getMethod("getPrice").invoke(data, null)).intValue();
		        	}
	        	}
	        	catch(Exception e) {}
	        }
	        
	        if(itemStack == null) {
	        	ModuleUtils.SendLog("§8[§6Eco§8] §6Предмет не найден.", 0);
	        	return;
	        }
	        ItemInfo item = getItemByStack(itemStack);
	        if(item == null) {
	        	ModuleUtils.SendLog("§8[§6Eco§8] §6id: " + getFullId(itemStack) + " §6не §6найден, §6возможно §6данный §6предмет §6ещё §6не §6добавлен §6в §6экономику.", 0);
	        	sendToDiscord(itemStack.getDisplayName(), getFullId(itemStack));
	        	return;
	        }
	        
	        if(price >= itemStack.getCount()*item.cost) {
	        	ModuleUtils.SendLog("§8[§6Eco§8] §aВерно: §6§l" + itemStack.getDisplayName() + " §a" + itemStack.getCount() + " §aшт. = §a" + (itemStack.getCount()*item.cost) + " §aэк.", 0);
	        	return;
	        }
	        else {
	        	JsonMessageBuilder builder = new JsonMessageBuilder()
	        			.newPart()
    		    		.setText("§8[§6Eco§8] §cНе верно: §6§l" + itemStack.getDisplayName() + " §c" + itemStack.getCount() + " §cшт. = §c" + (itemStack.getCount()*item.cost) + " §cэк.")
    		    		.setHoverText("*Копировать*")
    		    		.setClick(Type.RUN_COMMAND, "/setclipboard " + itemStack.getDisplayName() + " " + itemStack.getCount() + " шт. = " + (itemStack.getCount()*item.cost) + " эк.")
    		    		.end();
		        	ModuleUtils.SendLog(builder, 0);
	        	return;
	        }
		}
		else {
			final List<String> returnList = new LinkedList<String>();
			
	        final RayTraceResult hitObj = mc.objectMouseOver;
	        if (hitObj != null && hitObj.typeOfHit == RayTraceResult.Type.BLOCK) {
	            final TileEntity tileEnt = mc.world.getTileEntity(hitObj.getBlockPos());
	            if (tileEnt != null && (tileEnt.getBlockType() == Blocks.STANDING_SIGN || tileEnt.getBlockType() == Blocks.WALL_SIGN)) {
	                for (final ITextComponent line : ((TileEntitySign)tileEnt).signText) {
	                    returnList.add(line.getUnformattedText());
	                }
	            }
	        }
	        
	        if(returnList.size() == 0 || returnList.get(0).equals("") || returnList.get(1).equals("") || returnList.get(2).equals("") || returnList.get(3).equals("")) {
	        	ModuleUtils.SendLog("§8[§6Eco§8] §6Нет торговой таблички.", 0);
	        	return;
	        }
	        
	        int itemCount = 0;
	        int signCost = 0;
	        
	        itemCount = Integer.parseInt(returnList.get(1));
	        returnList.set(2, returnList.get(2).replaceAll("B", ""));
	        returnList.set(2, returnList.get(2).replaceAll(" ", ""));
	        signCost = Integer.parseInt(returnList.get(2));
	        String searchId = returnList.get(3);
	        
	        if(itemCount == 0 || signCost == 0) {
	        	ModuleUtils.SendLog("§8[§6Eco§8] §6Нет торговой таблички.", 0);
	        	return;
	        }
	        
	        ItemInfo item = getItemById(searchId);
	        if(item == null) {
	        	ModuleUtils.SendLog("§8[§6Eco§8] §6id: " + searchId + " §6не §6найден, §6возможно §6данный §6предмет §6ещё §6не §6добавлен §6в §6экономику.", 0);
	        	sendToDiscord(getItemStackById(searchId).getDisplayName(), searchId);
	        	return;
	        }
	        
	        if(signCost >= itemCount*item.cost) {
	        	ModuleUtils.SendLog("§8[§6Eco§8] §aВерно: §6§l" + item.itemStack.getDisplayName() + " §a" + itemCount + " §aшт. = §a" + (itemCount*item.cost) + " §aэк.", 0);
	        	return;
	        }
	        else {
	        	JsonMessageBuilder builder = new JsonMessageBuilder()
	        			.newPart()
    		    		.setText("§8[§6Eco§8] §cНе верно: §6§l" + item.itemStack.getDisplayName() + " §c" + itemCount + " §cшт. = §c" + (itemCount*item.cost) + " §cэк.")
    		    		.setHoverText("*Копировать*")
    		    		.setClick(Type.RUN_COMMAND, "/setclipboard " + item.itemStack.getDisplayName() + " " + itemCount + " шт. = " + (itemCount*item.cost) + " эк.")
    		    		.end();
	        	ModuleUtils.SendLog(builder, 0);
	        	return;
	        }
		}
	}
	
	@SuppressWarnings("unused")
	public void checkEco() {
		new Thread(() -> {
			ModuleUtils.SendMessage("//size");
			try {
	            Thread.sleep((long) (1000));
	        
				final WorldClient theWorld = Minecraft.getMinecraft().world;
				final EntityPlayerSP thePlayer = Minecraft.getMinecraft().player;
		        if (theWorld != null && thePlayer != null) {
		            int xPos = pos1[0], yPos = pos1[1], zPos = pos1[2];
		            int xPos2 = pos2[0], yPos2 = pos2[1], zPos2 = pos2[2];
		            
		            int tempInt = 0;
		            
		            if(xPos > xPos2) {
		            	tempInt = xPos;
		            	xPos = xPos2;
		            	xPos2 = tempInt;
		            }
		            if(yPos > yPos2) {
		            	tempInt = yPos;
		            	yPos = yPos2;
		            	yPos2 = tempInt;
		            }
		            if(zPos > zPos2) {
		            	tempInt = zPos;
		            	zPos = zPos2;
		            	zPos2 = tempInt;
		            }
		            TileEntity tile = null;
		            int totalGoodSigns = 0;
		            int totalSigns = 0;
		            
		            if(this.tradeStation) {
			        	Class tileEntityTradeStation = Class.forName("ru.simplemc.senergetics.common.tile.TileEntityTradeStation");
			            
			            for(int xInt = xPos; xInt <= xPos2;xInt++) {
			            	for(int zInt = zPos; zInt <= zPos2;zInt++) {
			            		for(int yInt = yPos; yInt <= yPos2;yInt++) {
			            			try {
				            			tile = theWorld.getTileEntity(new BlockPos(xInt, yInt, zInt));
				            			if(tile != null && tile.getClass() == tileEntityTradeStation) {
				            				
				        	        		Object data = tile.getClass().getField("data").get(tile);
				        	        	    ItemStack itemStack = (ItemStack) data.getClass().getMethod("getTradeItemStack").invoke(data, null);
				        	        	    int price = ((BigDecimal) data.getClass().getMethod("getPrice").invoke(data, null)).intValue();
				        	        	    
				        	        	    String itemId = String.valueOf(Item.getIdFromItem(itemStack.getItem()));
				        	                String itemMeta = String.valueOf(itemStack.getItemDamage());
				        	                int itemCount = itemStack.getCount();
				            		        
				        	                if(price == 0) continue;
				        	                
				            		        ItemInfo item = getItemByStack(itemStack);
				            		        
				            		        if(item == null) {
				            		        	JsonMessageBuilder builder = new JsonMessageBuilder()
		            		        			.newPart()
				            		    		.setText("§8[§6" + xInt + " " + yInt + " " + zInt + "§8]")
				            		    		.setHoverText("/tppos " + xInt + " " + yInt + " " + zInt)
				            		    		.setClick(Type.RUN_COMMAND, "/tppos " + xInt + " " + yInt + " " + zInt)
				            		    		.end()
				            		    		.newPart()
				            		    		.setText(" §6id: §a" + getFullId(itemStack) + " §6еще §6не §6добавлен §6в §6экономику.")
				            		    		.end();
				            		        	ModuleUtils.SendLog(builder, 0);
				            		        	sendToDiscord(itemStack.getDisplayName(), getFullId(itemStack));
				            		        	continue;
				            		        }
				            		        
				            		        
				            		        if(item.cost * itemCount > price) {
				            		        	JsonMessageBuilder builder = new JsonMessageBuilder()
		            		        			.newPart()
				            		    		.setText("§8[§6" + xInt + " " + yInt + " " + zInt + "§8]")
				            		    		.setHoverText("/tppos " + xInt + " " + yInt + " " + zInt)
				            		    		.setClick(Type.RUN_COMMAND, "/tppos " + xInt + " " + yInt + " " + zInt)
				            		    		.end()
				            		    		.newPart()
				            		    		.setText(" §cНе верно: §6§l" + itemStack.getDisplayName() + " §c" + itemCount + " §cшт. = §c" + (item.cost * itemCount) + " §cэк.")
				            		    		.setHoverText("*Копировать*")
				            		    		.setClick(Type.RUN_COMMAND, "/setclipboard " + itemStack.getDisplayName() + " " + itemCount + " шт. = " + (item.cost * itemCount) + " эк.")
				            		    		.end();
				            		        	ModuleUtils.SendLog(builder, 0);
				            		        }
				            		        else {
				            		        	totalGoodSigns++;
				            		        }
				            		        totalSigns++;
				            			}
			            			} catch(Exception e) {}
			            		}
			            	}
			            }
		            }
		            else {
		            	for(int xInt = xPos; xInt <= xPos2;xInt++) {
			            	for(int zInt = zPos; zInt <= zPos2;zInt++) {
			            		for(int yInt = yPos; yInt <= yPos2;yInt++) {
			            			try {
				            			tile = theWorld.getTileEntity(new BlockPos(xInt, yInt, zInt));
				            			if(tile != null) {
				            				final List<String> returnList = new LinkedList<String>();
				            				
			            		            if (tile.getBlockType() == Blocks.STANDING_SIGN || tile.getBlockType() == Blocks.WALL_SIGN) {
			            		                for (final ITextComponent line : ((TileEntitySign)tile).signText) {
			            		                    returnList.add(line.getUnformattedText());
			            		                }
			            		            }
				            		        
				            		        if(returnList.size() == 0 || returnList.get(0).equals("") || returnList.get(1).equals("") || returnList.get(2).equals("") || returnList.get(3).equals("")) {
				            		        	continue;
				            		        }
				            		        
				            		        int itemCount = 0;
				            		        int signCost = 0;
				            		        
				            		        itemCount = Integer.parseInt(returnList.get(1));
				            		        returnList.set(2, returnList.get(2).replaceAll("B", ""));
				            		        returnList.set(2, returnList.get(2).replaceAll(" ", ""));
				            		        signCost = Integer.parseInt(returnList.get(2));
				            		        String searchId = returnList.get(3);
				            		        
				            		        if(itemCount == 0 || signCost == 0) continue;
				        	                
				            		        ItemInfo item = getItemById(searchId);
				            		        
				            		        if(item == null) {
				            		        	JsonMessageBuilder builder = new JsonMessageBuilder()
		            		        			.newPart()
				            		    		.setText("§8[§6" + xInt + " " + yInt + " " + zInt + "§8]")
				            		    		.setHoverText("/tppos " + xInt + " " + yInt + " " + zInt)
				            		    		.setClick(Type.RUN_COMMAND, "/tppos " + xInt + " " + yInt + " " + zInt)
				            		    		.end()
				            		    		.newPart()
				            		    		.setText(" §6id: §a" + searchId + " §6еще §6не §6добавлен §6в §6экономику.")
				            		    		.end();
				            		        	ModuleUtils.SendLog(builder, 0);
				            		        	sendToDiscord(getItemStackById(searchId).getDisplayName(), searchId);
				            		        	continue;
				            		        }
				            		        
				            		        
				            		        if(item.cost * itemCount > signCost) {
				            		        	JsonMessageBuilder builder = new JsonMessageBuilder()
		            		        			.newPart()
				            		    		.setText("§8[§6" + xInt + " " + yInt + " " + zInt + "§8]")
				            		    		.setHoverText("/tppos " + xInt + " " + yInt + " " + zInt)
				            		    		.setClick(Type.RUN_COMMAND, "/tppos " + xInt + " " + yInt + " " + zInt)
				            		    		.end()
				            		    		.newPart()
				            		    		.setText(" §cНе верно: §6§l" + item.itemStack.getDisplayName() + " §c" + itemCount + " §cшт. = §c" + (item.cost * itemCount) + " §cэк.")
				            		    		.setHoverText("*Копировать*")
				            		    		.setClick(Type.RUN_COMMAND, "/setclipboard " + item.itemStack.getDisplayName() + " " + itemCount + " шт. = " + (item.cost * itemCount) + " эк.")
				            		    		.end();
				            		        	ModuleUtils.SendLog(builder, 0);
				            		        }
				            		        else {
				            		        	totalGoodSigns++;
				            		        }
				            		        totalSigns++;
				            			}
			            			} catch(Exception e) {}
			            		}
			            	}
			            }
		            }
		            ModuleUtils.SendLog("§8[§6Eco§8] §6Проверено: §a" + totalSigns + " §6торговых точек. Верно: §a" + totalGoodSigns + " §6торговых точек.", 0);
	        }
			} catch(Exception e) {}
		}).start();
	}
	
	@SuppressWarnings("unused")
	public void np() {
		new Thread(() -> {
			ModuleUtils.SendMessage("//size");
			try {
	            Thread.sleep((long) (1000));
	        
	            final WorldClient theWorld = Minecraft.getMinecraft().world;
				final EntityPlayerSP thePlayer = Minecraft.getMinecraft().player;
		        if (theWorld != null && thePlayer != null) {
		            int xPos = pos1[0], yPos = pos1[1], zPos = pos1[2];
		            int xPos2 = pos2[0], yPos2 = pos2[1], zPos2 = pos2[2];
		            
		            int tempInt = 0;
		            
		            if(xPos > xPos2) {
		            	tempInt = xPos;
		            	xPos = xPos2;
		            	xPos2 = tempInt;
		            }
		            if(yPos > yPos2) {
		            	tempInt = yPos;
		            	yPos = yPos2;
		            	yPos2 = tempInt;
		            }
		            if(zPos > zPos2) {
		            	tempInt = zPos;
		            	zPos = zPos2;
		            	zPos2 = tempInt;
		            }
		            TileEntity tile = null;
		            int totalNeed = 0;
		            int totalSigns = 0;
		            
		            if(this.tradeStation) {
			        	Class tileEntityTradeStation = Class.forName("ru.simplemc.senergetics.common.tile.TileEntityTradeStation");
			            
			            for(int xInt = xPos; xInt <= xPos2;xInt++) {
			            	for(int zInt = zPos; zInt <= zPos2;zInt++) {
			            		for(int yInt = yPos; yInt <= yPos2;yInt++) {
			            			try {
				            			tile = theWorld.getTileEntity(new BlockPos(xInt, yInt, zInt));
				            			if(tile != null && tile.getClass() == tileEntityTradeStation) {
				            				
				        	        		Object data = tile.getClass().getField("data").get(tile);
				        	        		ItemStack itemStack = (ItemStack) data.getClass().getMethod("getTradeItemStack").invoke(data, null);
				        	        	    int price = ((BigDecimal) data.getClass().getMethod("getPrice").invoke(data, null)).intValue();
				        	        	    int availability = (int) data.getClass().getMethod("getAvailability").invoke(data, null);
				            		        
				        	        	    if(itemStack == null || price == 0) continue;
				        	        	    
				            		        if(availability < 1) {
				            		        	JsonMessageBuilder builder = new JsonMessageBuilder()
		            		        			.newPart()
				            		    		.setText("§8[§6" + xInt + " " + yInt + " " + zInt + "§8]")
				            		    		.setHoverText("/tppos " + xInt + " " + yInt + " " + zInt)
				            		    		.setClick(Type.RUN_COMMAND, "/tppos " + xInt + " " + yInt + " " + zInt)
				            		    		.end()
				            		    		.newPart()
				            		    		.setText(" §a" + itemStack.getDisplayName() + " §cзакончился §cв §cторговой §cточке.")
				            		    		.end();
				            		        	ModuleUtils.SendLog(builder, 0);
				            		        	totalNeed++;
				            		        	continue;
				            		        }
				            		        totalSigns++;
				            			}
			            			} catch(Exception e) {}
			            		}
			            	}
			            }
		            }
		            ModuleUtils.SendLog("§8[§6Eco§8] §6Проверено: §a" + totalSigns + " §6торговых точек. Не достаточно товаров: §a" + totalNeed + ".", 0);
	        }
			} catch(Exception e) {}
		}).start();
	}
	
	@SuppressWarnings("unused")
	public void winfo(String file) {
		new Thread(() -> {
			ModuleUtils.SendMessage("//size");
			try {
	            Thread.sleep((long) (1000));
	        
	            List<String> allItems = new ArrayList<String>();
	            int topT = 0, topS = 0, allturnOver = 0;
	            String topTInfo = "", topSInfo = "";
	            
	            final WorldClient theWorld = Minecraft.getMinecraft().world;
				final EntityPlayerSP thePlayer = Minecraft.getMinecraft().player;
		        if (theWorld != null && thePlayer != null) {
		            int xPos = pos1[0], yPos = pos1[1], zPos = pos1[2];
		            int xPos2 = pos2[0], yPos2 = pos2[1], zPos2 = pos2[2];
		            
		            int tempInt = 0;
		            
		            if(xPos > xPos2) {
		            	tempInt = xPos;
		            	xPos = xPos2;
		            	xPos2 = tempInt;
		            }
		            if(yPos > yPos2) {
		            	tempInt = yPos;
		            	yPos = yPos2;
		            	yPos2 = tempInt;
		            }
		            if(zPos > zPos2) {
		            	tempInt = zPos;
		            	zPos = zPos2;
		            	zPos2 = tempInt;
		            }
		            TileEntity tile = null;
		            
		            if(this.tradeStation) {
			        	Class tileEntityTradeStation = Class.forName("ru.simplemc.senergetics.common.tile.TileEntityTradeStation");
			            
			            for(int xInt = xPos; xInt <= xPos2;xInt++) {
			            	for(int zInt = zPos; zInt <= zPos2;zInt++) {
			            		for(int yInt = yPos; yInt <= yPos2;yInt++) {
			            			try {
				            			tile = theWorld.getTileEntity(new BlockPos(xInt, yInt, zInt));
				            			if(tile != null && tile.getClass() == tileEntityTradeStation) {
				            				
				        	        		Object data = tile.getClass().getField("data").get(tile);
				        	        		ItemStack itemStack = (ItemStack) data.getClass().getMethod("getTradeItemStack").invoke(data, null);
				        	        	    int price = ((BigDecimal) data.getClass().getMethod("getPrice").invoke(data, null)).intValue();
				            		        int turnOver = ((BigDecimal) data.getClass().getMethod("getTurnover").invoke(data, null)).intValue();
				        	        	    int transaction = (int) data.getClass().getMethod("getTransactions").invoke(data, null);
				            		        if(itemStack == null || price == 0) continue;
				        	        	    
				            		        String itemInfo = itemStack.getDisplayName()+" - "+"Всего продано: "+transaction+" Прибыль: "+turnOver;
				            		        allturnOver += turnOver;
				        	        	    allItems.add(itemInfo);
				        	        	    
				        	        	    if(transaction > topT) {
				        	        	    	topTInfo = itemInfo;
				        	        	    	topT = transaction;
				        	        	    }
				        	        	    
				        	        	    if(turnOver > topS) {
				        	        	    	topSInfo = itemInfo;
				        	        	    	topS = turnOver;
				        	        	    }
				            			}
			            			} catch(Exception e) {}
			            		}
			            	}
			            }
		            }
		            String fullInfo = "// Данная статистика собрана при помощи SUtils\n\n"
		            		+ "Самый продаваемый предмет: "+topTInfo
		            		+ "\nСамый прибыльный предмет: "+topSInfo
		            		+ "\nПрибыль за все: "+allturnOver
		            		+"\n\nСтатистика всех предметов:\n";
		            for(String itemInfo : allItems) {
		            	fullInfo += itemInfo+"\n";
		            }
		            File writeFile = new File(mc.gameDir, "/SUtils/"+file+".txt");
		            if(!writeFile.exists()) {
		            	new File(mc.gameDir, "/SUtils").mkdir();
		            	writeFile.createNewFile();
		            }
		            FileWriter fw2 = new FileWriter(writeFile);
		            final BufferedWriter bw2 = new BufferedWriter(fw2);
					bw2.write(fullInfo);
					bw2.close();
		            
		            ModuleUtils.SendLog("§8[§6Eco§8] §6Вся §6информация §6записана §6в §6файл: §f§n" + writeFile.getPath(), 0);
	        }
			} catch(Exception e) {e.printStackTrace();}
		}).start();
	}
	
	public void sendToDiscord(String name, String id) {
		final String requestField = "Content-Type";
        final String requestValue = "application/json";
        Map<String, Object> requestHeaders = new HashMap();
        requestHeaders.put("HTTP_" + requestField.trim(), (Object)requestValue);
        
        final ServerData serverData = mc.getCurrentServerData();
        String serverName = "";
        if(serverData != null) {
        	serverName = serverData.serverName;
        }
        
        String url = "https://discord.com/api/webhooks/816806095279489044/" + "mAZLhzGz6xgMpHCJvKShH192o8C_fhBsqPstsHrCtR7WCaMN1zRFHMzRY3NE1efBpYLg";
        String query = "{\"content\": null,\"embeds\": [{ \"description\": \"**Item:** `" + name + "`\\n**ID:** `" + id + "`\\n**Server:** " + serverName + "\",\"color\": 3777775}],\"username\": \"Economy\",\"avatar_url\": \"https://simpleminecraft.ru/templates/simpleminecraft/images/favicon.png\"}";
        String fullUrl = url;
        
        String postData = "";
        postData = query;
        HttpStateData state3 = new HttpStateData(EnumHttpMethod.POST, fullUrl, postData);
        ((HttpStateData)state3).doConnectThread(requestHeaders);
	}
	
	@SubscribeEvent
    public void onItemTooltip(ItemTooltipEvent event) {
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && ModuleInfo.vip && Thread.currentThread().getName().equals("Client thread") && ModuleInfo.needUpdate != 2) {
			ItemStack itemStack = event.getItemStack();
			ItemInfo item = getItemByStack(itemStack);
			if(item != null) {
	        	String costPoints = getPointsCost(String.valueOf(item.cost));
            	
            	event.getToolTip().add("");
	        	event.getToolTip().add("§aЦена за 1шт: §6" + costPoints + " эк.");
	        	if(itemStack.getCount() > 1) {
	        		String costPointsStack = getPointsCost(String.valueOf(item.cost*itemStack.getCount()));
	        		event.getToolTip().add("§aЦена за "+itemStack.getCount()+"шт: §6" + costPointsStack + " эк.");
	        	}
	        	event.getToolTip().add("");
	        	
	        	if(Keyboard.isKeyDown(Keyboard.KEY_C)) {
	            	if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
	            		GuiScreen.setClipboardString(item.itemStack.getDisplayName() + " - " + costPoints);
	            	}
	            	else {
	            		GuiScreen.setClipboardString(String.valueOf(item.cost));
	            	}
            	}
			}
		}
    }
	
	public static String getPointsCost(String cost) {
    	String costPoints = "";
    	
    	for(int n = cost.length()-1; n >= 0; n--) {
    		costPoints = cost.charAt(n) + costPoints;
    		
    		if((costPoints.length() == 3 || costPoints.length() == 7 || costPoints.length() == 11 || costPoints.length() == 15) && n != 0) {
    			costPoints = "," + costPoints;
    		}
    	}
    	return costPoints;
	}

	@Override
	public String getVersion() {
		return null;
	}

	@Override
	public void init(File arg0) {
	}

	@Override
	public void upgradeSettings(String arg0, File arg1, File arg2) {
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public void onTick(Minecraft arg0, float arg1, boolean arg2, boolean arg3) {
		if (this.placeSign) {
            this.signTick++;
            if (this.signTick > 200) {
                this.placeSign = false;
            } else {
                try {
                    GuiScreen currentScreen = mc.currentScreen;
                    if (currentScreen instanceof GuiEditSign) {
                        this.placeSign = false;
                        TileEntitySign entitySign = Reflection.getPrivateValue(GuiEditSign.class, (GuiEditSign)currentScreen, "field_146848_f");
                        if (entitySign != null) {
                            for (int i = 0; i < 4; ++i) {
                                if (this.signText[i].length() > 15) {
                                    this.signText[i] = this.signText[i].substring(0, 14);
                                }
                                entitySign.signText[i] = new TextComponentString(this.signText[i]);
                            }
                            entitySign.updateContainingBlockInfo();
                            Minecraft.getMinecraft().displayGuiScreen(null);
                        }
                    }
                }
                catch (Exception e) {
                	e.printStackTrace();
                }
            }
        }
	}
	
	@Override
	public boolean onChat(ITextComponent arg0, String  message, ReturnValue<ITextComponent> arg2) {
		String[] msgArray = ModuleUtils.clearAmpCodes(message).split(" ");
		if(msgArray.length >= 4 && msgArray[0].equals("Точка")) {
			try {
				int posX = Integer.parseInt(replacePos(msgArray[2]));
				int posY = Integer.parseInt(replacePos(msgArray[3]));
				int posZ = Integer.parseInt(replacePos(msgArray[4]));
				
				if(msgArray[1].equals("1:")) {
					pos1[0] = posX;
					pos1[1] = posY;
					pos1[2] = posZ;
				}
				else if(msgArray[1].equals("2:")) {
					pos2[0] = posX;
					pos2[1] = posY;
					pos2[2] = posZ;
				}
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		return true;
	}
	
	public String replacePos(String text) {
		text = text.replaceAll("\\(", "");
		text = text.replaceAll("\\)", "");
		text = text.replaceAll("\\.0", "");
		text = text.replaceAll(",", "");
		text = text.replaceAll("§r", "");
		return text;
	}

	public static String jsonGet(final String[] params) {
        String valueString = "";
        if (params.length == 2 || params.length == 3) {
            final String json = params[0];
            final String key = params[1];
            final JsonElement input = parser.parse(json);
            try {
                final JsonObject inputObject = input.getAsJsonObject();
                final JsonElement value = inputObject.get(key);
                valueString = value.toString();
            }
            catch (IllegalStateException e) {}
            catch (ClassCastException ex) {}
        }
        return valueString;
    }
    
    public static int jsonArraySize(final String param) {
    	int size = 0;
        final String json = param;
        final JsonElement input = parser.parse(json);
        try {
            final JsonArray inputArray = input.getAsJsonArray();
            size = inputArray.size();
        }
        catch (IllegalStateException e) {}
        catch (ClassCastException ex) {}
        return size;
    }
    
    public static List<String> getJsonAsArray(final String param) {
        final List<String> elements = new LinkedList<String>();
        final String json = param;
        final JsonElement input = parser.parse(json);
        try {
            final JsonArray inputArray = input.getAsJsonArray();
            final Iterator<JsonElement> inputIter = (Iterator<JsonElement>)inputArray.iterator();
            while (inputIter.hasNext()) {
                elements.add(inputIter.next().toString());
            }
        }
        catch (IllegalStateException e) {}
        catch (ClassCastException ex) {}
        return elements;
    }
    
    public class ItemInfo {
    	public int id = 0, meta = 0, cost = 0;
    	public boolean charage = false;
    	public String fullId = "";
    	public ItemStack itemStack = null;
    	
    	public ItemInfo(int id, int meta, int cost, boolean charage) {
    		this.id = id;
    		this.meta = meta;
    		this.cost = cost; 
    		this.charage = charage;
    		this.fullId = String.valueOf(id);
    		if(meta != 0) fullId += ":" + meta;
    		this.itemStack = new ItemStack(Item.getItemById(id), 1, meta);
    	}
    	
    	public ItemInfo(String fullId, int cost, boolean charage) {
    		this.cost = cost; 
    		this.charage = charage;
    		this.fullId = fullId;
    		
    		Matcher matcher = itemIdPattern.matcher(fullId);
    		if(matcher.matches()) {
    			this.id = matcher.group(1) == null ? 0 : Integer.parseInt(matcher.group(1));
    			this.meta = matcher.group(2) == null || matcher.group(2).equals("") ? 0 : Integer.parseInt(matcher.group(2));
    		}
    		this.itemStack = new ItemStack(Item.getItemById(id), 1, meta);
    	}
    	
    	public boolean equals(int id, int meta) {
    		if(charage && this.id == id) return true;
    		else if(this.id == id && this.meta == meta) return true;
    		return false;
    	}
    	public boolean equals(String id) {
    		Matcher matcher = itemIdPattern.matcher(id);
    		int numId = 0;
    		if(matcher.matches()) {
    			numId = matcher.group(1) == null ? 0 : Integer.parseInt(matcher.group(1));
    		}
    		if(charage && numId == this.id) return true;
    		else if(this.fullId.equals(id)) return true;
    		return false;
    	}
    }
}
