package tumaini.tumaini.FinesOthers;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import tumaini.tumaini.Loans.Wakulima_Loan;
import tumaini.tumaini.R;

public class Fines extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fines);

        firebaseFirestore=FirebaseFirestore.getInstance();

        dialog();
    }

    private void dialog() {
        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(this);
        final EditText edittext = new EditText(Fines.this);
        alert.setMessage("Enter ID Number to fine");
        alert.setCancelable(false);
        alert.setTitle("ID number");

        alert.setView(edittext);

        alert.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, int whichButton) {
                //What ever you want to do with the value
                //OR
                final String id = edittext.getText().toString();
                final ProgressDialog progressDialog = new ProgressDialog(Fines.this);

                if (TextUtils.isEmpty(id)){
                    Toast.makeText(Fines.this, "Enter Id number", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }else {

                    new AlertDialog.Builder(Fines.this)
                            .setTitle("Confirmation")
                            .setMessage("Are you sure  " +id+ "  is the correct one?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(final DialogInterface dialogInterface, int i) {

                                    android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(Fines.this);
                                    final EditText mEdittext = new EditText(Fines.this);
                                    alert.setMessage("Enter Amount to fine");
                                    alert.setCancelable(false);
                                    alert.setTitle("Fine");

                                    alert.setView(mEdittext);

                                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        public void onClick(final DialogInterface dialog, int whichButton) {
                                            //What ever you want to do with the value
                                            //OR
                                            final String amount = mEdittext.getText().toString();

                                            if (TextUtils.isEmpty(amount)){
                                                Toast.makeText(Fines.this, "Enter Amount", Toast.LENGTH_SHORT).show();
                                                onBackPressed();
                                            }else {

                                                android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(Fines.this);
                                                final EditText mReason = new EditText(Fines.this);
                                                alert.setMessage("Enter Reason being fined");
                                                alert.setCancelable(false);
                                                alert.setTitle("Reason");

                                                alert.setView(mReason);

                                                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                    public void onClick(final DialogInterface dialog, int whichButton) {
                                                        //What ever you want to do with the value
                                                        //OR
                                                        final String reason = mReason.getText().toString();


                                                        progressDialog.setMessage("Please wait...");
                                                        progressDialog.show();


                                                        Map<String,Object> stringObjectMap=new HashMap<>();
                                                        stringObjectMap.put("amount",amount);
                                                        stringObjectMap.put("reason",reason);
                                                        stringObjectMap.put("id_no",id);
                                                        stringObjectMap.put("timeStamp", FieldValue.serverTimestamp());

                                                        firebaseFirestore.collection("Fines").document("AllFines").collection("Fines").document(id).set(stringObjectMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()){
                                                                    Toast.makeText(Fines.this, "Successful", Toast.LENGTH_SHORT).show();
                                                                    onBackPressed();
                                                                }else {
                                                                    Toast.makeText(Fines.this, "Network Error", Toast.LENGTH_SHORT).show();
                                                                }
                                                                progressDialog.dismiss();
                                                            }
                                                        });

                                                    }
                                                });

                                                alert.show();

                                            }

                                        }
                                    });


                                    alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            // what ever you want to do with No option.
                                            dialog.dismiss();
                                            onBackPressed();
                                        }
                                    });

                                    alert.show();

                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            onBackPressed();
                        }
                    }).setCancelable(false).show();

                }



            }
        });


        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
                dialog.dismiss();
                onBackPressed();
            }
        });

        alert.show();

    }
}
