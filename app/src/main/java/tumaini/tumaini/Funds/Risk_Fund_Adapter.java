package tumaini.tumaini.Funds;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import tumaini.tumaini.R;

/**
 * Created by George on 5/11/2019.
 */

public class Risk_Fund_Adapter extends RecyclerView.Adapter<Risk_Fund_Adapter.ViewHolder> {

    private FirebaseFirestore firebaseFirestore;
    private List<Risk_Fund_List>risk_fund_lists;
    private Context context;
    private ProgressDialog progressDialog;

    public Risk_Fund_Adapter(List<Risk_Fund_List> risk_fund_lists) {
        this.risk_fund_lists = risk_fund_lists;
    }

    @Override
    public Risk_Fund_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.risk_fund_layout,parent,false);
        context=parent.getContext();
        firebaseFirestore=FirebaseFirestore.getInstance();
        progressDialog=new ProgressDialog(context);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final Risk_Fund_Adapter.ViewHolder holder, int position) {

        final String id=risk_fund_lists.get(position).postID;

        firebaseFirestore.collection("AllMembers").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult().exists()){
                        final String fullname=task.getResult().getString("fname")+" "+task.getResult().getString("lname")+" "+task.getResult().getString("Other_names");

                        firebaseFirestore.collection("Allfunds").document("RiskFund").collection("Funds").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                if (task.isSuccessful()){
                                    if (task.getResult().exists()){

                                        String amount=task.getResult().getString("amount");
                                        holder.mAmountPaid.setVisibility(View.VISIBLE);
                                        holder.mContinue.setVisibility(View.GONE);
                                        holder.submiting(amount,fullname);
                                    }else {
                                        String Amount="0";
                                        holder.submiting(Amount,fullname);
                                    }
                                }
                            }
                        });

                        holder.mContinue.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new AlertDialog.Builder(context)
                                        .setTitle("Confirmation")
                                        .setMessage("Are you sure "+fullname+" has paid Ksh 30 for Risk fund?")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(final DialogInterface dialogInterface, int i) {
                                                progressDialog.setMessage("Please wait..");
                                                progressDialog.show();

                                                int amount = 30;

                                                Map<String,Object> stringObjectMap=new HashMap<>();
                                                stringObjectMap.put("amount",""+amount);
                                                stringObjectMap.put("timeStamp", FieldValue.serverTimestamp());
                                                firebaseFirestore.collection("Allfunds").document("RiskFund").collection("Funds").document(id).set(stringObjectMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            Toast.makeText(context, "Successfully submitted", Toast.LENGTH_SHORT).show();

                                                        }else {
                                                            Toast.makeText(context, "Something went Wrong", Toast.LENGTH_SHORT).show();
                                                        }
                                                        progressDialog.dismiss();
                                                    }
                                                });
                                            }
                                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                }).setCancelable(false).show();
                            }
                        });
                    }
                }
            }
        });

       /* Query query=firebaseFirestore.collection("Allfunds").document("RiskFund").collection("Funds");
        query.addSnapshotListener( new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentChange documentChange: queryDocumentSnapshots.getDocumentChanges()){
                    if (documentChange.getType()== DocumentChange.Type.ADDED){
                        String idno=documentChange.getDocument().getId();
                        if (id==idno){
                            holder.mContinue.setVisibility(View.INVISIBLE);
                        }
                    }
                }

            }
        });*/

        firebaseFirestore.collection("Allfunds").document("RiskFund").collection("Funds").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult().exists()){
                        Timestamp date =  task.getResult().getTimestamp("timeStamp");
                        Calendar current = Calendar.getInstance();

                        long currentDateTime = current.getTimeInMillis();

                        long diff = currentDateTime-(date.getSeconds()*1000);

                        long secs = diff/1000;
                        long min = secs/60;
                        long hr = min/60;
                        long day = hr/24;

                        if (day>=30){
                            firebaseFirestore.collection("Allfunds").document("RiskFund").collection("Funds").document(id).delete();
                            //holder.mContinue.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return risk_fund_lists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private Button mContinue;
        private TextView mAmountPaid,mFullName;
        public ViewHolder(View itemView) {
            super(itemView);
            view=itemView;

            mAmountPaid=(TextView)view.findViewById(R.id.tv_amount_risk_fund_layout);
            mContinue=(Button) view.findViewById(R.id.btn_risk_fund_layout);
            mFullName=(TextView) view.findViewById(R.id.tv_fullname_risk_fund_layout);
        }
        public void submiting(String amount,String name){

            mAmountPaid=(TextView)view.findViewById(R.id.tv_amount_risk_fund_layout);
            mFullName=(TextView) view.findViewById(R.id.tv_fullname_risk_fund_layout);

            mFullName.setText(name);
            mAmountPaid.setText("Paid: "+amount);


        }
    }


}
