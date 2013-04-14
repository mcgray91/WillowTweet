package com.example.willowtweet;

import java.util.Date;

public class Order {
    private long  id;
    private String orderName;
    private String orderStatus;
    private Date orderTime;
    private String orderPic;
    
    public Order(){
    	
    }
    
    public Order(final long id, final String orderName, final String orderStatus, final Date orderTime, final String orderPic){
    	this.id = id;
    	this.orderName = orderName;
    	this.orderStatus = orderStatus;
    	this.orderTime = orderTime;
    	this.orderPic = orderPic;
    }
       
    public long getId(){
    	return id;
    }
    
    public void setId(long id) {
    	this.id = id;
    }

	public String getOrderName() {
        return orderName;
    }
    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }
    public String getOrderStatus() {
        return orderStatus;
    }
    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
    public Date getOrderTime(){
    	return orderTime;
    }
    public void setOrderTime(Date orderTime){
    	this.orderTime = orderTime;
    }
    public String getOrderPic() {
        return orderPic;
    }
    public void setOrderPic(String orderPic) {
        this.orderPic = orderPic;
    }
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
