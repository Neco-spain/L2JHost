package l2jhost.droplist;

/**
 * @author BAN - L2JDEV
 */
public class EditItemSpoilHolder
{
	private int _monsterId;
	private int _itemId;
	private int _min;
	private int _max;
	private int _category;
	private int _chance;
	
	public EditItemSpoilHolder(int monsterId, int itemId, int min, int max, int category, int chance)
	{
		this._monsterId = monsterId;
		this._itemId = itemId;
		this._min = min;
		this._max = max;
		this._category = category;
		this._chance = chance;
	}
	
	public int getMonsterId()
	{
		return _monsterId;
	}
	
	public int getItemId()
	{
		return _itemId;
	}
	
	public int getMinCount()
	{
		return _min;
	}
	
	public int getMaxCount()
	{
		return _max;
	}
	
	public int getCategory()
	{
		return _category;
	}
	
	public int getChanceDrop()
	{
		return _chance;
	}
}
