package net.sf.l2j.gameserver.model.donate.handler;

import net.sf.l2j.commons.lang.Tokenizer;

import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.donate.previwer.HtmlJewels;

public class HandlerJewelList
{
	public static void handleJewelsListSet(Player player, Tokenizer tokenizer)
	{
		int numPages = tokenizer.getAsInteger(2, 0);
		HtmlJewels.listJewels(player, numPages);

	}
	
	public static void CreateJewels(Player player, Tokenizer tokenizer)
	{
		String target = tokenizer.getToken(2);
		int itemId = tokenizer.getAsInteger(3, 0);
		int page = tokenizer.getAsInteger(4, 0);
		Player fakePlayer = World.getInstance().getPlayer(target);
		HtmlJewels.handleCreateJewelsEquipped(player, fakePlayer, itemId, page);
	}
}
