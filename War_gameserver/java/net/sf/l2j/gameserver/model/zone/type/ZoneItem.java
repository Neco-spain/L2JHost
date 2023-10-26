package net.sf.l2j.gameserver.model.zone.type;

import net.sf.l2j.Config;
import net.sf.l2j.commons.pool.ThreadPool;
import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.enums.ZoneId;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.model.location.Location;
import net.sf.l2j.gameserver.model.zone.type.subtype.SpawnZoneType;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.ExShowScreenMessage;
import net.sf.l2j.gameserver.skills.L2Skill;

import java.util.concurrent.TimeUnit;

public class ZoneItem extends SpawnZoneType
{
	private static int REQUIRED_ITEM_ID = Config.ITEM_ZONE;
	private static final Location TELEPORT_LOCATION = new Location(10614, -23917, -3648);
    private static final int BUFF_ID = Config.BUFFER_ZONE; // Substitua YOUR_BUFF_ID pelo ID real do buff
    private static final int BUFF_LEVEL = 1; // Nível do buff. Ajuste conforme necessário.


	
	
	public ZoneItem(int id)
	{
		super(id);
	}
	
	@Override
	protected void onEnter(Creature character)
	{
		if (character instanceof Player)
		{
			Player player = (Player) character;
			
			if (player.getInventory().getItemCount(REQUIRED_ITEM_ID, -1) < (1))
			{
				player.sendPacket(new ExShowScreenMessage("You don't have required items!", (int) TimeUnit.SECONDS.toMillis(3)));
				ThreadPool.schedule(() -> player.teleportTo(TELEPORT_LOCATION, 20), 1000);
				return;
			}
            L2Skill skill = SkillTable.getInstance().getInfo(BUFF_ID, BUFF_LEVEL);
            if (skill != null)
            {
                player.addSkill(skill, true);
                player.sendSkillList();
            }
			player.sendPacket(SystemMessageId.ENTERED_COMBAT_ZONE);
		}

		character.setInsideZone(ZoneId.ZONE_ITEM, true);
		character.setInsideZone(ZoneId.NO_SUMMON_FRIEND, false);
	}

	@Override
	protected void onExit(Creature character)
	{
		character.setInsideZone(ZoneId.ZONE_ITEM, false);
		character.setInsideZone(ZoneId.NO_SUMMON_FRIEND, false);

		if (character instanceof Player)
		{
			Player player = (Player) character;

			// Remove 1 of the required item from the player's inventory
			ItemInstance requiredItem = player.getInventory().getItemByItemId(REQUIRED_ITEM_ID);
			if (requiredItem != null)
			{
				player.destroyItem("ZoneExit", requiredItem, 1, player, true);  // Ajustado para remover sempre 1 item
			}

			// Remove the buff from the player
			L2Skill skill = SkillTable.getInstance().getInfo(BUFF_ID, BUFF_LEVEL);
			if (skill != null)
			{
				player.removeSkill(BUFF_ID, false);
				player.sendSkillList();
			}

			player.sendPacket(SystemMessageId.LEFT_COMBAT_ZONE);
		}
	}

}