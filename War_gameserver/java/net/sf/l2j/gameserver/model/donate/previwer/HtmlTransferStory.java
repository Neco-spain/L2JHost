package net.sf.l2j.gameserver.model.donate.previwer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.l2j.commons.data.Pagination;
import net.sf.l2j.commons.pool.ConnectionPool;

import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.donate.data.Storyt;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

public class HtmlTransferStory
{
	public static void HtmlInfoPagments(Player player, int page)
	{
		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		int row = 0;
		boolean isTableGeneratedA = false;
		html.setFile(player.isLang() + "mods/donate/transfer_story.htm");
		int playerObjectId = player.getObjectId();
		final StringBuilder sb = new StringBuilder();
		List<Storyt> temporaryList = new ArrayList<>();
		int entriesPerPage = 8;
		
		if (!isTableGeneratedA)
		{
			try (Connection con = ConnectionPool.getConnection();
				PreparedStatement ps = con.prepareStatement("SELECT * FROM site_donations WHERE status = 1 AND personagem = ?"))
			{
				ps.setInt(1, playerObjectId);
				
				ResultSet rs = ps.executeQuery();
				while (rs.next())
				{
					// Extract donation details
					int protocolo = rs.getInt("protocolo");
					int quantCoins = rs.getInt("quant_coins");
					int status = rs.getInt("status");
					long dataSeconds = rs.getLong("data");
					long dataMillis = dataSeconds * 1000;
					
					// Criar um objeto Storyt
					Storyt storyt = new Storyt(protocolo, quantCoins, status, dataMillis);
					
					// Adicionar o objeto à lista temporária
					temporaryList.add(storyt);
					row++;
					
				}
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
			
			isTableGeneratedA = true;
		}
		Pagination<Storyt> pagination = new Pagination<>(temporaryList.stream(), page, entriesPerPage);
		
		int startIndex = (page - 1) * entriesPerPage;
		int endIndex = Math.min(startIndex + entriesPerPage, temporaryList.size());
		
		for (int i = startIndex; i < endIndex; i++)
		{
			Storyt storyt = temporaryList.get(i);
			
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			Date dt = new Date(storyt.getDataMillis());
			
			// table story
			{
				sb.append("<img src=\"L2UI.SquareGray\" width=\"300\" height=\"1\"/>");
				
				sb.append((row % 2 == 0) ? "<table bgcolor=\"000000\"><tr>" : "<table><tr>");
				sb.append("<tr>");
				sb.append("<td width=60 align=center><font color=\"0099FF\">" + storyt.getProtocolo() + "</font></td>");
				
				String itemCount = formatItemCount(storyt.getQuantCoins());
				
				sb.append("<td width=90 align=\"center\">$ " + itemCount + "</td>");
				
				sb.append("<td width=100 align=\"center\">" + df.format(dt) + " </td>");
				
				if (storyt.getStatus() == 1)
					sb.append("<td width=50 align=center><a action=\"bypass link https://nubank.com.br/pagar/eek9h/ouy8Wtx339\"><font color=\"00FF00\">Pay</font></a></td>");
				
				sb.append("</tr>");
				sb.append("</tr></table>");
				sb.append("<img src=\"L2UI.SquareGray\" width=\"300\" height=\"1\"/>");
				
			}
		}
		
		// table next previer
		{
			sb.append("<table width=\"300\" height=\"15\" bgcolor=\"000000\"><tr>");
			if (page > 1)
				sb.append("<td align=left width=70><a action=\"bypass donate info_pagments " + String.valueOf(page - 1) + "\">Previous</a></td>");
			else
				sb.append("<td align=left width=70>Previous</td>");
			
			sb.append("<td align=center width=128> Page: " + page + "</td>");
			if (pagination.size() > page)
				sb.append("<td align=right width=70><a action=\"bypass donate info_pagments " + String.valueOf(page + 1) + "\">Next</a></td>");
			else
				sb.append("<td align=right width=70>Next</td>");
			
			sb.append("</tr></table>");
			sb.append("<img src=\"L2UI.SquareGray\" width=\"300\" height=\"1\"/>");
		}
		// end code generation
		html.replace("%list%", sb.toString());
		player.sendPacket(html);
	}
	
	public static void HtmlInfoPagmentsEnd(Player player, int page)
	{
		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		int row = 0;
		boolean isTableGeneratedA = false;
		html.setFile(player.isLang() + "mods/donate/transfer_story.htm");
		int playerObjectId = player.getObjectId();
		final StringBuilder sb = new StringBuilder();
		List<Storyt> temporaryList = new ArrayList<>();
		int entriesPerPage = 8;
		
		if (!isTableGeneratedA)
		{
			try (Connection con = ConnectionPool.getConnection();
				PreparedStatement ps = con.prepareStatement("SELECT * FROM site_donations WHERE status = 6 AND personagem = ?"))
			{
				ps.setInt(1, playerObjectId);
				
				ResultSet rs = ps.executeQuery();
				while (rs.next())
				{
					// Extract donation details
					int protocolo = rs.getInt("protocolo");
					int quantCoins = rs.getInt("quant_coins");
					int status = rs.getInt("status");
					long dataSeconds = rs.getLong("data");
					long dataMillis = dataSeconds * 1000;
					
					// Criar um objeto Storyt
					Storyt storyt = new Storyt(protocolo, quantCoins, status, dataMillis);
					
					// Adicionar o objeto à lista temporária
					temporaryList.add(storyt);
					row++;
					
				}
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
			
			isTableGeneratedA = true;
		}
		Pagination<Storyt> pagination = new Pagination<>(temporaryList.stream(), page, entriesPerPage);
		
		int startIndex = (page - 1) * entriesPerPage;
		int endIndex = Math.min(startIndex + entriesPerPage, temporaryList.size());
		
		for (int i = startIndex; i < endIndex; i++)
		{
			Storyt storyt = temporaryList.get(i);
			
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			Date dt = new Date(storyt.getDataMillis());
			
			// table story
			{
				sb.append("<img src=\"L2UI.SquareGray\" width=\"300\" height=\"1\"/>");
				
				sb.append((row % 2 == 0) ? "<table bgcolor=\"000000\"><tr>" : "<table><tr>");
				sb.append("<tr>");
				sb.append("<td width=60 align=center><font color=\"0099FF\">" + storyt.getProtocolo() + "</font></td>");
				
				String itemCount = formatItemCount(storyt.getQuantCoins());
				
				sb.append("<td width=90 align=\"center\">$ " + itemCount + "</td>");
				
				sb.append("<td width=100 align=\"center\">" + df.format(dt) + " </td>");
				
				if (storyt.getStatus() == 6)
					sb.append("<td width=50 align=center><a action=\"bypass link https://nubank.com.br/pagar/eek9h/ouy8Wtx339\"><font color=\"0099FF\">Info</font></a></td>");
				
				sb.append("</tr>");
				sb.append("</tr></table>");
				sb.append("<img src=\"L2UI.SquareGray\" width=\"300\" height=\"1\"/>");
				
			}
		}
		
		// table next previer
		{
			sb.append("<table width=\"300\" height=\"15\" bgcolor=\"000000\"><tr>");
			if (page > 1)
				sb.append("<td align=left width=70><a action=\"bypass donate story_end " + String.valueOf(page - 1) + "\">Previous</a></td>");
			else
				sb.append("<td align=left width=70>Previous</td>");
			
			sb.append("<td align=center width=128> Page: " + page + "</td>");
			if (pagination.size() > page)
				sb.append("<td align=right width=70><a action=\"bypass donate story_end " + String.valueOf(page + 1) + "\">Next</a></td>");
			else
				sb.append("<td align=right width=70>Next</td>");
			
			sb.append("</tr></table>");
			sb.append("<img src=\"L2UI.SquareGray\" width=\"300\" height=\"1\"/>");
		}
		// end code generation
		html.replace("%list%", sb.toString());
		player.sendPacket(html);
	}
	
	private static String formatItemCount(int count)
	{
		NumberFormat formatter = new DecimalFormat("#,###");
		String formattedCount = formatter.format(count);
		
		if (count >= 10000 && count < 100000)
		{
			formattedCount = "<font color=\"00FFFF\">" + formattedCount + "</font>"; // Azul claro
		}
		else if (count >= 100000 && count < 1000000)
		{
			formattedCount = "<font color=\"FF0000\">" + formattedCount + "</font>"; // Rosa
		}
		else if (count >= 1000000 && count < 10000000)
		{
			formattedCount = "<font color=\"00FF00\">" + formattedCount + "</font>"; // Amarelo
		}
		else if (count >= 10000000 && count < 100000000)
		{
			formattedCount = "<font color=\"0000FF\">" + formattedCount + "</font>"; // Verde
		}
		else if (count >= 100000000 && count < 1000000000)
		{
			formattedCount = "<font color=\"FF4500\">" + formattedCount + "</font>"; // Azul claro
		}
		else if (count >= 1000000000)
		{
			formattedCount = "<font color=\"FF8C00\">" + formattedCount + "</font>"; // Amarelo dourado
		}
		
		return formattedCount;
	}
}
