package com.si_ware.neospectra.Activities.Interfaces.DataPage;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.si_ware.neospectra.Activities.IntroActivity;
import com.si_ware.neospectra.BluetoothSDK.SWS_P3API;
import com.si_ware.neospectra.Data.DataElements;
import com.si_ware.neospectra.Data.ConfigurableProperties;
import com.si_ware.neospectra.Data.DefaultConfig;
import com.si_ware.neospectra.Models.DataElementViewModel;
import com.si_ware.neospectra.R;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.si_ware.neospectra.Global.GlobalVariables.bluetoothAPI;

public class DataPageFragment extends Fragment {
    private DataElementViewModel dataElemenview = new DataElementViewModel();
    private Context mContext;
    /* Variable for config popup */
    Button btnOk, btnClose;
    CheckBox checkall, chkphh20, chkphkcl, chkcorg, chkntot, chkp20shcl, chkk20hcl, chkp20sbray, chkp20solsen, chkca, chkmg, chkk, chkna, chkkb, chkktk, chkpasir, chkdebu, chkliat;
    /* Variable for text view */
    LinearLayout Layphh20, Layphkcl, Laycorg, Layntot, Layp20shcl, Layk20hcl, Layp20sbray, Layp20solsen, Layca, Laymg, Layk, Layna, Laykb, Layktk, Laypasir, Laydebu, Layliat;
    TextView txtPhh20, txtPhkcl, txtCorg, txtNtotal, txtp20sHcl, txtk20Hcl, txtp20sBray, txtP20Olsen, txtCa, txtMg, txtK, txtNa, txtKb, txtPasir, txtDebu, txtLiat, txtKTK;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data_page, container, false);
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

        /* Variable for text view*/
        txtPhh20 = view.findViewById(R.id.txtPhh20);
        txtPhkcl = view.findViewById(R.id.txtPhkcl);
        txtCorg = view.findViewById(R.id.txtCorg);
        txtNtotal = view.findViewById(R.id.txtNtotal);
        txtp20sHcl = view.findViewById(R.id.txtp20sHcl);
        txtk20Hcl = view.findViewById(R.id.txtk20Hcl);
        txtp20sBray = view.findViewById(R.id.txtp20sBray);
        txtP20Olsen = view.findViewById(R.id.txtP20Olsen);
        txtCa = view.findViewById(R.id.txtCa);
        txtMg = view.findViewById(R.id.txtMg);
        txtK = view.findViewById(R.id.txtK);
        txtNa = view.findViewById(R.id.txtNa);
        txtKb = view.findViewById(R.id.txtKb);
        txtKTK = view.findViewById(R.id.txtKTK);
        txtPasir = view.findViewById(R.id.txtPasir);
        txtDebu = view.findViewById(R.id.txtDebu);
        txtLiat = view.findViewById(R.id.txtLiat);

        Layphh20 = view.findViewById(R.id.Layphh20);
        Layphkcl = view.findViewById(R.id.Layphkcl);
        Laycorg = view.findViewById(R.id.Laycorg);
        Layntot = view.findViewById(R.id.Layntot);
        Layp20shcl = view.findViewById(R.id.Layp20shcl);
        Layk20hcl = view.findViewById(R.id.Layk20hcl);
        Layp20sbray = view.findViewById(R.id.Layp20sbray);
        Layp20solsen = view.findViewById(R.id.Layp20solsen);
        Layca = view.findViewById(R.id.Layca);
        Laymg = view.findViewById(R.id.Laymg);
        Layk = view.findViewById(R.id.Layk);
        Layna = view.findViewById(R.id.Layna);
        Laykb = view.findViewById(R.id.Laykb);
        Layktk = view.findViewById(R.id.Layktk);
        Laypasir = view.findViewById(R.id.Laypasir);
        Laydebu = view.findViewById(R.id.Laydebu);
        Layliat = view.findViewById(R.id.Layliat);

        SetConfigFile();
        SetViewLayout();
        setTextView();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            android.support.v7.app.AlertDialog.Builder myAlert = new android.support.v7.app.AlertDialog
                    .Builder(mContext);
            myAlert.setTitle("Disconnect");
            myAlert.setMessage("Are you sure you want to disconnect the device?");
            myAlert.setPositiveButton("OK", (dialogInterface, i) -> {
                dialogInterface.dismiss();
                // Request of bluetooth disconnection
                if (bluetoothAPI != null) {
                    bluetoothAPI.disconnectFromDevice();

                    Intent iMain = new Intent(mContext, IntroActivity.class);
                    iMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(iMain);
                }

            });
            myAlert.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss());
            myAlert.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem shareitem = menu.add(Menu.NONE, 101, 0, "Options");
        shareitem.setIcon(R.drawable.icons_setting);
        shareitem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
        shareitem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                final Dialog d = new Dialog(getActivity());
                d.setTitle("SETTING");
                d.setContentView(R.layout.dialog_layout);

                // Initialize config view
                checkall = d.findViewById(R.id.checallornot);
                chkphh20 = d.findViewById(R.id.chkphh20);
                chkphkcl = d.findViewById(R.id.chkphkcl);
                chkcorg = d.findViewById(R.id.chkcorg);
                chkntot = d.findViewById(R.id.chkntot);
                chkp20shcl = d.findViewById(R.id.chkp20shcl);
                chkk20hcl = d.findViewById(R.id.chkk20hcl);
                chkp20sbray = d.findViewById(R.id.chkp20sbray);
                chkp20solsen = d.findViewById(R.id.chkp20solsen);
                chkca = d.findViewById(R.id.chkca);
                chkmg = d.findViewById(R.id.chkmg);
                chkk = d.findViewById(R.id.chkk);
                chkna = d.findViewById(R.id.chkna);
                chkkb = d.findViewById(R.id.chkkb);
                chkktk = d.findViewById(R.id.chkktk);
                chkpasir = d.findViewById(R.id.chkpasir);
                chkdebu = d.findViewById(R.id.chkdebu);
                chkliat = d.findViewById(R.id.chkliat);
                btnOk = d.findViewById(R.id.btnOk);
                btnClose = d.findViewById(R.id.btnClose);

                // Set checklist base on config
                chkphh20.setChecked(dataElemenview.phh20);
                chkphkcl.setChecked(dataElemenview.phhkcl);
                chkcorg.setChecked(dataElemenview.corg);
                chkntot.setChecked(dataElemenview.ntot);
                chkp20shcl.setChecked(dataElemenview.p20shcl);
                chkk20hcl.setChecked(dataElemenview.k20hcl);
                chkp20sbray.setChecked(dataElemenview.p20sbray);
                chkp20solsen.setChecked(dataElemenview.p20solsen);
                chkca.setChecked(dataElemenview.ca);
                chkmg.setChecked(dataElemenview.mg);
                chkk.setChecked(dataElemenview.k);
                chkna.setChecked(dataElemenview.na);
                chkkb.setChecked(dataElemenview.kb);
                chkktk.setChecked(dataElemenview.ktk);
                chkpasir.setChecked(dataElemenview.pasir);
                chkdebu.setChecked(dataElemenview.debu);
                chkliat.setChecked(dataElemenview.liat);

                if (chkphh20.isChecked() &&
                        chkphkcl.isChecked() &&
                        chkcorg.isChecked() &&
                        chkntot.isChecked() &&
                        chkp20shcl.isChecked() &&
                        chkk20hcl.isChecked() &&
                        chkp20sbray.isChecked() &&
                        chkp20solsen.isChecked() &&
                        chkca.isChecked() &&
                        chkmg.isChecked() &&
                        chkk.isChecked() &&
                        chkna.isChecked() &&
                        chkkb.isChecked() &&
                        chkktk.isChecked() &&
                        chkpasir.isChecked() &&
                        chkdebu.isChecked() &&
                        chkliat.isChecked()) {
                    checkall.setText("Uncheck all");
                    checkall.setChecked(true);
                } else {
                    checkall.setText("Check all");
                    checkall.setChecked(false);
                }

                //Action check all
                checkall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!checkall.isChecked()) {
                            checkall.setText("Check all");
                            chkphh20.setChecked(false);
                            chkphkcl.setChecked(false);
                            chkcorg.setChecked(false);
                            chkntot.setChecked(false);
                            chkp20shcl.setChecked(false);
                            chkk20hcl.setChecked(false);
                            chkp20sbray.setChecked(false);
                            chkp20solsen.setChecked(false);
                            chkca.setChecked(false);
                            chkmg.setChecked(false);
                            chkk.setChecked(false);
                            chkna.setChecked(false);
                            chkkb.setChecked(false);
                            chkktk.setChecked(false);
                            chkpasir.setChecked(false);
                            chkdebu.setChecked(false);
                            chkliat.setChecked(false);
                        }
                        if (checkall.isChecked()) {
                            checkall.setText("Uncheck all");
                            chkphh20.setChecked(true);
                            chkphkcl.setChecked(true);
                            chkcorg.setChecked(true);
                            chkntot.setChecked(true);
                            chkp20shcl.setChecked(true);
                            chkk20hcl.setChecked(true);
                            chkp20sbray.setChecked(true);
                            chkp20solsen.setChecked(true);
                            chkca.setChecked(true);
                            chkmg.setChecked(true);
                            chkk.setChecked(true);
                            chkna.setChecked(true);
                            chkkb.setChecked(true);
                            chkktk.setChecked(true);
                            chkpasir.setChecked(true);
                            chkdebu.setChecked(true);
                            chkliat.setChecked(true);
                        }
                    }
                });

                //Action Close
                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();
                    }
                });

                //Action Ok
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!chkphh20.isChecked() &&
                                !chkphkcl.isChecked() &&
                                !chkcorg.isChecked() &&
                                !chkntot.isChecked() &&
                                !chkp20shcl.isChecked() &&
                                !chkk20hcl.isChecked() &&
                                !chkp20sbray.isChecked() &&
                                !chkp20solsen.isChecked() &&
                                !chkca.isChecked() &&
                                !chkmg.isChecked() &&
                                !chkk.isChecked() &&
                                !chkna.isChecked() &&
                                !chkkb.isChecked() &&
                                !chkktk.isChecked() &&
                                !chkpasir.isChecked() &&
                                !chkdebu.isChecked() &&
                                !chkliat.isChecked()) {
                            Toast.makeText(getActivity(), "Please check the list first", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        SaveViewLayoutConfiguration();
                        // Display dialog is gone
                        d.dismiss();
                    }
                });
                //SHOW DIALOG
                d.show();
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void setTextView() {
        /* set value textview */
        if (DataElements.getPhH2o() > 0) {
            txtPhh20.setText(String.format("%.2f", DataElements.getPhH2o()));
        } else {
            txtPhh20.setText(Float.toString(0));
        }
        if (DataElements.getPhKcl() > 0) {
            txtPhkcl.setText(String.format("%.2f", DataElements.getPhKcl()));
        } else {
            txtPhkcl.setText(Float.toString(0));
        }
        if (DataElements.getCN() > 0) {
            txtCorg.setText(String.format("%.2f", DataElements.getCN()));
        } else {
            txtCorg.setText(Float.toString(0));
        }
        if (DataElements.getKjeldahlN() > 0) {
            txtNtotal.setText(String.format("%.2f", DataElements.getKjeldahlN()));
        } else {
            txtNtotal.setText(Float.toString(0));
        }
        if (DataElements.getHCl25P2O5() > 0) {
            txtp20sHcl.setText(String.format("%.2f", DataElements.getHCl25P2O5()));
        } else {
            txtp20sHcl.setText(Float.toString(0));
        }
        if (DataElements.getHCl25K2O() > 0) {
            txtk20Hcl.setText(String.format("%.2f", DataElements.getHCl25K2O()));
        } else {
            txtk20Hcl.setText(Float.toString(0));
        }
        //String data1 = Float.toString(DataElements.getBray1P2O5());
        if (DataElements.getBray1P2O5() > 0) {
            txtp20sBray.setText(String.format("%.2f", DataElements.getBray1P2O5()));
        } else {
            txtp20sBray.setText(Float.toString(0));
        }
        if (DataElements.getOlsenP2O5() > 0) {
            txtP20Olsen.setText(String.format("%.2f", DataElements.getOlsenP2O5()));
        } else {
            txtP20Olsen.setText(Float.toString(0));
        }
        if (DataElements.getCa() > 0) {
            txtCa.setText(String.format("%.2f", DataElements.getCa()));
        } else {
            txtCa.setText(Float.toString(0));
        }
        if (DataElements.getMg() > 0) {
            txtMg.setText(String.format("%.2f", DataElements.getMg()));
        } else {
            txtMg.setText(Float.toString(0));
        }
        if (DataElements.getK() > 0) {
            txtK.setText(String.format("%.2f", DataElements.getK()));
        } else {
            txtK.setText(Float.toString(0));
        }
        if (DataElements.getNa() > 0) {
            txtNa.setText(String.format("%.2f", DataElements.getNa()));
        } else {
            txtNa.setText(Float.toString(0));
        }
        if (DataElements.getKBAdjusted() > 0) {
            txtKb.setText(String.format("%.2f", DataElements.getKBAdjusted()));
        } else {
            txtKb.setText(Float.toString(0));
        }
        if (DataElements.getKTK() > 0) {
            txtKTK.setText(String.format("%.2f", DataElements.getKTK()));
        } else {
            txtKTK.setText(Float.toString(0));
        }
        if (DataElements.getSAND() > 0) {
            txtPasir.setText(String.format("%.2f", DataElements.getSAND()));
        } else {
            txtPasir.setText(Float.toString(0));
        }
        if (DataElements.getSILT() > 0) {
            txtDebu.setText(String.format("%.2f", DataElements.getSILT()));
        } else {
            txtDebu.setText(Float.toString(0));
        }
        if (DataElements.getCLAY() > 0) {
            txtLiat.setText(String.format("%.2f", DataElements.getCLAY()));
        } else {
            txtLiat.setText(Float.toString(0));
        }
    }

    @Override
    public void onResume() {
        //comment or uncomment this function to test the aplication without connect to device
        if (bluetoothAPI != null) {
            if (!bluetoothAPI.isDeviceConnected()) {
                endActivity();
                return;
            }
        }
        super.onResume();
    }

    private void endActivity() {
        bluetoothAPI = null;
        Intent mIntent = new Intent(getActivity(), IntroActivity.class);
        startActivity(mIntent);
    }

    public void SetViewLayout() {
        if (!dataElemenview.phh20)
            this.Layphh20.setVisibility(View.GONE);
        else
            this.Layphh20.setVisibility(View.VISIBLE);

        if (!dataElemenview.phhkcl)
            this.Layphkcl.setVisibility(View.GONE);
        else
            this.Layphkcl.setVisibility(View.VISIBLE);

        if (!dataElemenview.corg)
            this.Laycorg.setVisibility(View.GONE);
        else
            this.Laycorg.setVisibility(View.VISIBLE);

        if (!dataElemenview.ntot)
            this.Layntot.setVisibility(View.GONE);
        else
            this.Layntot.setVisibility(View.VISIBLE);

        if (!dataElemenview.p20shcl)
            this.Layp20shcl.setVisibility(View.GONE);
        else
            this.Layp20shcl.setVisibility(View.VISIBLE);

        if (!dataElemenview.k20hcl)
            this.Layk20hcl.setVisibility(View.GONE);
        else
            this.Layk20hcl.setVisibility((View.VISIBLE));

        if (!dataElemenview.p20sbray)
            this.Layp20sbray.setVisibility(View.GONE);
        else
            this.Layp20sbray.setVisibility(View.VISIBLE);

        if (!dataElemenview.p20solsen)
            this.Layp20solsen.setVisibility(View.GONE);
        else
            this.Layp20solsen.setVisibility(View.VISIBLE);

        if (!dataElemenview.ca)
            this.Layca.setVisibility(View.GONE);
        else
            this.Layca.setVisibility(View.VISIBLE);

        if (!dataElemenview.mg)
            this.Laymg.setVisibility(View.GONE);
        else
            this.Laymg.setVisibility(View.VISIBLE);

        if (!dataElemenview.k)
            this.Layk.setVisibility(View.GONE);
        else
            this.Layk.setVisibility(View.VISIBLE);

        if (!dataElemenview.na)
            this.Layna.setVisibility(View.GONE);
        else
            this.Layna.setVisibility(View.VISIBLE);

        if (!dataElemenview.kb)
            this.Laykb.setVisibility(View.GONE);
        else
            this.Laykb.setVisibility(View.VISIBLE);

        if (!dataElemenview.ktk)
            this.Layktk.setVisibility(View.GONE);
        else
            this.Layktk.setVisibility(View.VISIBLE);

        if (!dataElemenview.pasir)
            this.Laypasir.setVisibility(View.GONE);
        else
            this.Laypasir.setVisibility(View.VISIBLE);

        if (!dataElemenview.debu)
            this.Laydebu.setVisibility(View.GONE);
        else
            this.Laydebu.setVisibility(View.VISIBLE);

        if (!dataElemenview.liat)
            this.Layliat.setVisibility(View.GONE);
        else
            this.Layliat.setVisibility(View.VISIBLE);
    }

    private void SetConfigFile() {
        // get file config
        String fileName = ConfigurableProperties.DataElementView;
        String fileValue;
        FileInputStream fis = null;
        try {
            fis = mContext.openFileInput(fileName);
            InputStreamReader isr =
                    new InputStreamReader(fis, StandardCharsets.UTF_8);
            StringBuilder sB = new StringBuilder();
            BufferedReader reader = new BufferedReader(isr);
            String line = reader.readLine();
            while (line != null) {
                sB.append(line).append('\n');
                line = reader.readLine();
            }
            fileValue = sB.toString();
            if (fileValue != null && !fileValue.isEmpty()) {
                Gson gson = new Gson();
                dataElemenview = gson.fromJson(fileValue, DataElementViewModel.class);
            }
        } catch (FileNotFoundException ex) {
            CreateConfigFile(null);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void CreateConfigFile(String data) {
        String fileName = ConfigurableProperties.DataElementView;
        String fileValue = DefaultConfig.ElemenView;
        if (data != null && !data.isEmpty())
            fileValue = data;
        FileOutputStream fos = null;
        try {
            fos = mContext.openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(fileValue.getBytes());
            if (fileValue != null && !fileValue.isEmpty()) {
                Gson gson = new Gson();
                dataElemenview = gson.fromJson(fileValue, DataElementViewModel.class);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void SaveViewLayoutConfiguration() {
        DataElementViewModel newData = new DataElementViewModel();
        newData.phh20 = this.chkphh20.isChecked();
        newData.phhkcl = this.chkphkcl.isChecked();
        newData.corg = this.chkcorg.isChecked();
        newData.ntot = this.chkntot.isChecked();
        newData.p20shcl = this.chkp20shcl.isChecked();
        newData.k20hcl = this.chkk20hcl.isChecked();
        newData.p20sbray = this.chkp20sbray.isChecked();
        newData.p20solsen = this.chkp20solsen.isChecked();
        newData.ca = this.chkca.isChecked();
        newData.mg = this.chkmg.isChecked();
        newData.k = this.chkk.isChecked();
        newData.na = this.chkna.isChecked();
        newData.kb = this.chkkb.isChecked();
        newData.ktk = this.chkktk.isChecked();
        newData.pasir = this.chkpasir.isChecked();
        newData.debu = this.chkdebu.isChecked();
        newData.liat = this.chkliat.isChecked();

        Gson gson = new Gson();
        String data = gson.toJson(newData);
        CreateConfigFile(data);
        SetViewLayout();
    }
}