<<<<<<< HEAD
import java.awt.Color;  
import java.awt.GridLayout;  
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;  
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import javax.swing.JButton;  
import javax.swing.JFrame;  
import javax.swing.JPanel;  
import javax.swing.JTextField; 
import java.math.BigDecimal; //��������� 
import java.util.Collections;
import java.util.Stack;
import java.awt.*;
import java.text.DecimalFormat;
import java.lang.NumberFormatException;
import java.lang.String;
import java.awt.event.FocusListener;

 
/** 
 * һ����������֧�ּ򵥵ļ���
 */  
public class Calculator2 extends JFrame implements ActionListener {  
	/***************************************************************************************************************/
	/********��������ذ������Ƽ���ť����*******/
	
	/*������ģʽ���л���������ʾ*/
    private final String[] MODEL = {"����","��ѧ��","ͳ����","��������","����ת��"};
    /*�����������ϵļ�����ʾ���� */  
    private final String[] KEYS = {"(",")", "C","��","7", "8", "9", "+" , "4", "5", "6",  
            "-", "1", "2", "3", "*", "0", ".", "=", "," }; 
    /*��ѧ�ͼ������ϵĺ���ͼ����ʾ����*/ 
    private final String[] FUNCTION = { "1/x", "��2", "x^y", "x!", "sin","cos","tan","��" ,
    		"In","lg","log","%","e^x","e","��",","};
    /*ͳ���ͼ����������⹦��ͼ����ʾ����*/
    private final String[] SPECIAL_FUNTION = { "C(N,M)", "A(N,M)","D", "Ave","����ֲ�",
    		"���ɷֲ�","��׼��̫�ֲ�","��ɢϵ��"};
    /*��������������ϵļ�����ʾ����*/
    private final String[] SUPPLEMENT={"����ָ��","������","����"};
    /*����ת��ģ���ϵļ�����ʾ����*/
    private final String[] REFRESH={"ˢ��"};
 
    /*���������ť����ĳ��ȣ��������Ǵ�����Ӧ������*/
    int modelLen=MODEL.length;//���ڴ��MODEL�����а�ť����
    int keysLen=KEYS.length;//���ڴ��KEYS�����еİ�ť����
    int functionLen=FUNCTION.length;//���ڴ��FUNCTION�����еİ�ť����
    int special_functionLen=SPECIAL_FUNTION.length;//���ڴ��SPECIAL_FUNTION �����еİ�ť����
    int supplementLen=SUPPLEMENT.length;//���ڴ�Ž�������ģ���а�ť������
    int refreshLen=REFRESH.length;//���ڴ�Ż���ת����ť������
    
    /*ģʽ�л��ϵļ�����*/
    private JButton models[] = new JButton[keysLen];
    /*�������ı��� */  
    private JTextField resultText = new JTextField("");//������Ĭ�ϵ�0
    /*��������ı��� */  
    private JTextField heightText = new JTextField("������������");
    /*���������ı��� */  
    private JTextField weightText = new JTextField("�������������");
    /*��Χ�����ı��� */  
    private JTextField waistText = new JTextField("�����������Χ");
    /*���������ı��� */  
    private JTextField ageText = new JTextField("�������������");
    /*�򵥼������ϼ��İ�ť */  
    private JButton keys[] = new JButton[keysLen];  
    /*�������Ϲ���ͼ����ʾ����*/  
    private JButton function[] = new JButton[functionLen+keysLen]; 
    /*�������ϳ��ú����İ�ť*/
    private JButton special_function[] = new JButton[special_functionLen+keysLen];
    /*����������չģ���ϵİ�ť*/
    private JButton supplements[]=new JButton[supplementLen];
    /*����ת���ϵİ�ť*/
    private JButton refresh[] = new JButton[refreshLen];
    /*****************************************************************************************************/
    /****/
    
    /*Ϊ��ʵ�����ȼ�������*/
    private Stack<String> postfixStack  = new Stack<String>();//��׺ʽջ
    private Stack<Character> opStack  = new Stack<Character>();//�����ջ
    private int [] operatPriority  = new int[] {0,3,2,1,-1,1,0,2};//���������ASCII��-40����������������ȼ� 
    
    //���ü���panel,Panel1��ʾ���ͼ������Ľ��棬panel2��ʾ��ѧ�ͼ������Ľ��棬panel3��ʾͳ���ͼ������Ľ��棬panel4��ʾ��չ�ͼ������Ľ���
    JPanel panel,panel1,panel2,panel3,panel4,panel5;
    //���õ�ѡ���Է���ѡ����Ů
    Checkbox cRadio1,cRadio2;
    CheckboxGroup c;
    //���ü���String���͵ı������ڽ���û�оۼ�����Ӧ�ı�����ʱ��Ĭ����ʾ
    String info1="������������(��)";
    String info2="�������������(ǧ��)";
    String info3="�����������Χ";
    String info4="�������������(��)";
    
    /*Ϊ��ʵ�ֿ�ѧ���㼰���������������*/
    private double  baseNum = 1;    //�������x^yʱx��ֵ
    private String   expression = ""; //��ſ�ѧ����ʱ��ȥ������ı��ʽ�������sin��5+12*(3+5)/7��ʱ��expression=5+12*(3+5)/7
    private boolean isFunction = false;   //�Ƿ��ǿ�ѧ����
    private boolean isSpecialFunction = false;   //�Ƿ���ͳ�Ƽ���
    private String   opreator = ""; //��ſ�ѧ����ʱ�Ĳ����� 
    private Stack<String> numStack  = new Stack<String>();//�������Ķ��������
    
   /** 
    ************ ���캯�� *************************************************************************
    *********************/  
    public Calculator2() {  
        super();  
        // ��ʼ��������  
        init();  
        // ���ü������ı�����ɫ  
        this.setBackground(new Color(54,54,54   ));  
        this.setTitle("������");  
        // ����Ļ(500, 300)���괦��ʾ������  
        this.setLocation(500, 300);  
        // �����޸ļ������Ĵ�С  
        this.setResizable(true);             //ע�⣺�����С���Ըı� 
        // ʹ�������и������С����  
        this.setSize(600, 300); 
    }
  
    /** 
     *******��ʼ�������� ***************************************************************************************** 
     *�������Ļ�������
     */ 
    
    private void init() {   	
        // �ı����е����ݲ����Ҷ��뷽ʽ  
        resultText.setHorizontalAlignment(JTextField.RIGHT);  
        // �����޸Ľ���ı���  
        resultText.setEditable(true);            //����ı���ʵ��
        // �����ı��򱳾���ɫΪ��ɫ  
        resultText.setBackground(Color.white);
        //�����ı���Ŀ��
        resultText.setSize(100,100);
        
        //ģ���л����ĸ���ť����һ����������modelPanel������
        JPanel modelPanel = new JPanel();
        //����modelPanel����İ�ť����������,ˮƽ����
        FlowLayout fl = new FlowLayout(); 
        fl.setAlignment(FlowLayout.LEFT); 
        modelPanel.setLayout(fl);
        for (int i = 0; i < modelLen; i++) {  
            models[i] = new JButton(MODEL[i]);  
            //ȥ��modelsPanel�����button�ı߽���
            models[i].setBorderPainted(false);
            //ȥ��button���ʱ�����ϳ��ֵı߽��
            models[i].setFocusPainted(false);
            //����button��������Ĵ�С
            models[i].setFont(new Font("΢���ź�", Font.PLAIN, 14));
            models[i].setForeground(new Color(255,255,255));
            modelPanel.add(models[i]); 
            //����modelPanle��button�ı�����ɫ
            modelPanel.setBackground(new Color(54,54,54   ));
            models[i].setBackground(new Color(54,54,54   ));  
        }  
        
       // ����һ��������ı���  
       JPanel topPanel = new JPanel();  
       topPanel.setLayout(new BorderLayout(0,0));  
       topPanel.add("Center", resultText); 
      
       //����һ������Panel�����ģʽ�л�����ʾ���
       JPanel top = new JPanel();  
       top.setLayout(new GridLayout(2,1,0,0));  
       top.add(modelPanel); 
       top.add("South",topPanel);
      
       // ��ʼ�����������������֡����㡢���š�����ȼ��İ�ť����������һ������calkeysPanel������  
       JPanel calckeysPanel = new JPanel();  
       // �����񲼾�����4�У�4�е���������֮���ˮƽ������Ϊ3�����أ���ֱ������Ϊ3������  
       calckeysPanel.setLayout(new GridLayout(5, 4, 0, 0));  
       for (int i = 0; i < keysLen; i++) {  
            keys[i] = new JButton(KEYS[i]);  
            calckeysPanel.add(keys[i]);  
            //ȥ��button���ʱ�����ϳ��ֵı߽��
            keys[i].setFocusPainted(false);
            keys[i].setBackground(new Color(54,54,54   ));  
            //����button���������С
            keys[i].setFont(new Font("΢���ź�", Font.PLAIN, 16));
            keys[i].setForeground(new Color(255,255,255));
        }     
       
        // �½�һ����Ļ��壬��calckeys������ڸû�����  
        panel1 = new JPanel();  
        // ������ñ߽粼�ֹ����������������֮���ˮƽ�ʹ�ֱ�����ϼ����Ϊ0����  
        panel1.setLayout(new BorderLayout(0,0));   
        panel1.setBackground(new Color(54,54,54   ));
        panel1.add("Center", calckeysPanel);
        
        // ��ʼ����ѧ�������������ܼ����� �����������ܺͿ�ѧ���������ܼ�����һ��������  
        JPanel functionsPanel = new JPanel();  
        // �����񲼾ֹ�������5�У�7�е���������֮���ˮƽ������Ϊ0�����أ���ֱ������Ϊ0������  
        functionsPanel.setLayout(new GridLayout(9, 4,0,0));
        //����һЩ��ѧ����İ�ť
        for (int i = 0; i < functionLen; i++) {  
            function[i] = new JButton(FUNCTION[i]);   
            //ȥ��button���ʱ�����ϳ��ֵı߽��
            function[i].setFocusPainted(false);
            //����button���������С
            function[i].setFont(new Font("΢���ź�", Font.PLAIN, 16));
            function[i].setForeground(new Color(255,255,255));  
            function[i].setBackground(new Color(54,54,54   ));  
            functionsPanel.add(function[i]); 
        } 
        //������������Լ����ְ�ť
        for (int i = functionLen; i < (keysLen+functionLen); i++) {  
        	function[i] = new JButton(KEYS[i-functionLen]); 
        	//ȥ��button���ʱ�����ϳ��ֵı߽��
            function[i].setFocusPainted(false);
            //����button���������С
            function[i].setFont(new Font("΢���ź�", Font.PLAIN, 16));
            function[i].setForeground(new Color(255,255,255));  
            function[i].setBackground(new Color(54,54,54   ));  
        	functionsPanel.add(function[i]);  
        }  
 
        // �½�һ����Ļ��壬��function������ڸû����ڣ�ʵ�ֿ�ѧ�������Ľ���
        panel2=new JPanel();
        panel2.setLayout(new BorderLayout(0,0));
        panel2.setBackground(new Color(54,54,54   ));
        panel2.add("Center",functionsPanel);
        
        // ��ʼ�����������⺯�����ܼ����ú�ɫ��ʾ���������������ܼ�����һ��������  
        JPanel special_functionsPanel = new JPanel();  
        // �����񲼾ֹ�������6�У�4�е���������֮���ˮƽ������Ϊ0�����أ���ֱ������Ϊ0������  
        special_functionsPanel.setLayout(new GridLayout(7, 4, 0, 0)); 
        //����ͳ������Ĺ��ܰ�ť
        for (int i = 0; i < special_functionLen; i++) {  
        	special_function[i] = new JButton(SPECIAL_FUNTION[i]); 
        	//ȥ��button���ʱ�����ϳ��ֵı߽��
            special_function[i].setFocusPainted(false);
            //����button���������С
            special_function[i].setFont(new Font("΢���ź�", Font.PLAIN, 16));
            special_function[i].setForeground(new Color(255,255,255));
            special_function[i].setBackground(new Color(54,54,54   ));  
        	special_functionsPanel.add(special_function[i]);   
        }  
        //������������Լ����ְ�ť
        for (int i = special_functionLen; i < (keysLen+special_functionLen); i++) {  
        	special_function[i] = new JButton(KEYS[i-special_functionLen]); 
        	//ȥ��button���ʱ�����ϳ��ֵı߽��
            special_function[i].setFocusPainted(false);
            //����button���������С
            special_function[i].setFont(new Font("΢���ź�", Font.PLAIN, 16));
            special_function[i].setForeground(new Color(255,255,255));  
            special_function[i].setBackground(new Color(54,54,54   ));  
        	special_functionsPanel.add(special_function[i]);  
        }  
        //�½�һ����Ļ��壬��calckeys�Լ�special_function������ڸû����ڣ�ʵ��ͳ�Ƽ������Ľ���
        panel3=new JPanel();
        panel3.setLayout(new BorderLayout(0,0));
        panel3.setBackground(new Color(54,54,54   ));
        panel3.add("Center",special_functionsPanel);
        
        //��ʼ����������չģʽ������չ���ܵĺ�������һ��panel��
        JPanel supplementPanel=new JPanel();
        supplementPanel.setLayout(new GridLayout());
        for (int i = 0; i < supplementLen; i++) {  
        	supplements[i] = new JButton(SUPPLEMENT[i]); 
        	//ȥ��button���ʱ�����ϳ��ֵı߽��
            supplements[i].setFocusPainted(false);
            //����button���������С
            supplements[i].setFont(new Font("΢���ź�", Font.PLAIN, 16));
            supplements[i].setBackground(new Color(54,54,54   ));  
        	supplementPanel.add(supplements[i]);  
            supplements[i].setForeground(new Color(255,255,255));  
        }  
        
        //���õ�ѡ�� �������Ա��ѡ�� 
        c=new CheckboxGroup();
        cRadio1=new Checkbox("����",c,false);
        cRadio2=new Checkbox("Ů��",c,true);
        //�½�һ�����壬�����񲼾֣��������У�������������������
        JPanel panel_4=new JPanel();
        panel_4.setLayout(new GridLayout(3,2));
        panel_4.add(heightText);
        panel_4.add(weightText);
        panel_4.add(waistText);
        panel_4.add(ageText);
        panel_4.add(cRadio1);
        panel_4.add(cRadio2);
        //�½�һ����Ļ��壬��supplement������ڸû����ڣ�ʵ����չ�������Ľ���
        panel4=new JPanel();
        panel4.setLayout(new BorderLayout(3,3));
        panel4.setBackground(new Color(54,54,54   ));
        panel4.add("North", panel_4);
        panel4.add("Center",supplementPanel);
        
      //��ʼ������������ת��ģʽ���������һ��panel��
        JPanel refreshPanel=new JPanel();
        refreshPanel.setLayout(new GridLayout());
        for (int i = 0; i < refreshLen; i++) {  
            refresh[i] = new JButton(REFRESH[i]);  
            //ȥ��modelsPanel�����button�ı߽���
            refresh[i].setBorderPainted(false);
            //ȥ��button���ʱ�����ϳ��ֵı߽��
            refresh[i].setFocusPainted(false);
            //����button��������Ĵ�С
            refresh[i].setFont(new Font("΢���ź�", Font.PLAIN, 14));
            refresh[i].setForeground(new Color(255,255,255));
            refreshPanel.add(refresh[i]); 
            //����modelPanle��button�ı�����ɫ
            refreshPanel.setBackground(new Color(54,54,54   ));
            refresh[i].setBackground(new Color(54,54,54   ));  
        }  
        //�½�һ���󻭰�Panel5,��Ż���ת��
        panel5=new JPanel();
        panel5.setLayout(new BorderLayout());
        panel5.add("Center",refreshPanel);
        
        /***************Ϊ����ť����¼�������***********************************/   
        //��ʹ��ͬһ���¼����������������󡣱������������implements ActionListener
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
        for (int i = 0; i < refreshLen; i++) {  
        	refresh[i].addActionListener(this);  
        }
        
        /*Ϊ�ı����ʼ��*/
        heightText.setText(info1);
        weightText.setText(info2);
        waistText.setText(info3);
        ageText.setText(info4);
        /*Ϊ�ı�����ӽ����¼���Ӧ��*/
        heightText.addFocusListener(new MyFocusListener(info1, heightText));//��ӽ����¼���ӳ 
        weightText.addFocusListener(new MyFocusListener(info2, weightText));
        waistText.addFocusListener(new MyFocusListener(info3, waistText));//��ӽ����¼���ӳ 
        ageText.addFocusListener(new MyFocusListener(info4, ageText));
    
        //�½�һ������panel,����������岼��
        panel=new JPanel(new BorderLayout(0,0));
        panel.setBackground(new Color(54,54,54));  
        add(panel);
        panel.add("North",top);
        panel.add("Center",panel1);  
    }    
    

    /** 
     *********�����¼� *****************************************************************************************************
     */  
    public void actionPerformed(ActionEvent e) {  
        // �н����ռ�����  
    	double result = 0.0;
    	// ��ȡ�¼�Դ�ı�ǩ  
        String label = e.getActionCommand();
        //����ģʽѡ�񣺼��ͣ���ѧ�ͣ�ͳ���ͣ���չ�ͣ�
        if(label.equals(MODEL[0])){//����
        	panel.add("Center",panel1);
        	panel1.setVisible(true);
        	panel2.setVisible(false);
        	panel3.setVisible(false);
        	panel4.setVisible(false);
        	panel5.setVisible(false);
        	handleC();     
        }else if(label.equals(MODEL[1])){//��ѧ��
        	panel.add("Center",panel2);
        	panel1.setVisible(false);
        	panel2.setVisible(true);
        	panel3.setVisible(false);
        	panel4.setVisible(false);
        	panel5.setVisible(false);
        	handleC();     
        }else if(label.equals(MODEL[2])){//ͳ����
        	panel.add("Center",panel3);
        	panel1.setVisible(false);
        	panel2.setVisible(false);
        	panel3.setVisible(true);
        	panel4.setVisible(false);
        	panel5.setVisible(false);
        	handleC();     
        }else if(label.equals(MODEL[3])){//��������
        	panel.add("Center",panel4);
        	panel1.setVisible(false);
        	panel2.setVisible(false);
        	panel3.setVisible(false);
        	panel4.setVisible(true);
        	panel5.setVisible(false);
        	handleC();     
        }else if(label.equals(MODEL[4])){//����ת��
        	panel.add("Center",panel5);
        	panel1.setVisible(false);
        	panel2.setVisible(false);
        	panel3.setVisible(false);
        	panel4.setVisible(false);
        	panel5.setVisible(true);
        	handleC();     
        }else if (label.equals("=")) {  // �û�����"="��  ,���������������
        	if(isFunction){
        		handleFunction(opreator);
        	    isFunction = false;   //�Ƿ��ǿ�ѧ���㣬��sin,cos,lg��
        	    opreator = "";
        	}else if(isSpecialFunction){
        		
        		handleSpecialFunction(opreator);
        	    isSpecialFunction = false;   //�Ƿ���ͳ�Ƽ��㣬��A��N,M��,C(N,M)��
        	    opreator = "";
        	}else{
    	      	result = calculate(resultText.getText());//������
     		    resultText.setText(resultText.getText()+"//ceshi4// "); 

    	      	resultText.setText(String.valueOf(result));//������
        	}

        }else if (label.equals(KEYS[2])) { // �û�����"C"��  ����ʼ��������
        	handleC();                     //������������� 
        	
        }else if (label.equals(KEYS[3])) {  //�û����ˡ������˻ؼ������ı������һ���ַ�ȥ��
            handleBackspace();
        	
        }else if ("0123456789.()+-*/,e��".indexOf(label) >= 0) {  // �û���������Ҫ����ı��ʽ
        	if(isFunction||isSpecialFunction){           //����ǿ�ѧ���㣬��sin������������ı��ʽ����һ���ַ���
        		expression = expression + label;
        	}
        	resultText.setText(resultText.getText()+label);

        }else if(label.equals(FUNCTION[0])||label.equals(FUNCTION[1])||label.equals(FUNCTION[3])||label.equals(FUNCTION[11])){//���������ѧ�ͼ���������
            handleFunction(label);
            
        }else if(label.equals(SPECIAL_FUNTION[0])||label.equals(SPECIAL_FUNTION[1])||    //�����ͳ�Ƽ�������ת��handleSpecialFunction����
        		label.equals(SPECIAL_FUNTION[2])||label.equals(SPECIAL_FUNTION[3])||
        		label.equals(SPECIAL_FUNTION[4])||label.equals(SPECIAL_FUNTION[5])||
        	    label.equals(SPECIAL_FUNTION[6])||label.equals(SPECIAL_FUNTION[7])){
        	if(label.equals(SPECIAL_FUNTION[0])){
     		    resultText.setText("��ֱ�����n,m�����ԡ�,������                   C ( "); 

        	}else if(label.equals(SPECIAL_FUNTION[1])){
     		    resultText.setText("��ֱ�����n,m�����ԡ�,������                   A ( "); 

        	}else{
     		    resultText.setText("��������Ӧ���ݣ����ԡ�,������                   "+label + "( "); 
        	}
        	isSpecialFunction = true;	
        	opreator = label;
        	
        }else if(label.equals("BMI")||label.equals("BFR")||  //�������չ�����������ת��handleSupplement����
        		label.equals("$")||label.equals("����")){
        	handleSupplement(label);
        	
        }else{
        	if(label.equals("e^x")){
        		resultText.setText("e^"+"( ");
        	}else if(label.equals("x^y")){
        		baseNum = getNumberFromText();
        		resultText.setText(resultText.getText()+"^( ");
        	}else if(label.equals("log")){
        		
        		resultText.setText("��ֱ�����������Ա���/����  ���ԡ���������           "+"log"+"(   ");
        	}

        	else{
        		resultText.setText(label+"( ");
        	}
        	
        	isFunction = true;	
        	opreator = label;
        }
    }  
  
    /*����Backspace�������µ��¼� */  
    private void handleBackspace() {  
        String text = resultText.getText();  
        int i = text.length();  
        if (i > 0) {  
            // �˸񣬽��ı����һ���ַ�ȥ��  
            text = text.substring(0, i - 1);  
            expression = expression.substring(0, i - 1);
            if (text.length() == 0) {  
                // ����ı�û�������ݣ����ʼ���������ĸ���ֵ  y  
                resultText.setText("");  
            } else {  
                // ��ʾ�µ��ı�  
                resultText.setText(text);  
            }  
        }  
    } 
    /* ����C�������µ��¼� */  
    private void handleC() {  
        // ��ʼ���������ĸ���ֵ  
        resultText.setText(""); 
        isFunction = false;   
        isSpecialFunction = false;   
	    opreator = "";
        baseNum = 1;    
	    expression = "";
	    numStack.clear();
	    postfixStack.clear();
	    opStack.clear();
	    
        
    }
    /*****************************************************************************************************************/
    /**
     * ���͡��������ո����ı��ʽ����
     * @param expression Ҫ����ı��ʽ����:5+12*(3+5)/7
     * @return
     */
    public double calculate(String expression) {
        Stack<String> resultStack  = new Stack<String>();
        prepare(expression);
        Collections.reverse(postfixStack);//����׺ʽջ��ת
        String firstValue  ,secondValue,currentValue;//�������ĵ�һ��ֵ���ڶ���ֵ�����������
        while(!postfixStack.isEmpty()) {
            currentValue  = postfixStack.pop();
            if(!isOperator(currentValue.charAt(0))) {//����������������������ջ��
                resultStack.push(currentValue);
            } else {//������������Ӳ�����ջ��ȡ����ֵ�͸���ֵһ���������
                 secondValue  = resultStack.pop();
                 firstValue  = resultStack.pop();
                 String tempResult  = calculate(firstValue, secondValue, currentValue.charAt(0));
                 resultStack.push(tempResult);
            }

        }
        return Double.valueOf(resultStack.pop());
    }
    /*
     * ����׼���׶ν����ʽת����Ϊ��׺ʽջ   @param expression
     */ 
    private void prepare(String expression) {
        opStack.push(',');//���������ջ��Ԫ�ض��ţ��˷������ȼ����
        char[] arr  = expression.toCharArray();
        int currentIndex  = 0;//��ǰ�ַ���λ��
        int count = 0;//�ϴ����������������������������ַ��ĳ��ȱ��ڻ��֮�����ֵ
        char currentOp = 'o'  ,peekOp;//��ǰ��������ջ��������
        for(int i=0;i<arr.length;i++) {
            currentOp = arr[i];
            if(isOperator(currentOp)) {//�����ǰ�ַ��������
                if(count > 0) {
                	if(count==1&&arr[currentIndex] =='e'){//����������� e������ת������Ӧ��ֵ���ƽ�ջ��
                		postfixStack.push(String.valueOf(Math.E));	
                	}else if(count==1&&arr[currentIndex]=='��'){//��������Ǧ� ������ת������Ӧ��ֵ���ƽ�ջ��
                		postfixStack.push(String.valueOf(Math.PI));	
                	}else{
                        postfixStack.push(new String(arr,currentIndex,count));//ȡ���������֮�������
                	} 
                }
                peekOp = opStack.peek();
                if(currentOp == ')') {//�����������������ջ�е�Ԫ���Ƴ�����׺ʽջ��ֱ������������
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
        if(count > 1 || (count == 1 && !isOperator(arr[currentIndex]))) {//���һ���ַ��������Ż��������������������׺ʽջ��
        	if(currentOp =='e'){//����������� e������ת������Ӧ��ֵ���ƽ�ջ��
        		postfixStack.push(String.valueOf(Math.E));	
        	}else if(currentOp=='��'){//��������Ǧ� ������ת������Ӧ��ֵ���ƽ�ջ��
        		postfixStack.push(String.valueOf(Math.PI));	
        	}else{
                postfixStack.push(new String(arr,currentIndex,count));//ȡ���������֮�������
        	}        
        } 
        
        while(opStack.peek() != ',') {
            postfixStack.push(String.valueOf( opStack.pop()));//��������ջ�е�ʣ���Ԫ����ӵ���׺ʽջ��
        }
    }
    /*
     * �ж��Ƿ�Ϊ��������
     * @param c
     * @return
     */
    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '(' ||c == ')';
    }
    
    /*
     * ����ASCII��-40���±�ȥ�����������ȼ�
     * @param cur
     * @param peek	
     * @return
     */
    public  boolean compare(char cur,char peek) {// �����peek���ȼ�����cur������true��Ĭ�϶���peek���ȼ�Ҫ��
        boolean result  = false;
        if(operatPriority[(peek)-40] >= operatPriority[(cur) - 40]) {
           result = true;
        }
        return result;
    }
    
    /*
     * ���ո��������������������
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
     *��ѧ�͡������� �����ѧ����������������µ��¼� 
     */  
    private void handleFunction(String key) { 
    	double resultNum = 0;  
        if (key.equals(FUNCTION[0])) {  
        	//����һ�����ĵ���
        	if(getNumberFromText()==0){   //�ж�Ҫ��������Ƿ�Ϊ��
        		resultText.setText("0û�е�����");
        	}else{
        		resultNum = 1/getNumberFromText();
        	}
        	
        }else if(key.equals(FUNCTION[1])){
        	//����һ������ƽ��
        	resultNum = getNumberFromText()*getNumberFromText();
           
        }else if(key.equals(FUNCTION[2])){
        	//����x��y�η� 
        	resultNum = calculate(expression);
        	resultNum = Math.pow(baseNum,resultNum);
        }
        else if(key.equals(FUNCTION[3])){
        	 // �׳�����  
        	try{
        		int num = Integer.parseInt(resultText.getText());
        		resultNum = factorial(num);
        	}catch(NumberFormatException e2){
       		 	resultText.setText("����������");
        	} 
        	
        }else if (key.equals(FUNCTION[4])){
        	// ���Ǻ���sin()
        	resultNum = calculate(expression);
        	resultNum = Math.sin(resultNum);
        	
        }else if (key.equals(FUNCTION[5])) {  
        	 // ���Ǻ���cos() 
        	resultNum = calculate(expression);
        	resultNum = Math.cos(resultNum); 
        	
        }else if (key.equals(FUNCTION[6])){
        	 //���Ǻ���tan()
        	resultNum = calculate(expression);
            resultNum = Math.tan(resultNum);
        	
        }else if (key.equals(FUNCTION[7])) {  
        	// ƽ��������  
        	resultNum = calculate(expression);
        	if(resultNum<=0){
        		resultText.setText("you entered must be positive number");
            }else{
            	resultNum = Math.sqrt(resultNum);      	    	
        	}
                	
        }else if (key.equals(FUNCTION[8])) {  
        	//������Ȼ����
        	resultNum = calculate(expression);
        	resultNum = Log(Math.E,resultNum);
        
        }else if (key.equals(FUNCTION[9])) {  
        	//������10Ϊ�׵Ķ���
        	resultNum = calculate(expression);
        	resultNum = Log(10,resultNum);
        }else if (key.equals(FUNCTION[10])) {  
        	//����������Ϊ�׵ĺ���
        	getNumToken(expression);
        	resultNum = Double.valueOf(numStack.pop()).doubleValue();
        	baseNum = Double.valueOf(numStack.pop()).doubleValue();
        	resultNum =Log(baseNum,resultNum);

            
        }else if (key.equals(FUNCTION[11])){
        	//ȡ�ٷֺ�����
        	resultNum = getNumberFromText()/100;  
        	
        }else if (key.equals(FUNCTION[12])){
        	//������Ȼ����e��x�η�
        	resultNum = calculate(expression);
       	    resultNum = Math.exp(resultNum);//��ʾ�е�����
        	
        }

        
        
        resultText.setText(String.valueOf(resultNum));
 
    
    }  
    
//  /*����׳�--ԭ*/
//    private void factorial(double factorial) {
//    	if(factorial==0.0){
//    		resultText.setText("0");
//    	}else{
//    	    int base=(int)factorial;
//    	    String style="0.0";
//    	    DecimalFormat df = new DecimalFormat();  
//    	    style = "0.00000E000";  
//    	    BigDecimal num=new BigDecimal(String.valueOf(factorial));//����BigDecimalʱָ����Ч����s
//            for(int i = 1; i <= base; i++){  
//                String temp1 = Integer.toString(i);  
//                BigDecimal temp2 = new  BigDecimal(temp1);  
//                num = num.multiply(temp2);  
//           } 
//          df.  applyPattern(style);  
//          resultText.setText(df.format(num));  
//    	}
//   }
     /*����׳�--��*/
    private double factorial(int x) {
    	if(x==0){
    		return(1);
    	}else{
    		 
    	    String style="0.0";
    	    DecimalFormat df = new DecimalFormat();     	     
    	    BigDecimal num=new BigDecimal(String.valueOf(x));//����BigDecimalʱָ����Ч����s
            for(int i = 1; i < x; i++){  
                String temp1 = Integer.toString(i);  
                BigDecimal temp2 = new  BigDecimal(temp1);  
                num = num.multiply(temp2);  
           } 
           df.applyPattern(style);  
           return(Double.valueOf(df.format(num)));  //��������ת����double�ͷ���
    	}
   }
 
    /** 
     *������������Ϊ�׵Ķ���
     * @param x,y ����������
     */
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
    * ͳ���͡�������ʵ��ͳ�Ƽ������Ĺ���
    * */
   private  void handleSpecialFunction(String label){
	   
	   String resultNum = "";  
	   int n,m;
	   getNumToken(expression);
	   
	   if(label.equals(SPECIAL_FUNTION[0])){//���������
	       m = Integer.valueOf(numStack.pop());
	       n = Integer.valueOf(numStack.pop());
	       resultNum =String.valueOf(Combination(n,m));
		   
	   }else if(label.equals(SPECIAL_FUNTION[1])){//����������
	       m = Integer.valueOf(numStack.pop());
	       n = Integer.valueOf(numStack.pop());
	       resultNum =String.valueOf(Arrangement(n,m));

	   }else if(label.equals(SPECIAL_FUNTION[2])){//���㷽��
		   resultNum = String.valueOf(Variance());
		   
	   }else if(label.equals(SPECIAL_FUNTION[4])){//�������ֲ�
		   resultNum = String.valueOf(BiDistribution());
	   }else if(label.equals(SPECIAL_FUNTION[5])){//���㲴�ɷֲ�
		   resultNum = String.valueOf(PoDistribution());
	   }else if(label.equals(SPECIAL_FUNTION[6])){//�����׼��̫�ֲ�
		   StandardDistribution(resultText.getText());
	   }else if(label.equals(SPECIAL_FUNTION[7])){//������ɢϵ��
		   resultNum = String.valueOf(Discrete_index());
	   }else{
		   
	   }
	   
       resultText.setText(resultText.getText()+" ) = "+resultNum);//���������
   } 

   /** 
    * ������Ϲ�ʽ ����
    * @param n,m
    * @return 
    */  
  
   private int Combination(int n,int m){
	   
	   if(n<=0||m<=0){
	   		resultText.setText("your enter must be positive number");
	   		return 0;
	    }else{
		    if(m>n){
		  	    resultText.setText("x must be smaller than or equal to y");
		  		return 0;
		  	}else{
			   	int c = (int)(factorial(n)/(factorial(m)*factorial(n-m)));
			   	return (c);
			}
	    }
  	
  }

   /** 
    * �������й�ʽ ����
    * @param n,m
    * @return 
    */  
   private int Arrangement(int n,int m){
  		
    	if(n<=0||m<=0){
    		resultText.setText("your enter must be positive number");
    		return 0;
        }else{
	   	    if(m>n){
	   		  resultText.setText("m must be smaller than or equal to n");
	   		  return 0;
	   	    }else{
	    	  int a =(int) (factorial(n)/factorial(n-m));
	    	  return (a);
	        }
     }
   }
   /*����ƽ��ֵ*/
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
   /*���㷽��*/
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
    * �����ֲ���ֵ
    * @param n,p,k   ʵ��n�Σ�ÿ�η�������Ϊp����������ΪK
    */
   private double BiDistribution(){
	   int n,k;
	   double P;
	   Stack<String> numstack  = (Stack<String>)numStack.clone();
	   k=Integer.valueOf(numstack.pop());
	   P=Double.valueOf(numstack.pop());
	   n=Integer.valueOf(numstack.pop());
	   System.out.println(n);
	   P=Combination(n,k)*(Math.pow(P,k))*(Math.pow(1-P,(n-k)));
	   return P;
	
   }
   /**
    * ���ɷֲ���ֵ
    * @param ��,k   ÿ�η�������Ϊ�ˣ���������ΪK
    */
   private double PoDistribution(){
	   int k;
	   float ��;
	   double p;
	   Stack<String> numstack  = (Stack<String>)numStack.clone();
	   k=Integer.valueOf(numstack.pop());
	   ��=Float.valueOf(numstack.pop());
	   p=(Math.pow(��,k)*Math.pow(Math.E,-��))/factorial(k);
	   return p;
	
}
   /**
    * ����ɢϵ��
    */
   private double Discrete_index(){ 
	   double index;
	   index=Math.sqrt(Variance())/Ave();
	   return  index;
   }
   /**
    * ��׼��̫�ֲ�����
    * ���ݷָ���ַ�����û���ֵ
    * -3.89��3.89������Ļ������ С�� 0.0001��
    * ����ȷ����Ч�Ļ�������Ϊ-3.89��3.89
    * ��ʵ�ַָ��ʱ�򾫶ȶ�Ϊ0.0001���õ��Ľ���Ͳ��õ��Ľ�������-0.0002��+0.0002֮�䣨�Ѿ����飩
    * 
    * @param u      ��������
    */
   private  void StandardDistribution(String U){
	   float u=Float.valueOf(U);
       float ret  = 0;
       if(u < -3.89){
    	   resultText.setText("̫С");
       }
       else if(u > 3.89){
    	   resultText.setText("̫С");
       }
       float temp = -3.89f;
       while(temp <= u){
           ret += 0.0001f * fx(temp);
           temp += 0.0001f;
       }
       resultText.setText(ret+"");
   }

   /**
    * �󱻻������ĺ���ֵ    (1/(2 * PI)^(0.5))e^(-t^2/2)
    * @param x      ����x
    * @return       ����ֵ
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
   /**
    * ��չ�͡�������������չ�����¼�
    * */
   
   private void handleSupplement(String label){

	   if(label.equals("BMI")){
		   BMI();
	   }else if(label.equals("BFR")){
		   BFR();
  
	   }
   }
   /*��������ָ��*/
   private  void BMI(){
	   double weight = 0,height = 0;
	   //����δ�������ݵ��쳣
	   try {  
    	   weight = Double.valueOf(weightText.getText()); //��������л������
    	   height = Double.valueOf(heightText.getText());//��������л�����
       } catch (NumberFormatException e) { 
	   		resultText.setText("�ף�����������ߡ�����Ŷ��");

       } 
	   //����BMI ��ֵ
	   double bmi=weight/(height*height);
	   	//�ֵȼ����
	   	if(bmi<18.5){
	   		resultText.setText(bmi+"���ع���");
	   	}else if(bmi>=18.5&&bmi<23.9){
	   		resultText.setText(bmi+"������Χ");
	   	}else if(bmi>=23.9){
	   		if(bmi>=23.9&&bmi<27.9){
	   			resultText.setText(bmi+"����ǰ��");
	   	    }else if(bmi>=27.9&&bmi<29.9){
	   	    	resultText.setText(bmi+"I�ȷ���");
	   	    }else if(bmi>=29.9&&bmi<39.9){
	   	    	resultText.setText(bmi+"II�ȷ���");
	   	    }else{
	   	    	resultText.setText(bmi+"III�ȷ���");
	   	    }
	   }
	   
   }
   
   /*��������֬����*/
   private void BFR(){
	   double weight = 0,height = 0;
	   int    age = 0;
	   double bfr,bmi;
	   double dbw,bmr;//�������أ�DBW��:desirable body weight; ������л�ʣ�BMR��:Basal metabolic rate
	   double dailyCalorie;//ÿ�����迨·��
	   int    sex = 0;//�Ա�Ů��Ϊ0������Ϊ1��Ĭ����Ů��
	   //����δ�������ݵ��쳣
	   try {  
    	   
    	   weight = Double.valueOf(weightText.getText()); //��������л������
    	   height = Double.valueOf(heightText.getText());//��������л�����
    	  // waistLine = Double.valueOf(waistText.getText());
		   age = Integer.valueOf(ageText.getText());
       } catch (NumberFormatException e) { 
	   		resultText.setText("�ף�����������ߡ����ء������ֵŶ��");

       } 
	   //�ж��Ա�
	   if(!(c.getSelectedCheckbox()==cRadio1)){//�������Ů��������Ϊ1
		   sex = 1;
	   }
	   //����BMI
	   bmi = weight/(height*height);
	   //����BFR
       bfr = 1.2*bmi+0.23*age-5.4-10.8*sex; 
       //������������
       dbw = 22*height*height;
       //���������л��
       if(sex==1){
    	   bmr = 661+9.6*weight+1.72*height-4.7*age ;  
       }else{
    	   bmr = 67+13.73*weight+5*height-6.9*age ;  
       }
      
       //����ÿ�����迨·��
       //dailyCalorie = bmr*ϵ�� ;
       resultText.setText(bfr+"");
    }
   /***********************************************************************************************/
   
   /** 
    * �ӽ���ı����л�ȡ���� 
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
    * ��������ʽ�л�ȡ����
    * 
    */
    private void getNumToken(String expression){
    	char[] arr  = expression.toCharArray();
    	int arrLen = arr.length;
        int currentIndex  = 0;//��ǰ�ַ���λ��
        int count = 0;//�ϴζ��ŵ����ζ��ŵ��ַ��ĳ���,���ڻ��֮�����ֵ
        
        for(int i=0 ;i<arrLen;i++) {
            if("0123456789.".indexOf(arr[i])>= 0) {
                count++;

            } else {//�����ǰ�ַ��Ƿ����ֵ��ַ�
            	if(count>0){
                    numStack.push(new String(arr,currentIndex,count));//ȡ�����ַ�֮�������
            	}
                count = 0;
                currentIndex = i+1;
            }
        }
        if(count > 1 || (count == 1 && "0123456789.".indexOf(arr[currentIndex])>= 0)) {//���һ���ַ��������Ż�����������������������ջ��
            numStack.push(new String(arr,currentIndex,count));
        } 
    }
   
   
  /********���࣬����һ��Calculator2����****************************************************************************************************/
    public static void main(String args[]) {  
        Calculator2 calculator = new Calculator2();  
        calculator.setVisible(true);  
        calculator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
    }  
} 

class MyFocusListener implements FocusListener { 
    String info; 
    JTextField jtf; 
    public MyFocusListener(String info, JTextField jtf) { 
        this.info = info; 
        this.jtf = jtf; 
    } 
    public void focusGained(FocusEvent e){//��ý����ʱ��,�����ʾ���� 
        String temp = jtf.getText(); 
        if(temp.equals(info)){ 
            jtf.setText(""); 
        } 
    } 
    public void focusLost(FocusEvent e) {//ʧȥ�����ʱ��,�ж����Ϊ��,����ʾ��ʾ���� 
        String temp = jtf.getText(); 
        if(temp.equals("")){ 
            jtf.setText(info); 
        } 
   }
=======
import java.awt.Color;  
import java.awt.GridLayout;  
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;  
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import javax.swing.JButton;  
import javax.swing.JFrame;  
import javax.swing.JPanel;  
import javax.swing.JTextField; 
import java.math.BigDecimal; //��������� 
import java.util.Collections;
import java.util.Stack;
//import java.util.Scanner;//
import java.awt.*;
import java.text.DecimalFormat;
import java.lang.NumberFormatException;
import java.lang.String;
import java.awt.event.FocusListener;
/** 
 * һ����������֧�ּ򵥵ļ���
 */  
public class Calculator2 extends JFrame implements ActionListener {  
	/***************************************************************************************************************/
	/********��������ذ������Ƽ���ť����*******/

	
/********��������ذ������Ƽ���ť����*******/
	
	/*������ģʽ���л���������ʾ*/
    private final String[] MODEL = {"����","��ѧ��","ͳ����","��������","����ת��"};
    /*�����������ϵļ�����ʾ���� */  
    private final String[] KEYS = {"(",")", "C","��","7", "8", "9", "+" , "4", "5", "6",  
            "-", "1", "2", "3", "*", "0", ".", "=", "," }; 
    /*��ѧ�ͼ������ϵĺ���ͼ����ʾ����*/ 
    private final String[] FUNCTION = { "1/x", "��2", "x^y", "x!", "sin","cos","tan","��" ,
    		"In","lg","log","%","e^x","e","��",","};
    /*ͳ���ͼ����������⹦��ͼ����ʾ����*/
    private final String[] SPECIAL_FUNTION = { "C(N,M)", "A(N,M)","D", "Ave","����ֲ�",
    		"���ɷֲ�","��׼��̫�ֲ�","��ɢϵ��"};
    /*��������������ϵļ�����ʾ����*/
    private final String[] SUPPLEMENT={"����ָ��","������","����"};
    /*����ת��ģ���ϵļ�����ʾ����*/
    private final String[] REFRESH={"ˢ��"};
 
    /*���������ť����ĳ��ȣ��������Ǵ�����Ӧ������*/
    int modelLen=MODEL.length;//���ڴ��MODEL�����а�ť����
    int keysLen=KEYS.length;//���ڴ��KEYS�����еİ�ť����
    int functionLen=FUNCTION.length;//���ڴ��FUNCTION�����еİ�ť����
    int special_functionLen=SPECIAL_FUNTION.length;//���ڴ��SPECIAL_FUNTION �����еİ�ť����
    int supplementLen=SUPPLEMENT.length;//���ڴ�Ž�������ģ���а�ť������
    int refreshLen=REFRESH.length;//���ڴ�Ż���ת����ť������
    
    /*ģʽ�л��ϵļ�����*/
    private JButton models[] = new JButton[keysLen];
    /*�������ı��� */  
    private JTextField resultText = new JTextField("");//������Ĭ�ϵ�0
    /*��������ı��� */  
    private JTextField heightText = new JTextField();
    /*���������ı��� */  
    private JTextField weightText = new JTextField();
    /*��Χ�����ı��� */  
    private JTextField waistText = new JTextField();
    /*���������ı��� */  
    private JTextField ageText = new JTextField();
    /*�򵥼������ϼ��İ�ť */  
    private JButton keys[] = new JButton[keysLen];  
    /*�������Ϲ���ͼ����ʾ����*/  
    private JButton function[] = new JButton[functionLen+keysLen]; 
    /*�������ϳ��ú����İ�ť*/
    private JButton special_function[] = new JButton[special_functionLen+keysLen];
    /*����������չģ���ϵİ�ť*/
    private JButton supplements[]=new JButton[supplementLen];
    /*����ת���ϵİ�ť*/
    private JButton refresh[] = new JButton[refreshLen];
    /*****************************************************************************************************/
    /****/
    
    /*Ϊ��ʵ�����ȼ�������*/
    private Stack<String> postfixStack  = new Stack<String>();//��׺ʽջ
    private Stack<Character> opStack  = new Stack<Character>();//�����ջ
    private int [] operatPriority  = new int[] {0,3,2,1,-1,1,0,2};//���������ASCII��-40����������������ȼ� 
    
    //���ü���panel,Panel1��ʾ���ͼ������Ľ��棬panel2��ʾ��ѧ�ͼ������Ľ��棬panel3��ʾͳ���ͼ������Ľ��棬panel4��ʾ��չ�ͼ������Ľ���
    JPanel panel,panel1,panel2,panel3,panel4,panel5;
    //���õ�ѡ���Է���ѡ����Ů,�Ӷ���������ָ������֬��
    Checkbox cRadio1,cRadio2;
    CheckboxGroup c;
    //���ü���String���͵ı������ڽ���û�оۼ�����Ӧ�ı�����ʱ��Ĭ����ʾ
    String info1="������������(��)";
    String info2="�������������(ǧ��)";
    String info3="�����������Χ";
    String info4="�������������(��)";
    /*Ϊ��ʵ�ֿ�ѧ���㼰���������������*/
    private double  baseNum = 1;    //�������x^yʱx��ֵ
    private String   expression = ""; //��ſ�ѧ����ʱ��ȥ������ı��ʽ�������sin��5+12*(3+5)/7��ʱ��expression=5+12*(3+5)/7
    private boolean isFunction = false;   //�Ƿ��ǿ�ѧ����
    private boolean isSpecialFunction = false;   //�Ƿ���ͳ�Ƽ���
    private String   opreator = ""; //��ſ�ѧ����ʱ�Ĳ����� 
    private Stack<String> numStack  = new Stack<String>();//�������Ķ��������

    
    
   /** 
    ************ ���캯�� *************************************************************************
    *********************/  
    public Calculator2() {  
        super();  
        // ��ʼ��������  
        init();  
        // ���ü������ı�����ɫ  
        this.setBackground(new Color(54,54,54   ));  
        this.setTitle("������");  
        // ����Ļ(500, 300)���괦��ʾ������  
        this.setLocation(500, 300);  
        // �����޸ļ������Ĵ�С  
        this.setResizable(true);             //ע�⣺�����С���Ըı� 
        // ʹ�������и������С����  
        this.setSize(600, 300); 
    }  
  
    /** 
     *******��ʼ�������� ***************************************************************************************** 
     *�������Ļ�������
     */  
    
    private void init() {   	
        // �ı����е����ݲ����Ҷ��뷽ʽ  
        resultText.setHorizontalAlignment(JTextField.RIGHT);  
        // �����޸Ľ���ı���  
        resultText.setEditable(true);            //����ı���ʵ��
        // �����ı��򱳾���ɫΪ��ɫ  
        resultText.setBackground(Color.white);
        //�����ı���Ŀ��
        resultText.setSize(100,100);
        
        //ģ���л����ĸ���ť����һ����������modelPanel������
        JPanel modelPanel = new JPanel();
        //����modelPanel����İ�ť����������,ˮƽ����
        FlowLayout fl = new FlowLayout(); 
        fl.setAlignment(FlowLayout.LEFT); 
        modelPanel.setLayout(fl);
        for (int i = 0; i < modelLen; i++) {  
            models[i] = new JButton(MODEL[i]);  
            //ȥ��modelsPanel�����button�ı߽���
            models[i].setBorderPainted(false);
            //ȥ��button���ʱ�����ϳ��ֵı߽��
            models[i].setFocusPainted(false);
            //����button��������Ĵ�С
            models[i].setFont(new Font("΢���ź�", Font.PLAIN, 14));
            models[i].setForeground(new Color(255,255,255));
            modelPanel.add(models[i]); 
            //����modelPanle��button�ı�����ɫ
            modelPanel.setBackground(new Color(54,54,54   ));
            models[i].setBackground(new Color(54,54,54   ));  
        }  
        
       // ����һ��������ı���  
       JPanel topPanel = new JPanel();  
       topPanel.setLayout(new BorderLayout(0,0));  
       topPanel.add("Center", resultText); 
      
       //����һ������Panel�����ģʽ�л�����ʾ���
       JPanel top = new JPanel();  
       top.setLayout(new GridLayout(2,1,0,0));  
       top.add(modelPanel); 
       top.add("South",topPanel);
      
       // ��ʼ�����������������֡����㡢���š�����ȼ��İ�ť����������һ������calkeysPanel������  
       JPanel calckeysPanel = new JPanel();  
       // �����񲼾�����4�У�4�е���������֮���ˮƽ������Ϊ3�����أ���ֱ������Ϊ3������  
       calckeysPanel.setLayout(new GridLayout(5, 4, 0, 0));  
       for (int i = 0; i < keysLen; i++) {  
            keys[i] = new JButton(KEYS[i]);  
            calckeysPanel.add(keys[i]);  
            //ȥ��button���ʱ�����ϳ��ֵı߽��
            keys[i].setFocusPainted(false);
            keys[i].setBackground(new Color(54,54,54   ));  
            //����button���������С
            keys[i].setFont(new Font("΢���ź�", Font.PLAIN, 16));
            keys[i].setForeground(new Color(255,255,255));
        }     
       
        // �½�һ����Ļ��壬��calckeys������ڸû�����  
        panel1 = new JPanel();  
        // ������ñ߽粼�ֹ����������������֮���ˮƽ�ʹ�ֱ�����ϼ����Ϊ0����  
        panel1.setLayout(new BorderLayout(0,0));   
        panel1.setBackground(new Color(54,54,54   ));
        panel1.add("Center", calckeysPanel);
        
        // ��ʼ����ѧ�������������ܼ����� �����������ܺͿ�ѧ���������ܼ�����һ��������  
        JPanel functionsPanel = new JPanel();  
        // �����񲼾ֹ�������5�У�7�е���������֮���ˮƽ������Ϊ0�����أ���ֱ������Ϊ0������  
        functionsPanel.setLayout(new GridLayout(9, 4,0,0));
        //����һЩ��ѧ����İ�ť
        for (int i = 0; i < functionLen; i++) {  
            function[i] = new JButton(FUNCTION[i]);   
            //ȥ��button���ʱ�����ϳ��ֵı߽��
            function[i].setFocusPainted(false);
            //����button���������С
            function[i].setFont(new Font("΢���ź�", Font.PLAIN, 16));
            function[i].setForeground(new Color(255,255,255));  
            function[i].setBackground(new Color(54,54,54   ));  
            functionsPanel.add(function[i]); 
        } 
        //������������Լ����ְ�ť
        for (int i = functionLen; i < (keysLen+functionLen); i++) {  
        	function[i] = new JButton(KEYS[i-functionLen]); 
        	//ȥ��button���ʱ�����ϳ��ֵı߽��
            function[i].setFocusPainted(false);
            //����button���������С
            function[i].setFont(new Font("΢���ź�", Font.PLAIN, 16));
            function[i].setForeground(new Color(255,255,255));  
            function[i].setBackground(new Color(54,54,54   ));  
        	functionsPanel.add(function[i]);  
        }  
 
        // �½�һ����Ļ��壬��function������ڸû����ڣ�ʵ�ֿ�ѧ�������Ľ���
        panel2=new JPanel();
        panel2.setLayout(new BorderLayout(0,0));
        panel2.setBackground(new Color(54,54,54   ));
        panel2.add("Center",functionsPanel);
        
        // ��ʼ�����������⺯�����ܼ����ú�ɫ��ʾ���������������ܼ�����һ��������  
        JPanel special_functionsPanel = new JPanel();  
        // �����񲼾ֹ�������6�У�4�е���������֮���ˮƽ������Ϊ0�����أ���ֱ������Ϊ0������  
        special_functionsPanel.setLayout(new GridLayout(7, 4, 0, 0)); 
        //����ͳ������Ĺ��ܰ�ť
        for (int i = 0; i < special_functionLen; i++) {  
        	special_function[i] = new JButton(SPECIAL_FUNTION[i]); 
        	//ȥ��button���ʱ�����ϳ��ֵı߽��
            special_function[i].setFocusPainted(false);
            //����button���������С
            special_function[i].setFont(new Font("΢���ź�", Font.PLAIN, 16));
            special_function[i].setForeground(new Color(255,255,255));
            special_function[i].setBackground(new Color(54,54,54   ));  
        	special_functionsPanel.add(special_function[i]);   
        }  
        //������������Լ����ְ�ť
        for (int i = special_functionLen; i < (keysLen+special_functionLen); i++) {  
        	special_function[i] = new JButton(KEYS[i-special_functionLen]); 
        	//ȥ��button���ʱ�����ϳ��ֵı߽��
            special_function[i].setFocusPainted(false);
            //����button���������С
            special_function[i].setFont(new Font("΢���ź�", Font.PLAIN, 16));
            special_function[i].setForeground(new Color(255,255,255));  
            special_function[i].setBackground(new Color(54,54,54   ));  
        	special_functionsPanel.add(special_function[i]);  
        }  
        //�½�һ����Ļ��壬��calckeys�Լ�special_function������ڸû����ڣ�ʵ��ͳ�Ƽ������Ľ���
        panel3=new JPanel();
        panel3.setLayout(new BorderLayout(0,0));
        panel3.setBackground(new Color(54,54,54   ));
        panel3.add("Center",special_functionsPanel);
        
        //��ʼ����������չģʽ������չ���ܵĺ�������һ��panel��
        JPanel supplementPanel=new JPanel();
        supplementPanel.setLayout(new GridLayout());
        for (int i = 0; i < supplementLen; i++) {  
        	supplements[i] = new JButton(SUPPLEMENT[i]); 
        	//ȥ��button���ʱ�����ϳ��ֵı߽��
            supplements[i].setFocusPainted(false);
            //����button���������С
            supplements[i].setFont(new Font("΢���ź�", Font.PLAIN, 16));
            supplements[i].setBackground(new Color(54,54,54   ));  
        	supplementPanel.add(supplements[i]);  
            supplements[i].setForeground(new Color(255,255,255));  
        }  
        
        //���õ�ѡ�� �������Ա��ѡ�� 
        c=new CheckboxGroup();
        cRadio1=new Checkbox("����",c,false);
        cRadio2=new Checkbox("Ů��",c,true);
        //�½�һ�����壬�����񲼾֣��������У�������������������
        JPanel panel_4=new JPanel();
        panel_4.setLayout(new GridLayout(3,2));
        panel_4.add(heightText);
        panel_4.add(weightText);
        panel_4.add(waistText);
        panel_4.add(ageText);
        panel_4.add(cRadio1);
        panel_4.add(cRadio2);
        //�½�һ����Ļ��壬��supplement������ڸû����ڣ�ʵ����չ�������Ľ���
        panel4=new JPanel();
        panel4.setLayout(new BorderLayout(3,3));
        panel4.setBackground(new Color(54,54,54   ));
        panel4.add("North", panel_4);
        panel4.add("Center",supplementPanel);
        
      //��ʼ������������ת��ģʽ���������һ��panel��
        JPanel refreshPanel=new JPanel();
        refreshPanel.setLayout(new GridLayout());
        for (int i = 0; i < refreshLen; i++) {  
            refresh[i] = new JButton(REFRESH[i]);  
            //ȥ��modelsPanel�����button�ı߽���
            refresh[i].setBorderPainted(false);
            //ȥ��button���ʱ�����ϳ��ֵı߽��
            refresh[i].setFocusPainted(false);
            //����button��������Ĵ�С
            refresh[i].setFont(new Font("΢���ź�", Font.PLAIN, 14));
            refresh[i].setForeground(new Color(255,255,255));
            refreshPanel.add(refresh[i]); 
            //����modelPanle��button�ı�����ɫ
            refreshPanel.setBackground(new Color(54,54,54   ));
            refresh[i].setBackground(new Color(54,54,54   ));  
        }  
        //�½�һ���󻭰�Panel5,��Ż���ת��
        panel5=new JPanel();
        panel5.setLayout(new BorderLayout());
        panel5.add("Center",refreshPanel);
        
        /***************Ϊ����ť����¼�������***********************************/   
        //��ʹ��ͬһ���¼����������������󡣱������������implements ActionListener
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
        for (int i = 0; i < refreshLen; i++) {  
        	refresh[i].addActionListener(this);  
        }
        
        /*Ϊ�ı����ʼ��*/
        heightText.setText(info1);
        weightText.setText(info2);
        waistText.setText(info3);
        ageText.setText(info4);
        /*Ϊ�ı�����ӽ����¼���Ӧ��*/
        heightText.addFocusListener(new MyFocusListener(info1, heightText));//��ӽ����¼���ӳ 
        weightText.addFocusListener(new MyFocusListener(info2, weightText));
        waistText.addFocusListener(new MyFocusListener(info3, waistText));//��ӽ����¼���ӳ 
        ageText.addFocusListener(new MyFocusListener(info4, ageText));
    
        //�½�һ������panel,����������岼��
        panel=new JPanel(new BorderLayout(0,0));
        panel.setBackground(new Color(54,54,54));  
        add(panel);
        panel.add("North",top);
        panel.add("Center",panel1);  
    }    
    

    /** 
     *********�����¼� *****************************************************************************************************
     */  
    public void actionPerformed(ActionEvent e) {  
    	// �н����ռ�����  
    	double result = 0.0;
    	// ��ȡ�¼�Դ�ı�ǩ  
        String label = e.getActionCommand();
        //����ģʽѡ�񣺼��ͣ���ѧ�ͣ�ͳ���ͣ���չ�ͣ�
        if(label.equals(MODEL[0])){//����
        	panel.add("Center",panel1);
        	panel1.setVisible(true);
        	panel2.setVisible(false);
        	panel3.setVisible(false);
        	panel4.setVisible(false);
        	panel5.setVisible(false);
        	handleC();     
        }else if(label.equals(MODEL[1])){//��ѧ��
        	panel.add("Center",panel2);
        	panel1.setVisible(false);
        	panel2.setVisible(true);
        	panel3.setVisible(false);
        	panel4.setVisible(false);
        	panel5.setVisible(false);
        	handleC();     
        }else if(label.equals(MODEL[2])){//ͳ����
        	panel.add("Center",panel3);
        	panel1.setVisible(false);
        	panel2.setVisible(false);
        	panel3.setVisible(true);
        	panel4.setVisible(false);
        	panel5.setVisible(false);
        	handleC();     
        }else if(label.equals(MODEL[3])){//��������
        	panel.add("Center",panel4);
        	panel1.setVisible(false);
        	panel2.setVisible(false);
        	panel3.setVisible(false);
        	panel4.setVisible(true);
        	panel5.setVisible(false);
        	handleC();     
        }else if(label.equals(MODEL[4])){//����ת��
        	panel.add("Center",panel5);
        	panel1.setVisible(false);
        	panel2.setVisible(false);
        	panel3.setVisible(false);
        	panel4.setVisible(false);
        	panel5.setVisible(true);
        	handleC();     
        }else if (label.equals("=")) {  // �û�����"="��  ,���������������
        	if(isFunction){
        		handleFunction(opreator);
        	    isFunction = false;   //�Ƿ��ǿ�ѧ���㣬��sin,cos,lg��
        	    opreator = "";
        	}else if(isSpecialFunction){
        		
        		handleSpecialFunction(opreator);
        	    isSpecialFunction = false;   //�Ƿ���ͳ�Ƽ��㣬��A��N,M��,C(N,M)��
        	    opreator = "";
        	}else{

    	      	result = calculate(resultText.getText());//������
    	      	resultText.setText(String.valueOf(result));//������
        	}

        }else if (label.equals(KEYS[2])) { // �û�����"C"��  ����ʼ��������
        	handleC();                     //������������� 
        	
        }else if (label.equals(KEYS[3])) {  //�û����ˡ������˻ؼ������ı������һ���ַ�ȥ��
            handleBackspace();
        	
        }else if ("0123456789.()+-*/,e��".indexOf(label) >= 0) {  // �û���������Ҫ����ı��ʽ
        	if(isFunction||isSpecialFunction){           //����ǿ�ѧ���㣬��sin������������ı��ʽ����һ���ַ���
        		expression = expression + label;
        	}
        	resultText.setText(resultText.getText()+label);

        }else if(label.equals(FUNCTION[0])||label.equals(FUNCTION[1])||label.equals(FUNCTION[3])||label.equals(FUNCTION[11])){//���������ѧ�ͼ���������
            handleFunction(label);
            
        }else if(label.equals(SPECIAL_FUNTION[0])||label.equals(SPECIAL_FUNTION[1])||    //�����ͳ�Ƽ�������ת��handleSpecialFunction����
        		label.equals(SPECIAL_FUNTION[2])||label.equals(SPECIAL_FUNTION[3])||
        		label.equals(SPECIAL_FUNTION[4])||label.equals(SPECIAL_FUNTION[5])||
        	    label.equals(SPECIAL_FUNTION[6])||label.equals(SPECIAL_FUNTION[7])){
        	if(label.equals(SPECIAL_FUNTION[0])){
     		    resultText.setText("��ֱ�����n,m�����ԡ�,������                   C ( "); 

        	}else if(label.equals(SPECIAL_FUNTION[1])){
     		    resultText.setText("��ֱ�����n,m�����ԡ�,������                   A ( "); 

        	}else{
     		    resultText.setText("��������Ӧ���ݣ����ԡ�,������                   "+label + "( "); 
        	}
        	isSpecialFunction = true;	
        	opreator = label;
        	
        }else if(label.equals("BMI")||label.equals("BFR")||  //�������չ�����������ת��handleSupplement����
        		label.equals("$")||label.equals("����")){
        	handleSupplement(label);
        	
        }else{
        	if(label.equals("e^x")){
        		resultText.setText("e^"+"( ");
        	}else if(label.equals("x^y")){
        		baseNum = getNumberFromText();
        		resultText.setText(resultText.getText()+"^( ");
        	}else if(label.equals("log")){
        		//baseNum = getNumberFromText();
        		
        		resultText.setText("��ֱ�����������Ա���/����  ���ԡ���������           "+"log"+"(   ");
        	}
//        	else if(label.equals("e")){
//            	resultText.setText(resultText.getText()+label);
//        	}else if(label.equals("��")){
//            	resultText.setText(resultText.getText()+label);
//        	}
        	else{
        		resultText.setText(label+"( ");
        	}
        	
        	isFunction = true;	
        	opreator = label;
        }
    }  

    /*����Backspace�������µ��¼� */  
    private void handleBackspace() {  
        String text = resultText.getText();  
        int i = text.length();  
        if (i > 0) {  
            // �˸񣬽��ı����һ���ַ�ȥ��  
            text = text.substring(0, i - 1);  
            if (text.length() == 0) {  
                // ����ı�û�������ݣ����ʼ���������ĸ���ֵ  
                resultText.setText("");  
            } else {  
                // ��ʾ�µ��ı�  
                resultText.setText(text);  
            }  
        }  
    } 


   /* ����C�������µ��¼� */  
    private void handleC() {  
    	 // ��ʼ���������ĸ���ֵ  
        resultText.setText(""); 
        isFunction = false;   
        isSpecialFunction = false;   
	    opreator = "";
        baseNum = 1;    
	    expression = "";
	    numStack.clear();
	    postfixStack.clear();
	    opStack.clear();
        
    }
    
    /*****************************************************************************************************************/
    
   public double calculate(String expression) {
        Stack<String> resultStack  = new Stack<String>();
        prepare(expression);
        Collections.reverse(postfixStack);//����׺ʽջ��ת
        String firstValue  ,secondValue,currentValue;//�������ĵ�һ��ֵ���ڶ���ֵ�����������
        while(!postfixStack.isEmpty()) {
            currentValue  = postfixStack.pop();
            if(!isOperator(currentValue.charAt(0))) {//����������������������ջ��
                resultStack.push(currentValue);
            } else {//������������Ӳ�����ջ��ȡ����ֵ�͸���ֵһ���������
                 secondValue  = resultStack.pop();
                 firstValue  = resultStack.pop();
                 String tempResult  = calculate(firstValue, secondValue, currentValue.charAt(0));
                 resultStack.push(tempResult);
            }

        }
        return Double.valueOf(resultStack.pop());
    }
    /*
     * ����׼���׶ν����ʽת����Ϊ��׺ʽջ   @param expression
     */ 
    private void prepare(String expression) {
        opStack.push(',');//���������ջ��Ԫ�ض��ţ��˷������ȼ����
        char[] arr  = expression.toCharArray();
        int currentIndex  = 0;//��ǰ�ַ���λ��
        int count = 0;//�ϴ����������������������������ַ��ĳ��ȱ��ڻ��֮�����ֵ
        char currentOp = 'o'  ,peekOp;//��ǰ��������ջ��������
        for(int i=0;i<arr.length;i++) {
            currentOp = arr[i];
            if(isOperator(currentOp)) {//�����ǰ�ַ��������
                if(count > 0) {
                	if(count==1&&arr[currentIndex] =='e'){//����������� e������ת������Ӧ��ֵ���ƽ�ջ��
                		postfixStack.push(String.valueOf(Math.E));	
                	}else if(count==1&&arr[currentIndex]=='��'){//��������Ǧ� ������ת������Ӧ��ֵ���ƽ�ջ��
                		postfixStack.push(String.valueOf(Math.PI));	
                	}else{
                        postfixStack.push(new String(arr,currentIndex,count));//ȡ���������֮�������
                	} 
                }
                peekOp = opStack.peek();
                if(currentOp == ')') {//�����������������ջ�е�Ԫ���Ƴ�����׺ʽջ��ֱ������������
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
        if(count > 1 || (count == 1 && !isOperator(arr[currentIndex]))) {//���һ���ַ��������Ż��������������������׺ʽջ��
        	if(currentOp =='e'){//����������� e������ת������Ӧ��ֵ���ƽ�ջ��
        		postfixStack.push(String.valueOf(Math.E));	
        	}else if(currentOp=='��'){//��������Ǧ� ������ת������Ӧ��ֵ���ƽ�ջ��
        		postfixStack.push(String.valueOf(Math.PI));	
        	}else{
                postfixStack.push(new String(arr,currentIndex,count));//ȡ���������֮�������
        	}        
        } 
        
        while(opStack.peek() != ',') {
            postfixStack.push(String.valueOf( opStack.pop()));//��������ջ�е�ʣ���Ԫ����ӵ���׺ʽջ��
        }
    }
    /*
     * �ж��Ƿ�Ϊ��������
     * @param c
     * @return
     */
    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '(' ||c == ')';
    }
    
    /*
     * ����ASCII��-40���±�ȥ�����������ȼ�
     * @param cur
     * @param peek	
     * @return
     */
    public  boolean compare(char cur,char peek) {// �����peek���ȼ�����cur������true��Ĭ�϶���peek���ȼ�Ҫ��
    	boolean result  = false;
        if(operatPriority[(peek)-40] >= operatPriority[(cur) - 40]) {
           result = true;
        }
        return result;
    }
    
    /*
     * ���ո��������������������
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
     *��ѧ�͡������� �����ѧ����������������µ��¼� 
     * @param key ���������
     */  
    private void handleFunction(String key) { 
    	double resultNum = 0;  
        if (key.equals(FUNCTION[0])) {  
        	//����һ�����ĵ���
        	if(getNumberFromText()==0){   //�ж�Ҫ��������Ƿ�Ϊ��
        		resultText.setText("0û�е�����");
        	}else{
        		resultNum = 1/getNumberFromText();
        	}
        	
        }else if(key.equals(FUNCTION[1])){
        	//����һ������ƽ��
        	resultNum = getNumberFromText()*getNumberFromText();
           
        }else if(key.equals(FUNCTION[2])){
        	//����x��y�η� 
        	resultNum = calculate(expression);
        	resultNum = Math.pow(baseNum,resultNum);
        }
        else if(key.equals(FUNCTION[3])){
        	 // �׳�����  
        	try{
        		int num = Integer.parseInt(resultText.getText());
        		resultNum = factorial(num);
        	}catch(NumberFormatException e2){
       		 	resultText.setText("����������");
        	} 
        	
        }else if (key.equals(FUNCTION[4])){
        	// ���Ǻ���sin()
        	resultNum = calculate(expression);
        	resultNum = Math.sin(resultNum);
        	
        }else if (key.equals(FUNCTION[5])) {  
        	 // ���Ǻ���cos() 
        	resultNum = calculate(expression);
        	resultNum = Math.cos(resultNum); 
        	
        }else if (key.equals(FUNCTION[6])){
        	 //���Ǻ���tan()
        	resultNum = calculate(expression);
            resultNum = Math.tan(resultNum);
        	
        }else if (key.equals(FUNCTION[7])) {  
        	// ƽ��������  
        	resultNum = calculate(expression);
        	if(resultNum<=0){
        		resultText.setText("you entered must be positive number");
            }else{
            	resultNum = Math.sqrt(resultNum);      	    	
        	}
                	
        }else if (key.equals(FUNCTION[8])) {  
        	//������Ȼ����
        	resultNum = calculate(expression);
        	resultNum = In(resultNum);
        
        }else if (key.equals(FUNCTION[9])) {  
        	//������10Ϊ�׵Ķ���
        	resultNum = calculate(expression);
        	resultNum = Log(10,resultNum);
        }else if (key.equals(FUNCTION[10])) {  
        	//����������Ϊ�׵ĺ���
        	getNumToken(expression);
        	resultNum = Double.valueOf(numStack.pop()).doubleValue();
        	baseNum = Double.valueOf(numStack.pop()).doubleValue();
        	resultNum =Log(baseNum,resultNum);

            
        }else if (key.equals(FUNCTION[11])){
        	//ȡ�ٷֺ�����
        	resultNum = getNumberFromText()/100;  
        	
        }else if (key.equals(FUNCTION[12])){
        	//������Ȼ����e��x�η�
        	resultNum = calculate(expression);
       	    resultNum = Math.exp(resultNum);//��ʾ�е�����
        	
        }else if (key.equals(FUNCTION[13])){
       	 //������Ȼ����e
       	 resultNum = Math.E;//��ʾ�д��Ľ�
       	 
       }else if (key.equals(FUNCTION[14])){
       	 //����Բ���� ��
       	 resultNum = Math.PI;//��ʾ�е�����
       	 
       }
        
        
        resultText.setText(String.valueOf(resultNum));
 
    
    }  
    
    /** 
     *����׳˵ķ���
     * @param x 
     */  
    private double factorial(int x) {
    	if(x==0){
    		return(1);
    	}else{
    		 
    	    String style="0.0";
    	    DecimalFormat df = new DecimalFormat();  
    	    style = "0.0";  
    	    BigDecimal num=new BigDecimal(String.valueOf(x));//����BigDecimalʱָ����Ч����s
            for(int i = 1; i < x; i++){  
                String temp1 = Integer.toString(i);  
                BigDecimal temp2 = new  BigDecimal(temp1);  
                num = num.multiply(temp2);  
           } 
           df.applyPattern(style);  
           return(Double.valueOf(df.format(num)).doubleValue());  //��������ת����double�ͷ���
    	}
   }
 
    /** 
     *������EΪ�׵Ķ���
     * @param x  ����
     */  
   private double In(double x){
    	
    	if(x<=0){
    		resultText.setText("you entered must be positive number");
    		return 0;
        }else{
	    	double lg = Math.log(x);
	    	return (lg);
    	}
    	
   }
   
   /** 
    *������������Ϊ�׵Ķ���
    * @param x,y ����������
    */  
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
    * ͳ���͡�������ʵ��ͳ�Ƽ������Ĺ���
    * */
   private  void handleSpecialFunction(String label){
	   
	   String resultNum = "";  
	   int n,m,k;
	   float p;

	   getNumToken(expression);
	   
	   if(label.equals(SPECIAL_FUNTION[0])){//���������
	       m = Integer.valueOf(numStack.pop());
	       n = Integer.valueOf(numStack.pop());
	       resultNum =String.valueOf(Combination(n,m));
		   
	   }else if(label.equals(SPECIAL_FUNTION[1])){//����������
	       m = Integer.valueOf(numStack.pop());
	       n = Integer.valueOf(numStack.pop());
	       resultNum =String.valueOf(Arrangement(n,m));

	   }else if(label.equals(SPECIAL_FUNTION[2])){//���㷽��
		   resultNum = String.valueOf(Variance());
		   
	   }else if(label.equals(SPECIAL_FUNTION[3])){//����ƽ��ֵ
		   resultNum = String.valueOf(Ave());
	   }else if(label.equals(SPECIAL_FUNTION[4])){//�������ֲ�
		   resultNum = String.valueOf(BiDistribution());
	   }else if(label.equals(SPECIAL_FUNTION[5])){//���㲴�ɷֲ�
		   resultNum = String.valueOf(PoDistribution());
	   }else if(label.equals(SPECIAL_FUNTION[6])){//�����׼��̫�ֲ�
		   StandardDistribution(resultText.getText());
	   }else if(label.equals(SPECIAL_FUNTION[7])){//������ɢϵ��
		   resultNum = String.valueOf(Discrete_index());
	   }else{
		   
	   }
	   resultText.setText(resultText.getText()+" ) = "+String.valueOf(resultNum));//���������

   }    
   /** 
    * ������Ϲ�ʽ ����
    * @param n,m
    * @return 
    */  
  
   private int Combination(int n,int m){
	   
	   if(n<=0||m<=0){
	   		resultText.setText("your enter must be positive number");
	   		return 0;
	    }else{
		    if(m>n){
		  	    resultText.setText("x must be smaller than or equal to y");
		  		return 0;
		  	}else{
			   	int c = (int)(factorial(n)/(factorial(m)*factorial(n-m)));
			   	return (c);
			}
	    }
  	
  }

   /** 
    * �������й�ʽ ����
    * @param n,m
    * @return 
    */  
   private int Arrangement(int n,int m){
  		
    	if(n<=0||m<=0){
    		resultText.setText("your enter must be positive number");
    		return 0;
        }else{
	   	    if(m>n){
	   		  resultText.setText("m must be smaller than or equal to n");
	   		  return 0;
	   	    }else{
	    	  int a =(int) (factorial(n)/factorial(n-m));
	    	  return (a);
	        }
     }
   }
   
   /** 
    * �����ֵ����
    * @param order[] double���͵�����
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
   
   /** 
    * ���㷽���
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
    * �����ֲ���ֵ
    * @param n,p,k   ʵ��n�Σ�ÿ�η�������Ϊp����������ΪK
    */
   private double BiDistribution(){
	   int n,k;
	   double P;
	   Stack<String> numstack  = (Stack<String>)numStack.clone();
	   k=Integer.valueOf(numstack.pop());
	   P=Double.valueOf(numstack.pop());
	   n=Integer.valueOf(numstack.pop());
	   System.out.println(n);
	   P=Combination(n,k)*(Math.pow(P,k))*(Math.pow(1-P,(n-k)));
	   return P;
	
   }
   /**
    * ���ɷֲ���ֵ
    * @param ��,k   ÿ�η�������Ϊ�ˣ���������ΪK
    */
   private double PoDistribution(){
	   int k;
	   float ��;
	   double p;
	   Stack<String> numstack  = (Stack<String>)numStack.clone();
	   k=Integer.valueOf(numstack.pop());
	   ��=Float.valueOf(numstack.pop());
	   p=(Math.pow(��,k)*Math.pow(Math.E,-��))/factorial(k);
	   return p;
	
}
   /**
    * ����ɢϵ��
    */
   private double Discrete_index(){ 
	   double index;
	   index=Math.sqrt(Variance())/Ave();
	   return  index;
   }
   /**
    * ��׼��̫�ֲ�����
    * ���ݷָ���ַ�����û���ֵ
    * -3.89��3.89������Ļ������ С�� 0.0001��
    * ����ȷ����Ч�Ļ�������Ϊ-3.89��3.89
    * ��ʵ�ַָ��ʱ�򾫶ȶ�Ϊ0.0001���õ��Ľ���Ͳ��õ��Ľ�������-0.0002��+0.0002֮�䣨�Ѿ����飩
    * 
    * @param u      ��������
    */
   private  void StandardDistribution(String U){
	   float u=Float.valueOf(U);
       float ret  = 0;
       if(u < -3.89){
    	   resultText.setText("̫С");
       }
       else if(u > 3.89){
    	   resultText.setText("̫С");
       }
       float temp = -3.89f;
       while(temp <= u){
           ret += 0.0001f * fx(temp);
           temp += 0.0001f;
       }
       resultText.setText(ret+"");
   }

   /**
    * �󱻻������ĺ���ֵ    (1/(2 * PI)^(0.5))e^(-t^2/2)
    * @param x      ����x
    * @return       ����ֵ
    */
   private static float fx(float x){
       float ret = 0;
       double a = 1.0 / Math.sqrt(Math.PI * 2);
       a  = a * Math.pow(Math.E, -0.5 * Math.pow(x, 2));
       ret = (float) a;
       return ret;
   }
   
  
   
   /***********************************************************************************************/
   /**
    * ��չ�͡�������������չ�����¼�
    * */
   
   private void handleSupplement(String label){
	   double weight,height,waistLine;
	   int    age;
	   
	   weight = Double.valueOf(weightText.getText()); //��������л������
	   height = Double.valueOf(heightText.getText());//��������л�����
	   
	   if(label.equals(SUPPLEMENT[0])){//��������ָ��
		   BMI(weight,height);
	   }else if(label.equals(SUPPLEMENT[1])){//����������
		   waistLine = Double.valueOf(waistText.getText());
		   age = Integer.valueOf(ageText.getText());
		   BFR(weight,height,waistLine,age);   
	   }else{
		   
	   }
   }
   
   /**
    * �������غ���
    * @param weight��height   ���أ����
    */
   
   private void BMI(double weight,double height){
	    
	    if(height>3||height<0){
	    	resultText.setText("��ע����ߵĵ�λΪ��");
	    }else{
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
   }
   
   /**
    * �������غ���
    * @param weight��height,waistLine,age   ���أ���ߣ���Χ������
    */
   private  void BFR(double weight,double height,double waistline,int age){
	   double bfr;
    	if((c.getSelectedCheckbox())==cRadio1){//Ů�����㹫ʽ    //ȱʡ
    		double a=waistline*0.74;
    		double b=weight*0.082+34.89;
    		 bfr=(a-b)/weight;
    	}else{//�������㹫ʽ
    		double a=waistline*0.74;
    		double b=weight*0.082+44.74;
    	    bfr=(a-b)/weight;
    		
    	}	
    	resultText.setText(bfr+"");
    }
   
 
   /** 
    * �ӽ���ı����л�ȡ���� 
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
    * ��������ʽ�л�ȡ����
    * 
    */
    private void getNumToken(String expression){
    	char[] arr  = expression.toCharArray();
    	int arrLen = arr.length;
        int currentIndex  = 0;//��ǰ�ַ���λ��
        int count = 0;//�ϴζ��ŵ����ζ��ŵ��ַ��ĳ���,���ڻ��֮�����ֵ
        
        for(int i=0 ;i<arrLen;i++) {
            if("0123456789.".indexOf(arr[i])>= 0) {
                count++;

            } else {//�����ǰ�ַ��Ƿ����ֵ��ַ�
            	if(count>0){
                    numStack.push(new String(arr,currentIndex,count));//ȡ�����ַ�֮�������
            	}
                count = 0;
                currentIndex = i+1;
            }
        }
        if(count > 1 || (count == 1 && "0123456789.".indexOf(arr[currentIndex])>= 0)) {//���һ���ַ��������Ż�����������������������ջ��
            numStack.push(new String(arr,currentIndex,count));
        } 
    }
   

   
   
  /********���࣬����һ��Calculator2����****************************************************************************************************/
    public static void main(String args[]) {  
        Calculator2 calculator = new Calculator2();  
        calculator.setVisible(true);  
        calculator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
    }  
}  

class MyFocusListener implements FocusListener { 
    String info; 
    JTextField jtf; 
    public MyFocusListener(String info, JTextField jtf) { 
        this.info = info; 
        this.jtf = jtf; 
    } 
    public void focusGained(FocusEvent e){//��ý����ʱ��,�����ʾ���� 
        String temp = jtf.getText(); 
        if(temp.equals(info)){ 
            jtf.setText(""); 
        } 
    } 
    public void focusLost(FocusEvent e) {//ʧȥ�����ʱ��,�ж����Ϊ��,����ʾ��ʾ���� 
        String temp = jtf.getText(); 
        if(temp.equals("")){ 
            jtf.setText(info); 
        } 
   }
>>>>>>> origin/master
}