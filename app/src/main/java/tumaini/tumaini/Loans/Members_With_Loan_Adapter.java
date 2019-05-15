package tumaini.tumaini.Loans;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import tumaini.tumaini.All_Members_List;
import tumaini.tumaini.R;

/**
 * Created by George on 5/6/2019.
 */

public class Members_With_Loan_Adapter extends RecyclerView.Adapter<Members_With_Loan_Adapter.ViewHolder> {


    private Context context;
    private FirebaseFirestore firebaseFirestore;
    private List<Members_With_Loan_List>members_with_loan_lists;
    private List<Members_With_Loan_List>members_with_loan_listsfilterd;

    public Members_With_Loan_Adapter(List<Members_With_Loan_List> members_with_loan_lists) {
        this.members_with_loan_lists = members_with_loan_lists;
        this.members_with_loan_listsfilterd = members_with_loan_lists;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.people_with_loan,parent,false);
        context=parent.getContext();
        firebaseFirestore=FirebaseFirestore.getInstance();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final String id=members_with_loan_listsfilterd.get(position).getId();
        final String finalAmount=members_with_loan_listsfilterd.get(position).getFinal_amount();
        final String amount=members_with_loan_listsfilterd.get(position).getAmount();
        final String document=members_with_loan_listsfilterd.get(position).getDocument();
        final String docid=members_with_loan_listsfilterd.get(position).postID;
        String fname=members_with_loan_listsfilterd.get(position).getFname();
        String lname=members_with_loan_listsfilterd.get(position).getLname();


        firebaseFirestore.collection("AllMembers").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()){
                    if (task.getResult().exists()){
                        String fullname=task.getResult().getString("fname")+" "+task.getResult().getString("lname");

                        holder.settingup(fullname,amount,id,finalAmount);
                        holder.mviewall.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent=new Intent(v.getContext(),MemberwithloanDetails.class);
                                intent.putExtra("id",id);
                                intent.putExtra("amount",amount);
                                intent.putExtra("document",document);
                                intent.putExtra("docid",docid);
                                v.getContext().startActivity(intent);
                            }
                        });
                        holder.mPeople.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent=new Intent(v.getContext(),MemberwithloanDetails.class);
                                intent.putExtra("id",id);
                                intent.putExtra("docid",docid);
                                intent.putExtra("amount",amount);
                                intent.putExtra("document",document);
                                v.getContext().startActivity(intent);

                            }
                        });
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return members_with_loan_listsfilterd.size();
    }

    public Filter getFilter(){
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();    if (charString.isEmpty()) {
                    members_with_loan_listsfilterd = members_with_loan_lists;
                } else {
                    List<Members_With_Loan_List> filteredList = new ArrayList<>();
                    for (Members_With_Loan_List row : members_with_loan_lists) {

                        // name match condition. this might differ depending on your requirement
                        if (row.getFname().contains(constraint) || row.getLname().contains(constraint) || row.getId().contains(constraint)) {
                            filteredList.add(row);
                        }
                    }

                    members_with_loan_listsfilterd = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = members_with_loan_listsfilterd;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                members_with_loan_listsfilterd = (ArrayList<Members_With_Loan_List>) results.values;
                notifyDataSetChanged();

            }
        };
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private ConstraintLayout mPeople;
        private TextView mFullname,mIdno,mAmount,mviewall,mfinalamount;
        public ViewHolder(View itemView) {
            super(itemView);
            view=itemView;


            mviewall=(TextView)view.findViewById(R.id.tv_allmembers_people);
            mPeople=(ConstraintLayout)view.findViewById(R.id.Layout_people);
        }
        public void settingup(String name,String amount,String idno,String finalamount){

            mAmount=(TextView)view.findViewById(R.id.tv_amount_peoplewithloan);
            mFullname=(TextView)view.findViewById(R.id.tv_fullnamepeople_with_loan);
            mIdno=(TextView)view.findViewById(R.id.tv_id_peoplewithloan);
            mfinalamount=(TextView)view.findViewById(R.id.tv_loan_due);

            mIdno.setText("Id number: "+idno);
            mFullname.setText("Full name: "+name);
            mAmount.setText("Loan amount: "+amount);
            mfinalamount.setText("Loan due: "+finalamount);


        }
    }
}
