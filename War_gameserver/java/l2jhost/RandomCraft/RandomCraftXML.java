package l2jhost.RandomCraft;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import l2jhost.data.XMLDocument;

public class RandomCraftXML extends XMLDocument
{
   private Map<Integer, RandomCraftItem> items;
   
   public RandomCraftXML()
   {
       items = new HashMap<>();
       load();
   }
   
   public static RandomCraftXML getInstance()
   {
       return SingletonHolder.INSTANCE;
   }
   
   private static class SingletonHolder
   {
       protected static final RandomCraftXML INSTANCE = new RandomCraftXML();
   }
   
   @Override
   protected void load()
   {
       loadDocument("./data/xml/RandomCraft.xml");
       LOG.info("RandomCraftItemData: Loaded " + items.size() + " items.");
   }
   
   @Override
   protected void parseDocument(Document doc, File file)
   {
       try
       {
           final Node root = doc.getFirstChild();
           
           for (Node node = root.getFirstChild(); node != null; node = node.getNextSibling())
           {
               if (!"item".equalsIgnoreCase(node.getNodeName()))
               {
                   continue;
               }
               
               NamedNodeMap attrs = node.getAttributes();
               int id = Integer.parseInt(attrs.getNamedItem("id").getNodeValue());
               int cantidad = Integer.parseInt(attrs.getNamedItem("count").getNodeValue());
               double probabilidad = Double.parseDouble(attrs.getNamedItem("chance").getNodeValue());
               boolean announce = Boolean.parseBoolean(attrs.getNamedItem("announce").getNodeValue());
               
               RandomCraftItem item = new RandomCraftItem(id, cantidad, probabilidad, announce);
               items.put(id, item);
           }
       }
       catch (Exception e)
       {
           // LOG.warning("RandomCraftItemData: Error while loading items: " + e);
           e.printStackTrace();
       }
   }
   
   public Map<Integer, RandomCraftItem> getItems()
   {
       return items;
   }
   
   public RandomCraftItem getItemById(int id)
   {
       return items.get(id);
   }
}