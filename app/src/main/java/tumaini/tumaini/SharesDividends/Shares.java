package tumaini.tumaini.SharesDividends;

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

import tumaini.tumaini.All_Members;
import tumaini.tumaini.All_Members_Adapter;
import tumaini.tumaini.All_Members_List;
import tumaini.tumaini.R;

public class Shares extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Shares_List>shares_lists;
    private Shares_adapter shares_adapter;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shares);


        shares_lists=new ArrayList<>();
        shares_adapter=new Shares_adapter(shares_lists);
        firebaseFirestore=FirebaseFirestore.getInstance();
        recyclerView=(RecyclerView)findViewById(R.id.recycler_allShares);
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.refresh_shares);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(shares_adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        swipeRefreshLayout.setRefreshing(false);
                        recyclerView.setAdapter(shares_adapter);

                    }
                },300);
            }
        });


        Query query =  firebaseFirestore.collection("AllMembers").orderBy("timeStamp", Query.Direction.DESCENDING);
        query.addSnapshotListener(Shares.this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentChange doc: queryDocumentSnapshots.getDocumentChanges()){
                    if (doc.getType() == DocumentChange.Type.ADDED) {

                        final String postID = doc.getDocument().getId();


                        Shares_List shares_list = doc.getDocument().toObject(Shares_List.class).withId(postID);

                        shares_lists.add(shares_list);

                        shares_adapter.notifyDataSetChanged();

                    }
                }
            }
        });
    }
}
