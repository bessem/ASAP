package com.pirana.aka.asap.DAO;

import android.location.Location;

import java.io.Serializable;

/**
 * Created by aka on 11/29/15.
 */
public class Todo implements Serializable{
    int _id;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    private String about;
    private Location location;

    public Todo() {
    }

    public Todo(int id, String about, Location location) {
        this.about = about;
        this.location = location;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
