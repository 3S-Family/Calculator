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
    private final String[] FUNCTION = { "1/x", "x^2", "x^y", "x!", "��" ,"%",
    		"sin","cos","tan","In","lg","log","e^x","e","��"};
    /*ͳ���ͼ����������⹦��ͼ����ʾ����*/
    private final String[] SPECIAL_FUNTION = { "C(N,M)", "A(N,M)","D", "Ave" };
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
    double baseNum = 1;    //�������x^yʱx��ֵ
    String   expression = ""; //��ſ�ѧ����ʱ��ȥ������ı��ʽ�������sin��5+12*(3+5)/7��ʱ��expression=5+12*(3+5)/7
    boolean isFunction = false;   //�Ƿ��ǿ�ѧ����
    String   opreator = "";
    
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
        
        // ��ʼ����ѧ�������������ܼ����� �����������ܺͿ�ѧ���������ܼ�����һ��������  
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

        }else if(label.equals("1/x")||label.equals("x^2")||label.equals("x!")||label.equals("%")){//���������ѧ�ͼ���������
            handleFunction(label);
        }else if(label.equals("C(N,M)")||label.equals("A(N,M)")||    //�����ͳ�Ƽ�������ת��handleSpecialFunction����
        		label.equals("D")||label.equals("Ave")){
        	handleSpecialFunction(label);
        }else if(label.equals("BMI")||label.equals("BFR")||  //�������չ�����������ת��handleSupplement����
        		label.equals("$")||label.equals("����")){
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
 
   /*�������log(x,y)*/
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
	   if(label.equals("C(N,M)")){//���������
		   resultText.setText("������n��m�����ԡ�/������                  C( "); 
	   }else if(label.equals("A(N,M)")){//����������
		   resultText.setText("������n��m�����ԡ�/������                  A( "); 

	   }else if(label.equals("D")){//���㷽��
		   
	   }else{//����ƽ��ֵ
		   
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
	   waistLine = Double.valueOf(waistText.getText());
	   age = Integer.valueOf(ageText.getText());
	   if(label.equals("BMI")){
		   BMI(weight,height);
	   }else if(label.equals("BFR")){
		   BFR(weight,height,waistLine,age);   
	   }
   }
   /*��������ָ��*/
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
