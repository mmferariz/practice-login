package com.mferariz.myapplication.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.mferariz.myapplication.UserActivity;
import com.mferariz.myapplication.R;
import com.mferariz.myapplication.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.viewHolderUser> {

    private List<User> allUsers;
    private List<User> filteredUsers;
    private Context context;

    public UserAdapter(Context context) {
        this.allUsers = new ArrayList<>();
        this.filteredUsers = new ArrayList<>();
        this.context = context;
    }

    public void setUsers(List<User> users) {
        this.allUsers = users;
        filteredUsers.addAll(allUsers);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public viewHolderUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_tile, parent, false);

        return new UserAdapter.viewHolderUser(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolderUser holder, int position) {
        User auxUser = filteredUsers.get(position);
        holder.txtId.setText("ID: " + auxUser.getId());
        holder.txtName.setText(auxUser.getFirstName() + " " + auxUser.getLastName());
        holder.txtEmail.setText(auxUser.getEmail());
        Picasso.get().load(auxUser.getAvatar()).error(R.mipmap.ic_error_avatar).into(holder.avatar);
        holder.card.setOnClickListener(v -> {
            Intent i = new Intent(context, UserActivity.class);
            i.putExtra("id", auxUser.getId());
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return filteredUsers.size();
    }

    public class viewHolderUser extends RecyclerView.ViewHolder {

        protected TextView txtId;
        protected TextView txtName;
        protected TextView txtEmail;
        protected ImageView avatar;
        protected CardView card;

        public viewHolderUser(@NonNull View itemView) {
            super(itemView);
            txtId = (TextView) itemView.findViewById(R.id.txt_id);
            txtName = (TextView) itemView.findViewById(R.id.txt_name);
            txtEmail = (TextView) itemView.findViewById(R.id.txt_email);
            avatar = (ImageView) itemView.findViewById(R.id.avatar);
            card = (CardView) itemView.findViewById(R.id.card);
        }
    }

    public void filter(String query){
        int queryLength = query.length();
        if(queryLength == 0){
            filteredUsers.clear();
            filteredUsers.addAll(allUsers);
        } else {
            List<User> aux = allUsers.stream().filter(u ->
                u.getFirstName().toLowerCase().contains(query.toLowerCase()) ||         // Nombre
                u.getLastName().toLowerCase().contains(query.toLowerCase()) ||          // Apellido
                u.getEmail().toLowerCase().contains(query.toLowerCase()) ||             // Email
                String.valueOf(u.getId()).toLowerCase().contains(query.toLowerCase())   // Id
            ).collect(Collectors.toList());
            filteredUsers.clear();
            filteredUsers.addAll(aux);
        }
        notifyDataSetChanged();
    }
}
