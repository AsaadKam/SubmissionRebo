package com.example.challenge.ui;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.challenge.R;

import java.util.ArrayList;

public class CalculatorListAdapter extends RecyclerView.Adapter<CalculatorListAdapter.CalculatorViewHolder>
{
    private  ArrayList<CalculatorModel>  calculatorArrayList;
    public CalculatorListAdapter(ArrayList<CalculatorModel> passedCalculatorArrayList)
    {
        calculatorArrayList=passedCalculatorArrayList;
    }


    @NonNull
    @Override
    public CalculatorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.calculator_list_item,parent,false);
        return new CalculatorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalculatorViewHolder holder, int position)
    {

        holder.rVTextView.setText(Character.toString(calculatorArrayList.get(position).getChar_CalculatorModel_Operator())+Integer.toString(calculatorArrayList.get(position).getInt_CalculatorModel_Number()));

    }

    @Override
    public int getItemCount()
    {
        return calculatorArrayList.size();

    }



    public class CalculatorViewHolder extends RecyclerView.ViewHolder
    {
        TextView   rVTextView=null;

        public CalculatorViewHolder(@NonNull View itemView)
        {
            super(itemView);
            rVTextView=itemView.findViewById(R.id.rvItem);
        }

    }
}
