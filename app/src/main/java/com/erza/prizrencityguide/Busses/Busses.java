package com.erza.prizrencityguide.Busses;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.erza.prizrencityguide.R;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.internal.ViewToClickToExpand;
import it.gmariotti.cardslib.library.prototypes.CardSection;
import it.gmariotti.cardslib.library.view.CardListView;

public class Busses extends AppCompatActivity implements AsyncResponse {
    private ArrayList<BussesDB> productList;
    private ListView lvProduct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.busses_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);

        Glide.with(this).load(R.drawable.busses_map).into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Busses.this, BussesMap.class);
                startActivity(i);
            }
        });

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(Busses.this, MapsActivity.class);
//                startActivity(i);
//            }
//        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        PostResponseAsyncTask taskRead = new PostResponseAsyncTask(Busses.this, this);
        taskRead.execute("http://www.regjisori.com/pcg/Busses/busses.php");
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return  true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }


    @Override
    public void processFinish(String s) {
        productList = new JsonConverter<BussesDB>().toArrayList(s, BussesDB.class);

        int listImages[] = new int[]{R.drawable.bus_icon};
        ArrayList<Card> cards = new ArrayList<Card>();
        for (int i = 0; i<productList.size(); i++) {
            // Create a Card
            Card card = new Card(this);
            // Create a CardHeader
            CardHeader header = new CardHeader(this);
            // Add Header to card
            header.setTitle(productList.get(i).linja);

            card.setTitle("\nRoute "+(i+1)+ "\nBus Fare: 0.50 €");
            card.addCardHeader(header);
            CardThumbnail thumb = new CardThumbnail(this);
            thumb.setDrawableResource(listImages[0]);
            card.addCardThumbnail(thumb);
            cards.add(card);
            card.setClickable(true);
            card.setOnClickListener(new Card.OnCardClickListener() {
                @Override
                public void onClick(Card card, View view) {
                    Intent intent = new Intent(Busses.this,Routes.class);
                    intent.putExtra("route", card.getTitle());
                    startActivity(intent);
                }
            });

        }

        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(this, cards);
        CardListView listView = (CardListView) this.findViewById(R.id.cardListView);
        if (listView != null) {
            listView.setAdapter(mCardArrayAdapter);

    }



}}