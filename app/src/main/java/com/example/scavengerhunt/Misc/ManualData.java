package com.example.scavengerhunt.Misc;

import android.graphics.drawable.Drawable;

import com.example.scavengerhunt.Entities.Artifact;
import com.example.scavengerhunt.Entities.Scavenger;
import com.example.scavengerhunt.Entities.Site;
import com.example.scavengerhunt.R;

import java.util.ArrayList;
import java.util.List;

public class ManualData {

    private static ManualData INSTANCE = new ManualData();

    public Artifact artifact1;
    public Artifact artifact2;
    public Artifact artifact3;

    public int artifact1Raw;
    public int artifact2Raw;
    public int artifact3Raw;

    public Site site1;
    public Site site2;
    public Site site3;

    public List<Artifact> artifactsList;

    public int activeGeofence;

    public ManualData() {
        this.artifact1 = new Artifact("2100BC Chair", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent pulvinar vitae tortor sed posuere. Pellentesque ac neque libero. Sed iaculis tortor eu porta mattis. Aliquam venenatis lobortis lacinia. Proin blandit faucibus porttitor.",
                "https://firebasestorage.googleapis.com/v0/b/cct-scavenger-hunt.appspot.com/o/chair.png?alt=media&token=c7c9cd6d-2e19-400a-91c3-2860fdec865d",
                52.938268, -1.189575);
        this.artifact1Raw = R.raw.chair;

        this.artifact2 = new Artifact("1265 Totem", "Vestibulum suscipit metus libero, vitae sodales nulla maximus ultricies. Proin molestie sollicitudin nunc, in sagittis sem pulvinar vitae. Etiam dui lacus, aliquet quis feugiat ac ligula. Donec et tortor nisi. Maecenas elementum laoreet aliquam.",
                "https://firebasestorage.googleapis.com/v0/b/cct-scavenger-hunt.appspot.com/o/totem.png?alt=media&token=bad173ea-34b7-41f5-ad51-daf8a13b417f",
                52.935584, -1.194317);
        this.artifact2Raw = R.raw.totem;

        this.artifact3 = new Artifact("900s Anfora Romana", "Sed enim sem, tempus a nunc eleifend, tincidunt egestas magna. Sed a augue a nulla fermentum tincidunt vitae id lectus. Nam ut suscipit enim. Nam dictum et ipsum id facilisis.",
                "https://firebasestorage.googleapis.com/v0/b/cct-scavenger-hunt.appspot.com/o/anfora.png?alt=media&token=70e1871e-731b-402b-ac51-75dac0921f0d",
                52.933092, -1.201403);
        this.artifact3Raw = R.raw.anfora;

        this.site1 = new Site("Lakeside Arts UP", "Fusce lacinia varius leo sit amet porta. Suspendisse rhoncus turpis tortor. Fusce convallis, sem id vehicula vestibulum, mauris tellus tempus sapien, ullamcorper finibus tortor urna eu justo.",
                "https://firebasestorage.googleapis.com/v0/b/cct-scavenger-hunt.appspot.com/o/lakesidearts.png?alt=media&token=f0d00034-74f2-4a0b-91dc-d3e6aa0dca89",
                false, 52.938268, -1.189575);

        this.site2 = new Site("Highfield Park UP", "Proin nisl orci, feugiat eu accumsan eget, dictum vitae orci. Suspendisse rutrum sem massa, non lobortis ligula venenatis quis. Donec condimentum semper quam.",
                "https://firebasestorage.googleapis.com/v0/b/cct-scavenger-hunt.appspot.com/o/highfield.png?alt=media&token=20302111-6eb0-450c-8521-4986d19b75c9",
                false, 52.935584, -1.194317);

        this.site3 = new Site("Serenity Garden", "Nullam et libero eget urna interdum pharetra. Proin facilisis massa vestibulum, vulputate nulla sit amet, congue justo. Nam posuere id libero interdum vulputate. Integer finibus mi dolor, eu blandit ligula pretium id. Pellentesque lobortis.",
                "https://firebasestorage.googleapis.com/v0/b/cct-scavenger-hunt.appspot.com/o/serenity.png?alt=media&token=426a6693-f97d-48e3-9b6a-96f3ca017d1a",
                true, 52.933092, -1.201403);

        this.artifactsList = new ArrayList<>();
        this.artifactsList.add(artifact1);
        this.artifactsList.add(artifact2);
        this.artifactsList.add(artifact3);

        this.activeGeofence = 0;
    }

    public static ManualData getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ManualData();
        }
        return INSTANCE;
    }

    public int inArea() {
        return this.activeGeofence;
    }

    public int getRaw() {
        switch(this.activeGeofence) {
            case 1 : return this.artifact1Raw;
            case 2 : return this.artifact2Raw;
            case 3 : return this.artifact3Raw;
        }
        return 0;
    }

    public void setActiveGeofence(int active) {
        this.activeGeofence = active;
    }







}
