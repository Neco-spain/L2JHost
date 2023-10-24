package net.sf.l2j.gameserver.data.manager;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.sf.l2j.commons.data.StatSet;
import net.sf.l2j.commons.data.xml.IXmlReader;
import net.sf.l2j.commons.pool.ConnectionPool;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.buylist.NpcBuyList;
import net.sf.l2j.gameserver.model.buylist.Product;
import net.sf.l2j.gameserver.taskmanager.BuyListTaskManager;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;

public class CustomBuyListManager implements IXmlReader {
	
		private final Map<Integer, NpcBuyList> _buyLists = new HashMap<>();
		
		protected CustomBuyListManager()
		{
			load();
		}
		
		@Override
		public void load()
		{
			parseFile("./data/xml/customBuyLists.xml");
			LOGGER.info("Loaded {} buyListsCustom.", _buyLists.size());
			
			try (Connection con = ConnectionPool.getConnection();
				PreparedStatement ps = con.prepareStatement("SELECT * FROM `customBuyLists`");
				ResultSet rs = ps.executeQuery())
			{
				while (rs.next())
				{
					final NpcBuyList buyList = _buyLists.get(rs.getInt("buylist_id"));
					if (buyList == null)
						continue;
					
					final Product product = buyList.get(rs.getInt("item_id"));
					if (product == null)
						continue;
					
					BuyListTaskManager.getInstance().test(product, rs.getInt("count"), rs.getLong("next_restock_time"));
				}
			}
			catch (Exception e)
			{
				LOGGER.error("Failed to load buyList data from database.", e);
			}
		}
		
		@Override
		public void parseDocument(Document doc, Path path)
		{
		    // Convertendo a taxa de adena em porcentagem decimal.
		    // Por exemplo, 10x se torna 10.0 (ou 1000%).
		    double adenaPercentage = Config.RATE_DROP_CURRENCY;

		    forEach(doc, "list", listNode -> forEach(listNode, "buyList", buyListNode ->
		    {
		        final NamedNodeMap attrs = buyListNode.getAttributes();
		        final int buyListId = parseInteger(attrs, "id");
		        final NpcBuyList buyList = new NpcBuyList(buyListId);
		        buyList.setNpcId(parseInteger(attrs, "npcId"));

		        forEach(buyListNode, "product", productNode -> 
		        {
		            StatSet productAttributes = parseAttributes(productNode);
		            
		            // Adjusting product price based on adena percentage
		            int price = productAttributes.getInteger("price");
		            price += (int) (price * (adenaPercentage - 1));  // Subtracting 1 because original price is already 1x (or 100%).
		            productAttributes.set("price", price);

		            buyList.addProduct(new Product(buyListId, productAttributes));
		        });

		        _buyLists.put(buyListId, buyList);
		    }));
		}


		
		public void reload()
		{
			_buyLists.clear();
			load();
		}
		
		public NpcBuyList getBuyListCustom(int listId)
		{
			return _buyLists.get(listId);
		}
		
		public List<NpcBuyList> getBuyListsByNpcId(int npcId)
		{
			return _buyLists.values().stream().filter(b -> b.isNpcAllowed(npcId)).collect(Collectors.toList());
		}
		
		public static CustomBuyListManager getInstance()
		{
			return SingletonHolder.INSTANCE;
		}
		
		private static class SingletonHolder
		{
			protected static final CustomBuyListManager INSTANCE = new CustomBuyListManager();
		}
	}