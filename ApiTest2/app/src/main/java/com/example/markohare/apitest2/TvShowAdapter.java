package com.example.markohare.apitest2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;


public class TvShowAdapter extends RecyclerView.Adapter<TvShowAdapter.MyViewHolder> {

    private Context mContext;
    private List<TvShow> TvShowList;
    private List<TvShow> TvShowListfav;
    private List<TvShowFavourites> TvShowFavouritesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvShowTitle,tvShowPopularity, tvShowOverview;
        public ImageView thumbnail, overflow;

        public MyViewHolder(View view) {
            super(view);
            tvShowTitle = (TextView) view.findViewById(R.id.tvShowTitle);
            tvShowPopularity = (TextView) view.findViewById(R.id.tvShowPopularity);
            tvShowOverview = (TextView) view.findViewById(R.id.tvShowOverview);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }


    public TvShowAdapter(Context mContext, List<TvShow> TvShowList) {
        this.mContext = mContext;
        this.TvShowList = TvShowList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tvshow_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        TvShow tvshow = TvShowList.get(position);
        holder.tvShowTitle.setText(tvshow.getName());
        holder.tvShowPopularity.setText("Popularity rating: "+String.valueOf(tvshow.getPopularity()));
        holder.tvShowOverview.setText(tvshow.getOverview());

        // loading TV show poster  using Glide library
        Glide.with(mContext).load(tvshow.getPosterPath()).into(holder.thumbnail);

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow, position);
            }
        });
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view, int position) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_tvshowadapter, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(position));
        popup.show();

    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        int position;

        public MyMenuItemClickListener(int position) {
            this.position=position;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {

            switch (menuItem.getItemId()) {
                case R.id.add_to_favourites:
                    Toast.makeText(mContext, "Add to Favourites", Toast.LENGTH_SHORT).show();
                    // Write the favourite to the database so it can be retrieved later
                    SQLiteDatabaseHandler db = SQLiteDatabaseHandler.getInstance(mContext);
                    TvShowFavourites fav = new TvShowFavourites();
                    fav.setID(TvShowList.get(position).getId());
                    fav.setTitle(TvShowList.get(position).getName());
                    fav.setPosterPath(TvShowList.get(position).getPosterPath());
                    fav.setOverview(TvShowList.get(position).getOverview());
                    fav.setPopularity(TvShowList.get(position).getPopularity());
                    db.addTvShowFavourites(fav);
                    db.close();
                    return true;
                case R.id.go_to_favourites:
                    Toast.makeText(mContext, "Go to Favourites", Toast.LENGTH_SHORT).show();
                    // Need to use mContext since in menu listener
                    Intent intent = new Intent(mContext, FavouriteActivity.class);
//                    intent.putExtra() ;  // for passing primatives types via an intent - not needed in this case
                    mContext.startActivity(intent);
                    return true;
                default:
            }
            return false;
        }
    }


    @Override
    public int getItemCount() {
        return TvShowList.size();
    }
}