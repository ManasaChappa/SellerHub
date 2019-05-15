package com.vxv82560.sellermanagementapplication.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.vxv82560.sellermanagementapplication.R;
import com.vxv82560.sellermanagementapplication.helpers.ImageRetriever;
import com.vxv82560.sellermanagementapplication.model.Product;
import com.vxv82560.sellermanagementapplication.sql.ProductDatabaseHelper;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author veenapaniv
 */
public class AddProductActivity extends AppCompatActivity implements View.OnClickListener  {
    private final AppCompatActivity activity = AddProductActivity.this;
    private static final int SELECT_PICTURE = 100;

    //Declare variables for all the view elements and other required valriables
    private EditText productName;
    private EditText amount;
    private EditText quantity;
    private EditText shipping;
    private Button btnOpenGallery;
    private AppCompatImageView imgView;
    private Uri selectedImageUri;


    private AppCompatButton appCompatButtonReset;
    private AppCompatButton appCompatButtonSubmit;

    private Product product;
    ProductDatabaseHelper productDatabaseHelper;

    /**
     * This method is called when the activity is created. This is where most initialization should be done!
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        Toolbar toolbar = (Toolbar) findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        initViews();
        initListeners();
        initObjects();
    }

    /**
     * This method initializes the view elements required for the activity.
     */
    private void initViews() {
        btnOpenGallery = findViewById(R.id.btnSelectImage);
        imgView = findViewById(R.id.imgView);
        productName = findViewById(R.id.product_name);
        amount = findViewById(R.id.amount);
        quantity = findViewById(R.id.quantity);
        shipping = findViewById(R.id.shipping);

        appCompatButtonReset = findViewById(R.id.appCompatButtonReset);
        appCompatButtonSubmit = findViewById(R.id.appCompatButtonSubmit);
    }

    /**
     * This method is to initialize listeners
     */
    private void initListeners() {
        Log.e("this","this is"+this);
        Log.e("appCompatButtonReset","appCompatButtonReset"+appCompatButtonReset);
        btnOpenGallery.setOnClickListener(this);
        appCompatButtonReset.setOnClickListener(this);
        appCompatButtonSubmit.setOnClickListener(this);

    }

    /**
     * This implemented method is to listen the click on view
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSelectImage:
                openImageChooser();

            case R.id.appCompatButtonReset:
                 resetData();
                break;

            case R.id.appCompatButtonSubmit:
                postDataToSQLite();
                break;
        }
    }

    private void resetData() {
        imgView.setImageBitmap(null);
        productName.setText("");
        amount.setText("");
        quantity.setText("");
        shipping.setText("");
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        //Allow the user to select a particular kind of data and return it
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //use startActivityForResult to launch createChooser activity for which you would like a result when it finished.
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    /**
     * This method is called when the activity createChooser exists returning us a result code.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    imgView.setImageURI(selectedImageUri);
                }
            }
        }
    }


    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        productDatabaseHelper = new ProductDatabaseHelper(activity);
        product = new Product();

    }

    /**
     * This method is to validate the input text fields and post data to SQLite
     */
    private void postDataToSQLite() {
        Intent in = getIntent();
        byte[] inputData = new byte[0];
        //Get the user_Id from the intent
        int userId = in.getIntExtra("User_Id",0);

        try {
            //get the content resolver instance and open a stream on to the content associated with a content URI.
            InputStream iStream = getContentResolver().openInputStream(selectedImageUri);
            //Get the strem of image as bytes
            inputData = ImageRetriever.getBytes(iStream);
        }catch (IOException ioe) {
            ioe.printStackTrace();
        }
        //finally set the product image
        product.setProductImage(inputData);
        //set product name and other attributes
        product.setProductName(productName.getText().toString());
        product.setAmount(Float.parseFloat(amount.getText().toString()));
        product.setQuantity(Integer.parseInt(quantity.getText().toString()));
        //Save the product to database
        productDatabaseHelper.addProduct(product,userId);
        //start the productListActivity after saving the product to DB.
        Intent gotoAddProductin = new Intent(getApplicationContext(),ProductListActivity.class);
        gotoAddProductin.putExtra("User_Id",userId);
        startActivity(gotoAddProductin);

    }

}
