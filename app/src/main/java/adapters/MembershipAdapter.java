package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baroque.lujo.R;
import com.baroque.lujo.activities.my_account.CreditCard;

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
        viewHolder.tvUserName.setText(data.get(i).userName);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imgCreditCard;
        private TextView tvUserName;

        private ViewHolder(View view) {
            super(view);
            imgCreditCard = view.findViewById(R.id.imgCreditCard);
            tvUserName = view.findViewById(R.id.tvUserName);
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


