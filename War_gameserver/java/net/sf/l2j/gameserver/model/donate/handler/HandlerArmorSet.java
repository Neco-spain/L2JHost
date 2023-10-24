package net.sf.l2j.gameserver.model.donate.handler;

import net.sf.l2j.commons.lang.Tokenizer;

import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.donate.previwer.HtmlArmor;

public class HandlerArmorSet
{
	public static void handleListSet(Player player, Tokenizer tokenizer)
	{
		int numPages = tokenizer.getAsInteger(2, 0);
		HtmlArmor.listArmor(player, numPages);

	}
	
	public static void CreateArmor(Player player, Tokenizer tokenizer)
	{
		String target = tokenizer.getToken(2);
		int itemId = tokenizer.getAsInteger(3, 0);
		int page = tokenizer.getAsInteger(4, 0);
		Player fakePlayer = World.getInstance().getPlayer(target);
		HtmlArmor.handleCreateArmorEquipped(player, fakePlayer, itemId, page);
	}
}
