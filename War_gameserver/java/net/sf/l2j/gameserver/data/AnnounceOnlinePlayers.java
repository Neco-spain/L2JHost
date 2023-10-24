package net.sf.l2j.gameserver.data;

import net.sf.l2j.commons.pool.ThreadPool;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.World;

public class AnnounceOnlinePlayers
{
    public static void getInstance()
    {
        ThreadPool.scheduleAtFixedRate(new Runnable()
        {
            @Override
            public void run()
            {
                Announce();
            }
        }, 0,Config.ANNOUNCE_ONLINE_PLAYERS_DELAY * 1000);
     }
    
	protected static void Announce()
	{
		int NumberofPlayers = World.getInstance().getPlayers().size();
		
		if (NumberofPlayers == 1)
			World.announceToOnlinePlayers("Record " + NumberofPlayers + " player is online.");
		else
			World.announceToOnlinePlayers("Record " + NumberofPlayers + " player are online.");
	}
    
    

    }
