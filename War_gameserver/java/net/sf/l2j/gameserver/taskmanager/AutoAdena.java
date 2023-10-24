package net.sf.l2j.gameserver.taskmanager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.l2j.commons.pool.ThreadPool;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.serverpackets.ItemList;

public class AutoAdena implements Runnable
{
	@Override
	public final void run()
	{
		if (_players.isEmpty())
			return;
		
		for (Map.Entry<Player, Long> entry : _players.entrySet())
		{
			final Player player = entry.getKey();
			
			if (player.getInventory().getItemCount(Config.BANKING_SYSTEM_GOLDBAR_ID, 0) >= 1)
			{
				player.getInventory().destroyItem("GB", Config.BANKING_SYSTEM_GOLDBAR_ID, 1, player, player);
				player.getInventory().addItem("Adena", 57, Config.BANKING_SYSTEM_ADENA, player, null);
	
				player.getInventory().updateDatabase();
				player.sendPacket(new ItemList(player, false));
			}
		}
	}
	
	private final Map<Player, Long> _players = new ConcurrentHashMap<>();
	
	protected AutoAdena()
	{
		// Run task each 10 second.
		ThreadPool.scheduleAtFixedRate(this, 1000, 1000);
	}
	
	public final void add(Player player)
	{
		_players.put(player, System.currentTimeMillis());
	}
	
	public final void remove(Creature player)
	{
		_players.remove(player);
	}
	
	public static final AutoAdena getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder
	{
		protected static final AutoAdena _instance = new AutoAdena();
	}
}