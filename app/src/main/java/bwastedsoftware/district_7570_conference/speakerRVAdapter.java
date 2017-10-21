package bwastedsoftware.district_7570_conference;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class speakerRVAdapter extends RecyclerView.Adapter<speakerRVAdapter.EventViewHolder>{


    public static class EventViewHolder extends RecyclerView.ViewHolder
    {
        CardView cv;
        TextView speakerName;
        ImageView speakerPhoto;

        EventViewHolder(View itemView)
        {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            speakerName = (TextView) itemView.findViewById(R.id.speaker_name);
            speakerPhoto = (ImageView) itemView.findViewById(R.id.speaker_photo);
        }
    }

    private List<Speaker> speakers;

    speakerRVAdapter(List<Speaker> events)
    {
        this.speakers = speakers;
    }


    @Override
    public void onBindViewHolder(EventViewHolder eventViewHolder, int i) {
        eventViewHolder.speakerName.setText(speakers.get(i).getName());
        //eventViewHolder.speakerPhoto.setImageResource(speakers.get(i).getSpeaker().getPhoto());
    }

    @Override
    public int getItemCount()
    {
        return speakers.size();
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
        int size = this.speakers.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.speakers.remove(0);
            }

            this.notifyItemRangeRemoved(0, size);
        }
    }

}

