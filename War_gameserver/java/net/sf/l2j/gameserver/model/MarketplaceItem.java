package net.sf.l2j.gameserver.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.concurrent.ScheduledFuture;

import net.sf.l2j.commons.logging.CLogger;
import net.sf.l2j.commons.pool.ConnectionPool;
import net.sf.l2j.commons.pool.ThreadPool;

import net.sf.l2j.gameserver.data.sql.MarketplaceTable;
import net.sf.l2j.gameserver.idfactory.IdFactory;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.InventoryUpdate;
import net.sf.l2j.gameserver.network.serverpackets.L2GameServerPacket;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

public class MarketplaceItem {
  public static final CLogger LOGGER = new CLogger(MarketplaceItem.class.getName());
  
  private int _auctionId;
  
  private int _ownerId;
  
  private String _ownerName;
  
  private int _itemId;
  
  private int _count;
  
  private int _enchant;
  
  private int _augId;
  
  private int _augSKillId;
  
  private int _augSkillLevel;
  
  private int _costId;
  
  private int _costCount;
  
  private ScheduledFuture<?> _feeTask;
  
  private long _endTime;
  
  public MarketplaceItem(int auctionId, int ownerId, String ownerName, int itemId, int count, int enchant, int augId, int augSKillId, int augSkillLevel, int costId, int costCount, long endTime) {
    this._auctionId = auctionId;
    this._ownerId = ownerId;
    this._ownerName = ownerName;
    this._itemId = itemId;
    this._count = count;
    this._enchant = enchant;
    this._augId = augId;
    this._augSKillId = augSKillId;
    this._augSkillLevel = augSkillLevel;
    this._costId = costId;
    this._costCount = costCount;
    this._endTime = endTime;
    long currentTime = System.currentTimeMillis();
    if (this._endTime > currentTime)
      this._feeTask = ThreadPool.schedule(this::ReturnItemTask, this._endTime - currentTime); 
  }
  
  public int getAuctionId() {
    return this._auctionId;
  }
  
  public int getOwnerId() {
    return this._ownerId;
  }
  
  public String getOwnerName() {
    return this._ownerName;
  }
  
  public int getItemId() {
    return this._itemId;
  }
  
  public int getCount() {
    return this._count;
  }
  
  public int getAugId() {
    return this._augId;
  }
  
  public int getAugSkillId() {
    return this._augSKillId;
  }
  
  public int getAugSkillLevel() {
    return this._augSkillLevel;
  }
  
  public int getEnchant() {
    return this._enchant;
  }
  
  public int getCostId() {
    return this._costId;
  }
  
  public int getCostCount() {
    return this._costCount;
  }
  
  public long getEndTime() {
    return this._endTime;
  }
  
  public String getRemainingTimeString() {
    return (new SimpleDateFormat("dd/MM HH:mm")).format(Long.valueOf(this._endTime));
  }
  
  public void stopFeeTask() {
    if (this._feeTask != null) {
      this._feeTask.cancel(false);
      this._feeTask = null;
    } 
  }
  
  public void ReturnItemTask() {
    MarketplaceTable.getInstance().deleteItem(this);
    Player player = World.getInstance().getPlayer(this._ownerId);
    if (player != null && player.isOnline()) {
      ItemInstance item = player.addItem("return", this._itemId, this._count, (WorldObject)player, false);
      if (item == null)
        return; 
      item.setEnchantLevel(this._enchant);
      if (item.getCount() > 1) {
        player.sendPacket((L2GameServerPacket)SystemMessage.getSystemMessage(SystemMessageId.YOU_PICKED_UP_S2_S1).addItemName(item).addNumber(item.getCount()));
      } else if (item.getEnchantLevel() > 0) {
        player.sendPacket((L2GameServerPacket)SystemMessage.getSystemMessage(SystemMessageId.YOU_PICKED_UP_A_S1_S2).addNumber(item.getEnchantLevel()).addItemName(item));
      } else {
        player.sendPacket((L2GameServerPacket)SystemMessage.getSystemMessage(SystemMessageId.YOU_PICKED_UP_S1).addItemName(item));
      } 
      Augmentation aug = new Augmentation(this._augId, this._augSKillId, this._augSkillLevel);
      item.setAugmentation(aug);
      InventoryUpdate iu = new InventoryUpdate();
      iu.addItem(item);
      player.sendPacket((L2GameServerPacket)iu);
      player.sendMessage("Your item couldn't be sold on the set period, so it's now returning to you. Thanks for using our Auction House system.");
    } else {
      try {
        Connection con = ConnectionPool.getConnection();
        try {
          PreparedStatement stm = con.prepareStatement("INSERT INTO items (owner_id,item_id,count,loc,loc_data,enchant_level,object_id,custom_type1,custom_type2,mana_left,time) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
          try {
            stm.setInt(1, this._ownerId);
            stm.setInt(2, this._itemId);
            stm.setInt(3, this._count);
            stm.setString(4, "INVENTORY");
            stm.setInt(5, 0);
            stm.setInt(6, this._enchant);
            stm.setInt(7, IdFactory.getInstance().getNextId());
            stm.setInt(8, 0);
            stm.setInt(9, 0);
            stm.setInt(10, -1);
            stm.setInt(11, -1);
            stm.executeUpdate();
            if (stm != null)
              stm.close(); 
          } catch (Throwable throwable) {
            if (stm != null)
              try {
                stm.close();
              } catch (Throwable throwable1) {
                throwable.addSuppressed(throwable1);
              }  
            throw throwable;
          } 
          if (con != null)
            con.close(); 
        } catch (Throwable throwable) {
          if (con != null)
            try {
              con.close();
            } catch (Throwable throwable1) {
              throwable.addSuppressed(throwable1);
            }  
          throw throwable;
        } 
      } catch (Exception e) {
        LOGGER.error("Cannout add Item To Offline", e);
      } 
    } 
  }
}
