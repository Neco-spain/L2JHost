package net.sf.l2j.gameserver.model.zone.type;

import net.sf.l2j.gameserver.enums.MessageType;
import net.sf.l2j.gameserver.enums.ZoneId;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.group.Party;
import net.sf.l2j.gameserver.model.zone.type.subtype.SpawnZoneType;
import net.sf.l2j.gameserver.network.serverpackets.ExShowScreenMessage;
import net.sf.l2j.gameserver.network.serverpackets.L2GameServerPacket;

import java.util.ArrayList;
import java.util.List;

public class FarmSoloZone extends SpawnZoneType {

    private boolean _checkClasses;
    private static List<String> _classes = new ArrayList<>();

    public FarmSoloZone(int id) {
        super(id);
        _checkClasses = false;
    }

    public void setParameter(String name, String value) {
        if (name.equals("checkClasses")) {
            _checkClasses = Boolean.parseBoolean(value);
        } else if (name.equals("Classes")) {

            String[] propertySplit = value.split(",");
            for (String i : propertySplit) {
                _classes.add(i);
            }
        } else {
            super.setParameter(name, value);
        }
    }

    @Override
    protected void onEnter(Creature character) {
        if (character instanceof Player) {
            Player player = (Player) character;

            if (player.isInParty()) {
                Party party = player.getParty();
                if (party != null) {
                    party.removePartyMember(player, MessageType.EXPELLED);
                } else {
                    System.err.println("Party is null for player: " + player.getName());
                }
            } else {
                System.err.println("Player is not in a party: " + player.getName());
            }

            if (_checkClasses && _classes != null && _classes.contains("" + player.getClassId().getId())) {
                player.teleportTo(83597, 147888, -3405, 0);
                player.sendMessage("Your class is not allowed in this zone.");
                return;
            }

            character.setInsideZone(ZoneId.FARM_SOLO, true);
            player.broadcastUserInfo();
            player.sendPacket(new ExShowScreenMessage("You entered Farm Solo Zone!", 4000));
            player.sendPacket(new ExShowScreenMessage("You have entered Farm Solo Zone!", 4000));
            return;
        }
    }

    @Override
    protected void onExit(Creature character) {
        if (character instanceof Player) {

            if (character instanceof Player)
                character.setInsideZone(ZoneId.FARM_SOLO, false);
            character.sendPacket((L2GameServerPacket)new ExShowScreenMessage("You left Farm Solo Zone!", 4000));
            character.sendPacket((L2GameServerPacket)new ExShowScreenMessage("You came out Farm Solo Zone!", 4000));
            return;
        }
    }

    public void onDieInside(Player character) {}

    public void onReviveInside(Player character) {}
    }

