package net.sf.l2j.gameserver.model.donate.previwer;

import java.util.concurrent.TimeUnit;

import net.sf.l2j.commons.data.Pagination;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.data.xml.ItemData;
import net.sf.l2j.gameserver.enums.FloodProtector;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.donate.data.WeaponSetData;
import net.sf.l2j.gameserver.model.donate.holder.WeaponSet;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.ExShowScreenMessage;
import net.sf.l2j.gameserver.network.serverpackets.InventoryUpdate;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

public class HtmlWeapon
{
	private static final int ARMOR_PER_PAGE = 7;
	
	public static void listWeapon(Player player, int page)
	{
		final NpcHtmlMessage content = new NpcHtmlMessage(0);
		content.setFile(player.isLang() + "mods/donate/ListWeapon.htm");
		boolean isTableGenerated = false;
		
		final Pagination<WeaponSet> list = new Pagination<>(WeaponSetData.getInstance().getSets().stream(), page, ARMOR_PER_PAGE);
		
		final StringBuilder sb = new StringBuilder();
		int row = 0;
		int count = 0;
		for (WeaponSet armorSet : list)
		{
			if (!isTableGenerated)
			{
				ItemInstance item = player.getInventory().getItemByItemId(armorSet.getPrinceId());
				if (item != null)
				{
					count = item.getCount();
				}
				sb.append("<img src=\"L2UI.SquareGray\" width=\"300\" height=\"1\"/>");
				sb.append("<table width=\"300\" height=\"15\" bgcolor=\"000000\"><tr>");
				
				sb.append("<td align=left width=30><button value=\"\" action=\"bypass voiced_donate\" width=\"32\" height=\"23\" back=\"exclusive.Botsystem_DF_KeyBack_over\" fore=\"exclusive.Botsystem_DF_KeyBack\" /></td>");
				sb.append("<td align=center width=180>You Have, <font color=\"LEVEL\">" + ItemData.getInstance().getTemplate(armorSet.getPrinceId()).getName() + "</font>" + " Count: " + "{" + count + "}" + "</td>");
				
				sb.append("<td align=right width=30></td>");
				
				sb.append("</tr></table>");
				sb.append("<img src=\"L2UI.SquareGray\" width=\"300\" height=\"1\"/>");
				isTableGenerated = true;
			}
			
			sb.append((row % 2 == 0) ? "<table bgcolor=\"000000\"><tr>" : "<table><tr>");
			
			sb.append("<td height=\"40\" width=\"40\"><img src=\"" + armorSet.getIcon() + "\" width=\"32\" height=\"32\"></td>");
			String itemName = armorSet.getName().length() > 22 ? armorSet.getName().substring(0, 22) + "..." : armorSet.getName();
			itemName = "<font color=\"0099FF\">" + itemName + "</font>";
			sb.append("<td width=190 align=center>" + itemName + "<br1><font color=\"LEVEL\">Price per: </font>" + armorSet.getPrinceCont() + " | <font color=\"LEVEL\">Enchant: </font>" + armorSet.getEnchantLevel() + "</td>");
			
			int setItemId = armorSet.getWeapons()[0];
			sb.append("<td height=\"40\" width=\"40\"><button action=\"bypass donate create_weapon " + player.getName() + " " + setItemId + " " + page + "\" width=32 height=32 back=\"exclusive.LCoinShopWnd_Buy_Button_Over\" fore=\"exclusive.LCoinShopWnd_Buy_Button\"></td>");
			
			sb.append("</tr></table>");
			sb.append("<img src=\"L2UI.SquareGray\" width=\"300\" height=\"1\"/>");
			row++;
		}
		
		sb.append("<table width=\"300\" height=\"15\" bgcolor=\"000000\"><tr>");
		if (page > 1)
			sb.append("<td align=left width=70><a action=\"bypass donate list_weapon " + String.valueOf(page - 1) + "\">Previous</a></td>");
		else
			sb.append("<td align=left width=70>Previous</td>");
		
		sb.append("<td align=center width=128> Page: " + page + "</td>");
		if (list.size() > page)
			sb.append("<td align=right width=70><a action=\"bypass donate list_weapon " + String.valueOf(page + 1) + "\">Next</a></td>");
		else
			sb.append("<td align=right width=70>Next</td>");
		
		sb.append("</tr></table>");
		sb.append("<img src=\"L2UI.SquareGray\" width=\"300\" height=\"1\"/>");
		
		content.replace("%name%", player.getName());
		content.replace("%objectId%", "" + player.getObjectId());
		content.replace("%list%", sb.toString());
		player.sendPacket(content);
		
	}
	
	public static void handleCreateWeaponEquipped(Player player, Player target, int chestId, int page)
	{
		try
		{
			final WeaponSet armorSet = WeaponSetData.getInstance().getSet(chestId);
			
			if (armorSet == null)
			{
				target.sendMessage("This chest has no set.");
				return;
			}
			
			if (target.getInventory().getItemCount(armorSet.getPrinceId(), -1) < (armorSet.getPrinceCont()))
			{
				target.sendPacket(new ExShowScreenMessage("You don't have required items! " + ItemData.getInstance().getTemplate(armorSet.getPrinceId()).getName() + " " + armorSet.getPrinceCont(), (int) TimeUnit.SECONDS.toMillis(2)));
				return;
			}
			
			if (!target.getClient().performAction(FloodProtector.DONATE_BYPASS))
			{
				
				int seconds = (int) Math.ceil(Config.DONATE_BYPASS_TIME / 1000.0);
				target.sendPacket(new ExShowScreenMessage("Wait more " + seconds + " seconds before using the command again.", (int) TimeUnit.SECONDS.toMillis(3)));
				target.sendPacket(SystemMessageId.YOU_CAN_NOT_TRY_THOSE_ITEMS_ON_AT_THE_SAME_TIME);
				return;
			}
			
			if (!target.getInventory().validateCapacity(1))
			{
				target.sendPacket(SystemMessageId.SLOTS_FULL);
				return;
			}
			
		
			for (int itemId : armorSet.getWeapons())
			{
				if (itemId > 0)
				{
					ItemInstance item = target.addItem("AuctionPurchase", itemId, 1, target, true);
					item.setEnchantLevel(armorSet.getEnchantLevel());
					target.useEquippableItem(item, true);
					InventoryUpdate playerIU = new InventoryUpdate();
					playerIU.addItem(item);
					target.sendPacket(playerIU);
					
				}
			}
			target.broadcastCharInfo();
			target.broadcastUserInfo();
			target.destroyItemByItemId("Tkt", armorSet.getPrinceId(), armorSet.getPrinceCont(), target, true);
			target.sendPacket(new ExShowScreenMessage(target.getName() + " You have successfully purchase item. " + armorSet.getName(), (int) TimeUnit.SECONDS.toMillis(5)));
			
			listWeapon(player, page);
			
		}
		
		catch (Exception e)
		{
			target.sendMessage("An error occurred while handling the command.");
		}
	}
}
