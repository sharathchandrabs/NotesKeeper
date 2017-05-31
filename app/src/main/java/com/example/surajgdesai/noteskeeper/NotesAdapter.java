package com.example.surajgdesai.noteskeeper;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.ocpsoft.prettytime.PrettyTime;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

/**
 * Created by Suraj G Desai on 2/27/2017.
 */

public class NotesAdapter extends ArrayAdapter {
    Context cContext;
    int cResource;
    List<Note> cObjects;
    TextView notes;
    CheckBox status;
    TextView priority;
    TextView updatedTime;
    AlertDialog.Builder statusChangedAlertBuilder;
    AlertDialog.Builder statusChangedNotCompletedAlertBuilder;
    AlertDialog.Builder deleteAlertBuilder;

    public NotesAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
        this.cContext = context;
        this.cResource = resource;
        this.cObjects = objects;

    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Note note = (Note) cObjects.get(position);
        ViewHolder viewHolder = null;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) cContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(cResource, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.notes = (TextView) convertView.findViewById(R.id.notesNameTexView);
            viewHolder.status = (CheckBox) convertView.findViewById(R.id.checkBox);
            viewHolder.priority = (TextView) convertView.findViewById(R.id.priorityTexView);
            viewHolder.updatedTime = (TextView) convertView.findViewById(R.id.timeTexView);
            convertView.setTag(viewHolder);
        }

        viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.notes.setTag(position);
        this.notes = viewHolder.notes;
        this.status = viewHolder.status;
        this.priority = viewHolder.priority;
        this.updatedTime = viewHolder.updatedTime;
        this.notes.setText(note.getNote());
        this.status.setChecked(note.isStatus());
        this.priority.setText(note.getPriority());
        viewHolder.status.setTag(position);
        final ViewHolder finalViewHolder = viewHolder;
        if (note.getUpdate_time() != null) {
            PrettyTime time = new PrettyTime();
            String formattedTime = time.format(new Date(note.getUpdate_time().getTime()));
            this.updatedTime.setText(formattedTime);
        }

        this.status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                statusChangedAlertBuilder = new AlertDialog.Builder(cContext);
                statusChangedAlertBuilder.setMessage(cContext.getResources().getString(R.string.markCompleted));
                statusChangedAlertBuilder.setPositiveButton(cContext.getResources().getString(R.string.okAlertDialog), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ((IActivity) cContext).updateObject(Integer.parseInt(finalViewHolder.status.getTag().toString()), isChecked);
                    }
                });
                statusChangedAlertBuilder.setNegativeButton(cContext.getResources().getString(R.string.cancelAlertDialog), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });


                statusChangedNotCompletedAlertBuilder = new AlertDialog.Builder(cContext);
                statusChangedNotCompletedAlertBuilder.setMessage(cContext.getResources().getString(R.string.markPending));
                statusChangedNotCompletedAlertBuilder.setPositiveButton(cContext.getResources().getString(R.string.okAlertDialog), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ((IActivity) cContext).updateObject(Integer.parseInt(finalViewHolder.status.getTag().toString()), isChecked);
                    }
                });
                statusChangedNotCompletedAlertBuilder.setNegativeButton(cContext.getResources().getString(R.string.cancelAlertDialog), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                if (isChecked) {
                    statusChangedAlertBuilder.create().show();
                } else {
                    statusChangedNotCompletedAlertBuilder.create().show();
                }
            }
        });

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                deleteAlertBuilder = new AlertDialog.Builder(cContext);
                deleteAlertBuilder.setMessage(cContext.getResources().getString(R.string.DeleteConfirmation));
                deleteAlertBuilder.setPositiveButton(cContext.getResources().getString(R.string.okAlertDialog), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ((IActivity) cContext).deleteObject(Integer.parseInt(finalViewHolder.notes.getTag().toString()));
                    }
                });
                deleteAlertBuilder.setNegativeButton(cContext.getResources().getString(R.string.cancelAlertDialog), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                deleteAlertBuilder.create().show();
                return false;
            }
        });

        return convertView;
    }


    static class ViewHolder {
        TextView notes;
        CheckBox status;
        TextView priority;
        TextView updatedTime;
    }

    public interface IActivity {
        void updateObject(int position, boolean isChecked);

        void deleteObject(int position);
    }
}
