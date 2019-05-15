package tumaini.tumaini.Loans;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

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

public class Members_with_Loans extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FirebaseFirestore firebaseFirestore;
    private Members_With_Loan_Adapter members_with_loan_adapter;
    private List<Members_With_Loan_List>members_with_loan_lists;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members_with__loans);

        members_with_loan_lists=new ArrayList<>();
        members_with_loan_adapter = new Members_With_Loan_Adapter(members_with_loan_lists);

        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView=(RecyclerView)findViewById(R.id.recycler_people_with_loan);
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.refresh_memberswithloan);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(members_with_loan_adapter);


        showDialoD();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        swipeRefreshLayout.setRefreshing(false);
                        recyclerView.setAdapter(members_with_loan_adapter);

                    }
                },300);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.loanmenu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search_loan)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                members_with_loan_adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                members_with_loan_adapter.getFilter().filter(query);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        return true;
    }

    public void showDialoD(){
        final Dialog dialog = new Dialog(Members_with_Loans.this);
        dialog.setContentView(R.layout.dialog_loans_member);
        dialog.setTitle("Select Loan Type...");
        dialog.setCancelable(false);
        Button cancel = (Button)dialog.findViewById(R.id.btn_cancel_loans);
        Button ok = (Button)dialog.findViewById(R.id.btn_ok_loans);
        final Spinner loantype = (Spinner) dialog.findViewById(R.id.spinner_loatypes);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                onBackPressed();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (loantype.getSelectedItemPosition()== 0){
                    Toast.makeText(Members_with_Loans.this, "Select Loan Type", Toast.LENGTH_SHORT).show();
                }else if (loantype.getSelectedItemPosition()==1){
                    Query query =  firebaseFirestore.collection("Loans").document("NormalLoan").collection("Loaners")
                            .orderBy("timeStamp", Query.Direction.DESCENDING);
                    query.addSnapshotListener(Members_with_Loans.this, new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            for (DocumentChange doc: queryDocumentSnapshots.getDocumentChanges()){
                                if (doc.getType() == DocumentChange.Type.ADDED) {

                                    String documentiD = doc.getDocument().getId();
                                    Members_With_Loan_List members_with_loan_list = doc.getDocument().toObject(Members_With_Loan_List.class).withId(documentiD);
                                    members_with_loan_lists.add(members_with_loan_list);
                                    members_with_loan_adapter.notifyDataSetChanged();

                                }
                            }
                        }
                    });
                    dialog.dismiss();
                }else if (loantype.getSelectedItemPosition()==2){
                    Query query =  firebaseFirestore.collection("Loans").document("LoanAdvance").collection("Loaners")
                            .orderBy("timeStamp", Query.Direction.DESCENDING);
                    query.addSnapshotListener(Members_with_Loans.this, new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            for (DocumentChange doc: queryDocumentSnapshots.getDocumentChanges()){
                                if (doc.getType() == DocumentChange.Type.ADDED) {



                                    String documentiD = doc.getDocument().getId();
                                    Members_With_Loan_List members_with_loan_list = doc.getDocument().toObject(Members_With_Loan_List.class).withId(documentiD);
                                    members_with_loan_lists.add(members_with_loan_list);
                                    members_with_loan_adapter.notifyDataSetChanged();

                                }
                            }
                        }
                    });

                    dialog.dismiss();
                }else if (loantype.getSelectedItemPosition()==3){
                    Query query =  firebaseFirestore.collection("Loans").document("WakulimaLoan").collection("Loaners")
                            .orderBy("timeStamp", Query.Direction.DESCENDING);
                    query.addSnapshotListener(Members_with_Loans.this, new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            for (DocumentChange doc: queryDocumentSnapshots.getDocumentChanges()){
                                if (doc.getType() == DocumentChange.Type.ADDED) {



                                    String documentiD = doc.getDocument().getId();
                                    Members_With_Loan_List members_with_loan_list = doc.getDocument().toObject(Members_With_Loan_List.class).withId(documentiD);
                                    members_with_loan_lists.add(members_with_loan_list);
                                    members_with_loan_adapter.notifyDataSetChanged();

                                }
                            }
                        }
                    });
                    dialog.dismiss();
                }

            }
        });
        dialog.show();

    }
}
