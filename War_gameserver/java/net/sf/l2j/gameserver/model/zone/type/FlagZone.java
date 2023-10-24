package net.sf.l2j.gameserver.model.zone.type;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.enums.ZoneId;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.zone.type.subtype.SpawnZoneType;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.ExShowScreenMessage;
import net.sf.l2j.gameserver.network.serverpackets.L2GameServerPacket;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.taskmanager.PvpFlagTaskManager;

public class FlagZone extends SpawnZoneType {
    private boolean _isflagZone;

    public FlagZone(int id) {
        super(id);
        _isflagZone = false;
    }

    public void setParameter(String name, String value) {
        if (name.equals("isflagZone")) {
            _isflagZone = Boolean.parseBoolean(value);
        } else {
            super.setParameter(name, value);
        }
    }

    @Override
    protected void onEnter(Creature character) {
        if (character instanceof Player) {
            Player player = (Player) character;

            if (this._isflagZone) {
                player.updatePvPStatus();
            }
            character.setInsideZone(ZoneId.FLAG, true);
            character.setInsideZone(ZoneId.NO_SUMMON_FRIEND, true);
            player.updatePvPFlag(1);
            player.sendPacket((L2GameServerPacket) new ExShowScreenMessage("You have entered a Flag Zone!", 4000));
            return;
        }

    }

    @Override
    protected void onExit(Creature character) {
        if (character instanceof Player) {

            Player player = (Player)character;
            character.setInsideZone(ZoneId.FLAG, false);
            character.setInsideZone(ZoneId.NO_SUMMON_FRIEND, false);
            PvpFlagTaskManager.getInstance().add(player, Config.PVP_NORMAL_TIME);
            character.sendPacket((L2GameServerPacket)new SystemMessage(SystemMessageId.LEFT_COMBAT_ZONE));
            character.sendPacket((L2GameServerPacket)new ExShowScreenMessage("You came out Flag Zone! =( time left flag Status 40sec", 4000));
            return;
        }
    }
}