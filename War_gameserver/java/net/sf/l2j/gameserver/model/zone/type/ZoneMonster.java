package net.sf.l2j.gameserver.model.zone.type;

import net.sf.l2j.gameserver.enums.ZoneId;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.instance.Monster;
import net.sf.l2j.gameserver.model.zone.type.subtype.SpawnZoneType;

public class ZoneMonster extends SpawnZoneType {
    
    public ZoneMonster(int id) {
        super(id);
    }

    @Override
    protected void onEnter(Creature character) {
        if (character instanceof Monster) {
            Monster monster = (Monster) character;
            
            // Se a localização de spawn do monstro não estiver definida, defina-a agora.
            if (monster.getSpawnLocation() == null) {
                monster.setSpawnLocation(monster.getSpawnLocation());
            }
        }
        character.setInsideZone(ZoneId.ZONE_MONSTER, true);
    }

    @Override
    protected void onExit(Creature character) {
        if (character instanceof Monster) {
            Monster monster = (Monster) character;
            
            // Se o monstro estiver saindo da zona, teleporta-o de volta para sua localização de spawn original
            monster.instantTeleportTo(monster.getSpawnLocation(), 0);
            
            monster.getAggroList().cleanAllHate();
            monster.abortAll(false);

        }
        character.setInsideZone(ZoneId.ZONE_MONSTER, false);
    }
}
