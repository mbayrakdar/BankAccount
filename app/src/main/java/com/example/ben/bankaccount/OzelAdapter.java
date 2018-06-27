package com.example.ben.bankaccount;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Mine BAYRAKDAR on 31.10.2017.
 */

public class OzelAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<Customer> customerList;
    Context context;

    public OzelAdapter(MainActivity activity, List<Customer> customerListe) {
        context = activity;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customerList = customerListe;
    }

    @Override
    public int getCount() {
        return customerList.size();
    }

    @Override
    public Customer getItem(int position) {
        return customerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listview;

        listview = mInflater.inflate(R.layout.listview, null);

        TextView txtName = (TextView) listview.findViewById(R.id.txtName);
        TextView txtDob = (TextView) listview.findViewById(R.id.txtDob);
        TextView txtBalance = (TextView) listview.findViewById(R.id.txtBalance);
        Customer customer = customerList.get(position);

        txtName.setText(customer.getName());
        txtDob.setText(customer.getDob());
        txtBalance.setText(customer.getBalance());
        return listview;
    }
}