package net.smb.sutils.modules;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mumfrey.liteloader.ChatFilter;

import acs.tabbychat.core.TCChatLine;
import acs.tabbychat.core.TabbyChat;
import net.eq2online.macros.compatibility.PrivateFields;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.smb.sutils.ModuleInfo;
import net.smb.sutils.VariableProviderModer;
import net.smb.sutils.jchat.ClickEvent;
import net.smb.sutils.jchat.ClickEvent.Type;
import net.smb.sutils.jchat.JsonMessageBuilder;
import net.smb.sutils.utils.Utils;

public class ChatFilterModule implements ChatFilter {
	public static ChatFilterModule instance = new ChatFilterModule();
	
	public boolean statsFilter = false;
	public boolean featherInfo = false;
	public boolean seenCheck = false;
	public boolean guardian = false;
	
	public int clearList = 0;
	public int featherMessagesInfo = 0;
	public int personalNotifyTimer = 0;
	public int featherUsersCount;
	public int osModers = 0;
	public List<String> rgGetInfo = new ArrayList<String>();
	public List<String> guardianList = new ArrayList<String>();
	public static List<String> groups = Arrays.asList("Игроки:", "VIP:", "Premium:", "Grand:", "Ultra:", "Младшие модераторы:", "Модераторы:", "Главный модератор:", "Помощники администратора:", "Администратор:");

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
	
	String upH, upM, upS;
	
	void getUpTime(String arg1, String arg2) {
		if(arg2.startsWith("час")) upH = arg1;
		else if(arg2.startsWith("мин")) upM = arg1;
		else if(arg2.startsWith("сек")) upS = arg1;
	}

	@Override
	public boolean onChat(S02PacketChat packet, IChatComponent chat, String message) {
		String originalMessage = message;
		message = Utils.clearAmpCodes(message);
		String[] args = message.split(" ");
		String newMessage = message;
		
		if(ModuleInfo.needUpdate == 2) return true;
		
		if(message.startsWith(" #") && SettingsModule.getBool("guardian") && this.guardian) {
			this.guardianList.add(message);
			newMessage = "";
			return false;
		}
		else if((message.equals("Отчет заверешен.") || message.equals("Отчет завершен.")) && SettingsModule.getBool("guardian")) {
			this.guardian = false;
			VariableProviderModer.WriteGuardian();
			newMessage = "";
		}
		
		try {
			if(args.length > 0) {
				// Clear list Moder
				if(args[0].equals("Moder:")) {
					String[] moders = message.replaceAll("Moder: ", "").replaceAll(",", "").replaceAll("\\[Скрыт\\]", "").split(" ");
					if(ModuleInfo.permission < 2) {
						for(String m : moders){
							if(ModuleInfo.playerName.equals(m)) {
								ModuleInfo.permission = 2;
								break;
							}
						}
					}
					if(clearList > 0) {
						newMessage = "";
						clearList--;
					}
				}
				// Clear list LowModer
				else if(args[0].equals("Lowmoder:")) {
					String[] moders = message.replaceAll("Lowmoder: ", "").replaceAll(",", "").replaceAll("\\[Скрыт\\]", "").split(" ");
					if(ModuleInfo.permission < 1) {
						for(String m : moders){
							if(ModuleInfo.playerName.equals(m)) {
								ModuleInfo.permission = 1;
								break;
							}
						}
					}
					if(clearList > 0) {
						newMessage = "";
						clearList--;
					}
				}
				// Clear list HeadModer
				else if(args[0].equals("Headmoder:")) {
					String[] moders = message.replaceAll("Headmoder: ", "").replaceAll(",", "").replaceAll("\\[Скрыт\\]", "").split(" ");
					if(ModuleInfo.permission < 2) {
						for(String m : moders){
							if(ModuleInfo.playerName.equals(m)) {
								ModuleInfo.permission = 3;
								break;
							}
						}
					}
					if(clearList > 0) {
						newMessage = "";
						clearList--;
					}
				}
				// Clear list Adminhelper
				else if(args[0].equals("Adminhelper:")) {
					String[] moders = message.replaceAll("Adminhelper: ", "").replaceAll(",", "").replaceAll("\\[Скрыт\\]", "").split(" ");
					if(ModuleInfo.permission < 2) {
						for(String m : moders){
							if(ModuleInfo.playerName.equals(m)) {
								ModuleInfo.permission = 3;
								break;
							}
						}
					}
					if(clearList > 0) {
						newMessage = "";
						clearList--;
					}
				}
				// Clear Other list messages
				else if(clearList > 0 && args[0].equals("Сейчас") && args[2].equals("из") && args[4].equals("игроков")) newMessage = "";
				else if(clearList > 0 && message.equals("Ошибка: В данной группе нет игроков в игре!")) {
					newMessage = "";
					clearList--;
				}
				
				for(String group : groups) {
					if(message.startsWith(group)) {
						JsonMessageBuilder builder = new JsonMessageBuilder()
								.newPart()
					    		.setText("§6"+group+" ")
					    		.end();
						String[] nicks = message.replace(group+" ", "").split(", ");
						for(int i = 0; i < nicks.length; i++) {
							String nick = nicks[i];
							if(i == nicks.length-1) {
								builder
								.newPart()
					    		.setText("§r"+nick)
					    		.setClick(ClickEvent.Type.RUN_COMMAND, "/pi "+nick.replaceAll("\\[Скрыт\\]", ""))
					    		.setHoverText("§aСписок действий")
					    		.end();
							}
							else {
								builder
								.newPart()
					    		.setText("§r"+nick+", ")
					    		.setClick(ClickEvent.Type.RUN_COMMAND, "/pi "+nick.replaceAll("\\[Скрыт\\]", ""))
					    		.setHoverText("§aСписок действий")
					    		.end();
							}
						}
						newMessage = "";
						Utils.SendLog(builder, 0);
						break;
					}
				}
			}
			
			// Get Stats Balance, TPS, OS
			if(statsFilter && args.length >= 2) {
				if(args[0].equals("[Банк]") && args[1].equals("На") && args[2].equals("вашем") && args[3].equals("балансе:")) {
					ModuleInfo.playerBalance = args[4];
					newMessage = "";
				}
				if(args[0].equals("[Авторизация]") && args[1].equals("Модератор") && args[4].equals("дату")) {
					String h = "", m = "", color = "§c";
					int hInt = 0;
					if(args[10].equals("минут.")) m = args[9] + "m";
					else if(args.length > 11) {
						h = args[9] + "h";
						hInt = Integer.parseInt(args[9]);
						m = args[11] + "m";
					}
					else {
						h = args[9] + "h";
						hInt = Integer.parseInt(args[9]);
					}
					if(hInt >= 4) color = "§a";
					else if(hInt >= 2) color = "§e";
					ModuleInfo.playerOS = color + h + " " + m;
					newMessage = "";
					statsFilter = false;
				}
				if(SettingsModule.getBool("tpsFilter")) {
					if(args[0].equals("Аптайм:")) {
						newMessage = "";
						upH = "0";
						upM = "0";
						upS = "0";
						if(args.length >= 3) getUpTime(args[1], args[2]);
						if(args.length >= 5) getUpTime(args[3], args[4]);
						if(args.length >= 7) getUpTime(args[5], args[6]);
						ModuleInfo.uptime = upH + "h " + upM + "m " + upS + "s";
					}
					if((args[0].equals("Максимум") && args[1].equals("памяти:")) || (args[0].equals("Минимум") && args[1].equals("памяти:"))) newMessage = "";
					if(args[0].equals("Выделено") && args[1].equals("памяти:")) newMessage = "";
					if(args[0].equals("World") || (args[0].equals("Свободной") && args[1].equals("памяти:")) || (args[0].equals("The") && args[1].equals("End"))) newMessage = "";
					if(args[0].equals("Nether")) {
						newMessage = "";
					}
					if(args[0].equals("TPS")) {
						String color = "§a";
						float tps = Float.parseFloat(args[2]);
						if(tps < 18) color = "§e";
						else if(tps < 15) color = "§c";
						ModuleInfo.serverTPS = color + args[2];
						newMessage = "";
					}
				}
			}
			
			
			// Change color moder chat
			if(SettingsModule.getBool("chatFilterModer") && ModuleInfo.permission > 0) {
				if(args.length >= 3 && args[0].equals("[Наказания]") && args.length >= 3) {
					if(args[1].equals("Активные") && args[2].equals("блокировки")) {
						newMessage = "§8[§cНаказания§8] §6Активные блокировки §c" + args[3].replaceAll(":", "") + "§6:";
					}
					if(args[1].equals("Активные") && args[2].equals("предупреждения")) {
						newMessage = "§8[§cНаказания§8] §6Активные предупреждения §c" + args[3].replaceAll(":", "") + "§6:";
					}
					if(args[1].equals("Игроку") && args[3].equals("выдано") && args[4].equals("предупреждение.")) {
						newMessage = "§8[§cНаказания§8] §6Игроку §c" + args[2] +" §6выдано §6предупреждение.";
					}
					if(args[1].equals("Игроку") && args[3].equals("заблокирован") && args[4].equals("чат.")) {
						newMessage = "§8[§cНаказания§8] §6Игроку §c" + args[2] +" §6заблокирован §6чат.";
					}
					if(args[1].equals("Игрок") && args[3].equals("заблокирован") && args[5].equals("сервере.")) {
						newMessage = "§8[§cНаказания§8] §6Игрок §c" + args[2] +" §6заблокирован §6на §6сервере.";
					}
					if(args[1].equals("Игроку") && args[3].equals("разблокирован") && args[4].equals("чат.")) {
						newMessage = "§8[§cНаказания§8] §6Игроку §c" + args[2] +" §6разблокирован §6чат.";
					}
					if(args[1].equals("Все") && args[2].equals("варны") && args[5].equals("очищены.")) {
						newMessage = "§8[§cНаказания§8] §6Все варны игрока §c" + args[4] +" §6очищены.";
					}
					if(args[1].equals("Игрок") && args[3].equals("разблокирован.")) {
						newMessage = "§8[§cНаказания§8] §6Игрок §c" + args[2] +" §6разблокирован.";
					}
				}
				else if (args.length >= 2) {
					if(args[0].equals("Выдал:")) {
						newMessage = "§6 • Выдал: §c" + args[1];
					}
					if(args[0].equals("Заблокировал:")) {
						newMessage = "§6 • Заблокировал: §c" + args[1];
					}
					if(args[0].equals("Причина:")) {
						String reason = "";
						for(int i = 1; i < args.length; i++) reason += args[i] + "§c ";
						newMessage = "§6 • Причина: §c" + reason;
					}
					if(args[0].equals("Дата") && args[1].equals("разблокировки:")) {
						String date = "";
						for(int i = 2; i < args.length; i++) date += args[i] + "§c ";
						newMessage = "§6 • Дата разблокировки: §c" + date;
					}
					
					if(args[1].equals("*") && args[2].equals("Отсутствуют.")) {
						newMessage = "§6 • Отсутствуют.";
					}
					if(args.length >= 3 && args[2].equals("Предупреждение") && args[3].equals("от")) {
						String reason = "";
						for(int i = 6; i < args.length; i++) reason += args[i] + "§c ";
						newMessage = "§6 • " + args[1] + " Предупреждение от §c" + args[4].replaceAll("\\.", "") + "§6. Причина: §c" + reason;
					}
					if(args.length >= 3 && args[2].equals("Блокировка") && args[3].equals("чата")) {
						String reason = "";
						int i = 7;
						for(; i < args.length; i++) {
							if(args[i].equals("Дата")) break;
							reason += args[i] + "§c ";
						}
						String date = args[i+2];
						if(args.length > i+3) date += " §c" + args[i+3];
						newMessage = "§6 • Блокировка чата от §c" + args[5].replaceAll("\\.", "") + "§6. Причина: §c" + reason + " §6Дата окончания: §c" + date;
					}
					if(args.length >= 3 && args[2].equals("Блокировка") && args[3].equals("входа")) {
						String reason = "";
						int i = 9;
						for(; i < args.length; i++) {
							if(args[i].equals("Дата")) break;
							reason += args[i] + "§c ";
						}
						String date = args[i+2];
						if(args.length > i+3) date += " §c" + args[i+3];
						newMessage = "§6 • Блокировка входа на сервер от §c" + args[7].replaceAll("\\.", "") + "§6. Причина: §c" + reason + " §6Дата окончания: §c" + date;
					}
				}
			}
			
			if((message.equals("У Вас нет прав для выполнения данной команды.") || message.equals("У Вас недостаточно прав.")) && SettingsModule.getBool("noPermissionFilter")) newMessage = "";
			
			else if((message.equals("Вам запрещено взаимодействовать с блоками в этом регионе!") || message.equals("Вы не можете разбивать блоки на этой территории.")  || message.equals("Вы не можете выбрасывать предметы на данной территории.")) && SettingsModule.getBool("noRgPermissionFilter")) newMessage = "";
			
			else if((message.equals("У Вас выключен PvP.") || message.equals("Включить можно командой: /pvp")) && SettingsModule.getBool("pvpFilter")) newMessage = "";
			
			else if(message.equals("Вам нехватает знаний для изучения этого.")&& SettingsModule.getBool("knowledgeFilter")) newMessage = "";
			
			else if(message.equals("Предупреждение: критический урон !!!")&& SettingsModule.getBool("criticalDamageFilter")) newMessage = "";
			
			else if((message.equals("[Чистка] Чистка выброшенных предметов и сущностей пройдет через 3 минуты.") || message.equals("[Чистка] Чистка выброшенных предметов и сущностей пройдет через 1 минуту."))&& SettingsModule.getBool("clearingFilter")) newMessage = "";
			
			else if((message.equals("На указателе нет блока (или он слишком далеко)!")||message.equals("Там ничего нет!"))&& SettingsModule.getBool("noHitBlockFilter")) newMessage = "";
			
			else if(message.equals("Бедрок запрещён.") && SettingsModule.getBool("bedrokFilter")) newMessage = "";
			
			else if(args.length >= 3 && args[0].equals("Инвентарь") && args[1].equals("спавнера") && args[2].equals("переполнен!") && SettingsModule.getBool("spawnerFilter")) newMessage = "";
			
			if(SettingsModule.getBool("ifsFilter") && args.length >= 2) {
				if(args.length >= 3 && args[0].equals("[Магазин]") && args[1].equals("Предмет") && args[2].equals("будет")) newMessage = "";
				else if(args[0].equals("Название:")) newMessage = "";
				else if(args[0].equals("Данные") && args[1].equals("предмета:")) newMessage = "";
				else if(args.length >= 4 && args[0].equals("[Магазин]") && args[1].equals("Информация") && args[2].equals("о") && args[3].equals("продаваемом")) newMessage = "";
			}
			
			if(SettingsModule.getBool("tradingSetFilter") && args.length >= 3) {
				if(message.equals("[Магазин] Торговая точка успешно создана!")) newMessage = "";
				else if(args[1].equals("*Налог*") && args[2].equals("Уплачена")) newMessage = "";
			}
			
			if(SettingsModule.getBool("transactionFilter") && args.length >= 3) {
				if(message.equals("[Магазин] Уведомление о транзакции:")) newMessage = "";
				else if(args[1].equals("*Продажа*") && args[2].equals("Игрок")) newMessage = "";
				else if(args[1].equals("*Покупка*") && args[3].equals("Предмет")) newMessage = "";
			}
			
			if(args[0].equals(">>>") && SettingsModule.getBool("hintsFilter")) newMessage = "";
			
			if(args.length >= 4 && args[0].equals("Вы") && args[1].equals("стоите") && args[2].equals("в") && args[3].equals("нескольких") && featherInfo) newMessage = "";
			if(args.length >= 3 && args[0].equals("Вы") && args[1].equals("в:") && featherInfo) {
				Utils.SendMessage("/rg i " + args[2].replaceAll(",", ""));
				for(int i = 3; i < args.length; i++) {
					this.rgGetInfo.add("/rg i " + args[i].replaceAll(",", ""));
				}
				newMessage = "";
			}
			
			if((args[0].equals("Флаги:")) && featherInfo) newMessage = "";
			if(args.length >= 2 && args[0].equals("Участники:") && featherInfo) {
				if(args[1].equals("(нет")) {
					newMessage = "§aУчастники: §c(нет).";
				}
				else {
					String members = "";
					for(int i = 1; i < args.length; i++) members += args[i] + " §e";
					newMessage = "§aУчастники: §e" + members;
				}
			}
			if(args.length >= 2 && args[0].equals("Владельцы:") && featherInfo) {
				for(int i = 1; i < args.length; i++) {
					if(!args[i].equals("(нет") && !args[i].equals("владельцев)")) {
						featherUsersCount++;
						Utils.SendMessage("/seen " + args[i].replaceAll(",", ""));
					}
				}
				newMessage = "";
			}
			if(args.length >= 2 && ((args[1].equals("-") && args[2].equals("IP")) || (args[0].equals("Ошибка:") && args[1].equals("null"))) && featherInfo) {
				newMessage = "";
				
				featherUsersCount--;
				if(featherUsersCount <= 0) {
					Utils.SendLog("§7===================================================", 0);
					featherMessagesInfo = 2;
					if(this.rgGetInfo.size() > 0) {
						Utils.SendMessage(rgGetInfo.get(0));
						rgGetInfo.remove(0);
					}
					else this.featherInfo = false;
				}
			}
			if(args.length >= 3 && args[1].equals("-") && args[2].equals("Местоположение:") && (featherInfo || featherMessagesInfo > 0)) {
				newMessage = "";
			}
			if(args.length >= 2 && args[0].equals("Границы:") && featherInfo) newMessage = "";
			if(args.length >= 3 && args[1].equals("Информация") && args[2].equals("о")&& args[3].equals("регионе") && featherInfo) {
				newMessage = "§7===================================================";
			}
			if(args.length >= 2 && args[0].equals("Регион:") && featherInfo) {
				newMessage = "§aИнформация о регионе: §e" + args[1];
			}
			if(args.length >= 3 && args[0].equals("Игрок") && (args[2].equals("онлайн") || args[2].equals("оффлайн")) && featherInfo) {
				if(args[2].equals("онлайн")) {
					newMessage = "§e" + args[1] + " (§aонлайн§e).";
				}
				else {
					String online = "";
					for(int i = 4; i < args.length; i++) online += args[i].replaceAll("\\.", "") + " §c";
					newMessage = "§e" + args[1] + " §e(§c" + online + "§e).";
				}
			}
			
			if(args.length >= 2 && args[0].equals("Участники:") && !featherInfo && !args[1].equals("(нет")) {
				JsonMessageBuilder builder = new JsonMessageBuilder().newPart()
						.setText("§9Участники:")
						.end();
				for(int i = 1; i < args.length; i++) {
					builder.newPart()
					.setText(" §e" + args[i])
					.setHoverText("/pi " + args[i].replaceAll(",", ""))
					.setClick(Type.RUN_COMMAND, "/pi " + args[i].replaceAll(",", ""))
					.end();
				}
				Utils.SendLog(builder, 0);
				newMessage = "";
			}
			if(args.length >= 2 && args[0].equals("Владельцы:") && !featherInfo && !args[1].equals("(нет")) {
				JsonMessageBuilder builder = new JsonMessageBuilder().newPart()
						.setText("§9Владельцы:")
						.end();
				for(int i = 1; i < args.length; i++) {
					builder.newPart()
					.setText(" §e" + args[i])
					.setHoverText("/pi " + args[i].replaceAll(",", ""))
					.setClick(Type.RUN_COMMAND, "/pi " + args[i].replaceAll(",", ""))
					.end();
				}
				Utils.SendLog(builder, 0);
				newMessage = "";
			}
			
			// Notify
			Pattern pattern = Pattern.compile("^\\[[\\w-]{3,16}\\s->\\sЯ\\]\\s.*");
			Matcher matcher = pattern.matcher(message);
			if(matcher.find()) {
				if(SettingsModule.getBool("privateNotify") && !SettingsModule.getBool("allNotify")) Utils.sendNotify(message);
			}
			
			// Color chat filter
			String playerName = "";
			boolean messageFilterd = false;
			filter:
			{
				Matcher m = Pattern.compile("^(\\[\\w{1}\\])?\\s?(\\[([^\\[\\]]+)\\])?\\s?([\\w-]+)\\s»\\s(.*)$").matcher(message);
				Matcher warpAd = Pattern.compile("^(\\[T\\])\\s(\\[[^\\[\\]]+\\])\\s»\\s(.*)$").matcher(message);
				if(m.matches()) {
					int chatType = 0;
					String prefixColor = "§d";
				    String chatColor = Utils.convertAmpCodes(SettingsModule.getString("chatFilterColor")), tChatColor = Utils.convertAmpCodes(SettingsModule.getString("chatFilterTColor"));
				    String mChatColor = Utils.convertAmpCodes(SettingsModule.getString("chatFilterModerChatColor"));
				    if(chatColor.equals("")) chatColor = "§r";
				    if(tChatColor.equals("")) tChatColor = chatColor;
				    if(mChatColor.equals("")) mChatColor = "§r";
					boolean isModer = false;
					
					String chatString = m.group(1);
					String prefix = m.group(3);
					playerName = m.group(4);
					String messageContent = m.group(5);
					
					if(chatString != null) {
						if(chatString.equals("[G]")) chatType = 1;
						else if(chatString.equals("[T]")) chatType = 2;
						else if(chatString.equals("[M]")) chatType = 3;
						else if(chatString.equals("[L]")) {
							if(SettingsModule.getBool("lchat")) {
								newMessage = "";
								TCChatLine newLine = new TCChatLine(Minecraft.getMinecraft().ingameGUI.getUpdateCounter(), chat, 0, true);
								TabbyChat.getInstance().addToChannel("L", newLine, false);
								return false;
							}
							break filter;
						}
					}
					
					if(SettingsModule.getBool("globalNotify") && !SettingsModule.getBool("allNotify") && !playerName.equals(ModuleInfo.playerName)) Utils.sendNotify(message);
					
					if(SettingsModule.getBool("chatFilter")) {
						newMessage = "";
						if(chatType == 1) newMessage += "§8[§aG§8] ";
						else if(chatType == 2) {
							newMessage += "§8[§6T§8] ";
							chatColor = tChatColor;
						} 
						else if(chatType == 3) {
							newMessage += "§8[§9M§8] ";
							chatColor = mChatColor;
						} 
						else chatColor = Utils.convertAmpCodes(SettingsModule.getString("chatFilterLoaclColor"));
						
						if(prefix != null) {
							if(prefix.equals("VIP")) prefixColor = "§6";
							else if(prefix.equals("Premium")) prefixColor = "§b";
							else if(prefix.equals("Grand")) prefixColor = "§2";
							else if(prefix.equals("Admin") || prefix.equals("AdminHelper")) {
								prefixColor = "§c";
								isModer = true;
								if(!SettingsModule.getString("chatFilterModerColor").equals("") && chatType != 2 && chatType != 3)
									chatColor = Utils.convertAmpCodes(SettingsModule.getString("chatFilterModerColor"));
								else if(chatType != 2 && chatType != 3)
									chatColor = "§6";
							}
							else if(prefix.equals("LowModer") || prefix.equals("Moder") || prefix.equals("HeadModer")) {
								prefixColor = "§9";
								isModer = true;
								if(!SettingsModule.getString("chatFilterModerColor").equals("") && chatType != 2 && chatType != 3)
									chatColor = Utils.convertAmpCodes(SettingsModule.getString("chatFilterModerColor"));
								else if(chatType != 2 && chatType != 3)
									chatColor = "§6";
							}
							newMessage += "§8[" + prefixColor + prefix + "§8] ";
						}
						
						String messageContentOriginal = originalMessage.replaceAll(".*»\\s", "");
						if(chatColor.equals("")) chatColor = "§r";
						
						if(prefix != null && (prefix.equals("HeadModer") || prefix.equals("Moder") || prefix.equals("AdminHelper") || prefix.equals("Admin")) && Utils.hasAmpCodes(messageContentOriginal)) {
							messageContent = originalMessage.replaceAll(".*»\\s", "");
							messageContent = Utils.setAmpCodesToAllText(messageContent);
						}
						else {
							messageContent = Utils.replaceHighWords(messageContent, chatColor);
							messageContent = messageContent.replaceAll("\\s", " " + chatColor);
						}
						if(isModer) {
							// newMessage += "§e" + playerName + " §f§l»" + chatColor + " " + messageContent;
							JsonMessageBuilder builder = new JsonMessageBuilder().newPart()
									.setText(newMessage)
									.end()
									.newPart()
									.setText("§e" + playerName)
									.setHoverText("§aСписок действий")
									.setClick(Type.RUN_COMMAND, "/pi " + playerName)
									.end()
									.newPart()
									.setText(" §f§l»" + chatColor + " " + messageContent)
									.end();
							Utils.SendLog(builder, 0);
							newMessage = "";
						}else {
							// newMessage += "§7" + playerName + " §f§l»" + chatColor + " " + messageContent;
							JsonMessageBuilder builder = new JsonMessageBuilder().newPart()
									.setText(newMessage)
									.end()
									.newPart()
									.setText("§7" + playerName)
									.setHoverText("§aСписок действий")
									.setClick(Type.RUN_COMMAND, "/pi " + playerName)
									.end()
									.newPart()
									.setText(" §f§l»" + chatColor + " " + messageContent)
									.end();
							Utils.SendLog(builder, 0);
							newMessage = "";
						}
						messageFilterd = true;
					}
					
					if(SettingsModule.getBool("hints1") && !playerName.equals(ModuleInfo.playerName) && ((prefix != null && !prefix.equals("HeadModer") && !prefix.equals("AdminHelper") && !prefix.equals("Admin")) || prefix == null)) {
						pattern = Pattern.compile("^(\\[\\w*\\])?\\s*(\\[[^\\[\\]]*\\])?\\s*([\\w-]+)\\s»\\s(.*)");
						matcher = pattern.matcher(message);
						if(matcher.find()) {
							pattern = Pattern.compile("(.+)\\1{5,}");
							matcher = pattern.matcher(message.split("»")[1].toLowerCase());
							if(matcher.find()) {
								Utils.SendLog("§8[§6Подсказка§8] §6В §6сообщении §6ниже §6более §6пяти §6одинаковых §6символов.", 0);
							}
						}
					}
				}
				else if(SettingsModule.getBool("chatFilter") && warpAd.matches()) {
					String warp = warpAd.group(2);
					String text = warpAd.group(3);
					String tChatColor = Utils.convertAmpCodes(SettingsModule.getString("chatFilterTColor"));
					newMessage = "§8[§6T§8] " + Utils.setAmpCodesToAllText(tChatColor + warp + " §f§l» " + tChatColor + text);
				}
			}
			
			if(args.length >= 2 && args[1].equals(":") && SettingsModule.getBool("spchat")) {
				newMessage = "";
				TCChatLine newLine = new TCChatLine(Minecraft.getMinecraft().ingameGUI.getUpdateCounter(), chat, 0, true);
				TabbyChat.getInstance().addToChannel("S", newLine, false);
				return false;
			}

			if(SettingsModule.getBool("allNotify") && !playerName.equals(ModuleInfo.playerName) && (!newMessage.equals("") || messageFilterd)) Utils.sendNotify(message);
			else if(!SettingsModule.getBool("allNotify") && SettingsModule.getBool("personalNotify") && Pattern.compile("(@" + ModuleInfo.playerName +")").matcher(message).find() && personalNotifyTimer <= 0 && !playerName.equals(ModuleInfo.playerName)) {
				Utils.sendNotify(message);
				this.personalNotifyTimer = 5;
			}
			
			if(!newMessage.equals("") && args.length > 1 && args[0].equals("Границы:")) {
				String[] cords = args[1].split(",");
				String posX = cords[0].replaceAll("\\(", "");
				String posZ = cords[2].replaceAll("\\)", "");
				JsonMessageBuilder builder = new JsonMessageBuilder().newPart()
						.setText("§9Границы: §e" + args[1] + " " + args[2] + " " + args[3])
						.setHoverText("§r/tppos " + posX + " 200 " + posZ)
						.setClick(Type.RUN_COMMAND, "/tppos " + posX + " 200 " + posZ)
						.end();
				Utils.SendLog(builder, 0);
				newMessage = "";
			}
			else if(!newMessage.equals("") && args.length > 3 && args[1].equals("-") && args[2].equals("Местоположение:")) {
				String[] cords = message.split(", ");
				String posX = cords[1].replaceAll(",", "");
				String posY = cords[2].replaceAll(",", "");
				String posZ = cords[3].replaceAll(",", "").replaceAll("\\)", "");
				JsonMessageBuilder builder = new JsonMessageBuilder().newPart()
						.setText("§6 - Местоположение: §f" + args[3] + " " + args[4] + " " + args[5] + " " + args[6])
						.setHoverText("§r/tppos " + posX + " " + posY + " " + posZ)
						.setClick(Type.RUN_COMMAND, "/tppos " + posX + " " + posY + " " + posZ)
						.end();
				Utils.SendLog(builder, 0);
				newMessage = "";
			}
			else if(!newMessage.equals("") && args.length > 3 && args[1].equals("-") && args[2].equals("IP")) {
				String ip = args[4].replaceAll("/", "");
				JsonMessageBuilder builder = new JsonMessageBuilder().newPart()
						.setText("§6 - IP адрес: §f" + ip)
						.setHoverText("§r/seen " + ip)
						.setClick(Type.RUN_COMMAND, "/seen " + ip)
						.end();
				Utils.SendLog(builder, 0);
				newMessage = "";
				
				if(seenCheck && SettingsModule.getBool("autoseen")) Utils.SendMessage("/seen " + ip);
			}
			
			if(args.length >= 3 && args[0].equals("Приветствуем") && args[1].equals("нового") && SettingsModule.getBool("autoseen")) {
				Utils.SendMessage("/seen " + args[3]);
				this.seenCheck = true;
			}
			
			if(this.osModers > 0 && args.length >= 5 && args[0].equals("[Авторизация]") && args[1].equals("Модератор") && args[3].equals("на") && args[4].equals("дату")) {
				this.osModers--;
				String online = "";
				if(args.length >= 12) {
					String mins = args[11];
					if(Integer.parseInt(args[11]) < 10) mins = "0" + args[11];
					online = args[9] + ":" + mins;
				}
				else if(args[10].equals("минут.")) {
					String mins = args[9];
					if(Integer.parseInt(args[9]) < 10) mins = "0" + args[9];
					online = "0:" +  mins;
				}
				else {
					String mins = args[9];
					online = mins + ":00";
				}
				JsonMessageBuilder builder = new JsonMessageBuilder().newPart()
						.setText("§6[Страж] §9§l" + args[2] + ": §6" + online)
						.setHoverText("*Скопировать*")
						.setClick(Type.RUN_COMMAND, "/setclipboard " + online)
						.end();
				Utils.SendLog(builder, 0);
				newMessage = "";
			}
			
			if(this.osModers > 0 && args.length >= 4 && args[0].equals("[Авторизация]") && args[1].equals("Информации") && args[2].equals("о") && args[3].equals("онлайне")) {
				this.osModers--;
				Utils.SendLog("§6[Страж] §9§l" + args[4] + ": §6-", 0);
				newMessage = "";
			}
			
			if(args.length >= 4 && args[0].equals("Для") && args[1].equals("принятия") && args[2].equals("запроса")) {
				JsonMessageBuilder builder = new JsonMessageBuilder()
			    		.newPart()
			    		.setText("§6Для принятия запроса введите §c/tpaccept§6.")
			    		.setHoverText("/tpaccept")
			    		.setClick(ClickEvent.Type.RUN_COMMAND, "/tpaccept")
			    		.end();
						
				Utils.SendLog(builder, 0);
				newMessage = "";
			}
			
			if(args.length >= 4 && args[0].equals("Для") && args[1].equals("отказа") && args[2].equals("от")) {
				JsonMessageBuilder builder = new JsonMessageBuilder()
			    		.newPart()
			    		.setText("§6Для отказа от запроса введите §c/tpdeny§6.")
			    		.setHoverText("/tpdeny")
			    		.setClick(ClickEvent.Type.RUN_COMMAND, "/tpdeny")
			    		.end();
						
				Utils.SendLog(builder, 0);
				newMessage = "";
			}
			
		}catch(Exception e) {e.printStackTrace();}
		
		
		featherMessagesInfo--;
		if(newMessage.equals("")) return false;
		else {
			if(!newMessage.equals(message)) {
				PrivateFields.serverChatComponent.set(packet, (IChatComponent)new ChatComponentText(newMessage));
			}
			return true;
		}
	}

}
