package tumaini.tumaini.Funds;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import tumaini.tumaini.R;
import tumaini.tumaini.SharesDividends.Shares;
import tumaini.tumaini.SharesDividends.Shares_List;
import tumaini.tumaini.SharesDividends.Shares_adapter;

public class Project_Funds extends AppCompatActivity {


    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Project_Fund_List> project_fund_lists;
    private Project_Fund_Adapter project_fund_adapter;
    private FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project__funds);

        project_fund_lists=new ArrayList<>();
        project_fund_adapter=new Project_Fund_Adapter(project_fund_lists);
        firebaseFirestore=FirebaseFirestore.getInstance();
        recyclerView=(RecyclerView)findViewById(R.id.recycler_project_funds);
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.refresh_project_funds);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(project_fund_adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        swipeRefreshLayout.setRefreshing(false);
                        recyclerView.setAdapter(project_fund_adapter);

                    }
                },300);
            }
        });



        Query query =  firebaseFirestore.collection("AllMembers").orderBy("timeStamp", Query.Direction.DESCENDING);
        query.addSnapshotListener(Project_Funds.this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentChange doc: queryDocumentSnapshots.getDocumentChanges()){
                    if (doc.getType() == DocumentChange.Type.ADDED) {

                        final String postID = doc.getDocument().getId();


                        Project_Fund_List project_fund_list = doc.getDocument().toObject(Project_Fund_List.class).withId(postID);

                        project_fund_lists.add(project_fund_list);

                        project_fund_adapter.notifyDataSetChanged();

                    }
                }
            }
        });
    }
}
