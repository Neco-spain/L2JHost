package net.sf.l2j.gameserver.model.zone.type;

import net.sf.l2j.gameserver.enums.ZoneId;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.zone.type.subtype.SpawnZoneType;
import net.sf.l2j.gameserver.model.zone.type.subtype.ZoneType;
import net.sf.l2j.gameserver.network.SystemMessageId;

/**
 * A zone extending {@link ZoneType}, where summoning is forbidden. The place is considered a pvp zone (no flag, no karma). It is used for arenas.
 */
public class ArenaEventZone extends SpawnZoneType
{
	public ArenaEventZone(int id)
	{
		super(id);
	}
	
	@Override
	protected void onEnter(Creature character)
	{
		if (character instanceof Player)
		{
			final Player player = (Player) character;
			
			player.sendPacket(SystemMessageId.ENTERED_COMBAT_ZONE);
			character.setInsideZone(ZoneId.ARENA_EVENT, true);
			character.setInsideZone(ZoneId.NO_SUMMON_FRIEND, true);
			character.setInsideZone(ZoneId.NO_RESTART, true);
			character.setInsideZone(ZoneId.PVP, true);
			
		}
		
	}
	
	@Override
	protected void onExit(Creature character)
	{
		
		if (character instanceof Player)
		{
			final Player player = (Player) character;
			
			character.setInsideZone(ZoneId.ARENA_EVENT, false);
			character.setInsideZone(ZoneId.NO_SUMMON_FRIEND, false);
			character.setInsideZone(ZoneId.NO_RESTART, false);
			character.setInsideZone(ZoneId.PVP, false);
			player.sendPacket(SystemMessageId.LEFT_COMBAT_ZONE);
			
		}
	}
}