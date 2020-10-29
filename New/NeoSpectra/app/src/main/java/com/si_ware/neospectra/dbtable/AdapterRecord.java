package com.si_ware.neospectra.dbtable;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.si_ware.neospectra.Activities.Interfaces.ExportPage.ExportPageFragment;
import com.si_ware.neospectra.Activities.MainPage;
import com.si_ware.neospectra.R;

import java.util.ArrayList;

public class AdapterRecord extends RecyclerView.Adapter<AdapterRecord.HolderRecord> {

    //variables
    private Context context;
    private ArrayList<ModelRecord> recordList;

    //DB helper
    private DbHelper dbHelper;


    //constructor
    public AdapterRecord(Context context, ArrayList<ModelRecord> recordList) {
        this.context = context ;
        this.recordList = recordList;

        dbHelper = new DbHelper(context);
    }





    @NonNull
    @Override
    public HolderRecord onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //infate Layout
        View view = LayoutInflater.from(context).inflate(R.layout.row_record, parent, false);

        return new HolderRecord(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HolderRecord holder, int position) {
        //get data, set data, handel view clics in this method

        //get data
        ModelRecord model = recordList.get(position);
        final String id = model.getId();
        String bray = model.getBray();
        String ca = model.getCa();
        String clay = model.getClay();
        String cn = model.getCn();
        String addedTime = model.getAddedTime();


        //set data to view

        holder.txtBray.setText(bray);
        holder.txtCa.setText(ca);
        holder.txtClay.setText(clay);
        holder.txtCn.setText(cn);
        holder.txtId.setText(id);

        //handle item click (delete
        // )
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Delete entry");
                alert.setMessage("Are you sure you want to delete data with id = " + id + " ? ");
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        dbHelper.deleteData(id);
//                        ((ExportPageFragment)context).onResume(); //error code saat recode dari activity ke android
                    }
                });
                alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // close dialog
                        dialog.cancel();
                    }
                });
                alert.show();
            }


        });



    }

    @Override
    public int getItemCount() {
        return recordList.size(); //return size of list/number or records
    }


    class HolderRecord extends RecyclerView.ViewHolder{

        //views
        TextView txtId, txtBray, txtCa, txtClay, txtCn;


        public HolderRecord(@NonNull View itemView) {
            super(itemView);

            //init views
            txtBray = itemView.findViewById(R.id.txtBray);
            txtCa = itemView.findViewById(R.id.txtCa);
            txtClay = itemView.findViewById(R.id.txtClay);
            txtCn = itemView.findViewById(R.id.txtCn);
            txtId = itemView.findViewById(R.id.txtId);

        }
    }


}
