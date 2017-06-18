package com.poliveira.apps.materialtests;


import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import info.androidhive.slidingmenu.R;



import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.annotation.IntegerRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import info.androidhive.slidingmenu.R;
import info.androidhive.slidingmenu.model.BitmapLruCache;

import static android.widget.ImageView.ScaleType.CENTER_CROP;

public class DetailsActivity extends Activity {


    Intent intent = null;
    String movieId = "";
    Context context = null;
    public final static String KEY_backdropPath = "backdropPath", KEY_original_language = "original_language",
            KEY_original_title = "original_title", KEY_overview = "overview", KEY_popularity = "popularity",
                        KEY_poster_path = "poster_path", KEY_release_date = "release_date", KEY_vote_average = "vote_average",
                        KEY_vote_count = "vote_count", KEY_run_time = "run_time", KEY_genres = "KEY_genres", KEY_reve = "KEY_reve",
                        KEY_budget = "KEY_budget", KEY_production_companies = "KEY_production_companies", KEY_production_countries = "KEY_production_countries",
                        KEY_home_page = "KEY_home_page", KEY_spoken_langs = "KEY_spoken_langs", KEY_movie_id = "KEY_movie_id";
    HashMap<String, String> hashMap = new HashMap<String, String>();






    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        init();
    }

    private void init() {

        intent = getIntent();
        movieId = intent.getStringExtra("id");
        context = this;




        //createToast(movieId, Toast.LENGTH_LONG);
//         Log.i("id",movieId);//wonder woman = 297762

        new AsyncGetDataViaId(context).execute();
        //parseGetMovieDataViaItsId(movieId);



        new AsyncGetActors(context).execute();






    }


    public class AsyncGetActors extends AsyncTask{

        Context context = null;
        ProgressDialog progressDialog = null;


        public AsyncGetActors(Context context){
            this.context = context;
            this.progressDialog = new ProgressDialog(this.context);
            progressDialog.setMessage("Getting All Casts");
            progressDialog.setCancelable(false);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (!progressDialog.isShowing()){
                progressDialog.show();
            }


        }

        @Override
        protected Object doInBackground(Object[] params) {
            gettingActorsOfMovieViaId();

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            if (progressDialog.isShowing()){
                progressDialog.dismiss();
            }

        }



    }




    private void gettingActorsOfMovieViaId() {

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this.context);

        // Request a string response from the provided URL.
        String url = " http://api.themoviedb.org/3/movie/"+movieId+"/casts?api_key=242d544fe443aa59e56d47a3d5f2d6c4";


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            // cast json array for getting all actors info
                            JSONArray jsonArray = response.getJSONArray("cast");
                            ArrayList<HashMap<String, String>> hashMaps = new ArrayList<HashMap<String, String>>();

                            for (int i = 0; i < jsonArray.length(); i++){

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                HashMap<String, String> hashMap = new HashMap<String, String>();
                                hashMap.put("character", jsonObject.getString("character"));
                                hashMap.put("gender", jsonObject.getString("gender"));
                                hashMap.put("name", jsonObject.getString("name"));
                                hashMap.put("profile_path", jsonObject.getString("profile_path"));

                                hashMaps.add(hashMap);

                            }

                            //Log.d("actors", hashMaps.toString());

                            LinearLayout llMovieActors = null;
                            llMovieActors = (LinearLayout) findViewById(R.id.llActors);


                            LinearLayout.LayoutParams p = new
                                    LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT);

                            LinearLayout.LayoutParams p2 = new
                                    LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                            p2.setMargins(1,1,1,1);

                            for (int i = 0; i < hashMaps.size(); i++){

                                LinearLayout linearLayout = new LinearLayout(context);
                                linearLayout.setOrientation(LinearLayout.VERTICAL);
                                linearLayout.setBackgroundResource(R.drawable.rounded_actor);
                                linearLayout.setPadding(7,7,7,7);
                                linearLayout.setLayoutParams(p2);

                                ImageLoader imageLoader = new
                                        ImageLoader(Volley.newRequestQueue(context), new BitmapLruCache());

                                NetworkImageView networkImageView = new NetworkImageView(context);
                                networkImageView.setScaleType(CENTER_CROP);
                                networkImageView.setPadding(5,5,5,5);




                                TextView tvName = new TextView(context);
                                tvName.setText(hashMaps.get(i).get("name"));
                                tvName.setTypeface(Typeface.DEFAULT_BOLD);
                                tvName.setLayoutParams(p);
                                tvName.setGravity(Gravity.CENTER);

                                TextView space = new TextView(context);
                                space.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2));
                                space.setBackgroundColor(Color.RED);

                                TextView tvCharacter = new TextView(context);
                                tvCharacter.setText(hashMaps.get(i).get("character"));
                                tvCharacter.setTypeface(Typeface.DEFAULT_BOLD);
                                tvCharacter.setLayoutParams(p);


                                String url = "https://image.tmdb.org/t/p/w92//"+hashMaps.get(i).get("profile_path");
                                networkImageView.setImageUrl(url, imageLoader);

                                linearLayout.addView(networkImageView);
                                linearLayout.addView(tvName);
                                linearLayout.addView(space);
                                linearLayout.addView(tvCharacter);


                                llMovieActors.addView(linearLayout);

                            }
                            //--------------------getting Director Info---------------------------------------
                            JSONArray jsonArray2 = response.getJSONArray("crew");
                            ArrayList<HashMap<String, String>> hashMaps2 = new ArrayList<HashMap<String, String>>();
                            for (int i = 0; i < jsonArray2.length(); i++){

                                JSONObject jsonObject = jsonArray2.getJSONObject(i);
                                HashMap<String, String> hashMap = new HashMap<String, String>();
                                hashMap.put("department", jsonObject.getString("department"));
                                hashMap.put("job", jsonObject.getString("job"));
                                hashMap.put("gender", jsonObject.getString("gender"));
                                hashMap.put("name", jsonObject.getString("name"));
                                hashMap.put("profile_path", jsonObject.getString("profile_path"));

                                hashMaps2.add(hashMap);
                            }

                            LinearLayout llDirector = (LinearLayout) findViewById(R.id.llDirector);

                            LinearLayout linearLayout = new LinearLayout(context);
                            linearLayout.setOrientation(LinearLayout.VERTICAL);
                            linearLayout.setBackgroundResource(R.drawable.rounded_actor);
                            linearLayout.setPadding(7,7,7,7);

                            ImageLoader imageLoader = new
                                    ImageLoader(Volley.newRequestQueue(context), new BitmapLruCache());

                            NetworkImageView networkImageView = new NetworkImageView(context);
                            networkImageView.setScaleType(CENTER_CROP);
                            networkImageView.setPadding(5,5,5,5);
                            String strDirecName = "", strDirecProfilePath = "", strDirecDept = "";
                            for (int i = 0; i <hashMaps2.size(); i++){
                                if (hashMaps2.get(i).get("job").equals("Director")){
                                    strDirecName = hashMaps2.get(i).get("name");
                                    strDirecProfilePath = hashMaps2.get(i).get("profile_path");
                                    strDirecDept = hashMaps2.get(i).get("department");
                                }

                            }

                            TextView tvName = new TextView(context);
                            tvName.setText(strDirecName);
                            tvName.setTypeface(Typeface.DEFAULT_BOLD);
                            tvName.setLayoutParams(p);
                            tvName.setGravity(Gravity.CENTER);

                            TextView space = new TextView(context);
                            space.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2));
                            space.setBackgroundColor(Color.RED);

                            TextView tvCharacter = new TextView(context);
                            tvCharacter.setText(strDirecDept);
                            tvCharacter.setTypeface(Typeface.DEFAULT_BOLD);
                            tvCharacter.setLayoutParams(p);


                            String url = "https://image.tmdb.org/t/p/w92//"+strDirecProfilePath;
                            networkImageView.setImageUrl(url, imageLoader);

                            linearLayout.addView(networkImageView);
                            linearLayout.addView(tvName);
                            linearLayout.addView(space);
                            linearLayout.addView(tvCharacter);


                            llDirector.addView(linearLayout);
                            //-------------------------------------get Producer info----------------------
                            // i can get alot of producers with department="production"






                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

//

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        createToast(error.getMessage().toString(), Toast.LENGTH_LONG);
                    }
                }
        );

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }



    public void parseGetMovieDataViaItsId(String movieId) {

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this.context);

        // Request a string response from the provided URL.
        String url = "http://api.themoviedb.org/3/movie/"+ movieId+
                "?api_key=242d544fe443aa59e56d47a3d5f2d6c4";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            JSONObject jsonObject = response;




                            String backdropPath = jsonObject.getString("backdrop_path");//not completed
                            String homepage = jsonObject.getString("homepage");//complete link
                            String id = jsonObject.getString("id");
                            String original_language = jsonObject.getString("original_language");//en or ja
                            String original_title = jsonObject.getString("original_title");
                            String overview = jsonObject.getString("overview");
                            String popularity = jsonObject.getString("popularity");
                            String poster_path = jsonObject.getString("poster_path");//not completed
                            String release_date = jsonObject.getString("release_date");
                            String run_time = jsonObject.getString("runtime");
                            String budget = jsonObject.getString("budget");
                            String vote_average = jsonObject.getString("vote_average");
                            String vote_count = jsonObject.getString("vote_count");
                            String revenue = jsonObject.getString("revenue");
                            String strGenres = "", strProducComp = "", strProducCount = "";
                            ArrayList<String> genreses = new ArrayList<String>();
                            for (int j = 0; j < jsonObject.getJSONArray("genres").length(); j++) {
                                genreses.add(jsonObject.getJSONArray("genres").getJSONObject(j).getString("name"));//drama or action or fantazy
                                strGenres += genreses.get(j).toString();
                                if (j != jsonObject.getJSONArray("genres").length() -1){
                                    strGenres += "-";
                                }

                            }
                            ArrayList<String> production_companies = new ArrayList<String>();
                            for (int j = 0; j < jsonObject.getJSONArray("production_companies").length(); j++) {
                                production_companies.add(jsonObject.getJSONArray("production_companies").getJSONObject(j).getString("name"));
                                strProducComp += production_companies.get(j) + "\n";
                            }
                            ArrayList<String> production_countries = new ArrayList<String>();
                            for (int j = 0; j < jsonObject.getJSONArray("production_countries").length(); j++) {
                                production_countries.add(jsonObject.getJSONArray("production_countries").getJSONObject(j).getString("name"));
                                strProducCount += production_countries.get(j)+"\n";
                            }
                            ArrayList<String> spoken_languages = new ArrayList<String>();
                            for (int j = 0; j < jsonObject.getJSONArray("spoken_languages").length(); j++) {
                                spoken_languages.add(jsonObject.getJSONArray("spoken_languages").getJSONObject(j).getString("name"));
                            }

                            ImageLoader imageLoader = null;
                            imageLoader = new ImageLoader(Volley.newRequestQueue(context), new BitmapLruCache());
                            NetworkImageView networkImageView = (NetworkImageView) findViewById(R.id.ivDetails);
                            //Hi again, these are the the sizes that I know: "w92", "w154", "w185", "w342", "w500", "w780", or "original"; and I think there isn't any other sizes
                            // "original" will give you a very large poster, if you're on mobile "w185" is the best choice
                            networkImageView.setImageUrl("https://image.tmdb.org/t/p/w600//"+backdropPath,
                                    imageLoader);




                            DecimalFormat formatter = new DecimalFormat("###,###");

                            ((TextView)findViewById(R.id.tvDesc)).setText(overview);
                            ((TextView)findViewById(R.id.tvOriginalTitle)).setText(original_title);
                            ((TextView)findViewById(R.id.tvReleaseDate)).setText(release_date);
                            ((TextView)findViewById(R.id.tvRunTime)).setText(run_time+" min");
                            ((TextView)findViewById(R.id.tvGen)).setText(strGenres);
                            ((TextView)findViewById(R.id.tvCountry)).setText(strProducCount);

                            //format-a-string-number-to-have-commas-and-round
                            Double aDouble = Double.parseDouble(budget);
                            ((TextView)findViewById(R.id.tvCostOfProduction)).setText(formatter.format(aDouble)+" USD");

                            ((TextView)findViewById(R.id.tvProductionCmpanies)).setText(strProducComp);

                            //format-a-string-number-to-have-commas-and-round
                            Double integer = Double.parseDouble(revenue);
                            ((TextView)findViewById(R.id.tvRevenues)).setText(formatter.format(integer)+" USD");





                        } catch (JSONException e) {
                            createToast(e.getMessage().toString(), Toast.LENGTH_SHORT);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }


    public class AsyncGetDataViaId extends AsyncTask{

        Context context = null;
        ProgressDialog progressDialog = null;


        public AsyncGetDataViaId(Context context){
            this.context = context;
            progressDialog = new ProgressDialog(this.context);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Please wait .....");

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        if (!progressDialog.isShowing())
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] params) {

            parseGetMovieDataViaItsId(movieId);

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);


            if (progressDialog.isShowing())
                progressDialog.dismiss();


        }

    }


    public void createToast(String msg, int duration) {

        Toast.makeText(getApplicationContext(), msg, duration).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


}
