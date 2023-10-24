package net.sf.l2j.gameserver.model.donate.handler;

import net.sf.l2j.commons.lang.Tokenizer;

import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.donate.previwer.HtmlTransferStory;

public class HandlerTransferStory
{
	public static void infopagments(Player player, Tokenizer tokenizer)
	{
		int numPages = tokenizer.getAsInteger(2, 0);
		HtmlTransferStory.HtmlInfoPagments(player, numPages);
	}
	
	public static void infopagmentsEnd(Player player, Tokenizer tokenizer)
	{
		int numPages = tokenizer.getAsInteger(2, 0);
		HtmlTransferStory.HtmlInfoPagmentsEnd(player, numPages);
	}
}
