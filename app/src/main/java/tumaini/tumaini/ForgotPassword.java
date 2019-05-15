package tumaini.tumaini;

import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private FirebaseAuth auth;
    private ProgressDialog progressDialog;
    private Button mReset;
    private TextInputEditText mEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear_forgot);
        AnimationDrawable animationDrawable = (AnimationDrawable) linearLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();

        auth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        mReset=(Button)findViewById(R.id.btn_reset);
        mEmail=(TextInputEditText)findViewById(R.id.edt_Email_reset);


        mReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = mEmail.getText().toString().trim();
                if ((!TextUtils.isEmpty(email))){

                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ForgotPassword.this);
                    builder.setTitle("Password resetting...");
                    builder.setMessage("A link will be sent to this email "+email+"\nProceed?");
// Add the buttons
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            progressDialog.setMessage("Sending reset link...");
                            progressDialog.show();
                            auth.sendPasswordResetEmail(email)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                progressDialog.dismiss();
                                                Toast.makeText(ForgotPassword.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                progressDialog.dismiss();
                                                Toast.makeText(ForgotPassword.this, "Failed to send reset email!. Check your Internet connection", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
                    android.app.AlertDialog dialog = builder.create();
                    dialog.show();
                }else {
                    mEmail.setError("Enter valid email address..!");
                }
            }
        });

    }
}
