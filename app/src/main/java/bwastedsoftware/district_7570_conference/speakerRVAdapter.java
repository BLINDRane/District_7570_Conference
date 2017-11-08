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
import com.twitter.sdk.android.core.models.Card;

import java.util.List;

import static android.R.attr.fragment;

public class speakerRVAdapter extends RecyclerView.Adapter<speakerRVAdapter.CardViewHolder>{


    public static class CardViewHolder extends RecyclerView.ViewHolder
    {
        CardView cv;
        TextView speakerName;
        ImageView speakerPhoto;
        View view;

        CardViewHolder(View itemView)
        {
            super(itemView);
            this.view = itemView;
            cv = (CardView) itemView.findViewById(R.id.cv);
            speakerName = (TextView) itemView.findViewById(R.id.card_title);
            speakerPhoto = (ImageView) itemView.findViewById(R.id.card_photo);
        }
    }

    private List<Speaker> cards;
    private Context context;
    private SpeakerListFragment fragment;

    speakerRVAdapter(List<Speaker> cards, Context context, SpeakerListFragment fragment)
    {
        this.cards = cards;
        this.context = context;
        this.fragment = fragment;
    }

    @Override
    public void onBindViewHolder(CardViewHolder cardViewHolder, final int i) {
        cardViewHolder.speakerName.setText(cards.get(i).getName());
        Picasso.with(context).load(cards.get(i).getPhotoURL()).fit().placeholder(R.drawable.ic_account_circle_black_24dp).into(cardViewHolder.speakerPhoto);

        cardViewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.i("W4K","Click-"+position);
                fragment.loadSpeakerDetails(cards.get(i));
            }
        });
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

