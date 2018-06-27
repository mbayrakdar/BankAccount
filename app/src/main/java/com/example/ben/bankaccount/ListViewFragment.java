package com.example.ben.bankaccount;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ListViewFragment extends Fragment {

    View content;

    EditText updName, updCredit;
    TextView updDob;

    List<Customer> customers = new ArrayList<Customer>();
    List<String> customerList;
    String customerid, name, dob, balance;
    public ListView lv;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    String customerID;

    SqliteDatabase sqliteDatabase;

    public ListViewFragment() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_listview, container, false);

        sqliteDatabase = new SqliteDatabase(getActivity());
        lv = (ListView) view.findViewById(R.id.listView);

        customerList = sqliteDatabase.CustomerList();

        // herhangi bir customer var mı yok mu kontrol ediliyor

        if (customerList.size() == 0) {
            Toast.makeText(getActivity(), "No Customer Records", Toast.LENGTH_SHORT).show();
        }
        else {
            for (int i = 0; i < customerList.size(); i++) {
                customerid = customerList.get(i).split("-")[0];
                name = customerList.get(i).split("-")[1];
                dob = customerList.get(i).split("-")[2];
                balance = customerList.get(i).split("-")[3];
                customers.add(new Customer(customerid + "  " + name, dob, balance));
            }

            // var ise OzelAdapter classında tanımlanmış adaptere set ediliyor.
            lv.setAdapter(new OzelAdapter((MainActivity) getActivity(), customers));

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    List<String> list = sqliteDatabase.CustomerList();
                    // update delete get gibi işlemlerin yapılabilmesi için clicklenen satırın customerID'si alındı
                    customerID = list.get(position).split("-")[0];
                    String customer = sqliteDatabase.GetCustomer(customerID).toString();

                    // update işlemi için gereken dialog oluşturuldu
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                    View dialogView = inflater.inflate(R.layout.custom_dialog, null);
                    dialogBuilder.setView(dialogView);

                    updName = (EditText) dialogView.findViewById(R.id.updName);
                    updCredit = (EditText) dialogView.findViewById(R.id.updCredit);
                    updDob = (TextView) dialogView.findViewById(R.id.updDob);

                    // update edilecek kaydın bilgileri düzenlenmek üzere inputlara yazıldı
                    updName.setText(customer.split("-")[0]);
                    updDob.setText(customer.split("-")[1]);
                    updCredit.setText(customer.split("-")[2]);

                    // tarih seçildi
                    updDob.setOnClickListener(new View.OnClickListener() {
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

                    // gün ay yıl formatı yapıldı
                    mDateSetListener = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            month = month + 1;
                            String date = day + "/" + month + "/" + year;
                            updDob.setText(date);
                        }
                    };

                    // dialogta cancel ve update butonlarının işlevleri ayarlanıyor
                    dialogBuilder.setTitle("Update Customer");
                    dialogBuilder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // inputların boş girilmemesi için kontrol ediliyor
                            if (updName.getText().toString() != "" && updDob.getText().toString() != "" && updCredit.getText().toString() != ""){
                                sqliteDatabase.UpdateCustomer(customerID, updName.getText().toString(), updDob.getText().toString(), updCredit.getText().toString());
                                customerList = sqliteDatabase.CustomerList();
                                customers.clear();
                                for (int i = 0; i < customerList.size(); i++) {
                                    customerid = customerList.get(i).split("-")[0];
                                    name = customerList.get(i).split("-")[1];
                                    dob = customerList.get(i).split("-")[2];
                                    balance = customerList.get(i).split("-")[3];
                                    customers.add(new Customer(customerid + "  " + name, dob, balance));
                                }
                                lv.setAdapter(new OzelAdapter((MainActivity) getActivity(), customers));
                            }
                            else {
                                Toast.makeText(getActivity(), "Please enter all variables", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog b = dialogBuilder.create();
                    b.show();
                }
            });

            // uzun basıldığında silme işlemi gerçekleşmesi için gerekenler
            lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    List<String> list = sqliteDatabase.CustomerList();
                    customerID = list.get(position).split("-")[0];
                    // silme işlemi için dialog ayarlanıyor
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                    dialogBuilder.setTitle("Delete Customer");
                    dialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            sqliteDatabase.DeleteCustomer(customerID);
                            customerList = sqliteDatabase.CustomerList();
                            customers.clear();
                            for (int i = 0; i < customerList.size(); i++) {
                                customerid = customerList.get(i).split("-")[0];
                                name = customerList.get(i).split("-")[1];
                                dob = customerList.get(i).split("-")[2];
                                balance = customerList.get(i).split("-")[3];
                                customers.add(new Customer(customerid + "  " + name, dob, balance));
                            }
                            lv.setAdapter(new OzelAdapter((MainActivity) getActivity(), customers));

                            // silme işleminden sonra liste güncelleniyor

                        }
                    });
                    dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog b = dialogBuilder.create();
                    b.show();
                    return true;
                }
            });

        }
        return view;
    }

}