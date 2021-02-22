package com.example.challenge.ui;


import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;


import com.example.challenge.R;
import com.example.challenge.databinding.CalculatorActivityBinding;

import java.util.ArrayList;


public class CalculatorViewModel extends ViewModel
{
    private final static String TAG = "VIEWMODEL";
    private ArrayList<CalculatorModel> cvmIntegerRedoList = new ArrayList<CalculatorModel>();
    private int intergerClickedPosition = 0;
    private int intResultBuffer = 0;
    private int integerSecondOperandBuffer = 0;
    private char characterOperatorBuffer = '-';
    private MutableLiveData<ArrayList<CalculatorModel>> cvmRecycLerViewMutuableLiveData = new MutableLiveData<ArrayList<CalculatorModel>>();
    private MutableLiveData<String> cvmResultString = new MutableLiveData<String>();
    private ArrayList<CalculatorModel> cvmRecycLerViewArrayList = new ArrayList<CalculatorModel>();
    private CalculatorActivityBinding cvmCalculatorActivityBinding = null;
    private Context cvmContext=null;


    public void init(CalculatorActivityBinding activityBinding, Context Passedcontext)
    {

        cvmContext=Passedcontext;
        /*****Binding init***********/
        cvmCalculatorActivityBinding = activityBinding;
        /*******Initialization*********/
        initalizationActions();
        /******When the text has number we enable the equal to be clicked*********/
        cvmCalculatorActivityBinding.Entry.addTextChangedListener
                (
                        new TextWatcher()
                        {

                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                /****************Check whether the entered value is numbers or not******************/
                                if (TextUtils.isDigitsOnly(cvmCalculatorActivityBinding.Entry.getText())) {
                                    /************check if the length is zero**************/
                                    if (cvmCalculatorActivityBinding.Entry.getText().length() == 0) {
                                        cvmCalculatorActivityBinding.Equal.setEnabled(false);
                                    } else {
                                        cvmCalculatorActivityBinding.Equal.setEnabled(true);
                                    }
                                } else {
                                    cvmCalculatorActivityBinding.Equal.setEnabled(false);
                                }
                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        }

                );
        cvmCalculatorActivityBinding.recycler.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() 
        {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e)
            {
                if (e.getAction() != MotionEvent.ACTION_UP)
                {
                    return false;
                }
                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null)
                {

                    // tapped on child
                    for(int i=cvmRecycLerViewArrayList.size()-1;i>=rv.getChildLayoutPosition(child);i--)
                    {
                        characterOperatorBuffer = cvmRecycLerViewGetTheOperator(i);
                        integerSecondOperandBuffer = cvmRecycLerViewGetTheNumber(i);
                        cvmAddRedoArrayList(characterOperatorBuffer, integerSecondOperandBuffer);

                        if (cvmRecycLerViewArrayList.size() != 0)
                        {
                            intResultBuffer=cvmCalc_Undo_Operation(characterOperatorBuffer,integerSecondOperandBuffer,intResultBuffer);
                            cvmResultString.setValue(Integer.toString(intResultBuffer));
                            cvmRemoveRecyclerViewLastItem();
                            cvmUndoUiEffect();
                        } else
                        {

                        }
                    }

                    return false;
                }
                else
                {
                    // Tap occured outside all child-views.
                    // do something
                    return true;
                }
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e)
            {


            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }

    public void onClick(View view)
    {

        switch (view.getId())
        {
            case R.id.Minus:

                /******Disable operators and enable the entry******/
                cvmOperatorsUiEffect();
                /*****Make the value of character****/
                characterOperatorBuffer = '-';

                break;

            case R.id.Plus:

                /******Disable operators and enable the entry******/
                cvmOperatorsUiEffect();
                /*****Make the value of character****/
                characterOperatorBuffer = '+';
                break;


            case R.id.Div:

                /******Disable operators and enable the entry******/
                cvmOperatorsUiEffect();
                /*****Make the value of character****/
                characterOperatorBuffer = '/';
                break;

            case R.id.Mult:

                /******Disable operators and enable the entry******/
                cvmOperatorsUiEffect();
                /*****Make the value of character****/
                characterOperatorBuffer = '*';
                break;

            case R.id.Redo:


                integerSecondOperandBuffer = cvmRedoArrayListGetTheNumber();
                characterOperatorBuffer = cvmRedoArrayListGetTheOperator();

                cvmRemoveLastItemRedoArrayList();
                if(cvmIntegerRedoList.size()==0)
                {
                    cvmCalculatorActivityBinding.Redo.setEnabled(false);
                }
                cvmAddRecyclerViewItem(characterOperatorBuffer, integerSecondOperandBuffer);
                intResultBuffer=cvmCalc_Operation(characterOperatorBuffer,integerSecondOperandBuffer,intResultBuffer);
                cvmResultString.setValue(Integer.toString(intResultBuffer));
                cvmResultString.setValue(Integer.toString(intResultBuffer));
                cvmCalculatorActivityBinding.Undo.setEnabled(true);



                break;

            case R.id.Undo:

                characterOperatorBuffer = cvmRecycLerViewGetTheOperator();
                integerSecondOperandBuffer = cvmRecycLerViewGetTheNumber();
                cvmAddRedoArrayList(characterOperatorBuffer, integerSecondOperandBuffer);

                if (cvmRecycLerViewArrayList.size() != 0)
                {
                    intResultBuffer=cvmCalc_Undo_Operation(characterOperatorBuffer,integerSecondOperandBuffer,intResultBuffer);
                    cvmResultString.setValue(Integer.toString(intResultBuffer));
                    cvmRemoveRecyclerViewLastItem();
                    cvmUndoUiEffect();
                } else
                {

                }


                break;

            case R.id.Equal:

                /*****Change value of integer passed as 2nd operand****/
                integerSecondOperandBuffer = cvmGetIntNumberFromEntry();
                /*******Save the update on Mutual Data to be shown on the UI********/
                cvmAddRecyclerViewItem(characterOperatorBuffer, integerSecondOperandBuffer);
                /******Disable equal and remove number from edit text and write the default hint******/
                cvmEqualUiEffect();
                /**********Here is the operation*********/
                intResultBuffer=cvmCalc_Operation(characterOperatorBuffer,integerSecondOperandBuffer,intResultBuffer);
                cvmResultString.setValue(Integer.toString(intResultBuffer));

                break;


        }
    }
    /**Testable***/
    public LiveData<String> ReturnResultLiveData()
    {
        return cvmResultString;
    }
    /**Testable***/
    public LiveData<ArrayList<CalculatorModel>> ReturnRecycLerViewArrayList() {
        return cvmRecycLerViewMutuableLiveData;
    }
    /**Testable***/
    private int cvmCalc_Undo_Operation(char c, int number, int LastValueOfSum) {
        switch (c) {
            case '+':
                LastValueOfSum -= number;
                break;
            case '-':
                LastValueOfSum += number;

                break;
            case '*':
                LastValueOfSum /= number;

                break;
            case '/':
                LastValueOfSum *= number;

                break;
        }
        return LastValueOfSum;

    }
    /**Testable***/
    private int  cvmCalc_Operation(char c, int number,int LastValueOfSum) {
        switch (c) {
            case '+':
                LastValueOfSum += number;
                break;
            case '-':
                LastValueOfSum -= number;

                break;
            case '*':
                LastValueOfSum *= number;

                break;
            case '/':
                LastValueOfSum /= number;

                break;
        }

        return LastValueOfSum;
    }


    /**Testable***/
    private int cvmGetIntNumberFromEntry() {
        return Integer.parseInt(String.valueOf(cvmCalculatorActivityBinding.Entry.getText()));
    }

    private void initalizationActions()
    {
        /******Close The keyboard of editText *******/
        cvmCloseEditTextKeyboard();
        /*****Make result value is zero******/
        cvmResultString.setValue("0");
        /******Disable equal,undo,entry,redo buttons and enable the rest******/
        cvmCalculatorActivityBinding.Entry.setEnabled(false);
        cvmCalculatorActivityBinding.Equal.setEnabled(false);
        cvmCalculatorActivityBinding.Undo.setEnabled(false);
        cvmCalculatorActivityBinding.Redo.setEnabled(false);
        cvmCalculatorActivityBinding.Plus.setEnabled(true);
        cvmCalculatorActivityBinding.Minus.setEnabled(true);
        cvmCalculatorActivityBinding.Mult.setEnabled(true);
        cvmCalculatorActivityBinding.Div.setEnabled(true);

    }
    /**Testable***/
    private char cvmRecycLerViewGetTheOperator() {
        return cvmRecycLerViewArrayList.get(cvmRecycLerViewArrayList.size() - 1).getChar_CalculatorModel_Operator();
    }
    /**Testable***/
    private int cvmRecycLerViewGetTheNumber() {
        return cvmRecycLerViewArrayList.get(cvmRecycLerViewArrayList.size() - 1).getInt_CalculatorModel_Number();
    }
    /**Testable***/
    private char cvmRecycLerViewGetTheOperator(int index) {
        return cvmRecycLerViewArrayList.get(index).getChar_CalculatorModel_Operator();
    }
    /**Testable***/
    private int cvmRecycLerViewGetTheNumber(int index) {
        return cvmRecycLerViewArrayList.get(index).getInt_CalculatorModel_Number();
    }
    /**Testable***/
    private int cvmRedoArrayListGetTheNumber()
    {
        return cvmIntegerRedoList.get(cvmIntegerRedoList.size() - 1).getInt_CalculatorModel_Number();

    }
    /**Testable***/
    private char cvmRedoArrayListGetTheOperator() {
        return cvmIntegerRedoList.get(cvmIntegerRedoList.size() - 1).getChar_CalculatorModel_Operator();
    }

    private void cvmAddRecyclerViewItem( int itemIndex,char operator,int number )
    {
        cvmRecycLerViewArrayList.add(itemIndex,new CalculatorModel(operator,number));
        cvmRecycLerViewMutuableLiveData.setValue(cvmRecycLerViewArrayList);
    }
    private void cvmAddRecyclerViewItem(char operator,int number )
    {
        cvmRecycLerViewArrayList.add(new CalculatorModel(operator,number) );
        cvmRecycLerViewMutuableLiveData.setValue(cvmRecycLerViewArrayList);
    }
    private void cvmRemoveRecyclerViewItem( char operator,int number )
    {

        cvmRecycLerViewArrayList.remove(new CalculatorModel(operator,number)  );
        cvmRecycLerViewMutuableLiveData.setValue(cvmRecycLerViewArrayList);
    }
    private void cvmRemoveRecyclerViewLastItem()
    {
        cvmRecycLerViewArrayList.remove(cvmRecycLerViewArrayList.size()-1);
        cvmRecycLerViewMutuableLiveData.setValue(cvmRecycLerViewArrayList);
    }

    private void cvmRemoveRecyclerViewItem( int itemIndex)
    {
        cvmRecycLerViewArrayList.remove(itemIndex);
        cvmRecycLerViewMutuableLiveData.setValue(cvmRecycLerViewArrayList);
    }
    private void cvmAddRedoArrayList( int itemIndex,char operator,int number )
    {
        cvmIntegerRedoList.add(itemIndex,new CalculatorModel(operator,number));
    }
    private void cvmAddRedoArrayList(char operator,int number )
    {
        cvmIntegerRedoList.add(new CalculatorModel(operator,number) );
    }
    private void cvmRemoveLastItemRedoArrayList()
    {
        cvmIntegerRedoList.remove(cvmIntegerRedoList.size()-1 );
    }
    private void cvmRemoveRedoArrayList( char operator,int number )
    {
        cvmIntegerRedoList.remove(new CalculatorModel(operator,number)  );
    }
    private void cvmRemoveRedoArrayList( int itemIndex)
    {
        cvmIntegerRedoList.remove(itemIndex);
    }
    private void cvmOperatorsUiEffect()
    {
        /******Disable operators and enable the entry******/

        cvmCalculatorActivityBinding.Entry.setHint("Enter the second operand.");
        cvmCalculatorActivityBinding.Entry.setEnabled(true);
        cvmCalculatorActivityBinding.Plus.setEnabled(false);
        cvmCalculatorActivityBinding.Minus.setEnabled(false);
        cvmCalculatorActivityBinding.Mult.setEnabled(false);
        cvmCalculatorActivityBinding.Div.setEnabled(false);

        /************Check if Redo list contains data ,if it contains remove all the data*************/
        if (cvmIntegerRedoList.size() != 0) {
            cvmIntegerRedoList.clear();
            cvmCalculatorActivityBinding.Redo.setEnabled(false);
        }
        /********Open the kepboard of EditText**********/
        cvmShowdEditTextKeyboard();
    }
    private void cvmEqualUiEffect()
    {
        /******Disable operators and enable the entry******/
        cvmCalculatorActivityBinding.Equal.setEnabled(false);
        cvmCalculatorActivityBinding.Entry.setText("");
        cvmCalculatorActivityBinding.Entry.setHint("Enter the operator.");

        cvmCalculatorActivityBinding.Entry.setEnabled(false);
        cvmCalculatorActivityBinding.Plus.setEnabled(true);
        cvmCalculatorActivityBinding.Minus.setEnabled(true);
        cvmCalculatorActivityBinding.Mult.setEnabled(true);
        cvmCalculatorActivityBinding.Div.setEnabled(true);
        cvmCalculatorActivityBinding.Undo.setEnabled(true);



    }
    private void cvmUndoUiEffect()
    {
        /******Disable operators and enable the entry******/
        cvmCalculatorActivityBinding.Redo.setEnabled(true);
        if(cvmRecycLerViewArrayList.size()==0)
        {
            cvmCalculatorActivityBinding.Undo.setEnabled(false);
        }
    }


    private void cvmShowdEditTextKeyboard()
    {
        cvmCalculatorActivityBinding.Entry.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) cvmContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(cvmCalculatorActivityBinding.Entry, InputMethodManager.SHOW_IMPLICIT);
    }

    private void cvmCloseEditTextKeyboard()
    {
        cvmCalculatorActivityBinding.Entry.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) cvmContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

    }
}
