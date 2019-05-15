package tumaini.tumaini;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MemberDetails extends AppCompatActivity {
    private String id;
    private TextView mEdit,mShares,mDetails;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_details);

        id=getIntent().getExtras().getString("id");

        mEdit=(TextView)findViewById(R.id.edit_shares_details);
        mShares=(TextView)findViewById(R.id.shares_details);
        mDetails=(TextView)findViewById(R.id.member_details);

        firebaseFirestore=FirebaseFirestore.getInstance();

        firebaseFirestore.collection("AllMembers").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult().exists()){
                        String fname=task.getResult().getString("fname");
                        String lname=task.getResult().getString("lname");
                        String othername=task.getResult().getString("Other_names");
                        String phone=task.getResult().getString("phone_number");
                        String DoB=task.getResult().getString("DoB");
                        String memberno=task.getResult().getString("member_Number");
                        String email=task.getResult().getString("email");
                        String idno=task.getResult().getString("id_number");
                        String postal=task.getResult().getString("postal_address");
                        String gender=task.getResult().getString("gender");
                        String Shares=task.getResult().getString("shares");
                        String jobtitle=task.getResult().getString("job_title");
                        String location=task.getResult().getString("location_of_work");
                        String employer=task.getResult().getString("employer");
                        String fullname=lname+" "+fname+" "+othername;

                        setTitle(lname+" "+fname);

                        mDetails.setText("Fullname: "+fullname+"\nPhone number: "+phone+"\nId number: "+idno+"\nMember number: "+memberno+"\nemail: "+email+"\ngender: "
                        +gender+"\nDate of birth: "+DoB+"\npostal address: "+postal+"\njob title: "+jobtitle+"\nLocation of work: "+location+"\nEmployer: "+employer);
                        mShares.setText("\t"+Shares);

                    }
                }
            }
        });



    }
}
