package tumaini.tumaini;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
    private Button mAlready,mSignup;
    private TextInputEditText mEmail,mPassword,mCpassword;
    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    private String user_id;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.Layout_signup);
        AnimationDrawable animationDrawable = (AnimationDrawable) linearLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();

        auth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        firebaseFirestore=FirebaseFirestore.getInstance();
        mAlready=(Button)findViewById(R.id.btn_already_have_account);
        mSignup=(Button)findViewById(R.id.btn_create_account);
        mEmail=(TextInputEditText)findViewById(R.id.edt_Email_SignUp);
        mPassword=(TextInputEditText)findViewById(R.id.edt_password_SignUp);
        mCpassword=(TextInputEditText)findViewById(R.id.edt_Cpassword_SignUp);

        mAlready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=mEmail.getText().toString().trim();
                String password=mPassword.getText().toString().trim();
                String cpassword=mCpassword.getText().toString().trim();
                if (TextUtils.isEmpty(email)){
                    mEmail.setError("Enter Email.");
                }else if (TextUtils.isEmpty(password)){
                    mPassword.setError("Enter Password..");
                }else if (TextUtils.isEmpty(cpassword)){
                    mCpassword.setError("Confirm your password..");
                }else if (!password.equals(cpassword)){

                    mPassword.setError("Passwords don't match...!");
                    mCpassword.setError("Passwords don't match...!");

                }
                else {
                    progressDialog.setMessage("Creating Account..");
                    progressDialog.show();
                    signingup(email,password);

                }
            }
        });
    }
    public void signingup(final String email, final String password){
        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            user_id=auth.getCurrentUser().getUid();
                            String devicetoken = FirebaseInstanceId.getInstance().getToken();
                            Map<String,Object>stringObjectMap=new HashMap<>();
                            stringObjectMap.put("devicetoken",devicetoken);
                            firebaseFirestore.collection("Users").document(user_id).set(stringObjectMap);

                            startActivity(new Intent(SignUp.this,MainPanel.class));
                            finish();
                        }else {
                            Toast.makeText(SignUp.this,task.getException().toString(),Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
            }
        });
    }
}
