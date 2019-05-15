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

public class Risk_Fund extends AppCompatActivity {

    private List<Risk_Fund_List>risk_fund_lists;
    private FirebaseFirestore firebaseFirestore;
    private Risk_Fund_Adapter risk_fund_adapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_risk__fund);

        risk_fund_lists=new ArrayList<>();
        risk_fund_adapter=new Risk_Fund_Adapter(risk_fund_lists);
        firebaseFirestore=FirebaseFirestore.getInstance();
        recyclerView=(RecyclerView)findViewById(R.id.recycler_risk_funds);
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.refresh_risk_funds);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(risk_fund_adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        swipeRefreshLayout.setRefreshing(false);
                        recyclerView.setAdapter(risk_fund_adapter);

                    }
                },300);
            }
        });

        Query query =  firebaseFirestore.collection("AllMembers").orderBy("timeStamp", Query.Direction.DESCENDING);
        query.addSnapshotListener(Risk_Fund.this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentChange doc: queryDocumentSnapshots.getDocumentChanges()){
                    if (doc.getType() == DocumentChange.Type.ADDED) {

                        final String postID = doc.getDocument().getId();


                        Risk_Fund_List risk_fund_list = doc.getDocument().toObject(Risk_Fund_List.class).withId(postID);

                        risk_fund_lists.add(risk_fund_list);

                        risk_fund_adapter.notifyDataSetChanged();

                    }
                }
            }
        });

    }
}
