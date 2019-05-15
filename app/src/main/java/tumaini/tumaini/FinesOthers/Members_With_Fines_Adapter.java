package tumaini.tumaini.FinesOthers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import tumaini.tumaini.R;

/**
 * Created by George on 5/12/2019.
 */

public class Members_With_Fines_Adapter extends RecyclerView.Adapter<Members_With_Fines_Adapter.ViewHolder> {
    private List<Members_With_Fines_List>members_with_fines_lists;
    private Context context;
    private FirebaseFirestore firebaseFirestore;
    private ProgressDialog progressDialog;

    public Members_With_Fines_Adapter(List<Members_With_Fines_List> members_with_fines_lists) {
        this.members_with_fines_lists = members_with_fines_lists;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.members_with_fines_layout,parent,false);
        context=parent.getContext();
        firebaseFirestore=FirebaseFirestore.getInstance();
        progressDialog=new ProgressDialog(context);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final String id=members_with_fines_lists.get(position).postID;

        firebaseFirestore.collection("Fines").document("AllFines").collection("Fines").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult().exists()){
                        final String reason=task.getResult().getString("reason");
                        final String amount=task.getResult().getString("amount");

                        firebaseFirestore.collection("AllMembers").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                if (task.isSuccessful()){
                                    if (task.getResult().exists()){
                                        String fullname=task.getResult().getString("fname")+" "+task.getResult().getString("lname");

                                        holder.setingfine(reason,amount,fullname);
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });
        holder.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(context)
                        .setTitle("Confirmation")
                        .setIcon(R.drawable.ic_action_warning)
                        .setMessage("Are you sure you want to clear the member from fines list?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                progressDialog.setMessage("Deleating..");
                                progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
                                progressDialog.show();
                                firebaseFirestore.collection("Fines").document("AllFines").collection("Fines").document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();

                                        }else {
                                            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                        }progressDialog.dismiss();
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

    @Override
    public int getItemCount() {
        return members_with_fines_lists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private TextView mFullname,mAmount,mReason;
        private Button mButton;
        public ViewHolder(View itemView) {
            super(itemView);
            view=itemView;
            mButton=(Button)view.findViewById(R.id.btn_delete_fines_layout);

        }
        public void setingfine(String reason,String amount,String fullname){
            mFullname=(TextView)view.findViewById(R.id.tv_fullname_fine);
            mAmount=(TextView)view.findViewById(R.id.tv_amount_fine);
            mReason=(TextView)view.findViewById(R.id.tv_reason_fine);

            mReason.setText("Reason: "+reason);
            mAmount.setText("Ksh "+amount);
            mFullname.setText(fullname);

        }
    }
}
