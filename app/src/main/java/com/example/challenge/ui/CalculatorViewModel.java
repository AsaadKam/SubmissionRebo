package com.example.challenge.ui;


import android.content.Context;
import android.inputmethodservice.ExtractEditText;
import android.renderscript.ScriptGroup;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;


import com.example.challenge.R;
import com.example.challenge.databinding.CalculatorActivityBinding;

import java.util.ArrayList;

/**
 * Class of calculatorViewModel that handles the interaction between input and UI
 * and it deals with CalculatorViewModelFunction as a helper clas
 * 
 */
public class CalculatorViewModel extends ViewModel
{
    /**
     * ArrayList that handles the act the the memory for redoing
     */
    private ArrayList<CalculatorModel> cvmRedoRecycLerViewArrayList = new ArrayList<CalculatorModel>();
    /**
     * ArrayList that handles array of lists for carrying images
     */
    private ArrayList<ArrayList<CalculatorModel>> cvmRecycLerViewArrayListImages = new ArrayList<ArrayList<CalculatorModel>>();
    /**
     * ArrayList that handles the is send via LiveDate to be drawn in class
     */
    private ArrayList<CalculatorModel> cvmRecycLerViewArrayList = new ArrayList<CalculatorModel>();
    /**
     * It is an integer that
     */
    private int cvmIntergerClickedPosition = 0;
    /**
     * It is an integer that
     */
    private double cvmDoubleResultBuffer = 0;
    /**
     * It is an integer that act as for Second Operand as buffer
     */
    private int cvmIntegerSecondOperandBuffer = 0;
    /**
     * It is a character that act as a buffer for the operators
     */
    private char cvmCharacterOperatorBuffer = '-';
    /**
     * It is a MutableLiveData that is sent to the Ui  for ArrayList
     */
    private MutableLiveData<ArrayList<CalculatorModel>> cvmMutuableLiveDataListRecycLer = new MutableLiveData<ArrayList<CalculatorModel>>();
    /**
     * It is a MutableLiveData that is sent to the Ui  for string
     */
    private MutableLiveData<String> cvmMutuableLiveDataResultString = new MutableLiveData<String>();
    /**
     * It is a CalculatorActivityBinding the generated class by ViewModel of Calaculator Activity
     */
    private CalculatorActivityBinding cvmBindingCalculatorActivity = null;
    /**
     * It is Context object
     */
    private Context cvmContext=null;
    /**
     * It is helper class that used to abstract the calculatoViewModel and handle for it the ambiguous things
     */
    private CalculatorViewModelFunctions cvmCalculatorViewModelFunctions=null;

    /**
     * It is a method that takes the binding and passed
     * context from Calculator UI and passed it to
     * CalculatorViewModel also it init the viewMddel
     * and implement the listener of editText called entry
     * @param activityBinding
     * @param passedcontext
     */
    public void init(CalculatorActivityBinding activityBinding, Context passedcontext)
    {
        /*Init Context*/
        cvmContext=passedcontext;

        /*****Binding init***********/
        cvmBindingCalculatorActivity = activityBinding;
        /*******Initialization*********/
        cvmCalculatorViewModelFunctions=new CalculatorViewModelFunctions(cvmBindingCalculatorActivity,passedcontext);
        /*****Make result value is zero******/
        cvmMutuableLiveDataResultString.setValue("0");
        /******When the text has number we enable the equal to be clicked*********/
        cvmBindingCalculatorActivity.Entry.addTextChangedListener
        (
                new TextWatcher()
                {

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after)
                    {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count)
                    {
                        /****************Check whether the entered value is numbers or not******************/

                        String stringBuffer= String.valueOf(cvmBindingCalculatorActivity.Entry.getText());
                        if(cvmCalculatorViewModelFunctions.isThatNumber(stringBuffer) )
                        {

                            cvmBindingCalculatorActivity.Equal.setEnabled(true);
                        }
                        else
                        {
                            cvmBindingCalculatorActivity.Equal.setEnabled(false);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s)
                    {

                    }
                }

        );
        /***This is the implementation of the Gridlist on Clicking listener*******/
        cvmBindingCalculatorActivity.recycler.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() 
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

                        cvmCharacterOperatorBuffer = cvmCalculatorViewModelFunctions.getTheLastOperatorOfRecycLerViewList(cvmRecycLerViewArrayList);
                        cvmIntegerSecondOperandBuffer = cvmCalculatorViewModelFunctions.getTheLastNumberOfRecycLerViewList(cvmRecycLerViewArrayList);
                        cvmCalculatorViewModelFunctions.pushPassedOperatorAndNumberOnRedoArrayList(cvmCharacterOperatorBuffer, cvmIntegerSecondOperandBuffer,cvmRedoRecycLerViewArrayList);

                        if (cvmRecycLerViewArrayList.size() != 0)
                        {
                            cvmDoubleResultBuffer=cvmCalculatorViewModelFunctions.calc_Undo_Operation(cvmCharacterOperatorBuffer,cvmIntegerSecondOperandBuffer,(int)cvmDoubleResultBuffer);
                            cvmMutuableLiveDataResultString.setValue(Integer.toString((int) cvmDoubleResultBuffer));
                            cvmCalculatorViewModelFunctions.removeTheLastItemOfRecyclerView(cvmRecycLerViewArrayList);
                            cvmMutuableLiveDataListRecycLer.setValue(cvmRecycLerViewArrayList);
                            cvmCalculatorViewModelFunctions.undoUiEffect(cvmRecycLerViewArrayList);
                        } else
                        {

                        }
                    }
                    cvmMutuableLiveDataListRecycLer.setValue(cvmRecycLerViewArrayList);

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

    /**
     * This is the ReturnResultLiveData that returns LiveData for result string to be linked with UI of textview
     * @return
     */
    public LiveData<String> ReturnResultLiveData()
    {
        return cvmMutuableLiveDataResultString;
    }
    /**
     * This is the ReturnRecycLerViewArrayList that returns LiveData for arraylist to be linked with UI of list
     * @return
     */
    public LiveData<ArrayList<CalculatorModel>> ReturnRecycLerViewArrayList()
    {
        return cvmMutuableLiveDataListRecycLer;
    }

    /**
     * onClick method that deals with clicks on input views
     * @param view
     */
    public void onClick(View view)
    {


        switch (view.getId())
        {
            /**
             * Case of clicking on minus button
             */
            case R.id.Minus:

                /******Disable operators and enable the entry******/
                cvmCalculatorViewModelFunctions.operatorsUiEffect(cvmRedoRecycLerViewArrayList);
                /*****Make the value of character****/
                cvmCharacterOperatorBuffer = '-';

                break;
            /**
             * Case of clicking on plus button
             */
            case R.id.Plus:

                /******Disable operators and enable the entry******/
                cvmCalculatorViewModelFunctions.operatorsUiEffect(cvmRedoRecycLerViewArrayList);
                /*****Make the value of character****/
                cvmCharacterOperatorBuffer = '+';
                break;

            /**
             * Case of clicking on div button
             */
            case R.id.Div:

                /******Disable operators and enable the entry******/
                cvmCalculatorViewModelFunctions.operatorsUiEffect(cvmRedoRecycLerViewArrayList);
                /*****Make the value of character****/
                cvmCharacterOperatorBuffer = '/';
                break;
            /**
             * Case of clicking on mult button
             */
            case R.id.Mult:

                /******Disable operators and enable the entry******/
                cvmCalculatorViewModelFunctions.operatorsUiEffect(cvmRedoRecycLerViewArrayList);
                /*****Make the value of character****/
                cvmCharacterOperatorBuffer = '*';
                break;
            /**
             * Case of clicking on redo button
             */
            case R.id.Redo:


                /**Receive the calculator operators and numbers and pushes the operators and integer to the list of redo**/
                cvmIntegerSecondOperandBuffer = cvmCalculatorViewModelFunctions.getTheNumberRedoArrayList(cvmRedoRecycLerViewArrayList);
                cvmCharacterOperatorBuffer = cvmCalculatorViewModelFunctions.getTheLastOperatorOfRedoArrayList(cvmRedoRecycLerViewArrayList);
                /****Removes the operator and second operand on the peak of the arraylist of Redo Array List***/
                cvmCalculatorViewModelFunctions.removeLastItemOfRedoArrayList(cvmRedoRecycLerViewArrayList);
                /******if the RedoRecyclerViewArrayList is empty**/
                if(cvmRedoRecycLerViewArrayList.size()==0)
                {
                    /***Disable the Redo Button***/
                    cvmBindingCalculatorActivity.Redo.setEnabled(false);
                }
                /****Pushes the operator and second operand on the peak of the RecyclerViewList***/
                cvmCalculatorViewModelFunctions.pushTheNumberAndOperatorOnRecyclerViewList(cvmCharacterOperatorBuffer, cvmIntegerSecondOperandBuffer,cvmRecycLerViewArrayList);
                /**Set the data of list to be observed by UI **/
                cvmMutuableLiveDataListRecycLer.setValue(cvmRecycLerViewArrayList);
                /******Do the default calculation operation*****/
                cvmDoubleResultBuffer=cvmCalculatorViewModelFunctions.calc_Operation(cvmCharacterOperatorBuffer,cvmIntegerSecondOperandBuffer,(int)cvmDoubleResultBuffer);
                /**Set the data of Result sumation to be observed by UI **/
                cvmMutuableLiveDataResultString.setValue(Integer.toString((int)cvmDoubleResultBuffer));
                /*****Endables the undo button****/
                cvmBindingCalculatorActivity.Undo.setEnabled(true);

                break;
            /**
             * Case of clicking on undo button
             */
            case R.id.Undo:

                /**Receive the calculator operators and numbers and pushes the operators and integer to the list of redo**/
                cvmCharacterOperatorBuffer = cvmCalculatorViewModelFunctions.getTheLastOperatorOfRecycLerViewList(cvmRecycLerViewArrayList);
                cvmIntegerSecondOperandBuffer = cvmCalculatorViewModelFunctions.getTheLastNumberOfRecycLerViewList(cvmRecycLerViewArrayList);
                /****Pushes the operator and second operand on the peak of the arraylist of Redo Array List***/
                cvmCalculatorViewModelFunctions.pushPassedOperatorAndNumberOnRedoArrayList(cvmCharacterOperatorBuffer, cvmIntegerSecondOperandBuffer,cvmRedoRecycLerViewArrayList);
                /**If the size of recyclerView arraylist is not equal zero**/
                if (cvmRecycLerViewArrayList.size() != 0)
                {
                    /**Doing undoOperation and return sum**/
                    cvmDoubleResultBuffer=cvmCalculatorViewModelFunctions.calc_Undo_Operation(cvmCharacterOperatorBuffer,cvmIntegerSecondOperandBuffer,(int)cvmDoubleResultBuffer);
                    /**Set the data of Result sumation to be observed by UI **/
                    cvmMutuableLiveDataResultString.setValue(Integer.toString((int)cvmDoubleResultBuffer));
                    /*******Remove the last item on recyclerView******/
                    cvmCalculatorViewModelFunctions.removeTheLastItemOfRecyclerView(cvmRecycLerViewArrayList);
                    /**Set the data of list to be observed by UI **/
                    cvmMutuableLiveDataListRecycLer.setValue(cvmRecycLerViewArrayList);
                    /****Do the undo effects on UI ***/
                    cvmCalculatorViewModelFunctions.undoUiEffect(cvmRecycLerViewArrayList);
                }
                break;
            /**
             * Case of clicking on equal button
             */
            case R.id.Equal:

                /*****Change value of integer passed as 2nd operand****/
                cvmIntegerSecondOperandBuffer = cvmCalculatorViewModelFunctions.getIntNumberFromEntry();
                /*****In case division by zero****/
                if(cvmCharacterOperatorBuffer=='/' && cvmIntegerSecondOperandBuffer==0)
                {
                    Toast.makeText(cvmContext, "Division by zero prohibted", Toast.LENGTH_SHORT).show();
                    cvmCalculatorViewModelFunctions.equalWhenThereIsNoDivisonByZeorUiEffect();
                }
                else
                {
                    /*******Save the update on Mutual Data to be shown on the UI********/
                    cvmCalculatorViewModelFunctions.pushTheNumberAndOperatorOnRecyclerViewList(cvmCharacterOperatorBuffer, cvmIntegerSecondOperandBuffer, cvmRecycLerViewArrayList);
                    cvmMutuableLiveDataListRecycLer.setValue(cvmRecycLerViewArrayList);


                    /**********Here is the operation*********/
                    cvmDoubleResultBuffer = cvmCalculatorViewModelFunctions.calc_Operation(cvmCharacterOperatorBuffer, cvmIntegerSecondOperandBuffer, (int) cvmDoubleResultBuffer);
                    cvmMutuableLiveDataResultString.setValue(Integer.toString((int) cvmDoubleResultBuffer));
                    /******Disable equal and remove number from edit text and write the default hint******/
                    cvmCalculatorViewModelFunctions.equalUiEffect();

                }

            break;


        }
    }

}
