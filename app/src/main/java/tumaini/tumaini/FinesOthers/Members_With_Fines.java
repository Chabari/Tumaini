package tumaini.tumaini.FinesOthers;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import tumaini.tumaini.All_Members;
import tumaini.tumaini.All_Members_List;
import tumaini.tumaini.Loans.Wakulima_Loan;
import tumaini.tumaini.R;

public class Members_With_Fines extends AppCompatActivity {

    private List<Members_With_Fines_List>members_with_fines_lists;
    private Members_With_Fines_Adapter members_with_fines_adapter;
    private FirebaseFirestore firebaseFirestore;
    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Button mDelete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members__with__fines);

        members_with_fines_lists=new ArrayList<>();
        members_with_fines_adapter=new Members_With_Fines_Adapter(members_with_fines_lists);
        firebaseFirestore=FirebaseFirestore.getInstance();

        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe_memberwithfines);
        recyclerView=(RecyclerView)findViewById(R.id.recycler_member_with_fines);
        mDelete=(Button)findViewById(R.id.btn_delete_fines);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(members_with_fines_adapter);
        progressDialog=new ProgressDialog(this);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        swipeRefreshLayout.setRefreshing(false);
                        recyclerView.setAdapter(members_with_fines_adapter);

                    }
                },300);
            }
        });

        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Members_With_Fines.this)
                        .setTitle("Confirmation")
                        .setMessage("Are you sure you want to clear all the members with fines? ")
                        .setIcon(R.drawable.ic_action_warning)
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                progressDialog.setMessage("Deleating..");
                                progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
                                progressDialog.show();
                                firebaseFirestore.collection("Fines").document("AllFines").delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(Members_With_Fines.this, "Successful", Toast.LENGTH_SHORT).show();
                                        }else {
                                            Toast.makeText(Members_With_Fines.this, "Error", Toast.LENGTH_SHORT).show();
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


        Query query =  firebaseFirestore.collection("Fines").document("AllFines").collection("Fines").orderBy("timeStamp", Query.Direction.DESCENDING);
        query.addSnapshotListener(Members_With_Fines.this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentChange doc: queryDocumentSnapshots.getDocumentChanges()){
                    if (doc.getType() == DocumentChange.Type.ADDED) {

                        final String postID = doc.getDocument().getId();


                        Members_With_Fines_List members_with_fines_list = doc.getDocument().toObject(Members_With_Fines_List.class).withId(postID);

                        members_with_fines_lists.add(members_with_fines_list);

                        members_with_fines_adapter.notifyDataSetChanged();

                    }
                }
            }
        });
    }
}
