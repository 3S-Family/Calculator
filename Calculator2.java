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
    private final String[] FUNCTION = { "1/x", "x^2", "x^y", "x!", "√" ,"√(n&x)",
    		"sin","cos","tan","In","lg","log","e^x","e","π"};
    /*统计型计算器上特殊功能图标显示名称*/
    private final String[] SPECIAL_FUNTION = { "C_n^m", "A_n^m","s^2", "ave" };
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
    double baseNum=1;
    boolean expo=false;
    
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
        
        // 初始化科学计算器函数功能键，将基本函数功能和科学计算器功能键放在一个画板内  
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
        // 获取事件源的标签  
    	double result = 0.0;//baseNum是为了记录计算x的y次时的x的值
//    	char finalChar;//记录从文本框获得的最后一个字符
//    	String sign=resultText.getText();
//      	finalChar=sign.charAt(sign.length()-1);
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
      	
        }else if (label.equals(KEYS[2])) { // 用户按了"C"键  ，初始化计算器
        	resultText.setText("0");       //将所有输入清空 
        }else if (label.equals(KEYS[3])) {  //用户按了“←”退回键，将文本框最后一个字符去掉
            handleBackspace();
        }else if ("0123456789.()+-*/".indexOf(label) >= 0) {  // 用户按了输入要计算的表达式
        }
        else {
        	
        	} 


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
            } else {  
                // 显示新的文本  
                resultText.setText(text);  
            }  
        }  
    } 
    /** 
     * 处理C键被按下的事件 
     */  
    private void handleC() {  
        // 初始化计算器的各种值  
        resultText.setText("");    
    }

    
  /********主类，创建一个Calculator2对象****************************************************************************************************/
    public static void main(String args[]) {  
        Calculator2 calculator = new Calculator2();  
        calculator.setVisible(true);  
        calculator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
    }  
}  
