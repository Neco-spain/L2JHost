package net.sf.l2j.gameserver.model.actor.instance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.sf.l2j.commons.pool.ConnectionPool;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.ExShowScreenMessage;
import net.sf.l2j.gameserver.network.serverpackets.ItemList;
import net.sf.l2j.gameserver.network.serverpackets.MoveToPawn;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

import l2jhost.RandomCraft.RandomCraftItem;
import l2jhost.RandomCraft.RandomCraftXML;

/**
 * @author Terius
 */

public class RandomCraft extends Npc
{
   private boolean hasGeneratedItems;
   List<RandomCraftItem> items = new LinkedList<>();
   
   public RandomCraft(int objectId, NpcTemplate template)
   {
       super(objectId, template);
       setHasGeneratedItems(false);
   }
   
   
   @Override
    public void onInteract(Player player)
      {
           // Rotate the player to face the instance
           player.sendPacket(new MoveToPawn(player, this, Npc.INTERACTION_DISTANCE));
           
           if (hasRandomAnimation())
           {
               onRandomAnimation(1);
           }
           
           loadGeneratedItems(player);
           
           showHtmlWindow(player);
           
           // Enviar ActionFailed al jugador para evitar quedarse atascado
           player.sendPacket(ActionFailed.STATIC_PACKET);
      }
   
   

   
   @Override
   public void onBypassFeedback(Player player, String command)
   {
       if (command.startsWith("refresh"))
       {
           // Verifique si el jugador tiene el artículo requerido (ID 57) y la cantidad (50000)
           ItemInstance item57 = player.getInventory().getItemByItemId(Config.RANDOM_CRAFT_ITEM_ID_CONSUME);
           if (item57 != null && item57.getCount() >= Config.RANDOM_CRAFT_ITEM_CONSUME_REFRESH)
           {
               // Cargue al jugador el artículo requerido (ID 57) y la cantidad (50000)
               player.getInventory().destroyItemByItemId("Random Craft", Config.RANDOM_CRAFT_ITEM_ID_CONSUME, Config.RANDOM_CRAFT_ITEM_CONSUME_REFRESH, player, this);
               
               generateItems(player);
               
               player.sendPacket(new ItemList(player, true));
               
               showHtmlWindow(player);
               
               // Almacene los elementos generados en la base de datos para el jugador
               saveGeneratedItems(player);
           }
           else
           {
               player.sendMessage("Necesitas al menos 50000 Adena para actualizar Random Craft.");
           }
       }
       else if (command.startsWith("create"))
       {
           // Eliminar los elementos generados para el jugador de la tabla RandomCraftItem
           deleteGeneratedItems(player);
           
           createItem(player);
           
           // showHtmlWindow(player);
       }
       
       else if (command.startsWith("back"))
       {
           
           showHtmlWindow(player);
       }
       
   }
   
   private void generateItems(Player player)
   {
       List<RandomCraftItem> items = new LinkedList<>();
       RandomCraftXML randomCraftXML = RandomCraftXML.getInstance();
       Map<Integer, RandomCraftItem> craftItems = randomCraftXML.getItems();
       
       // Genera 4 elementos únicos para cada jugador en función de la probabilidad
       List<Integer> selectedItems = new ArrayList<>();
       while (selectedItems.size() < 4)
       {
           int itemId = getRandomItem(craftItems);
           if (!selectedItems.contains(itemId))
           {
               selectedItems.add(itemId);
               items.add(craftItems.get(itemId));
           }
       }
       
       // Asignar los elementos generados al jugador
       player.setGeneratedCraftItems(items);
       setHasGeneratedItems(true);
       
   }
   
   private static void deleteGeneratedItems(Player player)
   {
       try (Connection con = ConnectionPool.getConnection();
           PreparedStatement stmt = con.prepareStatement("DELETE FROM RandomCraftItem WHERE object_id = ?"))
       {
           stmt.setInt(1, player.getObjectId());
           stmt.execute();
       }
       catch (SQLException e)
       {
           e.printStackTrace();
       }
   }
   
   private static void saveGeneratedItems(Player player)
   {
       try (Connection con = ConnectionPool.getConnection();
           PreparedStatement stmt = con.prepareStatement("DELETE FROM RandomCraftItem WHERE object_id = ?"))
       {
           stmt.setInt(1, player.getObjectId());
           stmt.execute();
           
           try (PreparedStatement insertStmt = con.prepareStatement("INSERT INTO RandomCraftItem (object_id, item_id, amount, chance, announce) VALUES (?, ?, ?, ?, ?)"))
           {
               insertStmt.setInt(1, player.getObjectId());
               List<RandomCraftItem> items = player.getGeneratedCraftItems();
               if (items != null)
               {
                   for (RandomCraftItem item : items)
                   {
                       insertStmt.setInt(2, item.getId());
                       insertStmt.setInt(3, item.getCantidad());
                       insertStmt.setDouble(4, item.getProbabilidad());
                       insertStmt.setBoolean(5, item.getAnnounce());
                       insertStmt.addBatch(); // Agregar la consulta al lote (batch)
                   }
                   insertStmt.executeBatch(); // Ejecutar el lote de consultas
               }
           }
       }
       catch (SQLException e)
       {
           e.printStackTrace();
       }
   }
   
   private void loadGeneratedItems(Player player)
   {
       try (Connection con = ConnectionPool.getConnection();
           PreparedStatement stmt = con.prepareStatement("SELECT item_id, amount, chance, announce FROM RandomCraftItem WHERE object_id = ?"))
       {
           stmt.setInt(1, player.getObjectId());
           
           try (ResultSet rset = stmt.executeQuery())
           {
               List<RandomCraftItem> items = new LinkedList<>();
               while (rset.next())
               {
                   int itemId = rset.getInt("item_id");
                   int amount = rset.getInt("amount");
                   int chance = rset.getInt("chance");
                   boolean announce = rset.getBoolean("announce");
                   RandomCraftItem item = new RandomCraftItem(itemId, amount, chance, announce);
                   items.add(item);
               }
               
               player.setGeneratedCraftItems(items);
               setHasGeneratedItems(!items.isEmpty());
           }
       }
       catch (SQLException e)
       {
           e.printStackTrace();
       }
   }
   
   private static int getRandomItem(Map<Integer, RandomCraftItem> craftItems)
   {
       // Calcular la suma de probabilidad total
       double totalProbability = 0;
       for (RandomCraftItem item : craftItems.values())
       {
           totalProbability += item.getProbabilidad();
       }
       
       // Generar un valor aleatorio entre 0 y la probabilidad total
       Random random = new Random();
       double randomValue = random.nextDouble() * totalProbability;
       
       // Seleccione el elemento en funciÃ³n de la probabilidad
       double cumulativeProbability = 0;
       for (RandomCraftItem item : craftItems.values())
       {
           cumulativeProbability += item.getProbabilidad();
           if (randomValue <= cumulativeProbability)
           {
               return item.getId();
           }
       }
       
       // Si no se selecciona ningÃºn artÃ­culo, devolver un artÃ­culo al azar
       List<Integer> itemIds = new ArrayList<>(craftItems.keySet());
       int index = random.nextInt(itemIds.size());
       return itemIds.get(index);
   }
   
   private void createItem(Player player)
   {
       // Comprueba si la lista de elementos está vacía
       List<RandomCraftItem> items = player.getGeneratedCraftItems();
       if (items == null || items.isEmpty())
       {
           player.sendMessage("Necesita actualizar para poder crear un elemento aleatorio.");
           return;
       }
       
       // Obtén un elemento aleatorio de la lista de elementos generados
       Random random = new Random();
       int index = random.nextInt(items.size());
       RandomCraftItem craftItem = items.get(index);
       
       // Carga al jugador el artículo con ID 57 y cantidad 300000
       ItemInstance item57 = player.getInventory().getItemByItemId(Config.RANDOM_CRAFT_ITEM_ID_CONSUME);
       if (item57 != null && item57.getCount() >= Config.RANDOM_CRAFT_ITEM_CONSUME_CREATE)
       {
           player.getInventory().destroyItemByItemId("Random Craft", Config.RANDOM_CRAFT_ITEM_ID_CONSUME, Config.RANDOM_CRAFT_ITEM_CONSUME_CREATE, player, this);
           
           // Da el artículo al jugador
           ItemInstance itemInstance = player.getInventory().addItem("Random Craft", craftItem.getId(), craftItem.getCantidad(), player, this);
           if (itemInstance != null)
           {
               player.sendPacket(new ItemList(player, true));
               
               // Envía un mensaje al jugador con el nombre del artículo y la cantidad.
               String message = "¡Felicidades! Has recibido " + craftItem.getItem().getName() + " (Cantidad: " + craftItem.getCantidad() + ")";
               player.sendMessage(message);
               
               // Obtén el nombre del jugador que creó el elemento
               String creatorName = player.getName();
               
               // Comprueba si el artículo tiene announce en true en el archivo XML
               if (craftItem.getAnnounce())
               {
                   
                   for (Player players : World.getInstance().getPlayers())
                   {
                       
                       String text = creatorName + " Ha Crafteado: " + craftItem.getItem().getName() + " En el RandomCraft System";
                       
                       players.sendPacket(new ExShowScreenMessage(text, 12000));
                       
                   }
                   
               }
               
               // Borra la lista de elementos para el jugador
               player.clearGeneratedCraftItems();
               setHasGeneratedItems(false);
               
               // Muestra la ventana de felicitaciones con el item ganador
               showCongratulationsWindow(player, craftItem);
               
               return;
           }
       }
       
       player.sendMessage("Necesitas al menos 300000 Adena para crear un elemento aleatorio.");
   }
   
   private void showHtmlWindow(Player player)
   {
       StringBuilder html = new StringBuilder();
       html.append("<html><body>");
       html.append("<center>Bienvenido a Random Craft System!</center>");
       html.append("<br>");
       html.append("<center>Podras Crear 1 item entre los 4 que salgan random!</center>");
       html.append("<br>");
       html.append("<center>Les Deseo Mucha Suerte</center>");
       html.append("<br>");
       html.append("<br>");
       
       List<RandomCraftItem> items = player.getGeneratedCraftItems();
       if (items == null || items.isEmpty())
       {
           html.append("<center>La Lista Esta Vacia Dale a Refresh</center>");
       }
       else
       {
           // Generar los iconos de los artículos en forma horizontal
           for (RandomCraftItem item : items)
           {
               html.append("<img src=\"L2UI.SquareGray\" width=295 height=1>");
               html.append("<div align=center>");
               html.append("<table>");
               html.append("<tr>");
               html.append("<td>");
               html.append("<img src=").append(item.getIcon()).append(" width=32 height=32>");
               html.append("</td>");
               html.append("<td width=260>");
               html.append("<font color=LEVEL>").append(item.getItem().getName()).append("</font>");
               
               html.append("</td>");
               html.append("</tr>");
               html.append("</table>");
               html.append("</div>");
               html.append("<img src=\"L2UI.SquareGray\" width=295 height=1>");
               html.append("<br>");
           }
       }
       
       html.append("<br>");
       html.append("<br>");
       html.append("<center>");
       html.append("<table>");
       html.append("<tr>");
       html.append("<td width=75 height=21>");
       html.append("<button value=\"Refresh\" action=\"bypass -h npc_").append(getObjectId()).append("_refresh\" width=75 height=21 back=\"L2UI.DefaultButton_click\" fore=\"L2UI.DefaultButton\">");
       html.append("</td>");
       html.append("<td width=75 height=21>");
       html.append("<button value=\"Create\" action=\"bypass -h npc_").append(getObjectId()).append("_create\" width=75 height=21 back=\"L2UI.DefaultButton_click\" fore=\"L2UI.DefaultButton\">");
       html.append("</td>");
       html.append("</tr>");
       html.append("</table>");
       html.append("</center>");
       
       html.append("</body></html>");
       
       showHtmlWindow(player, html.toString());
   }
   
   private void showHtmlWindow(Player player, String htmlContent)
   {
       NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
       html.setHtml(htmlContent);
       
       player.sendPacket(html);
   }
   
   private void showCongratulationsWindow(Player player, RandomCraftItem craftItem)
   {
       StringBuilder html = new StringBuilder();
       
       html.append("<html><body>");
       html.append("<center>Felicidades, has ganado un item</center>");
       html.append("<br>");
       html.append("<center>¡Has recibido</center>");
       html.append("<br>");
       html.append("<br>");
       html.append("<img src=\"L2UI.SquareGray\" width=295 height=1>");
       html.append("<center>");
       html.append("<table>");
       html.append("<tr>");
       html.append("<td>");
       html.append("<img src=").append(craftItem.getIcon()).append(" width=32 height=32>");
       html.append("</td>");
       html.append("<td width=260>");
       html.append("<font color=LEVEL>").append(craftItem.getItem().getName()).append("</font>");
       html.append("</td>");
       html.append("</tr>");
       html.append("</table>");
       html.append("</center>");
       html.append("<img src=\"L2UI.SquareGray\" width=295 height=1>");
       html.append("<br>");
       html.append("<br>");
       html.append("<br>");
       html.append("<br>");
       html.append("<center>");
       html.append("<button value=\"Back\" action=\"bypass -h npc_").append(getObjectId()).append("_back\" width=75 height=21 back=\"L2UI.DefaultButton_click\" fore=\"L2UI.DefaultButton\">");
       html.append("</center>");
       html.append("</body></html>");
       
       showHtmlWindow(player, html.toString());
   }
   
   /**
    * @return the hasGeneratedItems
    */
   public boolean isHasGeneratedItems()
   {
       return hasGeneratedItems;
   }
   
   /**
    * @param hasGeneratedItems the hasGeneratedItems to set
    */
   public void setHasGeneratedItems(boolean hasGeneratedItems)
   {
       this.hasGeneratedItems = hasGeneratedItems;
   }
}