package bwastedsoftware.district_7570_conference;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class clueRVAdapter extends RecyclerView.Adapter<clueRVAdapter.CardViewHolder>{


    public static class CardViewHolder extends RecyclerView.ViewHolder
    {
        CardView cv;
        TextView clueInfo;
        View view;

        CardViewHolder(View itemView)
        {
            super(itemView);
            this.view = itemView;
            cv = (CardView) itemView.findViewById(R.id.cv);
            clueInfo = (TextView) itemView.findViewById(R.id.clue_Title);
        }
    }

    private List<Clue> cards;
    private Context context;
    private scavengerHunt fragment;

    clueRVAdapter(List<Clue> cards, Context context, scavengerHunt fragment)
    {
        this.cards = cards;
        this.context = context;
        this.fragment = fragment;
    }

    @Override
    public void onBindViewHolder(CardViewHolder cardViewHolder, final int i) {
        cardViewHolder.clueInfo.setText(cards.get(i).getTitle());
        cardViewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.i("W4K","Click-"+position);
                fragment.loadClue(cards.get(i));
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
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.clue_card, viewGroup, false);
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

