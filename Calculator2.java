import java.awt.Color;  
import java.awt.GridLayout;  
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;  
import java.awt.event.ActionListener;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import javax.swing.JButton;  
import javax.swing.JFrame;  
import javax.swing.JPanel;  
import javax.swing.JTextField; 
import javax.swing.JCheckBox; 
import java.math.BigDecimal; //引入大数类 
import java.util.Collections;
import java.util.Stack;
//import java.util.Scanner;//
import java.awt.*;
import java.text.DecimalFormat;
import java.lang.NumberFormatException;
import java.lang.String;
 
/** 
 * 一个计算器，支持简单的计算
 */  
public class Calculator2 extends JFrame implements ActionListener {  
	/***************************************************************************************************************/
	/********计算器相关按键名称及按钮设置*******/

	
/********计算器相关按键名称及按钮设置*******/
	
	/*计算器模式的切换的名称显示*/
    private final String[] MODEL = {"简单型","科学型","统计型","扩展型"};
    /*基础计算器上的键的显示名字 */  
    private final String[] KEYS = {"(",")", "C","←","7", "8", "9", "+" , "4", "5", "6",  
            "-", "1", "2", "3", "*", "0", ".", "=", "/" }; 
    /*科学型计算器上的函数图标显示名称*/ 
    private final String[] FUNCTION = { "1/x", "x^2", "x^y", "x!", "√" ,"%",
    		"sin","cos","tan","In","lg","log","e^x","e","π"};
    /*统计型计算器上特殊功能图标显示名称*/
    private final String[] SPECIAL_FUNTION = { "C(N,M)", "A(N,M)","D", "Ave" };
    /*扩展型计算器上的键的显示名称*/
    private final String[] SUPPLEMENT={"BMI","BFR","$","寿命"};
 
    /*计算各个按钮数组的长度，并将他们存入相应变量中*/
    int modelLen=MODEL.length;//用于存放MODEL数组中按钮个数
    int keysLen=KEYS.length;//用于存放KEYS数组中的按钮个数
    int functionLen=FUNCTION.length;//用于存放FUNCTION数组中的按钮个数
    int special_functionLen=SPECIAL_FUNTION.length;//用于存放SPECIAL_FUNTION 数组中的按钮个数
    int supplementLen=SUPPLEMENT.length;//用于存放扩展模型中按钮的数量
    
    /*模式切换上的计算器*/
    private JButton models[] = new JButton[keysLen];
    /*计算结果文本框 */  
    private JTextField resultText = new JTextField("");//消除了默认的0
    /*身高输入文本框 */  
    private JTextField heightText = new JTextField("请输入你的身高");
    /*体重输入文本框 */  
    private JTextField weightText = new JTextField("请输入你的体重");
    /*腰围输入文本框 */  
    private JTextField waistText = new JTextField("请输入你的腰围");
    /*年龄输入文本框 */  
    private JTextField ageText = new JTextField("请输入你的年龄");
    /*简单计算器上键的按钮 */  
    private JButton keys[] = new JButton[keysLen];  
    /*计算器上功能图标显示名称*/  
    private JButton function[] = new JButton[functionLen+keysLen]; 
    /*计算器上常用函数的按钮*/
    private JButton special_function[] = new JButton[special_functionLen+keysLen];
    /*计算器的扩展模型上的按钮*/
    private JButton supplements[]=new JButton[supplementLen];
    /*****************************************************************************************************/
    /****/
    
    private Stack<String> postfixStack  = new Stack<String>();//后缀式栈
    private Stack<Character> opStack  = new Stack<Character>();//运算符栈
    private int [] operatPriority  = new int[] {0,3,2,1,-1,1,0,2};//运用运算符ASCII码-40做索引的运算符优先级 
    
    //设置几个panel,Panel1表示简单型计算器的界面，panel2表示科学型计算器的界面，panel3表示统计型计算器的界面，panel4表示扩展型计算器的界面
    JPanel panel,panel1,panel2,panel3,panel4;
    //设置单选框，以方便选择男女
    Checkbox cRadio1,cRadio2;
    CheckboxGroup c;
    double baseNum = 1;    //保存计算x^y时x的值
    String   expression = ""; //存放科学计算时除去运算符的表达式。如计算sin（5+12*(3+5)/7）时，expression=5+12*(3+5)/7
    boolean isFunction = false;   //是否是科学计算
    String   opreator = "";
    
   /** 
    ************ 构造函数 *************************************************************************
    *********************/  
    public Calculator2() {  
        super();  
        // 初始化计算器  
        init();  
        // 设置计算器的背景颜色  
        this.setBackground(new Color(54,54,54   ));  
        this.setTitle("计算器");  
        // 在屏幕(500, 300)坐标处显示计算器  
        this.setLocation(500, 300);  
        // 不许修改计算器的大小  
        this.setResizable(true);             //注意：如果大小可以改变 
        // 使计算器中各组件大小合适  
        this.setSize(600, 300); 
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
        //设置文本框的宽度
        resultText.setSize(100,100);
        
        //模型切换的四个按钮放在一个画板名叫modelPanel的里面
        JPanel modelPanel = new JPanel();
        //设置modelPanel里面的按钮从左到右排列,水平居左
        FlowLayout fl = new FlowLayout(); 
        fl.setAlignment(FlowLayout.LEFT); 
        modelPanel.setLayout(fl);
        for (int i = 0; i < modelLen; i++) {  
            models[i] = new JButton(MODEL[i]);  
            //去掉modelsPanel里面的button的边界线
            models[i].setBorderPainted(false);
            //去掉button点击时字体上出现的边界框
            models[i].setFocusPainted(false);
            //设置button里面字体的大小
            models[i].setFont(new Font("微软雅黑", Font.PLAIN, 14));
            models[i].setForeground(new Color(255,255,255));
            modelPanel.add(models[i]); 
            //设置modelPanle和button的背景颜色
            modelPanel.setBackground(new Color(54,54,54   ));
            models[i].setBackground(new Color(54,54,54   ));  
        }  
        
       // 建立一个画板放文本框  
       JPanel topPanel = new JPanel();  
       topPanel.setLayout(new BorderLayout(0,0));  
       topPanel.add("Center", resultText); 
      
       //建立一个顶部Panel来存放模式切换和显示框架
       JPanel top = new JPanel();  
       top.setLayout(new GridLayout(2,1,0,0));  
       top.add(modelPanel); 
       top.add("South",topPanel);
      
       // 初始化基本计算器上数字、运算、括号、清除等键的按钮，将键放在一个名叫calkeysPanel画板内  
       JPanel calckeysPanel = new JPanel();  
       // 用网格布局器，4行，4列的网格，网格之间的水平方向间隔为3个象素，垂直方向间隔为3个象素  
       calckeysPanel.setLayout(new GridLayout(5, 4, 0, 0));  
       for (int i = 0; i < keysLen; i++) {  
            keys[i] = new JButton(KEYS[i]);  
            calckeysPanel.add(keys[i]);  
            //去掉button点击时字体上出现的边界框
            keys[i].setFocusPainted(false);
            keys[i].setBackground(new Color(54,54,54   ));  
            //设置button里面字体大小
            keys[i].setFont(new Font("微软雅黑", Font.PLAIN, 16));
            keys[i].setForeground(new Color(255,255,255));
        }     
       
        // 新建一个大的画板，将calckeys画板放在该画板内  
        panel1 = new JPanel();  
        // 画板采用边界布局管理器，画板里组件之间的水平和垂直方向上间隔都为0象素  
        panel1.setLayout(new BorderLayout(0,0));   
        panel1.setBackground(new Color(54,54,54   ));
        panel1.add("Center", calckeysPanel);
        
        // 初始化科学计算器函数功能键，将 基本函数功能和科学计算器功能键放在一个画板内  
        JPanel functionsPanel = new JPanel();  
        // 用网格布局管理器，5行，7列的网格，网格之间的水平方向间隔为0个象素，垂直方向间隔为0个象素  
        functionsPanel.setLayout(new GridLayout(5, 7,0,0));
        //加入一些科学运算的按钮
        for (int i = 0; i < functionLen; i++) {  
            function[i] = new JButton(FUNCTION[i]);   
            //去掉button点击时字体上出现的边界框
            function[i].setFocusPainted(false);
            //设置button里面字体大小
            function[i].setFont(new Font("微软雅黑", Font.PLAIN, 16));
            function[i].setForeground(new Color(255,255,255));  
            function[i].setBackground(new Color(54,54,54   ));  
            functionsPanel.add(function[i]); 
        } 
 
        // 新建一个大的画板，将function画板放在该画板内，实现科学计算器的界面
        panel2=new JPanel();
        panel2.setLayout(new BorderLayout());
        panel2.setBackground(new Color(54,54,54   ));
        panel2.add("Center",functionsPanel);
        
        // 初始化计算器特殊函数功能键，用红色标示，将基本函数功能键放在一个画板内  
        JPanel special_functionsPanel = new JPanel();  
        // 用网格布局管理器，6行，4列的网格，网格之间的水平方向间隔为0个象素，垂直方向间隔为0个象素  
        special_functionsPanel.setLayout(new GridLayout(6, 4, 0, 0)); 
        //加入统计运算的功能按钮
        for (int i = 0; i < special_functionLen; i++) {  
        	special_function[i] = new JButton(SPECIAL_FUNTION[i]); 
        	//去掉button点击时字体上出现的边界框
            special_function[i].setFocusPainted(false);
            //设置button里面字体大小
            special_function[i].setFont(new Font("微软雅黑", Font.PLAIN, 16));
            special_function[i].setForeground(new Color(255,255,255));
            special_function[i].setBackground(new Color(54,54,54   ));  
        	special_functionsPanel.add(special_function[i]);   
        }  
        //加入基本运算以及数字按钮
        for (int i = special_functionLen; i < (keysLen+special_functionLen); i++) {  
        	special_function[i] = new JButton(KEYS[i-special_functionLen]); 
        	//去掉button点击时字体上出现的边界框
            special_function[i].setFocusPainted(false);
            //设置button里面字体大小
            special_function[i].setFont(new Font("微软雅黑", Font.PLAIN, 16));
            special_function[i].setForeground(new Color(255,255,255));  
            special_function[i].setBackground(new Color(54,54,54   ));  
        	special_functionsPanel.add(special_function[i]);  
        }  
        //新建一个大的画板，将calckeys以及special_function画板放在该画板内，实现统计计算器的界面
        panel3=new JPanel();
        panel3.setLayout(new BorderLayout(0,0));
        panel3.setBackground(new Color(54,54,54   ));
        panel3.add("Center",special_functionsPanel);
        
        //初始化计算器扩展模式，将扩展功能的函数放在一个panel上
        JPanel supplementPanel=new JPanel();
        supplementPanel.setLayout(new GridLayout());
        for (int i = 0; i < supplementLen; i++) {  
        	supplements[i] = new JButton(SUPPLEMENT[i]); 
        	//去掉button点击时字体上出现的边界框
            supplements[i].setFocusPainted(false);
            //设置button里面字体大小
            supplements[i].setFont(new Font("微软雅黑", Font.PLAIN, 16));
            supplements[i].setBackground(new Color(54,54,54   ));  
        	supplementPanel.add(supplements[i]);  
            supplements[i].setForeground(new Color(255,255,255));  
        }  
        //设置单选框    
        c=new CheckboxGroup();
        cRadio1=new Checkbox("男性",c,false);
        cRadio2=new Checkbox("女性",c,true);
        //新建一个画板，将几个输入框放在里面
        JPanel panel_4=new JPanel();
        panel_4.setLayout(new GridLayout(2,2));
        panel_4.add(heightText);
        panel_4.add(weightText);
        panel_4.add(waistText);
        panel_4.add(ageText);
        panel_4.add(cRadio1);
        panel_4.add(cRadio2);
        //新建一个大的画板，将supplement画板放在该画板内，实现括展计算器的界面
        panel4=new JPanel();
        panel4.setLayout(new BorderLayout(3,3));
        panel4.setBackground(new Color(54,54,54   ));
        panel4.add("North", panel_4);
        panel4.add("Center",supplementPanel);
      
        
        /***************为各按钮添加事件侦听器***********************************/   
         //都使用同一个事件侦听器，即本对象。本类的声明中有implements ActionListener
        for (int i = 0; i < modelLen; i++) {  
            models[i].addActionListener(this);  
        }  
        for (int i = 0; i < keysLen; i++) {  
            keys[i].addActionListener(this);  
        }   
        for (int i = 0; i < functionLen; i++) {  
            function[i].addActionListener(this);  
        } 
        for (int i = 0; i < special_functionLen+keysLen; i++) {  
            special_function[i].addActionListener(this);  
        }
        for (int i = 0; i < supplementLen; i++) {  
        	supplements[i].addActionListener(this);  
        }
        
        //计算器的整体布局 
//        JPanel panelt=new JPanel(new GridLayout(2,1,0,0));
//        add(panelt);
//        panelt.setBackground(new Color(54,54,54   )); 
        panel=new JPanel(new BorderLayout(0,0));
        panel.setBackground(new Color(54,54,54   ));  
        add(panel);
        panel.add("North",top);
        panel.add("West",panel2);
        panel.add("Center",panel1);   
    }    
    /** 
     *********处理事件 *****************************************************************************************************
     */  
    public void actionPerformed(ActionEvent e) {  
        // 承接最终计算结果  
    	double result = 0.0;
    	// 获取事件源的标签  
        String label = e.getActionCommand();
        //四种模式选择：简单型；科学型；统计型；扩展型；
        if(label.equals(MODEL[0])){//简单型
        	panel.add("Center",panel1);
        	panel1.setVisible(true);
        	panel2.setVisible(false);
        	panel3.setVisible(false);
        	panel4.setVisible(false);
        }else if(label.equals(MODEL[1])){//科学型
        	panel1.setVisible(true);
        	panel2.setVisible(true);
        	panel3.setVisible(false);
        	panel4.setVisible(false);
        }else if(label.equals(MODEL[2])){//统计型
        	panel.add("Center",panel3);
        	panel1.setVisible(false);
        	panel2.setVisible(false);
        	panel3.setVisible(true);
        	panel4.setVisible(false);
        }else if(label.equals(MODEL[3])){//扩展型
        	panel.add("Center",panel4);
        	panel1.setVisible(false);
        	panel2.setVisible(false);
        	panel3.setVisible(false);
        	panel4.setVisible(true);

        }else if (label.equals("=")) {  // 用户按了"="键  ,输入结束，计算结果
        	if(isFunction){
        		handleFunction(opreator);
        	    isFunction = false;   //是否是科学计算，如sin,cos,lg等
        	    opreator = "";
        	}else{
    	      	result = calculate(resultText.getText());//计算结果
    	      	resultText.setText(String.valueOf(result));//输出结果
        	}

        }else if (label.equals(KEYS[2])) { // 用户按了"C"键  ，初始化计算器
        	handleC();                     //将所有输入清空 
        }else if (label.equals(KEYS[3])) {  //用户按了“←”退回键，将文本框最后一个字符去掉
            handleBackspace();
        }else if ("0123456789.()+-*/".indexOf(label) >= 0) {  // 用户按了输入要计算的表达式
        	if(isFunction){           //如果是科学计算，如sin（），将输入的表达式存入一个字符串
        		expression = expression + label;
        	}
        	resultText.setText(resultText.getText()+label);

        }else if(label.equals("1/x")||label.equals("x^2")||label.equals("x!")||label.equals("%")){//初步处理科学型计算器功能
            handleFunction(label);
        }else if(label.equals("C(N,M)")||label.equals("A(N,M)")||    //如果是统计计算器，转入handleSpecialFunction（）
        		label.equals("D")||label.equals("Ave")){
        	handleSpecialFunction(label);
        }else if(label.equals("BMI")||label.equals("BFR")||  //如果是拓展计算计算器，转入handleSupplement（）
        		label.equals("$")||label.equals("寿命")){
        	handleSupplement(label);
        	
        }else{
        	if(label.equals("e^x")){
        		resultText.setText("e^"+"( ");
        	}else if(label.equals("x^y")){
        		baseNum = getNumberFromText();
        		resultText.setText(resultText.getText()+"^( ");
        	}else{
        		resultText.setText(label+"( ");
        	}
        	
        	isFunction = true;	
        	opreator = label;
        }
    }  
  
    /*处理Backspace键被按下的事件 */  
    private void handleBackspace() {  
        String text = resultText.getText();  
        int i = text.length();  
        if (i > 0) {  
            // 退格，将文本最后一个字符去掉  
            text = text.substring(0, i - 1);  
            if (text.length() == 0) {  
                // 如果文本没有了内容，则初始化计算器的各种值  
                resultText.setText("");  
            } else {  
                // 显示新的文本  
                resultText.setText(text);  
            }  
        }  
    } 
    /* 处理C键被按下的事件 */  
    private void handleC() {  
        // 初始化计算器的各种值  
        resultText.setText(""); 
        isFunction = false;   
	    opreator = "";
        
    }
    /*****************************************************************************************************************/
    /**
     * 简单型———按照给定的表达式计算
     * @param expression 要计算的表达式例如:5+12*(3+5)/7
     * @return
     */
    public double calculate(String expression) {
        Stack<String> resultStack  = new Stack<String>();
        prepare(expression);
        Collections.reverse(postfixStack);//将后缀式栈反转
        String firstValue  ,secondValue,currentValue;//参与计算的第一个值，第二个值和算术运算符
        while(!postfixStack.isEmpty()) {
            currentValue  = postfixStack.pop();
            if(!isOperator(currentValue.charAt(0))) {//如果不是运算符则存入操作数栈中
                resultStack.push(currentValue);
            } else {//如果是运算符则从操作数栈中取两个值和该数值一起参与运算
                 secondValue  = resultStack.pop();
                 firstValue  = resultStack.pop();
                 String tempResult  = calculate(firstValue, secondValue, currentValue.charAt(0));
                 resultStack.push(tempResult);
            }
        }
        return Double.valueOf(resultStack.pop());
    }
    /*
     * 数据准备阶段将表达式转换成为后缀式栈   @param expression
     */
    private void prepare(String expression) {
        opStack.push(',');//运算符放入栈底元素逗号，此符号优先级最低
        char[] arr  = expression.toCharArray();
        int currentIndex  = 0;//当前字符的位置
        int count = 0;//上次算术运算符到本次算术运算符的字符的长度便于或者之间的数值
        char currentOp  ,peekOp;//当前操作符和栈顶操作符
        for(int i=0;i<arr.length;i++) {
            currentOp = arr[i];
            if(isOperator(currentOp)) {//如果当前字符是运算符
                if(count > 0) {
                    postfixStack.push(new String(arr,currentIndex,count));//取两个运算符之间的数字
                }
                peekOp = opStack.peek();
                if(currentOp == ')') {//遇到反括号则将运算符栈中的元素移除到后缀式栈中直到遇到左括号
                    while(opStack.peek() != '(') {
                        postfixStack.push(String.valueOf(opStack.pop()));
                    }
                    opStack.pop();
                } else {
                    while(currentOp != '(' && peekOp != ',' && compare(currentOp,peekOp) ) {
                        postfixStack.push(String.valueOf(opStack.pop()));
                        peekOp = opStack.peek();
                    }
                    opStack.push(currentOp);
                }
                count = 0;
                currentIndex = i+1;
            } else {
                count++;
            }
        }
        if(count > 1 || (count == 1 && !isOperator(arr[currentIndex]))) {//最后一个字符不是括号或者其他运算符的则加入后缀式栈中
            postfixStack.push(new String(arr,currentIndex,count));
        } 
        
        while(opStack.peek() != ',') {
            postfixStack.push(String.valueOf( opStack.pop()));//将操作符栈中的剩余的元素添加到后缀式栈中
        }
    }
    /*
     * 判断是否为算术符号
     * @param c
     * @return
     */
    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '(' ||c == ')';
    }
    
    /*
     * 利用ASCII码-40做下标去算术符号优先级
     * @param cur
     * @param peek	
     * @return
     */
    public  boolean compare(char cur,char peek) {// 如果是peek优先级高于cur，返回true，默认都是peek优先级要低
        boolean result  = false;
        if(operatPriority[(peek)-40] >= operatPriority[(cur) - 40]) {
           result = true;
        }
        return result;
    }
    
    /*
     * 按照给定的算术运算符做计算
     * @param firstValue
     * @param secondValue
     * @param currentOp
     * @return
     */
    private String calculate(String firstValue,String secondValue,char currentOp) {
    	double fV = Double.valueOf(firstValue).doubleValue();
    	double sV = Double.valueOf(secondValue).doubleValue();
        String result  = "";
        switch(currentOp) {
            case '+':
                result = String.valueOf(fV+sV);
                break;
            case '-':
                result = String.valueOf(fV-sV);
                break;
            case '*':
                result = String.valueOf(fV*sV);
                break;
            case '/':
                result = String.valueOf(fV/sV);
                break;
        }
        return result;
    }
    /**********************************************************************************************/
    
    /******************************************************************************************************************/
    /** 
     *科学型———— 处理科学计算运算符键被按下的事件 
     */  
    private void handleFunction(String key) { 
    	double resultNum = 0;  
        if (key.equals("x!")) {  
            // 阶乘运算  
        	try{
        		int num = Integer.parseInt(resultText.getText());
        		resultNum = factorial(num);
        	}catch(NumberFormatException e2){
       		 	resultText.setText("请输入整数");
        	} 
        	
        }else if(key.equals("1/x")){
        	//计算一个数的倒数
        	if(getNumberFromText()==0){   //判断要计算的数是否为零
        		resultText.setText("0没有倒数！");
        	}else{
        		resultNum = 1/getNumberFromText();
        	}
        	
        	
        }else if(key.equals("x^2")){
        	//计算一个数的平方
        	resultNum = getNumberFromText()*getNumberFromText();
        }else if (key.equals("%")){
        	//取百分号运算
        	resultNum = getNumberFromText()/100;  
        	
        }else if (key.equals("√")) {  
            // 平方根运算  
        	resultNum = calculate(expression);
        	if(resultNum<=0){
        		resultText.setText("you entered must be positive number");
            }else{
            	resultNum = Math.sqrt(resultNum);      	    	
        	}
        	
        }else if (key.equals("x^y")){
        	//计算x的y次方 
        	resultNum = calculate(expression);
        	resultNum = Math.pow(baseNum,resultNum);
        	
        }else if (key.equals("sin")) {  
            // 三角函数sin()
        	resultNum = calculate(expression);
        	resultNum = Math.sin(resultNum);
        	
        }else if (key.equals("cos")) {  
            // 三角函数cos() 
        	resultNum = calculate(expression);
        	resultNum = Math.cos(resultNum); 
        	 
        }else if (key.equals("tan")) {  
            //三角函数tan()
        	resultNum = calculate(expression);
            resultNum = Math.tan(resultNum); 
            
        }else if (key.equals("e^x")){
        	//计算自然常数e的x次方
        	resultNum = calculate(expression);
       	    resultNum = Math.exp(resultNum);//显示有点问题
       	    
        }else if (key.equals("In")){
        	//计算自然对数
        	resultNum = calculate(expression);
        	resultNum = In(resultNum);//计算错误
        	
        }else if (key.equals("e")){
       	 //计算自然常数e
       	 resultNum = Math.E;//显示有待改进
       	 
       }else if (key.equals("π")){
       	 //计算圆周率 π
       	 resultNum = Math.PI;//显示有点问题
       	 
       }
        
        
        resultText.setText(String.valueOf(resultNum));
 
    
    }  
    
    /*计算阶乘*/
    private double factorial(int x) {
    	if(x==0){
    		return(1);
    	}else{
    		 
    	    String style="0.0";
    	    DecimalFormat df = new DecimalFormat();  
    	    style = "0.00000E000";  
    	    BigDecimal num=new BigDecimal(String.valueOf(x));//构造BigDecimal时指定有效精度s
            for(int i = 1; i <= x; i++){  
                String temp1 = Integer.toString(i);  
                BigDecimal temp2 = new  BigDecimal(temp1);  
                num = num.multiply(temp2);  
           } 
           df.applyPattern(style);  
           return(Double.valueOf(df.format(num)).doubleValue());  //将大数类转换成double型返回
    	}
   }
 
   /*计算对数log(x,y)*/
   private double Log(double x,double y){
    	
    	if(x<=0||y<=0){
    		resultText.setText("you entered must be positive number");
    		return 0;
        }else{
	    	double log = Math.log(y)/Math.log(x);
	    	return (log);
    	}
    	
   }
   /***********************************************************************************************/
  
  
   /************************************************************************************************/
   /**
    * 统计型————实现统计计算器的功能
    * */
   private  void handleSpecialFunction(String label){
	   if(label.equals("C(N,M)")){//计算组合数
		   resultText.setText("请输入n和m，并以”/“隔开                  C( "); 
	   }else if(label.equals("A(N,M)")){//计算排列数
		   resultText.setText("请输入n和m，并以”/“隔开                  A( "); 

	   }else if(label.equals("D")){//计算方差
		   
	   }else{//计算平均值
		   
	   }
	   
   } 
   /*计算组合公式*/
   private int C(int x,int y){
   	
	   	if(x<=0||y<=0){
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
  		
    	if(n<=0||m<=0){
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
   /*计算平均值*/
   private double Ave(int order[]){
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
   	double average=Ave(s);
   	int total=0,k=0;
   	 for(int i=0;i<l;i++){
   		 total += (s[i]-average)*(s[i]-average); 
   		 k++;
   	 }
   	double S =(double)total/k;
   	return S;
   	
   }
   /***********************************************************************************************/
   /***********************************************************************************************/
   /**
    * 拓展型————处理拓展部分事件
    * */
   
   private void handleSupplement(String label){
	   double weight,height,waistLine;
	   int    age;
	   
	   weight = Double.valueOf(weightText.getText()); //从输入框中获得体重
	   height = Double.valueOf(heightText.getText());//从输入框中获得身高
	   waistLine = Double.valueOf(waistText.getText());
	   age = Integer.valueOf(ageText.getText());
	   if(label.equals("BMI")){
		   BMI(weight,height);
	   }else if(label.equals("BFR")){
		   BFR(weight,height,waistLine,age);   
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
	   double bfr;
    	if((c.getSelectedCheckbox())==cRadio1){//女生计算公式    //缺省
    		double a=waistline*0.74;
    		double b=weight*0.082+34.89;
    		 bfr=(a-b)/weight;
    	}else{//男生计算公式
    		double a=waistline*0.74;
    		double b=weight*0.082+44.74;
    	    bfr=(a-b)/weight;
    		
    	}	
    	resultText.setText(bfr+"");
    }
   /***********************************************************************************************/
   
   /** 
    * 从结果文本框中获取数字 
    *  
    * @return 
    */  
   private double getNumberFromText() {  
       double result = 0;  
       try {  
           result = Double.valueOf(resultText.getText()).doubleValue();  
       } catch (NumberFormatException e) {  
       }          return result;  
   }
   
  /********主类，创建一个Calculator2对象****************************************************************************************************/
    public static void main(String args[]) {  
        Calculator2 calculator = new Calculator2();  
        calculator.setVisible(true);  
        calculator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
    }  
}  
