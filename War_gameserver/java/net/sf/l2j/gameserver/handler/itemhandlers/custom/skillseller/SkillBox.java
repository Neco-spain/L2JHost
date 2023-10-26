package net.sf.l2j.gameserver.handler.itemhandlers.custom.skillseller;

import l2jhost.data.custom.SkillBox.SkillBoxData;
import l2jhost.data.custom.SkillBox.SkillBoxItem;
import net.sf.l2j.commons.random.Rnd;
import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.enums.skills.SkillType;
import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.handler.ISkillHandler;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;
import net.sf.l2j.gameserver.skills.L2Skill;

public class SkillBox implements IItemHandler, ISkillHandler {

    @Override
    public void useItem(Playable playable, ItemInstance item, boolean forceUse) {
        if (!(playable instanceof Player))
            return;

        final Player activeChar = (Player) playable;
        final int itemId = item.getItemId();

        SkillBoxItem skillBoxItem = SkillBoxData.getInstance().getSkillBoxItemById(itemId);
        if (skillBoxItem != null) {
            if (activeChar.getStatus().getLevel() < skillBoxItem.getPlayerLevel()) {
                activeChar.sendMessage("Para usar esta SkillBox, você precisa do nível " + skillBoxItem.getPlayerLevel() + ".");
                return;
            }

            for (SkillBoxItem.Skill skillDetails : skillBoxItem.getSkills()) {
                // Assuming a method exists to get L2Skill object from skillId. Adjust as per actual implementation.
                L2Skill newSkill = SkillTable.getInstance().getInfo(skillDetails.getSkillId(), skillDetails.getAmount());
                if (!activeChar.addSkill(newSkill, true, true)) {
                    activeChar.sendMessage("Você já possui a habilidade: " + skillDetails.getSkillId());
                } else {
                    activeChar.sendMessage("Você adquiriu uma nova habilidade: " + skillDetails.getSkillId());
                }
            }

        } else {
            activeChar.sendMessage("Esta SkillBox expirou ou é inválida!");
        }

        playable.destroyItem("Consume", item.getObjectId(), 1, null, false);
    }

    @Override
    public void useSkill(Creature creature, L2Skill skill, WorldObject[] targets, ItemInstance item) {

    }

    @Override
    public SkillType[] getSkillIds() {
        return new SkillType[0];
    }
}
