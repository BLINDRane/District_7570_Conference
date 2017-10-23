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

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.EventViewHolder>{


    public static class EventViewHolder extends RecyclerView.ViewHolder
    {
        CardView cv;
        TextView eventTitle;
        TextView eventTime;
        ImageView speakerPhoto;

        EventViewHolder(View itemView)
        {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            eventTitle = (TextView) itemView.findViewById(R.id.event_title);
            eventTime = (TextView) itemView.findViewById(R.id.event_time);
            speakerPhoto = (ImageView) itemView.findViewById(R.id.speaker_photo);
        }
    }

    private List<Event> events;
    private Context context;

    RVAdapter(List<Event> events, Context context)
    {
        this.events = events;
        this.context = context;
    }


    @Override
    public void onBindViewHolder(EventViewHolder eventViewHolder, int i) {
        eventViewHolder.eventTitle.setText(events.get(i).getTitle());
        eventViewHolder.eventTime.setText(events.get(i).getTime());
        Picasso.with(context).load(events.get(i).getSpeaker().getPhotoURL()).placeholder(R.drawable.ic_account_circle_black_24dp).into(eventViewHolder.speakerPhoto);
    }

    @Override
    public int getItemCount()
    {
        return events.size();
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_item, viewGroup, false);
        EventViewHolder pvh = new EventViewHolder(v);
        return pvh;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void clear() {
        int size = this.events.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.events.remove(0);
            }

            this.notifyItemRangeRemoved(0, size);
        }
    }

}
