package org.woheller69.lavatories.ui.RecycleList;

import android.content.Context;

import androidx.core.content.res.ResourcesCompat;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.woheller69.lavatories.R;
import org.woheller69.lavatories.database.Lavatory;
import java.util.List;

//**
// * Created by yonjuni on 02.01.17.
// * Adapter for the horizontal listView for course of the day.
// */import java.util.List;

public class LavatoriesAdapter extends RecyclerView.Adapter<LavatoriesAdapter.LavatoryViewHolder> {

    private List<Lavatory> lavatoryList;
    private Context context;
    private int selected = -1;

//Adapter for Lavatories recycler view
    LavatoriesAdapter(List<Lavatory> lavatoryList, Context context) {
        this.context = context;
        this.lavatoryList = lavatoryList;
    }


    @Override
    public LavatoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_lavatory, parent, false);
        return new LavatoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LavatoryViewHolder holder, int position) {

        holder.dist.setText(lavatoryList.get(position).getDistance()+" km");
        SharedPreferences prefManager = PreferenceManager.getDefaultSharedPreferences(context);
        if (prefManager.getBoolean("pref_Debug",false)){
            holder.address.setText((lavatoryList.get(position).getAddress1()).toUpperCase()+"\nOSM_ID: "+lavatoryList.get(position).getUuid()+"\n"+(position+1)+"/"+lavatoryList.size());
        }else{
            holder.address.setText((lavatoryList.get(position).getAddress1()).toUpperCase());
        }
        if (position == selected) holder.itemView.setBackground(ResourcesCompat.getDrawable(context.getResources(),R.drawable.rounded_highlight,null));
        else holder.itemView.setBackground(ResourcesCompat.getDrawable(context.getResources(),R.drawable.rounded_transparent,null));

        if (!lavatoryList.get(position).getOperator().trim().equals("")) {
            holder.operator.setText(lavatoryList.get(position).getOperator().toUpperCase());
            holder.operator.setVisibility(View.VISIBLE);
        }
        else holder.operator.setVisibility(View.GONE);

        if (!lavatoryList.get(position).getOpeningHours().trim().equals("")) {
            holder.openingHours.setText(lavatoryList.get(position).getOpeningHours());
            holder.openingHours.setVisibility(View.VISIBLE);
        }
        else holder.openingHours.setVisibility(View.GONE);

        if (!lavatoryList.get(position).isPaid()) holder.paid.setImageIcon(null); else holder.paid.setImageResource(R.drawable.ic_paid_black_24dp);
        if (!lavatoryList.get(position).isChemicalToilet()) holder.chemicalToilet.setImageIcon(null); else holder.chemicalToilet.setImageResource(R.drawable.ic_chemical_toilet_24dp);
        if (!lavatoryList.get(position).isWaterPoint()) holder.waterPoint.setImageIcon(null); else holder.waterPoint.setImageResource(R.drawable.ic_water_point_24dp);
        if (!lavatoryList.get(position).isGreyWater()) holder.greyWater.setImageIcon(null); else holder.greyWater.setImageResource(R.drawable.ic_dumpstation_24dp);
    }

    @Override
    public int getItemCount() {
        return lavatoryList.size();
    }

    public void setSelected(int position) {
        int oldSelected = selected;
        selected = position;
        notifyItemChanged(oldSelected);
        notifyItemChanged(selected);
    }

    public int getPosUUID(String id) {

        for (int i=0;i<lavatoryList.size();i++){
            if (lavatoryList.get(i).getUuid().equals(id)) return i;
        }
        return 0;
    }

    class LavatoryViewHolder extends RecyclerView.ViewHolder {

        TextView operator;
        TextView openingHours;
        TextView dist;
        TextView address;
        ImageView greyWater;
        ImageView chemicalToilet;
        ImageView waterPoint;
        ImageView paid;

        LavatoryViewHolder(View itemView) {
            super(itemView);

            openingHours = itemView.findViewById(R.id.lavatory_hours);
            operator = itemView.findViewById(R.id.lavatory_operator);
            dist = itemView.findViewById(R.id.lavatory_dist);
            address = itemView.findViewById(R.id.lavatory_address);
            greyWater = itemView.findViewById(R.id.lavatory_grey_water);
            chemicalToilet = itemView.findViewById(R.id.lavatory_chemical_toilet);
            waterPoint = itemView.findViewById(R.id.lavatory_water_point);
            paid = itemView.findViewById(R.id.lavatory_paid);
        }
    }
}

