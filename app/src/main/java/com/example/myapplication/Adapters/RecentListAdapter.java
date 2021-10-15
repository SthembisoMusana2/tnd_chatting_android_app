package com.example.myapplication.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Activities.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.chat_protocol.User;

import java.util.ArrayList;
import java.util.Calendar;

public class RecentListAdapter extends RecyclerView.Adapter<RecentListAdapter.ViewHolder> {

    private ArrayList<User> recentUsers;
    private Activity context;
    private String name, email;
    public  RecentListAdapter(Activity context, ArrayList<User> users, String name, String email){
        super();
        this.recentUsers = users;
        this.context = context;
        this.name = name;
        this.email = email;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recent_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            TextView userName = holder.getName();
            TextView messageSnippet = holder.getMessageSnippet();
            ImageView userProfileImage = holder.getUserProfileImage();
            TextView messageTime = holder.getMessageTimeView();
            TextView badgeCount = holder.getBadgeCount();
            CardView badge = holder.getBadge();
            String messageSnippetString;
            holder.setPosition(position);
            User tempUser = this.recentUsers.get(position);
            userName.setText(tempUser.getUserName());

            if(tempUser.getHistory().size() > 0) {
                messageSnippetString = new String(tempUser.getHistory().get(tempUser.getHistory().size()-1).getBody().getTextData());
                if(messageSnippetString.length() > 30){
                    messageSnippet.setText(String.format("%s...", messageSnippetString.substring(0, 30)));
                }else messageSnippet.setText(messageSnippetString);
                messageTime.setText(tempUser.getHistory().get(tempUser.getHistory().size()-1).getMessageTime());

                if(tempUser.getUnreadMessages() > 0) {
                    badge.setVisibility(View.VISIBLE);
                    badgeCount.setText(""+tempUser.getUnreadMessages());
                }
                else
                    badge.setVisibility(View.GONE);

            }
            else {
                messageSnippet.setText(" ");
                messageTime.setText(" ");
            }
            userProfileImage.setImageResource(R.drawable.user_avar);

    }

    @Override
    public int getItemCount() {
        return recentUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView userName;
        private  TextView rec_message, messageTime;
        private ImageView userProfileImage;
        private TextView badgeCount;
        private CardView badge;
        private int position = 0;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.id_rec_user_name);
            rec_message = itemView.findViewById(R.id.id_rec_message_snippet);
            userProfileImage = itemView.findViewById(R.id.id_rec_user_image_2);
            messageTime = itemView.findViewById(R.id.id_rec_message_time);
            badge = itemView.findViewById(R.id.id_badge);
            badgeCount = itemView.findViewById(R.id.id_badge_value);

            itemView.setOnClickListener(view -> {
                Intent a = new Intent(context.getApplicationContext(), MainActivity.class);
                a.putExtra(MainActivity.EXTRA_USER_POSITION, position);
                context.startActivity(a);
            });
        }

        public TextView getName(){return this.userName;}
        public TextView getMessageSnippet(){return this.rec_message;}
        public ImageView getUserProfileImage(){return this.userProfileImage;}
        public void setPosition(int p){this.position = p;}
        public TextView getMessageTimeView(){return this.messageTime;}
        public CardView getBadge(){return this.badge;}
        public TextView getBadgeCount(){return this.badgeCount;}
    }
}
