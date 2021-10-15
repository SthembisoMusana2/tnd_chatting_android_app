package com.example.myapplication.Adapters;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.chat_protocol.Message;
import com.example.myapplication.chat_protocol.User;

import java.util.ArrayList;

public class ChatBubblesAdapter extends RecyclerView.Adapter<ChatBubblesAdapter.ViewHolder> {

    private ArrayList<Message> chats;
    private Activity context;
    private User user;
    public ChatBubblesAdapter(Activity context, ArrayList<Message> chats, User user){
        this.chats = chats;
        this.context = context;
        this.user = user;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.chat_bubble_2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TextView messageBody = holder.getMessageBody();
        TextView timeStamp = holder.getTimeStamp();
//        TextView userName = holder.getUserNameView();
        LinearLayout messageDiv = holder.getMessageDiv();
        ConstraintLayout messageContainer = (ConstraintLayout) holder.getMessageContainer();
        CardView messageCardContainer = (CardView) holder.getMessageCardContainer();
        ConstraintSet constraintSet = new ConstraintSet();
//        userName.setHeight(0);
        constraintSet.clone(messageContainer);


        messageBody.setText(chats.get(position).getBody().getTextData());
        Message messageItem = chats.get(position);
//        userName.setText(messageItem.getSender());

        if(messageItem.getSender().equals(user.getUserName())){ // I'm the one sending the message
            messageCardContainer.setCardBackgroundColor(0xFF080CE6);// 080CE6
            constraintSet.setHorizontalBias(R.id.id_chat_card_container_sec, 1.0f);
            messageBody.setTextColor(0xFFFFFFFF);
            timeStamp.setTextColor(0xFFFFFFFF);
        }
        else{
            messageCardContainer.setCardBackgroundColor(0xFFA7AFDA);
            constraintSet.setHorizontalBias(R.id.id_chat_card_container_sec, 0f);
        }
        timeStamp.setText(chats.get(position).getMessageTime());
        constraintSet.applyTo(messageContainer);
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView messageBody;
        private final TextView timeStamp;
        private final LinearLayout messageDiv;
        private final TextView userName;
        private final View messageContainer;
        private final CardView messageCardContainer;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            DisplayMetrics displayMetrics = new DisplayMetrics();
            context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int maxWidth = (int)((int)displayMetrics.widthPixels*0.7f);
            messageBody = (TextView) itemView.findViewById(R.id.id_message_container);
            messageBody.setMaxWidth(maxWidth);
            timeStamp = (TextView) itemView.findViewById(R.id.id_chat_container_time);
            messageDiv =  itemView.findViewById(R.id.id_chat_container);
            userName = (TextView) itemView.findViewById(R.id.id_user_name);
            messageContainer = itemView;
            messageCardContainer = itemView.findViewById(R.id.id_chat_card_container_sec);
        }

        public TextView getMessageBody(){
            return messageBody;
        }
        public TextView getTimeStamp(){
            return timeStamp;
        }
        public LinearLayout getMessageDiv(){return this.messageDiv;}
        public TextView getUserNameView(){return this.userName;}
        public View getMessageContainer(){return this.messageContainer;}
        public CardView getMessageCardContainer(){return this.messageCardContainer;}
    }

}
