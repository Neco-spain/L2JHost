package net.sf.l2j.gameserver.handler.itemhandlers;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.idfactory.IdFactory;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;

import l2jhost.data.custom.CapsuleBox.CapsuleBoxData;
import l2jhost.data.custom.CapsuleBox.CapsuleBoxItem;
import l2jhost.data.custom.CapsuleBox.CapsuleBoxItem.Item;

public class CapsuleBox_System implements IItemHandler {

    @Override
    public void useItem(Playable playable, ItemInstance item, boolean forceUse) {
        if (!(playable instanceof Player))
            return;

        final Player activeChar = (Player) playable;
        final int itemId = item.getItemId();

        CapsuleBoxItem capsuleBoxItem = CapsuleBoxData.getInstance().getCapsuleBoxItemById(itemId);
        if (capsuleBoxItem != null) {
            if (activeChar.getStatus().getLevel() < capsuleBoxItem.getPlayerLevel()) {
                activeChar.sendMessage("Para Usar Esta Capsule Box Necesitas El LvL." + capsuleBoxItem.getPlayerLevel());
                return;
            }

            ItemInstance toGive = null;
            for (Item boxItem : capsuleBoxItem.getItems()) {
                toGive = new ItemInstance(IdFactory.getInstance().getNextId(), boxItem.getItemId());
                int random = Rnd.get(100);
                if (random < boxItem.getChance()) {
                    if (!toGive.isStackable()) {
                        toGive.setEnchantLevel(boxItem.getEnchantLevel());
                        activeChar.addItem("CapsuleBox", toGive, activeChar, true);
                    } else {
                        activeChar.addItem("CapsuleBox", boxItem.getItemId(), boxItem.getAmount(), activeChar, true);
                    }
                } else {
                    
                }
                MagicSkillUse MSU = new MagicSkillUse(activeChar, activeChar, 2024, 1, 1, 0);
                activeChar.broadcastPacket(MSU);
               
            }
           
        } else {
            activeChar.sendMessage("This Capsule box expired or is invalid!");
        }

        playable.destroyItem("Consume", item.getObjectId(), 1, null, false);
    }
}