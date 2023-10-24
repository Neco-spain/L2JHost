package net.sf.l2j.gameserver.model.donate.handler;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;

import net.sf.l2j.commons.lang.Tokenizer;
import net.sf.l2j.commons.logging.CLogger;
import net.sf.l2j.commons.pool.ConnectionPool;

import net.sf.l2j.gameserver.enums.SayType;
import net.sf.l2j.gameserver.idfactory.IdFactory;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.previwer.htm.GenerationDonateMain;
import net.sf.l2j.gameserver.network.serverpackets.CreatureSay;
import net.sf.l2j.gameserver.network.serverpackets.OpenUrlPacket;

public class HandlerPayProcess
{
	private static final CLogger LOGGER = new CLogger(HandlerPayProcess.class.getName());
	
	public static void saveCache(Player player, Tokenizer tokenizer)
	{
		String email = tokenizer.getToken(2);
		int pais = tokenizer.getAsInteger(3, 0);
		int ddd = tokenizer.getAsInteger(4, 0);
		String phone = tokenizer.getToken(5);
		int amount = tokenizer.getAsInteger(6, 0);
		String url = tokenizer.getToken(7);
		
		if (email.isEmpty() || phone.isEmpty() || amount == 0)
		{
			GenerationDonateMain.purchase(player);
			return;
		}
		
		handleCoins(player, email, pais, ddd, phone, amount, url);
		
		for (Player allgms : World.getInstance().getPlayers())
		{
			if(allgms.isGM())
			allgms.sendPacket(new CreatureSay(player.getObjectId(), SayType.SHOUT, "Donate Manager", player.getName() + " sent " + amount + " Donate Coin status pending."));
		}
	}
	
	private static final String INSERT_DONATE_STORE = "INSERT INTO donate_store (obj_Id, order_number, char_name, email, pais, ddd, phone, amount, date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	private static void handleCoins(Player player, String email, int pais, int ddd, String phone, int amount, String url)
	{
		int objectId = IdFactory.getInstance().getNextId();
		
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(INSERT_DONATE_STORE))
		{
			ps.setInt(1, player.getObjectId());
			ps.setInt(2, objectId);
			ps.setString(3, player.getName());
			ps.setString(4, email);
			ps.setInt(5, pais);
			ps.setInt(6, ddd);
			ps.setString(7, phone);
			ps.setInt(8, amount);
			
			java.sql.Timestamp timestamp = new java.sql.Timestamp(System.currentTimeMillis());
			ps.setTimestamp(9, timestamp);
			ps.execute();
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't insert donate purchase.", e);
		}
		handleSiteCoins(player, amount);
		player.sendPacket(new OpenUrlPacket(url));
		GenerationDonateMain.pay_confirmation(player);
		
	}
	
	private static final String INSERT_SITE_DONATE_STORE = "INSERT INTO site_donations (account, personagem, price, currency, metodo_pgto, quant_coins, coins_bonus, valor, status, data) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	private static void handleSiteCoins(Player player, int amount)
	{
	    try (Connection con = ConnectionPool.getConnection();
	        PreparedStatement ps = con.prepareStatement(INSERT_SITE_DONATE_STORE))
	    {
	        ps.setString(1, player.getAccountName());
	        ps.setInt(2, player.getObjectId());
	        ps.setBigDecimal(3, BigDecimal.valueOf(amount));
	        ps.setString(4, "USD");
	        ps.setString(5, "PayPal_USD");
	        ps.setInt(6, amount);
	        ps.setInt(7, 0);
	        ps.setBigDecimal(8, BigDecimal.valueOf(amount));
	        // Define o status como 1 (pendente)
	        ps.setInt(9, 1);
	        
	        long data = System.currentTimeMillis() / 1000;
	        ps.setLong(10, data);
	
	        
	        ps.execute();
	    }
	    catch (Exception e)
	    {
	        LOGGER.error("Couldn't insert donate purchase.", e);
	    }
	}	
}
