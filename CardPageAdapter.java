package com.example.mysportfriends_school_project;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class CardPageAdapter extends PagerAdapter {
    private List<CardItem> cardItems;

    public CardPageAdapter(List<CardItem> cardItems) {
        this.cardItems = cardItems;
    }

    @Override
    public int getCount() {
        return cardItems.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
    // start the item
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View view = inflater.inflate(R.layout.card_view_item, container, false);

        CardItem cardItem = cardItems.get(position);
        TextView titleTextView = view.findViewById(R.id.titleTextView);
        TextView contentTextView = view.findViewById(R.id.contentTextView);

        titleTextView.setText(cardItem.getTitle());
        contentTextView.setText(cardItem.getContent());

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}