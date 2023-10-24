package net.sf.l2j.gameserver.model;

public class MarketplaceRequest {
  private int _requestId;
  
  private int _ownerId;
  
  private int _tickets;
  
  private String _type;
  
  private String _pix;
  
  private int _status;
  
  public MarketplaceRequest(int requestId, int ownerId, int tickets, String type, String pix, int status) {
    this._requestId = requestId;
    this._ownerId = ownerId;
    this._tickets = tickets;
    this._type = type;
    this._pix = pix;
    this._status = status;
  }
  
  public int getRequestId() {
    return this._requestId;
  }
  
  public int getOwnerId() {
    return this._ownerId;
  }
  
  public int getTikects() {
    return this._tickets;
  }
  
  public String getType() {
    return this._type;
  }
  
  public String getPix() {
    return this._pix;
  }
  
  public int getStatus() {
    return this._status;
  }
}
