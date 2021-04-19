package com.si_ware.neospectra.Activities.Interfaces.ExportPage;

import android.Manifest;
//import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.si_ware.neospectra.Activities.IntroActivity;
import com.si_ware.neospectra.BluetoothSDK.SWS_P3API;
import com.si_ware.neospectra.R;
import com.si_ware.neospectra.dbtable.AdapterRecord;
import com.si_ware.neospectra.dbtable.Constants;
import com.si_ware.neospectra.dbtable.DBHelper;
import com.si_ware.neospectra.dbtable.ModelRecord;
import com.si_ware.neospectra.dbtable.addUpdateRecord;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static com.si_ware.neospectra.Global.GlobalVariables.bluetoothAPI;
import static java.util.Calendar.YEAR;

public class ExportPageFragment extends Fragment {
    private Context mContext;
    SearchView edtDate, edtTanggal;
    Calendar myCalendar, myCalendar2;
    DatePickerDialog.OnDateSetListener date, date2;
    public String tanggal1;
    public String tanggal2;
    private FloatingActionButton addRecordBtn;
    private RecyclerView recordsRv;
    private DBHelper dbHelper;
    ActionBar actionBar;
    TextView dateFrom;
    private ArrayList<ModelRecord> recordList;
    private int counter = 0;
    private AdapterRecord adapter = null;

    //sort option
    String orderByNewest = Constants.TABLE_NAME + "." + Constants.C_ID + " DESC";
    String orderByOldest = Constants.TABLE_NAME + "." + Constants.C_ADDED_TIME_STAMP + " ASC";

    //for refresh records, refresh with last choosen sort option
    String currentOrderByStatus = orderByNewest;

    //for storage permission//
    private static final int STORAGE_REQUEST_CODE_EXPORT = 1;
    private String[] storagePermissions;

    Button getselection;
    SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_export_page, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //comment or uncomment this function to test the aplication without connect to device
        if (bluetoothAPI == null) {
            bluetoothAPI = new SWS_P3API(getActivity(), mContext);
        }
        mContext = getActivity();

        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_keyboard_arrow_left);

        Toolbar toolbar = view.findViewById(R.id.titlebar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(upArrow);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        super.onCreate(savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences("date", MODE_PRIVATE);

        //init baru
        edtDate = view.findViewById(R.id.edtDate);
        edtTanggal = view.findViewById(R.id.edtTanggal);
        dateFrom = view.findViewById(R.id.dateFrom);

        //init views
        addRecordBtn = view.findViewById(R.id.addRecordBtn);
        recordsRv = view.findViewById(R.id.recordsRV);

        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //init db helper class
        dbHelper = new DBHelper(getActivity());

        //load records
        loadRecords(orderByNewest);

        addRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), addUpdateRecord.class));
            }
        });

        //format tanggal
        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMounth) {
                myCalendar.set(YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMounth);
                updateLabel();

            }
        };

        myCalendar2 = Calendar.getInstance();
        date2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMounth) {
                myCalendar2.set(YEAR, year);
                myCalendar2.set(Calendar.MONTH, month);
                myCalendar2.set(Calendar.DAY_OF_MONTH, dayOfMounth);
                updateLabel2();

            }
        };

        edtTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(), date2, myCalendar2.get(YEAR), myCalendar2.get(Calendar.MONTH), myCalendar2.get(Calendar.DAY_OF_MONTH))
                        .show();

            }
        });

        edtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(), date, myCalendar.get(YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });

        edtDate.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SimpleDateFormat minDate = new SimpleDateFormat("yyyy-MM-d", Locale.US);
                query = minDate.format(myCalendar.getTime());
                tanggal1 = query;
                if (tanggal1 != null && tanggal2 != null) {
                    //search when search button on keyboard clicked

                    searchRecord(tanggal1, tanggal2);
                } else {
                    loadRecords(orderByNewest);
                }
                return false;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                //search as you type
                SimpleDateFormat minDate = new SimpleDateFormat("yyyy-MM-d", Locale.US);
                newText = minDate.format(myCalendar.getTime());
                tanggal1 = newText;
                if (tanggal1 != null && tanggal2 != null) {
                    //search when search button on keyboard clicked

                    searchRecord(tanggal1, tanggal2);

                    Toast.makeText(getActivity(), tanggal1 + " sampai " + tanggal2, Toast.LENGTH_SHORT).show();

                } else {
                    loadRecords(orderByNewest);
                }

                return false;
            }
        });

        edtTanggal.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SimpleDateFormat maxDate = new SimpleDateFormat("yyyy-MM-d", Locale.US);
                query = maxDate.format(myCalendar2.getTime());
                tanggal2 = query;
                if (tanggal1 != null && tanggal2 != null) {
                    //search when search button on keyboard clicked
                    searchRecord(tanggal1, tanggal2);
                    Toast.makeText(getActivity(), tanggal1 + " sampai " + tanggal2, Toast.LENGTH_SHORT).show();

                } else {
                    loadRecords(orderByNewest);
                }
                return false;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                //search as you type
                SimpleDateFormat maxDate = new SimpleDateFormat("yyyy-MM-d", Locale.US);
                newText = maxDate.format(myCalendar2.getTime());
                tanggal2 = newText;

                if (tanggal1 != null && tanggal2 != null) {
                    //search when search button on keyboard clicked
                    searchRecord(tanggal1, tanggal2);
                } else {
                    loadRecords(orderByNewest);
                }

                return false;
            }
        });

        // Check if no view has focus:
        View view2 = getActivity().getCurrentFocus();
        if (view2 != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-d";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        String date = sdf.format(myCalendar.getTime());
        edtDate.setQuery(date, true);
    }


    private void updateLabel2() {
        String myFormat = "yyyy-MM-d";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        String tanggal = sdf.format(myCalendar2.getTime());
        edtTanggal.setQuery(tanggal, true);
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

    private void loadRecords(String orderby) {
        currentOrderByStatus = orderby;

        AdapterRecord adapterRecord = new AdapterRecord(getActivity(),
                dbHelper.getAllRecords(orderby));

        recordsRv.setAdapter(adapterRecord);

    }

    private void searchRecord(String query, String query2) {
        AdapterRecord adapterRecord = new AdapterRecord(getActivity(),
                dbHelper.SearchRecords(query, query2));

        recordsRv.setAdapter(adapterRecord);
    }


    @Override
    public void onResume() {
        loadRecords(currentOrderByStatus); // refresh record list

        //comment or uncomment this function to test the aplication without connect to device
        if (bluetoothAPI != null) {
            if (!bluetoothAPI.isDeviceConnected()) {
                endActivity();
                return;
            }
        }
        super.onResume();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_export) {
            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
            bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog);
            bottomSheetDialog.setCanceledOnTouchOutside(false);
            LinearLayout bottom_sheet_refresh = bottomSheetDialog.findViewById(R.id.bottom_sheet_refresh);
            CardView btn_yes = bottomSheetDialog.findViewById(R.id.btn_yes);
            CardView btn_cancel = bottomSheetDialog.findViewById(R.id.btn_cancel);
            bottom_sheet_refresh.setVisibility(View.VISIBLE);

            btn_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //fungsi yes pada bottom sheet
                    //sql to csv file
                    if (!checkStoragePermission()) {
                        checkStoragePermission();
                    }
                    if (checkStoragePermission()) {
                        //permission allowed
                        exportCSV();
                        bottomSheetDialog.hide();
                    } else {
                        requestStoragePermissionExport();
                    }
                }
            });

            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //fungsi cancel bottom sheet
                    bottomSheetDialog.hide();
                }
            });
            bottomSheetDialog.show();


        }

        if (id == R.id.action_exportXls) {

            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
            bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog);
            bottomSheetDialog.setCanceledOnTouchOutside(false);
            LinearLayout bottom_sheet_refresh = bottomSheetDialog.findViewById(R.id.bottom_sheet_refresh);
            CardView btn_yes = bottomSheetDialog.findViewById(R.id.btn_yes);
            CardView btn_cancel = bottomSheetDialog.findViewById(R.id.btn_cancel);
            bottom_sheet_refresh.setVisibility(View.VISIBLE);

            btn_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //fungsi yes pada bottom sheet
                    //sql to csv file
                    if (!checkStoragePermission()) {
                        checkStoragePermission();
                    }
                    if (checkStoragePermission()) {
                        //permission allowed
                        exportXls();
                        bottomSheetDialog.hide();
                    } else {
                        requestStoragePermissionExport();
                    }
                }
            });

            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //fungsi cancel bottom sheet
                    bottomSheetDialog.hide();
                }
            });
            bottomSheetDialog.show();
        }

        if (id == R.id.action_sort) {
            //show sort options (show in dialog)
//            sortOptionDialog();
            loadRecords(orderByNewest);
        }
        if (item.getItemId() == android.R.id.home) {
//            AlertDialog.Builder myAlert = new AlertDialog.Builder(mContext);
            android.support.v7.app.AlertDialog.Builder myAlert = new android.support.v7.app.AlertDialog
                    .Builder(mContext);
            myAlert.setTitle("Disconnect");
            myAlert.setMessage("Are you sure you want to disconnect the device?");
            myAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    // Request of bluetooth disconnection
                    if (bluetoothAPI != null) {
                        bluetoothAPI.disconnectFromDevice();

                        Intent iMain = new Intent(mContext, IntroActivity.class);
                        iMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(iMain);
                    }
                }
            });
            myAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            myAlert.show();
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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

    private void exportXls() {
        Workbook workbook = new HSSFWorkbook();
        Cell cell = null;
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillBackgroundColor(HSSFColor.SEA_GREEN.index);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.THICK);
        cellStyle.setBorderLeft(BorderStyle.THICK);
        cellStyle.setBorderRight(BorderStyle.THICK);
        cellStyle.setBorderTop(BorderStyle.THICK);


        CellStyle cellStyle1 = workbook.createCellStyle();
        cellStyle1.setFillBackgroundColor(HSSFColor.SEA_GREEN.index);
        cellStyle1.setAlignment(HorizontalAlignment.CENTER);
        cellStyle1.setBorderBottom(BorderStyle.THIN);
        cellStyle1.setBorderLeft(BorderStyle.THIN);
        cellStyle1.setBorderRight(BorderStyle.THIN);
        cellStyle1.setBorderTop(BorderStyle.THIN);

        //get records
        ArrayList<ModelRecord> recordsList = new ArrayList<>();
        recordsList.clear();
        recordsList = dbHelper.getAllRecords(orderByOldest);

        //creating sheet
        Sheet sheet = null;
        sheet = workbook.createSheet("Data Scan");

        //create row and column
        Row row = null;
        row = sheet.createRow(0);

        cell = row.createCell(0);
        cell.setCellValue("Id");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(1);
        cell.setCellValue("Bray1_P20");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(2);
        cell.setCellValue("Ca");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(3);
        cell.setCellValue("CLAY");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(4);
        cell.setCellValue("C_N");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(5);
        cell.setCellValue("HCl25_K2O");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(6);
        cell.setCellValue("HCl25_P2O5");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(7);
        cell.setCellValue("Jumlah");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(8);
        cell.setCellValue("K");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(9);
        cell.setCellValue("KB_adjusted");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(10);
        cell.setCellValue("KJELDAHL_N");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(11);
        cell.setCellValue("KTK");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(12);
        cell.setCellValue("Mg");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(13);
        cell.setCellValue("Morgan_K2O");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(14);
        cell.setCellValue("Na");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(15);
        cell.setCellValue("Olsen_P2O5");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(16);
        cell.setCellValue("PH_H2O");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(17);
        cell.setCellValue("PH_KCL");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(18);
        cell.setCellValue("RetensiP");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(19);
        cell.setCellValue("SAND");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(20);
        cell.setCellValue("SILT");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(21);
        cell.setCellValue("WBC");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(22);
        cell.setCellValue("Date");
        cell.setCellStyle(cellStyle);
        for (int i = 0; i < recordsList.size(); i++) {
            if (i == 0) {
                row = sheet.createRow(i + 1);

                cell = row.createCell(i);
                cell.setCellValue("" + recordsList.get(i).getId());
                cell.setCellStyle(cellStyle1);
                cell = row.createCell(i + 1);
                cell.setCellValue("" + recordsList.get(i).getBray());
                cell.setCellStyle(cellStyle1);
                cell = row.createCell(i + 2);
                cell.setCellValue("" + recordsList.get(i).getCa());
                cell.setCellStyle(cellStyle1);
                cell = row.createCell(i + 3);
                cell.setCellValue("" + recordsList.get(i).getClay());
                cell.setCellStyle(cellStyle1);
                cell = row.createCell(i + 4);
                cell.setCellValue("" + recordsList.get(i).getCn());
                cell.setCellStyle(cellStyle1);
                cell = row.createCell(i + 5);
                cell.setCellValue("" + recordsList.get(i).getHclk2o());
                cell.setCellStyle(cellStyle1);
                cell = row.createCell(i + 6);
                cell.setCellValue("" + recordsList.get(i).getHclp2o5());
                cell.setCellStyle(cellStyle1);
                cell = row.createCell(i + 7);
                cell.setCellValue("" + recordsList.get(i).getJumlah());
                cell.setCellStyle(cellStyle1);
                cell = row.createCell(i + 8);
                cell.setCellValue("" + recordsList.get(i).getK());
                cell.setCellStyle(cellStyle1);
                cell = row.createCell(i + 9);
                cell.setCellValue("" + recordsList.get(i).getKbadj());
                cell.setCellStyle(cellStyle1);
                cell = row.createCell(i + 10);
                cell.setCellValue("" + recordsList.get(i).getKjeldahl());
                cell.setCellStyle(cellStyle1);
                cell = row.createCell(i + 11);
                cell.setCellValue("" + recordsList.get(i).getKtk());
                cell.setCellStyle(cellStyle1);
                cell = row.createCell(i + 12);
                cell.setCellValue("" + recordsList.get(i).getMg());
                cell.setCellStyle(cellStyle1);
                cell = row.createCell(i + 13);
                cell.setCellValue("" + recordsList.get(i).getMorgan());
                cell.setCellStyle(cellStyle1);
                cell = row.createCell(i + 14);
                cell.setCellValue("" + recordsList.get(i).getNa());
                cell.setCellStyle(cellStyle1);
                cell = row.createCell(i + 15);
                cell.setCellValue("" + recordsList.get(i).getOlsen());
                cell.setCellStyle(cellStyle1);
                cell = row.createCell(i + 16);
                cell.setCellValue("" + recordsList.get(i).getPhh2o());
                cell.setCellStyle(cellStyle1);
                cell = row.createCell(i + 17);
                cell.setCellValue("" + recordsList.get(i).getPhkcl());
                cell.setCellStyle(cellStyle1);
                cell = row.createCell(i + 18);
                cell.setCellValue("" + recordsList.get(i).getRetensip());
                cell.setCellStyle(cellStyle1);
                cell = row.createCell(i + 19);
                cell.setCellValue("" + recordsList.get(i).getSand());
                cell.setCellStyle(cellStyle1);
                cell = row.createCell(i + 20);
                cell.setCellValue("" + recordsList.get(i).getSilt());
                cell.setCellStyle(cellStyle1);
                cell = row.createCell(i + 21);
                cell.setCellValue("" + recordsList.get(i).getWbc());
                cell.setCellStyle(cellStyle1);
                cell = row.createCell(i + 22);
                cell.setCellValue("" + recordsList.get(i).getAddedTime());
                cell.setCellStyle(cellStyle1);
            }
            if (i > 0) {
                int nextCell = ((i / i) - 1);
                row = sheet.createRow(i + 1);

                cell = row.createCell(nextCell);
                cell.setCellValue("" + recordsList.get(i).getId());
                cell.setCellStyle(cellStyle1);
                cell = row.createCell(nextCell + 1);
                cell.setCellValue("" + recordsList.get(i).getBray());
                cell.setCellStyle(cellStyle1);
                cell = row.createCell(nextCell + 2);
                cell.setCellValue("" + recordsList.get(i).getCa());
                cell.setCellStyle(cellStyle1);
                cell = row.createCell(nextCell + 3);
                cell.setCellValue("" + recordsList.get(i).getClay());
                cell.setCellStyle(cellStyle1);
                cell = row.createCell(nextCell + 4);
                cell.setCellValue("" + recordsList.get(i).getCn());
                cell.setCellStyle(cellStyle1);
                cell = row.createCell(nextCell + 5);
                cell.setCellValue("" + recordsList.get(i).getHclk2o());
                cell.setCellStyle(cellStyle1);
                cell = row.createCell(nextCell + 6);
                cell.setCellValue("" + recordsList.get(i).getHclp2o5());
                cell.setCellStyle(cellStyle1);
                cell = row.createCell(nextCell + 7);
                cell.setCellValue("" + recordsList.get(i).getJumlah());
                cell.setCellStyle(cellStyle1);
                cell = row.createCell(nextCell + 8);
                cell.setCellValue("" + recordsList.get(i).getK());
                cell.setCellStyle(cellStyle1);
                cell = row.createCell(nextCell + 9);
                cell.setCellValue("" + recordsList.get(i).getKbadj());
                cell.setCellStyle(cellStyle1);
                cell = row.createCell(nextCell + 10);
                cell.setCellValue("" + recordsList.get(i).getKjeldahl());
                cell.setCellStyle(cellStyle1);
                cell = row.createCell(nextCell + 11);
                cell.setCellValue("" + recordsList.get(i).getKtk());
                cell.setCellStyle(cellStyle1);
                cell = row.createCell(nextCell + 12);
                cell.setCellValue("" + recordsList.get(i).getMg());
                cell.setCellStyle(cellStyle1);
                cell = row.createCell(nextCell + 13);
                cell.setCellValue("" + recordsList.get(i).getMorgan());
                cell.setCellStyle(cellStyle1);
                cell = row.createCell(nextCell + 14);
                cell.setCellValue("" + recordsList.get(i).getNa());
                cell.setCellStyle(cellStyle1);
                cell = row.createCell(nextCell + 15);
                cell.setCellValue("" + recordsList.get(i).getOlsen());
                cell.setCellStyle(cellStyle1);
                cell = row.createCell(nextCell + 16);
                cell.setCellValue("" + recordsList.get(i).getPhh2o());
                cell.setCellStyle(cellStyle1);
                cell = row.createCell(nextCell + 17);
                cell.setCellValue("" + recordsList.get(i).getPhkcl());
                cell.setCellStyle(cellStyle1);
                cell = row.createCell(nextCell + 18);
                cell.setCellValue("" + recordsList.get(i).getRetensip());
                cell.setCellStyle(cellStyle1);
                cell = row.createCell(nextCell + 19);
                cell.setCellValue("" + recordsList.get(i).getSand());
                cell.setCellStyle(cellStyle1);
                cell = row.createCell(nextCell + 20);
                cell.setCellValue("" + recordsList.get(i).getSilt());
                cell.setCellStyle(cellStyle1);
                cell = row.createCell(nextCell + 21);
                cell.setCellValue("" + recordsList.get(i).getWbc());
                cell.setCellStyle(cellStyle1);
                cell = row.createCell(nextCell + 22);
                cell.setCellValue("" + recordsList.get(i).getAddedTime());
                cell.setCellStyle(cellStyle1);
            }
        }


        sheet.setColumnWidth(0, (10 * 200));
        sheet.setColumnWidth(1, (10 * 500));
        sheet.setColumnWidth(2, (10 * 500));
        sheet.setColumnWidth(3, (10 * 500));
        sheet.setColumnWidth(4, (10 * 500));
        sheet.setColumnWidth(5, (10 * 500));
        sheet.setColumnWidth(6, (10 * 500));
        sheet.setColumnWidth(7, (10 * 500));
        sheet.setColumnWidth(8, (10 * 500));
        sheet.setColumnWidth(9, (10 * 500));
        sheet.setColumnWidth(10, (10 * 500));
        sheet.setColumnWidth(11, (10 * 500));
        sheet.setColumnWidth(12, (10 * 500));
        sheet.setColumnWidth(13, (10 * 500));
        sheet.setColumnWidth(14, (10 * 500));
        sheet.setColumnWidth(15, (10 * 500));
        sheet.setColumnWidth(16, (10 * 500));
        sheet.setColumnWidth(17, (10 * 500));
        sheet.setColumnWidth(18, (10 * 500));
        sheet.setColumnWidth(19, (10 * 500));
        sheet.setColumnWidth(20, (10 * 500));
        sheet.setColumnWidth(21, (10 * 500));
        sheet.setColumnWidth(22, (10 * 500));
        sheet.setColumnWidth(23, (10 * 500));


        File folder = new File(Environment.getExternalStorageDirectory() + "/" + "Neospectra"); // Neospectra is folder name

        boolean isFolderCreated = false;
        if (!folder.exists()) {
            isFolderCreated = folder.mkdir(); //create folder if not exists
        }
        Log.d("CSC_TAG", "export To Excel: " + isFolderCreated);

        //file name
        String excelFileName = "Neospectra.xls";


        //complate path and name
        String filePathAndName = folder.toString() + "/" + excelFileName;


//        File file = new File(getActivity().getExternalFilesDir(null), "Neospectra.xls");
        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(filePathAndName);
            workbook.write(fileOutputStream);
            Toast.makeText(mContext, "Exported to : " + filePathAndName, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(mContext, "Neospectra.xls Not Created", Toast.LENGTH_SHORT).show();
            try {
                fileOutputStream.close();
            } catch (IOException ex) {

            }
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
        String csvFileName = "Neospectra.csv";

        //complate path and name
        String filePathAndName = folder.toString() + "/" + csvFileName;

        //get records
        ArrayList<ModelRecord> recordsList = new ArrayList<>();
        recordsList.clear();
        recordsList = dbHelper.getAllRecords(orderByOldest);

        try {
            //write csv file
            FileWriter fw = new FileWriter(filePathAndName);
            fw.append("Id,Bray,Ca,CLAY,C_N,HCL25_K2O,HCL25_P2O5,Jumlah,K,KB_ADJUSTED,Kjeldahl_N,KTK,Mg,Morgan,Na,Olsen_P2O5,PH_H2O,PH_KCL,RetensiP,Sand,Silt,WBC,Time");
            fw.append("\n");
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
                fw.append("" + recordsList.get(i).getHclk2o()); //kclk2o
                fw.append(",");
                fw.append("" + recordsList.get(i).getHclp2o5()); //hclp2o5
                fw.append(",");
                fw.append("" + recordsList.get(i).getJumlah()); //jumlah
                fw.append(",");
                fw.append("" + recordsList.get(i).getK()); //k
                fw.append(",");
                fw.append("" + recordsList.get(i).getKbadj()); //kbabj
                fw.append(",");
                fw.append("" + recordsList.get(i).getKjeldahl()); //kjeldahl
                fw.append(",");
                fw.append("" + recordsList.get(i).getKtk()); //ktk
                fw.append(",");
                fw.append("" + recordsList.get(i).getMg()); //mg
                fw.append(",");
                fw.append("" + recordsList.get(i).getMorgan()); //morgan
                fw.append(",");
                fw.append("" + recordsList.get(i).getNa()); //na
                fw.append(",");
                fw.append("" + recordsList.get(i).getOlsen()); //olsen
                fw.append(",");
                fw.append("" + recordsList.get(i).getPhh2o()); //phh2o
                fw.append(",");
                fw.append("" + recordsList.get(i).getPhkcl()); //phkcl
                fw.append(",");
                fw.append("" + recordsList.get(i).getRetensip()); //retensip
                fw.append(",");
                fw.append("" + recordsList.get(i).getSand()); //sand
                fw.append(",");
                fw.append("" + recordsList.get(i).getSilt()); //silt
                fw.append(",");
                fw.append("" + recordsList.get(i).getWbc()); //wbc
                fw.append(",");
                fw.append("" + recordsList.get(i).getRawdata()); //raw data
                fw.append(",");
                fw.append("" + recordsList.get(i).getAddedTime()); //addtime
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

    private void endActivity() {
        bluetoothAPI = null;
        Intent mIntent = new Intent(getActivity(), IntroActivity.class);
        startActivity(mIntent);
    }
}