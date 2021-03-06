package com.erza.prizrencityguide.Monuments;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;
import com.amigold.fundapter.interfaces.DynamicImageLoader;
import com.erza.prizrencityguide.R;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Monuments extends AppCompatActivity implements AsyncResponse {

    private ArrayList<monumentsdb> productList;
    private ListView monuments;
    private Button ReadMore_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.monuments_activity);

        /** ReadMore_button = (Button)findViewById(R.id.readmore_button);
        ReadMore_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Monuments.this,ReadMore.class);
                startActivity(intent);
            }
        });
**/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        PostResponseAsyncTask taskRead = new PostResponseAsyncTask(Monuments.this, this );
        taskRead.execute("http://www.regjisori.com/pcg/Monuments/monumentet.php");
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
        productList = new JsonConverter<monumentsdb>().toArrayList(s, monumentsdb.class);
        BindDictionary<monumentsdb> dict = new BindDictionary<monumentsdb>();
        dict.addStringField(R.id.emri_monument, new StringExtractor<monumentsdb>() {
            @Override
            public String getStringValue(monumentsdb monumentsdb, int i) {
                return monumentsdb.emri;
            }
        });

        dict.addStringField(R.id.lokacioni_monument, new StringExtractor<monumentsdb>() {
            @Override
            public String getStringValue(monumentsdb monumentsdb, int i) {
                return "Name of street: " + monumentsdb.lokacioni;
            }
        });
        dict.addDynamicImageField(R.id.imazhi_monument,
                new StringExtractor<monumentsdb>() {
                    @Override
                    public String getStringValue(monumentsdb monumentsdb, int i) {
                        return monumentsdb.imazhi_link;
                    }
                }, new DynamicImageLoader() {
                    @Override
                    public void loadImage(String link, ImageView imageView) {
                        Picasso.with(Monuments.this).load(link).into(imageView);
                    }
                });


        FunDapter<monumentsdb> adapter = new FunDapter<>(Monuments.this, productList, R.layout.monuments_layout_list,dict);
        monuments = (ListView)findViewById(R.id.Product_monuments);
        monuments.setAdapter(adapter);

    }
}
