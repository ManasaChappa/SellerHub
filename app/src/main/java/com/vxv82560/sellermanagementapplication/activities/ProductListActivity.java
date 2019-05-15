package com.vxv82560.sellermanagementapplication.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;


import com.vxv82560.sellermanagementapplication.MapsActivity;
import com.vxv82560.sellermanagementapplication.R;
import com.vxv82560.sellermanagementapplication.adapters.ProductsRecyclerAdapter;
import com.vxv82560.sellermanagementapplication.model.Product;
import com.vxv82560.sellermanagementapplication.sql.ProductDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author veenapaniv
 * This class displays the recycler view elements.
 */
public class ProductListActivity extends AppCompatActivity {
    private AppCompatActivity activity = ProductListActivity.this;

    private AppCompatTextView textViewName;
    private RecyclerView recyclerViewProducts;
    private List<Product> listProducts;
    private ProductsRecyclerAdapter productsRecyclerAdapter;
    private ProductDatabaseHelper productDatabaseHelper;
    private int userId;
    VideoView vv;
    MediaController mc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        initViews();
        initObjects();
        //register the news button as a context menu button
        registerForContextMenu(findViewById(R.id.news));

        vv= (VideoView) findViewById(R.id.sellerVideoView);
        //set the video path
        String videoPath= "android.resource://" + getPackageName() + "/" +R.raw.sellervideo;
        //Initialize the media controller
        mc= new MediaController(this);
        //Set the view that acts as the anchor for the control view.
        mc.setAnchorView(vv);
        //set the media controller
        vv.setMediaController(mc);
        //set the video path
        vv.setVideoPath(videoPath);
        //start the video
        vv.start();

    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        //initialize the popup menu inflater
        MenuInflater inflater = popup.getMenuInflater();
        //inflate the popup menu
        inflater.inflate(R.menu.popup_menu, popup.getMenu());
        //set the onclick listener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                return MenuClick(item);
            }
        });
        popup.show();
    }


    public boolean MenuClick(MenuItem item){
        switch (item.getItemId()) {
            //when user clicks on this button reload all the products for the particular user
            case R.id.refresh:
                Toast.makeText(getApplicationContext(),"clicked",Toast.LENGTH_SHORT);
                Intent inn = getIntent();
                int userID = inn.getIntExtra("User_Id",1);
                getDataFromSQLite(userID);
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.seller_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.addProduct:
                int userId = getIntent().getIntExtra("User_Id",0);
                Intent newProductIntent = new Intent(getApplicationContext(),AddProductActivity.class);
                newProductIntent.putExtra("User_Id",userId);
                startActivity(newProductIntent);
                return true;
            case R.id.call:  openDialer();
                return true;
            case R.id.mail:
                sendMail();
                return true;
            case R.id.maps:
                Intent in = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(in);
                return true;
            case R.id.myChannels:
                Toast.makeText(getApplicationContext(),"clicked",Toast.LENGTH_SHORT);
                Intent inn = new Intent(getApplicationContext(), ChannlesActivity.class);
                inn.putExtra("User_Id",getIntent().getIntExtra("User_Id",0));
                startActivity(inn);
                return true;
            case R.id.logout:
                Intent logout = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(logout);
                return true;
            default: super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    private void openDialer() {
        // It open the dialer app and allow user to call the number manually
        Intent intent = new Intent(Intent.ACTION_DIAL);
        // Send phone number to intent as data
        intent.setData(Uri.parse("tel:" + "+12336601436"));
        // Start the dialer app activity with number
        startActivity(intent);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Context Menu");
        menu.add(0, v.getId(), 0, "Latest News");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        String url = "https://ecommerceguide.com/news/";
        //Implicit intent for web view
        Intent i = new Intent(Intent.ACTION_VIEW);
        //set the required url
        i.setData(Uri.parse(url));
        startActivity(i);

        return true;
    }

    private void sendMail() {

        Intent mailIntent;
        //Get the input values and convert them to string
        String toEmail = "support@sellermanagement.com";
        String ccEmail = "admin@sellermanagement.com";
        String sub = "Support Mail";


        //Add cc & bcc as well as query parameters to setData method.
        mailIntent = new Intent(Intent.ACTION_SENDTO);
        mailIntent.setData(Uri.parse("mailto:" + toEmail + "?cc=" + ccEmail));
        mailIntent.putExtra(Intent.EXTRA_EMAIL, toEmail);
        mailIntent.putExtra(Intent.EXTRA_CC, ccEmail);
        mailIntent.putExtra(Intent.EXTRA_SUBJECT, sub);
        //returns the activity component that is necessary for handling this intent by resolving the package manager
        if (mailIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mailIntent);
        }
    }


    /**
     * This method is to initialize views
     */
    private void initViews() {
        textViewName = findViewById(R.id.textViewName);
        recyclerViewProducts = findViewById(R.id.recyclerViewProducts);
        Intent in = getIntent();
        userId = in.getIntExtra("User_Id",0);
    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        listProducts = new ArrayList<>();
        productsRecyclerAdapter = new ProductsRecyclerAdapter(listProducts, new ProductsRecyclerAdapter.ProductRecyclerListener() {
            @Override
            public void editViewOnClick(View v, int position) {
               Intent editIntent = new Intent(getApplicationContext(),EditProductActivity.class);
               int productId = listProducts.get(position).getProductId();
               String productName = listProducts.get(position).getProductName();
               float amount = listProducts.get(position).getAmount();
               int quantity = listProducts.get(position).getQuantity();
               float shippingRate = listProducts.get(position).getShippingRate();
               editIntent.putExtra("User_Id",userId);
               editIntent.putExtra("image",listProducts.get(position).getProductImage());
               Log.e("image","image is"+listProducts.get(position).getProductImage()+"");
               editIntent.putExtra("productID",productId);
               editIntent.putExtra("productName",productName);
               editIntent.putExtra("amount",amount);
               editIntent.putExtra("quantity",quantity);
               editIntent.putExtra("shippingRate",shippingRate);
               //start the intent activity
               startActivity(editIntent);
            }

            @Override
            public void deleteViewOnClick(View v, int position) {
                Log.e("Clicked","clicked at "+position);
                int productID = listProducts.get(position).getProductId();
                //delete the selected product
                productDatabaseHelper.deleteProductById(productID);
                Intent inn = getIntent();
                int userID = inn.getIntExtra("User_Id",1);
                getDataFromSQLite(userID);
            }
        });

        //set linear Layout as the layout for recycler view
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        //set the layout manager intialized above
        recyclerViewProducts.setLayoutManager(mLayoutManager);
        //set item animator
        recyclerViewProducts.setItemAnimator(new DefaultItemAnimator());
        //set has fixed size to true so as to manage the performance of the application
        recyclerViewProducts.setHasFixedSize(true);
        //set the recycler view adapter
        recyclerViewProducts.setAdapter(productsRecyclerAdapter);
        productDatabaseHelper = new ProductDatabaseHelper(activity);

        String emailFromIntent = getIntent().getStringExtra("EMAIL");
        textViewName.setText(emailFromIntent);

        AppCompatButton addNewProduct = (AppCompatButton) findViewById(R.id.addProduct);
        Intent inn = getIntent();
        int userID = inn.getIntExtra("User_Id",1);
        //get the data from SQL and load
        getDataFromSQLite(userID);
    }

    /**
     * This method is to fetch all user records from SQLite
     */
    private void getDataFromSQLite(int userID) {
        Intent inn = getIntent();
        final int user_ID = userID;
        // AsyncTask is used so that SQLite operation not blocks the UI Thread.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                listProducts.clear();
                Log.e("returnedtoPLA","returned to PLA"+user_ID);
                listProducts.addAll(productDatabaseHelper.getAllProducts(user_ID));

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                productsRecyclerAdapter.notifyDataSetChanged();
            }
        }.execute();
    }


}
