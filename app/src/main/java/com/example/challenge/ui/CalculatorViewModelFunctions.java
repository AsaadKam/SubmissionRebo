package com.example.challenge.ui;

import android.content.Context;
import android.renderscript.ScriptGroup;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.challenge.databinding.CalculatorActivityBinding;

import java.util.ArrayList;

/***
 * This class acts as a helper class
 */
public class CalculatorViewModelFunctions
{
    CalculatorActivityBinding calculatorActivityBinding=null;
    Context passedContext=null;

    /***
     * This method Constructor is helper class  of CalcululatorViewModel that do some abstaraction
     * in constructor this class receive the binding class and context
     * @param calculatorActivityBinding
     * @param passedContext
     */
    public CalculatorViewModelFunctions(CalculatorActivityBinding calculatorActivityBinding, Context passedContext) 
    {
        this.calculatorActivityBinding = calculatorActivityBinding;
        this.passedContext = passedContext;
        /******Close The keyboard of editText *******/
        closeEditTextKeyboard();
        /******Disable equal,undo,entry,redo buttons and enable the rest******/
        calculatorActivityBinding.Entry.setEnabled(false);
        calculatorActivityBinding.Equal.setEnabled(false);
        calculatorActivityBinding.Undo.setEnabled(false);
        calculatorActivityBinding.Redo.setEnabled(false);
        calculatorActivityBinding.Plus.setEnabled(true);
        calculatorActivityBinding.Minus.setEnabled(true);
        calculatorActivityBinding.Mult.setEnabled(true);
        calculatorActivityBinding.Div.setEnabled(true);      
    }

    /**
     * This method checks whether the passed integer is integer or not
     * @param string
     * @return
     */
    public boolean isThatNumber(String string)
    {
        return (TextUtils.isDigitsOnly(  string) && (string.length() > 0));
    }

    /**
     * This method do the undo calaculation
     * @param c
     * @param number
     * @param lastValueOfSum
     * @return
     */
    public int calc_Undo_Operation(char c, int number, int lastValueOfSum)
    {
        switch (c)
        {
            case '+':
                lastValueOfSum -= number;
                break;
            case '-':
                lastValueOfSum += number;

                break;
            case '*':
                lastValueOfSum /= number;

                break;
            case '/':
                lastValueOfSum *= number;

                break;
        }
        return lastValueOfSum;

    }

    /**
     * This method do the default calculation
     * @param c
     * @param number
     * @param lastValueOfSum
     * @return
     */
    public int  calc_Operation(char c, int number,int lastValueOfSum)
    {
        switch (c)
        {
            case '+':
                lastValueOfSum += number;
                break;
            case '-':
                lastValueOfSum -= number;

                break;
            case '*':
                lastValueOfSum *= number;

                break;
            case '/':

                lastValueOfSum /= number;

                break;
        }

        return lastValueOfSum;
    }

    /**
     * This method returns the integer value from the entry editview
     * @return
     */
    public int getIntNumberFromEntry()
    {
        return Integer.parseInt(String.valueOf(calculatorActivityBinding.Entry.getText()));
    }
    /**
     * This mehtod returns the character of last operator in recyclerview list
     * @param RecycLerViewArrayList
     * @return
     */
    public char getTheLastOperatorOfRecycLerViewList(ArrayList<CalculatorModel> RecycLerViewArrayList) {
        return RecycLerViewArrayList.get(RecycLerViewArrayList.size() - 1).getChar_CalculatorModel_Operator();
    }

    /**
     * This method get the last number on RecycLer Array list
     * @param RecycLerViewArrayList
     * @return
     */
    public int getTheLastNumberOfRecycLerViewList(ArrayList<CalculatorModel> RecycLerViewArrayList) {
        return RecycLerViewArrayList.get(RecycLerViewArrayList.size() - 1).getInt_CalculatorModel_Number();
    }
    /**
     *This method get the last number on Redo Array list
     * @param IntegerRedoList
     * @return
     */
    public int getTheNumberRedoArrayList(ArrayList<CalculatorModel> IntegerRedoList)
    {
        return IntegerRedoList.get(IntegerRedoList.size() - 1).getInt_CalculatorModel_Number();

    }

    /**
     *This method get the last operator on Redo Array list
     * @param IntegerRedoList
     * @return
     */

    public char getTheLastOperatorOfRedoArrayList(ArrayList<CalculatorModel> IntegerRedoList )
    {
        return IntegerRedoList.get(IntegerRedoList.size() - 1).getChar_CalculatorModel_Operator();
    }

    /**
     *This method pushes the last item on Redo array list
     * @param operator
     * @param number
     * @param RecycLerViewArrayList
     */
    public void pushTheNumberAndOperatorOnRecyclerViewList(char operator,int number,ArrayList<CalculatorModel> RecycLerViewArrayList)
    {
        RecycLerViewArrayList.add(new CalculatorModel(operator,number) );
    }

    /**
     *This method removes the last item on Recycler array list
     * @param RecycLerViewArrayList
     */
    public void removeTheLastItemOfRecyclerView(ArrayList<CalculatorModel> RecycLerViewArrayList)
    {
        RecycLerViewArrayList.remove(RecycLerViewArrayList.size()-1);
        
    }

    /**
     *This method pushes the last item on Redo array list
     * @param operator
     * @param number
     * @param IntegerRedoList
     */
    public void pushPassedOperatorAndNumberOnRedoArrayList(char operator,int number ,ArrayList<CalculatorModel> IntegerRedoList)
    {
        IntegerRedoList.add(new CalculatorModel(operator,number) );
    }

    /**
     *This method removes the last item on Redo array list
     * @param IntegerRedoList
     */
    public void removeLastItemOfRedoArrayList(ArrayList<CalculatorModel> IntegerRedoList)
    {
        IntegerRedoList.remove(IntegerRedoList.size()-1 );
    }

    /**
     *This mehthod do some effects on the ui of
     * the calculatorListView after clicking on
     * calculation operators
     * @param IntegerRedoList
     */
    public void operatorsUiEffect(ArrayList<CalculatorModel> IntegerRedoList)
    {
        /******Disable operators and enable the entry******/

        calculatorActivityBinding.Entry.setHint("Enter the second operand.");
        calculatorActivityBinding.Entry.setEnabled(true);
        calculatorActivityBinding.Plus.setEnabled(false);
        calculatorActivityBinding.Minus.setEnabled(false);
        calculatorActivityBinding.Mult.setEnabled(false);
        calculatorActivityBinding.Div.setEnabled(false);

        /************Check if Redo list contains data ,if it contains remove all the data*************/
        if (IntegerRedoList.size() != 0) {
            IntegerRedoList.clear();
            calculatorActivityBinding.Redo.setEnabled(false);
        }
        /********Open the kepboard of EditText**********/
        showdEditTextKeyboard();
    }

    /**
     *This mehthod do some effects on the ui of
     * the calculatorListView
     * after the equal button is clicked
     * */
    public void equalUiEffect()
    {
        /******Disable operators and enable the entry******/
        calculatorActivityBinding.Equal.setEnabled(false);
        calculatorActivityBinding.Entry.setText("");
        calculatorActivityBinding.Entry.setHint("Enter the operator.");

        calculatorActivityBinding.Entry.setEnabled(false);
        calculatorActivityBinding.Plus.setEnabled(true);
        calculatorActivityBinding.Minus.setEnabled(true);
        calculatorActivityBinding.Mult.setEnabled(true);
        calculatorActivityBinding.Div.setEnabled(true);
        calculatorActivityBinding.Undo.setEnabled(true);



    }

    /***
     *This mehthod do some effects on the ui of
     * the calculatorListView
     * after the undo button is clicked
     * @param RecycLerViewArrayList
     */
    public void undoUiEffect(ArrayList<CalculatorModel> RecycLerViewArrayList)
    {
        /******Disable operators and enable the entry******/
        calculatorActivityBinding.Redo.setEnabled(true);
        if(RecycLerViewArrayList.size()==0)
        {
            calculatorActivityBinding.Undo.setEnabled(false);
        }
    }

    /**
     *
     */
    void equalWhenThereIsNoDivisonByZeorUiEffect()
    {
        /******Disable operators and enable the entry******/
        calculatorActivityBinding.Equal.setEnabled(false);
        calculatorActivityBinding.Entry.setText("");
        calculatorActivityBinding.Entry.setHint("Enter the operator.");

        calculatorActivityBinding.Entry.setEnabled(false);
        calculatorActivityBinding.Plus.setEnabled(true);
        calculatorActivityBinding.Minus.setEnabled(true);
        calculatorActivityBinding.Mult.setEnabled(true);
        calculatorActivityBinding.Div.setEnabled(true);

    }


    /**
     * This method helps on make the edit text open sponetneously after certain action
     */
    public void showdEditTextKeyboard()
    {
        calculatorActivityBinding.Entry.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) passedContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(calculatorActivityBinding.Entry, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * This method helps on make the edit text close sponetneously after certain action
     */
    public void closeEditTextKeyboard()
    {
        calculatorActivityBinding.Entry.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) passedContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

    }
}
