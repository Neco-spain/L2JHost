package net.sf.l2j.gameserver.handler.itemhandlers;

import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;

import java.util.Calendar;

public class CoinPremium extends ItemSkills{

    public void useItem(Playable playable, ItemInstance item, boolean forceUse) {
        if (!(playable instanceof Player))
            return;
        Player player = (Player) playable;
        if (player.getPremiumService() != 0) {
            player.sendPacket(SystemMessageId.NOT_AUTHORIZED_TO_BESTOW_RIGHTS);
            return;
        }
        player.destroyItem("Consume", item.getObjectId(), 1, (WorldObject) player, true);
        player.setPremiumService(1);

        // Add 30 days of premium service
        Calendar finishtime = Calendar.getInstance();
        finishtime.add(Calendar.DAY_OF_MONTH, 30);
        // TODO: Save the end date (finishtime) to the database for this player

        MagicSkillUse MSU = new MagicSkillUse(player, player, 2024, 1, 1, 0);
        player.broadcastPacket(MSU);
    }
}
