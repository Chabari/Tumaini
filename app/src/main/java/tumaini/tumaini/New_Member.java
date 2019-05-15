package tumaini.tumaini;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

import static tumaini.tumaini.FirebaseNotificationActionHelper.Constants.KEY_TEXT;
import static tumaini.tumaini.FirebaseNotificationActionHelper.Constants.KEY_TITLE;

public class New_Member extends AppCompatActivity implements FirebaseNotiCallBack {
    private EditText mOthernames,mDateofBirt,mPhone,mPostalAddress,mEmployer,mMembernumber,
            mLocationofWork,mJobTitle,mlname,mfname,idNumber,mEmail,mShares;
    private RadioButton mmale,mfemale,mIagree,mDecline;
    private Button mSubmit;
    private FirebaseAuth auth;
    private String user_id;
    private ProgressDialog progressDialog;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new__member);

        firebaseFirestore=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        user_id=auth.getCurrentUser().getUid();
        progressDialog=new ProgressDialog(this);

        mOthernames=(EditText)findViewById(R.id.edtOtherNames);
        mDateofBirt=(EditText)findViewById(R.id.edt_date_of_birth);
        mPhone=(EditText)findViewById(R.id.edt_telephone1);
        mShares = (EditText)findViewById(R.id.edt_shares);
        mPostalAddress=(EditText)findViewById(R.id.edtPostAdress);
        mEmployer=(EditText)findViewById(R.id.edtemployer);
        mLocationofWork=(EditText)findViewById(R.id.edt_location_of_workstation);
        mJobTitle=(EditText)findViewById(R.id.edt_job_title);
        mlname=(EditText)findViewById(R.id.edtlnameMembership);
        mfname=(EditText)findViewById(R.id.edtfnameMembership);
        mEmail=(EditText)findViewById(R.id.edtemailMembership);
        idNumber=(EditText)findViewById(R.id.edtNationalidMembership);
        mMembernumber=(EditText)findViewById(R.id.edt_membership_number);

        //Radiobuttons
        mmale=(RadioButton)findViewById(R.id.radio_male);
        mfemale=(RadioButton)findViewById(R.id.radio_female);
        mIagree=(RadioButton)findViewById(R.id.rad_iagree);
        mDecline=(RadioButton)findViewById(R.id.rad_decline);

        mSubmit=(Button)findViewById(R.id.Submitmembership);

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email=mEmail.getText().toString().trim();
                String lname=mlname.getText().toString().trim();
                String fname=mfname.getText().toString().trim();
                String idnumber=idNumber.getText().toString().trim();
                String othernames=mOthernames.getText().toString().trim();
                String dateofbirth=mDateofBirt.getText().toString().trim();
                String phone=mPhone.getText().toString().trim();
                String postaladdress=mPostalAddress.getText().toString().trim();
                String employer=mEmployer.getText().toString().trim();
                String locationofwork=mLocationofWork.getText().toString().trim();
                String jobtitle=mJobTitle.getText().toString().trim();
                String memberno=mMembernumber.getText().toString().trim();
                String shares=mShares.getText().toString().trim();

                //radios

                String gender=null;
                if (mmale.isChecked()){
                    gender="male";
                }
                if (mfemale.isChecked()){
                    gender="female";
                }

                //ope

                if (TextUtils.isEmpty(fname)){
                    Toast.makeText(New_Member.this, "Enter first name", Toast.LENGTH_SHORT).show();

                }else if (TextUtils.isEmpty(lname)){
                    Toast.makeText(New_Member.this, "Enter Last Name", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(othernames)){
                    Toast.makeText(New_Member.this, "Enter Other Names", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(shares)){
                    Toast.makeText(New_Member.this, "Enter Member Shares", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(idnumber)){
                    Toast.makeText(New_Member.this, "Enter ID Number", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(dateofbirth)){
                    Toast.makeText(New_Member.this, "Enter date of birth", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(memberno)){
                    Toast.makeText(New_Member.this, "Enter Member number", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(phone)){
                    Toast.makeText(New_Member.this, "Enter telephone number", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(email)){
                    Toast.makeText(New_Member.this, "Enter email", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(postaladdress)){
                    Toast.makeText(New_Member.this, "Enter postal address", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(employer)){
                    Toast.makeText(New_Member.this, "Enter Employer", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(locationofwork)){
                    Toast.makeText(New_Member.this, "Enter Location of work", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(jobtitle)){
                    Toast.makeText(New_Member.this, "Enter Job title", Toast.LENGTH_SHORT).show();
                } else {
                    if(mIagree.isChecked()){
                        progressDialog.setMessage("Submitting your registration details..");
                        progressDialog.show();
                        fireupload(null,othernames,dateofbirth,phone,postaladdress,employer,locationofwork,
                                jobtitle,gender,fname,lname,idnumber,email,memberno,shares);
                    }else {
                        Toast.makeText(New_Member.this, "Agree to continue", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

    }
    private void fireupload(@NonNull Task<UploadTask.TaskSnapshot> task , String othernames, String dateofbirth, String phone_number, String postaladdress,
                            String employer, String locationofwork, String jobtitle, String gender, String FName, String LName, String Id, String Email, String member_Number, final String shares) {

        final String fullname=FName+" "+LName;
        Map<String, Object> stringMap = new HashMap<>();
        stringMap.put("Other_names", othernames);
        stringMap.put("DoB",dateofbirth);
        stringMap.put("phone_number",phone_number);
        stringMap.put("postal_address",postaladdress);
        stringMap.put("employer",employer);
        stringMap.put("location_of_work",locationofwork);
        stringMap.put("job_title",jobtitle);
        stringMap.put("gender",gender);
        stringMap.put("fname",FName);
        stringMap.put("lname",LName);
        stringMap.put("shares",shares);
        stringMap.put("email",Email);
        stringMap.put("member_Number",member_Number);
        stringMap.put("id_number",Id);
        stringMap.put("timeStamp", FieldValue.serverTimestamp());


        firebaseFirestore.collection("AllMembers").document(Id).set(stringMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    totalShares(shares);
                    sendingmessage(fullname);
                    Toast.makeText(New_Member.this, "Details submitted successfully", Toast.LENGTH_SHORT).show();
                    new android.support.v7.app.AlertDialog.Builder(New_Member.this)
                            .setTitle("New Member")
                            .setMessage("Member successfully added")
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

    private void totalShares(final String shares){

        //getting shaes (initial total shares)
        firebaseFirestore.collection("AllMembers").document("TotalShares").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            if (task.getResult().exists()){
                                String totalshares=task.getResult().getString("total_shares");

                                float finalShares = 0;

                                float initialShares = Float.parseFloat(totalshares);
                                float Shares=Float.parseFloat(shares);
                                finalShares = initialShares+Shares;

                                Map<String,Object>stringObjectMap=new HashMap<>();
                                stringObjectMap.put("total_shares",""+finalShares);

                                firebaseFirestore.collection("AllMembers").document("TotalShares").set(stringObjectMap);
                            }else {
                                Map<String,Object>stringObjectMap=new HashMap<>();
                                stringObjectMap.put("total_shares",shares);
                                firebaseFirestore.collection("AllMembers").document("TotalShares").set(stringObjectMap);

                            }
                        }
                    }
                });
    }

    public void sendingmessage(String name){
        final String message=name+" Has been Added to the sacco";
        final String title="Tumaini: New Member";

        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult().exists()){
                        String devicetoken=task.getResult().getString("devicetoken");


                        FirebaseNotificationHelper.initialize(getString(R.string.server_key))
                                .defaultJson(false, getJsonBody(title, message))
                                .setCallBack(New_Member.this)
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
