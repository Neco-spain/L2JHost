package net.sf.l2j.gameserver.model.actor.instance;

import net.sf.l2j.commons.lang.StringUtil;
import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.skills.L2Skill;

import java.util.Arrays;
import java.util.List;

public class L2SkillSellerInstance extends Npc
{
    public final static int NPC_ID = 15021992;
    public final static int ITEM_ID = 2807;
    public final static int ITEM_COUNT = 5;

    private final List<SkillGroup> SKILL_GROUPS = Arrays.asList(
            new SkillGroup("Dark Avenger", 1, new int[]{3134, 3132}, new int[]{10, 11}),
            new SkillGroup("Paladin", 2, new int[]{3124, 3125}, new int[]{10, 11})
            // ... adicione outros grupos aqui conforme necessário
    );

    private static class SkillGroup {
        String name;
        int groupId;
        int[] skillIds;
        int[] levels;

        SkillGroup(String name, int groupId, int[] skillIds, int[] levels) {
            this.name = name;
            this.groupId = groupId;
            this.skillIds = skillIds;
            this.levels = levels;
        }

        String getHtmlForGroup() {
            StringBuilder t = new StringBuilder();
            t.append("<br><b>").append(name).append("</b><br>");
            for (int i = 0; i < skillIds.length; i++) {
                L2Skill s = SkillTable.getInstance().getInfo(skillIds[i], levels[i]);
                if (s != null) {
                    t.append("<center><button value=\"" + s.getName() + " LvL:" + levels[i] + "\" action=\"bypass -h skill" + skillIds[i] + "\" width=204 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"><br>");
                }
            }
            return t.toString();
        }
    }

    public L2SkillSellerInstance(int objectId, NpcTemplate template) {
        super(objectId, template);
        setMortal(false);
    }

    @Override
    public void showChatWindow(Player player, int val) {
        final StringBuilder sb = new StringBuilder();

        if (val == 0) {
            // Mostrar a lista de grupos de habilidades
            for (SkillGroup group : SKILL_GROUPS) {
                StringUtil.append(sb, "<center><button action=\"bypass -h npc_" + getObjectId() + "_Chat_" + group.groupId + "\" value=\"" + group.name + "\" width=75 height=21 back=\"L2UI_ch3.Btn1_normalOn\" fore=\"L2UI_ch3.Btn1_normal\"></center>");
            }
        } else {
            // Mostrar as habilidades para o grupo selecionado
            SkillGroup selectedGroup = findSkillGroupById(val);
            if (selectedGroup != null) {
                StringUtil.append(sb, "<center>Skills for: " + selectedGroup.name + "</center><br>");
                for (int i = 0; i < selectedGroup.skillIds.length; i++) {
                    L2Skill s = SkillTable.getInstance().getInfo(selectedGroup.skillIds[i], selectedGroup.levels[i]);
                    if (s != null) {
                        StringUtil.append(sb, "<center><button action=\"bypass -h skill" + selectedGroup.skillIds[i] + "\" value=\"" + s.getName() + " LvL:" + selectedGroup.levels[i] + "\" width=204 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center>");
                    }
                }
                StringUtil.append(sb, "<br><center><button action=\"bypass -h npc_" + getObjectId() + "_Chat_0\" value=\"Back\" width=75 height=21 back=\"L2UI_ch3.Btn1_normalOn\" fore=\"L2UI_ch3.Btn1_normal\"></center>");
            }
        }

        final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
        html.setFile(player.isLang() +"/mods/Skillseller/skillSellerMain.htm"); // Este é o caminho para seu arquivo .htm. Adapte conforme necessário.
        html.replace("%skillList%", sb.toString());
        html.replace("%objectId%", String.valueOf(getObjectId()));
        player.sendPacket(html);
    }

    private SkillGroup findSkillGroupById(int id) {
        for (SkillGroup group : SKILL_GROUPS) {
            if (group.groupId == id) {
                return group;
            }
        }
        return null;
    }

}
