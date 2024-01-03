// OrderItem.java
package com.example;

public class OrderItem {
    private String senderAddress;
    private String receiverAddress;
    private String itemType;
    private String deliveryTime;

    public OrderItem(String senderAddress, String receiverAddress, String itemType, String deliveryTime) {
        this.senderAddress = senderAddress;
        this.receiverAddress = receiverAddress;
        this.itemType = itemType;
        this.deliveryTime = deliveryTime;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public String getItemType() {
        return itemType;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }
}
