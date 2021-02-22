package com.example.challenge.ui;


/********************************************************
 * This is the Model that we be used with Recycler view
 * and viewModel and also for undo and redo Arraylist
 *
 *********************************************************/
public class CalculatorModel
{
    /*****Private Data types of the class****/
    private char char_CalculatorModel_Operator;
    private int  int_CalculatorModel_Number;


    /*****Constructors for CalculatorModel****/
    public  CalculatorModel(char char_CalculatorModel_Operator, int int_CalculatorModel_Number)
    {
        this.char_CalculatorModel_Operator = char_CalculatorModel_Operator;
        this.int_CalculatorModel_Number = int_CalculatorModel_Number;
    }
    /*****Setter and getter for private variables of CalculatorModel*****/
    public char getChar_CalculatorModel_Operator()
    {
        return char_CalculatorModel_Operator;
    }

    /**
     *
     * @param char_CalculatorModel_Operator
     */
    public void setChar_CalculatorModel_Operator(char char_CalculatorModel_Operator)
    {
        this.char_CalculatorModel_Operator = char_CalculatorModel_Operator;
    }

    public int getInt_CalculatorModel_Number()
    {
        return int_CalculatorModel_Number;
    }

    public void setInt_CalculatorModel_Number(int int_CalculatorModel_Number)
    {
        this.int_CalculatorModel_Number = int_CalculatorModel_Number;
    }
}
