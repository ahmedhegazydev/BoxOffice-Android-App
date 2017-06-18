package info.androidhive.slidingmenu.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;


import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.poliveira.apps.materialtests.DetailsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import info.androidhive.slidingmenu.ImageRecord;

import info.androidhive.slidingmenu.R;
import info.androidhive.slidingmenu.adapter.ImageRecordsAdapter;


public class NowPlayingFragment extends Fragment implements AdapterView.OnItemClickListener {

    View rootView = null;
    Context context = null;
    ListView lvMovies = null;
    LinearLayout llButtonPages = null;
    ViewFlipper vfMovies = null;
    ImageRecordsAdapter imageRecordsApapter = null;

    RequestQueue queue = null;
    ArrayList<Button> buttons = new ArrayList<Button>();
    ArrayList<ImageRecord> imageRecords = new ArrayList<ImageRecord>();
    String constUrl = "https://api.themoviedb.org/3/movie/now_playing?api_key=242d544fe443aa59e56d47a3d5f2d6c4&page=";
    String pageNumber = "1";
    String backdropPath, original_language, original_title, overview, popularity,
            poster_path, release_date = "", vote_average, vote_count, run_time = "", genre = "", id = "";


    String url = constUrl + pageNumber;

    JSONObject jsonObject = null;
    JSONArray jsonArray = null;
    HorizontalScrollView horizontalScrollView = null;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //the framelayout
        rootView = inflater.inflate(R.layout.fragment_now_playing, container, false);

        this.context = container.getContext();///initialize context for future usage

        try {
            init();
        } catch (Exception e) {
            createToast(e.getMessage().toString());
        }

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        // Implementing ActionBar Search inside a fragment
        MenuItem item = menu.add("Search");
        item.setIcon(android.R.drawable.ic_menu_search); // sets icon
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        SearchView sv = new SearchView(getActivity());

        // modifying the text inside edittext component
        int id = sv.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) sv.findViewById(id);
        textView.setHint("Title/Region/Year/Language/");
        textView.setHintTextColor(getResources().getColor(R.color.DarkGray));
        textView.setTextColor(getResources().getColor(R.color.clouds));

        // implementing the listener
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchForMovie(s);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        item.setActionView(sv);


    }


    private void searchForMovie(String s) {

        url = "https://api.themoviedb.org/3/search/movie?api_key=242d544fe443aa59e56d47a3d5f2d6c4&query=" + s +
                "&language=" + s + "&year=" + s + "&region=" + s;
        imageRecords.clear();
        new AsyncGetData(context).execute();


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }


    private void init() {
        View view = rootView.findViewById(R.id.llNowPlaying);

        //getting the view flipper for setting the movies in cute way with animation
        vfMovies = (ViewFlipper) view.findViewById(R.id.vfNowPlaying);

        //getting layout for adding the button pages of movies as we have alot alot alot of movies
        horizontalScrollView = (HorizontalScrollView) view.findViewById(R.id.hsvNoewPlaying);

        llButtonPages = (LinearLayout) view.findViewById(R.id.llPagesNowPlaying);

        //adding some of button pages about 6 pages but there are alot in our case that is enough
        //when the user click on button that have page 1 , the app will get the movies by url with page 1 and so on
        LinearLayout.LayoutParams params = null;
        Button btnPage = null;
        for (int i = 0; i < 6; i++) {


            btnPage = new Button(context);
            btnPage.setPadding(1, 1, 1, 1);
            btnPage.setText(String.valueOf(i + 1));
            btnPage.setTypeface(Typeface.DEFAULT_BOLD);
            final int finalI = i;

            btnPage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button button = (Button) v;
                    //changePageOfMovies(button.getText().toString());
                    for (int i = 0; i < buttons.size(); i++) {
                        buttons.get(i).setBackgroundResource(R.drawable.rounded_button);//without color
                    }

                    button.setBackgroundResource(R.drawable.rounded_btn_with_color);//to be like as selected
                    imageRecords.clear();
                    url = constUrl + button.getText().toString();
                    new AsyncGetData(context).execute();

                }
            });
            btnPage.setBackgroundResource(R.drawable.rounded_button);

            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(1, 1, 1, 1);
            btnPage.setLayoutParams(params);

            buttons.add(btnPage);

            buttons.get(0).setBackgroundResource(R.drawable.rounded_btn_with_color);//to be like as selected

            ////adding the button to llButPages LinearLayout
            llButtonPages.addView(btnPage);

        }

        ///////////////////////////////////////
        //add the movies of page 1 as default to the view flipper
        //when the user enters

        new AsyncGetData(context).execute();//fetching json data

        //setting fetched data into listview with custom adapter
        lvMovies = new ListView(context);
        imageRecordsApapter = new ImageRecordsAdapter(context, R.layout.movie_item);//adapter for future listview movies
        lvMovies.setOnItemClickListener(this);

        //adding listview to view flipper
        vfMovies.addView(lvMovies, 0);



    }

    public class AsyncGetData extends AsyncTask<Void, Void, ArrayList<ImageRecord>> {

        ProgressDialog progressDialog = null;

        public AsyncGetData(Context context) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Please wait ....");
            progressDialog.setCancelable(false);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }

        @Override
        protected ArrayList<ImageRecord> doInBackground(Void... params) {

            parse(url);

            return null;
//            return parse(constUrl + pageNumber);
        }


        @Override
        protected void onPostExecute(ArrayList<ImageRecord> imageRecords) {
            super.onPostExecute(imageRecords);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
//                lvMovies.setAdapter(imageRecordsApapter);
//                imageRecordsApapter.swapImageRecords(imageRecords);
            }

        }
    }




    private void parse(String url) {


        // Instantiate the RequestQueue.
        queue = Volley.newRequestQueue(context);

        //http://www.omdbapi.com/ //not for free
//        http://www.theimdbapi.org/#movie
        //String url = "https://api.themoviedb.org/3/movie/550?api_key=242d544fe443aa59e56d47a3d5f2d6c4";
        //https://image.tmdb.org/t/p/w500//1npYV66ISVzBi1YbfMooPP6R4Q4.jpg
//        String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=242d544fe443aa59e56d47a3d5f2d6c4&page=1";

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            jsonArray = new JSONArray(response.getJSONArray("results").toString());

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                original_title = jsonObject.getString("original_title");
                                poster_path = jsonObject.getString("poster_path");//not completed
                                release_date = jsonObject.getString("release_date");
                                id = jsonObject.getString("id");

                                imageRecords.add(new ImageRecord(id,
                                        "https://image.tmdb.org/t/p/w300//" + poster_path,
                                        original_title
//                                                +"\n("+ getYearFroReleaseDate(release_date)+")"
                                ));
                            }

                            lvMovies.setAdapter(imageRecordsApapter);
                            imageRecordsApapter.swapImageRecords(imageRecords);

                            //Log.d("result", imageRecords.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        createToast("get data : " + error.getMessage().toString());
//                        creatSnackBar("No internet connection", Snackbar.LENGTH_LONG);
                    }
                }
        );

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);

    }

    private String getYearFroReleaseDate(String releaseDate) {

        String year = "", day = "", month = "";
        if (releaseDate != "" && releaseDate != null) {
            String dateArr[] = releaseDate.split("-");

            day = dateArr[2];
            month = dateArr[1];
            year = dateArr[0];
        }

        return year;
    }


    private void createToast(String original_title, int lengthShort) {
        Toast.makeText(context, original_title, lengthShort).show();
    }


    private void createToast(String s) {
        Toast.makeText(context, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        Intent intent = new Intent(getActivity(), DetailsActivity.class);

        intent.putExtra("id", view.getTag().toString());



        startActivity(intent);


    }


}
