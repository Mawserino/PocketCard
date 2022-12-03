package com.example.pocketcard.activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pocketcard.R;
import com.example.pocketcard.model.rvModel;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Myadapter extends RecyclerView.Adapter<Myadapter.MyViewHolder>{

    private ArrayList<rvModel> arrContacts;
    private onContactClickListener mOnContactClickListener;

    public Myadapter(ArrayList<rvModel> arrContactsIn, onContactClickListener on) {
        this.arrContacts = arrContactsIn;
        this.mOnContactClickListener = on;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView userName;
        private TextView userOccupation;
        private TextView userCompanyName;
        onContactClickListener conListener;
        public MyViewHolder(final View view, onContactClickListener conLis){
            super(view);
            userName = view.findViewById(R.id.userName);
            userOccupation = view.findViewById(R.id.userOccupation);
            userCompanyName = view.findViewById(R.id.userCompanyName);

            this.conListener = conLis;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            conListener.onContactClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.qrdata, parent, false);
        return new MyViewHolder(itemView, mOnContactClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String name = arrContacts.get(position).getName();
        String occupation = arrContacts.get(position).getOccupation2();
        String companyname = arrContacts.get(position).getCompanyname2();
        holder.userName.setText(name);
        holder.userOccupation.setText(occupation);
        holder.userCompanyName.setText(companyname);

    }

    @Override
    public int getItemCount() {
        return arrContacts.size();

    }

    public interface onContactClickListener
    {
        void onContactClick(int position);
    }
}

