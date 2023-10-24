package net.sf.l2j.gameserver.data.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import net.sf.l2j.commons.pool.ConnectionPool;

import net.sf.l2j.gameserver.enums.actors.ClassId;

public class BalanceManagerAI
{
	private Map<Integer, Map<Integer, Integer>> valuesPower = new HashMap<>();
	
	protected BalanceManagerAI()
	{
		valuesPower = new HashMap<>();
		ConnectLoad();
		
	}
	
	public void Reload()
	{
		valuesPower.clear();
		valuesPower = new HashMap<>();
		ConnectLoad();
	}
	public void ConnectLoad()
	{
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT * FROM balance");
			ResultSet rs = ps.executeQuery())
		{
			while (rs.next())
			{
				int from = rs.getInt("from_class");
				int to = rs.getInt("to_class");
				int mod = rs.getInt("mod_val");
				
				if (valuesPower.containsKey(from))
					valuesPower.get(from).put(to, mod);
				else
				{
					Map<Integer, Integer> temp = new HashMap<>();
					temp.put(to, mod);
					valuesPower.put(from, temp);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	public void addPowerMod(int player, int target, int mod)
	{
		try (Connection con = ConnectionPool.getConnection())
		{
			if (valuesPower.containsKey(player) && valuesPower.get(player).containsKey(target))
			{
				try (PreparedStatement ps = con.prepareStatement("UPDATE balance SET mod_val=? WHERE from_class=? AND to_class=?"))
				{
					ps.setInt(1, mod);
					ps.setInt(2, player);
					ps.setInt(3, target);
					ps.execute();
				}
			}
			else
			{
				try (PreparedStatement ps = con.prepareStatement("INSERT INTO balance VALUES (?,?,?)"))
				{
					ps.setInt(1, player);
					ps.setInt(2, target);
					ps.setInt(3, mod);
					ps.execute();
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		if (valuesPower.containsKey(player))
			valuesPower.get(player).put(target, mod);
		else
		{
			Map<Integer, Integer> temp = new HashMap<>();
			temp.put(target, mod);
			valuesPower.put(player, temp);
		}
		Reload();
	}
	
	public int getPowerMod(int player, int target)
	{
		int actualFrom = player;
		int actualTo = target;
		
		if (!valuesPower.containsKey(player))
		{
			for (ClassId ci : ClassId.values())
			{
				if (valuesPower.containsKey(ci.getId()) && ci.getParent().getId() == player)
				{
					actualFrom = ci.getId();
					break;
				}
			}
		}
		
		if (valuesPower.get(actualFrom) != null && !valuesPower.get(actualFrom).containsKey(target))
		{
			for (ClassId ci : ClassId.values())
			{
				if (valuesPower.get(actualFrom).containsKey(ci.getId()) && ci.getParent().getId() == target)
				{
					actualTo = ci.getId();
					break;
				}
			}
		}
		
		if (valuesPower.containsKey(actualFrom))
		{
			if (valuesPower.get(actualFrom).containsKey(actualTo))
				return valuesPower.get(actualFrom).get(actualTo);
		}
		return 0;
	}
	
	public static BalanceManagerAI getInstance()
	{
		return SingletonHolder.instance;
	}
	
	private static final class SingletonHolder
	{
		protected static final BalanceManagerAI instance = new BalanceManagerAI();
	}
}
