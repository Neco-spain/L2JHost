package net.sf.l2j.gameserver.data.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.l2j.commons.logging.CLogger;
import net.sf.l2j.commons.pool.ConnectionPool;
import net.sf.l2j.commons.pool.ThreadPool;

import net.sf.l2j.gameserver.model.MarketplaceItem;
import net.sf.l2j.gameserver.model.MarketplaceRequest;

public class MarketplaceTable {
  private static final CLogger LOGGER = new CLogger(MarketplaceTable.class.getName());
  
  private static final String RESTORE_MARKET_ITEM = "SELECT * FROM market_items";
  
  private static final String ADD_MARKET_ITEM = "INSERT INTO market_items (auction_id,owner_id,owner_name,item_id,count,enchant,attributes,skill_id,skill_level,cost_id,cost_count,expiration_time) VALUES (?,?,?,?,?,?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE auction_Id=VALUES(auction_Id)";
  
  private static final String DELETE_MARKET_ITEM = "DELETE FROM market_items WHERE auction_id=?";
  
  private static final String RESTORE_MARKET_SELLER = "SELECT * FROM market_seller";
  
  private static final String UPDATE_MARKET_SELLER = "UPDATE market_seller SET money=? WHERE seller_id=?";
  
  private static final String INSERT_MARKET_SELLER = "INSERT INT market_seller(seller_id, money) VALUES (?,?)";
  
  private static final String RESTORE_MARKET_REQUEST = "SELECT * FROM market_request";
  
  private static final String INSERT_MARKET_REQUEST = "INSERT INTO market_request (request_id,owner_id,tickets,type,pix,status) VALUES (?,?,?,?,?,?)";
  
  private Set<MarketplaceItem> _items = ConcurrentHashMap.newKeySet();
  
  private Map<Integer, Integer> _sellers = new ConcurrentHashMap<>();
  
  private Set<MarketplaceRequest> _request = ConcurrentHashMap.newKeySet();
  
  protected MarketplaceTable() {
    try {
      Connection con = ConnectionPool.getConnection();
      try {
        PreparedStatement ps = con.prepareStatement("SELECT * FROM market_items");
        try {
          ResultSet rs = ps.executeQuery();
          try {
            while (rs.next())
              this._items.add(new MarketplaceItem(rs.getInt("auction_id"), rs.getInt("owner_id"), rs.getString("owner_name"), rs.getInt("item_id"), rs.getInt("count"), rs.getInt("enchant"), rs.getInt("attributes"), rs.getInt("skill_id"), rs.getInt("skill_level"), rs.getInt("cost_id"), rs.getInt("cost_count"), rs.getLong("expiration_time"))); 
            if (rs != null)
              rs.close(); 
          } catch (Throwable throwable) {
            if (rs != null)
              try {
                rs.close();
              } catch (Throwable throwable1) {
                throwable.addSuppressed(throwable1);
              }  
            throw throwable;
          } 
          if (ps != null)
            ps.close(); 
        } catch (Throwable throwable) {
          if (ps != null)
            try {
              ps.close();
            } catch (Throwable throwable1) {
              throwable.addSuppressed(throwable1);
            }  
          throw throwable;
        } 
        ps = con.prepareStatement("SELECT * FROM market_seller");
        try {
          ResultSet rs = ps.executeQuery();
          try {
            while (rs.next())
              this._sellers.put(Integer.valueOf(rs.getInt("seller_id")), Integer.valueOf(rs.getInt("money"))); 
            if (rs != null)
              rs.close(); 
          } catch (Throwable throwable) {
            if (rs != null)
              try {
                rs.close();
              } catch (Throwable throwable1) {
                throwable.addSuppressed(throwable1);
              }  
            throw throwable;
          } 
          if (ps != null)
            ps.close(); 
        } catch (Throwable throwable) {
          if (ps != null)
            try {
              ps.close();
            } catch (Throwable throwable1) {
              throwable.addSuppressed(throwable1);
            }  
          throw throwable;
        } 
        ps = con.prepareStatement("SELECT * FROM market_request");
        try {
          ResultSet rs = ps.executeQuery();
          try {
            while (rs.next())
              this._request.add(new MarketplaceRequest(rs.getInt("request_id"), rs.getInt("owner_id"), rs.getInt("tickets"), rs.getString("type"), rs.getString("pix"), rs.getInt("status"))); 
            if (rs != null)
              rs.close(); 
          } catch (Throwable throwable) {
            if (rs != null)
              try {
                rs.close();
              } catch (Throwable throwable1) {
                throwable.addSuppressed(throwable1);
              }  
            throw throwable;
          } 
          if (ps != null)
            ps.close(); 
        } catch (Throwable throwable) {
          if (ps != null)
            try {
              ps.close();
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
      LOGGER.error("Couldn't load MarketplaceTable.", e);
    } 
    LOGGER.info("Loaded {} Marketplace, {} Sellers, {} Request.", new Object[] { Integer.valueOf(this._items.size()), Integer.valueOf(this._sellers.size()), Integer.valueOf(this._request.size()) });
  }
  
  public void addMoney(int sellerId, int money) {
    if (this._sellers != null)
      if (this._sellers.containsKey(Integer.valueOf(sellerId))) {
        int oldMoney = ((Integer)this._sellers.get(Integer.valueOf(sellerId))).intValue();
        money += oldMoney;
        this._sellers.put(Integer.valueOf(sellerId), Integer.valueOf(money));
        ThreadPool.schedule(new AddMoneyTask(sellerId, money), 2000L);
      } else {
        this._sellers.put(Integer.valueOf(sellerId), Integer.valueOf(money));
        ThreadPool.schedule(new AddSellerTask(sellerId, money), 2000L);
      }  
  }
  
  public int getMoney(int sellerId) {
    if (this._sellers != null && !this._sellers.isEmpty()) {
      if (this._sellers.containsKey(Integer.valueOf(sellerId)))
        return ((Integer)this._sellers.get(Integer.valueOf(sellerId))).intValue(); 
      return 0;
    } 
    return 0;
  }
  
  public void takeMoney(int sellerId, int amount) {
    if (this._sellers != null && !this._sellers.isEmpty())
      if (this._sellers.containsKey(Integer.valueOf(sellerId))) {
        int oldMoney = ((Integer)this._sellers.get(Integer.valueOf(sellerId))).intValue();
        if (oldMoney >= amount) {
          oldMoney -= amount;
          this._sellers.put(Integer.valueOf(sellerId), Integer.valueOf(oldMoney));
          ThreadPool.schedule(new AddMoneyTask(sellerId, oldMoney), 2000L);
        } 
      }  
  }
  
  private static class AddMoneyTask implements Runnable {
    private final int _sellerId;
    
    private final int _money;
    
    public AddMoneyTask(int sellerId, int money) {
      this._sellerId = sellerId;
      this._money = money;
    }
    
    public void run() {
      try {
        Connection con = ConnectionPool.getConnection();
        try {
          PreparedStatement ps = con.prepareStatement("UPDATE market_seller SET money=? WHERE seller_id=?");
          try {
            ps.setInt(1, this._money);
            ps.setInt(2, this._sellerId);
            ps.executeUpdate();
            if (ps != null)
              ps.close(); 
          } catch (Throwable throwable) {
            if (ps != null)
              try {
                ps.close();
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
        MarketplaceTable.LOGGER.warn("Error while adding money in DB " + e.getMessage());
      } 
    }
  }
  
  private static class AddSellerTask implements Runnable {
    private final int _sellerId;
    
    private final int _money;
    
    public AddSellerTask(int sellerId, int money) {
      this._sellerId = sellerId;
      this._money = money;
    }
    
    public void run() {
      try {
        Connection con = ConnectionPool.getConnection();
        try {
          PreparedStatement statement = con.prepareStatement("INSERT INT market_seller(seller_id, money) VALUES (?,?)");
          try {
            statement.setInt(1, this._sellerId);
            statement.setInt(2, this._money);
            statement.executeUpdate();
            if (statement != null)
              statement.close(); 
          } catch (Throwable throwable) {
            if (statement != null)
              try {
                statement.close();
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
        MarketplaceTable.LOGGER.warn("Error while adding seller in DB " + e.getMessage());
      } 
    }
  }
  
  public void addRequest(MarketplaceRequest request) {
    this._request.add(request);
    try {
      Connection con = ConnectionPool.getConnection();
      try {
        PreparedStatement ps = con.prepareStatement("INSERT INTO market_request (request_id,owner_id,tickets,type,pix,status) VALUES (?,?,?,?,?,?)");
        try {
          ps.setInt(1, request.getRequestId());
          ps.setInt(2, request.getOwnerId());
          ps.setInt(3, request.getTikects());
          ps.setString(4, request.getType());
          ps.setString(5, request.getPix());
          ps.setInt(6, request.getStatus());
          ps.executeUpdate();
          if (ps != null)
            ps.close(); 
        } catch (Throwable throwable) {
          if (ps != null)
            try {
              ps.close();
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
      LOGGER.warn("Error while adding request in DB " + e.getMessage());
    } 
  }
  
  public void addItem(MarketplaceItem item) {
    this._items.add(item);
    try {
      Connection con = ConnectionPool.getConnection();
      try {
        PreparedStatement ps = con.prepareStatement("INSERT INTO market_items (auction_id,owner_id,owner_name,item_id,count,enchant,attributes,skill_id,skill_level,cost_id,cost_count,expiration_time) VALUES (?,?,?,?,?,?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE auction_Id=VALUES(auction_Id)");
        try {
          ps.setInt(1, item.getAuctionId());
          ps.setInt(2, item.getOwnerId());
          ps.setString(3, item.getOwnerName());
          ps.setInt(4, item.getItemId());
          ps.setInt(5, item.getCount());
          ps.setInt(6, item.getEnchant());
          ps.setInt(7, item.getAugId());
          ps.setInt(8, item.getAugSkillId());
          ps.setInt(9, item.getAugSkillLevel());
          ps.setInt(10, item.getCostId());
          ps.setInt(11, item.getCostCount());
          ps.setLong(12, item.getEndTime());
          ps.executeUpdate();
          if (ps != null)
            ps.close(); 
        } catch (Throwable throwable) {
          if (ps != null)
            try {
              ps.close();
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
      LOGGER.error("Couldn't store auction item.", e);
    } 
  }
  
  public void deleteItem(MarketplaceItem item) {
    this._items.remove(item);
    try {
      Connection con = ConnectionPool.getConnection();
      try {
        PreparedStatement ps = con.prepareStatement("DELETE FROM market_items WHERE auction_id=?");
        try {
          ps.setInt(1, item.getAuctionId());
          ps.execute();
          if (ps != null)
            ps.close(); 
        } catch (Throwable throwable) {
          if (ps != null)
            try {
              ps.close();
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
      LOGGER.error("Couldn't delete auction item.", e);
    } 
  }
  
  public MarketplaceItem getItem(int auctionId) {
    return this._items.stream().filter(x -> (x.getAuctionId() == auctionId)).findFirst().orElse(null);
  }
  
  public List<MarketplaceItem> getOwnerId(int ownerId) {
    List<MarketplaceItem> auctions = new ArrayList<>();
    for (MarketplaceItem auction : this._items) {
      if (auction.getOwnerId() == ownerId)
        auctions.add(auction); 
    } 
    return auctions;
  }
  
  public Set<MarketplaceItem> getItems() {
    return this._items;
  }
  
  public Map<Integer, Integer> getSellers() {
    return this._sellers;
  }
  
  public Set<MarketplaceRequest> getRequest() {
    return this._request;
  }
  
  public static MarketplaceTable getInstance() {
    return SingletonHolder.INSTANCE;
  }
  
  private static class SingletonHolder {
    protected static final MarketplaceTable INSTANCE = new MarketplaceTable();
  }
}
