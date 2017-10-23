package bwastedsoftware.district_7570_conference;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.CardViewHolder>{


    public static class CardViewHolder extends RecyclerView.ViewHolder
    {
        CardView cv;
        TextView eventTitle;
        TextView eventTime;
        ImageView speakerPhoto;

        CardViewHolder(View itemView)
        {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            eventTitle = (TextView) itemView.findViewById(R.id.card_title);
            eventTime = (TextView) itemView.findViewById(R.id.card_text);
            speakerPhoto = (ImageView) itemView.findViewById(R.id.card_photo);
        }
    }

    private List<Event> cards;
    private Context context;

    RVAdapter(List<Event> cards, Context context)
    {
        this.cards = cards;
        this.context = context;
    }


    @Override
    public void onBindViewHolder(CardViewHolder cardViewHolder, int i) {
        cardViewHolder.eventTitle.setText(cards.get(i).getTitle());
        cardViewHolder.eventTime.setText(cards.get(i).getTime());
        Picasso.with(context).load(cards.get(i).getSpeaker().getPhotoURL()).fit().placeholder(R.drawable.ic_account_circle_black_24dp).into(cardViewHolder.speakerPhoto);
    }

    @Override
    public int getItemCount()
    {
        return cards.size();
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_item, viewGroup, false);
        CardViewHolder pvh = new CardViewHolder(v);
        return pvh;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void clear() {
        int size = this.cards.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.cards.remove(0);
            }

            this.notifyItemRangeRemoved(0, size);
        }
    }

}
