package tumaini.tumaini.Loans;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import tumaini.tumaini.FirebaseNotificationActionHelper.FirebaseNotiCallBack;
import tumaini.tumaini.FirebaseNotificationActionHelper.FirebaseNotificationHelper;
import tumaini.tumaini.New_Member;
import tumaini.tumaini.Normal_Loan;
import tumaini.tumaini.R;

import static tumaini.tumaini.FirebaseNotificationActionHelper.Constants.KEY_TEXT;
import static tumaini.tumaini.FirebaseNotificationActionHelper.Constants.KEY_TITLE;

public class Wakulima_Loan extends AppCompatActivity implements FirebaseNotiCallBack{

    private EditText mFname,mLname,mSurname,mMemberno,mPhoneNo,mID,mAmount,mRates;
    private Button mSubmit;
    private RadioButton mAgree,mDecline;
    private FirebaseFirestore firebaseFirestore;
    private String user_id;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wakulima__loan);

        firebaseFirestore=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        user_id=auth.getCurrentUser().getUid();
        progressDialog=new ProgressDialog(this);
        mAgree=(RadioButton)findViewById(R.id.rad_iagree_wakulima);
        mDecline=(RadioButton)findViewById(R.id.rad_decline_wakulima);

        mAmount=(EditText)findViewById(R.id.edt_amount_applying_wakulima);
        mFname=(EditText)findViewById(R.id.edt_First_name_wakulima);
        mLname=(EditText)findViewById(R.id.edt_Last_name_wakulima);
        mSurname=(EditText)findViewById(R.id.edt_Surname_name_wakulima);
        mMemberno=(EditText)findViewById(R.id.edt_Member_number_wakulima);
        mPhoneNo=(EditText)findViewById(R.id.edt_Phone_number_wakulima);
        mID=(EditText)findViewById(R.id.edt_ID_number_wakulima);
        mRates=(EditText)findViewById(R.id.edt_Phone_rate_wakulima);

        mSubmit=(Button)findViewById(R.id.SubmitWakulima);


        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount=mAmount.getText().toString().trim();
                String fname=mFname.getText().toString().trim();
                String lname=mLname.getText().toString().trim();
                String surname=mSurname.getText().toString().trim();
                String memberno=mMemberno.getText().toString().trim();
                String phone=mPhoneNo.getText().toString().trim();
                String id=mID.getText().toString().trim();
                String rate=mRates.getText().toString().trim();
                if (TextUtils.isEmpty(fname)){
                    mFname.setError("Enter First name");
                }else if (TextUtils.isEmpty(lname)){
                    mLname.setError("Enter last name");
                }else if (TextUtils.isEmpty(surname)){
                    mSurname.setError("Enter Other names");
                }else if (TextUtils.isEmpty(memberno)){
                    mMemberno.setError("Enter Member Number");
                }else if (TextUtils.isEmpty(phone)){
                    mPhoneNo.setError("Enter Phone number");
                }else if (TextUtils.isEmpty(id)){
                    mID.setError("Enter ID number");
                }else if (TextUtils.isEmpty(amount)){
                    mAmount.setError("Enter Amount applying for");
                }else if (TextUtils.isEmpty(rate)){
                    mRates.setError("Enter the rates");
                }else {

                    int amnt = Integer.parseInt(amount);
                    if (mAgree.isChecked()){
                        if (amnt>20000){
                            Toast.makeText(Wakulima_Loan.this, "Amount requested is too high...", Toast.LENGTH_SHORT).show();
                        }else {

                            progressDialog.setMessage("Submitting Application");
                            progressDialog.show();
                            submittingnormal(null,fname,lname,surname,memberno,phone,id,amount,rate);

                        }

                    }else {
                        Toast.makeText(Wakulima_Loan.this, "Agree to continue", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        dialog();
    }

    public void submittingnormal(@NonNull Task<UploadTask.TaskSnapshot> task , String fname, String lname, String Othernames, String Memberno, String Phoneno, String IdNo,
                                 final String Amount, String Rate){


        float finalAmount = 0;

        float initialAmount = Float.parseFloat(Amount);
        float initialRate=Float.parseFloat(Rate);
        float interest = (float) (0.01*initialRate*initialAmount);
        finalAmount = interest+initialAmount;

        String doc="WakulimaLoan";
        String Recentpayed="0.00";
        final String fullname=fname+" "+lname;

        Map<String,Object> stringObjectMap=new HashMap<>();
        stringObjectMap.put("fname",fname);
        stringObjectMap.put("lname",lname);
        stringObjectMap.put("Other_Names",Othernames);
        stringObjectMap.put("Member_Number",Memberno);
        stringObjectMap.put("Phone_Number",Phoneno);
        stringObjectMap.put("id",IdNo);
        stringObjectMap.put("amount",Amount);
        stringObjectMap.put("document",doc);
        stringObjectMap.put("recentpayed",Recentpayed);
        stringObjectMap.put("final_amount",""+finalAmount);
        stringObjectMap.put("timeStamp", FieldValue.serverTimestamp());

        final float finalAmount1 = finalAmount;
        firebaseFirestore.collection("Loans").document("WakulimaLoan").collection("Loaners").add(stringObjectMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()){
                    sendingmessage(fullname,Amount);
                    Toast.makeText(Wakulima_Loan.this, "Application submitted successfully", Toast.LENGTH_SHORT).show();
                    new android.support.v7.app.AlertDialog.Builder(Wakulima_Loan.this)
                            .setTitle("Wakulima loan")
                            .setMessage("Application submitted successfully, You can now give out the loan\n\nAmount due is Ksh: "+ finalAmount1+" .The member is reminded to fully repay the loan within 4 moths")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setCancelable(false)
                            .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    onBackPressed();
                                }
                            }).show();
                }else {
                    String errorMessage = task.getException().getMessage();
                    Toast.makeText(getApplicationContext(),"Server error!..\n"+errorMessage,Toast.LENGTH_LONG);
                }
                progressDialog.dismiss();
            }
        });

    }

    private void dialog() {
        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(this);
        final EditText edittext = new EditText(Wakulima_Loan.this);
        alert.setMessage("Enter ID Number to continue");
        alert.setCancelable(false);
        alert.setTitle("Search..");

        alert.setView(edittext);

        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, int whichButton) {
                //What ever you want to do with the value
                //OR
                final String YouEditTextValue = edittext.getText().toString();

                final ProgressDialog progressDialog = new ProgressDialog(Wakulima_Loan.this);
                progressDialog.setMessage("Searching...");
                progressDialog.show();

                firebaseFirestore.collection("AllMembers").document(YouEditTextValue).get().addOnCompleteListener(
                        new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().exists()) {
                                        String fname = task.getResult().getString("fname");
                                        String lname = task.getResult().getString("lname");
                                        String surname = task.getResult().getString("Other_names");
                                        String membernumber = task.getResult().getString("member_Number");
                                        String phone = task.getResult().getString("phone_number");


                                        //setting them details
                                        mFname.setText(fname);
                                        mLname.setText(lname);
                                        mSurname.setText(surname);
                                        mID.setText(YouEditTextValue);
                                        mMemberno.setText(membernumber);
                                        mPhoneNo.setText(phone);


                                        dialog.dismiss();
                                    } else {
                                        Toast.makeText(Wakulima_Loan.this, "Member does not exist...", Toast.LENGTH_LONG).show();
                                        onBackPressed();
                                    }
                                    progressDialog.dismiss();
                                }
                            }
                        }
                );

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


    public void sendingmessage(String name,String amount){
        final String message=name+" Has been Credited with Wakulima Loan of Ksh "+amount;
        final String title="Tumaini: New Loan";

        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult().exists()){
                        String devicetoken=task.getResult().getString("devicetoken");


                        FirebaseNotificationHelper.initialize(getString(R.string.server_key))
                                .defaultJson(false, getJsonBody(title, message))
                                .setCallBack(Wakulima_Loan.this)
                                .receiverFirebaseToken(devicetoken)
                                .send();

                    }
                }
            }
        });

    }


    private String getJsonBody(String Title, String Message) {

        JSONObject jsonObjectData = new JSONObject();
        try {
            jsonObjectData.put(KEY_TITLE, Title);
            jsonObjectData.put(KEY_TEXT, Message);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonObjectData.toString();
    }

    @Override
    public void success(String s) {

    }

    @Override
    public void fail(String s) {

    }
}
