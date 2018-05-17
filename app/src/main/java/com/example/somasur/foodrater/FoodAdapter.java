package com.example.somasur.foodrater;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {

    private Context mContext;
    final ArrayList<String> mFoodNames = new ArrayList<>();
    final ArrayList<Integer> mFoodImages = new ArrayList<>();
    HashMap<String, Float> myRatings = new HashMap<String, Float>();
    private Random mRandom = new Random();
    private RecyclerView mrecyclerView;

    public FoodAdapter(Context context, RecyclerView recyclerView) {
        mContext = context;
        mrecyclerView = recyclerView;
        while (mFoodNames.size() < 5) {
            int index = mRandom.nextInt(8);
            if (!mFoodNames.contains(getRandomName(index))) {
                addRandomName(index);
                addRandomImage(index);
            }
        }
    }

    private void addRandomName(int index) {
        String[] names = new String[]{
                "Banana", "Briyani", "Dosa", "Idli",
                "Poori", "Mutton gravy", "Samosa", "Gulab Jamun"
        };
        mFoodNames.add(names[index]);
    }

    private String getRandomName(int index) {
        String[] names = new String[]{
                "Banana", "Briyani", "Dosa", "Idli",
                "Poori", "Mutton gravy", "Samosa", "Gulab Jamun"
        };
        return names[index];
    }

    private void addRandomImage(int index) {
        TypedArray imgs = mContext.getResources().obtainTypedArray(R.array.food_imgs);
        mFoodImages.add(imgs.getResourceId(index, R.drawable.food_1));
    }

    private int getRandomImage(int index) {
        TypedArray imgs = mContext.getResources().obtainTypedArray(R.array.food_imgs);
        return imgs.getResourceId(index, R.drawable.food_1);
    }

    @Override
    public int getItemCount() {
        return mFoodNames.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_view, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String name = mFoodNames.get(position);
        holder.mNameTextView.setText(name);
        holder.mFoodImageView.setImageResource(mFoodImages.get(position));
        if (!myRatings.isEmpty()) {
            Float value;
            if (!(myRatings.get(name) == null)) {
                value = myRatings.get(name);
                holder.mRatingBarView.setRating(value);
            } else {
                holder.mRatingBarView.setRating(Float.parseFloat("0"));
            }

        }
    }


    public String addName() {
        String foodAdded = "";
        while (mFoodNames.size() < 8) {
            int index = mRandom.nextInt(8);
            if (!mFoodNames.contains(getRandomName(index))) {
                mFoodNames.add(0, getRandomName(index));
                mFoodImages.add(0, getRandomImage(index));
                notifyItemInserted(0);
                notifyItemRangeChanged(0, mFoodImages.size());
                mrecyclerView.getLayoutManager().scrollToPosition(0);
                foodAdded = getRandomName(index);
                break;
            }
        }
        return foodAdded;
    }

    public void removeName(int position) {
        String foodToBeRemoved = mFoodNames.get(position);
        mFoodNames.remove(position);
        mFoodImages.remove(position);
        myRatings.remove(foodToBeRemoved);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mFoodNames.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mNameTextView;
        private ImageView mFoodImageView;
        private RatingBar mRatingBarView;


        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    removeName(getAdapterPosition());
                    return false;
                }
            });

            mNameTextView = itemView.findViewById(R.id.name_view);
            mFoodImageView = itemView.findViewById(R.id.image_view);
            mRatingBarView = itemView.findViewById(R.id.ratingBar);

            mRatingBarView.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                public void onRatingChanged(RatingBar ratingBar, float rating,
                                            boolean fromUser) {
//                    HashMap<String, Float> map = new HashMap<String, Float>();
                    myRatings.put(mFoodNames.get(getAdapterPosition()), rating);

                }
            });
        }
    }
}
