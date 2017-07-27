package com.example.leo.firetry;

import android.content.DialogInterface;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ProgressBar progressBar;
    EditText ided;
    Button addbtn;
    String mainId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button checkBtn  = (Button)findViewById(R.id.checkuserbtn);
        //addbtn   = (Button)findViewById(R.id.adduserbtn);
        ided = (EditText)findViewById(R.id.entryIded);
        progressBar = (ProgressBar)findViewById(R.id.progressBar_cyclic);
        progressBar.setVisibility(View.GONE);

        checkBtn.setOnClickListener(this);
    }
    boolean toggle =true;
    @Override
    public void onClick(View view) {

        switch (view.getId())
        {

            case R.id.checkuserbtn:
                progressBar.setVisibility(View.VISIBLE);
                if(!ided.getText().toString().trim().equals(""))
                {
                    toggle=true;
                    final String id = ided.getText().toString();
                    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Residents");
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(toggle==true) {
                                toggle = false;
                                int i=0;
                                for (final DataSnapshot dp : dataSnapshot.getChildren()
                                        ) {
                                    progressBar.setVisibility(View.INVISIBLE);

                                    if (dp.child("id").getValue().equals(id)) {
                                        final Users usr = dp.getValue(Users.class);
                                        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
                                        View content = inflater.inflate(R.layout.few_det,null);
                                        TextView name =(TextView)content.findViewById(R.id.name_few);
                                        TextView phone =(TextView)content.findViewById(R.id.phone_few);
                                        TextView id =(TextView)content.findViewById(R.id.id_few);
                                        TextView intime =(TextView)content.findViewById(R.id.intime_few);
                                        name.setText(usr.name);
                                        id.setText(usr.id);
                                        phone.setText(usr.phoneNo);
                                        intime.setText(usr.inTime);


                                        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                        builder.setView(content);
                                        builder.setMessage("Do you want checkout recident?");
                                        builder.setTitle("Exists");
                                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                DataService.CheckOut(dp.getKey(),usr.inMilis);
                                                DataService.fetchAllbyid(usr.id, new WalkCallback() {
                                                    @Override
                                                    public void getuserKey(String key) {

                                                    }

                                                    @Override
                                                    public void getuserAll(Users usr) {
                                                        if(usr!=null)
                                                        {
                                                            showwalker(usr);
                                                        }
                                                    }
                                                });


                                            }
                                        });
                                        builder.setNegativeButton("No", null);
                                        AlertDialog alert = builder.create();
                                        alert.show();
                                        i=1;
                                        break;
                                    }


                                }
                                if(i==0){
                                    progressBar.setVisibility(View.INVISIBLE);
                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                    builder.setMessage("Do you want to add?");
                                    builder.setTitle("Nothing Here");
                                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            addwalker(id);
                                            //addbtn.setBackgroundColor(Color.BLUE);
                                        }
                                    });
                                      AlertDialog alert = builder.create();
                                    alert.show();
                                }





                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                            //progressBar.setVisibility(View.VISIBLE);

                            //Toast.makeText(MainActivity.this,id,Toast.LENGTH_SHORT).show();







            }
            else {
                    Toast.makeText(getApplicationContext(),"Enter ID",Toast.LENGTH_SHORT).show();}

                break;





        }

    }

    public void showwalker(Users usr)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
        View content = inflater.inflate(R.layout.details_resident,null);
        final TextView name = (TextView) content.findViewById(R.id.name_tv);
        final TextView id = (TextView)content.findViewById(R.id.id_tv);

        final TextView addr = (TextView)content.findViewById(R.id.add_tv);
        final TextView phone = (TextView)content.findViewById(R.id.phone_tv);
        final TextView intime = (TextView)content.findViewById(R.id.inTime_tv);
        final TextView outtime = (TextView)content.findViewById(R.id.outTime_tv);

        final TextView duration = (TextView)content.findViewById(R.id.duration_tv);

        name.setText(usr.name);
        id.setText(usr.id);
        phone.setText(usr.phoneNo);
        addr.setText(usr.address);
        intime.setText(usr.inTime);
        outtime.setText(usr.OutTime);
        duration.setText(usr.duration);


        alert.setTitle("Details of walker");
        alert.setView(content);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alert.setNegativeButton("Cancel",null);
        AlertDialog dialog = alert.create();
        dialog.show();

    }


    public void addwalker(final String id)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
        View content = inflater.inflate(R.layout.dialog_add,null);
        final EditText nameed = (EditText) content.findViewById(R.id.nameed);
        final EditText ided = (EditText)content.findViewById(R.id.ided);
        ided.setText(id);
        final EditText addred = (EditText)content.findViewById(R.id.addred);
        final EditText phoneed = (EditText)content.findViewById(R.id.phoned);
        final EditText intimeed = (EditText)content.findViewById(R.id.intimeed);
        GregorianCalendar gc = new GregorianCalendar();
        int am_pm = gc.get(Calendar.AM_PM);

        intimeed.setText(gc.get(Calendar.HOUR)+"H:"+gc.get(Calendar.MINUTE)+"M:"+gc.get(Calendar.HOUR)+"S:"+(am_pm==0?"AM":"PM"));
        alert.setTitle("Details of walker");
        alert.setView(content);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                DataService.entryData(new Users(nameed.getText().toString(),
                           id,
                        addred.getText().toString(),
                        phoneed.getText().toString(),
                        intimeed.getText().toString()));
            }
        });
        alert.setNegativeButton("Cancel",null);
        AlertDialog dialog = alert.create();
        dialog.show();

    }
}
