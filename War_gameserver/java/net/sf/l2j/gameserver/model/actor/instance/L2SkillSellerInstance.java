package net.sf.l2j.gameserver.model.actor.instance;


import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.skills.L2Skill;

public class L2SkillSellerInstance extends Npc
{

    public final static int NPC_ID = 24246;
    public final static int ITEM_ID = 2807;
    public final static int ITEM_COUNT = 5;
    private final int[] SKILL_IDS =
            {
                    3134, 3132, 3124, 3125, 3133, 3135, 3136, 3137, 3138, 3139, 3140, 3141, 3134
            };

    public L2SkillSellerInstance(int objectId, NpcTemplate template)
    {
        super(objectId, template);
    }

    @Override
    public void showChatWindow(Player j, int val)
    {
        if (j == null)
            return;

        StringBuilder t = new StringBuilder();
        NpcHtmlMessage n = new NpcHtmlMessage(getObjectId());
        sendHtml(t, n, j);
    }

    private void sendHtml(StringBuilder t, NpcHtmlMessage n, Player j)
    {
        t.append("<html><head><title>");
        t.append("L2Skill Seller");
        t.append("</title</head>");
        t.append("<body><center>");
        t.append("<br>Hello , do you want some special skills?");
        t.append("<br>Choose whatever you want but don't forget");
        t.append("<br>you need 5 Gold Bars for each one");
        for (int i : SKILL_IDS)
        {
            L2Skill s = SkillTable.getInstance().getInfo(i, 10);
            String name = "";
            if (s != null)
                name = s.getName();
            if (name != "")
                t.append("<center><button value=\"" + name + " LvL:10\" action=\"bypass -h skill" + i + "\" width=204 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"><br>");
        }
        t.append("</center></body></html>");
        n.setHtml(t.toString());
        j.sendPacket(n);
    }
}
