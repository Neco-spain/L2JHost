package net.sf.l2j.gameserver.model.donate.data;

public class Storyt
{
	private int protocolo;
	private int quantCoins;
	private int status;
	private long dataMillis;
	
	public Storyt(int protocolo, int quantCoins, int status, long dataMillis)
	{
		this.protocolo = protocolo;
		this.quantCoins = quantCoins;
		this.status = status;
		this.dataMillis = dataMillis;
	}
	
	public int getProtocolo()
	{
		return protocolo;
	}
	
	public int getQuantCoins()
	{
		return quantCoins;
	}
	
	public int getStatus()
	{
		return status;
	}
	
	public long getDataMillis()
	{
		return dataMillis;
	}
}
