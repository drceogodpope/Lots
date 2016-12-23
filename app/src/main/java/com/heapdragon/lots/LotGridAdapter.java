//package com.heapdragon.lots;
//
//import android.content.Context;
//import android.graphics.drawable.Drawable;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//
///**
// * Created by Francesco on 2016-12-21.
// */
//
//public class LotGridAdapter extends BaseAdapter{
//
//    ArrayList<Lot> list;
//    Context context;
//
//    public LotGridAdapter(ArrayList<Lot> lots, Context context){
//        list = lots;
//        this.context = context;
//    }
//
//    @Override
//    public int getCount() {
//        return list.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return list.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return list.get(position).getNumber();
//    }
//
//
//    class Holder {
//        private TextView lotID;
//        public Holder(View view){
//            lotID = (TextView) view.findViewById(R.id.lot_number_textview);
//        }
//        public void setID(int i){
//            lotID.setText(String.valueOf(i));
//        }
//    }
//
//    @Override
//    public View getView(int position, final View convertView, ViewGroup parent) {
//        View cell = convertView;
//        Holder holder;
//        Drawable complete = cell.getResources().getDrawable(R.drawable.rounded_corner_complete);
//        Drawable incomplete = cell.getResources().getDrawable(R.drawable.rounded_corner_incomplete);
//        Drawable issue = cell.getResources().getDrawable(R.drawable.rounded_corner_issue);
//
//        if(cell == null) {
//            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            cell = inflater.inflate(R.layout.single_lot, parent, false);
//            holder = new Holder(cell);
//            cell.setTag(holder);
//        }
//        else {
//            holder = (Holder) cell.getTag();
//        }
//
//        holder.setID(position+1);
//
//        switch (list.get(position).getStatus()) {
//
//            case Lot.INCOMPLETE: cell.setBackground(incomplete);
//                break;
//            case Lot.COMPLETE: cell.setBackground(complete);
//                break;
//            default: cell.setBackground(issue);
//
//        }
//        return cell;
//    }
//}
//
