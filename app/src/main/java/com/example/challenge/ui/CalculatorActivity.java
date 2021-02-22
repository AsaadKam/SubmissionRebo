package com.example.challenge.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;


import com.example.challenge.R;
import com.example.challenge.databinding.CalculatorActivityBinding;

import java.util.ArrayList;

/*****************************************************
 * This is the java code of the view
 * interaction with the fetched data
 * from everywhere and create the main ui
 ******************************************************/


public class CalculatorActivity extends AppCompatActivity
{

    private static final String TAG = "MainActivity";

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {


        /************Super onCreate Call**********/
        super.onCreate(savedInstanceState);
        /*****Activity binding of view model create*******/
        /******ViewModel init******/
        CalculatorViewModel  calculatorViewModel = new ViewModelProvider(this).get(CalculatorViewModel.class);
        CalculatorActivityBinding activityMainBinding = DataBindingUtil.setContentView(this, R.layout.calculator_activity);
        /******Passing binding*******/
        calculatorViewModel.init(activityMainBinding,this);
        /*****Create the linke between the xml and activity to let the clicking events and observing on xml****/
        activityMainBinding.setViewModel(calculatorViewModel);
        activityMainBinding.setLifecycleOwner(this);

        /******Intialization for RecyclerView******/
        ArrayList<CalculatorModel> calculatorViewList = new ArrayList<CalculatorModel>();
        CalculatorListAdapter calculatorListAdapter =new CalculatorListAdapter(calculatorViewList);
        activityMainBinding.recycler.setLayoutManager(new GridLayoutManager(getApplicationContext(),5));
        activityMainBinding.recycler.setAdapter(calculatorListAdapter);

        /******observe liveData of result*******/
        calculatorViewModel.ReturnRecycLerViewArrayList()
        .observe
        (
                this
                , new Observer<ArrayList<CalculatorModel>>()
                {
                    @Override
                    public void onChanged(ArrayList<CalculatorModel> calculatorModels)
                    {
                        calculatorViewList.clear();
                        calculatorListAdapter.notifyDataSetChanged();
                        calculatorViewList.addAll(calculatorModels);
                        calculatorListAdapter.notifyDataSetChanged();
                    }
                }
         );



    }


}