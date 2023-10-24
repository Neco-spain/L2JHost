package net.sf.l2j.gameserver.model.actor.instance;

import net.sf.l2j.gameserver.enums.actors.ClassId;
import net.sf.l2j.gameserver.enums.actors.ClassRace;
import net.sf.l2j.gameserver.enums.actors.ClassType;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;

public final class AllClass extends VillageMaster
{
    public AllClass(int objectId, NpcTemplate template)
    {
        super(objectId, template);
    }

    @Override
    protected final boolean checkVillageMasterRace(ClassId pclass)
    {
        if (pclass == null)
            return false;

        return pclass.getRace() == ClassRace.HUMAN || pclass.getRace() == ClassRace.ELF || pclass.getRace() == ClassRace.ORC
                || pclass.getRace() == ClassRace.DARK_ELF || pclass.getRace() == ClassRace.DWARF;
    }

    @Override
    protected final boolean checkVillageMasterTeachType(ClassId pclass)
    {
        if (pclass == null)
            return false;

        return pclass.getType() == ClassType.FIGHTER;

    }
}