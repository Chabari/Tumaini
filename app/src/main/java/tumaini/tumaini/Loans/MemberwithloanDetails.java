package tumaini.tumaini.Loans;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import tumaini.tumaini.Normal_Loan;
import tumaini.tumaini.R;

public class MemberwithloanDetails extends AppCompatActivity {

    private TextView mDetails,mActualloan,mBalance,mRecent;
    private FirebaseFirestore firebaseFirestore;
    private Button mPayment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memberwithloan_details);

        mActualloan=(TextView)findViewById(R.id.acualloan_details);
        mBalance=(TextView)findViewById(R.id.balance_details);
        mDetails=(TextView)findViewById(R.id.member_loan_details);
        mRecent=(TextView)findViewById(R.id.Recentpayed_details);
        firebaseFirestore=FirebaseFirestore.getInstance();
        mPayment=(Button) findViewById(R.id.edit_shares_details);

        String id=getIntent().getExtras().getString("id");
        final String amount=getIntent().getExtras().getString("amount");
        final String doc=getIntent().getExtras().getString("document");
        final String docid=getIntent().getExtras().getString("docid");

        firebaseFirestore.collection("AllMembers").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()){
                    if (task.getResult().exists()){

                        final String fullname=task.getResult().getString("fname")+" "+task.getResult().getString("lname");
                        String phone=task.getResult().getString("phone_number");
                        String memberno=task.getResult().getString("member_Number");
                        String idno=task.getResult().getString("id_number");
                        String postal=task.getResult().getString("postal_address");
                        String gender=task.getResult().getString("gender");
                        String Shares=task.getResult().getString("shares");
                        String jobtitle=task.getResult().getString("job_title");

                        firebaseFirestore.collection("Loans").document(doc).collection("Loaners").document(docid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                if (task.isSuccessful()){
                                    if (task.getResult().exists()){
                                        String recentpayed=task.getResult().getString("recentpayed");
                                        final String finalamount=task.getResult().getString("final_amount");

                                        mBalance.setText("Loan Balance: "+finalamount);
                                        mRecent.setText("Recent Payed: "+recentpayed);

                                        mPayment.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                dialog(finalamount,doc,docid,fullname);


                                            }
                                        });

                                    }
                                }
                            }
                        });

                        setTitle(fullname);

                        mDetails.setText("Fullname: "+fullname+"\nPhone number: "+phone+"\nId number: "+idno+"\nMember number: "+memberno+"\ngender: "
                                +gender+"\npostal address: "+postal+"\njob title: "+jobtitle+"\nShares: "+Shares);
                        mActualloan.setText("Actual loan: "+amount);

                    }
                }

            }
        });

    }

    private void dialog(final String finalamount, final String Doc, final String DocId, final String fullname) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(MemberwithloanDetails.this);
        alert.setMessage("Enter Amount payed");
        alert.setCancelable(false);
        alert.setTitle("Loan Repayment..");

        alert.setView(edittext);

        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, int whichButton) {
                //What ever you want to do with the value
                //OR
                final String Recent = edittext.getText().toString();

                float finalpay = 0;

                float initialAmount = Float.parseFloat(finalamount);
                float recent = Float.parseFloat(Recent);
                finalpay = (initialAmount - recent);

                final ProgressDialog progressDialog = new ProgressDialog(MemberwithloanDetails.this);
                progressDialog.setMessage("Please wait...");
                progressDialog.show();

                Map<String, Object> stringObjectMap = new HashMap<>();
                stringObjectMap.put("final_amount", "" + finalpay);
                stringObjectMap.put("recentpayed", "" + recent);

                final float finalPay = finalpay;
                firebaseFirestore.collection("Loans").document(Doc).collection("Loaners").document(DocId).update(stringObjectMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {


                            Toast.makeText(MemberwithloanDetails.this, "Successful", Toast.LENGTH_LONG).show();

                            if (finalPay <=0){

                                new android.support.v7.app.AlertDialog.Builder(MemberwithloanDetails.this)
                                        .setTitle("Confirmation")
                                        .setMessage(fullname+" Has completed paying loan.")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                firebaseFirestore.collection("Loans").document(Doc).collection("Loaners").document(DocId).delete();
                                                dialogInterface.dismiss();
                                                onBackPressed();
                                            }
                                        }).setCancelable(false)
                                        .show();

                            }
                            else {

                                new android.support.v7.app.AlertDialog.Builder(MemberwithloanDetails.this)
                                        .setTitle("Confirmation")
                                        .setMessage(fullname+" Has a balance of: "+finalPay)
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                                onBackPressed();
                                            }
                                        }).setCancelable(false)
                                        .show();

                            }

                        } else {
                            Toast.makeText(MemberwithloanDetails.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                        progressDialog.dismiss();

                    }
                });

            }
        });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // what ever you want to do with No option.
                        dialog.dismiss();
                    }
                });

                alert.show();

            }
}
