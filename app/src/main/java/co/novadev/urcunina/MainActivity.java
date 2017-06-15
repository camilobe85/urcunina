package co.novadev.urcunina;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import co.novadev.urcunina.Objects.Coche;
import co.novadev.urcunina.Objects.FirebaseReferences;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button buttonCoche;

    Button buttonRegister, buttonSignIn;
    EditText editTextMail, editTextPass;

    FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //buttonCoche = (Button) findViewById(R.id.boton_coche);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        /*
        //TUTO1

        DatabaseReference myRef = database.getReference(FirebaseReferences.TEST_REFERENCE);
        //Log.i("KEY", myRef.getKey());

        //addValueEventListener: Sincronizado con DB
        //addListenerForSingleValueEvent: Dato, solo una vez
        //myRef.addListenerForSingleValueEvent
        myRef.push().setValue(7);
        ValueEventListener valueEventListener  = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int valor = dataSnapshot.getValue(Integer.class);
                Log.i("DATOS", valor+"");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("ERROR", databaseError.getMessage());
            }
        };

        myRef.addValueEventListener(valueEventListener);*/

       /* ///TUTO2: Añadir referencias a nuestro Firebase
        final DatabaseReference tutorialRef = database.getReference(FirebaseReferences.TEST_REFERENCE);
        buttonCoche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Coche coche = new Coche("Ford", "CamiloB", 5, 4);
                tutorialRef.child(FirebaseReferences.COCHE_REFERENCE).push().setValue(coche);
            }
        });*/

        buttonRegister = (Button) findViewById(R.id.register_button);
        buttonSignIn = (Button) findViewById(R.id.signin_button);
        editTextMail = (EditText) findViewById(R.id.login_email);
        editTextPass = (EditText) findViewById(R.id.login_password);

        buttonRegister.setOnClickListener(this);
        buttonSignIn.setOnClickListener(this);
        /*Intent i = new Intent(MainActivity.this, RecyclerActivity.class);
        this.startActivity(i);
        finish();*/
        //setContentView(R.layout.activity_main);
        mAuthStateListener = new FirebaseAuth.AuthStateListener(){

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user !=null){

                    /*Intent intent = new Intent(MainActivity.this, RecyclerActivity.class);
                    intent.putExtra("userID", user.getUid());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();*/
                    Log.i("SESION", "Sesión iniciada con email: " + user.getEmail());
                    Intent intent = new Intent(MainActivity.this, RecyclerActivity.class);
                    startActivity(intent);
                    finish();
                    //setContentView(R.layout.activity_recycler);

                }
                else{
                    Log.i("SESION", "Sesión cerrada");
                }
            }
        };

    }

    private void registrarSesion(String email, String pass){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.i("SESION", "Usuario creado correctamente");
                }
                else
                {
                    Log.e("SESION", task.getException().getMessage()+"");
                }
            }
        });
    }

    private void iniciarSesion(String email, String pass){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.i("SESION", "Sesión iniciada");
                }
                else
                {
                    Log.e("SESION", task.getException().getMessage()+"");
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signin_button:
                String emailInicio = editTextMail.getText().toString();
                String passInicio = editTextPass.getText().toString();
                iniciarSesion(emailInicio, passInicio);
                break;
            case  R.id.register_button:
                String emailReg = editTextMail.getText().toString();
                String passReg = editTextPass.getText().toString();
                registrarSesion(emailReg, passReg);
                break;

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthStateListener != null){
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthStateListener);
        }

    }
}
