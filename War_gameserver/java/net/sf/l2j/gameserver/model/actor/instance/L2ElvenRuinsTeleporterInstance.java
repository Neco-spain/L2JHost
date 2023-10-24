package net.sf.l2j.gameserver.model.actor.instance;

import net.sf.l2j.commons.math.MathUtil;
import net.sf.l2j.commons.pool.ThreadPool;
import net.sf.l2j.commons.random.Rnd;
import net.sf.l2j.gameserver.data.cache.HtmCache;
import net.sf.l2j.gameserver.data.xml.NpcData;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.model.group.Party;
import net.sf.l2j.gameserver.model.spawn.Spawn;
import net.sf.l2j.gameserver.network.serverpackets.ExShowScreenMessage;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class L2ElvenRuinsTeleporterInstance extends Npc {
    private boolean _mayEnter = true;
    private long _enterTime;

    private int [] [] spawnList = {
            {47382, 248864, -6361}, {47648, 248820, -6361},
            {46485, 247892, -6361},	{46087, 247876, -6361},
            {47524, 247185, -6617}, {47476, 246771, -6617},
            {47808, 246603, -6617}, {47957, 247063, -6617},
            {45407, 249046, -6361}, {45130, 249287, -6361},
            {45722, 249420, -6361}, {45142, 249163, -6361},
            {45044, 248507, -6411}, {43664, 248686, -6491},
            {42855, 247334, -6462}, {43018, 245186, -6462},
            {42693, 247676, -6462}, {41634, 246449, -6462},
            {43200, 245624, -6462}, {44058, 246578, -6512},
            {45603, 245721, -6612}, {45533, 246816, -6612},
            {47003, 246591, -6662}, {48000, 246045, -6662},
            {47039, 245266, -6662}, {44657, 245547, -6415},
            {44737, 247436, -6436}, {42648, 245383, -6462},
            {41644, 246296, -6462}, {42780, 247283, -6462},
            {44177, 245545, -6462}, {43717, 244419, -6498},
            {45244, 243127, -6463}, {45754, 244325, -6517},
            {47086, 243145, -6563}, {47999, 244305, -6563},
    };
    private int [] monsterId =
            {
                    20099, 20022, 20020, 20017, 20015, 20039,
                    20001, 20008, 20012, 20031, 20034, 20037,
            };
    private List<Player> _raiders = new ArrayList<>();
    private Set<Npc> _monsters = new HashSet<>();

    public L2ElvenRuinsTeleporterInstance(int objectId, NpcTemplate template)
    {

        super(objectId, template);
    }

 public void onBypassFeedback(Player player, String command) {
     if (player == null)
         return;
     if (command.startsWith("enterInside")) {
         teleportInside(player);
     } else if (command.startsWith("giranTele")) {
         player.teleportTo(83551, 147945, -3400, 0);
     } else if (command.startsWith("mainWindow")) {
         showChatWindow(player);
     }
 }
        /**
         * Check if party may enter.
         * @param player
         * @return
         */
        private boolean checkConditions (Player player)
        {
            Party party = player.getParty();
            if (party == null)
            {
                player.sendPacket(new ExShowScreenMessage("You need party with at least 5 members to enter!", 5000));
                return false;
            }
            if (party.getLeader() != player)
            {
                player.sendPacket(new ExShowScreenMessage("You are not party leader!", 5000));
                return false;
            }
            if (party.getMembersCount() < 2) //TODO change to 5
            {
                player.sendPacket(new ExShowScreenMessage("You need party with at least 5 members to enter!", 5000));
                return false;
            }
            for (Player partyMember : party.getMembers())
            {
                if (partyMember.getLevel() == 80 && !partyMember.isInParty())
                {
                    ExShowScreenMessage msg = new ExShowScreenMessage("Each party member should be level 76 at least!", 5000);
                    party.broadcastToPartyMembers(player, msg);
                    return false;
                }
                if (!MathUtil.checkIfInRange(500, player, partyMember, true))
                {
                    ExShowScreenMessage msg = new ExShowScreenMessage("Each party member should be close to the leader!", 5000);
                    party.broadcastToPartyMembers(player, msg);
                    return false;
                }
            }
            return true;
        }
    @Override
    public void showChatWindow(Player player) {
        var tb = new StringBuilder(); // Assuming TextBuilder is similar to StringBuilder

        tb.append("<html><title>Elven Ruins Manager:</title><body><center><br>");
        tb.append("Glad to see you <font color=\"LEVEL\">" + player.getName() + "</font>!<br>");

        if (mayEnter())
        {
            tb.append("This Location is: <font color=\"LEVEL\">Empty</font>!<br>");
            tb.append("Feel free to enter with your party!<br>");
            tb.append("<button value=\"Enter with Party\" action=\"bypass -h npc_%objectId%_enterInside\" width=160 height=32 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\">");
        }
        else
        {
            tb.append("This Location is: <font color=\"LEVEL\">Not Empty</font>!<br>");
            tb.append("You will have to wait!<br>");
            tb.append("About: <font color=\"LEVEL\">" + getTimeLeft() + " minutes left!<br>");
        }
        tb.append("<button value=\"How does it work\" action=\"bypass -h npc_%objectId%_tutorial\" width=160 height=32 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\">");
        tb.append("<button value=\"Giran Teleport\" action=\"bypass -h npc_%objectId%_giranTele\" width=160 height=32 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\">");
        tb.append("<br><br><br>ElvenRuins");

        NpcHtmlMessage msg = new NpcHtmlMessage(this.getObjectId());
        msg.setHtml(tb.toString());
        msg.replace("%objectId%", String.valueOf(this.getObjectId()));

        player.sendPacket(msg);
    }

    /**
     * @param player
     * @param htm
     */
    public void showHtml(Player player, String htm)
    {
        String html = null;
        html =(HtmCache.getInstance().getHtmForce(player.isLang() + "mods/RuinsMod/" + htm));

        NpcHtmlMessage msg = new NpcHtmlMessage(getObjectId());
        msg.setHtml(html);
        msg.replace("%objectId%", String.valueOf(getObjectId()));
        player.sendPacket(msg);
    }

    /**
     * Player need party with at least 5 party members.
     * If they meets the requirements, they may enter.
     * For next 10 min, there won't be any possibility to enter there again
     * for example by another parties.
     * @param player
     */
    private void teleportInside(Player player)
    {
        if (checkConditions(player))
        {
            Party party = player.getParty();

            for (Player member: party.getMembers())
            {
                member.teleportTo(49315, 248452, -5960, 0);
                _raiders.add(member);
                setEnterTime(System.currentTimeMillis());
                spawnMonsters();
                setMayEnter(false);
                ThreadPool.scheduleAtFixedRate(new ScheduleEnterTask(), 300000, 300000); //TODO 600000
            }
        }
    }
    private void clearRuins()
    {
        if (!_raiders.isEmpty())
        {
            for (Player raider: _raiders)
            {
                raider.teleportTo(-112367, 234703, -3688, 0); //TODO is this correct?
                raider.sendMessage("Time is over, you will have to leave now!");
            }

            _raiders.clear();
            _monsters.clear();
        }
    }
    private void spawnMonsters()
    {
        Npc monster = null;

        for (int[] spawn: spawnList)
        {
            monster = addSpawn(monsterId[Rnd.get(monsterId.length)], spawn[0], spawn[1], spawn[2]);
            _monsters.add(monster);
        }
    }

    private static Npc addSpawn(int npcId, int x, int y, int z)
    {
        try {
            NpcTemplate template = NpcData.getInstance().getTemplate(npcId);
            if (template != null) {
                Spawn spawn = new Spawn(template);
                spawn.setLoc(x, y, z, 0);  // Definindo a localização com o heading padrão de 0
                spawn.setRespawnDelay(0);
                return spawn.doSpawn(false);
            }
        } catch (Exception e) {
            LOGGER.error("Erro ao criar spawn", e);
        }
        return null;  // Retorna null se o template do NPC não for encontrado ou se houver um erro
    }




    private class ScheduleEnterTask implements Runnable
    {
        public ScheduleEnterTask()
        {
            // Nothing
        }

        @Override
        public void run()
        {
            clearRuins();
            setMayEnter(true);
        }
    }

    public int getTimeLeft()
    {
        long ms = System.currentTimeMillis() - _enterTime;
        long seconds = ms / 1000;
        int minutes = (int) (seconds / 60);

        return minutes;
    }

    public boolean mayEnter()
    {
        return _mayEnter;
    }

    public void setMayEnter(boolean b)
    {
        _mayEnter = b;
    }

    public void setEnterTime(long l)
    {
        _enterTime = l;
    }
}


