package com.example.mycontacts;

import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public Adapter mAdapter;
    public static final  int CONTACTLOADER = 0;
    final SwipeDetector swipeDetector = new SwipeDetector();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        ListView listView = findViewById(R.id.list);
        mAdapter = new Adapter(this, null);
        listView.setAdapter(mAdapter);
        listView.setOnTouchListener(swipeDetector);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (swipeDetector.swipeDetected()) {
                    Log.i(String.valueOf(id),"got swipe");
                    if (swipeDetector.getAction() == SwipeDetector.Action.LR) {
                        Button button_remove = (Button) findViewById(R.id.btn_remove);
                        button_remove.setVisibility(View.VISIBLE);
                        Button button_edit = (Button) findViewById(R.id.btn_edit);
                        button_edit.setVisibility(View.GONE);
                    }
                    if (swipeDetector.getAction() == SwipeDetector.Action.RL) {
                        Button button_remove = (Button) findViewById(R.id.btn_remove);
                        button_remove.setVisibility(View.GONE);
                        Button button_edit = (Button) findViewById(R.id.btn_edit);
                        button_edit.setVisibility(View.VISIBLE);
                    }
                }else{
                    Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                    Uri newUri = ContentUris.withAppendedId(Contract.ContactEntry.CONTENT_URI, id);
                    intent.setData(newUri);
                    startActivity(intent);
                }
            }
        });

        // get the loader running
        getLoaderManager().initLoader(CONTACTLOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {Contract.ContactEntry._ID,
                Contract.ContactEntry.COLUMN_NAME,
                Contract.ContactEntry.COLUMN_EMAIL,
                Contract.ContactEntry.COLUMN_PICTURE,
                Contract.ContactEntry.COLUMN_PHONENUMBER,
                Contract.ContactEntry.COLUMN_TYPEOFCONTACT
        };

        return new CursorLoader(this, Contract.ContactEntry.CONTENT_URI,
                projection, null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mAdapter.swapCursor(null);

    }
}