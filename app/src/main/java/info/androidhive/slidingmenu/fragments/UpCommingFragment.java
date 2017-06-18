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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
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

import java.util.ArrayList;
import java.util.HashMap;

import info.androidhive.slidingmenu.ImageRecord;
import info.androidhive.slidingmenu.R;
import info.androidhive.slidingmenu.adapter.ImageRecordsAdapter;


/**
 * Created by ahmed on 16/06/17.
 */


public class UpCommingFragment extends Fragment implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener {


    View rootView = null;
    Context context = null;
    ListView lvMovies = null;
    LinearLayout llButtonPages = null;
    ViewFlipper vfMovies = null;
    ImageRecordsAdapter imageRecordsApapter = null;
    RequestQueue queue = null;
    ArrayList<Button> buttons = new ArrayList<Button>();
    ArrayList<ImageRecord> imageRecords = new ArrayList<ImageRecord>();
    String pageNumber = "1";
    String url =
            "http://api.themoviedb.org/3/movie/upcoming?api_key=242d544fe443aa59e56d47a3d5f2d6c4&page=" + pageNumber;


    String backdropPath, original_language, original_title, overview, popularity,
            poster_path, release_date = "", vote_average, vote_count, run_time = "", genre = "", id = "";

    public final static String KEY_backdropPath = "backdropPath", KEY_original_language = "original_language",
            KEY_original_title = "original_title", KEY_overview = "overview", KEY_popularity = "popularity",
            KEY_poster_path = "poster_path", KEY_release_date = "release_date", KEY_vote_average = "vote_average",
            KEY_vote_count = "vote_count", KEY_run_time = "run_time", KEY_genres = "KEY_genres";


    JSONObject jsonObject = null;
    JSONArray jsonArray = null;
    private int preLast;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //the framelayout
        rootView = inflater.inflate(R.layout.popular, container, false);

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


        lvMovies = (ListView) rootView.findViewById(R.id.lvPopular);
        lvMovies.setOnScrollListener(this);
        imageRecordsApapter = new ImageRecordsAdapter(context, R.layout.movie_item);
        lvMovies.setAdapter(imageRecordsApapter);
        new AsyncGetData(context).execute();
        imageRecordsApapter.swapImageRecords(imageRecords);


    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    boolean toggle = true;
    @Override
    public void onScroll(AbsListView lw, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if (firstVisibleItem == 0) {
            // check if we reached the top or bottom of the list
            View v = lw.getChildAt(0);
            int offset = (v == null) ? 0 : v.getTop();
            if (offset == 0) {
                // reached the top:
                //createToast("top");
                return;
            }
        } else if (totalItemCount - visibleItemCount == firstVisibleItem) {
            View v = lw.getChildAt(totalItemCount - 1);
            int offset = (v == null) ? 0 : v.getTop();
            if (offset == 0 && toggle) {
                // reached the bottom
                // createToast("bottom");
                toggle = false;
                loadMoreMovies();

            }
        }
    }

    private void loadMoreMovies() {


        pageNumber = (Integer.parseInt(pageNumber)+1)+"";
        url = "https://api.themoviedb.org/3/movie/popular?api_key=242d544fe443aa59e56d47a3d5f2d6c4&language=en-US&page="+pageNumber;
        new AsyncGetData(context).execute();
        createToast("load more movies ");
        toggle = true;
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

    private void creatSnackBar(String s, int lengthLong) {
        Snackbar.make(rootView, s, lengthLong).show();
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
                        //creatSnackBar("No internet connection", Snackbar.LENGTH_LONG);
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
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        Intent intent = new Intent(getActivity(), DetailsActivity.class);
        intent.putExtra("map", view.getTag().toString());


        startActivity(intent);


    }


}
