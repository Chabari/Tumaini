package tumaini.tumaini;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import tumaini.tumaini.FinesOthers.Fines;
import tumaini.tumaini.FinesOthers.Members_With_Fines;
import tumaini.tumaini.Funds.Project_Funds;
import tumaini.tumaini.Funds.Risk_Fund;
import tumaini.tumaini.Loans.Loan_Advances;
import tumaini.tumaini.Loans.Members_with_Loans;
import tumaini.tumaini.Loans.Wakulima_Loan;
import tumaini.tumaini.SharesDividends.Dividends;
import tumaini.tumaini.SharesDividends.Shares;

public class MainPanel extends AppCompatActivity {
    private Button mNewmember,mAll;
    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    private List<String> expandableListTitle;
    private HashMap<String, List<String>> expandableListDetail;
    private FirebaseAuth auth;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_panel); expandableListView = (ExpandableListView)findViewById(R.id.expandableListView);

       /* mNewmember=(Button)findViewById(R.id.btn_New_member);
        mAll=(Button)findViewById(R.id.btn_all_members);*/
       auth=FirebaseAuth.getInstance();
        expandableListDetail = ExpandableListDataPump.getData();
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setNestedScrollingEnabled(false);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                String selected = (String) expandableListAdapter.getChild(  groupPosition, childPosition);

                Intent intent;
                switch (selected){
                    case "Add new member":
                    intent = new Intent(MainPanel.this, New_Member.class);
                    startActivity(intent);
                    break;
                    case "Sacco Members":
                    intent = new Intent(MainPanel.this, All_Members.class);
                    startActivity(intent);
                    break;
                    case "Wakulima Loan":
                        intent = new Intent(MainPanel.this, Wakulima_Loan.class);
                        startActivity(intent);
                        break;
                    case "Normal Loan":
                        intent = new Intent(MainPanel.this, Normal_Loan.class);
                        startActivity(intent);
                        break;
                    case "Loan Advance":
                        intent = new Intent(MainPanel.this, Loan_Advances.class);
                        startActivity(intent);
                        break;


                    case "Members with Loan":
                        intent = new Intent(MainPanel.this, Members_with_Loans.class);
                        startActivity(intent);
                        break;
                    case "Shares":
                        intent = new Intent(MainPanel.this, Shares.class);
                        startActivity(intent);
                        break;

                    case "Dividends":
                        intent = new Intent(MainPanel.this, Dividends.class);
                        startActivity(intent);
                        break;
                    case "Risk Fund":
                        intent = new Intent(MainPanel.this, Risk_Fund.class);
                        startActivity(intent);
                        break;
                    case "Project Funds":
                        intent = new Intent(MainPanel.this, Project_Funds.class);
                        startActivity(intent);
                        break;
                    case "Fine":
                        intent = new Intent(MainPanel.this, Fines.class);
                        startActivity(intent);
                        break;
                    case "Members with fine":
                        intent = new Intent(MainPanel.this, Members_With_Fines.class);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.mainpanelmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_logout){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Exiting...");
            builder.setMessage("Are you sure you want to logout from your account?");
// Add the buttons
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    startActivity(new Intent(MainPanel.this,Login.class));
                    auth.signOut();
                    finish();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        }

        return true;
    }
}
