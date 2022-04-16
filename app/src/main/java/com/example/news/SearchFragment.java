/*
 * @author: Rachel Stinnett
 * @file: Search Fragment.java
 * @assignment: Programming Assignment 7- News Search
 * @course: CSC 317; Spring 2022
 * @description: This program creates an application that can be used to search
 * for and browse through news articles. When application begins, the user is
 * presented with a simple user interface, including the title of the application,
 * a text field and search button, and then the remaining space can be used to
 * display the search results. The user is able to complete one keyword searches
 * to get articles related to that topic, browse through them, and even view the
 * entire article on the original website. This program only has one Activity
 * because fragments are utilized for majority of the UI functionality. In this
 * SearchFragment.java file is in charge of being the functionality for the
 * search page. This search fragment takes the text from the edit text bar
 * and when the search button is pushed the articles are generated into
 * a list view. This program does this by using an async task to handle
 * the Json downloading. Then this program uses a simple adapter to put
 * the website title and the website author into the list view format.
 * Then when one of the articles is clicked on the next fragment is
 * called using a transaction.
 */
package com.example.news;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SearchFragment extends Fragment {
    private Activity containerActivity;
    private String searchTerm;
    private String startDate;
    private JSONArray articles;
    private ArrayList<String> websiteTitle;
    private ArrayList<String> websiteAuthor;
    private String content;
    private String url;
    private  View view;

    /**
     * The purpose of this method is to create a context of an activity within
     * the fragment to keep track of the container activity from main. This
     * makes the process of completing certain tasks a lot easier when there is
     * a context of the main activity.
     * @param containerActivity = An activity that represents the container
     * activity which is main.
     */
    public void setContainerActivity(Activity containerActivity){
        this.containerActivity = containerActivity;
    }

    /**
     * The purpose of this private class is to be an async task that is
     * responsible for making requests to the news API to get information
     * about news articles.
     */
    private class DownloadJsonTask extends AsyncTask<String, Integer, Long> {
        /**
         * The purpose of this method is to perform a computation on
         * a background thread. This method uses the API to fetch news
         * articles that can be used for the list view. This method
         * does this by getting a url and reading that url to
         * then create a Json object. Then to access the articles this
         * method uses the .getJSONArray() method with the right key
         * to get access to the array of articles. Then this method uses
         * a helper method to organize the article information into
         * seperate websitetitle and websiteauthor arrays to make it easier
         * to separate for the simple adapter later.
         * @param strs = A String that holds multiple strings for this method
         * to work with.
         * @return long = A Long object used for a place holder to follow
         * the syntax of the doInBackground method.
         */
        @Override
        protected Long doInBackground(String... strs) {
            String json = "";
            String line;
           websiteTitle = new ArrayList<>();
           websiteAuthor = new ArrayList<>();
            try {
                URL getUrl = new URL(getString(R.string.urlPartOne) + searchTerm + "&from="+
                        startDate+getString(R.string.urlPartTwo));
                URLConnection myUrl = getUrl.openConnection();
                myUrl.setRequestProperty(getString(R.string.userAgent),
                        getString(R.string.computerRequestProperty));
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(myUrl.getInputStream()));
                //Reads each line to avoid nulls
                while ((line = bufferedReader .readLine()) != null) {
                    json += line;
                }
                bufferedReader .close();
                JSONObject jsonObject = new JSONObject(json);
                articles = jsonObject.getJSONArray("articles");
                organizeArticleInfo(articles);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new Long(0) ;
        }

        /**
         * The purpose of this method is to run the Ui thread after
         * doInBackground() method is finished. This method is
         * responsible for creating the simple adapter in the list view
         * to hold the website title and the website author. This method
         * does this by using a hashmap and a for loop to get the right
         * order. Then the from and to arrays are used to match up the
         * correct string with the right text view. Then this method
         * crates the simple adapter and sets that adapter to the list
         * view within the fragment search xml.
         * @param result = A Long object used for a place holder to follow
         * the syntax of the doInBackground method.
         */
        @Override
        protected void onPostExecute(Long result){
            List<HashMap<String, String>> aList =
                    new ArrayList<HashMap<String, String>>();
            for (int i = 0; i < websiteTitle.size(); i++) {
                HashMap<String, String> hm = new HashMap<String, String>();
                hm.put("website_row_title", websiteTitle.get(i));
                hm.put("website_row_author", websiteAuthor.get(i));
                aList.add(hm);
            }
            String[] from = {"website_row_title", "website_row_author"};
            int[] to = {R.id.firstText, R.id.secondText};
            SimpleAdapter simpleAdapter =
                    new SimpleAdapter(containerActivity.getBaseContext(), aList,
                            R.layout.custom_list_view_layout, from, to);
            ListView websiteListView = getView().findViewById(R.id.listView);
            websiteListView.setAdapter(simpleAdapter);
        }
    }

    /**
     * The purpose of this method is to be a helper for the download json
     * object async task. This method iterates through the JSONArray of articles
     * and finds the article title and the article author and puts it into
     * arrays so that it will be easier to access for the simple adapter.
     * @param newsArtic = A JSONArray of articles within the JSONobject.
     */
    public void organizeArticleInfo(JSONArray newsArtic){
        for(int i = 0; i < newsArtic.length(); i++){
            try {
                JSONObject article = newsArtic.getJSONObject(i);
                JSONObject source = article.getJSONObject("source");
                String publisher = source.getString("name");
                String author = article.getString("author");
                websiteTitle.add(publisher);
                websiteAuthor.add("(" +author+")");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * The purpose of this method is to be called to have the
     * fragment instantiate its user interface view. This method
     * first inflates the view which is an important aspect apart
     * of all onCreateView() methods in fragments. This method
     * is responsible for using an setOnClickListener to know when
     * the search button is clicked to gather the search term from
     * the edit text and then executed the DownloadJsonTask async
     * task. This method also uses the setOnClickListener for
     * the list view to know which website is clicked on in
     * order to start the next web preview fragment correctly.
     * @param inflater = An inflater used to inflate the fragment.
     * @param container = The view container from main activity that can
     *  have elements added to it or replaced.
     * @param savedInstanceState = A Bundle object used to
     * re-create the activity so that prior information is not
     * lost.
     * @return view = A view returned so that the fragment can
     * be correctly placed onto the container.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment_search, container, false);
        Button button = (Button) view.findViewById(R.id.searchButton);
        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v)
            {
                EditText editText = view.findViewById(R.id.editText);
                searchTerm = editText.getText().toString();
                startDate = LocalDate.now().minusDays(7).toString();
                DownloadJsonTask jsonTask = new DownloadJsonTask();
                jsonTask.execute();
            }
        });
        ListView listView = view.findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> list, View v, int pos, long id) {
                listViewOnClickHelper(pos);
                WebPreviewFragment webPreviewFragment = new WebPreviewFragment();
                webPreviewFragment.setContainerActivity(containerActivity);
                Bundle args = new Bundle();
                args.putString("websiteContent", content);
                args.putString("websiteUrl", url);
                args.putString("websiteTitle", websiteTitle.get(pos));
                webPreviewFragment.setArguments(args);
                getFragmentManager().beginTransaction().
                        replace(R.id.mainLayoutContainerInner,webPreviewFragment).
                        addToBackStack(null).commit();
            }
        });
        return view;
    }

    /**
     * The purpose of this method is to be a helper method to
     * gather the correct website information for the web preview
     * fragment. This method does this by iterating through
     * the articles JSONArray and then finding the exact
     * article index to just gather the website content and
     * the website url.
     * @param pos = An integer that represents the index needed
     * to find the exact artcle in the JSONArray.
     */
    public void listViewOnClickHelper(int pos){
        for(int i = 0; i < articles.length(); i++){
            if(i == pos){
                try {
                    JSONObject article = articles.getJSONObject(i);
                    content = article.getString("content");
                    url = article.getString("url");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * This method is used to save the instance of the
     * fragment so that when the user navigates back
     * to this screen the search and the articles are
     * preserved. This method does this by checking
     * the search term and executing the json async
     * task.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onResume() {
        super.onResume();
        EditText editText = view.findViewById(R.id.editText);
        searchTerm = editText.getText().toString();
        if(searchTerm.equals("Enter a Search")){
            return;
        }
        else{
            startDate = LocalDate.now().minusDays(7).toString();
            DownloadJsonTask jsonTask = new DownloadJsonTask();
            jsonTask.execute();
        }
    }

}