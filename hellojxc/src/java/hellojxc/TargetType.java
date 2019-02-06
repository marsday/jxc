/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hellojxc;

/**
 *
 * @author marsday
 */
public final class TargetType {
    public static final int SALES_INPUT = 1;    //销售收入
    public static final int DAILY_INPUT = 2;    //日常收入
    public static final int DAILY_OUTPUT = 4;   //日常支出
    public static final int ALL = 7;            //所有
    
    private TargetType(){}
    /*
    SALES_INPUT(1),DAILY_INPUT(2),DAILY_OUTPUT(4),ALL(7);//销售收入，日常收入，日常支出, 所有
    
    private final int value;
    private TargetType(int value)
    {
        this.value = value;
    }
    
    public TargetType valueof(int value)
    {
        switch (value) 
        { 
            case 1: return TargetType.SALES_INPUT; 
            case 2: return TargetType.DAILY_INPUT; 
            case 4: return TargetType.DAILY_OUTPUT; 
            case 7: return TargetType.ALL; 
            default: return null; 
        }
  
    }
    */
}
