package bwastedsoftware.district_7570_conference;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.CardViewHolder> {


    public static class CardViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView eventTitle;
        TextView eventTime;
        ImageView speakerPhoto;
        View view;


        CardViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            cv = (CardView) itemView.findViewById(R.id.cv);
            eventTitle = (TextView) itemView.findViewById(R.id.card_title);
            eventTime = (TextView) itemView.findViewById(R.id.card_text);
            speakerPhoto = (ImageView) itemView.findViewById(R.id.card_photo);
        }
    }

    private List<Event> cards;
    private Context context;
    private ScheduleFragment fragment;

    RVAdapter(List<Event> cards, Context context, ScheduleFragment fragment) {
        this.cards = cards;
        this.context = context;
        this.fragment = fragment;
    }

    public void setCards(List<Event> cards) {
        this.cards = cards;
    }

    @Override
    public void onBindViewHolder(final CardViewHolder cardViewHolder, final int i) {

        cardViewHolder.eventTitle.setText(cards.get(i).getTitle());
        cardViewHolder.eventTime.setText(cards.get(i).getTime());
        Picasso.with(context).load(cards.get(i).getSpeaker().getPhotoURL()).fit().centerCrop().placeholder(R.drawable.ic_account_circle_black_24dp).transform(new PicassoCircleTransform()).into(cardViewHolder.speakerPhoto);

        if(fragment.isMine){ //this affects events in mySchedule when they are over, so they change color and
        for(int a = 0; a <cards.size(); a++) {
            if (i == a && cards.get(a).isOver()) {
                Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
                animation.setDuration(400); // duration - half a second
                animation.setInterpolator(new DecelerateInterpolator()); // animation slows over time
                animation.setRepeatCount(5); // Repeat animation 5 times
                animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in
                cardViewHolder.view.startAnimation(animation);
                cardViewHolder.view.setBackgroundColor(Color.GRAY);
            }
        }
        } else { //this is to affect events that have ended in main schedule. might not want this.
            for(int a = 0; a <cards.size(); a++) {
                if (i == a && cards.get(a).isOver()) {
                    cardViewHolder.view.setBackgroundColor(Color.GRAY);
                     }
                }
            }

        cardViewHolder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                CharSequence options[] = new CharSequence[] {"View Event", "Remove Event", "Cancel"};

                AlertDialog.Builder builder = new AlertDialog.Builder(cardViewHolder.view.getContext());
                builder.setTitle("Options");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int choice = which;
                        switch(choice) {
                            case 0:
                                fragment.loadEventDetails(cards.get(i));
                                break;
                            case 1:
                                fragment.removeEvent(cards.get(i));
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
                cardViewHolder.view.clearAnimation();
                fragment.loadEventDetails(cards.get(i));
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
