package com.pirana.aka.asap;

import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.pirana.aka.asap.CustomArrayAdapter.OnTodoClickListener;
import com.pirana.aka.asap.CustomArrayAdapter.TodosArrayAdapter;
import com.pirana.aka.asap.DAO.Todo;
import com.pirana.aka.asap.DAO.TodoOperations;

import java.util.ArrayList;
import java.util.List;

public class TodoListActivity extends AppCompatActivity implements LocationListener{
    public Intent i;
    public LocationManager locationManager ;
    public Location currentLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        Location location;
        try{
            currentLocation = locationManager.getLastKnownLocation(bestProvider);
        if (currentLocation != null) {
            onLocationChanged(currentLocation);
            Toast.makeText(getApplicationContext(),currentLocation.toString(),Toast.LENGTH_SHORT).show();
        }
        locationManager.requestLocationUpdates(bestProvider, 20000, 0, this);}
        catch (SecurityException e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        i = new Intent(this, MapsActivity.class);
        try{
        if(!currentLocation.equals(null)){
            i.putExtra("location",currentLocation);
        }}catch (Exception e){}
        TodosArrayAdapter adapter = new TodosArrayAdapter(this,R.layout.todo_element,generate());
        ListView listViewItems = (ListView) findViewById(R.id.listView);
        listViewItems.setAdapter(adapter);
        listViewItems.setOnItemClickListener(new OnTodoClickListener());
        Button addTodo = (Button) findViewById(R.id.addTodo);
        addTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i);

            }
        });
    }
    public List<Todo> poupulateList(){
        List<Todo> todos = new ArrayList<Todo>();
        TodoOperations op = new TodoOperations(getApplicationContext());
        try {
            todos = op.getAllTodos();
        }catch(SQLiteException e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return todos;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_todo_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public List<Todo> generate(){
        List<Todo> todos = new ArrayList<Todo>();
        for(int i=0;i<5;i++){
            Todo todo = new Todo();
            todo.set_id(i);
            todo.setAbout("TODO Number"+i);
            todos.add(todo);
        }
        return todos;
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        String msg = String.format(
                getResources().getString(R.string.new_location), currentLocation.getLatitude(),
                currentLocation.getLongitude(), currentLocation.getAltitude(), currentLocation.getAccuracy());
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        String newStatus = "";
        switch (status) {
            case LocationProvider.OUT_OF_SERVICE:
                newStatus = "OUT_OF_SERVICE";
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                newStatus = "TEMPORARILY_UNAVAILABLE";
                break;
            case LocationProvider.AVAILABLE:
                newStatus = "AVAILABLE";
                break;
        }
        String msg = String.format(
                getResources().getString(R.string.provider_disabled), provider,
                newStatus);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderEnabled(String provider) {
        String msg = String.format(
                getResources().getString(R.string.provider_enabled), provider);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        String msg = String.format(
                getResources().getString(R.string.provider_disabled), provider);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
