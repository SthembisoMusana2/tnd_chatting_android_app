package com.example.myapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.Misc.UsersModel;

import java.util.ArrayList;

public class AddRecipientAdapter extends RecyclerView.Adapter<AddRecipientAdapter.ViewHolder> {

    // The adapter contains the data to be inflated in the list
    private Context ctx;
    private ArrayList<UsersModel> users;
    public AddRecipientAdapter(Context context, ArrayList<UsersModel> data){
        ctx = context;
        this.users = data;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recent_user, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Do something when we click the object here
                Toast toast = new Toast(view.getContext());
                toast.setDuration(Toast.LENGTH_LONG);
                TextView txt = (TextView) view.findViewById(R.id.id_rec_name);
                toast.setText("You Clicked the object "+txt.getText().toString());
                toast.show();
            }
        });
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TextView textView = (TextView) holder.getTextView();
        ImageView imageView = (ImageView) holder.getImageView();
        UsersModel currentUser = users.get(position);

        textView.setText(currentUser.getName());
        imageView.setImageResource(currentUser.getImageRef());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView textView;
        private final ImageView imgView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.id_rec_user_name);
            imgView = (ImageView) itemView.findViewById(R.id.id_rec_user_image_2);
        }

        public View getTextView(){
            return textView;
        }
        public View getImageView(){
            return imgView;
        }

    }
}
