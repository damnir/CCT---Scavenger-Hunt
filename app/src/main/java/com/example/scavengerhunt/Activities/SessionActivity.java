package com.example.scavengerhunt.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.scavengerhunt.Entities.Artifact;
import com.example.scavengerhunt.Entities.Scavenger;
import com.example.scavengerhunt.Entities.Session;
import com.example.scavengerhunt.Entities.User;
import com.example.scavengerhunt.Firebase.Database;
import com.example.scavengerhunt.R;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class SessionActivity extends AppCompatActivity {

    TextView name;
    private Session session;
    private Database dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);

        dbRef = Database.getInstance();
        session = Session.getInstance();


        Log.d("name", "Name on create: " + User.getInstance().getName());

        name = findViewById(R.id.sessionUsername);
        name.setText(User.getInstance().getName());

        /*
        Artifact coin = new Artifact();
        coin.setName("The Parade Shield");
        coin.setDescription("The shield is part of a suit of armour that was delivered to the King in 1562. The whole set was problably made in Arboga and then sent to Antwerp where it was decorated by the goldsmith Eliseus Libaerts, following drawings by the French artist Etienne Delaune. To create the relief decor the steel has been chased, embossed, etched and gilded. The motif is inspired by tales from antiquity and depicts amazons fighting male warriors. It is probably a battle scene from the Trojan war. According to the myth the amazons supported King Priam of Troy in the war but also lost their queen Penthesileia when she was killed by Achilles.");
        coin.setUrl("gs://cct-scavenger-hunt.appspot.com/coin.png");
        dbRef.addCoin(coin);*/
    }

    public void newSessionClick(View v) {
        String newSessionId = generateSessionId();
        User.getInstance().setActiveSessionId(newSessionId);
        session.setSessionId(newSessionId);
        session.setOwner(User.getInstance());

        Scavenger scavenger = Scavenger.getInstance();
        scavenger.setUser(User.getInstance());
        session.addScavenger(scavenger);

        dbRef.newSession(session);
        Intent intent = new Intent(this, NewSessionActivity.class);
        startActivity(intent);
    }

    public void joinSessionClick(View v) {
        Intent intent = new Intent(this, JoinActivity.class);
        startActivity(intent);
    }

    public String generateSessionId() {
        Random rand = new Random();
        String id = "";
        for(int i = 0; i < 6; i++) id += rand.nextInt(9);
        return id;
    }


}