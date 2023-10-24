package net.sf.l2j.gameserver.model.donate.previwer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.sf.l2j.commons.data.Pagination;
import net.sf.l2j.commons.lang.Tokenizer;
import net.sf.l2j.commons.pool.ConnectionPool;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.data.RecoverableItem;
import net.sf.l2j.gameserver.data.manager.ItemRecoveryManager;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

public class HtmlItemRecover
{
	private static final int ITEM_PER_PAGE = 7;
	
	public static void handleRecoverItemsList(Player player, Tokenizer tokenizer)
	{
		int numPages = tokenizer.getAsInteger(2, 0);
		listRecoverItem(player, numPages);
		
	}

	public static void listRecoverItem(Player player, int page)
	{
		final NpcHtmlMessage content = new NpcHtmlMessage(0);
		content.setFile(player.isLang() + "mods/donate/ItemRecover.htm");
		int entriesPerPage = ITEM_PER_PAGE;
		List<RecoverableItem> recoverableItems = new ArrayList<>();
	
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT * FROM item_recover WHERE object_id = ?"))
		{
			ps.setInt(1, player.getObjectId());
			
			ResultSet rs = ps.executeQuery();
			while (rs.next())
			{
		
				RecoverableItem item = new RecoverableItem(rs.getInt("id"), rs.getInt("item_id"), rs.getInt("enchant_level"), rs.getString("item_name"));
				recoverableItems.add(item);
				
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		final Pagination<RecoverableItem> list = new Pagination<>(recoverableItems.stream(), page, ITEM_PER_PAGE);
		
		final StringBuilder sb = new StringBuilder();
		int row = 0;
		
		int startIndex = (page - 1) * entriesPerPage;
		int endIndex = Math.min(startIndex + entriesPerPage, recoverableItems.size());
		
		for (int i = startIndex; i < endIndex; i++)
		{
			RecoverableItem recover = recoverableItems.get(i);
			
			sb.append((row % 2 == 0) ? "<table bgcolor=\"000000\"><tr>" : "<table><tr>");
			
			sb.append("<td height=\"40\" width=\"40\"><img src=\"" + recover.getIcon() + "\" width=\"32\" height=\"32\"></td>");
			String itemName = recover.getItemName().length() > 22 ? recover.getItemName().substring(0, 22) + "..." : recover.getItemName();
			itemName = "<font color=\"0099FF\">" + itemName + "</font>";
			sb.append("<td width=190 align=center>" + itemName + "<br1><font color=\"LEVEL\">Price per: </font>" + Config.DONATE_ITEM_RECOVER_COUNT + " | <font color=\"LEVEL\">Enchant: </font>" + recover.getEnchantLevel() + "</td>");
			
			int setItemId = recover.getItemId();
			sb.append("<td height=\"40\" width=\"40\"><button action=\"bypass -h donate recover " + player.getName() + " " + setItemId + " " + recover.getEnchantLevel() + " " + page + "\" width=32 height=32 back=\"exclusive.LCoinShopWnd_Buy_Button_Over\" fore=\"exclusive.LCoinShopWnd_Buy_Button\"></td>");
			
			sb.append("</tr></table>");
			sb.append("<img src=\"L2UI.SquareGray\" width=\"300\" height=\"1\"/>");
			row++;
		}

		
		
		sb.append("<table width=\"300\" height=\"15\" bgcolor=\"000000\"><tr>");
		if (page > 1)
			sb.append("<td align=left width=70><a action=\"bypass donate recover_list " + String.valueOf(page - 1) + "\">Previous</a></td>");
		else
			sb.append("<td align=left width=70>Previous</td>");
		
		sb.append("<td align=center width=128> Page: " + page + "</td>");
		if (list.size() > page)
			sb.append("<td align=right width=70><a action=\"bypass donate recover_list " + String.valueOf(page + 1) + "\">Next</a></td>");
		else
			sb.append("<td align=right width=70>Next</td>");
		
		sb.append("</tr></table>");
		sb.append("<img src=\"L2UI.SquareGray\" width=\"300\" height=\"1\"/>");
		
		content.replace("%name%", player.getName());
		content.replace("%objectId%", "" + player.getObjectId());
		content.replace("%list%", sb.toString());
		player.sendPacket(content);
	}
	
	public static void recover(Player player, Tokenizer tokenizer)
	{
		String target = tokenizer.getToken(2);
		int itemId = tokenizer.getAsInteger(3, 0);
		int enchantLevel = tokenizer.getAsInteger(4, 0);
		int page = tokenizer.getAsInteger(5, 0);
		Player fakePlayer = World.getInstance().getPlayer(target);
		ItemRecoveryManager.getInstance().recoverSelectedItem(fakePlayer, itemId, enchantLevel, page);
	}
	
}
