package net.sf.l2j.gameserver.model;

public class TraderSkillLearn
{
    // these two build the primary key
    private final int _id;
    private final int _level;

    // not needed, just for easier debug
    private final String _name;

    private final int _spCost;
    private final int _minLevel;
    private final int _costid;
    private final int _costcount;


    public TraderSkillLearn(int id, int level, String name, int spCost, int minLevel, int costid, int costcount) {
        _id = id;
        _level = level;
        _name = name.intern();
        _spCost = spCost;
        _minLevel = minLevel;
        _costid = costid;
        _costcount = costcount;
    }

    /**
     * @return Returns the id.
     */
    public int getId()
    {
        return _id;
    }

    /**
     * @return Returns the level.
     */
    public int getLevel()
    {
        return _level;
    }

    /**
     * @return Returns the minLevel.
     */
    public int getMinLevel()
    {
        return _minLevel;
    }

    /**
     * @return Returns the name.
     */
    public String getName()
    {
        return _name;
    }

    /**
     * @return Returns the spCost.
     */
    public int getSpCost()
    {
        return _spCost;
    }
    public int getIdCost()
    {
        return _costid;
    }
    public int getCostCount()
    {
        return _costcount;
    }
}



