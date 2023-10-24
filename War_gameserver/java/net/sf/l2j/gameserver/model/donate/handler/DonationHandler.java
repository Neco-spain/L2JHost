package net.sf.l2j.gameserver.model.donate.handler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.sf.l2j.commons.pool.ConnectionPool;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.data.xml.ItemData;
import net.sf.l2j.gameserver.enums.items.ItemLocation;
import net.sf.l2j.gameserver.idfactory.IdFactory;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.kind.Item;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

public class DonationHandler
{
	
	private ScheduledExecutorService executorService;
	
	public void start()
	{
		executorService = Executors.newSingleThreadScheduledExecutor();
		executorService.scheduleWithFixedDelay(this::processDonations, 0, 1, TimeUnit.MINUTES);
		executorService.scheduleWithFixedDelay(this::processSaldo, 0, 1, TimeUnit.MINUTES);
	}
	
	public void stop()
	{
		if (executorService != null && !executorService.isShutdown())
		{
			executorService.shutdown();
		}
	}
	
	private void processSaldo()
	{
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT * FROM site_balance WHERE saldo > 0"))
		{
			
			ResultSet rs = ps.executeQuery();
			while (rs.next())
			{
				String account = rs.getString("account");
				int saldo = rs.getInt("saldo");
				
				String characterName = getCharacterNameFromAccount(account);
				
				if (characterName != null)
				{
					boolean success = deliverItemToCharacter(characterName, saldo);
					if (success)
					{
						updatePlayerBalance(account);
					}
				}
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	private boolean deliverItemToCharacter(String account, int quantCoins)
	{
		Player target = null;
		int idval = Config.DONATE_ITEMID;
		
		target = World.getInstance().getPlayer(account);
		
		if (target != null)
			createItem(target, idval, quantCoins, getItemLocation("inventory"));
		else
			giveItemToOfflinePlayer(account, idval, quantCoins, getItemLocation("inventory"));
		
		return true;
	}
	
	private String getCharacterNameFromAccount(String account)
	{
		String characterName = null;
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT char_name FROM characters WHERE account_name = ?"))
		{
			
			ps.setString(1, account);
			try (ResultSet rs = ps.executeQuery())
			{
				if (rs.next())
				{
					characterName = rs.getString("char_name");
				}
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return characterName;
	}
	
	private void processDonations()
	{
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT * FROM site_donations WHERE status = 4"))
		{
			ResultSet rs = ps.executeQuery();
			while (rs.next())
			{
				// Extract donation details
				int protocolo = rs.getInt("protocolo");
				String account = rs.getString("account");
				int quantCoins = rs.getInt("quant_coins");
				int status = rs.getInt("status");
				
				// Perform item delivery logic for status 4 (Entregue)
				boolean success = deliverItemToPlayer(account, quantCoins, status);
				if (success)
				{
					updateStatusToDelivered(protocolo);
					updatePlayerBalance(account);
				}
				
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	private boolean deliverItemToPlayer(String account, int quantCoins, int status)
	{
		Player target = null;
		int idval = Config.DONATE_ITEMID;
		
		target = World.getInstance().getPlayer(account);
		
		if (status == 4) // Status 4 corresponds to "Entregue"
		{
			if (target != null)
				createItem(target, idval, quantCoins, getItemLocation("inventory"));
			else
				giveItemToOfflinePlayer(account, idval, quantCoins, getItemLocation("inventory"));
			
		}
		
		return true;
	}
	
	private void updatePlayerBalance(String account)
	{
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement("UPDATE site_balance SET saldo = 0 WHERE account = ?"))
		{
			ps.setString(1, account);
			ps.executeUpdate();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	private static void createItem(Player player, int id, int count, ItemLocation location)
	{
		Item item = ItemData.getInstance().getTemplate(id);
		if (item == null)
		{
			return;
		}
		
		if (count > 10 && !item.isStackable())
		{
			return;
		}
		
		if (location == ItemLocation.INVENTORY)
			player.getInventory().addItem("Admin", id, count, player, player);
		
		if (count > 1)
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_PICKED_UP_S2_S1).addItemName(id).addNumber(count));
		else
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_PICKED_UP_S1).addItemName(id));
		
	}
	
	private static ItemLocation getItemLocation(String name)
	{
		ItemLocation location = null;
		if (name.equalsIgnoreCase("inventory"))
			location = ItemLocation.INVENTORY;
		return location;
	}
	
	private void updateStatusToDelivered(int protocolo)
	{
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement("UPDATE site_donations SET status = 6 WHERE protocolo = ?"))
		{
			ps.setInt(1, protocolo);
			ps.executeUpdate();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void giveItemToOfflinePlayer(String playername, int id, int count, ItemLocation location)
	{
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement selectStatement = con.prepareStatement("SELECT obj_Id FROM characters WHERE char_name=?");
			PreparedStatement insertStatement = con.prepareStatement("INSERT INTO items (owner_id,item_id,count,loc,loc_data,enchant_level,object_id,custom_type1,custom_type2,mana_left,time) VALUES (?,?,?,?,?,?,?,?,?,?,?)"))
		{
			
			Item item = ItemData.getInstance().getTemplate(id);
			int objectId = IdFactory.getInstance().getNextId();
			
			selectStatement.setString(1, playername);
			try (ResultSet result = selectStatement.executeQuery())
			{
				int objId = 0;
				
				if (result.next())
				{
					objId = result.getInt(1);
				}
				
				if (objId == 0)
				{
					return;
				}
				
				if (item == null)
				{
					return;
				}
				
				if (count > 1 && !item.isStackable())
				{
					return;
				}
				
				insertStatement.setInt(1, objId);
				insertStatement.setInt(2, item.getItemId());
				insertStatement.setInt(3, count);
				insertStatement.setString(4, location.name());
				insertStatement.setInt(5, 0);
				insertStatement.setInt(6, 0);
				insertStatement.setInt(7, objectId);
				insertStatement.setInt(8, 0);
				insertStatement.setInt(9, 0);
				insertStatement.setInt(10, -1);
				insertStatement.setLong(11, 0);
				
				insertStatement.executeUpdate();
				
			}
		}
		catch (SQLException e)
		{
			
		}
	}
}
