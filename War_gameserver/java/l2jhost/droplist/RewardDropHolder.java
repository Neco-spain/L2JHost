package l2jhost.droplist;

/**
 * @author BAN - L2JDEV
 */
public class RewardDropHolder
{
	private int _monsterId;
	private int _itemId;
	private int _count;
	private int _category;
	private int _chance;
	
	public RewardDropHolder(int monsterId, int itemId, int count, int category, int chance)
	{
		this._monsterId = monsterId;
		this._itemId = itemId;
		this._count = count;
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
	
	public int getCount()
	{
		return _count;
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
