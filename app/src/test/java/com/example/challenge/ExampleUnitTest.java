package com.example.challenge;

import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.challenge.ui.CalculatorActivity;
import com.example.challenge.ui.CalculatorViewModel;
import com.example.challenge.ui.CalculatorViewModelFunctions;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest
{

    CalculatorViewModelFunctions calculatorViewModelFunctions=null;
    @Test
    public void CalculatorViewModelFunctions_CalcUndoOperation_Plus_PostiveNumber_NegativeNumber()
    {
        assertEquals(-1, calculatorViewModelFunctions.calc_Undo_Operation('+',1,0));
    }
    @Test
    public void CalculatorViewModelFunctions_calc_Operation_Plus_PostiveNumber_NegativeNumber()
    {
        assertEquals(1, calculatorViewModelFunctions.calc_Operation('+',1,0));
    }

}