package com.example.mysportfriends_school_project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

public class FragmentHowToUse extends Fragment {
    private ViewPager viewPager;
    private CardPageAdapter cardPageAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_how_to_use, container, false);

        viewPager = view.findViewById(R.id.viewPager);

        List<CardItem> cardItems = createCardItems();
        cardPageAdapter = new CardPageAdapter(cardItems);
        viewPager.setAdapter(cardPageAdapter);

        return view;
    }
    // set the card itmems
    private List<CardItem> createCardItems() {
        List<CardItem> cardItems = new ArrayList<>();
        cardItems.add(new CardItem("שלב 0 - בחירה מתפריט", "לאחר ההרשמה תבחרו מהתפריט לפי עניינכם."));
        cardItems.add(new CardItem("שלב 1 - יצירת פעולת ספורט", "בחרתם ליצור פעולת ספורט חדשה,ועכשיו עליכם לבחור קטגוריה, את זה תעשו על ידי לחיצה על התמונה שאתם רוצים."));
        cardItems.add(new CardItem("שלב 2 - הכנסת פרטי הפעולה", "כאן עליכם למלא את פרטי פעולתכם. תמלאו את השדות שליפנכים כפי שנראה לכם נכון, בחרו מיקום בעזרת לחיצה על המיקום במפה ותאשרו. "));
        cardItems.add(new CardItem(" שלב 3 - יצירת פעולה", "סיימתם למלא את פרטי הפעולה שלכם, מעולה, עכשיו תלחצו על כפתור יצירת הפעולה, תוודאו שמילאתם לפי רצונכם, ואשרו או בטלו ובצעו עוד שינוים."));
        cardItems.add(new CardItem("שלב 4 - צפו בפעולות שלכם", "פתחו את תפריט האפליצקיה, ובחרו בצפייה בפעולותכם."));
        cardItems.add(new CardItem("שלב 5 - שתפו את הפעולות שלכם", "אחרי שהגעתם לדף הפעולות שלכם, לחתצו על הפעולה שאותה אתם רוצים לשתף לחיצה כפולה,שתפו בעזרת שליחת sms לאנשי קשר שתחברו."));

        return cardItems;
    }
}