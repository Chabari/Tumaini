package tumaini.tumaini.SharesDividends;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tumaini.tumaini.R;

/**
 * Created by George on 5/10/2019.
 */

public class Dividents_Adapter extends RecyclerView.Adapter<Dividents_Adapter.ViewHolder> {
    private Context context;
    private FirebaseFirestore firebaseFirestore;
    private List<Divident_List>divident_lists;
    private ProgressDialog progressDialog;

    public Dividents_Adapter(List<Divident_List> divident_lists) {
        this.divident_lists = divident_lists;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.dividents_layout,parent,false);
        context=parent.getContext();
        firebaseFirestore=FirebaseFirestore.getInstance();
        progressDialog=new ProgressDialog(context);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final String id=divident_lists.get(position).getId_number();


        firebaseFirestore.collection("AllMembers").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()){
                    if (task.getResult().exists()){
                        final String fullname=task.getResult().getString("fname")+" "+task.getResult().getString("lname");
                        final String idno=task.getResult().getString("id_number");
                        final String Shares=task.getResult().getString("shares");

                        firebaseFirestore.collection("AllMembers").document("TotalShares").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                    if (task.getResult().exists()){
                                        final String totalamount=task.getResult().getString("total_shares");

                                        firebaseFirestore.collection("Profit").document("Dividends").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                if (task.isSuccessful()){
                                                    if (task.getResult().exists()){
                                                        String profit=task.getResult().getString("dividends");

                                                        float memberShare = 0;

                                                        float initialShares = Float.parseFloat(totalamount);
                                                        float profitgot=Float.parseFloat(profit);
                                                        float myShares=Float.parseFloat(Shares);
                                                        memberShare =( myShares/initialShares)*profitgot;

                                                        holder.setingdetails(memberShare,fullname,idno);
                                                    }else {
                                                        holder.mCard.setVisibility(View.GONE);
                                                    }
                                                }
                                            }
                                        });

                                    }else {
                                        holder.mCard.setVisibility(View.GONE);

                                    }
                                }
                            }
                        });
                    }else {
                        holder.mCard.setVisibility(View.GONE);

                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return divident_lists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private Button mDividents;
        private CardView mCard;
        private TextView mId,mFullname;
        public ViewHolder(View itemView) {
            super(itemView);
            view=itemView;
            mCard=(CardView)view.findViewById(R.id.card_Dev_layout);
            mDividents=(Button)view.findViewById(R.id.btn_all_dividents_display);
            mFullname=(TextView)view.findViewById(R.id.tv_fullname_dividents_display);
            mId=(TextView)view.findViewById(R.id.tv_idnumber_dividents_display);
        }
        public void setingdetails(float Div, String name, String id){
            mDividents=(Button)view.findViewById(R.id.btn_all_dividents_display);
            mFullname=(TextView)view.findViewById(R.id.tv_fullname_dividents_display);
            mId=(TextView)view.findViewById(R.id.tv_idnumber_dividents_display);

            mId.setText(id);
            mDividents.setText("Dividends: Ksh "+Div);
            mFullname.setText(name);

        }

    }
}
