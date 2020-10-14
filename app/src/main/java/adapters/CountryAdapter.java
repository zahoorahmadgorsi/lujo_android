package adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.baroque.lujo.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;
import java.util.List;

import com.baroque.lujo.activities.country.CountryModel;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    private List<CountryModel> data , dataCopy   ;
    private CountryAdapter.OnItemClickListener clickListener;
    private int selectedItemIndex = -1 ;

    public CountryAdapter (Context context, List<CountryModel> countries) {
        this.context = context;
        this.data = countries;
        if (dataCopy == null)  {
            dataCopy = new ArrayList<>();
        }
        this.dataCopy.addAll(this.data);
    }

//    public void addCountries( List<CountryModel> countries){
//        this.data = countries;
//        if (dataCopy == null)  {
//            dataCopy = new ArrayList<>();
//        }
//        this.dataCopy.addAll(this.data);
//        notifyDataSetChanged();
//    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_country, viewGroup, false);
        return new CountryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        String imgURL = data.get(i).getImage();
        Glide.with(context)
                .asBitmap()
                .load(imgURL)
                .placeholder(R.drawable.ic_placeholder)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        viewHolder.imgCountry.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }

                });
        viewHolder.tvCountryName.setText(data.get(i).getCountry());
        viewHolder.tvCountryCode.setText( data.get(i).getPhonePrefix());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void filter(String text) {
        data.clear();
        if(text.isEmpty()){
            data.addAll(dataCopy);
        } else{
            text = text.toLowerCase();
            for(CountryModel item: dataCopy){
                if(item.getCountry().toLowerCase().contains(text) || item.getPhonePrefix().toLowerCase().contains(text)){
                    data.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imgCountry;
        private TextView tvCountryName;
        private TextView tvCountryCode;

        private ViewHolder(View view) {
            super(view);
            imgCountry = view.findViewById(R.id.cImgCountry);
            tvCountryName = view.findViewById(R.id.tvCountryName);
            tvCountryCode = view.findViewById(R.id.tvPhonePrefix);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                selectedItemIndex = getLayoutPosition();
//                notifyItemChanged(selectedItemIndex);
                clickListener.onItemClick(getLayoutPosition(), data.get(getLayoutPosition()));
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, CountryModel countryModel);
    }

}

