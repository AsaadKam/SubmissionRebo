package com.example.challenge.ui;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.challenge.R;

import java.util.ArrayList;

/**
 * This class is the adapter of the Recycler view
 */
public class CalculatorListAdapter extends RecyclerView.Adapter<CalculatorListAdapter.CalculatorViewHolder>
{
    /***
     * This array list that
     */
    private  ArrayList<CalculatorModel>  calculatorArrayList;

    /***
     * This is the constructor of the class
     * @param passedCalculatorArrayList
     */
    public CalculatorListAdapter(ArrayList<CalculatorModel> passedCalculatorArrayList)
    {
        calculatorArrayList=passedCalculatorArrayList;
    }

    /***
     *Here where the view is inflated
     * @param parent
     * @param viewType
     * @return
     */

    @NonNull
    @Override
    public CalculatorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.calculator_list_item,parent,false);
        return new CalculatorViewHolder(view);
    }

    /**
     * This method where the update of the view happens
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull CalculatorViewHolder holder, int position)
    {

        holder.rVTextView.setText(Character.toString(calculatorArrayList.get(position).getChar_CalculatorModel_Operator())+Integer.toString(calculatorArrayList.get(position).getInt_CalculatorModel_Number()));

    }

    /**
     * This method returns the size of list
     * @return
     */
    @Override
    public int getItemCount()
    {
        return calculatorArrayList.size();

    }

    /***
     * This class implements the interface of RecyclerView.ViewHolder to generate the view of the list
     */
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
