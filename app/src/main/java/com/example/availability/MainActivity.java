package com.example.availability;

import static com.example.availability.constants.TOPIC;

import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.availability.api.ApiUtilities;
import com.example.availability.model.notificationdata;
import com.example.availability.model.pushnotifcation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    Button button7, button8, button9;
    public EditText Player1;
    public EditText Player2;
    public EditText year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button7 = findViewById(R.id.button7);
        button8 = findViewById(R.id.button8);
        View.OnClickListener buttonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView10 = findViewById(R.id.textView10);
                String status = textView10.getText().toString().trim();
                if (status.equalsIgnoreCase("Status: Occupied")) {
                    // Display a message that it is occupied
                    Toast.makeText(MainActivity.this, "The slot is occupied", Toast.LENGTH_SHORT).show();
                    Button newButton = findViewById(R.id.newButton);
                    newButton.setVisibility(View.VISIBLE);


                    newButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Create an AlertDialog Builder
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
                            dialogBuilder.setTitle("Enter Your Details");

                            // Inflate the layout for the dialog
                            LayoutInflater inflater = getLayoutInflater();
                            View dialogView = inflater.inflate(R.layout.dialog_add_to_queue, null);
                            dialogBuilder.setView(dialogView);

                            // Find the EditText views inside the dialog layout
                            EditText nameEditText = dialogView.findViewById(R.id.editTextName);
                            EditText phoneNumberEditText = dialogView.findViewById(R.id.editTextPhoneNumber);

                            dialogBuilder.setPositiveButton("Add to Queue", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String name = nameEditText.getText().toString().trim();
                                    String phoneNumber = phoneNumberEditText.getText().toString().trim();

                                    // Create a HashMap to store the name and phone number
                                    HashMap<String, Object> queueData = new HashMap<>();
                                    queueData.put("name", name);
                                    queueData.put("phoneNumber", phoneNumber);

                                    // Add the data to the queue in the Firebase Realtime Database
                                    DatabaseReference queueRef = FirebaseDatabase.getInstance().getReference().child("queue");
                                    queueRef.push().setValue(queueData);

                                    // Show a toast message indicating the name and phone number are added to the queue
                                    Toast.makeText(MainActivity.this, "Name and phone number added to the queue", Toast.LENGTH_SHORT).show();
                                }
                            });

                            dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            AlertDialog dialog = dialogBuilder.create();
                            dialog.show();
                        }
                    });


                } else {
                    // Create an AlertDialog Builder
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
                    dialogBuilder.setTitle("Enter Player Details");

                    LayoutInflater inflater = getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.alert_dialog_add_new_user, null);
                    dialogBuilder.setView(dialogView);

                    Player1 = dialogView.findViewById(R.id.Player1);
                    Player2 = dialogView.findViewById(R.id.Player2);
                    year = dialogView.findViewById(R.id.year);


                    dialogBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String player1name = Player1.getText().toString().trim();
                            String player2name = Player2.getText().toString().trim();
                            String yearname = year.getText().toString().trim();
                            // Add the data to the Firebase database
                            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("players");
                            DatabaseReference newEntryRef = databaseRef.push();
                            newEntryRef.child("player1name").setValue(player1name);
                            newEntryRef.child("player2name").setValue(player2name);
                            newEntryRef.child("yearname").setValue(yearname);

                            Toast.makeText(MainActivity.this, "Data added to Firebase", Toast.LENGTH_SHORT).show();
                            TextView textView10 = findViewById(R.id.textView10);
                            textView10.setText("Status: occupied");


                        }
                    });



                    dialogBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Cancel button clicked, close the dialog
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = dialogBuilder.create();
                    dialog.show();

                }
            }

        };
        button7.setOnClickListener(buttonClickListener);
        button8.setOnClickListener(buttonClickListener);

        button9=findViewById(R.id.button9);
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC);
        // Inside the deleteButton click listener
        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
                dialogBuilder.setTitle("Delete Player");

                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_delete_player, null);
                dialogBuilder.setView(dialogView);

                EditText nameEditText = dialogView.findViewById(R.id.editTextName);

                String message="Player has leave slot is empty now";
                String message1=" Catch your table now";


                dialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = nameEditText.getText().toString().trim();
                        // Perform the delete operation based on the entered name
                        // ...
                        pushnotifcation notification=new pushnotifcation(new notificationdata(message,message1),TOPIC);
                        sendNotification(notification);

                        Toast.makeText(MainActivity.this, "Player deleted successfully", Toast.LENGTH_SHORT).show();
                    }

                });

                dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = dialogBuilder.create();
                dialog.show();
            }
        });


    }

    private void sendNotification(pushnotifcation notification) {
        ApiUtilities.getClient().sendNotification(notification).enqueue(new Callback<pushnotifcation>() {
            @Override
            public void onResponse(Call<pushnotifcation> call, Response<pushnotifcation> response) {
                if(response.isSuccessful())
                    Toast.makeText(MainActivity.this,"success",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MainActivity.this,"success",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<pushnotifcation> call, Throwable t) {
                Toast.makeText(MainActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }


}