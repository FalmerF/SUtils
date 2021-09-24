package net.smb.moderutils.actions;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.lwjgl.opengl.GL11;

import com.mumfrey.liteloader.ChatFilter;
import com.mumfrey.liteloader.Tickable;
import com.mumfrey.liteloader.core.LiteLoaderEventBroker.ReturnValue;

import net.eq2online.console.Log;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.smb.moderutils.ModuleUtils;
import net.smb.moderutils.WorldRender;
import net.smb.moderutils.jchat.ClickEvent;
import net.smb.moderutils.jchat.JsonMessageBuilder;

public class Count implements ChatFilter {
    public int[] pos1 = new int[3];
    public int[] pos2 = new int[3];
    
    public static Count instance = new Count();
    private Map<Integer, List<int[]>> blocks = new HashMap<Integer, List<int[]>>();
    public int selecteBLock = -1;
	
	public void mainCheck(boolean debug) {
		new Thread(() -> {
			ModuleUtils.SendMessage("//size");
			try {
	            Thread.sleep((long) (1000));
	        } catch(Exception e) {}
			Minecraft mc = Minecraft.getMinecraft();
			final WorldClient theWorld = mc.world;
			final EntityPlayerSP thePlayer = mc.player;
	        if (theWorld != null && thePlayer != null) {
	            int xPos = pos1[0];
	            int yPos = pos1[1];
	            int zPos = pos1[2];
	            int xPos2 = pos2[0];
	            int yPos2 = pos2[1];
	            int zPos2 = pos2[2];

	            final ServerData serverData = Minecraft.getMinecraft().getCurrentServerData();
	            String serverName = "TechnoMagic";
		        if(serverData != null) {
		        	serverName = serverData.serverName.split(" ")[0];
		        }
		        
		        
		        String[] value = ModuleUtils.getDataString(serverName).split("\\|");
		        String ids = value[0];
		        String limits = value[1];
		        String names = value[2];
	            
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
	            Block block = null;
	            int blockId = 0;
	            int blockDamage = 0;
	            String blockName= "";
	            
	            String[] ids_array = ids.split(" ");
	            int[] blocks_num = new int[ids_array.length];
	            blocks.clear();
	            selecteBLock = -1;
	            String[] ids_num_max = limits.split(" ");
	            Pattern pattern = Pattern.compile("([\\d]+)(\\((def=)([\\d]+)\\))");
	            
	            for(int xInt = xPos; xInt <= xPos2;xInt++) {
	            	for(int zInt = zPos; zInt <= zPos2;zInt++) {
	            		for(int yInt = yPos; yInt <= yPos2;yInt++) {
	            			try {
	            			TileEntity tile = theWorld.getTileEntity(new BlockPos(xInt, yInt, zInt));
	            			block = theWorld.getBlockState(new BlockPos(xInt, yInt, zInt)).getBlock();
	            			blockId = Block.getIdFromBlock(block); //Block.REGISTRY.getIDForObject(block);
	            			Log.info("Pos - "+xInt+" "+yInt+" "+zInt);
	            			Log.info("tile == null - " + (tile == null));
	            			blockDamage = block.getMetaFromState(theWorld.getBlockState(new BlockPos(xInt, yInt, zInt)));
	            			Log.info("blockId: " + blockId +"  blockDamage: " + blockDamage);
	            			if(blockDamage != 0) {
	            				blockName=blockId + ":" + blockDamage;
	            			}
	            			else {
	            				blockName=String.valueOf(blockId);
	            			}
	            			if(debug) {
	            				ModuleUtils.SendLog("[" + xInt + " " + yInt + " " + zInt + "] id: " + blockName, 0);
	            			}
	            			
	            			for(int i = 0; i < ids_array.length;i++) {
	            				String[] ids2_array = ids_array[i].split(",");
	            				for(String id2 : ids2_array) {
	            					try {
	            						if(id2.startsWith(blockName)) {
                                            Matcher matcher = pattern.matcher(id2);
                                            if(matcher.find()) {
                                                if(matcher.group(1).equals(String.valueOf(blockId))) {
                                                    NBTTagCompound tag = new NBTTagCompound();
                                                    tile.writeToNBT(tag);
                                                    for(int t = 0; t < 6; t++) {
                                                        try {
                                                            if(this.GetDefNbt(tag, "def:" + t).equals(matcher.group(4)+"s")) {
                                                                blocks_num[i]++;
                                                                addBlock(i, xInt, yInt, zInt);
                                                            }
                                                        } catch(Exception e) {}
                                                    }
                                                }
                                            }
                                            else if(id2.equals(blockName)) {
                                            	blocks_num[i]++;
                                            	addBlock(i, xInt, yInt, zInt);
                                                break;
                                            }
                                        }
	            					} catch(Exception e) {}
	            				}
	            			}
	            			} catch(Exception e) {}
	                    }
	                }
	            }
	          
	            GuiNewChat newChat = Minecraft.getMinecraft().ingameGUI.getChatGUI();
	            
	            newChat.printChatMessageWithOptionalDeletion(new TextComponentString("§8= = = = = = = = = = = [ §6LIMITS §8] = = = = = = = = = = ="), 1500000);
	            newChat.printChatMessageWithOptionalDeletion(new TextComponentString(" "), 1500001);
	            
	            int idsCount = Math.min(ids_array.length, 50);
	            
	            String[] namesArray = names.split(",");
	            int blocksFound = 0;
	            
	            for(int i = 0; i < idsCount;i++) {
	            	String blocks_num_string = "";
	            	
	            	String[] first_id = ids_array[i].split(",");
	            	String[] normal_id = first_id[0].split(":");
	            	String itemName = "";
	            	
	            	if(i < namesArray.length && !namesArray[i].equals("")) itemName = ModuleUtils.convertAmpCodes(namesArray[i]);
	            	
	            	if(i < ids_num_max.length && !ids_num_max[i].equals("")) {
	            		int blocksCount = Integer.parseInt(ids_num_max[i]);
	            		if(blocks_num[i] <= 0) continue;
	        			if(blocks_num[i] > blocksCount) {
	        				blocks_num_string = "§c" + blocks_num[i] + " / " + ids_num_max[i];
	        			}
	        			else {
	        				blocks_num_string = "§a" + blocks_num[i];
	        			}
	            	}
	            	else {
	            		blocks_num_string = "§b" + blocks_num[i];
	            	}
	            	
	            	JsonMessageBuilder builder = new JsonMessageBuilder()
				    		.newPart()
				    		.setText("§6 • §b" + itemName + " §6§l>> " + blocks_num_string)
				    		.setHoverText("/bsel " + i)
				    		.setClick(ClickEvent.Type.RUN_COMMAND, "/bsel " + i)
				    		.end();
	            	ModuleUtils.SendLog(builder, 1500002+i);
	            	//newChat.printChatMessageWithOptionalDeletion(new ChatComponentText("§6 • §b" + itemName + " §6§l>> " + blocks_num_string), 1500002+i);
	            	blocksFound++;
				}
	            if(blocksFound == 0) ModuleUtils.SendLog("§6 • §aБлоков не найдено", 1500002);
	            newChat.printChatMessageWithOptionalDeletion(new TextComponentString(" "), 1500201);
	            JsonMessageBuilder builder = new JsonMessageBuilder()
			    		.newPart()
			    		.setText("§8= = = = = = = = = = [ §6ОЧИСТИТЬ §8] = = = = = = = = = =")
			    		.setHoverText("/checkclear")
			    		.setClick(ClickEvent.Type.RUN_COMMAND, "/checkclear")
			    		.end();
						
				ModuleUtils.SendLog(builder, 1500202);
	        }
		}).start();
	}
	
	public void addBlock(int id, int x, int y, int z) {
		if(blocks.get(id) == null) {
			blocks.put(id, new ArrayList<int[]>());
		}
		blocks.get(id).add(new int[] {x, y, z});
	}
	
	public String GetDefNbt(NBTTagCompound tag, String tagName) {
		if(!tag.getString("Damage").equals("")) return tag.getString("Damage");
		else {
			NBTTagCompound defTag = (NBTTagCompound) tag.getTag(tagName);
			if(defTag != null) return defTag.getString("Damage");
			
			Matcher matcher = Pattern.compile(tagName + ":\\{([\\w:,]+)\\}").matcher(String.valueOf(tag));
			if(matcher.find()) {
				matcher = Pattern.compile("Damage:([\\w]+)").matcher(matcher.group(1));
				if(matcher.find()) return matcher.group(1);
			}
		}
		return "";
	}
	
	public void ClearCheckMessages() {
		selecteBLock = -1;
	}
	
	public String replacePos(String text) {
		text = text.replaceAll("\\(", "");
		text = text.replaceAll("\\)", "");
		text = text.replaceAll("\\.0", "");
		text = text.replaceAll(",", "");
		text = text.replaceAll("§r", "");
		return text;
	}

	@Override
	public String getVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(File arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void upgradeSettings(String arg0, File arg1, File arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onChat(ITextComponent packet, String message, ReturnValue<ITextComponent> arg2) {
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
	
	@SubscribeEvent 
	public void worldRender(RenderWorldLastEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		int pX = (int)mc.player.posX;
		int pY = (int)mc.player.posY;
		int pZ = (int)mc.player.posZ;
        if(selecteBLock != -1 && blocks.size() > 0 && blocks.get(selecteBLock) != null) {
        	mc.entityRenderer.disableLightmap();
        	List<int[]> b = blocks.get(selecteBLock);
        	for(int[] cords : b) {
        		if(distance(cords[0], cords[1], cords[2], pX, pY, pZ) < 200) {
	        		try {
	                    WorldRender.drawOutlinedEspBlock(cords[0], cords[1], cords[2], 1.0f, 1.0f, 1.0f, 1.0F, 0.1F);
	                    WorldRender.drawEspBlock(cords[0], cords[1], cords[2], 0.6f, 0.1f, 0.1f, 0.4F, 0.1F);
	        		} catch(Exception e) {}
        		}
        	}
        	mc.entityRenderer.enableLightmap();
        	GL11.glColor4f(1, 1, 1, 1);
        }
    }
	
	public static float distance(float x1, float y1, float z1, float x2, float y2, float z2) {
		return (float) Math.pow(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2) + Math.pow(z2 - z1, 2), 0.5f);
	}
}

