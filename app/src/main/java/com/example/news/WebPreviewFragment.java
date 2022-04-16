/*
 * @author: Rachel Stinnett
 * @file: WebPreviewFragment.java
 * @assignment: Programming Assignment 7- News Search
 * @course: CSC 317; Spring 2022
 * @description: This program creates an application that can be used to search
 * for and browse through news articles. When application begins, the user is
 * presented with a simple user interface, including the title of the application,
 * a text field and search button, and then the remaining space can be used to
 * display the search results. The user is able to complete one keyword searches
 * to get articles related to that topic, browse through them, and even view the
 * entire article on the original website. This program only has one Activity
 * because fragments are utilized for majority of the UI functionality. In
 * this WebPreviewFragment.java file is where the website title, website content,
 * and on click method is created programmatically. This method does this by using
 * the onCreate() method to gather information that was placed in the bundle from the
 * previous fragment. Then within the onCreateView the Ui is updated programmatically
 * and the next fragment is called when the read on button is clicked.
 */
package com.example.news;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class WebPreviewFragment extends Fragment {
    private String webContent;
    private String webUrl;
    private String webTitle;
    private Activity containerActivity;

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
     * This method is responsible for gathering the information
     * that was saved into the bundle in the previous fragment.
     * This method does this by using the getArguments().getString()
     * method to gather the key value pairs saved. This method
     * saves those as class variables to use in the onCreateView()
     * method.
     * @param savedInstanceState = A Bundle object used to
     * re-create the activity so that prior information is not
     * lost.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            webContent = getArguments().getString("websiteContent");
            webUrl = getArguments().getString("websiteUrl");
            webTitle =  getArguments().getString("websiteTitle");
        }
    }

    /**
     * The purpose of this method is to be called to have the
     * fragment instantiate its user interface view. This method
     * first inflates the view which is an important aspect apart
     * of all onCreateView() methods in fragments. This method sets
     * the correct content text. Then if the read on button is
     * clicked this method starts the next fragment. And saves
     * the url in a bundle.
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
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_web_preview, container, false);
        TextView titleTextView = view.findViewById(R.id.websiteTitle);
        titleTextView.setText(webTitle);
        TextView webDescripTextView = view.findViewById(R.id.websiteDescrip);
        webDescripTextView.setText(webContent);
        Button button = (Button) view.findViewById(R.id.searchButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                WebpageViewFragment webpageViewFragment = new WebpageViewFragment();
                webpageViewFragment.setContainerActivity(containerActivity);
                Bundle args = new Bundle();
                args.putString("websiteUrl", webUrl);
                webpageViewFragment.setArguments(args);
                getFragmentManager().beginTransaction().
                        replace(R.id.mainLayoutContainerInner,webpageViewFragment).
                        addToBackStack(null).commit();
            }
        });
        return view;
    }
}