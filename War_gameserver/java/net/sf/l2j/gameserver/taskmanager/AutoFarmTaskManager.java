package net.sf.l2j.gameserver.taskmanager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import net.sf.l2j.commons.pool.ThreadPool;

import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.ExclusiveBot;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.serverpackets.SocialAction;

public class AutoFarmTaskManager implements Runnable
{
private final Map<Player, Long> _players = new ConcurrentHashMap<>();
	
	protected AutoFarmTaskManager()
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
	
	@Override
	public final void run()
	{
		if (_players.isEmpty())
			return;
		
		for (Map.Entry<Player, Long> entry : _players.entrySet())
		{
			final Player player = entry.getKey();
			
			if (player.getMemos().getLong("botEndTime") < System.currentTimeMillis())
			{
				DisableStatbot(player);
				ExclusiveBot bot = player.getBot();
				
				if (player.isbot())
					bot.stop();
				
				remove(player);
			}
		}
	}
	
	public static void BotTimeMinutos(Player player, int time)
	{
		player.broadcastPacket(new SocialAction(player, 3));
		player.setbot(true);
		
		AutoFarmTaskManager.getInstance().add(player);
		long remainingTime = player.getMemos().getLong("botEndTime", 0);
		if (remainingTime > 0)
		{
			player.getMemos().set("botEndTime", remainingTime + TimeUnit.MINUTES.toMillis(time));
			player.sendMessage("AutoFarm Manager Dear " + player.getName() + ", your AutoFarm status has been extended by " + time + " minute(s).");
		}
		else
		{
			player.getMemos().set("botEndTime", System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(time));
			player.sendMessage("AutoFarm Manager Dear " + player.getName() + ", you got AutoFarm Status for " + time + " minute(s).");
			player.broadcastCharInfo();
		}
	}
	
	public static void BotTimehours(Player player, int time)
	{
		player.broadcastPacket(new SocialAction(player, 3));
		player.setbot(true);
		
		AutoFarmTaskManager.getInstance().add(player);
		long remainingTime = player.getMemos().getLong("botEndTime", 0);
		if (remainingTime > 0)
		{
			player.getMemos().set("botEndTime", remainingTime + TimeUnit.HOURS.toMillis(time));
			player.sendMessage("AutoFarm Manager Dear " + player.getName() + ", your AutoFarm status has been extended by " + time + " day(s).");
		}
		else
		{
			player.getMemos().set("botEndTime", System.currentTimeMillis() + TimeUnit.HOURS.toMillis(time));
			player.sendMessage("AutoFarm Manager Dear " + player.getName() + ", you got AutoFarm Status for " + time + " day(s).");
			player.broadcastCharInfo();
		}
	}
	
	public static void BotTimedays(Player player, int time)
	{
		player.broadcastPacket(new SocialAction(player, 3));
		player.setbot(true);
		
		AutoFarmTaskManager.getInstance().add(player);
		long remainingTime = player.getMemos().getLong("botEndTime", 0);
		if (remainingTime > 0)
		{
			player.getMemos().set("botEndTime", remainingTime + TimeUnit.DAYS.toMillis(time));
			player.sendMessage("AutoFarm Manager Dear " + player.getName() + ", your AutoFarm status has been extended by " + time + " day(s).");
		}
		else
		{
			player.getMemos().set("botEndTime", System.currentTimeMillis() + TimeUnit.DAYS.toMillis(time));
			player.sendMessage("AutoFarm Manager Dear " + player.getName() + ", you got AutoFarm Status for " + time + " day(s).");
			player.broadcastCharInfo();
		}
	}
	
	public static void removeTask(Player player)
	{
		AutoFarmTaskManager.getInstance().remove(player);
		player.getMemos().set("botEndTime", 0);
		player.setbot(false);
	}
	public static void DisableStatbot(Player player)
	{
		AutoFarmTaskManager.getInstance().remove(player);
		player.getMemos().set("botEndTime", 0);
		player.setbot(false);
		player.sendMessage("AutoFarm Manager Dear " + player.getName() + ", Your AutoFarm period is over.");
		player.broadcastPacket(new SocialAction(player, 13));
		player.broadcastCharInfo();
	}
	
	public static final AutoFarmTaskManager getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder
	{
		protected static final AutoFarmTaskManager _instance = new AutoFarmTaskManager();
	}
}
