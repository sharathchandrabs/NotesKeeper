package com.example.surajgdesai.noteskeeper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;

public class MainActivity extends AppCompatActivity implements NotesAdapter.IActivity {

    Button addButton;
    Spinner prioritySpinner;
    EditText editText;
    RealmResults<Note> notes;
    ArrayList<Note> allNotes;
    //DatabaseDataManager dbManager;
    NotesAdapter notesAdapter;
    ListView notesListView;
    Realm realm;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        realm.init(this);
        realm = Realm.getDefaultInstance();
/*        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.deleteRealm(config);
        Realm.setDefaultConfiguration(config);*/

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolBar);



        try {

            setSupportActionBar(myToolbar);
            getSupportActionBar().setTitle(R.string.app_name);
            myToolbar.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));


            //dbManager = new DatabaseDataManager(this);

            addButton = (Button) findViewById(R.id.addButton);
            addButton.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));

            prioritySpinner = (Spinner) findViewById(R.id.spinner);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.priorities, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            prioritySpinner.setAdapter(adapter);
            editText = (EditText) findViewById(R.id.notesEditText);

            notes = realm.where(Note.class).findAllSorted("status", Sort.ASCENDING);
            allNotes = new ArrayList<>(notes);
            notesAdapter = new NotesAdapter(this, R.layout.list_view_layout, notes);
            notesListView = (ListView) findViewById(R.id.notesListView);
            notesListView.setAdapter(notesAdapter);
            notesAdapter.setNotifyOnChange(true);

            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!(prioritySpinner.getSelectedItem().toString().equals(getResources().getStringArray(R.array.priorities)[0]) || editText.getText().toString().equals(""))) {
                        final UUID temp = UUID.randomUUID();
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    try{
                                        Note note = new Note();
                                        note.set_id(temp.toString());
                                        note.setNote(editText.getText().toString());
                                        if(prioritySpinner.getSelectedItem().toString().equals
                                                ("High")){
                                            note.setPriority(prioritySpinner.getSelectedItem().toString());
                                            note.setPrioritystatus(2);
                                            note.setStatus(false);
                                            note.setUpdate_time(new Date());
                                            realm.copyToRealmOrUpdate(note);
                                        }
                                        else if(prioritySpinner.getSelectedItem().toString().equals(
                                                "Medium")){
                                            note.setPriority(prioritySpinner.getSelectedItem().toString());
                                            note.setPrioritystatus(1);
                                            note.setStatus(false);
                                            note.setUpdate_time(new Date());
                                            realm.copyToRealmOrUpdate(note);

                                        }else if(prioritySpinner.getSelectedItem
                                                ().toString().equals("Low")){
                                            note.setPriority(prioritySpinner.getSelectedItem().toString());
                                            note.setPrioritystatus(0);
                                            note.setStatus(false);
                                            note.setUpdate_time(new Date());
                                            realm.copyToRealmOrUpdate(note);

                                        }

                                    }catch (Exception e){
                                        e.printStackTrace();
                                    };

                                }
                            });
                        notesAdapter.notifyDataSetChanged();

                        /*if (dbManager.saveNote(note) > 0) {
                            notes.add(note);
                            notesAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(MainActivity.this, getResources().getString(R.string.InsertFailed), Toast.LENGTH_LONG).show();
                        }*/
                    } else {
                        Toast.makeText(MainActivity.this, getResources().getString(R.string.SelectPriority), Toast.LENGTH_LONG).show();
                    }
                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuitems, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sortByPriority:
//                Collections.sort(notes);
                //notes = realm.where(Note.class).findAll();

                notes = notes.sort("status",Sort.ASCENDING,"prioritystatus", Sort.DESCENDING);
                notesAdapter.notifyDataSetChanged();
                notesAdapter = new NotesAdapter(this, R.layout.list_view_layout, notes);
                notesListView = (ListView) findViewById(R.id.notesListView);
                notesListView.setAdapter(notesAdapter);
                return true;
            case R.id.sortByTime:
//                Collections.sort(notes, new Comparator<Note>() {
//                    @Override
//                    public int compare(Note o1, Note o2) {
//                        return o1.getUpdate_time().compareTo(o2.getUpdate_time());
//                    }
//                });
                notes = notes.sort("status",Sort.ASCENDING,"update_time", Sort.ASCENDING);
                notesAdapter.notifyDataSetChanged();
                notesAdapter = new NotesAdapter(this, R.layout.list_view_layout, notes);
                notesListView = (ListView) findViewById(R.id.notesListView);
                notesListView.setAdapter(notesAdapter);
                return true;
            case R.id.showAll:
                //notes.clear();


                notes = realm.where(Note.class).findAll();
                notesAdapter.notifyDataSetChanged();
                /*for (Note note : dbManager.getAllNotes()) {
                    notes.add(note);
                }*/
                notesAdapter = new NotesAdapter(this, R.layout.list_view_layout, notes);
                notesListView = (ListView) findViewById(R.id.notesListView);
                notesListView.setAdapter(notesAdapter);
                return true;
            case R.id.showCompleted:

                RealmResults<Note> completedNote = realm.where(Note.class).equalTo("status", true)
                        .findAll();
                notes = completedNote;
                notesAdapter.notifyDataSetChanged();
                notesAdapter = new NotesAdapter(this, R.layout.list_view_layout, notes);
                notesListView = (ListView) findViewById(R.id.notesListView);
                notesListView.setAdapter(notesAdapter);
/*                //notes.clear();
                notesAdapter.notifyDataSetChanged();
                List<Note> notesTemp = dbManager.getAllNotes();
                notes = realm.where(Note.class).findAll();
                *//*for (Note note : notesTemp) {
                    if (note.isStatus()) {
                        notes.add(note);
                    }
                }*//*

                notesAdapter = new NotesAdapter(this, R.layout.list_view_layout, notes);
                notesListView = (ListView) findViewById(R.id.notesListView);
                notesListView.setAdapter(notesAdapter);
                return true;*/
                return true;
            case R.id.showPending:
                RealmResults<Note> pendingNotes = realm.where(Note.class).equalTo("status", false)
                        .findAll();
                notes = pendingNotes;
                notesAdapter.notifyDataSetChanged();
                notesAdapter = new NotesAdapter(this, R.layout.list_view_layout, notes);
                notesListView = (ListView) findViewById(R.id.notesListView);
                notesListView.setAdapter(notesAdapter);
/*                notes.clear();
                notesAdapter.notifyDataSetChanged();
                List<Note> notesTemp1 = dbManager.getAllNotes();
                notes = realm.where(Note.class).findAll();
                *//*for (Note note : notesTemp1) {
                    if (!note.isStatus()) {
                        notes.add(note);
                    }
                }*//*

                notesAdapter = new NotesAdapter(this, R.layout.list_view_layout, notes);
                notesListView = (ListView) findViewById(R.id.notesListView);
                notesListView.setAdapter(notesAdapter);
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void updateObject(final int position, boolean isChecked) {
        //notes.get(position).setStatus(isChecked);
        if(isChecked){
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    try{
                        Note note = notes.get(position);
                        note.setStatus(true);
                        realm.copyToRealmOrUpdate(note);
                    }catch (Exception e){
                        e.printStackTrace();
                    };

                }
            });
            notesAdapter.notifyDataSetChanged();
        }
        else{
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    try{
                        Note note = notes.get(position);
                        note.setStatus(false);
                        realm.copyToRealmOrUpdate(note);
                    }catch (Exception e){
                        e.printStackTrace();
                    };

                }
            });
            notesAdapter.notifyDataSetChanged();
        }


        /*if (dbManager.updateNote(notes.get(position))) {
            notesAdapter.notifyDataSetChanged();
        } else {
            notes.clear();
            notesAdapter.notifyDataSetChanged();
            notes = realm.where(Note.class).findAll();
            notesAdapter.notifyDataSetChanged();
            Toast.makeText(MainActivity.this, getResources().getString(R.string.UpdateFailed), Toast.LENGTH_LONG).show();
        }*/
    }

    @Override
    public void deleteObject(final int position) {
        try{
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Note deleteNote = notes.get(position);
                    deleteNote.deleteFromRealm();
                    notesAdapter.notifyDataSetChanged();
                }
            });

        }catch(Exception e){
            Toast.makeText(this, "Could not delete",Toast.LENGTH_LONG).show();
        }
        /*if (dbManager.deleteNote(notes.get(position))) {
            notes.remove(position);
            notesAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(MainActivity.this, getResources().getString(R.string.DeleteFailed), Toast.LENGTH_LONG).show();
        }*/
    }
}
