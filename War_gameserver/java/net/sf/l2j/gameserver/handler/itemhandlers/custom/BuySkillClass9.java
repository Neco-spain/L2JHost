package net.sf.l2j.gameserver.handler.itemhandlers.custom;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.skills.L2Skill;

import java.util.Map;

public class BuySkillClass9 implements IItemHandler {

    @Override
    public void useItem(Playable playable, ItemInstance item, boolean forceUse) {
        if (!(playable instanceof Player)) {
            return;
        }

        Player player = (Player) playable;

        // Iterar sobre todas as entradas no mapa RANDOM_SKILLS
        for (Map.Entry<Integer, Integer> skillEntry : Config.SKILLCLASS9.entrySet()) {
            int skillId = skillEntry.getKey();
            int skillLevel = skillEntry.getValue();

            // Verifique se o jogador já possui a habilidade
            L2Skill skill = player.getSkill(skillId);
            if (skill != null) {
                continue;  // Se o jogador já tem a habilidade, continue para a próxima
            }

            // Adicione a habilidade ao jogador
            skill = SkillTable.getInstance().getInfo(skillId, skillLevel);
            if (skill != null) {
                player.addSkill(skill, true);
            }
        }

        // Atualize a mensagem HTML para refletir a mudança
        NpcHtmlMessage html = new NpcHtmlMessage(0);
        html.setHtml("<html><body><center>Congratulations!</center><br>You have acquired all the skills!</body></html>");
        player.sendPacket(html);

        playable.destroyItem("Consume", item.getObjectId(), 1, null, false);
    }

}

