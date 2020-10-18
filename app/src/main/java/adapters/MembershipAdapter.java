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
import com.baroque.lujo.activities.country.CountryModel;
import com.baroque.lujo.activities.my_account.CreditCard;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;
import java.util.List;

public class MembershipAdapter extends RecyclerView.Adapter<MembershipAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    private List<CreditCard> data    ;
    private MembershipAdapter.OnItemClickListener clickListener;
    private int selectedItemIndex = -1 ;

    public MembershipAdapter (Context context, List<CreditCard> data) {
        this.context = context;
        this.data = data;
    }

    public void setOnItemClickListener(MembershipAdapter.OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public MembershipAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_membership, viewGroup, false);
        return new MembershipAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MembershipAdapter.ViewHolder viewHolder, int i) {
        int resID = data.get(i).imageId;
        viewHolder.imgCreditCard.setImageResource(resID);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

//    public void filter(String text) {
//        data.clear();
//        if(text.isEmpty()){
//            data.addAll(dataCopy);
//        } else{
//            text = text.toLowerCase();
//            for(CountryModel item: dataCopy){
//                if(item.getCountry().toLowerCase().contains(text) || item.getPhonePrefix().toLowerCase().contains(text)){
//                    data.add(item);
//                }
//            }
//        }
//        notifyDataSetChanged();
//    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imgCreditCard;

        private ViewHolder(View view) {
            super(view);
            imgCreditCard = view.findViewById(R.id.imgCreditCard);
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

        void onItemClick(int position, CreditCard data);
    }


}


