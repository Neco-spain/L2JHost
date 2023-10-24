package net.sf.l2j.gameserver.handler.voicedcommandhandlers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import net.sf.l2j.commons.data.StatSet;
import net.sf.l2j.commons.math.MathUtil;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.data.manager.GrandBossManager;
import net.sf.l2j.gameserver.data.manager.SpawnManager;
import net.sf.l2j.gameserver.data.xml.NpcData;
import net.sf.l2j.gameserver.handler.IVoicedCommandHandler;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.model.spawn.ASpawn;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

public class VoiceRaidBossInfo implements IVoicedCommandHandler
{
	String hora = Config.RAID_BOSS_DATE_FORMAT;
	private final static int PAGE_LIMIT = Config.RAID_BOSS_DROP_PAGE_LIMIT;
	
	private static final String[] _voicedCommands =
	{
		"raid","chat"
	};
	
	@Override
	public boolean useVoicedCommand(String command, Player player, String target)
	{
		final StringTokenizer st = new StringTokenizer(command, " ");
		String currentCommand = st.nextToken();
		
		if (currentCommand.startsWith("chat"))
		{
			int val = Integer.parseInt(st.nextToken());
			showChatWindow(player, val);
			player.getRadarList().removeAllMarkers();
		}
		
		if (command.equals("raid") && Config.ENABLE_RAIDBOSS_COMMAND)
		{
			final NpcHtmlMessage html = new NpcHtmlMessage(0);
			html.setFile(player.isLang() + "mods/boss/index.htm");
			html.replace("%list%", getList(player, 0));
			player.sendPacket(html);
		}

		return true;
	}
	
	public void showChatWindow(Player player, int val)
	{
		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setFile(player.isLang() +  "mods/boss/index.htm");
		html.replace("%list%", getList(player, val));
		player.sendPacket(html);
	}
	
	private String getList(Player player, int page)
	{
		// Retrieve the entire types list based on group type.
		List<Integer> list = Config.LIST_RAID_BOSS_IDS;
		
		// Calculate page number.
		final int max = MathUtil.countPagesNumber(list.size(), PAGE_LIMIT);
		page = page > max ? max : page < 1 ? 1 : page;
		
		// Cut skills list up to page number.
		list = list.subList((page - 1) * PAGE_LIMIT, Math.min(page * PAGE_LIMIT, list.size()));
		
		final StringBuilder sb = new StringBuilder();
		sb.append("<img src=L2UI.SquareGray width=296 height=1>");
		
		int row = 0;
		
		for (int bossId : list)
		{
			String name = "";
			NpcTemplate template = null;
			if ((template = NpcData.getInstance().getTemplate(bossId)) != null)
			{
				name = template.getName();
			}
			else
			{
				
				continue;
			}
			StatSet boss_stat = null;
			ASpawn actual_boss_stat = null;
			GrandBossManager.getInstance().getStatSet(bossId);
			long delay = 0;
			if (NpcData.getInstance().getTemplate(bossId).isType("RaidBoss"))
			{
				actual_boss_stat = SpawnManager.getInstance().getSpawn(bossId);
				if (actual_boss_stat != null)
					delay = SpawnManager.getInstance().getSpawn(bossId).getSpawnData().getRespawnTime();
			}
			else if (NpcData.getInstance().getTemplate(bossId).isType("GrandBoss"))
			{
				boss_stat = GrandBossManager.getInstance().getStatSet(bossId);
				if (boss_stat != null)
					delay = boss_stat.getLong("respawn_time");
			}
			else
				continue;
			int lvl = template.getLevel();
			if (delay <= System.currentTimeMillis())
				sb.append("<table width=296 bgcolor=000000><tr><td width=226 align=left> <font color=FFFFFF>" + "Lv " + lvl + " " + name + "</font> </td><td width=70 align=right><font color=00FF00> IS ALIVE! </font></td></tr></table><img src=L2UI.SquareGray width=296 height=1>");
			else
				sb.append("<table width=296 bgcolor=000000><tr><td width=226 align=left> <font color=FF0000>" + "Lv " + lvl + " " + name + " IS DEAD!</font>" + " </td><td width=70 align=right><font color=FF0000>" + new SimpleDateFormat(hora).format(new Date(delay)) + "</font></td></tr></table><img src=L2UI.SquareGray width=296 height=1>");
			
			row++;
		}
		
		for (int i = PAGE_LIMIT; i > row; i--)
			sb.append("<img height=20>");
		
		// Build page footer.
		sb.append("<img height=4><img src=L2UI.SquareGray width=296 height=1><table width=296 bgcolor=000000><tr>");
		sb.append("<td align=left width=75><button value=\"Refresh\" action=\"bypass voiced" + "_chat " + page + "\" width=65 height=19 back=L2UI_ch3.smallbutton2_over fore=L2UI_ch3.smallbutton2></td>");
		sb.append("<td align=center width=75>" + (page > 1 ? "<button value=\"< PREV\" action=\"bypass voiced" + "_chat " + (page - 1) + "\" width=65 height=19 back=L2UI_ch3.smallbutton2_over fore=L2UI_ch3.smallbutton2>" : "") + "</td>");
		sb.append("<td align=center width=71>Page " + page + "</td>");
		sb.append("<td align=right width=75>" + (page < max ? "<button value=\"NEXT >\" action=\"bypass voiced" + "_chat " + (page + 1) + "\" width=65 height=19 back=L2UI_ch3.smallbutton2_over fore=L2UI_ch3.smallbutton2>" : "") + "</td>");
		sb.append("</tr></table>");
		
		return sb.toString();
	}
	
	@Override
	public String[] getVoicedCommandList()
	{
		return _voicedCommands;
	}
}
