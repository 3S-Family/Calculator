import java.awt.Color;  
import java.awt.GridLayout;  
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;  
import java.awt.event.ActionListener; 
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
 
/** 
 * һ����������֧�ּ򵥵ļ���
 */  
public class Calculator2 extends JFrame implements ActionListener {  
	/***************************************************************************************************************/
	/********��������ذ������Ƽ���ť����*******/

	
/********��������ذ������Ƽ���ť����*******/
	
	/*������ģʽ���л���������ʾ*/
    private final String[] MODEL = {"����","��ѧ��","ͳ����","��չ��"};
    /*�����������ϵļ�����ʾ���� */  
    private final String[] KEYS = {"(",")", "C","��","7", "8", "9", "+" , "4", "5", "6",  
            "-", "1", "2", "3", "*", "0", ".", "=", "/" }; 
    /*��ѧ�ͼ������ϵĺ���ͼ����ʾ����*/ 
    private final String[] FUNCTION = { "1/x", "x^2", "x^y", "x!", "��" ,"��(n&x)",
    		"sin","cos","tan","In","lg","log","e^x","e","��"};
    /*ͳ���ͼ����������⹦��ͼ����ʾ����*/
    private final String[] SPECIAL_FUNTION = { "C_n^m", "A_n^m","s^2", "ave" };
    /*��չ�ͼ������ϵļ�����ʾ����*/
    private final String[] SUPPLEMENT={"BMI","BFR","$","����"};
 
    /*���������ť����ĳ��ȣ��������Ǵ�����Ӧ������*/
    int modelLen=MODEL.length;//���ڴ��MODEL�����а�ť����
    int keysLen=KEYS.length;//���ڴ��KEYS�����еİ�ť����
    int functionLen=FUNCTION.length;//���ڴ��FUNCTION�����еİ�ť����
    int special_functionLen=SPECIAL_FUNTION.length;//���ڴ��SPECIAL_FUNTION �����еİ�ť����
    int supplementLen=SUPPLEMENT.length;//���ڴ����չģ���а�ť������
    
    /*ģʽ�л��ϵļ�����*/
    private JButton models[] = new JButton[keysLen];
    /*�������ı��� */  
    private JTextField resultText = new JTextField("");//������Ĭ�ϵ�0
    /*��������ı��� */  
    private JTextField heightText = new JTextField("������������");
    /*���������ı��� */  
    private JTextField weightText = new JTextField("�������������");
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
    /*****************************************************************************************************/
    /****/
    
    private Stack<String> postfixStack  = new Stack<String>();//��׺ʽջ
    private Stack<Character> opStack  = new Stack<Character>();//�����ջ
    private int [] operatPriority  = new int[] {0,3,2,1,-1,1,0,2};//���������ASCII��-40����������������ȼ� 
    
    //���ü���panel,Panel1��ʾ���ͼ������Ľ��棬panel2��ʾ��ѧ�ͼ������Ľ��棬panel3��ʾͳ���ͼ������Ľ��棬panel4��ʾ��չ�ͼ������Ľ���
    JPanel panel,panel1,panel2,panel3,panel4;
    //���õ�ѡ���Է���ѡ����Ů
    Checkbox cRadio1,cRadio2;
    CheckboxGroup c;
    double baseNum=1;
    boolean expo=false;
    
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
        
        // ��ʼ����ѧ�������������ܼ����������������ܺͿ�ѧ���������ܼ�����һ��������  
        JPanel functionsPanel = new JPanel();  
        // �����񲼾ֹ�������5�У�7�е���������֮���ˮƽ������Ϊ0�����أ���ֱ������Ϊ0������  
        functionsPanel.setLayout(new GridLayout(5, 7,0,0));
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
 
        // �½�һ����Ļ��壬��function������ڸû����ڣ�ʵ�ֿ�ѧ�������Ľ���
        panel2=new JPanel();
        panel2.setLayout(new BorderLayout());
        panel2.setBackground(new Color(54,54,54   ));
        panel2.add("Center",functionsPanel);
        
        // ��ʼ�����������⺯�����ܼ����ú�ɫ��ʾ���������������ܼ�����һ��������  
        JPanel special_functionsPanel = new JPanel();  
        // �����񲼾ֹ�������6�У�4�е���������֮���ˮƽ������Ϊ0�����أ���ֱ������Ϊ0������  
        special_functionsPanel.setLayout(new GridLayout(6, 4, 0, 0)); 
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
        //���õ�ѡ��    
        c=new CheckboxGroup();
        cRadio1=new Checkbox("����",c,false);
        cRadio2=new Checkbox("Ů��",c,true);
        //�½�һ�����壬������������������
        JPanel panel_4=new JPanel();
        panel_4.setLayout(new GridLayout(2,2));
        panel_4.add(heightText);
        panel_4.add(weightText);
        panel_4.add(ageText);
        panel_4.add(cRadio1);
        panel_4.add(cRadio2);
        //�½�һ����Ļ��壬��supplement������ڸû����ڣ�ʵ����չ�������Ľ���
        panel4=new JPanel();
        panel4.setLayout(new BorderLayout(3,3));
        panel4.setBackground(new Color(54,54,54   ));
        panel4.add("North", panel_4);
        panel4.add("Center",supplementPanel);
      
        
        /***************Ϊ����ť����¼�������***********************************/   
         //��ʹ��ͬһ���¼����������������󡣱������������implements ActionListener
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
        
        //�����������岼�� 
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
     *********�����¼� *****************************************************************************************************
     */  
    public void actionPerformed(ActionEvent e) {  
        // ��ȡ�¼�Դ�ı�ǩ  
    	double result = 0.0;//baseNum��Ϊ�˼�¼����x��y��ʱ��x��ֵ
//    	char finalChar;//��¼���ı����õ����һ���ַ�
//    	String sign=resultText.getText();
//      	finalChar=sign.charAt(sign.length()-1);
        String label = e.getActionCommand();
        //����ģʽѡ�񣺼��ͣ���ѧ�ͣ�ͳ���ͣ���չ�ͣ�
        if(label.equals(MODEL[0])){//����
        	panel.add("Center",panel1);
        	panel1.setVisible(true);
        	panel2.setVisible(false);
        	panel3.setVisible(false);
        	panel4.setVisible(false);
        }else if(label.equals(MODEL[1])){//��ѧ��
        	panel1.setVisible(true);
        	panel2.setVisible(true);
        	panel3.setVisible(false);
        	panel4.setVisible(false);
        }else if(label.equals(MODEL[2])){//ͳ����
        	panel.add("Center",panel3);
        	panel1.setVisible(false);
        	panel2.setVisible(false);
        	panel3.setVisible(true);
        	panel4.setVisible(false);
        }else if(label.equals(MODEL[3])){//��չ��
        	panel.add("Center",panel4);
        	panel1.setVisible(false);
        	panel2.setVisible(false);
        	panel3.setVisible(false);
        	panel4.setVisible(true);
        }else if (label.equals("=")) {  // �û�����"="��  ,���������������
      	
        }else if (label.equals(KEYS[2])) { // �û�����"C"��  ����ʼ��������
        	resultText.setText("0");       //������������� 
        }else if (label.equals(KEYS[3])) {  //�û����ˡ������˻ؼ������ı������һ���ַ�ȥ��
            handleBackspace();
        }else if ("0123456789.()+-*/".indexOf(label) >= 0) {  // �û���������Ҫ����ı��ʽ
        }
        else {
        	
        	} 


        } 
  
    /** 
     * ����Backspace�������µ��¼� 
     */  
    private void handleBackspace() {  
        String text = resultText.getText();  
        int i = text.length();  
        if (i > 0) {  
            // �˸񣬽��ı����һ���ַ�ȥ��  
            text = text.substring(0, i - 1);  
            if (text.length() == 0) {  
                // ����ı�û�������ݣ����ʼ���������ĸ���ֵ  
                resultText.setText("0");  
            } else {  
                // ��ʾ�µ��ı�  
                resultText.setText(text);  
            }  
        }  
    } 
    /** 
     * ����C�������µ��¼� 
     */  
    private void handleC() {  
        // ��ʼ���������ĸ���ֵ  
        resultText.setText("");    
    }

    
  /********���࣬����һ��Calculator2����****************************************************************************************************/
    public static void main(String args[]) {  
        Calculator2 calculator = new Calculator2();  
        calculator.setVisible(true);  
        calculator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
    }  
}  
