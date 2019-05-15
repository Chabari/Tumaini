package tumaini.tumaini;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by George on 5/3/2019.
 */

public class All_Members_Adapter extends RecyclerView.Adapter<All_Members_Adapter.ViewHolder>implements Filterable {

    private FirebaseFirestore firebaseFirestore;
    private Context context;
    private List<All_Members_List>all_members_lists;
    private ProgressDialog progressDialog;
    private List<All_Members_List>all_members_listsfiltered;


    public All_Members_Adapter(List<All_Members_List> all_members_lists) {
        this.all_members_lists = all_members_lists;
        this.all_members_listsfiltered = all_members_lists;
    }

    @Override
    public All_Members_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.allmembers_layout,parent,false);
        context=parent.getContext();
        firebaseFirestore=FirebaseFirestore.getInstance();
        progressDialog=new ProgressDialog(context);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final All_Members_Adapter.ViewHolder holder, int position) {

        final String id = all_members_listsfiltered.get(position).getId_number();
        String fname=all_members_listsfiltered.get(position).getFname();
        String lname=all_members_listsfiltered.get(position).getLname();



        firebaseFirestore.collection("AllMembers").document(id).get().addOnCompleteListener(
                new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            if (task.getResult().exists()){
                                String name = task.getResult().getString("fname")+" "+task.getResult().getString("lname");
                                String membernumber = task.getResult().getString("member_Number");
                                String phone = task.getResult().getString("phone_number");

                                holder.setingdetails(name,membernumber,phone);
                            }
                        }
                    }
                }
        );


        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(),MemberDetails.class);
                intent.putExtra("id",id);
                v.getContext().startActivity(intent);
            }
        });
        holder.mViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(),MemberDetails.class);
                intent.putExtra("id",id);
                v.getContext().startActivity(intent);
            }
        });
        holder.mLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                new AlertDialog.Builder(context)
                        .setTitle("Deleting")
                        .setIcon(R.drawable.ic_action_warning)
                        .setMessage("Are you sure you want to delete this member?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                progressDialog.setMessage("Deleting..");
                                progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
                                progressDialog.show();

                                firebaseFirestore.collection("AllMembers").document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(context, "Member successfully deleted", Toast.LENGTH_SHORT).show();
                                        }else {
                                            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
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
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return all_members_listsfiltered.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    all_members_listsfiltered = all_members_lists;
                } else {
                    List<All_Members_List> filteredList = new ArrayList<>();
                    for (All_Members_List row : all_members_lists) {

                        // name match condition. this might differ depending on your requirement
                        if (row.getFname().contains(charSequence) || row.getLname().contains(charSequence) || row.getId_number().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    all_members_listsfiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = all_members_listsfiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                all_members_listsfiltered = (ArrayList<All_Members_List>) filterResults.values;
                notifyDataSetChanged();
            }

        };

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mName,mPhone,mViewAll,mMemberNo;
        private ConstraintLayout mLayout;
        private View view;
        public ViewHolder(View itemView) {
            super(itemView);
            view=itemView;

            mLayout=(ConstraintLayout)view.findViewById(R.id.Layout_all_members);
            mViewAll=(TextView)view.findViewById(R.id.tv_viewmemberdetails);
        }
        public void setingdetails(String name,String memberno,String phone){

            mName=(TextView)view.findViewById(R.id.tv_Name_allmember);
            mMemberNo=(TextView)view.findViewById(R.id.tv_memberno_allmembers);
            mPhone=(TextView)view.findViewById(R.id.tv_phone_no_allmembers);

            mName.setText("Full Name: "+name);
            mMemberNo.setText("Member number: "+memberno);
            mPhone.setText("Phone Number: "+phone);
        }
    }

}
