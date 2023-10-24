package net.sf.l2j.gameserver.data.xml;

public class DressMeData
{
	private int chestId, HairAllId, legsId, glovesId, feetId;
	
	public DressMeData()
	{
		chestId = 0;
		HairAllId = 0;
		legsId = 0;
		glovesId = 0;
		feetId = 0;
	}
	
	public int getChestId()
	{
		return chestId;
	}
	
	public int getHairAllId()
	{
		return HairAllId;
	}
	
	public int getLegsId()
	{
		return legsId;
	}
	
	public int getGlovesId()
	{
		return glovesId;
	}
	
	public int getBootsId()
	{
		return feetId;
	}
	
	public void setChestId(int val)
	{
		chestId = val;
	}
	
	public void setHairAllId(int val)
	{
		HairAllId = val;
	}

	public void setLegsId(int val)
	{
		legsId = val;
	}
	
	public void setGlovesId(int val)
	{
		glovesId = val;
	}
	
	public void setBootsId(int val)
	{
		feetId = val;
	}
}