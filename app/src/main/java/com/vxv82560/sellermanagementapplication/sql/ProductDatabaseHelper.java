package com.vxv82560.sellermanagementapplication.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.vxv82560.sellermanagementapplication.model.Product;
import com.vxv82560.sellermanagementapplication.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is to insert products to database
 * @author veenapaniv
 */
public class ProductDatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 15;

    // Database Name
    private static final String DATABASE_NAME = "SellerManagement.db";

    // User table name
    private static final String TABLE_INVENTORY = "inventory";

    // User Table Columns names
    private static final String COLUMN_PRODUCT_ID = "product_id";
    private static final String COLUMN_PRODUCT_NAME = "product_name";
    private static final String COLUMN_PRODUCT_IMAGE = "product_image";
    private static final String COLUMN_QUANTITY = "quantity";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_SHIPPING_RATE = "shipping_rate";
    private static final String COLUMN_LAST_UPDATED = "last_updated";
    private static final String COLUMN_USER_ID = "user_id";

    private static final String TABLE_USER = "user";

    /**
     * Constructor
     *
     * @param context
     */
    public ProductDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //do nothing as we are creating the table in the main dbhelper
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        onCreate(db);

    }

    /***This method is to add a product
     *
     */
    public void addProduct(Product product, int User_Id){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues product_basic_values = new ContentValues();

        product_basic_values.put(COLUMN_PRODUCT_IMAGE,product.getProductImage());

        product_basic_values.put(COLUMN_PRODUCT_NAME,product.getProductName());
        product_basic_values.put(COLUMN_QUANTITY,product.getQuantity());
        product_basic_values.put(COLUMN_AMOUNT,product.getAmount());
        product_basic_values.put(COLUMN_SHIPPING_RATE,product.getShippingRate());
        product_basic_values.put(COLUMN_LAST_UPDATED,"");
        product_basic_values.put(COLUMN_USER_ID,User_Id);

        db.insert(TABLE_INVENTORY, null, product_basic_values);
        db.close();
        // Log.e("product_Insert","inserted");
    }

    public List<Product> getAllProducts(int userId) {
        Log.e("inPDHUIDis","In PDH userID is"+userId);
        // array of columns to fetch
        String[] columns = {
                COLUMN_PRODUCT_ID,
                COLUMN_PRODUCT_NAME,
                COLUMN_PRODUCT_IMAGE,
                COLUMN_AMOUNT,
                COLUMN_QUANTITY,
                COLUMN_SHIPPING_RATE
        };
        String[] whereArgs= {""+userId};
        // sorting orders
        String sortOrder =
                COLUMN_PRODUCT_ID + " ASC";
        List<Product> productList = new ArrayList<Product>();

        SQLiteDatabase db = this.getReadableDatabase();

        // query the user table
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
         */
        Cursor cursor = db.query(TABLE_INVENTORY, //Table to query
                columns,    //columns to return
                COLUMN_USER_ID+" = ?",        //columns for the WHERE clause
                whereArgs,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setProductId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_ID))));
                product.setProductName(cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_NAME)));
                product.setProductImage(cursor.getBlob(cursor.getColumnIndex(COLUMN_PRODUCT_IMAGE)));
                product.setAmount(cursor.getInt(cursor.getColumnIndex(COLUMN_AMOUNT)));
                product.setQuantity(cursor.getInt(cursor.getColumnIndex(COLUMN_QUANTITY)));
                product.setShippingRate(cursor.getFloat(cursor.getColumnIndex(COLUMN_SHIPPING_RATE)));
                // Adding user record to list
                productList.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return user list
        return productList;
    }


    public int editProductByID(Product product){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] whereArgs= {""+product.getProductId()};

        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_PRODUCT_NAME,product.getProductName());
        contentValues.put(COLUMN_PRODUCT_IMAGE,product.getProductImage());
        contentValues.put(COLUMN_QUANTITY,product.getQuantity());
        contentValues.put(COLUMN_AMOUNT,product.getAmount());
        contentValues.put(COLUMN_SHIPPING_RATE,product.getShippingRate());
        contentValues.put(COLUMN_LAST_UPDATED,"");
        //update the inventory table
        int count =db.update(
                TABLE_INVENTORY,contentValues,
                COLUMN_PRODUCT_ID+" = ?",whereArgs );
        return count;
    }

    public  int deleteProductById(int productId)
    {
        //Create and/or open a database that will be used for reading and writing
        SQLiteDatabase db = this.getWritableDatabase();
        //set the where conditions
        String[] whereArgs ={""+productId};
        //delete from table
        int count =db.delete(TABLE_INVENTORY ,COLUMN_PRODUCT_ID+" = ?",whereArgs);
        return  count;
    }

}
