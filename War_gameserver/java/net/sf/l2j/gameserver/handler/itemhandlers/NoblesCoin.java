package net.sf.l2j.gameserver.handler.itemhandlers;

import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;

public class NoblesCoin extends ItemSkills {
	public void useItem(Playable playable, ItemInstance item, boolean forceUse) {
		if (!(playable instanceof Player))
			return;
		Player player = (Player) playable;
		if (player.isNoble()) {
			player.sendPacket(SystemMessageId.NOT_AUTHORIZED_TO_BESTOW_RIGHTS);
			return;
		}
		player.destroyItem("Consume", item.getObjectId(), 1, (WorldObject) player, true);
		player.setNoble(true, true);
		player.getInventory().addItem("Nobles Circlets", 7694, 1, player, (WorldObject) player);
		player.broadcastUserInfo();
	}
}
