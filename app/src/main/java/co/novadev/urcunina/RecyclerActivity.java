package co.novadev.urcunina;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import co.novadev.urcunina.Objects.Adapter;
import co.novadev.urcunina.Objects.Coche;
import co.novadev.urcunina.Objects.FirebaseReferences;

/**
 * Created by devcam on 29/05/2017.
 */

public class RecyclerActivity extends AppCompatActivity {

    RecyclerView rv;

    List<Coche> coches;

    Adapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("SESION", "Recycler Activo");
        setContentView(R.layout.activity_recycler);

        rv = (RecyclerView) findViewById(R.id.recycler);

        rv.setLayoutManager(new LinearLayoutManager(this));

        coches = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        adapter = new Adapter(coches);

        rv.setAdapter(adapter);
        Log.i("SESION", "Dibujando datos");
        final DatabaseReference tutorialRef = database.getReference(FirebaseReferences.TEST_REFERENCE ).child(FirebaseReferences.COCHE_REFERENCE);
        tutorialRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                coches.removeAll(coches);
                for (DataSnapshot snapshot:
                        dataSnapshot.getChildren()){
                    Coche coche = snapshot.getValue(Coche.class);
                    coches.add(coche);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
