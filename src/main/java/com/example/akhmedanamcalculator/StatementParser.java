package com.example.akhmedanamcalculator;

/**
 * Created by ahmed on 14.12.2016.
 */

public class StatementParser {

    int m_highestPriorityOperator;
    int[] m_highestPriorityBracks;
    String m_currentStatement;
    double m_result;
    boolean m_isStatSimple;

    char[] m_operationsMask;
    char[] m_highPriorOperMask;
    char[] m_lowPriorOperMask;
    char[] bracketsMask;

    public StatementParser(String statement){

        m_currentStatement = statement;
        m_highestPriorityBracks = new int[] {0, 0};
        m_operationsMask = new char[] { '+', '-', '*', '/', '√' };
        m_highPriorOperMask = new char[] { '*', '/', '√' };
        m_lowPriorOperMask = new char[] { '+', '-' };
        bracketsMask = new char[] { '(', ')' };
        m_isStatSimple = IsSimpleStat(m_currentStatement);
        m_highestPriorityBracks = FindHighestPriorityBracks(m_currentStatement);
        CalculateResult();
    }

    double getAnswer(){return m_result;}

    private int[] FindHighestPriorityBracks(String str) {

        int lastLeftBracket = lastIndexOf(str, '(');
        int firstRightBracket = str.indexOf(')', lastLeftBracket + 1);

        return new int[]{lastLeftBracket, firstRightBracket};
    }

    private int FindHighestProrityOperator(String str) {

        int lastHighOp = lastIndexOfAny(str, m_highPriorOperMask);
        int firstLowOp = indexOfAny(str, m_lowPriorOperMask);

        if (lastHighOp < 0)
            return firstLowOp;
        else
            return lastHighOp;
    }

    private boolean IsSimpleStat(String str) {

        int firstOccur = indexOfAny(str, m_operationsMask);
        int lastOccur = lastIndexOfAny(str, m_operationsMask);

        return (firstOccur == lastOccur) ? true : false;
    }

    private double SimpleCalculation(String str) {

        double res = 0;
        int operationIndex = indexOfAny(str, m_operationsMask);
        String operation = String.valueOf(str.charAt(operationIndex));
        String left;
        String right;

        if(operation.equals("+")){

            left = str.substring(0, operationIndex);
            right = str.substring(operationIndex + 1, str.length());
            res = Double.valueOf(left) + Double.valueOf(right);

        }
        else if(operation.equals("-")){

            left = str.substring(0, operationIndex);
            right = str.substring(operationIndex + 1, str.length());
            res = Double.parseDouble(left) - Double.parseDouble(right);

        }
        else if(operation.equals("*")){

            left = str.substring(0, operationIndex);
            right = str.substring(operationIndex + 1, str.length());
            res = Double.parseDouble(left) * Double.parseDouble(right);

        }
        else if(operation.equals("/")){

            left = str.substring(0, operationIndex);
            right = str.substring(operationIndex + 1, str.length());
            res = Double.parseDouble(left) / Double.parseDouble(right);

        }
        else if(operation.equals("√")){

            right = str.substring(operationIndex + 1, str.length());
            res = Math.sqrt(Double.parseDouble(right));

        }

        return res;
    }

    private double ComplexCalculation(String str) {

        String temp = str;

        while (!IsSimpleStat(temp))
        {
            m_highestPriorityOperator = FindHighestProrityOperator(temp);


            int leftNeighbor, rightNeighbor = indexOfAny(temp, m_operationsMask, m_highestPriorityOperator + 1);

            if (m_highestPriorityOperator == 0)
                leftNeighbor = -1;
            else
                leftNeighbor = lastIndexOfAny(temp, m_operationsMask, m_highestPriorityOperator - 1);

            double currentResult;

            if (rightNeighbor < 0)
            {
                String forSimpCalc;

                forSimpCalc = temp.substring(leftNeighbor + 1, temp.length());
                currentResult = SimpleCalculation(forSimpCalc);
                temp = temp.substring(0, leftNeighbor + 1) + currentResult;
                temp = temp.replace("--", "+");
                temp = temp.replace("++", "+");
            }
            else if (leftNeighbor < 0)
            {
                String forSipCalc = temp.substring(0, rightNeighbor);
                currentResult = SimpleCalculation(forSipCalc);
                temp = currentResult + temp.substring(rightNeighbor, temp.length());
                temp = temp.replace("--", "+");
                temp = temp.replace("++", "+");
            }
            else
            {
                String forSimCalc = temp.substring(leftNeighbor + 1, rightNeighbor);
                currentResult = SimpleCalculation(forSimCalc);
                temp = temp.substring(0, leftNeighbor + 1)
                        + currentResult + temp.substring(rightNeighbor, temp.length());
                temp = temp.replace("--", "+");
                temp = temp.replace("++", "+");
            }
        }

        return SimpleCalculation(temp);
    }

    private int lastIndexOf(String str, char mask){

        int index = -1;

        char[] input = str.toCharArray();

        for(int i = input.length - 1; i > -1; i--){

            if(input[i] == mask)
            {
                index = i;
                break;
            }
        }

        return  index;

    }

    private int lastIndexOfAny(String str, char[] mask){

        int index = -1;

        char[] input = str.toCharArray();

        for(int i = input.length - 1; i > -1; i--){

            if(isSymbolInMask(input[i], mask))
            {
                index = i;
                break;
            }
        }

        return  index;
    }

    private int lastIndexOfAny(String str, char[] mask, int startIndex) throws IllegalArgumentException{

        if(startIndex >= str.length() || startIndex < 0)
            throw new IllegalArgumentException("Error in 'lastIndexOfAny' method: start index bigger than string length or less than 0");

        int index = -1;

        char[] input = str.toCharArray();

        for(int i = startIndex; i > -1; i--){

            if(isSymbolInMask(input[i], mask))
            {
                index = i;
                break;
            }
        }

        return  index;

    }

    private int indexOfAny (String str, char[] mask, int startIndex) throws IllegalArgumentException{

        if(startIndex >= str.length() || startIndex < 0)
            throw new IllegalArgumentException("Error in 'indexOfAny' method: start index bigger than string length or less than 0");

        int index = -1;

        char[] input = str.toCharArray();

        for(int i = startIndex; i < input.length; i++){

            if(isSymbolInMask(input[i], mask))
            {
                index = i;
                break;
            }
        }

        return  index;
    }

    private int indexOfAny(String str, char[] mask){
        int index = -1;

        char[] input = str.toCharArray();

        for(int i = 0; i < input.length; i++){

            if(isSymbolInMask(input[i], mask))
            {
                index = i;
                break;
            }
        }

        return  index;
    }

    private boolean isSymbolInMask(char symbol, char[] mask){
        boolean toReturn = false;

        for(int i = 0; i < mask.length; i++)
            toReturn = toReturn || symbol == mask[i];

        return  toReturn;
    }

    private void CalculateResult() {

        if (m_highestPriorityBracks[0] < 0) //there is no brackets in our statement
        {
            if (m_isStatSimple)
            {
                m_result = SimpleCalculation(m_currentStatement);
            }
            else
            {
                m_result = ComplexCalculation(m_currentStatement);
            }
        }
        else
        {
            String inBracks = m_currentStatement.substring(m_highestPriorityBracks[0] + 1, m_highestPriorityBracks[1]);
            String currRes;

            if (IsSimpleStat(inBracks))
            {
                currRes = "" + SimpleCalculation(inBracks);
            }

            else
            {
                currRes = "" + ComplexCalculation(inBracks);
            }

            if (m_highestPriorityBracks[1] != (m_currentStatement.length() - 1)) //the right bracket is not last in our statement
                m_currentStatement = m_currentStatement.substring(0, m_highestPriorityBracks[0]) + currRes
                        + m_currentStatement.substring(m_highestPriorityBracks[1] + 1, m_currentStatement.length());
            else
                m_currentStatement = m_currentStatement.substring(0, m_highestPriorityBracks[0]) + currRes;

            m_highestPriorityBracks = FindHighestPriorityBracks(m_currentStatement);
            CalculateResult(); //recursion
        }
    }
}
