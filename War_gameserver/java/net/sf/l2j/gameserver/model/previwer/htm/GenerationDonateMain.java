package net.sf.l2j.gameserver.model.previwer.htm;

import net.sf.l2j.commons.logging.CLogger;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.data.xml.ItemData;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

public class GenerationDonateMain
{
	protected static final CLogger LOGGER = new CLogger(GenerationDonateMain.class.getName());
	
	public static void Main(Player player)
	{
		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setFile(player.isLang() + "mods/donate/index.htm");
		
		final StringBuilder sb = new StringBuilder();
		// code here
		
		
		// end code generation
		html.replace("%list%", sb.toString());
		player.sendPacket(html);
	}
	
	public static void purchase(Player player)
	{
		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setFile(player.isLang() + "mods/donate/Purchase.htm");
		
		final StringBuilder sb = new StringBuilder();
		// code here
		
		
		// end code generation
		html.replace("%list%", sb.toString());
		player.sendPacket(html);
	}
	
	public static void bank_select(Player player)
	{
		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setFile(player.isLang() + "mods/donate/PurchasePay.htm");
		
		final StringBuilder sb = new StringBuilder();
		// code here
		
		
		// end code generation
		html.replace("%list%", sb.toString());
		player.sendPacket(html);
	}
	
	public static void pay_confirmation(Player player)
	{
		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setFile(player.isLang() + "mods/donate/PurchaseConfirmation.htm");
		
		final StringBuilder sb = new StringBuilder();
		// code here
		
		
		// end code generation
		html.replace("%list%", sb.toString());
		player.sendPacket(html);
	}
	
	public static void transferencia(Player player)
	{
		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setFile(player.isLang() + "mods/donate/Transfer.htm");
		
		final StringBuilder sb = new StringBuilder();
		// code here
		int idval = Config.DONATE_ITEMID;
		int count = 0;
		
		ItemInstance item = player.getInventory().getItemByItemId(idval);
		if (item != null)
		{
			count = item.getCount();
		}
		
		sb.append("You Have, <font color=\"LEVEL\">" + ItemData.getInstance().getTemplate(idval).getName() + "</font>" + " Count: " + "{" + count + "}");
		
		
		// end code generation
		html.replace("%list%", sb.toString());
		player.sendPacket(html);
	}
}
