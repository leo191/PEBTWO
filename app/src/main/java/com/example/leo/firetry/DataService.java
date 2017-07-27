package com.example.leo.firetry;

import android.os.AsyncTask;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by leo on 26/07/17.
 */

public class DataService {




    public static void entryData(Users usr)
    {
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Residents");
        String key = databaseReference.push().getKey();
        usr.inMilis = System.currentTimeMillis();
        databaseReference.child(key).setValue(usr);


    }





    public static void fetchAllbyid(final String id, final WalkCallback callback) {


            new getWalker(id,callback).execute();


    }

    public static void CheckOut(String id,long start) {

        String path = "Residents/"+id;
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(path);
        long duration = System.currentTimeMillis()-start;

        int seconds = (int) (duration / 1000) % 60 ;
        int minutes = (int) ((duration / (1000*60)) % 60);
        int hours   = (int) ((duration / (1000*60*60)) % 24);


        databaseReference.child("duration").setValue(String.valueOf(hours)+"H:"
        +String.valueOf(minutes)+"M:"+String.valueOf(seconds)+"S");
        GregorianCalendar gc = new GregorianCalendar();
        int am_pm = gc.get(Calendar.AM_PM);
        databaseReference.child("OutTime").setValue(gc.get(Calendar.HOUR)+"H:"+gc.get(Calendar.MINUTE)+"M:"+ gc.get(Calendar.SECOND)+"S"+(am_pm==1?"PM":"AM"));


    }






    public static  class getWalker extends AsyncTask<Void,Void,Void>
    {
            String id;
            WalkCallback callback;
            static Users usr;
            public getWalker(String id,WalkCallback callback)
            {
                this.id=id;
                this.callback = callback;
            }



        @Override
        protected Void doInBackground(Void... voids) {

            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Residents");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot dp: dataSnapshot.getChildren()
                         ) {
                            usr = dp.getValue(Users.class);
                            if(usr.id.equals(id))
                            {
                                callback.getuserAll(usr);
                                break;
                            }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



            return null;
        }



    }

}
