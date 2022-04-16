/*
 * @author: Rachel Stinnett
 * @file: MainActivity.java
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
 * MainActivity.java there is only one method and that is onCreate() which is
 * responsible for setting the content view and creating the first fragment
 * which is the search fragment. This program does not have much because
 * most of the Ui functionality is completed within the fragments.
 */
package com.example.news;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    /**
     * The purpose of this method is to be the place where the
     * activity starts for the java program. The onCreate()
     * method is supposed to initialize the activity. The
     * setContentView is set to R.layout.activity_main. In
     * this onCreate() method the search fragment is created
     * which creates the title, the edit text for the search,
     * the search button, and the list view. This method
     * does this by using the getSupportFragmentManager() and
     * the beginTransaction() method in order to create a
     * transaction so the fragment could begin.
     * @param savedInstanceState = A Bundle object used to
     * re-create the activity so that prior information is not
     * lost.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SearchFragment searchFragment = new SearchFragment();
        searchFragment.setContainerActivity(this);
        //Creates the transaction
        getSupportFragmentManager().beginTransaction().
                add(R.id.mainLayoutContainerInner,searchFragment).
                addToBackStack(null).commit();
    }
}
