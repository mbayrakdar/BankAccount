package com.example.ben.bankaccount;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class AddCustomerFragment extends Fragment {

    View content;
    Button btnAdd, btnReset;
    EditText edtName, edtCredit;
    TextView  edtDob;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    String Name, Dob, Credit;
    SqliteDatabase sqliteDatabase;

    public AddCustomerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_customer, container, false);

        sqliteDatabase = new SqliteDatabase(getActivity());

        btnAdd = (Button) view.findViewById(R.id.btnAdd);
        btnReset = (Button) view.findViewById(R.id.btnReset);

        edtName = (EditText) view.findViewById(R.id.edtName);
        edtDob = (TextView) view.findViewById(R.id.edtDob);
        edtCredit = (EditText) view.findViewById(R.id.edtCredit);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Name = edtName.getText().toString();
                Dob = edtDob.getText().toString();
                Credit = edtCredit.getText().toString();

                // Add butonuna tıklandığında tüm alanlar doldurulmuşsa customer ekleniyor, dolmamışsa uyarı veriliyor.

                if (Name != "" && Dob != "" && Credit != ""){
                    sqliteDatabase.InsertCustomer(Name, Dob, Credit);
                    ListViewFragment listViewFragment = new ListViewFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    ft.replace(R.id.container, listViewFragment);
                    ft.commit();
                }
                else {
                    Toast.makeText(getActivity(), "Please enter all variables", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtName.setText("");
                edtDob.setText("");
                edtCredit.setText("");
            }
        });

        // tarih pickerı ile seçim yapılıyor

        edtDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        getActivity(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        // gün ay yıl formatı ayarlandı

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;

                String date = day + "/" + month + "/" + year;
                edtDob.setText(date);
            }
        };

        // fragment view döndürüldü
        return view;
    }
}