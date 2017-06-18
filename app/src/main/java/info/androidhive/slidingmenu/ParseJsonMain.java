package info.androidhive.slidingmenu;

import android.content.Context;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import info.androidhive.slidingmenu.ImageRecord;

/**
 * Created by ahmed on 17/06/17.
 */

public class ParseJsonMain {

    public ArrayList<ImageRecord> getImageRecords() {
        return imageRecords;
    }

    public final static String KEY_backdropPath = "backdropPath", KEY_original_language = "original_language",
            KEY_original_title = "original_title", KEY_overview = "overview", KEY_popularity = "popularity",
            KEY_poster_path = "poster_path", KEY_release_date = "release_date", KEY_vote_average = "vote_average",
            KEY_vote_count = "vote_count", KEY_run_time = "run_time", KEY_genres = "KEY_genres", KEY_reve = "KEY_reve",
            KEY_budget = "KEY_budget", KEY_production_companies = "KEY_production_companies", KEY_production_countries = "KEY_production_countries",
            KEY_home_page = "KEY_home_page", KEY_spoken_langs = "KEY_spoken_langs", KEY_movie_id = "KEY_movie_id";
    Context context = null;
    ArrayList<ImageRecord> imageRecords = new ArrayList<ImageRecord>();

    public ParseJsonMain(Context context) {
        this.context = context;

    }


    public void parseGetMovieDataViaItsId(String movieId) {

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this.context);

        // Request a string response from the provided URL.
        String url = "http://api.themoviedb.org/3/movie/" + movieId + "?api_key=242d544fe443aa59e56d47a3d5f2d6c4";

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
                            String run_time = jsonObject.getString("run_time");
                            String budget = jsonObject.getString("budget");
                            String vote_average = jsonObject.getString("vote_average");
                            String vote_count = jsonObject.getString("vote_count");
                            String revenue = jsonObject.getString("revenue");
                            ArrayList<String> genreses = new ArrayList<String>();
                            for (int j = 0; j < jsonObject.getJSONArray("genres").length(); j++) {
                                genreses.add(jsonObject.getJSONArray("genres").getJSONObject(j).getString("name"));//drama or action or fantazy
                            }
                            ArrayList<String> production_companies = new ArrayList<String>();
                            for (int j = 0; j < jsonObject.getJSONArray("production_companies").length(); j++) {
                                production_companies.add(jsonObject.getJSONArray("production_companies").getJSONObject(j).getString("name"));
                            }
                            ArrayList<String> production_countries = new ArrayList<String>();
                            for (int j = 0; j < jsonObject.getJSONArray("production_countries").length(); j++) {
                                production_countries.add(jsonObject.getJSONArray("production_countries").getJSONObject(j).getString("name"));
                            }
                            ArrayList<String> spoken_languages = new ArrayList<String>();
                            for (int j = 0; j < jsonObject.getJSONArray("spoken_languages").length(); j++) {
                                spoken_languages.add(jsonObject.getJSONArray("spoken_languages").getJSONObject(j).getString("name"));
                            }


                            HashMap<String, String> hashMap = new HashMap<String, String>();
                            hashMap.put(KEY_original_title, original_title);
                            hashMap.put(KEY_vote_count, vote_count);
                            hashMap.put(KEY_overview, overview);
                            hashMap.put(KEY_backdropPath, backdropPath);
                            hashMap.put(KEY_budget, budget);
                            hashMap.put(KEY_genres, genreses.toString());
                            hashMap.put(KEY_home_page, homepage);
                            hashMap.put(KEY_popularity, popularity);
                            hashMap.put(KEY_poster_path, poster_path);
                            hashMap.put(KEY_original_language, original_language);
                            hashMap.put(KEY_release_date, release_date);
                            hashMap.put(KEY_run_time, run_time);
                            hashMap.put(KEY_vote_average, vote_average);
                            hashMap.put(KEY_reve, revenue);
                            hashMap.put(KEY_production_companies, production_companies.toString());
                            hashMap.put(KEY_production_countries, production_countries.toString());
                            hashMap.put(KEY_spoken_langs, spoken_languages.toString());


//                            imageRecords.add(new ImageRecord(hashMap,
//                                    "https://image.tmdb.org/t/p/w300//" + poster_path,
//                                    original_title + "\n(" + getYearFroReleaseDate(release_date) + ")"));


                        } catch (JSONException e) {
                            e.printStackTrace();
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


    private String getYearFroReleaseDate(String releaseDate) {

        String dateArr[] = releaseDate.split("-");

        String day = dateArr[2];
        String month = dateArr[1];
        String year = dateArr[0];


        return year;
    }


    public ArrayList<String> nowPlayingGetIds(String pageNumber){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this.context);

        final ArrayList<String> ids = new ArrayList<String>();

        String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=242d544fe443aa59e56d47a3d5f2d6c4&page="+pageNumber;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("results");

                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String id = jsonObject.getString("id");
                                ids.add(id);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
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

        return ids;

    }




}
