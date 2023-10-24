package net.sf.l2j.gameserver.data.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import net.sf.l2j.commons.pool.ConnectionPool;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.data.xml.ItemData;
import net.sf.l2j.gameserver.enums.FloodProtector;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.ExShowScreenMessage;
import net.sf.l2j.gameserver.network.serverpackets.InventoryUpdate;

public class ItemRecoveryManager
{
	public ItemRecoveryManager()
	{
		
	}
	
	public void isInsertItem(int objectId, int itemId, String itemName, int enchantLevel, String itemType, int itemCount)
	{
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement checkPs = con.prepareStatement("SELECT item_count FROM item_recover WHERE object_id = ? AND item_id = ?");
			PreparedStatement insertPs = con.prepareStatement("INSERT INTO item_recover (object_id, item_id, item_name, enchant_level, item_type, item_count) VALUES (?,?,?,?,?,?)");
			PreparedStatement updatePs = con.prepareStatement("UPDATE item_recover SET item_count = item_count + ? WHERE object_id = ? AND item_id = ?"))
		{
			checkPs.setInt(1, objectId);
			checkPs.setInt(2, itemId);
			try (ResultSet resultSet = checkPs.executeQuery())
			{
				if (resultSet.next())
				{
					// Item already exists, update its count
					updatePs.setInt(1, itemCount);
					updatePs.setInt(2, objectId);
					updatePs.setInt(3, itemId);
					updatePs.executeUpdate();
				}
				else
				{
					// Item doesn't exist, insert new row
					insertPs.setInt(1, objectId);
					insertPs.setInt(2, itemId);
					insertPs.setString(3, itemName);
					insertPs.setInt(4, enchantLevel);
					insertPs.setString(5, itemType);
					insertPs.setInt(6, itemCount);
					insertPs.executeUpdate();
				}
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public void recoverSelectedItem(Player player, int itemId, int enchantLevel, int page)
	{
		
		if (!player.getClient().performAction(FloodProtector.DONATE_BYPASS))
		{
			
			int seconds = (int) Math.ceil(Config.DONATE_BYPASS_TIME / 1000.0);
			player.sendPacket(new ExShowScreenMessage("Wait more " + seconds + " seconds before using the command again.", (int) TimeUnit.SECONDS.toMillis(3)));
			player.sendPacket(SystemMessageId.YOU_CAN_NOT_TRY_THOSE_ITEMS_ON_AT_THE_SAME_TIME);
			return;
		}
		
		if (player.getInventory().getItemCount(Config.DONATE_ITEMID, -1) < (Config.DONATE_ITEM_RECOVER_COUNT))
		{
			player.sendPacket(new ExShowScreenMessage("You don't have required items! " + ItemData.getInstance().getTemplate(Config.DONATE_ITEMID).getName() + " " + Config.DONATE_ITEM_RECOVER_COUNT, (int) TimeUnit.SECONDS.toMillis(2)));
			return;
		}
		
		int validCount = getValidRecoveryItemCount(itemId, player.getObjectId());
		
		if (validCount == 0)
		{
			player.sendMessage("You cannot retrieve this item.");
			return;
		}
		
		if (!isValidEnchantLevel(itemId, enchantLevel, player.getObjectId()))
		{
			player.sendMessage("You cannot retrieve this item with this enchant level.");
			return;
		}
		try
		{
			ItemInstance item = player.addItem("AuctionPurchase", itemId, 1, player, true);
			item.setEnchantLevel(enchantLevel);
			player.useEquippableItem(item, true);
			InventoryUpdate playerIU = new InventoryUpdate();
			playerIU.addItem(item);
			player.sendPacket(playerIU);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		removeRecoverableItem(itemId, player.getObjectId());
		player.destroyItemByItemId("Tkt", Config.DONATE_ITEMID, Config.DONATE_ITEM_RECOVER_COUNT, player, true);
	}
	
	public boolean isValidEnchantLevel(int itemId, int enchantLevel, int objectId)
	{
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT enchant_level FROM item_recover WHERE object_id = ? AND item_id = ?"))
		{
			
			ps.setInt(1, objectId);
			ps.setInt(2, itemId);
			
			try (ResultSet resultSet = ps.executeQuery())
			{
				if (resultSet.next())
				{
					int validEnchantLevel = resultSet.getInt("enchant_level");
					return enchantLevel == validEnchantLevel;
				}
			}
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return false;
	}
	
	public int getValidRecoveryItemCount(int itemId, int objectId)
	{
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT item_count FROM item_recover WHERE object_id = ? AND item_id = ?"))
		{
			ps.setInt(1, objectId);
			ps.setInt(2, itemId);
			
			try (ResultSet resultSet = ps.executeQuery())
			{
				if (resultSet.next())
				{
					return resultSet.getInt("item_count");
				}
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return 0;
	}
	
	public void removeRecoverableItem(int itemId, int objectId)
	{
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement("UPDATE item_recover SET item_count = item_count - 1 WHERE item_id = ? AND object_id = ? AND item_count > 0"))
		{
			ps.setInt(1, itemId);
			ps.setInt(2, objectId);
			ps.executeUpdate();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		removeDB(itemId, objectId);
	}
	
	public void removeDB(int itemId, int objectId)
	{
		
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement("DELETE FROM item_recover WHERE item_id = ? AND object_id = ? AND item_count <= 0"))
		{
			ps.setInt(1, itemId);
			ps.setInt(2, objectId);
			ps.executeUpdate();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public static ItemRecoveryManager getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final ItemRecoveryManager INSTANCE = new ItemRecoveryManager();
	}
}
