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
            "-", "1", "2", "3", "*", "0", ".", "=", "/" }; 
    /*��ѧ�ͼ������ϵĺ���ͼ����ʾ����*/ 
    private final String[] FUNCTION = { "1/x", "x^2", "x^y", "x!", "sin","cos","tan","��" ,
    		"In","lg","log","%","e^x","e","��",","};
    /*ͳ���ͼ����������⹦��ͼ����ʾ����*/
    private final String[] SPECIAL_FUNTION = { "C(N,M)", "A(N,M)","D", "Ave","����ֲ�",
    		"���ɷֲ�","��׼��̫�ֲ�","�����ֲ�"};
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
    double baseNum = 1;    //�������x^yʱx��ֵ
    String   expression = ""; //��ſ�ѧ����ʱ��ȥ������ı��ʽ�������sin��5+12*(3+5)/7��ʱ��expression=5+12*(3+5)/7
    boolean isFunction = false;   //�Ƿ��ǿ�ѧ����
    String   opreator = "";
    //���ü���String���͵ı������ڽ���û�оۼ�����Ӧ�ı�����ʱ��Ĭ����ʾ
    String info1="������������(��)";
    String info2="�������������(ǧ��)";
    String info3="�����������Χ";
    String info4="�������������(��)";
    
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
        //����ģʽѡ�񣺼��ͣ���ѧ�ͣ�ͳ���ͣ����������ͣ�����ת����
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
        } else if (label.equals("=")) {  // �û�����"="��  ,���������������
        	if(isFunction){
        		handleFunction(opreator);
        	    isFunction = false;   //�Ƿ��ǿ�ѧ���㣬��sin,cos,lg��
        	    opreator = "";
        	}else{
    	      	result = calculate(resultText.getText());//������
    	      	resultText.setText(String.valueOf(result));//������
        	}

        }else if (label.equals(KEYS[2])) { // �û�����"C"��  ����ʼ��������
        	handleC();                     //������������� 
        }else if (label.equals(KEYS[3])) {  //�û����ˡ������˻ؼ������ı������һ���ַ�ȥ��
            handleBackspace();
        }else if ("0123456789.()+-*/".indexOf(label) >= 0) {  // �û���������Ҫ����ı��ʽ
        	if(isFunction){           //����ǿ�ѧ���㣬��sin������������ı��ʽ����һ���ַ���
        		expression = expression + label;
        	}
        	resultText.setText(resultText.getText()+label);

        }else if(label.equals(FUNCTION[0])||label.equals(FUNCTION[1])||label.equals(FUNCTION[3])||label.equals(FUNCTION[11])){//���������ѧ�ͼ���������
            handleFunction(label);
        }else if(label.equals(SPECIAL_FUNTION[0])||label.equals(SPECIAL_FUNTION[1])||//�����ͳ�Ƽ�������ת��handleSpecialFunction����
        		label.equals(SPECIAL_FUNTION[2])||label.equals(SPECIAL_FUNTION[3])||
        		label.equals(SPECIAL_FUNTION[4])||label.equals(SPECIAL_FUNTION[5])||
        		label.equals(SPECIAL_FUNTION[6])||label.equals(SPECIAL_FUNTION[7])){
        	handleSpecialFunction(label);
        }else if(label.equals(SUPPLEMENT[0])||label.equals(SUPPLEMENT[1])||  //�������չ�����������ת��handleSupplement����
        		label.equals(SUPPLEMENT[2])){
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
	    opreator = "";
        
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
        int count = 0;//�ϴ����������������������������ַ��ĳ��ȱ��ڻ���֮�����ֵ
        char currentOp  ,peekOp;//��ǰ��������ջ��������
        for(int i=0;i<arr.length;i++) {
            currentOp = arr[i];
            if(isOperator(currentOp)) {//�����ǰ�ַ��������
                if(count > 0) {
                    postfixStack.push(new String(arr,currentIndex,count));//ȡ���������֮�������
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
            postfixStack.push(new String(arr,currentIndex,count));
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
     *��ѧ�͡������� �����ѧ����������������µ��¼� 
     */  
    private void handleFunction(String key) { 
    	double resultNum = 0;  
        if (key.equals("x!")) {  
            // �׳�����  
        	try{
        		int num = Integer.parseInt(resultText.getText());
        		resultNum = factorial(num);
        	}catch(NumberFormatException e2){
       		 	resultText.setText("����������");
        	} 
        	
        }else if(key.equals("1/x")){
        	//����һ�����ĵ���
        	if(getNumberFromText()==0){   //�ж�Ҫ��������Ƿ�Ϊ��
        		resultText.setText("0û�е�����");
        	}else{
        		resultNum = 1/getNumberFromText();
        	}
        	
        	
        }else if(key.equals("x^2")){
        	//����һ������ƽ��
        	resultNum = getNumberFromText()*getNumberFromText();
        }else if (key.equals("%")){
        	//ȡ�ٷֺ�����
        	resultNum = getNumberFromText()/100;  
        	
        }else if (key.equals("��")) {  
            // ƽ��������  
        	resultNum = calculate(expression);
        	if(resultNum<=0){
        		resultText.setText("you entered must be positive number");
            }else{
            	resultNum = Math.sqrt(resultNum);      	    	
        	}
        	
        }else if (key.equals("x^y")){
        	//����x��y�η� 
        	resultNum = calculate(expression);
        	resultNum = Math.pow(baseNum,resultNum);
        	
        }else if (key.equals("sin")) {  
            // ���Ǻ���sin()
        	resultNum = calculate(expression);
        	resultNum = Math.sin(resultNum);
        	
        }else if (key.equals("cos")) {  
            // ���Ǻ���cos() 
        	resultNum = calculate(expression);
        	resultNum = Math.cos(resultNum); 
        	 
        }else if (key.equals("tan")) {  
            //���Ǻ���tan()
        	resultNum = calculate(expression);
            resultNum = Math.tan(resultNum); 
            
        }else if (key.equals("e^x")){
        	//������Ȼ����e��x�η�
        	resultNum = calculate(expression);
       	    resultNum = Math.exp(resultNum);//��ʾ�е�����
       	    
        }else if (key.equals("In")){
        	//������Ȼ����
        	resultNum = calculate(expression);
        	resultNum = In(resultNum);//�������
        	
        }else if (key.equals("e")){
       	 //������Ȼ����e
       	 resultNum = Math.E;//��ʾ�д��Ľ�
       	 
       }else if (key.equals("��")){
       	 //����Բ���� ��
       	 resultNum = Math.PI;//��ʾ�е�����
       	 
       }
        
        
        resultText.setText(String.valueOf(resultNum));
 
    
    }  
    
    /*����׳�*/
    private double factorial(int x) {
    	if(x==0){
    		return(1);
    	}else{
    		 
    	    String style="0.0";
    	    DecimalFormat df = new DecimalFormat();  
    	    style = "0.00000E000";  
    	    BigDecimal num=new BigDecimal(String.valueOf(x));//����BigDecimalʱָ����Ч����s
            for(int i = 1; i <= x; i++){  
                String temp1 = Integer.toString(i);  
                BigDecimal temp2 = new  BigDecimal(temp1);  
                num = num.multiply(temp2);  
           } 
           df.applyPattern(style);  
           return(Double.valueOf(df.format(num)).doubleValue());  //��������ת����double�ͷ���
    	}
   }
 
   /*������Ȼ���� In*/
   private double In(double x){
    	
    	if(x<=0){
    		resultText.setText("you entered must be positive number");
    		return 0;
        }else{
	    	double lg = Math.log(x);
	    	return (lg);
    	}
    	
   }
   /***********************************************************************************************/
  
  
   /************************************************************************************************/
   /**
    * ͳ���͡�������ʵ��ͳ�Ƽ������Ĺ���
    * */
   private  void handleSpecialFunction(String label){
	   if(label.equals(SPECIAL_FUNTION[0])){//���������
		   resultText.setText("������n��m�����ԡ�/������                  C( "); 
	   }else if(label.equals(SPECIAL_FUNTION[1])){//����������
		   resultText.setText("������n��m�����ԡ�/������                  A( "); 

	   }else if(label.equals(SPECIAL_FUNTION[2])){//���㷽��
		   
	   }else if(label.equals(SPECIAL_FUNTION[3])){//����ƽ��ֵ
		   
	   }else if(label.equals(SPECIAL_FUNTION[4])){//�������ֲ�
	   
	   }else if(label.equals(SPECIAL_FUNTION[5])){//���㲴�ɷֲ�
		   
	   }else if(label.equals(SPECIAL_FUNTION[6])){//�����׼��̫�ֲ�
		   StandardDistribution(resultText.getText());
	   }else if(label.equals(SPECIAL_FUNTION[7])){//���㿨���ֲ�
		   
	   }
	   
   } 
   /*������Ϲ�ʽ*/
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
   /*�������й�ʽ*/
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
   /*����ƽ��ֵ*/
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
   /*���㷽��*/
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
    * ��չ�͡�������������չ�����¼�
    * */
   
   private void handleSupplement(String label){
	   double weight,height,waistLine;
	   int    age;
	   
	   weight = Double.valueOf(weightText.getText()); //��������л������
	   height = Double.valueOf(heightText.getText());//��������л�����
	   
	   if(label.equals(SUPPLEMENT[0])){
		   BMI(weight,height);
	   }else if(label.equals(SUPPLEMENT[1])){
		   waistLine = Double.valueOf(waistText.getText());
		   age = Integer.valueOf(ageText.getText());
		   BFR(weight,height,waistLine,age);   
	   }else{
		   
	   }
   }
   
   /**
    * �������غ���
    * @param weight��height   
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
   
   /*��������֬����*/
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
   
   
   /***********************************************************************************************/
   
   /**
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
   /**
    * �����ֲ���ֵ
    * @param n,p,k   ʵ��n�Σ�ÿ�η�������Ϊp����������ΪK
    */
   private void BiDistribution(int n,float p,int k){
	   int c=C(k,n);
	   double P;
	   P=c*(Math.pow(p,k))*(Math.pow(1-p,(n-k)));
	   resultText.setText(P+"");
	
   }
   /**
    * ���ɷֲ���ֵ
    * @param ��,k   ÿ�η�������Ϊ�ˣ���������ΪK
    */
   private void PoDistribution(float ��,int k){
	   double p;
	   p=(Math.pow(��,k)*Math.pow(Math.E,-��))/factorial(k);
	
}
   /**
    * ٤�꺯��
    * @param ��  
    */
   private void Gamma(float ��){
	   double ��2,x=1;//xδ�����������
	   double h = Math.abs(Integer.MAX_VALUE - Integer.MIN_VALUE) /Integer.MAX_VALUE ;
	   double sum = 0;
		  for (double xi = Integer.MIN_VALUE; xi <= Integer.MAX_VALUE; xi = xi + h) {
		   sum += fx(��,1/2,x) * h;
		  }
   }
   /**
    * ٤�꺯���е��ܶȺ���, ȡ��x�Ķ�����
    * @param �� ,x 
    */
  
	 public static double f(double x,float ��) {
	  double f;
	  f =Math.pow(x,��-1)*Math.pow(Math.E,-x);
	  return f;
	 }
	 /**
	    * ٤��ֲ��е��ܶȺ���, ȡ��x�Ķ�����
	    * @param �� ,x 
	 */
	 public static double fx(float ��,float ��,double x) {
		 double f;
		 f=(Math.pow(��,��)*Math.pow(x,��-1)*Math.pow(Math.E, - ��*x))/getDefiniteIntegralByRectangle2(0,Double.MAX_VALUE,��);;
		 return f;
	 }
	 
   /**
    * ٤�꺯��
    * @param ��  
    */
   public static double getDefiniteIntegralByRectangle2(double x0, double xn,float ��) {
	      // 0~1�������ɵȷ�
		  int n = Integer.MAX_VALUE;
		  double h = Math.abs(xn - x0) / n;
		  double sum = 0;
		  for (double xi = 0; xi <= xn; xi = xi + h) {
		   sum += f(xi,��) * h;
		  }
		  return sum;
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
}