package bwastedsoftware.district_7570_conference;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class SpeakerRVAdapter extends RecyclerView.Adapter<SpeakerRVAdapter.CardViewHolder>{


    public static class CardViewHolder extends RecyclerView.ViewHolder
    {
        CardView cv;
        TextView speakerName;
        ImageView speakerPhoto;
        TextView speakerBio;
        View view;

        CardViewHolder(View itemView)
        {
            super(itemView);
            this.view = itemView;
            cv = (CardView) itemView.findViewById(R.id.cv);
            speakerName = (TextView) itemView.findViewById(R.id.card_title);
            speakerPhoto = (ImageView) itemView.findViewById(R.id.card_photo);
            speakerBio = (TextView) itemView.findViewById(R.id.card_text);
        }
    }

    private List<Speaker> cards;
    private Context context;
    private SpeakerListFragment fragment;

    SpeakerRVAdapter(List<Speaker> cards, Context context, SpeakerListFragment fragment)
    {
        this.cards = cards;
        this.context = context;
        this.fragment = fragment;
    }

    @Override
    public void onBindViewHolder(final CardViewHolder cardViewHolder, final int i) {
        cardViewHolder.speakerName.setText(cards.get(i).getName());
        cardViewHolder.speakerBio.setText(cards.get(i).getTitle());
        Picasso.with(context).load(cards.get(i).getPhotoURL()).fit().centerCrop().transform(new PicassoCircleTransform()).placeholder(R.drawable.ic_account_circle_black_24dp).into(cardViewHolder.speakerPhoto);

        cardViewHolder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                CharSequence options[] = new CharSequence[] {"View Speaker", "Remove Speaker", "Cancel"};

                AlertDialog.Builder builder = new AlertDialog.Builder(cardViewHolder.view.getContext());
                builder.setTitle("Options");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int choice = which;
                        switch(choice) {
                            case 0:
                                fragment.loadSpeakerDetails(cards.get(i));
                                break;
                            case 1:
                                fragment.removeSpeaker(cards.get(i));
                                break;
                            case 2:
                                break;
                        }

                    }
                });
                builder.show();
                return true;
            }
        });

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
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_card, viewGroup, false);
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

