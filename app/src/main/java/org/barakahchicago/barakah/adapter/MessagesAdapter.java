package org.barakahchicago.barakah.adapter;

import java.util.ArrayList;

import org.barakahchicago.barakah.util.DateUtil;
import org.barakahchicago.barakah.ui.MessagesFragment.OnMessageSelectedListener;
import org.barakahchicago.barakah.R;
import org.barakahchicago.barakah.model.Message;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class MessagesAdapter extends
        RecyclerView.Adapter<MessagesAdapter.ViewHolder> {

    /*
        Tag used for logging
    */
    private static final String LOG_TAG = "MESSAGE ADAPTER";

    /*
       Callback used to handle whe user selects an message
   */
    OnMessageSelectedListener callback;

    /*
      List of messages used with the adapter
   */
    private ArrayList<Message> messages = new ArrayList<Message>();

    /*
        Constructor, initializes a new MessagesAdapter with list of messages and callback object
     */
    public MessagesAdapter(ArrayList<Message> messages, OnMessageSelectedListener callback) {
        this.messages = messages;
        this.callback = callback;
    }

    public OnMessageSelectedListener getCallback() {
        return callback;
    }


    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    @Override
    public int getItemCount() {

        return messages.size();
    }

    /*
        Binds individual views with data.
     */
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        if (messages.size() > 0) {
            viewHolder.messageTitle.setText(messages.get(position).getTitle());
            viewHolder.messageBody.setText(messages.get(position).getMessage());
            viewHolder.messageDate.setText(DateUtil.getFormattedDate(messages.get(position).getDate_created()));

            viewHolder.position = position;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.message_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(v, parent.getContext());

        return viewHolder;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements
            OnClickListener {
        public int position;
        TextView messageTitle;
        TextView messageDate;
        TextView messageBody;
        ImageView messageImage;

        public ViewHolder(View itemView, Context context) {
            super(itemView);

            messageTitle = (TextView) itemView.findViewById(R.id.message_title);
            messageBody = (TextView) itemView.findViewById(R.id.message_body);
            messageDate = (TextView) itemView.findViewById(R.id.message_date);

            messageImage = (ImageView) itemView
                    .findViewById(R.id.message_image);

            // setting the context of the MainActivity as callback listener to
            // change fragments
            callback = (OnMessageSelectedListener) context;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View arg0) {

            callback.onClick(messages.get(position));
        }
    }
}

