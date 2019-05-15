package com.vxv82560.sellermanagementapplication.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.vxv82560.sellermanagementapplication.R;
import com.vxv82560.sellermanagementapplication.helpers.ImageRetriever;
import com.vxv82560.sellermanagementapplication.model.Product;
import com.vxv82560.sellermanagementapplication.sql.ProductDatabaseHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class EditProductActivity extends AppCompatActivity {

    private final AppCompatActivity activity = EditProductActivity.this;
    private int productID;

    private EditText productNameEtv;
    private EditText amountEtv;
    private EditText quantityEtv;
    private EditText shippingRateEtv;
    private AppCompatImageView loadimage;
    private Button btnEditImage;
    private Uri selectedImageUri;
    private static final int SELECT_PICTURE = 100;
    private int userID;

    String editProductName;
    int editQuantity;
    float editAmount;
    float editShippingRate;

    public ProductDatabaseHelper productDatabaseHelper;
    public Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        Toolbar toolbar = (Toolbar) findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        productDatabaseHelper = new ProductDatabaseHelper(activity);
        product = new Product();

        final Intent receiveIntent = getIntent();

        loadimage = findViewById(R.id.editloadedImg);
        productNameEtv = findViewById(R.id.edit_product_name);
        amountEtv = findViewById(R.id.edit_amount);
        quantityEtv = findViewById(R.id.edit_quantity);
        shippingRateEtv = findViewById(R.id.edit_shipping);
        btnEditImage = findViewById(R.id.btnEditImage);
        userID = receiveIntent.getIntExtra("User_Id",0);
        //This onClickListener helps in editing the product's image
        btnEditImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);

            }
        });



        //set the values in the edit text fields

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final byte[] bytes = receiveIntent.getByteArrayExtra("image");
                    // Show Image from DB in ImageView
                    loadimage.post(new Runnable() {
                        @Override
                        public void run() {
                            //Sets a Bitmap as the content of this ImageView.
                            loadimage.setImageBitmap(ImageRetriever.getImage(bytes));
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        productNameEtv.setText(receiveIntent.getStringExtra("productName"));
        amountEtv.setText(receiveIntent.getFloatExtra("amount", (float) 0.0)+"");
        quantityEtv.setText(receiveIntent.getIntExtra("quantity",0)+"");
        shippingRateEtv.setText(receiveIntent.getFloatExtra("shippingRate", (float) 0.0)+"");
        productID = receiveIntent.getIntExtra("productID",0);


        AppCompatButton saveButton = findViewById(R.id.appCompatButtonSave);
        saveButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                byte[] inputData = new byte[0];

                editProductName = productNameEtv.getText().toString();
                editAmount = Float.parseFloat(amountEtv.getText().toString());
                editQuantity = Integer.parseInt(quantityEtv.getText().toString());
                editShippingRate = Float.parseFloat(shippingRateEtv.getText().toString());
                if(selectedImageUri != null) {
                    try {
                        //gets the content resolver and opens the input stream for the image uri
                        InputStream iStream = getContentResolver().openInputStream(selectedImageUri);
                        //retrives bytes of the image stream
                        inputData = ImageRetriever.getBytes(iStream);
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                    //sets the product image
                    product.setProductImage(inputData);
                }

                else{
                    product.setProductImage(receiveIntent.getByteArrayExtra("image"));
                }

                //set product details
                product.setProductId(productID);
                product.setProductName(editProductName);
                product.setAmount(editAmount);
                product.setQuantity(editQuantity);
                product.setShippingRate(editShippingRate);
                //save the product to DB
                productDatabaseHelper.editProductByID(product);

                //navigate to the dashboard
                Intent inn = new Intent(getApplicationContext(),ProductListActivity.class);
                inn.putExtra("User_Id",userID);
                startActivity(inn);
            }
        });




    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    loadimage.setImageURI(selectedImageUri);
                }
            }
        }
    }
}
