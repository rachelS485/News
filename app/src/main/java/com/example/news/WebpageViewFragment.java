/*
 * @author: Rachel Stinnett
 * @file: WebpageViewFragment.java
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
 * this WebpageViewFragment.java file the webview is updated programmatically
 * based on the url. This program does this by getting the url saved in the
 * bundle from the previous fragment. Then the .loadUrl method is used
 * to update the Ui to display the proper website.
 */
package com.example.news;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;


public class WebpageViewFragment extends Fragment {
    private Activity containerActivity;
    private String webUrl;
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
     * saves those a class variable to use in the onCreateView()
     * method.
     * @param savedInstanceState = A Bundle object used to
     * re-create the activity so that prior information is not
     * lost.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            webUrl = getArguments().getString("websiteUrl");
        }
    }

    /**
     * The purpose of this method is to be called to have the
     * fragment instantiate its user interface view. This method
     * first inflates the view which is an important aspect apart
     * of all onCreateView() methods in fragments. This is where it
     * accesses the WebView to then load the url using the .loadUrl()
     * method.
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
        View view = inflater.inflate(R.layout.fragment_webpage_view, container, false);
        WebView webView = (WebView)view.findViewById(R.id.mainWebview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(webUrl);
        return view;
    }
}