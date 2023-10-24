package net.sf.l2j.gameserver.handler.itemhandlers;

import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.L2GameServerPacket;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

public class LevelCoin extends ItemSkills {
	public void useItem(Playable playable, ItemInstance item, boolean forceUse) {
		if (!(playable instanceof Player))
			return;
		Player player = (Player) playable;
		if (player.getStatus().getLevel() == 81 || player.getStatus().getLevel() == 85) {
			player.sendMessage("Sorry more you are already max level.");
			return;
		}
		player.destroyItem("Consume", item.getObjectId(), 1, (WorldObject) player, true);
		player.getStatus().addExp(item.getItem().getExp());
		player.sendPacket((L2GameServerPacket) SystemMessage.getSystemMessage(SystemMessageId.EARNED_S1_EXPERIENCE)
				.addNumber(item.getItem().getExp()));
	}
}
