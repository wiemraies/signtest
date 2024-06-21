package com.example.signtest;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class AddVehiculeInfoFragment extends Fragment {
    private SwitchMaterial getLocation;
    private static final int REQUEST_LOCATION = 1;
    private EditText carName, carNumber, carModel;
    private MaterialCardView addCarPhotoButton;
    private ImageView carImageView, fullImageView;
    private AppCompatButton addCarInfoButton;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private FirebaseStorage mStorage;
    private StorageReference storageReference;
    private String currentUserId;
    private static final int IMAGE_PICK_CODE = 2;
    private Uri ImageUri;
    private Bitmap selectedImageBitmap;
    private KProgressHUD progressHUD;
    private TextView carLocation;
    private Animation animation;
    private LocationManager locationManager;
    private String CarLongitude, CarLatitude, address;

    public AddVehiculeInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_vehicule_info, container, false);
        carImageView = view.findViewById(R.id.carImage);
        addCarPhotoButton = view.findViewById(R.id.addPhoto);
        carName = view.findViewById(R.id.CarName);
        carModel = view.findViewById(R.id.CarModel);
        carNumber = view.findViewById(R.id.CarNumber);
        carLocation = view.findViewById(R.id.CarLocation);
        addCarInfoButton = view.findViewById(R.id.AddCarInfo);
        fullImageView = view.findViewById(R.id.FullImage);
        getLocation = view.findViewById(R.id.GetLocation);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        currentUserId = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        mStorage = FirebaseStorage.getInstance();
        storageReference = mStorage.getReference();

        addCarPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check storage permissions
                checkStoragePermission();
            }
        });

        addCarInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        getLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    // Check location permissions
                    checkLocationPermission();
                }else {
                    // Stop location updates
                    stopLocationUpdates();
                }
            }
        });


        return view;
    }

    private void onGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Enable GPS").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        final AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showProgressBar() {
        progressHUD = KProgressHUD.create(requireContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setMaxProgress(100)
                .setWindowColor(R.color.light_green)
                .show();
        progressHUD.setProgress(90);
    }

    private void checkStoragePermission() {
        // Code to check storage permissions
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_LOCATION);
        } else {
            // Permission already granted, proceed with picking an image
            pickImages();
        }
    }

    private void pickImages() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        launchSomeActivity.launch(intent);
    }

    ActivityResultLauncher<Intent> launchSomeActivity
            = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    // Do your operation from here ...
                    if (data != null && data.getData() != null) {
                        ImageUri = data.getData();
                        try {
                            selectedImageBitmap = MediaStore.Images.Media.getBitmap(
                                    getActivity().getContentResolver(), ImageUri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (ImageUri != null) {
                            carImageView.setVisibility(View.GONE);
                            fullImageView.setVisibility(View.VISIBLE);
                            fullImageView.setImageBitmap(selectedImageBitmap);
                        } else {
                            fullImageView.setVisibility(View.GONE);
                        }
                    }
                }
            });

    private void uploadImage() {
        if (ImageUri != null) {
            showProgressBar();
            final StorageReference myRef = storageReference.child("photo/" + ImageUri.getLastPathSegment());
            myRef.putFile(ImageUri).addOnSuccessListener(taskSnapshot -> {
                myRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    if (uri != null) {
                        String photoUrl = uri.toString();
                        uploadCarInfo(photoUrl);
                    }
                }).addOnFailureListener(e -> {
                    progressHUD.dismiss();
                    Toast.makeText(getContext(), "Failed to get download URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }).addOnFailureListener(e -> {
                progressHUD.dismiss();
                Toast.makeText(getContext(), "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void closeFragment() {
        // Retirer le fragment actuel
        getParentFragmentManager().popBackStack();

        // Réafficher le bouton et la barre de recherche dans le fragment Garag_Fragment
        Garag_Fragment garagFragment = new Garag_Fragment();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, garagFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void uploadCarInfo(String photoUrl) {
        String name = carName.getText().toString().trim();
        String carModelText = carModel.getText().toString().trim();
        String number = carNumber.getText().toString().trim();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(carModelText) || TextUtils.isEmpty(number)) {
            progressHUD.dismiss();
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
        } else {
            // Ensure document reference is created correctly
            DocumentReference documentReference = firestore.collection("VehiculeInfo").document();
            String vehiculeDocId = documentReference.getId(); // Get document ID
            VehiculeModel vehiculeModel = new VehiculeModel(name, carModelText, number, CarLongitude, CarLatitude, photoUrl, vehiculeDocId, currentUserId,address);
            documentReference.set(vehiculeModel, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    progressHUD.dismiss();
                    Toast.makeText(getContext(), "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                    closeFragment();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressHUD.dismiss();
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void checkLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        }
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            onGPS();
        } else {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            } else {
                getCurrentLocation();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (gpsLocation != null) {
                double latitude = gpsLocation.getLatitude();
                double longitude = gpsLocation.getLongitude();
                CarLatitude = String.valueOf(latitude);
                CarLongitude = String.valueOf(longitude);
                String location = CarLatitude + ", " + CarLongitude;
                carLocation.setText(location);
                getAddressFromLatLong(getContext(), latitude, longitude);
            } else {
                // Si la dernière localisation connue est nulle, essayez de demander une mise à jour de la localisation
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            CarLatitude = String.valueOf(latitude);
            CarLongitude = String.valueOf(longitude);
            String loc = CarLatitude + ", " + CarLongitude;
            carLocation.setText(loc);
            getAddressFromLatLong(getContext(), latitude, longitude);
            locationManager.removeUpdates(this);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

        @Override
        public void onProviderEnabled(@NonNull String provider) {
            Toast.makeText(getContext(), "GPS Enabled", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {
            Toast.makeText(getContext(), "GPS Disabled", Toast.LENGTH_SHORT).show();
            onGPS();
        }
    };

    public void getAddressFromLatLong(Context context, double LATITUDE, double LONGITUDE) {
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null && addresses.size() > 0) {
                address = addresses.get(0).getAddressLine(0);
            }
            carLocation.setText(address);
            carLocation.setSelected(true);
        } catch (IOException e) {
            e.printStackTrace();
            carLocation.setText("Unable to get address");
        }
    }
    private void stopLocationUpdates() {
        if (locationManager != null && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.removeUpdates(locationListener);
            carLocation.setText("Location updates stopped");
        }
    }
}
