package net.sf.l2j.gameserver.model.holder;

import java.util.ArrayList;
import java.util.List;

import net.sf.l2j.gameserver.model.buffer.BuffCategory;

/**
 * A container extending {@link IntIntHolder} used for schemes buffer.
 */
public final class BuffSkillHolder extends IntIntHolder
{
	private final int _price;
	private final int _priceitemId;
	private final BuffCategory _type;
	private final String _description;
	private final int _order;
	private List<Integer> _stackingBuffs;
	
	public BuffSkillHolder(int id, int level, int priceItemId, int priceCount, BuffCategory buffCategory, String description, int order)
	{
		super(id, level);
		
		_priceitemId = priceItemId;
		_price = priceCount;
		_type = buffCategory;
		_description = description;
		_order = order;
		_stackingBuffs = new ArrayList<>();
	}
	
	public void addStackingBuff(int buffId)
	{
		_stackingBuffs.add(buffId);
	}
	
	public boolean isStacking(int buffId)
	{
		return _stackingBuffs.contains(buffId);
	}
	
	public final BuffCategory getBuffCategory()
	{
		return _type;
	}
	
	public int getOrder()
	{
		return _order;
	}
	
	public final String getDescription()
	{
		return _description;
	}
	
	public final int getPriceItemCount()
	{
		return _price;
	}
	
	public final int getPriceItemId()
	{
		return _priceitemId;
	}
	
	public final int getLevel()
	{
		return this.getValue();
	}
}