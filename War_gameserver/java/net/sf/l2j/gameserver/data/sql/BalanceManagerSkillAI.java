package net.sf.l2j.gameserver.data.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import net.sf.l2j.commons.pool.ConnectionPool;

import net.sf.l2j.gameserver.enums.actors.ClassId;

public class BalanceManagerSkillAI
{

	private Map<Integer, Map<Integer, Integer>> skillModifiers = new HashMap<>();
	
	
	protected BalanceManagerSkillAI()
	{
		skillModifiers = new HashMap<>();
		loadSkillModifiers();
	}
	
	private void loadSkillModifiers()
	{
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT * FROM balance_skill");
			ResultSet rs = ps.executeQuery())
		{
			while (rs.next())
			{
				
				int from = rs.getInt("from_type");
				int to = rs.getInt("to_type");
				int mod = rs.getInt("mod_val");
				
				if (skillModifiers.containsKey(from))
					skillModifiers.get(from).put(to, mod);
				else
				{
					Map<Integer, Integer> temp = new HashMap<>();
					temp.put(to, mod);
					skillModifiers.put(from, temp);
				}
				
			
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	

	public int getPowerMod(int player, int target)
	{
		int actualFrom = player;
		int actualTo = target;
		
		if (!skillModifiers.containsKey(player))
		{
			for (ClassId ci : ClassId.values())
			{
				if (skillModifiers.containsKey(ci.getId()) && ci.getParent().getId() == player)
				{
					actualFrom = ci.getId();
					break;
				}
			}
		}
		
		if (skillModifiers.get(actualFrom) != null && !skillModifiers.get(actualFrom).containsKey(target))
		{
			for (ClassId ci : ClassId.values())
			{
				if (skillModifiers.get(actualFrom).containsKey(ci.getId()) && ci.getParent().getId() == target)
				{
					actualTo = ci.getId();
					break;
				}
			}
		}
		
		if (skillModifiers.containsKey(actualFrom))
		{
			if (skillModifiers.get(actualFrom).containsKey(actualTo))
				return skillModifiers.get(actualFrom).get(actualTo);
		}
		return 0;
	}
	
	public void Reload()
	{
		skillModifiers.clear();
		skillModifiers = new HashMap<>();
		loadSkillModifiers();
	}
	public void addPowerMod(int player, int target, int mod)
	{
		try (Connection con = ConnectionPool.getConnection())
		{
			if (skillModifiers.containsKey(player) && skillModifiers.get(player).containsKey(target))
			{
				try (PreparedStatement ps = con.prepareStatement("UPDATE balance_skill SET mod_val=? WHERE from_type=? AND to_type=?"))
				{
					ps.setInt(1, mod);
					ps.setInt(2, player);
					ps.setInt(3, target);
					ps.execute();
				}
			}
			else
			{
				try (PreparedStatement ps = con.prepareStatement("INSERT INTO balance_skill VALUES (?,?,?)"))
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
		
		if (skillModifiers.containsKey(player))
			skillModifiers.get(player).put(target, mod);
		else
		{
			Map<Integer, Integer> temp = new HashMap<>();
			temp.put(target, mod);
			skillModifiers.put(player, temp);
		}
		Reload();
	}
	
	public static BalanceManagerSkillAI getInstance()
	{
		return SingletonHolder.instance;
	}
	
	private static final class SingletonHolder
	{
		protected static final BalanceManagerSkillAI instance = new BalanceManagerSkillAI();
	}
}
