import java.awt.event.ActionEvent;  
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;  
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import java.math.BigDecimal; //引入大数类 
import java.util.Collections;
import java.util.Stack;
import java.awt.*;
import java.text.DecimalFormat;
import java.lang.NumberFormatException;
import java.lang.String;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

 
/** 
 * 一个计算器，支持简单的计算
 */  
public class Calculator extends JFrame implements ActionListener {  
	/***************************************************************************************************************/
	/********计算器相关按键名称及按钮设置*******/
	
	/*计算器模式的切换的名称显示*/
    private final String[] MODEL = {"简单型","科学型","统计型","健康工具"};
    /*基础计算器上的键的显示名字 */  
    private final String[] KEYS = {"(",")", "C","←","7", "8", "9", "+" , "4", "5", "6",  
            "-", "1", "2", "3", "*", "0", ".", "=", "/" }; 
    /*科学型计算器上的函数图标显示名称*/ 
    private final String[] FUNCTION = { "1/x", "χ2", "x^y", "x!", "sin","cos","tan","√" ,
    		"In","lg","log","%","e^x","e","π",","};
    /*统计型计算器上特殊功能图标显示名称*/
    private final String[] SPECIAL_FUNTION = { "排列", "组合","D", "Ave","B",
    		"P(λ)","Φ",","};
    /*健康管理计算器上的键的显示名称*/
    private final String[] SUPPLEMENT={"男","女","确定","Back"};
    
    /*计算各个按钮数组的长度，并将他们存入相应变量中*/
    int modelLen=MODEL.length;//用于存放MODEL数组中按钮个数
    int keysLen=KEYS.length;//用于存放KEYS数组中的按钮个数
    int functionLen=FUNCTION.length;//用于存放FUNCTION数组中的按钮个数
    int special_functionLen=SPECIAL_FUNTION.length;//用于存放SPECIAL_FUNTION 数组中的按钮个数
    int supplementLen=SUPPLEMENT.length;//用于存放健康管理模型中按钮的数量
   
    /*模式切换上的计算器*/
    private JButton models[] = new JButton[keysLen];
    /*计算结果文本框 */  
    private JTextField resultText = new JTextField("");//消除了默认的0
    /*身高输入文本框 */  
    private JTextField heightText = new JTextField();
    /*体重输入文本框 */  
    private JTextField weightText = new JTextField();
    /*年龄输入文本框 */  
    private JTextField ageText = new JTextField();
    /*简单计算器上键的按钮 */  
    private JButton keys[] = new JButton[keysLen];  
    /*计算器上功能图标显示名称*/  
    private JButton function[] = new JButton[functionLen+keysLen]; 
    /*计算器上常用函数的按钮*/
    private JButton special_function[] = new JButton[special_functionLen+keysLen];
    /*计算器的健康工具模型上的按钮*/
    private JButton supplements[]=new JButton[supplementLen];
    
    /*****************************************************************************************************/
    /****/
    
    /*为了实现优先级的设置*/
    //后缀式栈
    private Stack<String> postfixStack  = new Stack<String>();
    //运算符栈	
    private Stack<Character> opStack  = new Stack<Character>();
    //运用运算符ASCII码-40做索引的运算符优先级
    private int [] operatPriority  = new int[] {0,3,2,1,-1,1,0,2}; 
    
    /*为了实现科学计算及其他多个数据输入*/
    private double  baseNum = 1;    //保存计算x^y时x的值
    private String   expression = "("; //存放科学计算时除去运算符的表达式。如计算sin（5+12*(3+5)/7）时，expression=5+12*(3+5)/7
    private boolean isFunction = false;   //是否是科学计算
    private boolean isSpecialFunction = false;   //是否是统计计算
    private String   opreator = ""; //存放科学计算时的操作符 
    private Stack<String> numStack  = new Stack<String>();//存放输入的多个操作数
    
    /*健康工具模式下的管理*/

    //设置几个panel,Panel1表示简单型计算器的界面，panel2表示科学型计算器的界面，panel3表示统计型计算器的界面，panel4表示健康工具计算器的界面
    //topPanel画板放文本框，resultPanel是为了放置最后体重管理的结果
    JPanel panel,panel1,panel2,panel3,panel4,panel5,topPanel,resultPanel;  
    //设置几个String类型的变量，在焦点没有聚集在相应文本框上时的默认显示
    String Sex="";//记录选择的是男性还是女性
    //weightText,heightText,ageText的初始化信息
    String info1="请输入你的年龄(岁)";
    String info2="请输入你的身高(米)";
    String info3="请输入你的体重(千克)";
    /*为了实现BMI-体质率计算设置的label*/
    JLabel labeltop,label1,label2,label3,label4,label5;
    //设置单选框，以方便选择男女
    Checkbox cRadio1,cRadio2;
    CheckboxGroup c;
    /*为了实现运动强度的选择下拉框*/
    private JComboBox comboBox;
    //age为了记录健康管理中的年龄，sex为了记录男女，男性为1，女性为0
    int age,sex;
    //weight，height为了记录健康管理中的身高，体重
	double weight,height;
	//存放运功强度
	String [] NAMES;
	//体重指数显示中的各个label以及返回按钮
	JLabel shapeLabel,idealLabel,bmiLabel, rateLabel,bfrLabel,
	       baseLabel,needLabel,onLabel,loseLabel,attentionLabel;
	//小数格式化保留六位小数
	DecimalFormat df= new DecimalFormat("0.000000");   
	DecimalFormat dfp= new DecimalFormat("0.000"); 
	DecimalFormat dfs= new DecimalFormat("0.0000"); 
	DecimalFormat dft= new DecimalFormat("0.00"); 
	//记录点击男性还是女性，美化界面
	boolean isMan=false,isWoman=false;
    
   /** 
    ************ 构造函数 *************************************************************************
    *********************/  
    public Calculator() {  
        super();  
        // 初始化计算器  
        init();  
        // 设置计算器的背景颜色  
        this.setTitle("Calculator");  
        // 在屏幕(500, 300)坐标处显示计算器  
        this.setLocation(500, 300);  
        // 不许修改计算器的大小  
        this.setResizable(false);              
        //设置计算器大小  
        this.setSize(400, 600); 
    }
  
    /** 
     *******初始化计算器 ***************************************************************************************** 
     *计算器的基本布局
     */ 
    
    private void init() {   	
        // 文本框中的内容采用右对齐方式  
        resultText.setHorizontalAlignment(JTextField.RIGHT);  
        // 允许修改结果文本框  
        resultText.setEditable(true);            
        // 设置文本框背景颜色为白色  
        resultText.setBackground(Color.white);
        //去掉文本框的边框
        resultText.setBorder(new EmptyBorder(5,5,5,10));
        //设置文本框的大小
        resultText.setSize(100,200);
        //设置文本框的字体格式
        resultText.setFont(new Font("微软雅黑", Font.PLAIN, 20));
        resultText.setForeground(new Color(139,126,102 )); 
        /*为健康工具中的文本框初始化以及设置字体样式*/
        ageText.setText(info1);
        ageText.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        ageText.setForeground(new Color(139,126,102 ));
        heightText.setText(info2);
        heightText.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        heightText.setForeground(new Color(139,126,102 )); 
        weightText.setText(info3);
        weightText.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        weightText.setForeground(new Color(139,126,102 ));
        /*为文本框添加焦点事件相应器*/
        ageText.addFocusListener(new MyFocusListener(info1, ageText));
        heightText.addFocusListener(new MyFocusListener(info2, heightText));//添加焦点事件反映 
        weightText.addFocusListener(new MyFocusListener(info3, weightText));
        
        //模型切换的四个按钮放在一个画板名叫modelPanel的里面
        JPanel modelPanel = new JPanel();
        //设置modelPanel里面的按钮从左到右排列,水平居左以及其背景色等
        FlowLayout fl = new FlowLayout(); 
        fl.setAlignment(FlowLayout.LEFT); 
        modelPanel.setLayout(fl);
        modelPanel.setBackground(new Color(39,42,47 ));
        for (int i = 0; i < modelLen; i++) {  
            models[i] = new JButton(MODEL[i]);  
            //去掉modelsPanel里面的button的边界线
            models[i].setBorderPainted(false);
            //去掉button点击时字体上出现的边界框
            models[i].setFocusPainted(false);
            //设置button里面字体的格式
            models[i].setFont(new Font("微软雅黑", Font.PLAIN, 18));
            models[i].setForeground(Color.white);
            models[i].setBackground(new Color(39,42,47 ));
            modelPanel.add(models[i]); 

        }  
        
       // 建立一个画板放文本框  
       topPanel = new JPanel();  
       topPanel.setLayout(new BorderLayout(0,0));  
       topPanel.add("Center", resultText); 
      
       //建立一个顶部Panel来存放模式切换和显示框架
       JPanel top = new JPanel();  
       top.setLayout(new GridLayout(2,1,0,0));  
       top.add(modelPanel); 
       top.add(topPanel);
      
       // 初始化基本计算器上数字、运算、括号、清除等键的按钮，将键放在一个名叫calkeysPanel画板内  
       JPanel calckeysPanel = new JPanel();  
       // 用网格布局器，4行，4列的网格，网格之间的水平方向间隔为3个象素，垂直方向间隔为3个象素  
       calckeysPanel.setLayout(new GridLayout(5, 4, 15, 15)); 
       calckeysPanel.setBackground(Color.black);
       for (int i = 0; i < keysLen; i++) {  
            keys[i] = new RaButton(KEYS[i]);  
            calckeysPanel.add(keys[i]);  
       }     

       // 新建一个大的画板，将calckeys画板放在该画板内  
       panel1 = new JPanel();  
       // 画板采用边界布局管理器，画板里组件之间的水平和垂直方向上间隔都为0象素  
       panel1.setLayout(new BorderLayout(0,0));   
       panel1.setBackground(new Color(54,54,54   ));
       JLabel space=new JLabel();
       panel1.add("North",space);
       panel1.add("Center", calckeysPanel);
        
       // 初始化科学计算器函数功能键，将 基本函数功能和科学计算器功能键放在一个画板内  
       JPanel functionsPanel = new JPanel();  
       // 用网格布局管理器，5行，7列的网格，网格之间的水平方向间隔为0个象素，垂直方向间隔为0个象素  
       functionsPanel.setLayout(new GridLayout(9, 4,15,5));
       functionsPanel.setBackground(Color.black);
       //加入一些科学运算的按钮
       for (int i = 0; i < functionLen; i++) {  
            function[i] = new RaButton(FUNCTION[i]);   
            functionsPanel.add(function[i]); 
       } 
       //加入基本运算以及数字按钮
       for (int i = functionLen; i < (keysLen+functionLen); i++) {  
        	function[i] = new RaButton(KEYS[i-functionLen]); 
        	functionsPanel.add(function[i]);  
       }  
 
       // 新建一个大的画板，将function画板放在该画板内，实现科学计算器的界面
       panel2=new JPanel();
       panel2.setLayout(new BorderLayout(0,0));
       panel2.setBackground(new Color(54,54,54   ));
       panel2.add("Center",functionsPanel);
        
       // 初始化计算器特殊函数功能键，将基本函数功能键放在一个画板内  
       JPanel special_functionsPanel = new JPanel();  
       // 用网格布局管理器，7行，4列的网格，网格之间的水平方向间隔为0个象素，垂直方向间隔为0个象素  
       special_functionsPanel.setLayout(new GridLayout(7, 4, 10, 10)); 
       special_functionsPanel.setBackground(Color.black);
       //加入统计运算的功能按钮
       for (int i = 0; i < special_functionLen; i++) {  
        	special_function[i] = new RaButton(SPECIAL_FUNTION[i]);   
        	special_functionsPanel.add(special_function[i]);   
       }  
       //加入基本运算以及数字按钮
       for (int i = special_functionLen; i < (keysLen+special_functionLen); i++) {  
        	special_function[i] = new RaButton(KEYS[i-special_functionLen]);   
        	special_functionsPanel.add(special_function[i]);  
       }  
       //新建一个大的画板，将special_function画板放在该画板内，实现统计计算器的界面
       panel3=new JPanel();
       panel3.setLayout(new BorderLayout(0,0));
       panel3.setBackground(new Color(54,54,54   ));
       panel3.add("Center",special_functionsPanel);
        
       //初始化计算器扩展模式，将扩展功能的函数放在一个panel上        
       //sexPanel存放性别，设置其格式扥
       JPanel sexPanel=new JPanel();
       sexPanel.setLayout(new FlowLayout(FlowLayout.CENTER,20,10));
       //label1存放性别选项
       label1=new JLabel("请输入性别");
       label1.setFont(new Font("微软雅黑", Font.PLAIN, 18));
       label1.setForeground(Color.white); 
       //sexPanel存放性别
       sexPanel.add(label1);
       sexPanel.setBackground(new Color(39,42,47 ));
       //sex存放性别前的提示
       JPanel sex=new JPanel();
       sex.setLayout(new GridLayout(1,2,10,0));
       sex.setBackground(new Color(39,42,47 ));
       for (int i = 0; i < 2; i++) {  
        	supplements[i] = new sexButton(SUPPLEMENT[i]); 
            //设置button里面字体大小
            supplements[i].setFont(new Font("微软雅黑", Font.PLAIN, 18));
        	sex.add(supplements[i]);  
        }  
        sexPanel.add(sex);
        //agePanel存放年龄
        JPanel agePanel=new JPanel();
        agePanel.setLayout(new FlowLayout(FlowLayout.CENTER,20,10));
        agePanel.setBackground(new 	Color(39,42,47 ));
        //label2存放年龄输入框
        label2=new JLabel("请输入年龄");
        label2.setFont(new Font("微软雅黑", Font.PLAIN, 18));
        label2.setForeground(Color.white); 
        ageText.setBorder(new EmptyBorder(5,5,5,10));//去文本框边框
        agePanel.add(label2);
        agePanel.add(ageText);
        
        //heightPanel存放身高
        JPanel heightPanel=new JPanel();
        heightPanel.setLayout(new FlowLayout(FlowLayout.CENTER,20,10));
        heightPanel.setBackground(new Color(39,42,47 ));
        heightText.setBorder(new EmptyBorder(5,5,5,10));//去文本框边框
        //label3存放身高输入框
        label3=new JLabel("请输入身高");
        label3.setFont(new Font("微软雅黑", Font.PLAIN, 18));
        label3.setForeground(Color.white); 
        heightPanel.add(label3);
        heightPanel.add(heightText);
        
        //weightPanel存放体重
        JPanel weightPanel=new JPanel();
        weightPanel.setLayout(new FlowLayout(FlowLayout.CENTER,20,10));
        weightPanel.setBackground(new Color(39,42,47 ));
        label4=new JLabel("请输入体重");
        label4.setFont(new Font("微软雅黑", Font.PLAIN, 18));
        label4.setForeground(Color.white); 
        weightText.setBorder(new EmptyBorder(5,5,5,10));//去文本框边框
        weightPanel.add(label4);
        weightPanel.add(weightText);
        
        //sportPanel存放运动强度
        JPanel sportPanel=new JPanel();
        sportPanel.setBackground(new Color(39,42,47 ));
        sportPanel.setLayout(new FlowLayout(FlowLayout.CENTER,20,10));
        label5=new JLabel("请选择运动程度");
        label5.setFont(new Font("微软雅黑", Font.PLAIN, 18));
        label5.setForeground(Color.white);  
        sportPanel.add(label5);
        //下拉框
        NAMES= new String[5];
        NAMES[0] = "坐式生活方式(极少运动)";
        NAMES[1] = "轻微活动，日常活动";
        NAMES[2] = "中等强度健身(每周3-4次运动)";
        NAMES[3] = "大强度健身(每周4次以上)";
        NAMES[4] = "专业运动员(每周6次以上)";
        //下拉框选项的样式设置
        comboBox = new JComboBox(NAMES);
        comboBox.setEditable(false);
        comboBox.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        comboBox.setForeground(new Color(0,0,0)); 
        sportPanel.add(comboBox);
        
        //submitPanel存放确认键
        JPanel submitPanel=new JPanel();
        submitPanel.setBackground(new 	Color(39,42,47 ));
        submitPanel.setLayout(new FlowLayout());
        supplements[2] = new sexButton(SUPPLEMENT[2]); 
        submitPanel.add(supplements[2]);
         
        //新建一个大画板Panel4,BMI-体脂率计算
        JPanel panel_4=new JPanel();
        panel_4.setLayout(new GridLayout(6,1,0,0));
        panel_4.add(sexPanel);
        panel_4.add(agePanel);
        panel_4.add(heightPanel);
        panel_4.add(weightPanel);
        panel_4.add(sportPanel);
        panel_4.add(submitPanel);
        panel4=new JPanel();
        panel4.setLayout(new BorderLayout());
        panel4.add("Center",panel_4);
        
        //新建一个resultPanel,现实BMI等健康计算结果
        resultPanel=new JPanel();
      
        //实例化各个结果label
        shapeLabel =new JLabel("",JLabel.CENTER);
        idealLabel=new JLabel("",JLabel.CENTER);
        bmiLabel=new JLabel("",JLabel.CENTER);
        rateLabel=new JLabel("",JLabel.CENTER);
        bfrLabel=new JLabel("",JLabel.CENTER);
        baseLabel=new JLabel("",JLabel.CENTER);
        needLabel=new JLabel("",JLabel.CENTER);
        onLabel=new JLabel("",JLabel.CENTER);
        loseLabel=new JLabel("",JLabel.CENTER);
        attentionLabel=new JLabel("",JLabel.CENTER);
        //健康指数结果显示的各个label的样式设置
        handleLabel(shapeLabel);
        handleLabel(idealLabel);
        handleLabel(bmiLabel);
        handleLabel(rateLabel);
        handleLabel(bfrLabel);
        handleLabel(baseLabel);
        handleLabel(needLabel);
        handleLabel(onLabel);
        handleLabel(loseLabel);
        handleLabel(attentionLabel);
        //重新计算按钮
        supplements[3] = new sexButton(SUPPLEMENT[3]); 
        //健康指数panel样式设置
        resultPanel.setLayout(new GridLayout(11,1));
        resultPanel.setBackground(new Color(39,42,47 ));
        resultPanel.add(shapeLabel);
        resultPanel.add(idealLabel);
        resultPanel.add(bmiLabel);
        resultPanel.add(rateLabel);
        resultPanel.add(bfrLabel);
        resultPanel.add(baseLabel);
        resultPanel.add(needLabel);
        resultPanel.add(onLabel);
        resultPanel.add(loseLabel);
        resultPanel.add(attentionLabel);
        resultPanel.add(supplements[3]);
        
        /***************为各按钮添加事件侦听器***********************************/   
        //都使用同一个事件侦听器，即本对象。本类的声明中有implements ActionListener
        for (int i = 0; i < modelLen; i++) {  
            models[i].addActionListener(this);  
        }  
        for (int i = 0; i < keysLen; i++) {  
            keys[i].addActionListener(this);  
        }   
        for (int i = 0; i < functionLen+keysLen; i++) {  
            function[i].addActionListener(this);  
        } 
        for (int i = 0; i < special_functionLen+keysLen; i++) {  
            special_function[i].addActionListener(this);  
        }
        for (int i = 0; i < supplementLen; i++) {  
        	supplements[i].addActionListener(this);  
        }
        
        
        //新建一个画板panel,计算机的整体布局
        panel=new JPanel(new BorderLayout(0,0));
        panel.setBackground(new Color(54,54,54));  
        add(panel);
        panel.add("North",top);
        panel.add("Center",panel1);  
    }    
    

    /** 
     *********处理事件 *****************************************************************************************************
     */  
    public void actionPerformed(ActionEvent e) {  
    	// 获取事件源的标签  
        String label = e.getActionCommand();
        //四种模式选择：简单型；科学型；统计型；扩展型；
        if(label.equals(MODEL[0])){//简单型
        	panel.add("Center",panel1);
        	panel1.setVisible(true);
        	panel2.setVisible(false);
        	panel3.setVisible(false);
        	panel4.setVisible(false);
        	resultPanel.setVisible(false);
        	handleC();     
        }else if(label.equals(MODEL[1])){//科学型
        	panel.add("Center",panel2);
        	panel1.setVisible(false);
        	panel2.setVisible(true);
        	panel3.setVisible(false);
        	panel4.setVisible(false);
        	resultPanel.setVisible(false);
        	handleC();     
        }else if(label.equals(MODEL[2])){//统计型
        	panel.add("Center",panel3);
        	panel1.setVisible(false);
        	panel2.setVisible(false);
        	panel3.setVisible(true);
        	panel4.setVisible(false);
        	resultPanel.setVisible(false);
        	handleC();     
        }else if(label.equals(MODEL[3])){//健康管理
        	ageText.setText(info1);
        	heightText.setText(info2);
        	weightText.setText(info3);
        	panel.add("Center",panel4);
           	panel1.setVisible(false);
            panel2.setVisible(false);
      	    panel3.setVisible(false);
      	    panel4.setVisible(true); 
      	    resultPanel.setVisible(false);
            handleC();   
        	    
        }else if (label.equals("=")) {  // 用户按了"="键  ,输入结束，计算结果
        	    handleEqual();

        }else if (label.equals(KEYS[2])) { // 用户按了"C"键  ，初始化计算器
        	    handleC();                     //将所有输入清空 
        	
        }else if (label.equals(KEYS[3])) {  //用户按了“←”退回键，将文本框最后一个字符去掉
            handleBackspace();
        	
        }else if ("0123456789.()+-*/,eπ".indexOf(label) >= 0) {  // 用户按了输入要计算的多项式
        	if(isFunction||isSpecialFunction){           //如果是科学计算，如sin（），将输入的多项式存入一个字符串
        		expression = expression + label;
        	}
        	resultText.setText(resultText.getText()+label);

        }else if(label.equals(FUNCTION[0])||label.equals(FUNCTION[1])||//处理科学型计算器部分功能,调用handleFunction()函数
        		label.equals(FUNCTION[3])||label.equals(FUNCTION[11])){//依次为：1/x,x^2,x!,%
            handleFunction(label);
            
        }else if(label.equals(SPECIAL_FUNTION[0])||label.equals(SPECIAL_FUNTION[1])||//如果是统计计算器，转入handleSpecialFunction（）
        		label.equals(SPECIAL_FUNTION[2])||label.equals(SPECIAL_FUNTION[3])||
        		label.equals(SPECIAL_FUNTION[4])||label.equals(SPECIAL_FUNTION[5])||
        	    label.equals(SPECIAL_FUNTION[6])){
        	if(label.equals(SPECIAL_FUNTION[0])){//用户按了“排列”按钮
     		    resultText.setText("C ("); 

        	}else if(label.equals(SPECIAL_FUNTION[1])){//用户按了“组合”按钮
     		    resultText.setText("A ("); 

        	}else if(label.equals(SPECIAL_FUNTION[5])){//用户按了“P(λ)”按钮
     		    resultText.setText("P ("); 

        	}else {
     		    resultText.setText(label + "("); //用户按了其他统计函数，输出模式相同
        	}
        	isSpecialFunction = true;	
        	opreator = label;

        }else if(label.equals(SUPPLEMENT[0])){//处理选择为男性
 		    Sex="男";
 		    isMan=true;
 		    if(isMan){
 		      supplements[0].setForeground(Color.RED);
 		      supplements[1].setForeground(Color.white);
 		      isMan=false;
 		    }

    	}else if(label.equals(SUPPLEMENT[1])){//处理选择为女性
 		    Sex="女";
		    isWoman=true;
		    if(isWoman){
		       supplements[0].setForeground(Color.white);
 		       supplements[1].setForeground(Color.RED);
 		       isWoman=false;
		    }
        	
        	
        }else if(label.equals(SUPPLEMENT[2])){//
        	String input;//判断输入是否完全
        	input=HandelNum();
        	if(input=="输入完成"){//输出结果
                shapeLabel.setText(BMIInform());
                idealLabel.setText("理想体重:"+DBW()+"公斤");
                bmiLabel.setText("BMI(身体质量指数):"+BMI());
                rateLabel.setText("疾病发病危险性:"+DiseaseRate());
                bfrLabel.setText("体脂率:"+BFR()+"%");
                baseLabel.setText("基础代谢率:"+BMR()+"卡路里/天");
                needLabel.setText("每天需要热量:"+dailyCalorie()+"卡路里");
                onLabel.setText("如需增重:"+PutOn()+"卡路里/天");
                loseLabel.setText("如需减肥:"+LoseWeight()+"卡路里/天");
                attentionLabel.setText("您体内的脂肪量"+BFRInform());
                
                supplements[0].setForeground(Color.white);
   		        supplements[1].setForeground(Color.white);

        	    panel.add("Center",resultPanel);
        	    resultPanel.setVisible(true);
         	    panel1.setVisible(false);
      	        panel2.setVisible(false);
      	        panel3.setVisible(false);
      	        panel4.setVisible(false);
      	        
      	        handleC();
      	    }else{
      	    	resultText.setText("请先完成输入");
      	    }
        	
        	
        }else if(label.equals(SUPPLEMENT[3])){//用户按了“确定”键，转入计算体脂等
        	ageText.setText(info1);
        	heightText.setText(info2);
        	weightText.setText(info3);
          	panel.add("Center",panel4);
         	panel1.setVisible(false);
      	    panel2.setVisible(false);
      	    panel3.setVisible(false);
      	    panel4.setVisible(true);
      	    resultPanel.setVisible(false);
      	    handleC();   
        	
        }else{//用户按了科学型中其他函数
        	if(label.equals("e^x")){
        		resultText.setText("e^"+"(");
        	}else if(label.equals("x^y")){
        		baseNum = getNumberFromText();
        		resultText.setText(resultText.getText()+"^(");
        	}else{
        		resultText.setText(label+"(");
        	}
        	
        	isFunction = true;	
        	opreator = label;
        }
    }
  
    /*处理回删键被按下的事件 */  
    private void handleBackspace() {  
        String text = resultText.getText();  
        int i = text.length(); 
        int j = expression.length(); 

        if (i > 0) {  
            // 退格，将文本最后一个字符去掉  
            text = text.substring(0, i - 1);  
            if (text.length() == 0) {  
                // 如果文本没有了内容，则初始化计算器的各种值  y  
                resultText.setText("");  
            } else {  
                // 显示新的文本  
                resultText.setText(text);  
            }  
        }  
        if(j>1){
            expression = expression.substring(0, j - 1);
            
        }
    } 
    
    
    /* 处理C键被按下的事件 */  
    private void handleC() {  
        // 初始化计算器的各种值  
        resultText.setText(""); 
        isFunction = false;   
        isSpecialFunction = false;   
	    opreator = "";
        baseNum = 1;    
	    expression = "(";
	    numStack.clear();
	    postfixStack.clear();
	    opStack.clear();
	    
        
    }
    
    /*处理" = "被按下的事件*/
    private void handleEqual() {
        
    	double result = 0.0;// 承接最终计算结果 
    	if(isFunction){
        	if((expression.charAt(expression.length()-1))!=')'){
        		expression = expression + ")";
        	}

    		handleFunction(opreator);
    	    isFunction = false;   //是否是科学计算，如sin,cos,lg等
    	    opreator = "";
    	}else if(isSpecialFunction){
    		
    		handleSpecialFunction(opreator);
    	    isSpecialFunction = false;   //是否是统计计算，如A（N,M）,C(N,M)等
    	    opreator = "";
    	}else{
    		if((resultText.getText() )==""){//如果用户没有输入数据，给予提醒
    	      	resultText.setText("请先输入数据");

    		}else{
    			result = calculate(resultText.getText());//计算结果
    	      	resultText.setText(resultText.getText()+" = "+result);//输出结果
    		}
	      	
    	}
    	
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
        int count = 0;//上次算术运算符到本次算术运算符的字符的长度便于获得之间的数值
        char currentOp = 'o'  ,peekOp;//当前操作符和栈顶操作符
        for(int i=0;i<arr.length;i++) {
            currentOp = arr[i];
            if(isOperator(currentOp)) {//如果当前字符是运算符
                if(count > 0) {
                	if(count==1&&arr[currentIndex] =='e'){//如果操作数是 e，则将其转换成相应数值再推进栈中
                		postfixStack.push(String.valueOf(Math.E));	
                	}else if(count==1&&arr[currentIndex]=='π'){//如果操作是π ，则将其转换成相应数值再推进栈中
                		postfixStack.push(String.valueOf(Math.PI));	
                	}else{
                        postfixStack.push(new String(arr,currentIndex,count));//取两个运算符之间的数字
                	} 
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
        	if(currentOp =='e'){//如果操作数是 e，则将其转换成相应数值再推进栈中
        		postfixStack.push(String.valueOf(Math.E));	
        	}else if(currentOp=='π'){//如果操作是π ，则将其转换成相应数值再推进栈中
        		postfixStack.push(String.valueOf(Math.PI));	
        	}else{
                postfixStack.push(new String(arr,currentIndex,count));//取两个运算符之间的数字
        	}        
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
    
    	double fV =0.0;
    	double sV =0.0;

        fV = Double.valueOf(firstValue).doubleValue();
        sV = Double.valueOf(secondValue).doubleValue();
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
  	/** 
     *科学型———— 处理科学计算运算符键被按下的事件 
     */  
    private void handleFunction(String key) { 
    	double resultNum = 0;  
        if (key.equals(FUNCTION[0])) {  
        	//计算一个数的倒数
        	if(getNumberFromText()==0){   //判断要计算的数是否为零
        		resultText.setText("0没有倒数");
        	}else{
        		resultNum = 1/getNumberFromText();
        		resultNum=Double.valueOf(df.format(resultNum));
        	    resultText.setText("1/"+getNumberFromText()+"="+resultNum);
        		 
        	}
        	
        }else if(key.equals(FUNCTION[1])){
        	//计算一个数的平方
        	resultNum = getNumberFromText()*getNumberFromText();
        	resultNum=Double.valueOf(df.format(resultNum));
            resultText.setText(getNumberFromText()+"^(2) = "+resultNum);
           
        }else if(key.equals(FUNCTION[2])){
        	//计算x的y次方
        	resultNum = calculate(expression);
        	resultNum = Math.pow(baseNum,resultNum);
        	 resultNum=Double.valueOf(df.format(resultNum));
             resultText.setText(resultText.getText()+" = "+resultNum);
        }else if(key.equals(FUNCTION[3])){
        	  //阶乘运算  
        	try{
        		double num = Double.valueOf(resultText.getText());
        		resultText.setText(num+"!= "+factorial0(num));
              	}catch(NumberFormatException e2){
       		 	resultText.setText("请输入整数");
        	} 
        	
        }else if (key.equals(FUNCTION[4])){
        	// 三角函数sin()
        	resultNum = calculate(expression);
        	resultNum = Math.sin(resultNum);
        	resultNum=Double.valueOf(df.format(resultNum));
            resultText.setText(resultText.getText()+" = "+resultNum);
        	
        }else if (key.equals(FUNCTION[5])) {  
        	 // 三角函数cos() 
        	resultNum = calculate(expression);
        	resultNum = Math.cos(resultNum);
        	resultNum=Double.valueOf(df.format(resultNum));
            resultText.setText(resultText.getText()+" = "+resultNum);        	
        }else if (key.equals(FUNCTION[6])){
        	 //三角函数tan()
        	resultNum = calculate(expression);
            resultNum = Math.tan(resultNum);
            resultNum=Double.valueOf(df.format(resultNum));
            resultText.setText(resultText.getText()+" = "+resultNum);        	
        	
        }else if (key.equals(FUNCTION[7])) {  
        	// 平方根运算  
        	resultNum = calculate(expression);
        	if(resultNum<=0){
        		resultText.setText("请输入正数");
            }else{
            	resultNum = Math.sqrt(resultNum); 
            	resultNum=Double.valueOf(df.format(resultNum));
                resultText.setText(resultText.getText()+" = "+resultNum);        	
        	}
                	
        }else if (key.equals(FUNCTION[8])) {  
        	//计算自然对数
        	resultNum = calculate(expression);
        	resultNum = Double.valueOf(Log(Math.E,resultNum));
        	resultNum=Double.valueOf(df.format(resultNum));
            resultText.setText(resultText.getText()+" = "+resultNum);        	
        
        }else if (key.equals(FUNCTION[9])) {  
        	//计算以10为底的对数
        	resultNum = calculate(expression);
        	resultNum =Double.valueOf(Log(10,resultNum));
        	resultNum=Double.valueOf(df.format(resultNum));
            resultText.setText(resultText.getText()+" = "+resultNum);        	
        }else if (key.equals(FUNCTION[10])) {  
        	//计算以任意为底的函数
        	getNumToken(expression);
        	resultNum = Double.valueOf(numStack.pop()).doubleValue();
        	baseNum = Double.valueOf(numStack.pop()).doubleValue();
            try {  
            	resultNum =Double.valueOf(Log(baseNum,resultNum));
            	resultNum=Double.valueOf(df.format(resultNum));
                resultText.setText(resultText.getText()+" = "+resultNum);    
            } catch (NumberFormatException e) { 
                resultText.setText(Log(baseNum,resultNum));    

            }  
        	    	        
        }else if (key.equals(FUNCTION[11])){
        	//取百分号运算
        	resultNum = getNumberFromText()/100;  
        	resultNum=Double.valueOf(df.format(resultNum));
            resultText.setText(resultText.getText()+"% = "+resultNum);        	
        	
        }else if (key.equals(FUNCTION[12])){
        	//计算自然常数e的x次方
        	resultNum = calculate(expression);
       	    resultNum = Math.exp(resultNum);//显示有点问题
       	    resultNum=Double.valueOf(df.format(resultNum));
            resultText.setText(resultText.getText()+" = "+resultNum);        	
        	
        }
    }  
    
    /*
     * 计算阶乘
     * @param factorial
     * @return
     */
    private String factorial0(double factorial) {
    	if(factorial==0.0){
    		return("1");
    	}else{
    	    int base=(int)factorial;
    	    String style="0.0";
    	    DecimalFormat df = new DecimalFormat();  
    	    style = "0.00000E000";  
    	    BigDecimal num=new BigDecimal(String.valueOf(factorial));//构造BigDecimal时指定有效精度s
            for(int i = 1; i <base; i++){  
                String temp1 = Integer.toString(i);  
                BigDecimal temp2 = new  BigDecimal(temp1);  
                num = num.multiply(temp2);  
                
           } 
            df.  applyPattern(style);  
            return(df.format(num)); 
    	}
    }
    
    /*
     * 计算大数阶乘
     * @param x
     * @return
     */
    private double factorial(int x) {
    	if(x==0){
    		return(1);
    	}else{
    		 
    	    String style="0.0";
    	    DecimalFormat df = new DecimalFormat();     	     
    	    BigDecimal num=new BigDecimal(String.valueOf(x));//构造BigDecimal时指定有效精度s
            for(int i = 1; i < x; i++){  
                String temp1 = Integer.toString(i);  
                BigDecimal temp2 = new  BigDecimal(temp1);  
                num = num.multiply(temp2);  
           } 
           df.applyPattern(style);  
           return(Double.valueOf(df.format(num)));  //将大数类转换成double型返回
    	}
   }
 
    /** 
     *计算以任意数为底的对数
     * @param x,y 底数，真数
     */
   private String Log(double x,double y){
    	if(x<=0){
    		return "底数需为正数";
    	}
    	if(y<=0){
    		return "真数需为正数";
        }
    	if(x==1){
    		return "底数不能为1";
        }
    	else{
	    	double log = Math.log(y)/Math.log(x);
	    	return String.valueOf(log);
    	}
    	
   }
   
  
  
   /************************************************************************************************/
   /**
    * 统计型————实现统计计算器的功能
    * */
   private  void handleSpecialFunction(String label){
	   
	   String resultNum = "";  
	   int n,m;
	   getNumToken(expression);
	   
	   if(label.equals(SPECIAL_FUNTION[0])){//计算组合数
		   m = Integer.valueOf(numStack.pop());
	       n = Integer.valueOf(numStack.pop());
	       resultNum =Combination(n,m);
	       try {  
	    	   int resultC = Integer.valueOf(resultNum);
			   //输出计算结果
		   	   if((expression.charAt(expression.length()-1))!=')'){
		   	       resultText.setText(resultText.getText()+")  = "+resultNum);//用户没有按")"，输出时使其自动补上
		       }else{
		           resultText.setText(resultText.getText()+"  = "+resultNum);
		       }    
           } catch (NumberFormatException e) { 
               resultText.setText(resultNum);    

           }
		   
	   }else if(label.equals(SPECIAL_FUNTION[1])){//计算排列数
		   m = Integer.valueOf(numStack.pop());
	       n = Integer.valueOf(numStack.pop());
	       resultNum =Arrangement(n,m);
	       try {  
	    	   int resultA = Integer.valueOf(resultNum);
			   //输出计算结果
		   	   if((expression.charAt(expression.length()-1))!=')'){
		   	       resultText.setText(resultText.getText()+")  = "+resultNum);//用户没有按")"，输出时使其自动补上
		       }else{
		           resultText.setText(resultText.getText()+"  = "+resultNum);
		       }    
           } catch (NumberFormatException e) { 
               resultText.setText(resultNum);    

           } 
	   }else if(label.equals(SPECIAL_FUNTION[2])){//计算方差
		   resultNum = df.format(Variance());
			 //输出计算结果
	   	   if((expression.charAt(expression.length()-1))!=')'){
	   	       resultText.setText(resultText.getText()+")  = "+resultNum);//用户没有按")"，输出时使其自动补上
	       }else{
	           resultText.setText(resultText.getText()+"  = "+resultNum);
	       }
		   
	   }else if(label.equals(SPECIAL_FUNTION[3])){//计算平均值
		   resultNum = df.format(Ave());
			 //输出计算结果
	   	   if((expression.charAt(expression.length()-1))!=')'){
	   	       resultText.setText(resultText.getText()+")  = "+resultNum);//用户没有按")"，输出时使其自动补上
	       }else{
	           resultText.setText(resultText.getText()+"  = "+resultNum);
	       }

	   }else if(label.equals(SPECIAL_FUNTION[4])){//计算二项分布
		   resultNum = dfs.format(BiDistribution());
			 //输出计算结果
	   	   if((expression.charAt(expression.length()-1))!=')'){
	   	       resultText.setText(resultText.getText()+")  = "+resultNum);//用户没有按")"，输出时使其自动补上
	       }else{
	           resultText.setText(resultText.getText()+"  = "+resultNum);
	       }
	   }else if(label.equals(SPECIAL_FUNTION[5])){//计算泊松分布
		   //结果保留三位小数
		   resultNum = dfp.format(PoDistribution());
           //输出计算结果
	   	   if((expression.charAt(expression.length()-1))!=')'){
	   	       resultText.setText(resultText.getText()+")  = "+resultNum);//用户没有按")"，输出时使其自动补上
	       }else{
	           resultText.setText(resultText.getText()+"  = "+resultNum);
	       }
	   }else if(label.equals(SPECIAL_FUNTION[6])){//计算标准正太分布
		   double result;
		   result=StandardDistribution();
		   if(result==-2){
			   resultText.setText("积分上限太小");
		   }else if(result==-1){
			   resultText.setText("积分上限太大");
		   }else{
			   resultNum=dfs.format(result);   
			 //输出计算结果
		   	   if((expression.charAt(expression.length()-1))!=')'){
		   	       resultText.setText(resultText.getText()+")  = "+resultNum);//用户没有按")"，输出时使其自动补上
		       }else{
		           resultText.setText(resultText.getText()+"  = "+resultNum);
		       }		   }
	   }
	   
       
   } 

   /** 
    * 计算组合公式 函数
    * @param n,m
    * @return 
    */  
  
   private String Combination(int n,int m){
	   
	   if(n<=0||m<=0){
	   		return "n,m需为正数";
	    }else{
		    if(m>n){
		  		return "n必须大于或等于m";
		  	}else{
			   	int c = (int)(factorial(n)/(factorial(m)*factorial(n-m)));
			   	return (String.valueOf(c));
			}
	    }
  	
  }

   /** 
    * 计算排列公式 函数
    * @param n,m
    * @return 
    */  
   private String Arrangement(int n,int m){
  		
    	if(n<=0||m<=0){
	   		return "n,m需为正数";
        }else{
	   	    if(m>n){
		  		return "n必须大于或等于m";
	   	    }else{
	    	  int a =(int) (factorial(n)/factorial(n-m));
	    	  return (String.valueOf(a));
	        }
     }
   }
   /*
    * 计算平均数
    * @return
    */
   private double Ave(){
  	 double result=0,opNum = 0.0;
  	 int numStackLen = numStack.size();
  	 Stack<String> numstack  = (Stack<String>)numStack.clone();
  	 for(int i=0;i<numStackLen;i++){
   		 opNum =Double.valueOf(numstack.pop()) ;
  		 result += opNum;
  	 }
  	 result = result/numStackLen;
  	 return result;
  	
   }
   /*
    * 计算方差
    * @param factorial
    * @return
    */
   private double Variance(){
	   
   	 double result=0,opNum = 0.0;
   	 int    numStackLen = numStack.size();
  	 Stack<String> numstack  = (Stack<String>)numStack.clone();
   	 double average=Ave();
   	
   	 for(int i=0;i<numStackLen;i++){
   		 opNum =Double.valueOf(numstack.pop());
   		result += (opNum-average)*(opNum-average); 
   	 }
   	result =result/numStackLen;
   	return result;
   	
   }
   /**
    * 求二项分布的值
    * @param n,p,k   实验n次，每次发生概率为p，发生次数为K
    */
   private double BiDistribution(){
	   int n,k;
	   double P;
	   Stack<String> numstack  = (Stack<String>)numStack.clone();
	   k=Integer.valueOf(numstack.pop());
	   P=Double.valueOf(numstack.pop());
	   n=Integer.valueOf(numstack.pop());
	   System.out.println(n);
	   P=(factorial(n)/(factorial(k)*factorial(n-k)))*(Math.pow(P,k))*(Math.pow(1-P,(n-k)));
	   return P;
	
   }
   /**
    * 求泊松分布的值
    * @param λ,k   每次发生概率为λ，发生次数为K
    */
   private double PoDistribution(){
	   int k;
	   float λ;
	   double p,sum=0;
	   Stack<String> numstack  = (Stack<String>)numStack.clone();
	   k=Integer.valueOf(numstack.pop());
	   λ=Float.valueOf(numstack.pop());
	   for(int i=0;i<=k;i++){
	     p=(Math.pow(λ,i)*Math.pow(Math.E,-λ))/factorial(i);
	     sum+=p;
	   }
	   return sum;
	
   }

   /**
    * 标准正太分布函数
    * 根据分割积分法来求得积分值
    * -3.89～3.89区间外的积分面积 小于 0.0001，
    * 所以确定有效的积分区间为-3.89～3.89
    * 在实现分割的时候精度定为0.0001，得到的结果和查表得到的结果误差在-0.0002～+0.0002之间（已经检验）
    * 
    * @param u      积分上限
    */
   private  double StandardDistribution(){
	   float u;
	   Stack<String> numstack  = (Stack<String>)numStack.clone();
	   u=Float.valueOf(numstack.pop());
       float ret  = 0;
       if(u < -3.89){
    	   return(-2);
       }
       else if(u > 3.89){
    	   return(-1);
       }
       float temp = -3.89f;
       while(temp <= u){
           ret += 0.0001f * fx(temp);
           temp += 0.0001f;
       }
       return(ret);
   }

   /**
    * 求被积函数的函数值    (1/(2 * PI)^(0.5))e^(-t^2/2)
    * @param x      变量x
    * @return       函数值
    */
   private static float fx(float x){
       float ret = 0;
       double a = 1.0 / Math.sqrt(Math.PI * 2);
       a  = a * Math.pow(Math.E, -0.5 * Math.pow(x, 2));
       ret = (float) a;
       return ret;
   }
   /***********************************************************************************************/
   /***********************************************************************************************/
   
   /*健康指数显示前的数据处理*/
   private  String HandelNum(){
       try {  
    	   
    	   weight = Double.valueOf(weightText.getText()); //从输入框中获得体重
    	   height = Double.valueOf(heightText.getText());//从输入框中获得身高
		   age = Integer.valueOf(ageText.getText());
       } catch (NumberFormatException e) { 
	   		return("请先输入身高、体重、年龄");
       } 
       //判断性别
	   if(Sex==""){
		   return("请先输入年龄");
	   }else if(Sex=="男"){//如果是男生，则置为1
		   sex = 1;
		   return("输入完成");
       }else{//女生置0
    	   sex = 0;
    	   return("输入完成");
       }

   }
   /*体重类型提醒*/
   private  String BMIInform(){
	   //计算BMI 的值
	   double bmi=BMI();
	   	//分等级输出
	   	if(bmi<18.5){
	   		return("您体重过低");
	   	}else if(bmi>=18.5&&bmi<23.9){
	   		return("您体重在正常范围");
	   	}else {
	   		if(bmi>=23.9&&bmi<27.9){
	   			return("您目前体重处于肥胖前期,注意控制哦");
	   	    }else if(bmi>=27.9&&bmi<29.9){
	   	    	return("您目前体重处于I度肥胖,注意加强运动哦");
	   	    }else if(bmi>=29.9&&bmi<39.9){
	   	    	return("您目前体重处于II度肥胖,注意加强运动哦");
	   	    }else{
	   	    	return("您目前体重处于,III度肥胖,注意加强运动哦");
	   	    }
	   }
	   
   }
   
   /*计算理想体重（DBW）:desirable body weight;*/
   private  double DBW(){
	   double bmi=22;
       return(Double.valueOf(dft.format(bmi*height*height)));
	   
   }
   /*计算BMI*/
   private double BMI(){
	   double bmi;
       bmi = weight/(height*height);
       return(Double.valueOf(dft.format(bmi)));
   }
   /*计算疾病发病率*/
   private String DiseaseRate(){
	   double bmi=BMI();
		//分等级输出
	   	if(bmi<18.5){
	   		return("低(但其他疾病危险性增加)");
	   	}else if(bmi>=18.5&&bmi<23.9){
	   		return("平均水平");
	   	}else {
	   		if(bmi>=23.9&&bmi<27.9){
	   			return("增加");
	   	    }else if(bmi>=27.9&&bmi<29.9){
	   	    	return("中度增加");
	   	    }else if(bmi>=29.9&&bmi<39.9){
	   	    	return("严重增加");
	   	    }else{
	   	    	return("非常严重增加");
	   	    }
	   }
	   
   }
   /*计算身体脂肪率*/
   private double BFR(){
	   double bmi,bfr;
	   bmi=BMI();
       bfr = 1.2*bmi+0.23*age-5.4-10.8*sex; 
       return(Double.valueOf(dft.format(bfr)));
   }
   
   /*基础代谢率（BMR）:Basal metabolic rate*/
   private double BMR(){
	   double bmr;
   	   //计算基础代谢率
       if(sex==1){
    	   bmr = 661+9.6*weight+1.72*height-4.7*age ;  
       }else{
    	   bmr = 67+13.73*weight+5*height-6.9*age ;  
       }
       return(Double.valueOf(dft.format(bmr)));
   }
   
   //每日所需卡路里
   private double dailyCalorie(){
	   int id;
	   double bmr,dailyCalorie,index;
	   bmr=BMR();
	   id=comboBox.getSelectedIndex();
	   if(id==0){
		   index=1.15;
	   }else if(id==1){
		   index=1.3;
	   }else if(id==2){
		   index=1.4;
	   }else if(id==3){
		   index=1.6;
	   }else{
		   index=1.8;
	   }
	   dailyCalorie = bmr*index;
	   return(Double.valueOf(dft.format(dailyCalorie)));
   }
   /*增重所需的能量*/
   private String PutOn(){
	   double bottom,top;
	   bottom=dailyCalorie()+500;
	   top=dailyCalorie()+1000;
	   return(dft.format(bottom)+"~"+dft.format(top));
   }
   /*增重所需的能量*/
   private String LoseWeight(){
	   double bottom,top;
	   bottom=dailyCalorie()-1000;
	   top=dailyCalorie()-500;
	   return(dft.format(bottom)+"~"+dft.format(top));
   }
   
   /*计算身体脂肪率*/
   private String BFRInform(){
	   double bfr;
	   bfr=BFR();
       if(sex==1){//男生
		   if(bfr<10){
			   return("过少");
		   }else if(bfr>=10&&bfr<14){
			   return("仅仅能满足必要需求");
		   }else if(bfr>=14&&bfr<21){
			   return("达到了运动员的要求");
		   }else if(bfr>=21&&bfr<25){
			   return("正好合适,请继续保持 ");
		   }else if(bfr>=25&&bfr<=31){
			   return("有点偏高，不过尚可接受");
		   }else{
			   return("过高，加强运动吧");
		   }
	   }else{
		   if(bfr<2){
			   return("过少");
		   }else if(bfr>=2&&bfr<6){
			   return("仅仅能满足必要需求");
		   }else if(bfr>=6&&bfr<14){
			   return("达到了运动员的要求");
		   }else if(bfr>=14&&bfr<18){
			   return("正好合适,请继续保持");
		   }else if(bfr>=18&&bfr<=25){
			   return("有点偏高，不过尚可接受");
		   }else{
			   return("过高，加强运动吧");
		   }
	   }
	   
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
   
   /**
    * 从输入表达式中获取数字
    * 
    */
    private void getNumToken(String expression){
    	char[] arr  = expression.toCharArray();
    	int arrLen = arr.length;
        int currentIndex  = 0;//当前字符的位置
        int count = 0;//上次逗号到本次逗号的字符的长度,便于获得之间的数值
        
        for(int i=0 ;i<arrLen;i++) {
            if("0123456789.".indexOf(arr[i])>= 0) {
                count++;

            } else {//如果当前字符是非数字的字符
            	if(count>0){
                    numStack.push(new String(arr,currentIndex,count));//取两个字符之间的数字
            	}
                count = 0;
                currentIndex = i+1;
            }
        }
        if(count > 1 || (count == 1 && "0123456789.".indexOf(arr[currentIndex])>= 0)) {//最后一个字符不是括号或者其他运算符的则加入数字栈中
            numStack.push(new String(arr,currentIndex,count));
        } 
    }
   
    /**
     * 从输入表达式中获取数字
     * 
     */
    private void handleLabel(JLabel label){
    	 label.setFont(new Font("微软雅黑", Font.PLAIN, 14));
         label.setForeground(Color.WHITE);
    }

  /********主类，创建一个Calculator对象****************************************************************************************************/
    public static void main(String args[]) {  
        Calculator calculator = new Calculator(); 
        calculator.setVisible(true);  
        calculator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     
   } 
}

//实现文本框监听，当点击文本框文本框清空初始化内容
class MyFocusListener implements FocusListener { 
	String info; 
    JTextField jtf; 
    public MyFocusListener(String info, JTextField jtf) { 
        this.info = info; 
        this.jtf = jtf; 
    } 
    public void focusGained(FocusEvent e){//获得焦点的时候,清空提示文字 
        String temp = jtf.getText(); 
        if(temp.equals(info)){ 
            jtf.setText(""); 
        } 
    } 
    public void focusLost(FocusEvent e) {//失去焦点的时候,判断如果为空,就显示提示文字 
        String temp = jtf.getText(); 
        if(temp.equals("")){ 
            jtf.setText(info); 
        } 
    }
}

//实现button圆角以及光标移入移除时候的效果
class RaButton extends JButton {
	private static final long serialVersionUID = 39082560987930759L;
    public  final Color BUTTON_COLOR1 = new Color(59,71,86);
    public  final Color BUTTON_COLOR2 = Color.black;
  
    public  final Color BUTTON_FOREGROUND_COLOR = Color.WHITE;
    private boolean hover;

    public RaButton(String name) {
       this.setText(name);
       setFont(new Font("system", Font.PLAIN, 20));
       setBorderPainted(false);
       setForeground(BUTTON_FOREGROUND_COLOR);//前景颜色，字体颜色
       setFocusPainted(false);
       setContentAreaFilled(false);//透明
       addMouseListener(new MouseAdapter() {//移入移除效果
          //  @Override
          public void mouseEntered(MouseEvent e) {
              setForeground(BUTTON_FOREGROUND_COLOR);
              hover = true;
              repaint();
          }

          @Override
          public void mouseExited(MouseEvent e) {
              setForeground(BUTTON_FOREGROUND_COLOR);
              hover = false;
              repaint();
          }
       });
    }

    //@Override
    protected void paintComponent(Graphics g) {
       Graphics2D g2d = (Graphics2D) g.create();
       int h = getHeight();
       int w = getWidth();
       float tran = 1F;
       if (!hover) {
          tran = 0.3F;
       }

       g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
              RenderingHints.VALUE_ANTIALIAS_ON);
       GradientPaint p1;
       GradientPaint p2;
       if (getModel().isPressed()) {
          p1 = new GradientPaint(0, 0, new Color(0, 0, 0), 0, h - 1,
                  new Color(100, 100, 10));
          p2 = new GradientPaint(0, 1, new Color(0, 0, 0, 10), 0, h - 3,
                  new Color(255, 255, 255, 10));
       } else {
          p1 = new GradientPaint(0, 0, new Color(100, 100, 10), 0, h - 1,
                  new Color(0, 0, 0));
          p2 = new GradientPaint(0, 1, new Color(255, 255, 255, 10), 0,
                  h - 3, new Color(0, 0, 0, 10));
       }
       g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
              tran));
       RoundRectangle2D.Float r2d = new RoundRectangle2D.Float(0, 0, w - 1,
              h - 1, 20, 20);
       Shape clip = g2d.getClip();
       g2d.clip(r2d);
       //实现颜色渐变
       GradientPaint gp = new GradientPaint(0.0F, 0.0F, BUTTON_COLOR1, 0.0F, 
              h, BUTTON_COLOR2, true);
       g2d.setPaint(gp);
       g2d.fillRect(0, 0, w, h);
       g2d.setClip(clip);
       g2d.setPaint(p1);
       g2d.drawRoundRect(0, 0, w - 1, h - 1, 20, 20);
       g2d.setPaint(p2);
       g2d.drawRoundRect(1, 1, w - 3, h - 3, 18, 18);
       g2d.dispose();
       super.paintComponent(g);
    }
}

//实现sexButton的圆角，但去掉移入移出颜色变化功能，继承与上一个RaButton
class sexButton extends JButton {
	  private static final long serialVersionUID = 39082560987930759L;
	  public  final Color BUTTON_COLOR1 = new Color(59,71,86);
	  public  final Color BUTTON_COLOR2 = Color.black;
	  
	  public  final Color BUTTON_FOREGROUND_COLOR = Color.WHITE;
	  private boolean hover;

      public sexButton(String name) {
         this.setText(name);
         setFont(new Font("system", Font.PLAIN, 20));
         setBorderPainted(false);
         setForeground(BUTTON_FOREGROUND_COLOR);//前景颜色，字体颜色
         setFocusPainted(false);
         setContentAreaFilled(false);//透明

      }
      //@Override
      protected void paintComponent(Graphics g) {
         Graphics2D g2d = (Graphics2D) g.create();
         int h = getHeight();
         int w = getWidth();
         float tran = 1F;
         if (!hover) {
          tran = 0.3F;
         }

      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
              RenderingHints.VALUE_ANTIALIAS_ON);
      GradientPaint p1;
      GradientPaint p2;
      if (getModel().isPressed()) {
          p1 = new GradientPaint(0, 0, new Color(0, 0, 0), 0, h - 1,
                  new Color(100, 100, 10));
          p2 = new GradientPaint(0, 1, new Color(0, 0, 0, 10), 0, h - 3,
                  new Color(255, 255, 255, 10));
      } else {
          p1 = new GradientPaint(0, 0, new Color(100, 100, 10), 0, h - 1,
                  new Color(0, 0, 0));
          p2 = new GradientPaint(0, 1, new Color(255, 255, 255, 10), 0,
                  h - 3, new Color(0, 0, 0, 10));
      }
      g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
              tran));
      RoundRectangle2D.Float r2d = new RoundRectangle2D.Float(0, 0, w - 1,
              h - 1, 20, 20);
      Shape clip = g2d.getClip();
      g2d.clip(r2d);
      //实现颜色渐变
      GradientPaint gp = new GradientPaint(0.0F, 0.0F, BUTTON_COLOR1, 0.0F, 
              h, BUTTON_COLOR2, true);
      g2d.setPaint(gp);
      g2d.fillRect(0, 0, w, h);
      g2d.setClip(clip);
      g2d.setPaint(p1);
      g2d.drawRoundRect(0, 0, w - 1, h - 1, 20, 20);
      g2d.setPaint(p2);
      g2d.drawRoundRect(1, 1, w - 3, h - 3, 18, 18);
      g2d.dispose();
      super.paintComponent(g);
  }
}
 
 
