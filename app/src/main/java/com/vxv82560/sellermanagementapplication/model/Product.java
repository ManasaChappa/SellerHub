package com.vxv82560.sellermanagementapplication.model;


import android.net.Uri;

import java.net.URI;
import java.sql.Blob;
import java.sql.Timestamp;
import java.util.List;

public class Product {
    private int productId;
    private String productName;
    private byte[] productImage;
    private String image;
    private int quantity;
    private float amount;
    private float shippingRate;
    private java.sql.Timestamp lastUpdated;
    private List<String> channels;
    private int sold;
    private int returned;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public byte[] getProductImage() {
        return productImage;
    }

    public void setProductImage(byte[] productImage) {
        this.productImage = productImage;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getShippingRate() {
        return shippingRate;
    }

    public void setShippingRate(float shippingRate) {
        this.shippingRate = shippingRate;
    }

    public List<String> getChannels() {
        return channels;
    }

    public void setChannels(List<String> channels) {
        this.channels = channels;
    }

    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }

    public int getReturned() {
        return returned;
    }

    public void setReturned(int returned) {
        this.returned = returned;
    }

    public Timestamp getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Timestamp lastUpdated) {
        this.lastUpdated = lastUpdated;
    }



}
