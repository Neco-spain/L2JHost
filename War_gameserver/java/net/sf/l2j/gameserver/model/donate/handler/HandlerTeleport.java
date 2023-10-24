package net.sf.l2j.gameserver.model.donate.handler;

import net.sf.l2j.commons.lang.Tokenizer;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.actor.Player;

public class HandlerTeleport
{
	public static void handleTeleport(Player player, Tokenizer tokenizer)
	{
		int X = tokenizer.getAsInteger(2, 0);
		int Y = tokenizer.getAsInteger(3, 0);
		int Z = tokenizer.getAsInteger(4, 0);
		if (!Config.KARMA_PLAYER_CAN_USE_GK && player.getKarma() > 0)
		{
			player.sendMessage(player.getName() + " " + "You dont teleport for karma.");
			return;
		}
		player.teleportTo(X, Y, Z, 15);
	}
}
