package net.sf.l2j.gameserver.handler.voicedcommandhandlers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import net.sf.l2j.commons.data.Pagination;
import net.sf.l2j.commons.lang.StringUtil;
import net.sf.l2j.commons.lang.Tokenizer;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.data.manager.BufferManager;
import net.sf.l2j.gameserver.data.xml.ItemData;
import net.sf.l2j.gameserver.enums.ShortcutType;
import net.sf.l2j.gameserver.enums.actors.ClassRace;
import net.sf.l2j.gameserver.enums.actors.Sex;
import net.sf.l2j.gameserver.enums.skills.SkillType;
import net.sf.l2j.gameserver.handler.IVoicedCommandHandler;
import net.sf.l2j.gameserver.model.Shortcut;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.ExclusiveBot;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.serverpackets.ExShowScreenMessage;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.ShortCutRegister;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.taskmanager.AutoFarmTaskManager;

public class VoiceExclusiveFarm implements IVoicedCommandHandler
{
	private static final String[] _voicedCommands =
	{
		"autofarm",
		"autofarmbuy",
		"buy_minutes",
		"buy_hours",
		"buy_days"
	};
	
	@Override
	public boolean useVoicedCommand(String command, Player player, String target)
	{
		final StringTokenizer st = new StringTokenizer(command, " ");
		String currentCommand = st.nextToken();
		
		if (command.equals("autofarm") && Config.ENABLE_RAIDBOSS_COMMAND)
		{
			showHtml(player);
			
		}
		if (command.equals("autofarmbuy") && Config.ENABLE_RAIDBOSS_COMMAND)
		{
			showbuyHtml(player);
		}
		if (currentCommand.equals("buy_minutes") && Config.ENABLE_RAIDBOSS_COMMAND)
		{
			int itemId = Integer.parseInt(st.nextToken());
			int count = Integer.parseInt(st.nextToken());
			int minutes = Integer.parseInt(st.nextToken());
			handleBuyMinutes(player, itemId, count, minutes);
		}
		
		if (currentCommand.equals("buy_hours") && Config.ENABLE_RAIDBOSS_COMMAND)
		{
			int itemId = Integer.parseInt(st.nextToken());
			int count = Integer.parseInt(st.nextToken());
			int hours = Integer.parseInt(st.nextToken());
			handleBuyHours(player, itemId, count, hours);
		}
		
		if (currentCommand.equals("buy_days") && Config.ENABLE_RAIDBOSS_COMMAND)
		{
			int itemId = Integer.parseInt(st.nextToken());
			int count = Integer.parseInt(st.nextToken());
			int days = Integer.parseInt(st.nextToken());
			handleBuyDays(player, itemId, count, days);
		}
		
		return true;
		
	}
	
	public static void handleSkills(Player player, Tokenizer tokenizer)
	{
		int fakePlayerId = tokenizer.getAsInteger(2, 0);
		int page = tokenizer.getAsInteger(3, 0);
		int slot = tokenizer.getAsInteger(4, 0);
		Player fakePlayer = World.getInstance().getPlayer(fakePlayerId);
		SkillsPage(fakePlayer, fakePlayer, page, slot);
	}
	
	public static void showHtml(Player player)
	{
		ExclusiveBot bot = player.getBot();
		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		
		html.setFile(player.isLang() + "mods/autofarm/index.htm");
		if (player.getMemos().getLong("botEndTime") > 1)
		{
			long delay = player.getMemos().getLong("botEndTime", 0);
			html.replace("%Timebot%", "" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(delay) + "");
		}
		else
		{
			html.replace("%Timebot%", "Buy now you no have time");
		}
		
		final ClassRace race = player.getClassId().getRace();
		final Sex sex = Sex.VALUES[player.getAppearance().getSex().ordinal()];
		String icon = "";
		
		switch (race)
		{
			case HUMAN:
				icon = "exclusive.Human";
				break;
			case ELF:
				icon = "exclusive.Elf";
				break;
			case DARK_ELF:
				icon = "exclusive.Dark";
				break;
			case ORC:
				icon = "exclusive.Orc";
				break;
			case DWARF:
				icon = "exclusive.Dwarf";
				break;
		}
		
		if (sex == Sex.MALE)
		{
			icon += "_Male";
		}
		else if (sex == Sex.FEMALE)
		{
			icon += "_Female";
		}
		
		if (player.isMageClass())
		{
			icon += "_Mage";
		}
		else
		{
			icon += "_Fighter";
		}
		//aqui Stage
		html.replace("%state%", bot.isActive() ? "<img src=\"exclusive.on\" width=\"85\" height=\"22\" />" : "<img src=\"exclusive.off\" width=\"85\" height=\"22\" />");
		html.replace("%potion%", player.isPotionDOA() ? "checked" : "unable");
		html.replace("%pethelp%", player.isPetAttackDOA() ? "checked" : "unable");
		html.replace("%assist%", player.isPartyAssistDOA() ? "checked" : "unable");
		html.replace("%attack%", player.isForceAttackDOA() ? "checked" : "unable");
		html.replace("%viwer%", player.isViwerRandiosDOA() ? "checked" : "unable");
		
		html.replace("%buffer%", player.isAutoBufferDOA() ? "checked" : "unable");
		
		html.replace("%chance%", String.valueOf(bot.getChancePercentage()));
		html.replace("%radius%", StringUtil.formatNumber(bot.getRadius()));
		// Buttao ative
		html.replace("%button%", (bot.isActive() ? "<button value=\"\" action=\"bypass service farm_off\" width=85 height=22 back=\"exclusive.on\" fore=\"exclusive.off\">" : "<button value=\"\" action=\"bypass service farm_on\" width=85 height=22 back=\"exclusive.off\" fore=\"exclusive.on\">"));
		html.replace("%radius%", StringUtil.formatNumber(bot.getRadius()));
		
		final StringBuilder sb = new StringBuilder();
		final StringBuilder sb1 = new StringBuilder();
		
		sb.append("<img src=\"L2UI.SquareGray\" width=\"300\" height=\"1\" />");
		sb.append("<table bgcolor=\"000000\" width=\"300\" height=\"15\">");
		sb.append("<tr>");
		sb.append("<td height=\"16\" width=\"16\"><button value=\"\" action=\"bypass service tryattak\" width=14 height=14 back=\"L2UI.control.checkBox_" + (player.isForceAttackDOA() ? "checked" : "unable") + "\" fore=\"L2UI.control.checkBox_" + (player.isForceAttackDOA() ? "checked" : "unable") + "\"></td>");
		sb.append("<td width=\"190\">Automatic Attack - F1</td>");
		sb.append("</tr>");
		sb.append("</table>");
		sb.append("<img src=\"L2UI.SquareGray\" width=\"300\" height=\"1\" />");
		
		sb1.append("<table width=\"300\" height=\"15\">");
		sb1.append("<tr>");
		if (player.getMemos().getInteger("slot_0") > 1)
		{
			int skillId = player.getMemos().getInteger("slot_0");
			sb1.append("<td><button value=\"\" action=\"bypass service skill_list " + player.getObjectId() + " 1 0\" width=\"32\" height=\"32\" back=\"exclusive.SteadyBoxSlot_Plus\" fore=\"" + getIconForBufferId(skillId) + "\"></td>");
		}
		else
			sb1.append("<td><button value=\"\" action=\"bypass service skill_list " + player.getObjectId() + " 1 0\" width=\"32\" height=\"32\" back=\"exclusive.SteadyBoxSlot_Plus\" fore=\"exclusive.SteadyBoxSlot_Bg\"></td>");
		
		if (player.getMemos().getInteger("slot_1") > 1)
		{
			int skillId = player.getMemos().getInteger("slot_1");
			sb1.append("<td><button value=\"\" action=\"bypass service skill_list " + player.getObjectId() + " 1 1\" width=\"32\" height=\"32\" back=\"exclusive.SteadyBoxSlot_Plus\" fore=\"" + getIconForBufferId(skillId) + "\"></td>");
		}
		else
			sb1.append("<td><button value=\"\" action=\"bypass service skill_list " + player.getObjectId() + " 1 1\" width=\"32\" height=\"32\" back=\"exclusive.SteadyBoxSlot_Plus\" fore=\"exclusive.SteadyBoxSlot_Bg\"></td>");
		
		if (player.getMemos().getInteger("slot_2") > 1)
		{
			int skillId = player.getMemos().getInteger("slot_2");
			sb1.append("<td><button value=\"\" action=\"bypass service skill_list " + player.getObjectId() + " 1 2\" width=\"32\" height=\"32\" back=\"exclusive.SteadyBoxSlot_Plus\" fore=\"" + getIconForBufferId(skillId) + "\"></td>");
		}
		else
			sb1.append("<td><button value=\"\" action=\"bypass service skill_list " + player.getObjectId() + " 1 2\" width=\"32\" height=\"32\" back=\"exclusive.SteadyBoxSlot_Plus\" fore=\"exclusive.SteadyBoxSlot_Bg\"></td>");
		
		if (player.getMemos().getInteger("slot_3") > 1)
		{
			int skillId = player.getMemos().getInteger("slot_3");
			sb1.append("<td><button value=\"\" action=\"bypass service skill_list " + player.getObjectId() + " 1 3\" width=\"32\" height=\"32\" back=\"exclusive.SteadyBoxSlot_Plus\" fore=\"" + getIconForBufferId(skillId) + "\"></td>");
		}
		else
			sb1.append("<td><button value=\"\" action=\"bypass service skill_list " + player.getObjectId() + " 1 3\" width=\"32\" height=\"32\" back=\"exclusive.SteadyBoxSlot_Plus\" fore=\"exclusive.SteadyBoxSlot_Bg\"></td>");
		
		if (player.getMemos().getInteger("slot_4") > 1)
		{
			int skillId = player.getMemos().getInteger("slot_4");
			sb1.append("<td><button value=\"\" action=\"bypass service skill_list " + player.getObjectId() + " 1 4\" width=\"32\" height=\"32\" back=\"exclusive.SteadyBoxSlot_Plus\" fore=\"" + getIconForBufferId(skillId) + "\"></td>");
		}
		else
			sb1.append("<td><button value=\"\" action=\"bypass service skill_list " + player.getObjectId() + " 1 4\" width=\"32\" height=\"32\" back=\"exclusive.SteadyBoxSlot_Plus\" fore=\"exclusive.SteadyBoxSlot_Bg\"></td>");
		
		sb1.append("</tr>");
		sb1.append("</table>");
		
		html.replace("%skills%", sb1.toString());
		html.replace("%force%", sb.toString());
		
		html.replace("%icon%", "<img src=\"" + icon + "\" width=\"32\" height=\"32\">");
		player.sendPacket(html);
	}
	
	private static void showbuyHtml(Player player)
	{
		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setFile(player.isLang() + "mods/autofarm/buynow.htm");
		final ClassRace race = player.getClassId().getRace();
		final Sex sex = Sex.VALUES[player.getAppearance().getSex().ordinal()];
		String icon = "";
		
		switch (race)
		{
			case HUMAN:
				icon = "exclusive.Human";
				break;
			case ELF:
				icon = "exclusive.Elf";
				break;
			case DARK_ELF:
				icon = "exclusive.Dark";
				break;
			case ORC:
				icon = "exclusive.Orc";
				break;
			case DWARF:
				icon = "exclusive.Dwarf";
				break;
		}
		
		if (sex == Sex.MALE)
		{
			icon += "_Male";
		}
		else if (sex == Sex.FEMALE)
		{
			icon += "_Female";
		}
		
		if (player.isMageClass())
		{
			icon += "_Mage";
		}
		else
		{
			icon += "_Fighter";
		}
		
		html.replace("%icon%", "<img src=\"" + icon + "\" width=\"32\" height=\"32\">");
		player.sendPacket(html);
	}
	
	protected void handleBuyMinutes(Player player, int itemid, int count, int minutes)
	{
		if (player.getInventory().getItemCount(itemid, -1) < (count))
		{
			player.sendPacket(new ExShowScreenMessage("You don't have required items! " + ItemData.getInstance().getTemplate(itemid).getName() + " " + count, (int) TimeUnit.SECONDS.toMillis(2)));
			
			return;
		}
		
		player.destroyItemByItemId("Tkt", itemid, count, player.getTarget(), true);
		player.sendPacket(new ExShowScreenMessage(player.getName() + " You have successfully purchase " + minutes + " Minutes AutoFarm.", (int) TimeUnit.SECONDS.toMillis(5)));
		AutoFarmTaskManager.BotTimeMinutos(player, minutes);
		
	}
	
	protected void handleBuyHours(Player player, int itemid, int count, int hours)
	{
		if (player.getInventory().getItemCount(itemid, -1) < (count))
		{
			player.sendPacket(new ExShowScreenMessage("You don't have required items! " + ItemData.getInstance().getTemplate(itemid).getName() + " " + count, (int) TimeUnit.SECONDS.toMillis(2)));
			
			return;
		}
		
		player.destroyItemByItemId("Tkt", itemid, count, player.getTarget(), true);
		player.sendPacket(new ExShowScreenMessage(player.getName() + " You have successfully purchase " + hours + " Hours AutoFarm.", (int) TimeUnit.SECONDS.toMillis(5)));
		AutoFarmTaskManager.BotTimehours(player, hours);
		
	}
	
	protected void handleBuyDays(Player player, int itemid, int count, int days)
	{
		if (player.getInventory().getItemCount(itemid, -1) < (count))
		{
			player.sendPacket(new ExShowScreenMessage("You don't have required items! " + ItemData.getInstance().getTemplate(itemid).getName() + " " + count, (int) TimeUnit.SECONDS.toMillis(2)));
			
			return;
		}
		
		player.destroyItemByItemId("Tkt", itemid, count, player.getTarget(), true);
		player.sendPacket(new ExShowScreenMessage(player.getName() + " You have successfully purchase " + days + " Days AutoFarm.", (int) TimeUnit.SECONDS.toMillis(5)));
		AutoFarmTaskManager.BotTimedays(player, days);
		
	}
	
	private static final int SKILLS_PER_PAGE = 8;
	
	public static void SkillsPage(Player player, Player fake, int page, int slot)
	{
		Map<Integer, L2Skill> skills = fake.getSkillsFromDataSource(fake, fake.getClassIndex());
		
		fake.setSkills(skills);
		
		Map<Integer, L2Skill> activeSkills = fake.getActiveSkills();
		
		for (L2Skill skill : fake.getSkills().values())
		{
			if (skill.getSkillType() == SkillType.COMMON_CRAFT || skill.getSkillType() == SkillType.DWARVEN_CRAFT || skill.getSkillType() == SkillType.DUMMY)
				continue;
			
			if (skill.isAutofarm())
			{
				activeSkills.put(skill.getId(), skill);
			}
		}
		
		final Pagination<L2Skill> list = new Pagination<>(activeSkills.values().stream(), page, SKILLS_PER_PAGE);
		
		final StringBuilder sb = new StringBuilder();
		final StringBuilder sb1 = new StringBuilder();
		int row = 0;
		boolean isTableGenerated = false;
		
		for (L2Skill effect : list)
		{
			if (effect.isAutofarm())
			{
				
				int skillId = effect.getId();
				if (!isTableGenerated)
				{
					sb1.append("<table width=\"300\" height=\"15\" bgcolor=\"000000\"><tr>");
					
					sb1.append("<td align=left width=70><button value=\"\" action=\"bypass voiced_autofarm\" width=\"32\" height=\"23\" back=\"exclusive.Botsystem_DF_KeyBack_over\" fore=\"exclusive.Botsystem_DF_KeyBack\" /></td>");
					
					sb1.append("<td align=center width=108> Slot: " + slot + "</td>");
					
					sb1.append("<td align=right width=70><button value=\"clean\" action=\"bypass service skill_remove " + player.getObjectId() + " " + skillId + " " + slot + "\" width=64 height=22 back=\"exclusive.Btn_Down_Olympiad\" fore=\"exclusive.Btn_DF_Olympiad\" /></td>");
					
					sb1.append("</tr></table>");
					isTableGenerated = true;
				}
				if (row % 2 == 0)
				{
					sb.append("<table width=\"300\" height=\"15\" bgcolor=\"000000\">");
					sb.append("<tr>");
				}
				else
				{
					sb.append("<table width=\"300\" height=\"15\">");
					sb.append("<tr>");
				}
				
				sb.append("<td><img src=\"" + getIconForBufferId(skillId) + "\" width=\"32\" height=\"32\"></td>");
				String itemName = effect.getName().length() > 17 ? effect.getName().substring(0, 17) + "..." : effect.getName();
				sb.append("<td width=118 align=center>" + itemName + "</td>");
				sb.append("<td width=100 align=center>" + "<a action=\"bypass service skill_use " + fake.getObjectId() + " " + skillId + " " + slot + "\"><font color=\"FE9A2E\">Use</font></a>" + "</td>");
				sb.append("</tr></table>");
				row++;
				
			}
		}
		sb.append("<table width=\"300\" height=\"15\" bgcolor=\"000000\"><tr>");
		if (page > 1)
			sb.append("<td align=left width=70><a action=\"bypass service skill_list " + fake.getObjectId() + " " + String.valueOf(page - 1) + " " + slot + "\">Previous</a></td>");
		else
			sb.append("<td align=left width=70>Previous</td>");
		
		sb.append("<td align=center width=108> Page: " + page + "</td>");
		if (list.size() > page)
			sb.append("<td align=right width=70><a action=\"bypass service skill_list " + fake.getObjectId() + " " + String.valueOf(page + 1) + " " + slot + "\">Next</a></td>");
		else
			sb.append("<td align=right width=70>Next</td>");
		
		sb.append("</tr></table>");
		
		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setFile(player.isLang() + "mods/autofarm/skills.htm");
		
		html.replace("%name%", fake.getName());
		html.replace("%menu%", sb1.toString());
		html.replace("%list%", sb.toString());
		player.sendPacket(html);
		
	}
	
	public static String getIconForBufferId(int skillId)
	{
		if (skillId == 4702)
		{
			return "icon.skill1332";
		}
		else if (skillId == 4703)
		{
			return "icon.skill1332";
		}
		else if (skillId == 4551)
		{
			return "icon.skill1164";
		}
		else if (skillId == 4552)
		{
			return "icon.skill1164";
		}
		else if (skillId == 4553)
		{
			return "icon.skill1164";
		}
		else if (skillId == 4554)
		{
			return "icon.skill1164";
		}
		else if (skillId == 4700)
		{
			return "icon.skill1331";
		}
		else if (skillId == 4699)
		{
			return "icon.skill1331";
		}
		else
		{
			return (skillId < 10) ? "icon.skill000" + skillId : (skillId < 100) ? "icon.skill00" + skillId : (skillId < 1000) ? "icon.skill0" + skillId : "icon.skill" + skillId;
		}
	}
	
	@Override
	public String[] getVoicedCommandList()
	{
		return _voicedCommands;
	}
	
	public static void handleUseSkill(Player player, Tokenizer tokenizer)
	{
		int fakePlayerId = tokenizer.getAsInteger(2, 0);
		int skillId = tokenizer.getAsInteger(3, 0);
		int slot = tokenizer.getAsInteger(4, 0);
		Player fakePlayer = World.getInstance().getPlayer(fakePlayerId);
		
		Shortcut shortcut = new Shortcut(slot, 9, ShortcutType.SKILL, skillId, -1, 1);
		final int level = fakePlayer.getSkillLevel(skillId);
		if (level > 0)
		{
			shortcut = new Shortcut(slot, 9, ShortcutType.SKILL, skillId, level, 1);
			fakePlayer.sendPacket(new ShortCutRegister(fakePlayer, shortcut));
			fakePlayer.getShortcutList().addShortcut(shortcut);
		}
		fakePlayer.getMemos().set("slot_" + slot, skillId);
		showHtml(fakePlayer);
	}
	
	public static void handleRemoveSkill(Player player, Tokenizer tokenizer)
	{
		int fakePlayerId = tokenizer.getAsInteger(2, 0);
		int skillId = tokenizer.getAsInteger(3, 0);
		int slot = tokenizer.getAsInteger(4, 0);
		Player fakePlayer = World.getInstance().getPlayer(fakePlayerId);
		
		Shortcut shortcut = new Shortcut(slot, 9, ShortcutType.SKILL, skillId, -1, 1);
		final int level = fakePlayer.getSkillLevel(skillId);
		if (level > 0)
		{
			shortcut = new Shortcut(shortcut.getSlot(), 9, ShortcutType.SKILL, skillId, level, 1);
			fakePlayer.getShortcutList().deleteShortcut(shortcut.getSlot(), 9);
		}
		fakePlayer.getMemos().set("slot_" + slot, 0);
		showHtml(fakePlayer);
	}
	
	public static void showHtmlBuffer(Player player)
	{
		showGiveBuffsWindow(player);
		
	}
	
	private static void showGiveBuffsWindow(Player player)
	{
		final StringBuilder sb = new StringBuilder(200);
		
		final Map<String, ArrayList<L2Skill>> schemes = BufferManager.getInstance().getPlayerSchemes(player.getObjectId());
		if (schemes == null || schemes.isEmpty())
			sb.append("<font color=\"LEVEL\">You haven't defined any scheme.</font>");
		else
		{
			for (Map.Entry<String, ArrayList<L2Skill>> scheme : schemes.entrySet())
			{
				final int cost = getFee(scheme.getValue());
				StringUtil.append(sb, "<button value=\"\" action=\"bypass service appy\" width=14 height=14 back=\"L2UI.control.checkBox_%select%\" fore=\"L2UI.control.checkBox_%select%\">", "<font color=\"LEVEL\">", scheme.getKey(), " [", scheme.getValue().size(), " / ", player.getMaxBuffCount(), "]", ((cost > 0) ? " - cost: " + StringUtil.formatNumber(cost) : ""), "</font>");
				
			}
		}
		
		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		
		html.setFile(player.isLang() + "mods/autofarm/buffer.htm");
		html.replace("%schemes%", sb.toString());
		html.replace("%max_schemes%", Config.BUFFER_MAX_SCHEMES);
		html.replace("%select%", player.isSelectBufferDOA() ? "checked" : "unable");
		
		html.replace("%count%", player.getBuffCount() + " / " + player.getMaxBuffCount());
		
		player.sendPacket(html);
	}
	
	private static int getFee(List<L2Skill> list)
	{
		if (Config.BUFFER_STATIC_BUFF_COST > 0)
			return list.size() * Config.BUFFER_STATIC_BUFF_COST;
		
		int fee = 0;
		for (L2Skill sk : list)
			fee += BufferManager.getInstance().getAvailableBuff(sk).getPriceItemId();
		
		return fee;
	}
	
	public static void giveBuffers(Player player, Tokenizer tokenizer)
	{
		String schemeName = tokenizer.getToken(2);
		int cost = tokenizer.getAsInteger(3, 0);
		
		if (cost == 0 || player.reduceAdena("NPC Buffer", cost, null, true))
			BufferManager.getInstance().applySchemeEffects(null, player, player.getObjectId(), schemeName);
		
	}
	
}
