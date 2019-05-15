package tumaini.tumaini;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
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
import com.google.firebase.firestore.ServerTimestamp;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import tumaini.tumaini.FirebaseNotificationActionHelper.FirebaseNotiCallBack;
import tumaini.tumaini.FirebaseNotificationActionHelper.FirebaseNotificationHelper;
import tumaini.tumaini.Loans.Loan_Advances;

import static tumaini.tumaini.FirebaseNotificationActionHelper.Constants.KEY_TEXT;
import static tumaini.tumaini.FirebaseNotificationActionHelper.Constants.KEY_TITLE;

public class Normal_Loan extends AppCompatActivity implements FirebaseNotiCallBack{
    private EditText mFname,mLname,mSurname,mMemberno,mPhoneNo,mID,mGuar1name,mguar1amount,mGuar2name,mGuar2amount,mAmount,mRates;
    private Button mSubmit;
    private RadioButton mAgree,mDecline;
    private Button mOk,mCancel;
    private EditText mSearch;
    private String user_id;
    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal__loan);

        firebaseFirestore=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        user_id=auth.getCurrentUser().getUid();
        progressDialog=new ProgressDialog(this);
        mAgree=(RadioButton)findViewById(R.id.rad_iagree_normal);
        mDecline=(RadioButton)findViewById(R.id.rad_decline_normal);

        mAmount=(EditText)findViewById(R.id.edt_amount_applying_normal);
        mRates=(EditText)findViewById(R.id.edt_Phone_rate_normal);
        mFname=(EditText)findViewById(R.id.edt_First_name_normal);
        mLname=(EditText)findViewById(R.id.edt_Last_name_normal);
        mSurname=(EditText)findViewById(R.id.edt_Surname_name_normal);
        mMemberno=(EditText)findViewById(R.id.edt_Member_number_normal);
        mPhoneNo=(EditText)findViewById(R.id.edt_Phone_number_normal);
        mID=(EditText)findViewById(R.id.edt_ID_number_normal);
        mGuar1name=(EditText)findViewById(R.id.edt_guarantor1_fullname);
        mguar1amount=(EditText)findViewById(R.id.edt_amount_garanta1);
        mGuar2name=(EditText)findViewById(R.id.edt_guarantor2_fullname);
        mGuar2amount=(EditText)findViewById(R.id.edt_amount_garanta2);


        mSubmit=(Button)findViewById(R.id.Submitnormal);

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String amount=mAmount.getText().toString().trim();
                final String fname=mFname.getText().toString().trim();
                final String lname=mLname.getText().toString().trim();
                final String surname=mSurname.getText().toString().trim();
                final String memberno=mMemberno.getText().toString().trim();
                final String phone=mPhoneNo.getText().toString().trim();
                final String id=mID.getText().toString().trim();
                final String guarantoronename=mGuar1name.getText().toString().trim();
                final String guarantoroneamount=mguar1amount.getText().toString().trim();
                final String guarantortwoname=mGuar2name.getText().toString().trim();
                final String guarantortwoamount=mGuar2amount.getText().toString().trim();
                final String rates=mRates.getText().toString().trim();

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
                }else if (TextUtils.isEmpty(guarantoronename)){
                    mGuar1name.setError("Enter Guarantor full name");
                }else if (TextUtils.isEmpty(guarantoroneamount)){
                    mguar1amount.setError("Enter Amount");
                }else if (TextUtils.isEmpty(guarantortwoname)){
                    mGuar2name.setError("Enter Guarantor full name");
                }else if (TextUtils.isEmpty(guarantortwoamount)){
                    mGuar2amount.setError("Enter Amount");
                }else if (TextUtils.isEmpty(amount)){
                    mAmount.setError("Enter Amount applying for");
                }else if (TextUtils.isEmpty(rates)){
                    mRates.setError("Enter Rates");
                }else {
                    if (mAgree.isChecked()){
                        progressDialog.setMessage("Submitting Application");
                        progressDialog.show();
                        firebaseFirestore.collection("AllMembers").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                    if (task.getResult().exists()){
                                        String shares=task.getResult().getString("shares");
                                        int amnt = Integer.parseInt(shares);
                                        int finam=amnt*3;
                                        int amnt2 = Integer.parseInt(amount);

                                        if (amnt2<=finam){

                                            submittingnormal(null,fname,lname,surname,memberno,phone,id,guarantoronename,guarantoroneamount,guarantortwoname,guarantortwoamount,amount,rates);
                                        }else {
                                            Toast.makeText(Normal_Loan.this, "Amount is too high", Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        }
                                    }
                                }
                            }
                        });

                    }else {
                        Toast.makeText(Normal_Loan.this, "Agree to continue", Toast.LENGTH_LONG).show();
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
                                 String guarantoronefull, String guarantoroneamount, String guarantortwofull,
                                 String guarantortwoamount, final String Amount, String Rates){

        String doc="NormalLoan";
        String Recentpayed="0.00";

        float finalAmount = 0;

        float initialAmount = Float.parseFloat(Amount);
        float initialRate=Float.parseFloat(Rates);
        float interest = (float) (0.01*initialRate*initialAmount);
        finalAmount = interest+initialAmount;

        final String fullname=fname+" "+lname;

        Map<String,Object> stringObjectMap=new HashMap<>();
        stringObjectMap.put("fname",fname);
        stringObjectMap.put("lname",lname);
        stringObjectMap.put("Other_Names",Othernames);
        stringObjectMap.put("Member_Number",Memberno);
        stringObjectMap.put("Phone_Number",Phoneno);
        stringObjectMap.put("id",IdNo);
        stringObjectMap.put("amount",Amount);
        stringObjectMap.put("final_amount",""+finalAmount);
        stringObjectMap.put("Guarantor_one_name",guarantoronefull);
        stringObjectMap.put("Guarantor_one_amount",guarantoroneamount);
        stringObjectMap.put("Guarantor_two_name",guarantortwofull);
        stringObjectMap.put("Guarantor_two_amount",guarantortwoamount);
        stringObjectMap.put("recentpayed",Recentpayed);
        stringObjectMap.put("document",doc);
        stringObjectMap.put("timeStamp", FieldValue.serverTimestamp());


        final float finalAmount1 = finalAmount;
        firebaseFirestore.collection("Loans").document("NormalLoan").collection("Loaners").add(stringObjectMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()){

                    sendingmessage(fullname,Amount);
                    Toast.makeText(Normal_Loan.this, "Application submitted successfully", Toast.LENGTH_SHORT).show();
                    new android.support.v7.app.AlertDialog.Builder(Normal_Loan.this)
                            .setTitle("Normal loan")
                            .setMessage("Application submitted successfully, You can now give out the loan\n\nLoan Due is Ksh: "+ finalAmount1+" .Member is required to repay the loan fully within 18 months")
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
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(Normal_Loan.this);
        alert.setMessage("Enter ID number to continue");
        alert.setCancelable(false);
        alert.setTitle("Search..");

        alert.setView(edittext);

        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, int whichButton) {
                //What ever you want to do with the value
                //OR
                final String YouEditTextValue = edittext.getText().toString();

                final ProgressDialog progressDialog = new ProgressDialog(Normal_Loan.this);
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
                                        Toast.makeText(Normal_Loan.this, "Member does not exist...", Toast.LENGTH_LONG).show();
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
        final String message=name+" Has been Credited with Ksh "+amount;
        final String title="Tumaini: New Loan";

        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult().exists()){
                        String devicetoken=task.getResult().getString("devicetoken");


                        FirebaseNotificationHelper.initialize(getString(R.string.server_key))
                                .defaultJson(false, getJsonBody(title, message))
                                .setCallBack( Normal_Loan.this)
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
