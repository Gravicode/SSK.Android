package com.si_ware.neospectra.Activities.Interfaces.ExportPage;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.si_ware.neospectra.Activities.IntroActivity;
import com.si_ware.neospectra.Activities.SettingsActivity;
import com.si_ware.neospectra.BluetoothSDK.SWS_P3API;
import com.si_ware.neospectra.R;
import com.si_ware.neospectra.dbtable.Constants;
import com.si_ware.neospectra.dbtable.DBAdapter;
import com.si_ware.neospectra.dbtable.DBHelper;
import com.si_ware.neospectra.dbtable.Spacecraft;
import com.si_ware.neospectra.dbtable.TableHelper;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import de.codecrafters.tableview.toolkit.TableDataRowBackgroundProviders;

import static com.si_ware.neospectra.Global.GlobalVariables.bluetoothAPI;
import static java.util.Calendar.YEAR;

public class ExportPageFragment extends Fragment {

    private Context mContext;

    EditText edtDate, edtTanggal, edtSeacrh;
    Calendar myCalendar, myCalendar2;
    DatePickerDialog.OnDateSetListener date, date2;

    EditText edtBray, edtCa, edtClay, edtCn, edtHcl, edtP2o5;
    Button saveBtn;
    TableView<String[]> tb;
    TableHelper tableHelper;


    //sort option
    String orderByNewest = Constants.ROW_ID + " DESC";
    String orderByOldest = Constants.ROW_ID + " ASC";

    //for storage permission//
    private static final int STORAGE_REQUEST_CODE_EXPORT = 1;
    private String[] storagePermissions;


    //DB helper
    private DBHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_export_page, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (bluetoothAPI == null) {
            bluetoothAPI = new SWS_P3API(getActivity(), mContext);
        }
        mContext = getActivity();

        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_keyboard_arrow_left);

        Toolbar toolbar = view.findViewById(R.id.titlebar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Export Data");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(upArrow);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.icons_menu_dot);
        toolbar.setOverflowIcon(drawable);
        super.onCreate(savedInstanceState);

        edtDate = view.findViewById(R.id.edtDate);
        edtTanggal = view.findViewById(R.id.edtTanggal);
//        edtSeacrh = view.findViewById(R.id.edtSearch);
        //hapus db
//        final DBHelper db = new DBHelper(ExportPage.this);

        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


        //init db helper class
        dbHelper = new DBHelper(getActivity());


        myCalendar = Calendar.getInstance();
        date = (datePicker, year, month, dayOfMounth) -> {
            myCalendar.set(YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMounth);
            updateLabel();

        };

        myCalendar2 = Calendar.getInstance();
        date2 = (datePicker, year, month, dayOfMounth) -> {
            myCalendar2.set(YEAR, year);
            myCalendar2.set(Calendar.MONTH, month);
            myCalendar2.set(Calendar.DAY_OF_MONTH, dayOfMounth);
            updateLabel2();

        };

        edtTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(), date2, myCalendar2.get(Calendar.YEAR), myCalendar2.get(Calendar.MONTH), myCalendar2.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });

        edtDate.setOnClickListener(view1 -> new DatePickerDialog(getActivity(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH))
                .show());


//        btnMenu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                PopupMenu popup = new PopupMenu(getActivity(), btnMenu);
//
//                popup.getMenuInflater().inflate(R.menu.export_menu, popup.getMenu());
//
//                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem menuItem) {
//                        if (menuItem.getItemId() == R.id.share) {
////                            Intent intent = new Intent(ExportPage.this, TambahData.class);
////                            startActivity(intent);
//
//                            try {
//
//                                String filelocation = Environment.getExternalStorageDirectory().getPath() + "/Neospectra/NEO.csv";
//                                Intent intent = new Intent(Intent.ACTION_SENDTO);
//                                intent.setType("text/plain");
//                                String message = "File to be shared is .";
//                                intent.putExtra(Intent.EXTRA_SUBJECT, "Students REPORT");
//                                intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + filelocation));
//                                intent.putExtra(Intent.EXTRA_TEXT, message);
//                                intent.setData(Uri.parse("mailto:csent.company@gmail.com"));   //insert your email address("mailto:csent.company@gmail.com")
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                                startActivity(intent);
//                            } catch (Exception e) {
//                                System.out.println("is exception raises during sending mail" + e);
//                            }
//
//
//                        }
//                        if (menuItem.getItemId() == R.id.export) {
//
////                            Intent intent = new Intent(ExportPage.this, TambahData.class);
////                            startActivity(intent);
//
//                            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ExportPage.this);
//                            bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog);
//
//                            bottomSheetDialog.setCanceledOnTouchOutside(false);
//
//                            LinearLayout bottom_sheet_refresh = bottomSheetDialog.findViewById(R.id.bottom_sheet_export);
//                            CardView btn_yes = bottomSheetDialog.findViewById(R.id.btn_yes);
//                            CardView btn_cancel = bottomSheetDialog.findViewById(R.id.btn_cancel);
//                            bottom_sheet_refresh.setVisibility(View.VISIBLE);
//
//                            btn_yes.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
////                        fungsi yes pada bottom sheet
////                                    ExportPage.this.finish();
////                                    startActivity(ExportPage.this.getIntent());
//
//                                    if (checkStoragePermission()) {
//                                        //permission allowed
//                                        exportCSV();
//                                    } else {
//                                        requestStoragePermissionExport();
//                                    }
//
////                                    sqliteToExcel = new SQLiteToExcel(getApplicationContext(), DBHelper.DB_NAME, directory_path);
////                                    // Exclude Columns
////                                    List<String> excludeColumns = new ArrayList<>();
////                                    excludeColumns.add("contact_id");
////
////                                    // Prettify or Naming Columns
////                                    HashMap<String, String> prettyNames = new HashMap<>();
////                                    prettyNames.put("contact_person_name", "Person Name");
////                                    prettyNames.put("contact_no", "Mobile Number");
////
////                                    sqliteToExcel.setExcludeColumns(excludeColumns);
////                                    sqliteToExcel.setPrettyNameMapping(prettyNames);
////                                    sqliteToExcel.setCustomFormatter(new SQLiteToExcel.ExportCustomFormatter() {
////                                        @Override
////                                        public String process(String columnName, String value) {
////                                            switch (columnName) {
////                                                case "contact_person_name":
////                                                    value = "Sale";
////                                            }
////                                            return value;
////                                        }
////                                    });
////                                    sqliteToExcel.exportAllTables("users1.xls", new SQLiteToExcel.ExportListener() {
////                                        @Override
////                                        public void onStart() {
////
////                                        }
////
////                                        @Override
////                                        public void onCompleted(String filePath) {
////                                            Utils.showSnackBar(view, "Successfully Exported");
////                                        }
////
////                                        @Override
////                                        public void onError(Exception e) {
////                                            Utils.showSnackBar(view, e.getMessage());
////                                        }
////                                    });
//
//
//                                }
//                            });
//
//                            btn_cancel.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
////                        fungsi cancel bottom sheet
//                                    bottomSheetDialog.hide();
//                                }
//                            });
//                            bottomSheetDialog.show();
////
////
////            finish();
//                        }
//
//                        if (menuItem.getItemId() == R.id.delete) {
//                            Toast.makeText(ExportPage.this, "Button Delete", Toast.LENGTH_SHORT).show();
//                        }
//                        return true;
//                    }
//                });
//                popup.show();
//            }
//
//        });


        //TABLEVIEW
        tableHelper = new TableHelper(getActivity());
        tb = (TableView<String[]>) view.findViewById(R.id.tableView);
        tb.setColumnCount(6);
        tb.setHeaderBackgroundColor(Color.parseColor("#ffffff"));

        tb.setHeaderAdapter(new SimpleTableHeaderAdapter(getActivity(), tableHelper.getSpaceProbeHeaders()));
        tb.setDataAdapter(new SimpleTableDataAdapter(getActivity(), tableHelper.getSpaceProbes()));

        int colorEvenRows = getResources().getColor(R.color.colorEvenRows);
        int colorOddRows = getResources().getColor(R.color.colorOddRows);
        tb.setDataRowBackgroundProvider(TableDataRowBackgroundProviders.alternatingRowColors(colorEvenRows, colorOddRows));


//        TextView tv = new TextView(this);
//        tv.setLayoutParams(new .LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
//                TableRow.LayoutParams.WRAP_CONTENT));
//        tb.setGravity(Gravity.CENTER);
//        tb.setTextSize(16);
//        tb.setPadding(5, 5, 5, 5);
//        tb.setText(text);
//        row.addView(tv);

        //TABLE CLICK
        tb.addDataClickListener(new TableDataClickListener() {
            @Override
            public void onDataClicked(int rowIndex, Object clickedData) {
//                Toast.makeText(getActivity(), ((String[]) clickedData)[0], Toast.LENGTH_SHORT).show();
//                dbHelper.deleteData(clickedData);

                dbHelper.deleteData(((String) clickedData));
                Toast.makeText(getActivity(), "Data dihapus ", Toast.LENGTH_SHORT).show();

//                Integer hapusBaris = db.deleteData(edtBray.getText().toString());
//                if (hapusBaris > 0 ) {
//                    Toast.makeText(ExportPage.this, "Hapus berhasil", Toast.LENGTH_SHORT).show();
//
//
//                    ExportPage.this.finish();
//                } else {
//                    Toast.makeText(ExportPage.this, "Hapus gagal", Toast.LENGTH_SHORT).show();
//                }


            }
        });

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayDialog();
            }
        });
    }


    private boolean checkStoragePermission() {
        //check if storage permission is enabled or not and return true/false
        boolean result = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermissionExport() {
        ActivityCompat.requestPermissions(getActivity(), storagePermissions, STORAGE_REQUEST_CODE_EXPORT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        //handle permission result
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case STORAGE_REQUEST_CODE_EXPORT: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission granted
                    exportCSV();
                } else {
                    //permission denied
                    Toast.makeText(getActivity(), "Storage permission required...", Toast.LENGTH_SHORT).show();
                }
            }
            break;

        }

    }

    private void exportCSV() {
        //path of csv file

        File folder = new File(Environment.getExternalStorageDirectory() + "/" + "Neospectra"); // Neospectra is folder name

        boolean isFolderCreated = false;
        if (!folder.exists()) {
            isFolderCreated = folder.mkdir(); //create folder if not exists
        }

        Log.d("CSC_TAG", "exportCSV: " + isFolderCreated);

        //file name
        String csvFileName = "NEOSPECTRA.csv";

        //complate path and name
        String filePathAndName = folder.toString() + "/" + csvFileName;

        //get records
        ArrayList<Spacecraft> recordsList = new ArrayList<>();
        recordsList.clear();
        recordsList = dbHelper.getAllRecords(orderByOldest);

        try {
            //write csv file
            FileWriter fw = new FileWriter(filePathAndName);
            for (int i = 0; i < recordsList.size(); i++) {
                fw.append("" + recordsList.get(i).getId()); //id
                fw.append(",");
                fw.append("" + recordsList.get(i).getBray()); //bray
                fw.append(",");
                fw.append("" + recordsList.get(i).getCa()); //ca
                fw.append(",");
                fw.append("" + recordsList.get(i).getClay()); //clay
                fw.append(",");
                fw.append("" + recordsList.get(i).getCn()); //cn
                fw.append(",");
                fw.append("" + recordsList.get(i).getHcl()); //hcl
//                fw.append(",");
//                fw.append("" + recordsList.get(i).getP2o5()); //p2o5
                fw.append("\n");

            }
            fw.flush();
            fw.close();

            Toast.makeText(getActivity(), "Exported to: " + filePathAndName, Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            //if there any exeption, show exception messege
            Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void displayDialog() {
        Dialog d = new Dialog(getActivity());
        d.setTitle("SQLITE DATA");
        d.setContentView(R.layout.dialog_layout);

        //INITIALIZE VIEWS
//        EditText edtId = d.findViewById(R.id.edtId);
        edtBray = (EditText) d.findViewById(R.id.edtBray);
        edtCa = (EditText) d.findViewById(R.id.edtCa);
        edtClay = (EditText) d.findViewById(R.id.edtClay);
        edtCn = d.findViewById(R.id.edtCn);
        edtHcl = d.findViewById(R.id.edtHcl);
//        edtP2o5 = d.findViewById(R.id.edtP2o5);

        saveBtn = (Button) d.findViewById(R.id.saveBtn);

        //SAVE
        saveBtn.setOnClickListener(v -> {

            Spacecraft s = new Spacecraft();

//                s.setId(edtId.get);
            s.setBray(edtBray.getText().toString());
            s.setCa(edtCa.getText().toString());
            s.setClay(edtClay.getText().toString());
            s.setCn(edtCn.getText().toString());
            s.setHcl(edtHcl.getText().toString());
//                s.setP2o5(edtP2o5.getText().toString());

            if (new DBAdapter(getActivity()).saveSpacecraft(s)) {
                edtBray.setText("");
                edtCa.setText("");
                edtClay.setText("");
                edtCn.setText("");
                edtHcl.setText("");
//                    edtP2o5.setText("");


                tb.setDataAdapter(new SimpleTableDataAdapter(getActivity(), tableHelper.getSpaceProbes()));
                Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Not Saved", Toast.LENGTH_SHORT).show();
            }
        });

        //SHOW DIALOG
        d.show();

    }


    private void updateLabel() {
        String myFormat = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        edtDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabel2() {
        String myFormat = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        edtTanggal.setText(sdf.format(myCalendar2.getTime()));

    }


}
//@Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);
//    }
//
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_export, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            android.support.v7.app.AlertDialog.Builder myAlert = new android.support.v7.app.AlertDialog
//                    .Builder(mContext);
//            myAlert.setTitle("Disconnect");
//            myAlert.setMessage("Are you sure you want to disconnect the device?");
//            myAlert.setPositiveButton("OK", (dialogInterface, i) -> {
//                dialogInterface.dismiss();
//                // Request of bluetooth disconnection
//                if (bluetoothAPI != null) {
//                    bluetoothAPI.disconnectFromDevice();
//
//                    Intent iMain = new Intent(mContext, IntroActivity.class);
//                    iMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(iMain);
//                }
//
//            });
//            myAlert.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss());
//            myAlert.show();
//        }
//        if (item.getItemId() == R.id.share) {
//            try {
//                String filelocation = Environment.getExternalStorageDirectory().getPath() + "/Neospectra/NEO.csv";
//                Intent intent = new Intent(Intent.ACTION_SENDTO);
//                intent.setType("text/plain");
//                String message = "File to be shared is .";
//                intent.putExtra(Intent.EXTRA_SUBJECT, "Students REPORT");
//                intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + filelocation));
//                intent.putExtra(Intent.EXTRA_TEXT, message);
//                intent.setData(Uri.parse("kaisarfajaroktavinusentiman@gmail.com"));   //insert your email address("mailto:csent.company@gmail.com")
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//
//                Toast.makeText(getActivity(), "Berhasil", Toast.LENGTH_SHORT).show();
//            } catch (Exception e) {
//                System.out.println("is exception raises during sending mail" + e);
//                Log.e("Erorr : ", String.valueOf(e));
//                Toast.makeText(getActivity(), "Error : " + String.valueOf(e), Toast.LENGTH_SHORT).show();
//            }
//        }
//        if (item.getItemId() == R.id.export_data) {
//            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
//            bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog);
//
//            bottomSheetDialog.setCanceledOnTouchOutside(false);
//
//            LinearLayout bottom_sheet_refresh = bottomSheetDialog.findViewById(R.id.bottom_sheet_export);
//            bottom_sheet_refresh.setVisibility(View.VISIBLE);
//            CardView btn_yes = bottomSheetDialog.findViewById(R.id.btn_yes3);
//            CardView btn_cancel = bottomSheetDialog.findViewById(R.id.btn_cancel3);
//
//            btn_yes.setOnClickListener(v -> {
//
//                if (checkStoragePermission()) {
//                    //permission allowed
//                    exportCSV();
//                } else {
//                    requestStoragePermissionExport();
//                }
//            });
//
//            btn_cancel.setOnClickListener(view -> bottomSheetDialog.hide());
//
//            bottomSheetDialog.show();
//
//        }
//        if (item.getItemId() == R.id.delete) {
//            Toast.makeText(getActivity(), "Delete Action", Toast.LENGTH_SHORT).show();
//        }
//        return super.onOptionsItemSelected(item);
//    }