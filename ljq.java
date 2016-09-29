import java.awt.Color;  
import java.awt.GridLayout;  
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;  
import java.awt.event.ActionListener; 
import javax.swing.JButton;  
import javax.swing.JFrame;  
import javax.swing.JPanel;  
import javax.swing.JTextField;  
import java.math.BigDecimal; //引入大数类 
//import java.util.Scanner;//
import java.awt.*;
import java.math.*;
/** 
 * 一个计算器，支持简单的计算
 */  

public class calculator extends JFrame implements ActionListener {  
	/***************************************************************************************************************/
	/********计算器相关按键名称及按钮设置*******/

	
    /*简单计算器上的键的显示名字 */  
    private final String[] KEYS = { "7", "8", "9", "+" , "4", "5", "6",  
            "-", "1", "2", "3", "*", "0", ".", "=", "/" };  
    /*简单计算器上的清楚等功能键的显示名字 */  
    private final String[] COMMAND = { "( )","AC", "C","←" };  //括号用的英文版，若不合适可改
    /*科学计算器上的函数图标显示名称*/ 
    private final String[] FUNCTION = { "1/x", "x^2", "x^y", "x!", "√" ,"√(n&x)",
    		"sin","cos","tan","In","log","π","BMI/BFR","e^x","e"};
    /*计算器上特殊功能图标显示名称*/
    private final String[] SPECIAL_FUNTION = { "Cov", "ρ","s^2", "ave","$" };
    
    /*计算各个按钮数组的长度，并将他们存入相应变量中*/
    int keysLen=KEYS.length;//用于存放KEYS数组中的按钮个数
    int commandLen=COMMAND.length;//用于存放COMMAND数组中的按钮个数
    int functionLen=FUNCTION.length;//用于存放FUNCTION数组中的按钮个数
    int special_functionLen=SPECIAL_FUNTION.length;//用于存放SPECIAL_FUNTION 数组中的按钮个数
    
    /*简单计算器上键的按钮 */  
    private JButton keys[] = new JButton[keysLen];  
    /* 计算器上的功能键的按钮 */  
    private JButton commands[] = new JButton[commandLen];  
    /*计算器上功能图标显示名称*/  
    private JButton function[] = new JButton[functionLen]; 
    /*计算器上常用函数的按钮*/
    private JButton special_function[] = new JButton[special_functionLen]; 
    /*计算结果文本框 */  
    private JTextField resultText = new JTextField("");     //去掉了默认的零 
  
    /*****************************************************************************************************/
    // 标志用户按的是否是整个表达式的第一个数字,或者是运算符后的第一个数字  
    private boolean firstDigit = true;  
    // 计算的中间结果。  
    private double resultNum =0;  
    // 当前运算的运算符  
    private String operator = "=";  
    // 操作是否合法  
    private boolean operateValidFlag = true; 
    
   /** 
    ************ 构造函数 *************************************************************************
    *********************/  
    public calculator() {  
        super("MyCalculator");  
        // 初始化计算器  
        init();  
        // 设置计算器的背景颜色  
        this.setBackground(Color.LIGHT_GRAY);  
        this.setTitle("计算器");  
        // 在屏幕(500, 300)坐标处显示计算器  
        this.setLocation(500, 300);  
        // 不许修改计算器的大小  
        this.setResizable(false);             //注意：如果大小可以改变 
        // 使计算器中各组件大小合适  
        this.pack();  
    }  
  
    /** 
     *******初始化计算器 ***************************************************************************************** 
     */  
    private void init() {   	
    	
        // 文本框中的内容采用右对齐方式  
        resultText.setHorizontalAlignment(JTextField.RIGHT);  
        // 允许修改结果文本框  
        resultText.setEditable(true);            //允许改变现实框
        // 设置文本框背景颜色为白色  
        resultText.setBackground(Color.white);  
  
        // 初始化基本计算器上数字、运算等键的按钮，将键放在一个画板内  
        JPanel calckeysPanel = new JPanel();  
        // 用网格布局器，4行，4列的网格，网格之间的水平方向间隔为3个象素，垂直方向间隔为3个象素  
        calckeysPanel.setLayout(new GridLayout(4, 4, 3, 3));  
        for (int i = 0; i < keysLen; i++) {  
            keys[i] = new JButton(KEYS[i]);  
            calckeysPanel.add(keys[i]);  
            keys[i].setForeground(Color.blue);  
        }  
        // 运算符键用红色标示，其他键用蓝色表示  
        keys[3].setForeground(Color.red);  
        keys[7].setForeground(Color.red);  
        keys[11].setForeground(Color.red);  
        keys[15].setForeground(Color.red);   
  
        // 初始化清楚，回删等功能键，都用红色标示。将功能键放在一个画板内  
        JPanel commandsPanel = new JPanel();  
        // 用网格布局器，1行，3列的网格，网格之间的水平方向间隔为3个象素，垂直方向间隔为3个象素  
        commandsPanel.setLayout(new GridLayout(1, 3, 3, 3));  
        for (int i = 0; i < commandLen; i++) {  
            commands[i] = new JButton(COMMAND[i]);  
            commandsPanel.add(commands[i]);  
            commands[i].setForeground(Color.red);  
        }  
  
        // 初始化科学计算器函数功能键，用红色标示，将基本函数功能键放在一个画板内  
        JPanel functionsPanel = new JPanel();  
        // 用网格布局管理器，5行，3列的网格，网格之间的水平方向间隔为3个象素，垂直方向间隔为3个象素  
        functionsPanel.setLayout(new GridLayout(5, 3, 3, 3));  
        for (int i = 0; i < functionLen; i++) {  
            function[i] = new JButton(FUNCTION[i]);  
            functionsPanel.add(function[i]);  
            function[i].setForeground(Color.red);  
        }  
        
        // 初始化计算器特殊函数功能键，用红色标示，将基本函数功能键放在一个画板内  
        JPanel special_functionsPanel = new JPanel();  
        // 用网格布局管理器，5行，3列的网格，网格之间的水平方向间隔为3个象素，垂直方向间隔为3个象素  
        special_functionsPanel.setLayout(new GridLayout(5, 3, 3, 3));  
        for (int i = 0; i < special_functionLen; i++) {  
        	special_function[i] = new JButton(SPECIAL_FUNTION[i]);  
        	special_functionsPanel.add(special_function[i]);  
            function[i].setForeground(Color.red);  
        } 
  
        // 下面进行计算器的整体布局，将calckeys和commands画板放在计算器的中部，  
        // 将文本框放在北部，将functions和special_functions画板放在计算器的西部。  
  
        // 新建一个大的画板，将上面建立的command和calckeys画板放在该画板内  
        JPanel panel1 = new JPanel();  
        // 画板采用边界布局管理器，画板里组件之间的水平和垂直方向上间隔都为3象素  
        panel1.setLayout(new BorderLayout(3, 3));  
        panel1.add("North", commandsPanel);  
        panel1.add("South", calckeysPanel);
          // 建立一个画板放文本框  
        JPanel top = new JPanel();  
        top.setLayout(new BorderLayout());  
        top.add("Center", resultText); 
        //新建一个大的画板，将上面建立的functions和special_functions画板放在该画板内  
        JPanel panel2 = new JPanel();  
        // 画板采用边界布局管理器，画板里组件之间的水平和垂直方向上间隔都为3象素  
        panel2.setLayout(new BorderLayout(3, 3));  
        panel2.add("East", functionsPanel);  
        panel2.add("West", special_functionsPanel);
 
  
        // 整体布局  
        getContentPane().setLayout(new BorderLayout(3, 5));  
        getContentPane().add("North", top);  
        getContentPane().add("Center", panel1);  
        getContentPane().add("West", panel2);  
        
        /***************为各按钮添加事件侦听器***********************************/   
        // 都使用同一个事件侦听器，即本对象。本类的声明中有implemennts ActionListener  
        for (int i = 0; i < keysLen; i++) {  
            keys[i].addActionListener(this);  
        }  
        for (int i = 0; i < commandLen; i++) {  
            commands[i].addActionListener(this);  
        }  
        for (int i = 0; i < functionLen; i++) {  
            function[i].addActionListener(this);  
        } 
        for (int i = 0; i < special_functionLen; i++) {  
            special_function[i].addActionListener(this);  
        } 
    }    
    /** 
     *********处理事件 *****************************************************************************************************
     */  
    public void actionPerformed(ActionEvent e) {  
        // 获取事件源的标签  
        String label = e.getActionCommand();  
        if (label.equals(COMMAND[0])) {  
            // 用户按了"( )"键  ,左右括号交替出现
             
        }else if (label.equals(COMMAND[1])) {  
            // 用户按了"AC"键，清空文本框上的数据
            resultText.setText("0");  
        }else if (label.equals(COMMAND[2])) {  
            // 用户按了"C"键  ，初始化计算器
            handleC();
        }else if (label.equals(COMMAND[3])) {  
        	//用户按了“←”退回键，将文本框最后一个字符去掉
             handleBackspace();
        }else if ("0123456789.".indexOf(label) >= 0) {  
            // 用户按了数字键或者小数点键  
            handleNumber(label);  
            // handlezero(zero);  
        } 
        else {  
            // 用户按了运算符键   
            handleOperator(label);            
            } 
//        else{
//        	//缺省——处理其他更复杂功能
//        } 
    }  
  
    /** 
     * 处理Backspace键被按下的事件 
     */  
    private void handleBackspace() {  
        String text = resultText.getText();  
        int i = text.length();  
        if (i > 0) {  
            // 退格，将文本最后一个字符去掉  
            text = text.substring(0, i - 1);  
            if (text.length() == 0) {  
                // 如果文本没有了内容，则初始化计算器的各种值  
                resultText.setText("0");  
                firstDigit = true;  
                operator = "=";  
            } else {  
                // 显示新的文本  
                resultText.setText(text);  
            }  
        }  
    }  
  
    /** 
     * 处理数字键或小数点键被按下的事件 
     *  
     * @param key 
     */  
    private void handleNumber(String key) {  
        if (firstDigit) {  
            // 输入的第一个数字  
            resultText.setText(key);  
        } else if ((key.equals(".")) && (resultText.getText().indexOf(".") < 0)) {  
            // 输入的是小数点，并且之前没有小数点，则将小数点附在结果文本框的后面  
            resultText.setText(resultText.getText() + ".");  
        } else if (!key.equals(".")) {  
            // 如果输入的不是小数点，则将数字附在结果文本框的后面  
            resultText.setText(resultText.getText() + key);  
        }  
        // 以后输入的肯定不是第一个数字了  
        firstDigit = false;  
    }  
  
    /** 
     * 处理C键被按下的事件 
     */  
    private void handleC() {  
        // 初始化计算器的各种值  
        resultText.setText("0");  
        firstDigit = true;  
        operator = "=";  
    }  
  
    /** 
     * 处理运算符键被按下的事件 
     *  
     * @param key 
     */  
    private void handleOperator(String key) {  
        if (operator.equals("/")) {  
        	// 除法运算  
            // 如果当前结果文本框中的值等于0  
            if (getNumberFromText() == 0.0) {  
                // 操作不合法  
                operateValidFlag = false;  
                resultText.setText("除数不能为零");  
            } else {  
                resultNum /= getNumberFromText();  
                resultText.setText("just for测试");  

            }   
       }else if (operator.equals("1/x")) {  
        // 倒数运算  
            if (resultNum == 0.0) {  
                // 操作不合法  
                operateValidFlag = false;  
                resultText.setText("零没有倒数");  
            } else {  
                resultNum = 1 / resultNum;
                resultText.setText(String.valueOf(resultNum));  
            }  
       }else if (operator.equals("+")) {  
        // 加法运算  
            resultNum += getNumberFromText();  
        } else if (operator.equals("-")) {  
            // 减法运算  
            resultNum -= getNumberFromText();  
        } else if (operator.equals("*")) {  
            // 乘法运算  
            resultNum *= getNumberFromText();  
        } else if (operator.equals("√")) {  
            // 平方根运算  
            resultNum = Math.sqrt(resultNum);  
        } else if (operator.equals("x!")) {  
            // 阶乘运算  
        	resultNum = factorial(resultNum);
        } else if (operator.equals("sin")) {  
            // 三角函数sin()  
            resultNum = sin(getNumberFromText());  
        } else if (operator.equals("cos")) {  
             // 三角函数cos()  
        	 resultNum = cos(getNumberFromText());  
        } else if (operator.equals("tan")) {  
            //三角函数tan()    
            resultNum = tan(getNumberFromText());  
        } else if (operator.equals("e")){
        	 resultNum = Math.E;//显示有待改进
        }else if (operator.equals("π")){
        	 resultNum = Math.PI;//显示有点问题
        }else if (operator.equals("e^x")){
       	    resultNum =exponential(getNumberFromText());//显示有点问题
        }else if (operator.equals("x^2")){
        	resultNum = square(getNumberFromText());
        }else if (operator.equals("ln")){
        	resultNum = ln(getNumberFromText());//计算错误
        }
        if (operateValidFlag) {  
            // 双精度浮点数的运算  
//            long t1;  
//            double t2;  
//            t1 = (long) resultNum;  
//            t2 = resultNum - t1;  
//            if (t2 == 0) {  
                resultText.setText(String.valueOf(resultNum));  
//            } else {  
//                resultText.setText(String.valueOf(resultNum));  
//            }  
        }  
        // 运算符等于用户按的按钮  
        operator = key;  
        firstDigit = true;  
        operateValidFlag = true;  
    }  
    
    
    /**
     * 计算函数************************************************************
     */
    /*计算阶乘*/
//    public static long factorial(long n){  
//        BigDecimal bd1 = new BigDecimal(1);//BigDecimal类型的1  
//        BigDecimal bd2 = new BigDecimal(2);//BigDecimal类型的2  
//        BigDecimal result = bd1;//结果集，初值取1  
//        while(n.compareTo(bd1) > 0){//参数大于1，进入循环  
//            result = result.multiply(n.multiply(n.subtract(bd1)));//实现result*（n*（n-1））  
//            n = n.subtract(bd2);//n-2后继续  
//        }  
//        return result;  
//    } 
    private static double factorial(double factorial) {
            if (factorial == 1) {
    	       return 1;
    	    }
    	    return factorial * factorial(factorial - 1);
    }
    /*计算平方数*/
    private static double square(double sq){
    	sq *= sq;
    	return sq;
    }
    /*计算三角函数*/
    private static double sin(double s){
    	s = Math.sin(s);
    	return s;    	
    }
    private static double cos(double c){
    	c = Math.cos(c);
    	return c;   	
    }
    private static double tan(double t){
    	t = Math.tan(t);
    	return t;   	
    }
    /*计算幂函数*/
    private static double power(double x,double y){
    	p = Math.pow(x,y);
    	return p;    	
    }
    /*计算以e为底的指数函数*/
    private static double exponential(double ex){
    	double e = Math.exp(ex);
    	return e;   	
    }
    
    /*计算自然对数函数*/
    private double ln(double x){
    	
    	if(x<=0){
    		resultText.setText("you entered must be positive number");
    		return 0;
        }else{
    	double lg = Math.log(x);
    	return (lg);
    	}
    	
    }
    /*计算对数函数*/
    private double log(double x,double y){
    	
    	if(x<=0&&y<=0){
    		resultText.setText("you entered must be positive number");
    		return 0;
     }else{
    	double l = Math.log(x)/Math.log(y);
    	return (l);
    	}
    	
    }
    /*计算平均值*/
     private double ave(int order[]){
    	int l = order.length;
    	int total=0,k=0;
    	 for(int i=0;i<l;i++){
    		 total += order[i]; 
    		 k++;
    	 }
    	double a =(double)total/k;
    	return a;
    	
    }
     /*计算方差*/
     private double D(int s[]){
     	int l = s.length;
     	double average=ave(s);
     	int total=0,k=0;
     	 for(int i=0;i<l;i++){
     		 total += (s[i]-average)*(s[i]-average); 
     		 k++;
     	 }
     	double S =(double)total/k;
     	return S;
     	
     }
     /*计算组合公式*/
     private int C(int x,int y){
     	
     	if(x<=0&&y<=0){
     		resultText.setText("your enter must be positive number");
     		return 0;
      }else{
    	  if(x>y){
    		  resultText.setText("x must be smaller than or equal to y");
    		  return 0;
    	  }else{
     	int c = (int)(factorial(y)/(factorial(x)*factorial(y-x)));
     	return (c);
     	}
      }
     	
     }
     /*计算排列公式*/
     private int A(int m,int n){
    		
      	if(n<=0&&m<=0){
      		resultText.setText("your enter must be positive number");
      		return 0;
       }else{
     	  if(m>n){
     		  resultText.setText("m must be smaller than or equal to n");
     		  return 0;
     	  }else{
      	int a = (int)(factorial(n)/factorial(n-m));
      	return (a);
      	}
       }
     }
     /*计算体重指数*/
     private  void BMI(double weight,double height){
     	double bmi=weight/(height*height);
     	
     	if(bmi<19){
     		resultText.setText(bmi+"Underweight");
     	}else if(bmi>=19&&bmi<25){
     		resultText.setText(bmi+"Normalweight");
     	}else if(bmi>=25&&bmi<30){
     		resultText.setText(bmi+"Overweight");
     	}else if(bmi>=30&&bmi<39){
     		resultText.setText(bmi+"Obese");
     	}else{
     		resultText.setText(bmi+"Morbidly Obese");
     	}
     }
     /*计算身体脂肪率*/
     private  void BFR(double weight,double height,double waistline,int age){
      	double bmi=weight/(height*height);
      	resultText.setText("Please push number 0(Male) or 1(Female)?");
      	if(){
      		double a=waistline*0.74;
      		double b=weight*0.082+34.89;
      		double bfr=(a-b)/weight;
      	}else{
      		double a=waistline*0.74;
      		double b=weight*0.082+44.74;
      		double bfr=(a-b)/weight;
      		
      	}
      	
      
      }
    /** 
     * 从结果文本框中获取数字 
     *  
     * @return 
     */  
    private Double getNumberFromText() {  
        double result = 0;  
        try {  
            result = Double.valueOf(resultText.getText()).doubleValue();  
        } catch (NumberFormatException e) {  
        }          return result;  
    }  
  /********主类，创建一个calculator对象****************************************************************************************************/
    public static void main(String args[]) {  
        calculator calculator = new calculator();  
        calculator.setVisible(true);  
        calculator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
    }  
}  
