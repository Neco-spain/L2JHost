package net.sf.l2j.gameserver.data;

import net.sf.l2j.gameserver.data.xml.ItemData;
import net.sf.l2j.gameserver.model.item.kind.Item;

public class RecoverableItem
{
	private int objectId;
	private int itemId;
	private int enchantLevel;
	private String itemName;
	
	public RecoverableItem(int objectid, int itemId, int enchantLevel, String itemName)
	{
		this.objectId = objectid;
		this.itemId = itemId;
		this.enchantLevel = enchantLevel;
		this.itemName = itemName;
		
	}
	
	public int getobjectId()
	{
		return objectId;
	}
	
	public int getItemId()
	{
		return itemId;
	}
	
	public int getEnchantLevel()
	{
		return enchantLevel;
	}
	
	public String getItemName()
	{
		return itemName;
	}
	
	public String getIcon()
	{
		return getItem().getIcon();
	}
	
	public Item getItem()
	{
		return ItemData.getInstance().getTemplate(itemId);
	}
}
