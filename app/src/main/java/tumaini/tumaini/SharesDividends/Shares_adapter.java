package tumaini.tumaini.SharesDividends;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tumaini.tumaini.Loans.Wakulima_Loan;
import tumaini.tumaini.R;

/**
 * Created by George on 5/8/2019.
 */

public class Shares_adapter extends RecyclerView.Adapter<Shares_adapter.ViewHolder> {
    private List<Shares_List>shares_lists;
    private Context context;
    private FirebaseFirestore firebaseFirestore;

    public Shares_adapter(List<Shares_List> shares_lists) {
        this.shares_lists = shares_lists;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.all_shares_display,parent,false);
        context=parent.getContext();
        firebaseFirestore=FirebaseFirestore.getInstance();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        String id=shares_lists.get(position).getId_number();

       firebaseFirestore.collection("AllMembers").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()){
                    if (task.getResult().exists()){
                        final String name=task.getResult().getString("Other_names")+" "+task.getResult().getString("fname")+" "+task.getResult().getString("lname");
                        final String shares=task.getResult().getString("shares");
                        final String id=task.getResult().getString("id_number");

                        holder.settingshares(name,id,shares);

                        holder.mshares.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                holder.dialog(name,id,shares);
                            }
                        });

                    }
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return shares_lists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private Button mshares;
        private TextView mName,mIdnumber;
        private View view;
        public ViewHolder(View itemView) {
            super(itemView);
            view=itemView;
            mshares=(Button)view.findViewById(R.id.btn_all_shares_display);
            mIdnumber=(TextView)view.findViewById(R.id.tv_idnumber_shares_display);
            mName=(TextView)view.findViewById(R.id.tv_fullname_shares_display);

        }
        public void settingshares(String fullname,String idnumber,String shares){

            mshares=(Button)view.findViewById(R.id.btn_all_shares_display);
            mIdnumber=(TextView)view.findViewById(R.id.tv_idnumber_shares_display);
            mName=(TextView)view.findViewById(R.id.tv_fullname_shares_display);

            mName.setText("Full Name: "+fullname);
            mIdnumber.setText("Id Number: "+idnumber);
            mshares.setText("Ksh "+shares+"  Shares");

        }

        private void dialog(String fullname, final String Id, final String myShares) {
            android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(context);
            final EditText edittext = new EditText(context);
            alert.setMessage("Enter Contributed Shares for "+fullname);
            alert.setCancelable(false);
            alert.setTitle("New Shares..");

            alert.setView(edittext);

            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(final DialogInterface dialog, int whichButton) {
                    //What ever you want to do with the value
                    //OR
                    final String newshares = edittext.getText().toString();

                    float finalShares = 0;

                    float initialShares = Float.parseFloat(newshares);
                    float Shares=Float.parseFloat(myShares);
                    finalShares = initialShares+Shares;

                    int amnt = Integer.parseInt(newshares);
                    if (amnt>5000){
                        edittext.setError("The amount is too high");
                    }else if (amnt<400){
                        edittext.setError("The amount is too low");

                    }else {

                        final ProgressDialog progressDialog = new ProgressDialog(context);
                        progressDialog.setMessage("Please wait...");
                        progressDialog.show();

                        Map<String,Object>stringObjectMap=new HashMap<>();
                        stringObjectMap.put("shares",""+finalShares);

                        firebaseFirestore.collection("AllMembers").document(Id).update(stringObjectMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()){

                                    Toast.makeText(context, "Shares successfully submitted", Toast.LENGTH_SHORT).show();

                                    dialog.dismiss();

                                }else {
                                    Toast.makeText(context, "Something went wrong...", Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                }
                                progressDialog.dismiss();

                            }
                        });

                    }

                }
            });


            alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // what ever you want to do with No option.
                    dialog.dismiss();
                }
            });

            alert.show();

        }

    }
}
