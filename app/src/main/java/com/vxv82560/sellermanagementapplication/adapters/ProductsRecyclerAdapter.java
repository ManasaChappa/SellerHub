package com.vxv82560.sellermanagementapplication.adapters;

import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.vxv82560.sellermanagementapplication.R;
import com.vxv82560.sellermanagementapplication.helpers.ImageRetriever;
import com.vxv82560.sellermanagementapplication.model.Product;

import java.util.List;

/**
 * @author veenapaniv
 */
public class ProductsRecyclerAdapter extends RecyclerView.Adapter<ProductsRecyclerAdapter.UserViewHolder> {

    private List<Product> listProducts;
    private AppCompatButton editProduct;
    private AppCompatButton deleteProduct;
    public ProductRecyclerListener onClickListener;

    //this interface is used to set the on click listeners for the two buttons in recycler view
    public interface ProductRecyclerListener {
        void editViewOnClick(View v,int position);
        void deleteViewOnClick(View v,int position);
    }

    //constructor of the recycler view
    public ProductsRecyclerAdapter(List<Product> listProducts, ProductRecyclerListener listener) {

        //initialize the data source and the onClickListener object
        this.listProducts = listProducts;
        this.onClickListener = listener;

    }

    /**
     * Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item.
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // get the context from parent and inflate recycler item view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_recycler, parent, false);
        //finally return the viewHolder item
        return new UserViewHolder(itemView);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final UserViewHolder holder, final int position) {

        //set the data from the data source to the holder object created

        holder.textViewProdID.setText(listProducts.get(position).getProductId()+"");
        holder.textViewProdName.setText(listProducts.get(position).getProductName());
        holder.textViewProdAmount.setText(listProducts.get(position).getAmount()+"");
        holder.textViewProdQuantity.setText(listProducts.get(position).getQuantity()+"");
        holder.textViewShippingRate.setText(listProducts.get(position).getShippingRate()+"");


        //This is to get the product in bytes and set it as a imageBitmap in the holder and display it
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final byte[] bytes = listProducts.get(position).getProductImage();
                    // Show Image from DB in ImageView
                    holder.textProdImage.post(new Runnable() {
                        @Override
                        public void run() {
                            holder.textProdImage.setImageBitmap(ImageRetriever.getImage(bytes));
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    /**Returns the
     * size of the dataset
      */

    @Override
    public int getItemCount() {
        Log.v(ProductsRecyclerAdapter.class.getSimpleName(),""+listProducts.size());
        return listProducts.size();
    }


    /**
     * ViewHolder class which is a nested class to help holding the view elements required for the recycler view
     */
    public class UserViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView textViewProdID;
        public AppCompatImageView textProdImage;
        public AppCompatTextView textViewProdName;
        public AppCompatTextView textViewProdAmount;
        public AppCompatTextView textViewProdQuantity;
        public AppCompatTextView textViewShippingRate;
        public AppCompatButton editButton;
        public AppCompatButton deleteButton;

        public UserViewHolder(View view) {
            super(view);
            textProdImage = view.findViewById(R.id.loadedImg);
            textViewProdID = view.findViewById(R.id.textViewProdID);
            textViewProdName = view.findViewById(R.id.textViewProdName);
            textViewProdAmount = view.findViewById(R.id.textViewProdAmount);
            textViewProdQuantity = view.findViewById(R.id.textViewProdQuantity);
            textViewShippingRate = view.findViewById(R.id.textViewShippingRate);
            editProduct = (AppCompatButton) view.findViewById(R.id.editProduct);
            deleteProduct = (AppCompatButton) view.findViewById(R.id.deleteProduct);

            editProduct.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    onClickListener.editViewOnClick(v,getAdapterPosition());
                }
            });

            deleteProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.deleteViewOnClick(v,getAdapterPosition());
                }
            });



        }
    }


}
