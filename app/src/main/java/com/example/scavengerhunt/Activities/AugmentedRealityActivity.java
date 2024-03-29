package com.example.scavengerhunt.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scavengerhunt.Entities.Action;
import com.example.scavengerhunt.Entities.Artifact;
import com.example.scavengerhunt.Firebase.Database;
import com.example.scavengerhunt.Misc.CameraPermissionHelper;
import com.example.scavengerhunt.Misc.DataManager;
import com.example.scavengerhunt.R;
import com.google.ar.core.Anchor;
import com.google.ar.core.ArCoreApk;
import com.google.ar.core.Config;
import com.google.ar.core.Plane;
import com.google.ar.core.Pose;
import com.google.ar.core.Session;
import com.google.ar.core.exceptions.UnavailableApkTooOldException;
import com.google.ar.core.exceptions.UnavailableArcoreNotInstalledException;
import com.google.ar.core.exceptions.UnavailableDeviceNotCompatibleException;
import com.google.ar.core.exceptions.UnavailableException;
import com.google.ar.core.exceptions.UnavailableSdkTooOldException;
import com.google.ar.core.exceptions.UnavailableUserDeclinedInstallationException;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.squareup.picasso.Picasso;

import java.util.Collection;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class AugmentedRealityActivity extends AppCompatActivity {

    //from tutorial https://medium.com/geekculture/develop-your-helloar-app-in-android-studio-using-arcore-and-sceneform-d032e5788036

    private Session mSession;
    private ArFragment arFragment;
    private Plane plane;
    private static Random rand = new Random();
    private TextView scanMessage;
    private AnchorNode anchorNode;
    private TransformableNode node;
    private Boolean finalArtifact = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        scanMessage = findViewById(R.id.scanText);

        if (!isARCoreSupportedAndUpToDate())
            return;

        setContentView(R.layout.activity_augmented_reality);

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);

        handler.postDelayed(placeRandomly, 10000);



    }


    private boolean mUserRequestedInstall = true;

    @Override
    protected void onResume() {
        super.onResume();

        // ARCore requires camera permission to operate.
        if (!CameraPermissionHelper.hasCameraPermission(this)) {
            CameraPermissionHelper.requestCameraPermission(this);
            return;
        }

        // Ensure that Google Play Services for AR and ARCore device profile data are
        // installed and up to date.
        try {
            if (mSession == null) {
                switch (ArCoreApk.getInstance().requestInstall(this, mUserRequestedInstall)) {
                    case INSTALLED:
                        // Success: Safe to create the AR session.
                        if(isARCoreSupportedAndUpToDate())
                        {
                            mSession = new Session(this);
                            Config config = new Config(mSession);
                            // Configure the session.
                            mSession.configure(config);

                        }
                        break;
                    case INSTALL_REQUESTED:
                        mUserRequestedInstall = false;
                        return;
                }
            }
        } catch (UnavailableUserDeclinedInstallationException | UnavailableArcoreNotInstalledException | UnavailableApkTooOldException | UnavailableSdkTooOldException | UnavailableDeviceNotCompatibleException e) {
            // Display an appropriate message to the user and return gracefully.
            Toast.makeText(this, "TODO: handle exception " + e, Toast.LENGTH_LONG)
                    .show();
            return;
        }

        anchorNode = new AnchorNode();
        anchorNode.setOnTapListener((hitTestResult, motionEvent) -> {
            Log.d("TAP", "object tapped");
            TextView text = findViewById(R.id.scanText);
            text.setText("POO");
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] results) {
        super.onRequestPermissionsResult(requestCode, permissions, results);
        if (!CameraPermissionHelper.hasCameraPermission(this)) {
            Toast.makeText(this, "Camera permission is needed to run this application", Toast.LENGTH_LONG)
                    .show();
            if (!CameraPermissionHelper.shouldShowRequestPermissionRationale(this)) {
                // Permission denied with checking "Do not ask again".
                CameraPermissionHelper.launchPermissionSettings(this);
            }
            finish();
        }
    }

    final Handler handler = new Handler(Looper.getMainLooper());

    private Runnable placeRandomly = new Runnable() {
        @Override
        public void run() {
            Collection<Plane> planes = arFragment.getArSceneView().getSession()
                    .getAllTrackables(Plane.class);
            //Plane[] arrPlanes = (Plane[]) planes.toArray();
            Plane[] arrPlanes = planes.toArray(new Plane[planes.size()]);
            if (!planes.isEmpty()) {
                /*BROING BACKK !£OJ!EO!
                if(planes.size() < 3) {
                    handler.postDelayed(placeRandomly, 3000);
                    return;
                }*/
                TextView text = findViewById(R.id.scanText);
                text.setText("Find the artifact!");

                //scanMessage.setEnabled(false);
                //int randPlane = rand.nextInt(planes.size() - 2); BRING BACK!K! ! ! !!
                int randPlane = 0;
                Pose pose = arrPlanes[randPlane].getCenterPose();
                Anchor anchor = arFragment.getArSceneView().getSession().createAnchor(pose);
                int rawId = DataManager.getInstance().getRaw();
                Log.d("GEO", "RAW ID: " + rawId);
                Log.d("GEO", "CHAIR ID: " + R.raw.chair);

                //placeObject(arFragment, anchor, rawId);
                placeObject(arFragment, anchor, rawId);
            }
            else{
                handler.postDelayed(placeRandomly, 3000);
                return;
            }
        }
    };

    private boolean isARCoreSupportedAndUpToDate() {
        ArCoreApk.Availability availability = ArCoreApk.getInstance().checkAvailability(this);
        switch (availability) {
            case SUPPORTED_INSTALLED:
                return true;

            case SUPPORTED_APK_TOO_OLD:
            case SUPPORTED_NOT_INSTALLED:
                try {
                    // Request ARCore installation or update if needed.
                    ArCoreApk.InstallStatus installStatus = ArCoreApk.getInstance().requestInstall(this, true);
                    switch (installStatus) {
                        case INSTALL_REQUESTED:
                            Log.i("TAG", "ARCore installation requested.");
                            return false;
                        case INSTALLED:
                            return true;
                    }
                } catch (UnavailableException e) {
                    Log.e("TAG", "ARCore not installed", e);
                }
                return false;

            case UNSUPPORTED_DEVICE_NOT_CAPABLE:
                // This device is not supported for AR.
                return false;

            case UNKNOWN_CHECKING:
                // ARCore is checking the availability with a remote query.
                // This function should be called again after waiting 200 ms to determine the query result.
            case UNKNOWN_ERROR:
            case UNKNOWN_TIMED_OUT:
                // There was an error checking for AR availability. This may be due to the device being offline.
                // Handle the error appropriately.
        }
        return true;
    }

    private void placeObject(ArFragment arFragment, Anchor anchor, int uri) {
        ModelRenderable.builder()
                .setSource(arFragment.getContext(), uri)
                .build()
                .thenAccept(modelRenderable -> addNodeToScene(arFragment, anchor, modelRenderable))
                .exceptionally(throwable -> {
                            Toast.makeText(arFragment.getContext(), "Error:" + throwable.getMessage(), Toast.LENGTH_LONG).show();
                            return null;
                        }
                );
    }

    private void addNodeToScene(ArFragment arFragment, Anchor anchor, Renderable renderable) {
        anchorNode.setAnchor(anchor);
        node = new TransformableNode(arFragment.getTransformationSystem());
        node.setRenderable(renderable);
        node.setParent(anchorNode);
        arFragment.getArSceneView().getScene().addChild(anchorNode);
        node.select();

        node.setOnTapListener((hitTestResult, motionEvent) -> {
            Log.d("HIT", "HIT REGISTERED");
            com.example.scavengerhunt.Entities.Log log = new com.example.scavengerhunt.Entities.Log();
            Artifact artifact = DataManager.getInstance().artifactsList.get(DataManager.getInstance().activeGeofence-1);

            log.setStamp(getTime() + ": Artifact Collected");
            log.setTitle(artifact.getName());
            log.setLabel("Description:");
            log.setDescription(artifact.getDescription());
            log.setArtifact(artifact);
            log.setImage(artifact.getUrl());

            com.example.scavengerhunt.Entities.Session.getInstance().addLog(log);
            Database.getInstance().addLog();
            //Database.getInstance().testLogArtifact();
            DataManager.getInstance().artifactsList.get(DataManager.getInstance().activeGeofence-1).setCollected(true);
            if(DataManager.getInstance().activeGeofence-1 == 2) finalArtifact = true;
            DataManager.getInstance().activeGeofence = 0;

            inflatePopup(artifact);
        });


    }

    public String getTime() {
        Long time = System.currentTimeMillis();
        String string = "%02d:%02d";
        String text = String.format(Locale.ENGLISH, string, TimeUnit.MILLISECONDS.toHours(time),
                TimeUnit.MILLISECONDS.toMinutes(time));
        return text;
    }

    public void inflatePopup(Artifact artifact) {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_view, null);

        TextView stamp = popupView.findViewById(R.id.popup_stamp);
        TextView description = popupView.findViewById(R.id.popup_description);
        TextView title = popupView.findViewById(R.id.popup_title);
        ImageView imageView = popupView.findViewById(R.id.popup_image);

        stamp.setText("Artifact Collected!");
        description.setText(artifact.getDescription());
        title.setText(artifact.getName());
        try{
            Picasso.get().load(artifact.getUrl()).into(imageView);
        }catch (NullPointerException e){
            android.util.Log.d("IMAGE", "Exception: " + e);
        };

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(findViewById(android.R.id.content).getRootView()
                , Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        Button button = popupView.findViewById(R.id.popup_button);
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                Action action = new Action();
                action.setType("artifact");
                action.setData1(artifact.getName());
                if(finalArtifact) {
                    action.setType("final");
                }
                Database.getInstance().addArtifact(artifact);
                Database.getInstance().newAction(action);
                finish();
                return true;
            }
        });
    }


}