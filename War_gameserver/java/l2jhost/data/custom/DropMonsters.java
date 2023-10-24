package l2jhost.data.custom;

import net.sf.l2j.commons.data.StatSet;
import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.model.holder.IntIntHolder;

/**
 * @author Williams
 */
public class DropMonsters
{
	private int _itemId;
	private int _minDrop;
	private int _maxDrop;
	private int _chance;
	
	private final IntIntHolder _enchant;
	private final int _enchantChance;
	
	public DropMonsters(StatSet set)
{
		_itemId = set.getInteger("itemId");
		_minDrop = set.getInteger("minDrop");
		_maxDrop = set.getInteger("maxDrop");
		_chance = set.getInteger("chance");
		
		_enchant = set.getString("enchant", "").isEmpty() ? null : set.getIntIntHolder("enchant");
		_enchantChance = set.getInteger("enchantChance", 0);
	}
	
	public int getMinDrop()
	{
		return _minDrop;
	}
	
	public int getMaxDrop()
	{
		return _maxDrop;
	}
	
	public int getChance()
	{
		return _chance;
	}
	
	public int getItemId()
	{
		return _itemId;
	}
	
	public int getEnchant()
	{
		return Rnd.get(Math.min(_enchant.getId(), _enchant.getValue()), Math.max(_enchant.getId(), _enchant.getValue()));
	}
	
	public int getEnchantChance()
	{
		return _enchantChance;
	}
	
	public boolean hasEnchant()
	{
		return _enchant != null;
	}
}