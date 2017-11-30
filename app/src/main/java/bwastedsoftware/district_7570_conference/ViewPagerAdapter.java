package bwastedsoftware.district_7570_conference;

/**
 * Created by Boston on 10/30/2017.
 */

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class ViewPagerAdapter extends PagerAdapter {
    // Declare Variables
    Context context;
    RecyclerView rv;
    RVAdapter adapter;
    LayoutInflater inflater;
    ScheduleFragment sFrag;
    ArrayList<ArrayList<Event>> days;

    public ViewPagerAdapter(Context context, ArrayList<ArrayList<Event>> days, ScheduleFragment sFrag) {
        this.context = context;
        this.sFrag = sFrag;
        this.days = days;
    }

    @Override
    public int getCount() {
        return days.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    public int getItemPosition(Object object){
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        //it's actually building recycler views full of cards!
        //followed tutorial on creating cards, found here: https://code.tutsplus.com/tutorials/getting-started-with-recyclerview-and-cardview-on-android--cms-23465

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.partial_recycler_view, container,
                false);

        rv = (RecyclerView) view.findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        rv.setLayoutManager(llm);

        ArrayList<Event> events = days.get(position);


            adapter = new RVAdapter(events, context, sFrag);
            rv.setAdapter(adapter);

            //adapter.clear();
            //adapter.setCards(events);
            //adapter.notifyDataSetChanged();

        ((ViewPager) container).addView(view);

        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return days.get(position).get(0).getDate();
    }
}
