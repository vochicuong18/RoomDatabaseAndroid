package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> mlisUsers;
    private IClickItem iClickItem;

    public interface IClickItem {
        void updateUser(User user);
        void deleteUser(User user);
    }

    public UserAdapter(IClickItem iClickItem) {
        this.iClickItem = iClickItem;
    }

    public void setData(List<User> list){
        this.mlisUsers =list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user,parent,false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
       final User user = mlisUsers.get(position);
        if(user == null){
            return;
        }
        holder.tvUserName.setText(user.getName());
        holder.tvAddress.setText(user.getAddRess());

        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iClickItem.updateUser(user);
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iClickItem.deleteUser(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mlisUsers != null){
            return mlisUsers.size();
        }
        return 0;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder{

        private TextView tvUserName, tvAddress;
        private Button btnUpdate;
        private Button btnDelete;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUserName = itemView.findViewById(R.id.tv_username);
            tvAddress =  itemView.findViewById(R.id.tv_address);
            btnUpdate =  itemView.findViewById(R.id.btn_update);
            btnDelete =  itemView.findViewById(R.id.btn_delete);
        }
    }
}
