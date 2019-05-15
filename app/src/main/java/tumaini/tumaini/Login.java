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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    private Button mLogin,mForgot,mSignup;
    private TextInputEditText mEmail,mPassword;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear_layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) linearLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();

        auth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        progressDialog=new ProgressDialog(this);
        mForgot=(Button)findViewById(R.id.btn_forgot_password);
        mLogin=(Button)findViewById(R.id.btn_Login);
        mSignup=(Button)findViewById(R.id.btn_Signup_here);
        mEmail=(TextInputEditText)findViewById(R.id.edt_Email_login);
        mPassword=(TextInputEditText)findViewById(R.id.edt_password_login);

        mForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,ForgotPassword.class));
            }
        });
        mSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,SignUp.class));
            }
        });

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=mEmail.getText().toString().trim();
                String password=mPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    mEmail.setError("Please input your email!!");
                }else if (TextUtils.isEmpty(password)){
                    mPassword.setError("Enter your password!!");
                }else {
                    progressDialog.setMessage("Authenticating...");
                    progressDialog.show();
                    logingin(email,password);

                }
            }
        });

    }
    public void logingin(final String email, final String password){
        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){


                           String user_id=auth.getCurrentUser().getUid();
                            String devicetoken = FirebaseInstanceId.getInstance().getToken();
                            Map<String,Object> stringObjectMap=new HashMap<>();
                            stringObjectMap.put("devicetoken",devicetoken);
                            firebaseFirestore.collection("Users").document(user_id).update(stringObjectMap);

                            startActivity(new Intent(Login.this,MainPanel.class));
                            
                        }else {
                            Toast.makeText(Login.this,task.getException().toString(),Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser !=null){
            startActivity(new Intent(Login.this, MainPanel.class));
            finish();
        }
    }
}
