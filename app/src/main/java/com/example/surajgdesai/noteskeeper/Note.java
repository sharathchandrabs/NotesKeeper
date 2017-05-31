package com.example.surajgdesai.noteskeeper;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;


/**
 * Created by Suraj G Desai on 2/27/2017.
 */

public class Note extends RealmObject implements Comparable<Note> {
    @PrimaryKey
    public String _id;

    public String note, priority;

    public int prioritystatus;

    public int getPrioritystatus() {
        return prioritystatus;
    }

    public void setPrioritystatus(int prioritystatus) {
        this.prioritystatus = prioritystatus;
    }

    public Date update_time;
    public boolean status;

    public Note() {
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    @Override
    public String toString() {
        return "Note{" +
                "_id=" + _id +
                ", note='" + note + '\'' +
                ", priority='" + priority + '\'' +
                ", update_time=" + update_time +
                ", status=" + status +
                '}';
    }

    @Override
    public int compareTo(Note o) {
        int returnValue = 0;

        if (!this.priority.equals(o.getPriority())) {
            if (this.priority.equals("High")) {
                returnValue = 1;
            } else if (o.getPriority().equals("High")) {
                return -1;
            } else if (o.getPriority().equals("Medium")) {
                return -1;
            } else if (this.priority.equals("Medium")) {
                return 1;
            }
        }

        return returnValue;
    }
}
