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

import java.math.BigDecimal; //��������� 
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
 * һ����������֧�ּ򵥵ļ���
 */  
public class Calculator extends JFrame implements ActionListener {  
	/***************************************************************************************************************/
	/********��������ذ������Ƽ���ť����*******/
	
	/*������ģʽ���л���������ʾ*/
    private final String[] MODEL = {"����","��ѧ��","ͳ����","��������"};
    /*�����������ϵļ�����ʾ���� */  
    private final String[] KEYS = {"(",")", "C","��","7", "8", "9", "+" , "4", "5", "6",  
            "-", "1", "2", "3", "*", "0", ".", "=", "/" }; 
    /*��ѧ�ͼ������ϵĺ���ͼ����ʾ����*/ 
    private final String[] FUNCTION = { "1/x", "��2", "x^y", "x!", "sin","cos","tan","��" ,
    		"In","lg","log","%","e^x","e","��",","};
    /*ͳ���ͼ����������⹦��ͼ����ʾ����*/
    private final String[] SPECIAL_FUNTION = { "����", "���","D", "Ave","B",
    		"P(��)","��",","};
    /*��������������ϵļ�����ʾ����*/
    private final String[] SUPPLEMENT={"��","Ů","ȷ��","Back"};
    
    /*���������ť����ĳ��ȣ��������Ǵ�����Ӧ������*/
    int modelLen=MODEL.length;//���ڴ��MODEL�����а�ť����
    int keysLen=KEYS.length;//���ڴ��KEYS�����еİ�ť����
    int functionLen=FUNCTION.length;//���ڴ��FUNCTION�����еİ�ť����
    int special_functionLen=SPECIAL_FUNTION.length;//���ڴ��SPECIAL_FUNTION �����еİ�ť����
    int supplementLen=SUPPLEMENT.length;//���ڴ�Ž�������ģ���а�ť������
   
    /*ģʽ�л��ϵļ�����*/
    private JButton models[] = new JButton[keysLen];
    /*�������ı��� */  
    private JTextField resultText = new JTextField("");//������Ĭ�ϵ�0
    /*��������ı��� */  
    private JTextField heightText = new JTextField();
    /*���������ı��� */  
    private JTextField weightText = new JTextField();
    /*���������ı��� */  
    private JTextField ageText = new JTextField();
    /*�򵥼������ϼ��İ�ť */  
    private JButton keys[] = new JButton[keysLen];  
    /*�������Ϲ���ͼ����ʾ����*/  
    private JButton function[] = new JButton[functionLen+keysLen]; 
    /*�������ϳ��ú����İ�ť*/
    private JButton special_function[] = new JButton[special_functionLen+keysLen];
    /*�������Ľ�������ģ���ϵİ�ť*/
    private JButton supplements[]=new JButton[supplementLen];
    
    /*****************************************************************************************************/
    /****/
    
    /*Ϊ��ʵ�����ȼ�������*/
    //��׺ʽջ
    private Stack<String> postfixStack  = new Stack<String>();
    //�����ջ	
    private Stack<Character> opStack  = new Stack<Character>();
    //���������ASCII��-40����������������ȼ�
    private int [] operatPriority  = new int[] {0,3,2,1,-1,1,0,2}; 
    
    /*Ϊ��ʵ�ֿ�ѧ���㼰���������������*/
    private double  baseNum = 1;    //�������x^yʱx��ֵ
    private String   expression = "("; //��ſ�ѧ����ʱ��ȥ������ı��ʽ�������sin��5+12*(3+5)/7��ʱ��expression=5+12*(3+5)/7
    private boolean isFunction = false;   //�Ƿ��ǿ�ѧ����
    private boolean isSpecialFunction = false;   //�Ƿ���ͳ�Ƽ���
    private String   opreator = ""; //��ſ�ѧ����ʱ�Ĳ����� 
    private Stack<String> numStack  = new Stack<String>();//�������Ķ��������
    
    /*��������ģʽ�µĹ���*/

    //���ü���panel,Panel1��ʾ���ͼ������Ľ��棬panel2��ʾ��ѧ�ͼ������Ľ��棬panel3��ʾͳ���ͼ������Ľ��棬panel4��ʾ�������߼������Ľ���
    //topPanel������ı���resultPanel��Ϊ�˷���������ع���Ľ��
    JPanel panel,panel1,panel2,panel3,panel4,panel5,topPanel,resultPanel;  
    //���ü���String���͵ı������ڽ���û�оۼ�����Ӧ�ı�����ʱ��Ĭ����ʾ
    String Sex="";//��¼ѡ��������Ի���Ů��
    //weightText,heightText,ageText�ĳ�ʼ����Ϣ
    String info1="�������������(��)";
    String info2="������������(��)";
    String info3="�������������(ǧ��)";
    /*Ϊ��ʵ��BMI-�����ʼ������õ�label*/
    JLabel labeltop,label1,label2,label3,label4,label5;
    //���õ�ѡ���Է���ѡ����Ů
    Checkbox cRadio1,cRadio2;
    CheckboxGroup c;
    /*Ϊ��ʵ���˶�ǿ�ȵ�ѡ��������*/
    private JComboBox comboBox;
    //ageΪ�˼�¼���������е����䣬sexΪ�˼�¼��Ů������Ϊ1��Ů��Ϊ0
    int age,sex;
    //weight��heightΪ�˼�¼���������е���ߣ�����
	double weight,height;
	//����˹�ǿ��
	String [] NAMES;
	//����ָ����ʾ�еĸ���label�Լ����ذ�ť
	JLabel shapeLabel,idealLabel,bmiLabel, rateLabel,bfrLabel,
	       baseLabel,needLabel,onLabel,loseLabel,attentionLabel;
	//С����ʽ��������λС��
	DecimalFormat df= new DecimalFormat("0.000000");   
	DecimalFormat dfp= new DecimalFormat("0.000"); 
	DecimalFormat dfs= new DecimalFormat("0.0000"); 
	DecimalFormat dft= new DecimalFormat("0.00"); 
	//��¼������Ի���Ů�ԣ���������
	boolean isMan=false,isWoman=false;
    
   /** 
    ************ ���캯�� *************************************************************************
    *********************/  
    public Calculator() {  
        super();  
        // ��ʼ��������  
        init();  
        // ���ü������ı�����ɫ  
        this.setTitle("Calculator");  
        // ����Ļ(500, 300)���괦��ʾ������  
        this.setLocation(500, 300);  
        // �����޸ļ������Ĵ�С  
        this.setResizable(false);              
        //���ü�������С  
        this.setSize(400, 600); 
    }
  
    /** 
     *******��ʼ�������� ***************************************************************************************** 
     *�������Ļ�������
     */ 
    
    private void init() {   	
        // �ı����е����ݲ����Ҷ��뷽ʽ  
        resultText.setHorizontalAlignment(JTextField.RIGHT);  
        // �����޸Ľ���ı���  
        resultText.setEditable(true);            
        // �����ı��򱳾���ɫΪ��ɫ  
        resultText.setBackground(Color.white);
        //ȥ���ı���ı߿�
        resultText.setBorder(new EmptyBorder(5,5,5,10));
        //�����ı���Ĵ�С
        resultText.setSize(100,200);
        //�����ı���������ʽ
        resultText.setFont(new Font("΢���ź�", Font.PLAIN, 20));
        resultText.setForeground(new Color(139,126,102 )); 
        /*Ϊ���������е��ı����ʼ���Լ�����������ʽ*/
        ageText.setText(info1);
        ageText.setFont(new Font("΢���ź�", Font.PLAIN, 14));
        ageText.setForeground(new Color(139,126,102 ));
        heightText.setText(info2);
        heightText.setFont(new Font("΢���ź�", Font.PLAIN, 14));
        heightText.setForeground(new Color(139,126,102 )); 
        weightText.setText(info3);
        weightText.setFont(new Font("΢���ź�", Font.PLAIN, 14));
        weightText.setForeground(new Color(139,126,102 ));
        /*Ϊ�ı�����ӽ����¼���Ӧ��*/
        ageText.addFocusListener(new MyFocusListener(info1, ageText));
        heightText.addFocusListener(new MyFocusListener(info2, heightText));//��ӽ����¼���ӳ 
        weightText.addFocusListener(new MyFocusListener(info3, weightText));
        
        //ģ���л����ĸ���ť����һ����������modelPanel������
        JPanel modelPanel = new JPanel();
        //����modelPanel����İ�ť����������,ˮƽ�����Լ��䱳��ɫ��
        FlowLayout fl = new FlowLayout(); 
        fl.setAlignment(FlowLayout.LEFT); 
        modelPanel.setLayout(fl);
        modelPanel.setBackground(new Color(39,42,47 ));
        for (int i = 0; i < modelLen; i++) {  
            models[i] = new JButton(MODEL[i]);  
            //ȥ��modelsPanel�����button�ı߽���
            models[i].setBorderPainted(false);
            //ȥ��button���ʱ�����ϳ��ֵı߽��
            models[i].setFocusPainted(false);
            //����button��������ĸ�ʽ
            models[i].setFont(new Font("΢���ź�", Font.PLAIN, 18));
            models[i].setForeground(Color.white);
            models[i].setBackground(new Color(39,42,47 ));
            modelPanel.add(models[i]); 

        }  
        
       // ����һ��������ı���  
       topPanel = new JPanel();  
       topPanel.setLayout(new BorderLayout(0,0));  
       topPanel.add("Center", resultText); 
      
       //����һ������Panel�����ģʽ�л�����ʾ���
       JPanel top = new JPanel();  
       top.setLayout(new GridLayout(2,1,0,0));  
       top.add(modelPanel); 
       top.add(topPanel);
      
       // ��ʼ�����������������֡����㡢���š�����ȼ��İ�ť����������һ������calkeysPanel������  
       JPanel calckeysPanel = new JPanel();  
       // �����񲼾�����4�У�4�е���������֮���ˮƽ������Ϊ3�����أ���ֱ������Ϊ3������  
       calckeysPanel.setLayout(new GridLayout(5, 4, 15, 15)); 
       calckeysPanel.setBackground(Color.black);
       for (int i = 0; i < keysLen; i++) {  
            keys[i] = new RaButton(KEYS[i]);  
            calckeysPanel.add(keys[i]);  
       }     

       // �½�һ����Ļ��壬��calckeys������ڸû�����  
       panel1 = new JPanel();  
       // ������ñ߽粼�ֹ����������������֮���ˮƽ�ʹ�ֱ�����ϼ����Ϊ0����  
       panel1.setLayout(new BorderLayout(0,0));   
       panel1.setBackground(new Color(54,54,54   ));
       JLabel space=new JLabel();
       panel1.add("North",space);
       panel1.add("Center", calckeysPanel);
        
       // ��ʼ����ѧ�������������ܼ����� �����������ܺͿ�ѧ���������ܼ�����һ��������  
       JPanel functionsPanel = new JPanel();  
       // �����񲼾ֹ�������5�У�7�е���������֮���ˮƽ������Ϊ0�����أ���ֱ������Ϊ0������  
       functionsPanel.setLayout(new GridLayout(9, 4,15,5));
       functionsPanel.setBackground(Color.black);
       //����һЩ��ѧ����İ�ť
       for (int i = 0; i < functionLen; i++) {  
            function[i] = new RaButton(FUNCTION[i]);   
            functionsPanel.add(function[i]); 
       } 
       //������������Լ����ְ�ť
       for (int i = functionLen; i < (keysLen+functionLen); i++) {  
        	function[i] = new RaButton(KEYS[i-functionLen]); 
        	functionsPanel.add(function[i]);  
       }  
 
       // �½�һ����Ļ��壬��function������ڸû����ڣ�ʵ�ֿ�ѧ�������Ľ���
       panel2=new JPanel();
       panel2.setLayout(new BorderLayout(0,0));
       panel2.setBackground(new Color(54,54,54   ));
       panel2.add("Center",functionsPanel);
        
       // ��ʼ�����������⺯�����ܼ����������������ܼ�����һ��������  
       JPanel special_functionsPanel = new JPanel();  
       // �����񲼾ֹ�������7�У�4�е���������֮���ˮƽ������Ϊ0�����أ���ֱ������Ϊ0������  
       special_functionsPanel.setLayout(new GridLayout(7, 4, 10, 10)); 
       special_functionsPanel.setBackground(Color.black);
       //����ͳ������Ĺ��ܰ�ť
       for (int i = 0; i < special_functionLen; i++) {  
        	special_function[i] = new RaButton(SPECIAL_FUNTION[i]);   
        	special_functionsPanel.add(special_function[i]);   
       }  
       //������������Լ����ְ�ť
       for (int i = special_functionLen; i < (keysLen+special_functionLen); i++) {  
        	special_function[i] = new RaButton(KEYS[i-special_functionLen]);   
        	special_functionsPanel.add(special_function[i]);  
       }  
       //�½�һ����Ļ��壬��special_function������ڸû����ڣ�ʵ��ͳ�Ƽ������Ľ���
       panel3=new JPanel();
       panel3.setLayout(new BorderLayout(0,0));
       panel3.setBackground(new Color(54,54,54   ));
       panel3.add("Center",special_functionsPanel);
        
       //��ʼ����������չģʽ������չ���ܵĺ�������һ��panel��        
       //sexPanel����Ա��������ʽ�O
       JPanel sexPanel=new JPanel();
       sexPanel.setLayout(new FlowLayout(FlowLayout.CENTER,20,10));
       //label1����Ա�ѡ��
       label1=new JLabel("�������Ա�");
       label1.setFont(new Font("΢���ź�", Font.PLAIN, 18));
       label1.setForeground(Color.white); 
       //sexPanel����Ա�
       sexPanel.add(label1);
       sexPanel.setBackground(new Color(39,42,47 ));
       //sex����Ա�ǰ����ʾ
       JPanel sex=new JPanel();
       sex.setLayout(new GridLayout(1,2,10,0));
       sex.setBackground(new Color(39,42,47 ));
       for (int i = 0; i < 2; i++) {  
        	supplements[i] = new sexButton(SUPPLEMENT[i]); 
            //����button���������С
            supplements[i].setFont(new Font("΢���ź�", Font.PLAIN, 18));
        	sex.add(supplements[i]);  
        }  
        sexPanel.add(sex);
        //agePanel�������
        JPanel agePanel=new JPanel();
        agePanel.setLayout(new FlowLayout(FlowLayout.CENTER,20,10));
        agePanel.setBackground(new 	Color(39,42,47 ));
        //label2������������
        label2=new JLabel("����������");
        label2.setFont(new Font("΢���ź�", Font.PLAIN, 18));
        label2.setForeground(Color.white); 
        ageText.setBorder(new EmptyBorder(5,5,5,10));//ȥ�ı���߿�
        agePanel.add(label2);
        agePanel.add(ageText);
        
        //heightPanel������
        JPanel heightPanel=new JPanel();
        heightPanel.setLayout(new FlowLayout(FlowLayout.CENTER,20,10));
        heightPanel.setBackground(new Color(39,42,47 ));
        heightText.setBorder(new EmptyBorder(5,5,5,10));//ȥ�ı���߿�
        //label3�����������
        label3=new JLabel("���������");
        label3.setFont(new Font("΢���ź�", Font.PLAIN, 18));
        label3.setForeground(Color.white); 
        heightPanel.add(label3);
        heightPanel.add(heightText);
        
        //weightPanel�������
        JPanel weightPanel=new JPanel();
        weightPanel.setLayout(new FlowLayout(FlowLayout.CENTER,20,10));
        weightPanel.setBackground(new Color(39,42,47 ));
        label4=new JLabel("����������");
        label4.setFont(new Font("΢���ź�", Font.PLAIN, 18));
        label4.setForeground(Color.white); 
        weightText.setBorder(new EmptyBorder(5,5,5,10));//ȥ�ı���߿�
        weightPanel.add(label4);
        weightPanel.add(weightText);
        
        //sportPanel����˶�ǿ��
        JPanel sportPanel=new JPanel();
        sportPanel.setBackground(new Color(39,42,47 ));
        sportPanel.setLayout(new FlowLayout(FlowLayout.CENTER,20,10));
        label5=new JLabel("��ѡ���˶��̶�");
        label5.setFont(new Font("΢���ź�", Font.PLAIN, 18));
        label5.setForeground(Color.white);  
        sportPanel.add(label5);
        //������
        NAMES= new String[5];
        NAMES[0] = "��ʽ���ʽ(�����˶�)";
        NAMES[1] = "��΢����ճ��";
        NAMES[2] = "�е�ǿ�Ƚ���(ÿ��3-4���˶�)";
        NAMES[3] = "��ǿ�Ƚ���(ÿ��4������)";
        NAMES[4] = "רҵ�˶�Ա(ÿ��6������)";
        //������ѡ�����ʽ����
        comboBox = new JComboBox(NAMES);
        comboBox.setEditable(false);
        comboBox.setFont(new Font("΢���ź�", Font.PLAIN, 14));
        comboBox.setForeground(new Color(0,0,0)); 
        sportPanel.add(comboBox);
        
        //submitPanel���ȷ�ϼ�
        JPanel submitPanel=new JPanel();
        submitPanel.setBackground(new 	Color(39,42,47 ));
        submitPanel.setLayout(new FlowLayout());
        supplements[2] = new sexButton(SUPPLEMENT[2]); 
        submitPanel.add(supplements[2]);
         
        //�½�һ���󻭰�Panel4,BMI-��֬�ʼ���
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
        
        //�½�һ��resultPanel,��ʵBMI�Ƚ���������
        resultPanel=new JPanel();
      
        //ʵ�����������label
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
        //����ָ�������ʾ�ĸ���label����ʽ����
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
        //���¼��㰴ť
        supplements[3] = new sexButton(SUPPLEMENT[3]); 
        //����ָ��panel��ʽ����
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
    	// ��ȡ�¼�Դ�ı�ǩ  
        String label = e.getActionCommand();
        //����ģʽѡ�񣺼��ͣ���ѧ�ͣ�ͳ���ͣ���չ�ͣ�
        if(label.equals(MODEL[0])){//����
        	panel.add("Center",panel1);
        	panel1.setVisible(true);
        	panel2.setVisible(false);
        	panel3.setVisible(false);
        	panel4.setVisible(false);
        	resultPanel.setVisible(false);
        	handleC();     
        }else if(label.equals(MODEL[1])){//��ѧ��
        	panel.add("Center",panel2);
        	panel1.setVisible(false);
        	panel2.setVisible(true);
        	panel3.setVisible(false);
        	panel4.setVisible(false);
        	resultPanel.setVisible(false);
        	handleC();     
        }else if(label.equals(MODEL[2])){//ͳ����
        	panel.add("Center",panel3);
        	panel1.setVisible(false);
        	panel2.setVisible(false);
        	panel3.setVisible(true);
        	panel4.setVisible(false);
        	resultPanel.setVisible(false);
        	handleC();     
        }else if(label.equals(MODEL[3])){//��������
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
        	    
        }else if (label.equals("=")) {  // �û�����"="��  ,���������������
        	    handleEqual();

        }else if (label.equals(KEYS[2])) { // �û�����"C"��  ����ʼ��������
        	    handleC();                     //������������� 
        	
        }else if (label.equals(KEYS[3])) {  //�û����ˡ������˻ؼ������ı������һ���ַ�ȥ��
            handleBackspace();
        	
        }else if ("0123456789.()+-*/,e��".indexOf(label) >= 0) {  // �û���������Ҫ����Ķ���ʽ
        	if(isFunction||isSpecialFunction){           //����ǿ�ѧ���㣬��sin������������Ķ���ʽ����һ���ַ���
        		expression = expression + label;
        	}
        	resultText.setText(resultText.getText()+label);

        }else if(label.equals(FUNCTION[0])||label.equals(FUNCTION[1])||//�����ѧ�ͼ��������ֹ���,����handleFunction()����
        		label.equals(FUNCTION[3])||label.equals(FUNCTION[11])){//����Ϊ��1/x,x^2,x!,%
            handleFunction(label);
            
        }else if(label.equals(SPECIAL_FUNTION[0])||label.equals(SPECIAL_FUNTION[1])||//�����ͳ�Ƽ�������ת��handleSpecialFunction����
        		label.equals(SPECIAL_FUNTION[2])||label.equals(SPECIAL_FUNTION[3])||
        		label.equals(SPECIAL_FUNTION[4])||label.equals(SPECIAL_FUNTION[5])||
        	    label.equals(SPECIAL_FUNTION[6])){
        	if(label.equals(SPECIAL_FUNTION[0])){//�û����ˡ����С���ť
     		    resultText.setText("C ("); 

        	}else if(label.equals(SPECIAL_FUNTION[1])){//�û����ˡ���ϡ���ť
     		    resultText.setText("A ("); 

        	}else if(label.equals(SPECIAL_FUNTION[5])){//�û����ˡ�P(��)����ť
     		    resultText.setText("P ("); 

        	}else {
     		    resultText.setText(label + "("); //�û���������ͳ�ƺ��������ģʽ��ͬ
        	}
        	isSpecialFunction = true;	
        	opreator = label;

        }else if(label.equals(SUPPLEMENT[0])){//����ѡ��Ϊ����
 		    Sex="��";
 		    isMan=true;
 		    if(isMan){
 		      supplements[0].setForeground(Color.RED);
 		      supplements[1].setForeground(Color.white);
 		      isMan=false;
 		    }

    	}else if(label.equals(SUPPLEMENT[1])){//����ѡ��ΪŮ��
 		    Sex="Ů";
		    isWoman=true;
		    if(isWoman){
		       supplements[0].setForeground(Color.white);
 		       supplements[1].setForeground(Color.RED);
 		       isWoman=false;
		    }
        	
        	
        }else if(label.equals(SUPPLEMENT[2])){//
        	String input;//�ж������Ƿ���ȫ
        	input=HandelNum();
        	if(input=="�������"){//������
                shapeLabel.setText(BMIInform());
                idealLabel.setText("��������:"+DBW()+"����");
                bmiLabel.setText("BMI(��������ָ��):"+BMI());
                rateLabel.setText("��������Σ����:"+DiseaseRate());
                bfrLabel.setText("��֬��:"+BFR()+"%");
                baseLabel.setText("������л��:"+BMR()+"��·��/��");
                needLabel.setText("ÿ����Ҫ����:"+dailyCalorie()+"��·��");
                onLabel.setText("��������:"+PutOn()+"��·��/��");
                loseLabel.setText("�������:"+LoseWeight()+"��·��/��");
                attentionLabel.setText("�����ڵ�֬����"+BFRInform());
                
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
      	    	resultText.setText("�����������");
      	    }
        	
        	
        }else if(label.equals(SUPPLEMENT[3])){//�û����ˡ�ȷ��������ת�������֬��
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
        	
        }else{//�û����˿�ѧ������������
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
  
    /*�����ɾ�������µ��¼� */  
    private void handleBackspace() {  
        String text = resultText.getText();  
        int i = text.length(); 
        int j = expression.length(); 

        if (i > 0) {  
            // �˸񣬽��ı����һ���ַ�ȥ��  
            text = text.substring(0, i - 1);  
            if (text.length() == 0) {  
                // ����ı�û�������ݣ����ʼ���������ĸ���ֵ  y  
                resultText.setText("");  
            } else {  
                // ��ʾ�µ��ı�  
                resultText.setText(text);  
            }  
        }  
        if(j>1){
            expression = expression.substring(0, j - 1);
            
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
	    expression = "(";
	    numStack.clear();
	    postfixStack.clear();
	    opStack.clear();
	    
        
    }
    
    /*����" = "�����µ��¼�*/
    private void handleEqual() {
        
    	double result = 0.0;// �н����ռ����� 
    	if(isFunction){
        	if((expression.charAt(expression.length()-1))!=')'){
        		expression = expression + ")";
        	}

    		handleFunction(opreator);
    	    isFunction = false;   //�Ƿ��ǿ�ѧ���㣬��sin,cos,lg��
    	    opreator = "";
    	}else if(isSpecialFunction){
    		
    		handleSpecialFunction(opreator);
    	    isSpecialFunction = false;   //�Ƿ���ͳ�Ƽ��㣬��A��N,M��,C(N,M)��
    	    opreator = "";
    	}else{
    		if((resultText.getText() )==""){//����û�û���������ݣ���������
    	      	resultText.setText("������������");

    		}else{
    			result = calculate(resultText.getText());//������
    	      	resultText.setText(resultText.getText()+" = "+result);//������
    		}
	      	
    	}
    	
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
  	/** 
     *��ѧ�͡������� �����ѧ����������������µ��¼� 
     */  
    private void handleFunction(String key) { 
    	double resultNum = 0;  
        if (key.equals(FUNCTION[0])) {  
        	//����һ�����ĵ���
        	if(getNumberFromText()==0){   //�ж�Ҫ��������Ƿ�Ϊ��
        		resultText.setText("0û�е���");
        	}else{
        		resultNum = 1/getNumberFromText();
        		resultNum=Double.valueOf(df.format(resultNum));
        	    resultText.setText("1/"+getNumberFromText()+"="+resultNum);
        		 
        	}
        	
        }else if(key.equals(FUNCTION[1])){
        	//����һ������ƽ��
        	resultNum = getNumberFromText()*getNumberFromText();
        	resultNum=Double.valueOf(df.format(resultNum));
            resultText.setText(getNumberFromText()+"^(2) = "+resultNum);
           
        }else if(key.equals(FUNCTION[2])){
        	//����x��y�η�
        	resultNum = calculate(expression);
        	resultNum = Math.pow(baseNum,resultNum);
        	 resultNum=Double.valueOf(df.format(resultNum));
             resultText.setText(resultText.getText()+" = "+resultNum);
        }else if(key.equals(FUNCTION[3])){
        	  //�׳�����  
        	try{
        		double num = Double.valueOf(resultText.getText());
        		resultText.setText(num+"!= "+factorial0(num));
              	}catch(NumberFormatException e2){
       		 	resultText.setText("����������");
        	} 
        	
        }else if (key.equals(FUNCTION[4])){
        	// ���Ǻ���sin()
        	resultNum = calculate(expression);
        	resultNum = Math.sin(resultNum);
        	resultNum=Double.valueOf(df.format(resultNum));
            resultText.setText(resultText.getText()+" = "+resultNum);
        	
        }else if (key.equals(FUNCTION[5])) {  
        	 // ���Ǻ���cos() 
        	resultNum = calculate(expression);
        	resultNum = Math.cos(resultNum);
        	resultNum=Double.valueOf(df.format(resultNum));
            resultText.setText(resultText.getText()+" = "+resultNum);        	
        }else if (key.equals(FUNCTION[6])){
        	 //���Ǻ���tan()
        	resultNum = calculate(expression);
            resultNum = Math.tan(resultNum);
            resultNum=Double.valueOf(df.format(resultNum));
            resultText.setText(resultText.getText()+" = "+resultNum);        	
        	
        }else if (key.equals(FUNCTION[7])) {  
        	// ƽ��������  
        	resultNum = calculate(expression);
        	if(resultNum<=0){
        		resultText.setText("����������");
            }else{
            	resultNum = Math.sqrt(resultNum); 
            	resultNum=Double.valueOf(df.format(resultNum));
                resultText.setText(resultText.getText()+" = "+resultNum);        	
        	}
                	
        }else if (key.equals(FUNCTION[8])) {  
        	//������Ȼ����
        	resultNum = calculate(expression);
        	resultNum = Double.valueOf(Log(Math.E,resultNum));
        	resultNum=Double.valueOf(df.format(resultNum));
            resultText.setText(resultText.getText()+" = "+resultNum);        	
        
        }else if (key.equals(FUNCTION[9])) {  
        	//������10Ϊ�׵Ķ���
        	resultNum = calculate(expression);
        	resultNum =Double.valueOf(Log(10,resultNum));
        	resultNum=Double.valueOf(df.format(resultNum));
            resultText.setText(resultText.getText()+" = "+resultNum);        	
        }else if (key.equals(FUNCTION[10])) {  
        	//����������Ϊ�׵ĺ���
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
        	//ȡ�ٷֺ�����
        	resultNum = getNumberFromText()/100;  
        	resultNum=Double.valueOf(df.format(resultNum));
            resultText.setText(resultText.getText()+"% = "+resultNum);        	
        	
        }else if (key.equals(FUNCTION[12])){
        	//������Ȼ����e��x�η�
        	resultNum = calculate(expression);
       	    resultNum = Math.exp(resultNum);//��ʾ�е�����
       	    resultNum=Double.valueOf(df.format(resultNum));
            resultText.setText(resultText.getText()+" = "+resultNum);        	
        	
        }
    }  
    
    /*
     * ����׳�
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
    	    BigDecimal num=new BigDecimal(String.valueOf(factorial));//����BigDecimalʱָ����Ч����s
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
     * ��������׳�
     * @param x
     * @return
     */
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
   private String Log(double x,double y){
    	if(x<=0){
    		return "������Ϊ����";
    	}
    	if(y<=0){
    		return "������Ϊ����";
        }
    	if(x==1){
    		return "��������Ϊ1";
        }
    	else{
	    	double log = Math.log(y)/Math.log(x);
	    	return String.valueOf(log);
    	}
    	
   }
   
  
  
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
	       resultNum =Combination(n,m);
	       try {  
	    	   int resultC = Integer.valueOf(resultNum);
			   //���������
		   	   if((expression.charAt(expression.length()-1))!=')'){
		   	       resultText.setText(resultText.getText()+")  = "+resultNum);//�û�û�а�")"�����ʱʹ���Զ�����
		       }else{
		           resultText.setText(resultText.getText()+"  = "+resultNum);
		       }    
           } catch (NumberFormatException e) { 
               resultText.setText(resultNum);    

           }
		   
	   }else if(label.equals(SPECIAL_FUNTION[1])){//����������
		   m = Integer.valueOf(numStack.pop());
	       n = Integer.valueOf(numStack.pop());
	       resultNum =Arrangement(n,m);
	       try {  
	    	   int resultA = Integer.valueOf(resultNum);
			   //���������
		   	   if((expression.charAt(expression.length()-1))!=')'){
		   	       resultText.setText(resultText.getText()+")  = "+resultNum);//�û�û�а�")"�����ʱʹ���Զ�����
		       }else{
		           resultText.setText(resultText.getText()+"  = "+resultNum);
		       }    
           } catch (NumberFormatException e) { 
               resultText.setText(resultNum);    

           } 
	   }else if(label.equals(SPECIAL_FUNTION[2])){//���㷽��
		   resultNum = df.format(Variance());
			 //���������
	   	   if((expression.charAt(expression.length()-1))!=')'){
	   	       resultText.setText(resultText.getText()+")  = "+resultNum);//�û�û�а�")"�����ʱʹ���Զ�����
	       }else{
	           resultText.setText(resultText.getText()+"  = "+resultNum);
	       }
		   
	   }else if(label.equals(SPECIAL_FUNTION[3])){//����ƽ��ֵ
		   resultNum = df.format(Ave());
			 //���������
	   	   if((expression.charAt(expression.length()-1))!=')'){
	   	       resultText.setText(resultText.getText()+")  = "+resultNum);//�û�û�а�")"�����ʱʹ���Զ�����
	       }else{
	           resultText.setText(resultText.getText()+"  = "+resultNum);
	       }

	   }else if(label.equals(SPECIAL_FUNTION[4])){//�������ֲ�
		   resultNum = dfs.format(BiDistribution());
			 //���������
	   	   if((expression.charAt(expression.length()-1))!=')'){
	   	       resultText.setText(resultText.getText()+")  = "+resultNum);//�û�û�а�")"�����ʱʹ���Զ�����
	       }else{
	           resultText.setText(resultText.getText()+"  = "+resultNum);
	       }
	   }else if(label.equals(SPECIAL_FUNTION[5])){//���㲴�ɷֲ�
		   //���������λС��
		   resultNum = dfp.format(PoDistribution());
           //���������
	   	   if((expression.charAt(expression.length()-1))!=')'){
	   	       resultText.setText(resultText.getText()+")  = "+resultNum);//�û�û�а�")"�����ʱʹ���Զ�����
	       }else{
	           resultText.setText(resultText.getText()+"  = "+resultNum);
	       }
	   }else if(label.equals(SPECIAL_FUNTION[6])){//�����׼��̫�ֲ�
		   double result;
		   result=StandardDistribution();
		   if(result==-2){
			   resultText.setText("��������̫С");
		   }else if(result==-1){
			   resultText.setText("��������̫��");
		   }else{
			   resultNum=dfs.format(result);   
			 //���������
		   	   if((expression.charAt(expression.length()-1))!=')'){
		   	       resultText.setText(resultText.getText()+")  = "+resultNum);//�û�û�а�")"�����ʱʹ���Զ�����
		       }else{
		           resultText.setText(resultText.getText()+"  = "+resultNum);
		       }		   }
	   }
	   
       
   } 

   /** 
    * ������Ϲ�ʽ ����
    * @param n,m
    * @return 
    */  
  
   private String Combination(int n,int m){
	   
	   if(n<=0||m<=0){
	   		return "n,m��Ϊ����";
	    }else{
		    if(m>n){
		  		return "n������ڻ����m";
		  	}else{
			   	int c = (int)(factorial(n)/(factorial(m)*factorial(n-m)));
			   	return (String.valueOf(c));
			}
	    }
  	
  }

   /** 
    * �������й�ʽ ����
    * @param n,m
    * @return 
    */  
   private String Arrangement(int n,int m){
  		
    	if(n<=0||m<=0){
	   		return "n,m��Ϊ����";
        }else{
	   	    if(m>n){
		  		return "n������ڻ����m";
	   	    }else{
	    	  int a =(int) (factorial(n)/factorial(n-m));
	    	  return (String.valueOf(a));
	        }
     }
   }
   /*
    * ����ƽ����
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
    * ���㷽��
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
	   P=(factorial(n)/(factorial(k)*factorial(n-k)))*(Math.pow(P,k))*(Math.pow(1-P,(n-k)));
	   return P;
	
   }
   /**
    * ���ɷֲ���ֵ
    * @param ��,k   ÿ�η�������Ϊ�ˣ���������ΪK
    */
   private double PoDistribution(){
	   int k;
	   float ��;
	   double p,sum=0;
	   Stack<String> numstack  = (Stack<String>)numStack.clone();
	   k=Integer.valueOf(numstack.pop());
	   ��=Float.valueOf(numstack.pop());
	   for(int i=0;i<=k;i++){
	     p=(Math.pow(��,i)*Math.pow(Math.E,-��))/factorial(i);
	     sum+=p;
	   }
	   return sum;
	
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
   
   /*����ָ����ʾǰ�����ݴ���*/
   private  String HandelNum(){
       try {  
    	   
    	   weight = Double.valueOf(weightText.getText()); //��������л������
    	   height = Double.valueOf(heightText.getText());//��������л�����
		   age = Integer.valueOf(ageText.getText());
       } catch (NumberFormatException e) { 
	   		return("����������ߡ����ء�����");
       } 
       //�ж��Ա�
	   if(Sex==""){
		   return("������������");
	   }else if(Sex=="��"){//���������������Ϊ1
		   sex = 1;
		   return("�������");
       }else{//Ů����0
    	   sex = 0;
    	   return("�������");
       }

   }
   /*������������*/
   private  String BMIInform(){
	   //����BMI ��ֵ
	   double bmi=BMI();
	   	//�ֵȼ����
	   	if(bmi<18.5){
	   		return("�����ع���");
	   	}else if(bmi>=18.5&&bmi<23.9){
	   		return("��������������Χ");
	   	}else {
	   		if(bmi>=23.9&&bmi<27.9){
	   			return("��Ŀǰ���ش��ڷ���ǰ��,ע�����Ŷ");
	   	    }else if(bmi>=27.9&&bmi<29.9){
	   	    	return("��Ŀǰ���ش���I�ȷ���,ע���ǿ�˶�Ŷ");
	   	    }else if(bmi>=29.9&&bmi<39.9){
	   	    	return("��Ŀǰ���ش���II�ȷ���,ע���ǿ�˶�Ŷ");
	   	    }else{
	   	    	return("��Ŀǰ���ش���,III�ȷ���,ע���ǿ�˶�Ŷ");
	   	    }
	   }
	   
   }
   
   /*�����������أ�DBW��:desirable body weight;*/
   private  double DBW(){
	   double bmi=22;
       return(Double.valueOf(dft.format(bmi*height*height)));
	   
   }
   /*����BMI*/
   private double BMI(){
	   double bmi;
       bmi = weight/(height*height);
       return(Double.valueOf(dft.format(bmi)));
   }
   /*���㼲��������*/
   private String DiseaseRate(){
	   double bmi=BMI();
		//�ֵȼ����
	   	if(bmi<18.5){
	   		return("��(����������Σ��������)");
	   	}else if(bmi>=18.5&&bmi<23.9){
	   		return("ƽ��ˮƽ");
	   	}else {
	   		if(bmi>=23.9&&bmi<27.9){
	   			return("����");
	   	    }else if(bmi>=27.9&&bmi<29.9){
	   	    	return("�ж�����");
	   	    }else if(bmi>=29.9&&bmi<39.9){
	   	    	return("��������");
	   	    }else{
	   	    	return("�ǳ���������");
	   	    }
	   }
	   
   }
   /*��������֬����*/
   private double BFR(){
	   double bmi,bfr;
	   bmi=BMI();
       bfr = 1.2*bmi+0.23*age-5.4-10.8*sex; 
       return(Double.valueOf(dft.format(bfr)));
   }
   
   /*������л�ʣ�BMR��:Basal metabolic rate*/
   private double BMR(){
	   double bmr;
   	   //���������л��
       if(sex==1){
    	   bmr = 661+9.6*weight+1.72*height-4.7*age ;  
       }else{
    	   bmr = 67+13.73*weight+5*height-6.9*age ;  
       }
       return(Double.valueOf(dft.format(bmr)));
   }
   
   //ÿ�����迨·��
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
   /*�������������*/
   private String PutOn(){
	   double bottom,top;
	   bottom=dailyCalorie()+500;
	   top=dailyCalorie()+1000;
	   return(dft.format(bottom)+"~"+dft.format(top));
   }
   /*�������������*/
   private String LoseWeight(){
	   double bottom,top;
	   bottom=dailyCalorie()-1000;
	   top=dailyCalorie()-500;
	   return(dft.format(bottom)+"~"+dft.format(top));
   }
   
   /*��������֬����*/
   private String BFRInform(){
	   double bfr;
	   bfr=BFR();
       if(sex==1){//����
		   if(bfr<10){
			   return("����");
		   }else if(bfr>=10&&bfr<14){
			   return("�����������Ҫ����");
		   }else if(bfr>=14&&bfr<21){
			   return("�ﵽ���˶�Ա��Ҫ��");
		   }else if(bfr>=21&&bfr<25){
			   return("���ú���,��������� ");
		   }else if(bfr>=25&&bfr<=31){
			   return("�е�ƫ�ߣ������пɽ���");
		   }else{
			   return("���ߣ���ǿ�˶���");
		   }
	   }else{
		   if(bfr<2){
			   return("����");
		   }else if(bfr>=2&&bfr<6){
			   return("�����������Ҫ����");
		   }else if(bfr>=6&&bfr<14){
			   return("�ﵽ���˶�Ա��Ҫ��");
		   }else if(bfr>=14&&bfr<18){
			   return("���ú���,���������");
		   }else if(bfr>=18&&bfr<=25){
			   return("�е�ƫ�ߣ������пɽ���");
		   }else{
			   return("���ߣ���ǿ�˶���");
		   }
	   }
	   
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
   
    /**
     * ��������ʽ�л�ȡ����
     * 
     */
    private void handleLabel(JLabel label){
    	 label.setFont(new Font("΢���ź�", Font.PLAIN, 14));
         label.setForeground(Color.WHITE);
    }

  /********���࣬����һ��Calculator����****************************************************************************************************/
    public static void main(String args[]) {  
        Calculator calculator = new Calculator(); 
        calculator.setVisible(true);  
        calculator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     
   } 
}

//ʵ���ı��������������ı����ı�����ճ�ʼ������
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
}

//ʵ��buttonԲ���Լ���������Ƴ�ʱ���Ч��
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
       setForeground(BUTTON_FOREGROUND_COLOR);//ǰ����ɫ��������ɫ
       setFocusPainted(false);
       setContentAreaFilled(false);//͸��
       addMouseListener(new MouseAdapter() {//�����Ƴ�Ч��
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
       //ʵ����ɫ����
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

//ʵ��sexButton��Բ�ǣ���ȥ�������Ƴ���ɫ�仯���ܣ��̳�����һ��RaButton
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
         setForeground(BUTTON_FOREGROUND_COLOR);//ǰ����ɫ��������ɫ
         setFocusPainted(false);
         setContentAreaFilled(false);//͸��

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
      //ʵ����ɫ����
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
 
 
