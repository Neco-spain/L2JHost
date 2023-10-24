package net.sf.l2j.gameserver.model.actor.instance;

import net.sf.l2j.commons.lang.StringUtil;

import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.model.entity.events.tournament.AbstractTournament;
import net.sf.l2j.gameserver.model.entity.events.tournament.TournamentManager;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

public class Tournament extends Folk
{
	public Tournament(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if (command.startsWith("match"))
		{
			int id = Integer.parseInt(command.substring(6));
			
			final AbstractTournament tournament = TournamentManager.getTournament(id);
			
			if (tournament == null)
			{
				player.sendMessage("Tournament does not exist.");
				return;
			}
			
			tournament.handleRegUnReg(player);
		}
		else
			super.onBypassFeedback(player, command);
	}
	
	@Override
	public void showChatWindow(Player player, int val)
	{
		final StringBuilder sb = new StringBuilder();

		if (player.isRegisteredInTournament())
			StringUtil.append(sb, "<center>You are currently registered at: " + player.getTournament().getTeamSize() + " vs " + player.getTournament().getTeamSize() + "<br><button action=\"bypass -h npc_" + getObjectId() + "_match " + player.getTournament().getTeamSize() + " \" value=\"" + "Unregister" + "\" width=75 height=21 back=\"L2UI_ch3.Btn1_normalOn\" fore=\"L2UI_ch3.Btn1_normal\"></center>");
		else
		{
			StringUtil.append(sb, "<center>The goal is to defeat the enemy team.</center><br>");
			
			for (final AbstractTournament match : TournamentManager.getTournaments())
				StringUtil.append(sb, "<center><button action=\"bypass -h npc_" + getObjectId() + "_match " + match.getTeamSize() + " \" value=\"" + match.getTeamSize() + " vs " + match.getTeamSize() + "\" width=75 height=21 back=\"L2UI_ch3.Btn1_normalOn\" fore=\"L2UI_ch3.Btn1_normal\"></center>");
		}

		final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
		html.setFile(player.isLang() + "/mods/tournament/Index.htm");
		html.replace("%tournament%", sb.toString());
		html.replace("%objectId%", String.valueOf(getObjectId()));
		player.sendPacket(html);
	}

}
