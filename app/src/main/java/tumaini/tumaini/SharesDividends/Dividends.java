package tumaini.tumaini.SharesDividends;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import tumaini.tumaini.FinesOthers.Members_With_Fines;
import tumaini.tumaini.R;

public class Dividends extends AppCompatActivity {

    private TextInputEditText mAmount;
    private Button mSubmit;
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressDialog progressDialog;
    private List<Divident_List>divident_lists;
    private LinearLayout linearLayout;
    private Button Delete;
    private CardView mCard;
    private Dividents_Adapter dividents_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dividends);

        divident_lists=new ArrayList<>();
        dividents_adapter=new Dividents_Adapter(divident_lists);
        recyclerView=(RecyclerView)findViewById(R.id.recycler_devidends);
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe_dividends);
        firebaseFirestore=FirebaseFirestore.getInstance();
        mAmount=(TextInputEditText)findViewById(R.id.edt_dividents);
        mCard=(CardView)findViewById(R.id.card_dividends);
        mSubmit=(Button)findViewById(R.id.btn_submit_devidents);
        Delete=(Button)findViewById(R.id.btn_delete_dividends);
        linearLayout=(LinearLayout)findViewById(R.id.linear_dividends);
        progressDialog=new ProgressDialog(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(dividents_adapter);

        chekingcardview();
        chekinglayout();

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount=mAmount.getText().toString().trim();
                if (TextUtils.isEmpty(amount)){
                    mAmount.setError("Enter dividends");
                }else {
                    progressDialog.setMessage("Submitting dividends");
                    progressDialog.show();
                    submitdev(amount);
                    mAmount.setText("");
                }
            }
        });

        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Dividends.this)
                        .setTitle("Confirmation")
                        .setMessage("Are you sure you want to clear all the Dividends? ")
                        .setIcon(R.drawable.ic_action_warning)
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                progressDialog.setMessage("Deleating..");
                                progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
                                progressDialog.show();
                                firebaseFirestore.collection("Profit").document("Dividends").delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(Dividends.this, "Successful", Toast.LENGTH_SHORT).show();
                                        }else {
                                            Toast.makeText(Dividends.this, "Error", Toast.LENGTH_SHORT).show();
                                        }
                                        progressDialog.dismiss();
                                    }
                                });
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        swipeRefreshLayout.setRefreshing(false);
                        recyclerView.setAdapter(dividents_adapter);

                    }
                },300);
            }
        });


        Query query =  firebaseFirestore.collection("AllMembers").orderBy("timeStamp", Query.Direction.DESCENDING);
        query.addSnapshotListener(Dividends.this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentChange doc: queryDocumentSnapshots.getDocumentChanges()){
                    if (doc.getType() == DocumentChange.Type.ADDED) {

                        final String postID = doc.getDocument().getId();


                        Divident_List divident_list = doc.getDocument().toObject(Divident_List.class).withId(postID);

                        divident_lists.add(divident_list);

                        dividents_adapter.notifyDataSetChanged();

                    }
                }
            }
        });


    }


    public void submitdev(String amount){

        Map<String,Object> stringObjectMap=new HashMap<>();
        stringObjectMap.put("dividends",amount);
        stringObjectMap.put("timeStamp", FieldValue.serverTimestamp());
        firebaseFirestore.collection("Profit").document("Dividends").set(stringObjectMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){
                    Toast.makeText(Dividends.this, "Successful", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(Dividends.this, "Something went wrong..", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();

            }
        });
    }

    public void chekingcardview(){
        firebaseFirestore.collection("Profit").document("Dividends").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()){
                    if (task.getResult().exists()){

                        mCard.setVisibility(View.GONE);

                    }
                }

            }
        });


    }

    public void chekinglayout(){

        firebaseFirestore.collection("Profit").document("Dividends").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()){
                    if (task.getResult().exists()){

                        linearLayout.setVisibility(View.VISIBLE);

                    }
                }

            }
        });

    }

}
