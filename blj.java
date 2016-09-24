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
//import java.util.Scanner;//
import java.awt.*;
/** 
 * һ����������֧�ּ򵥵ļ���
 */  
public class blj extends JFrame implements ActionListener {  
	/***************************************************************************************************************/
	/********��������ذ������Ƽ���ť����*******/

	
    /*�򵥼������ϵļ�����ʾ���� */  
    private final String[] KEYS = { "7", "8", "9", "+" , "4", "5", "6",  
            "-", "1", "2", "3", "*", "0", ".", "=", "/" };  
    /*�򵥼������ϵ�����ȹ��ܼ�����ʾ���� */  
    private final String[] COMMAND = { "( )","AC", "C","��" };  //�����õ�Ӣ�İ棬�������ʿɸ�
    /*��ѧ�������ϵĺ���ͼ����ʾ����*/ 
    private final String[] FUNCTION = { "1/x", "x^2", "x^y", "x!", "��" ,"��(n&x)",
    		"sin","cos","tan","In","log","��","BMI/BFR","e^x","e"};
    /*�����������⹦��ͼ����ʾ����*/
    private final String[] SPECIAL_FUNTION = { "Cov", "��","s^2", "ave","$" };
    
    /*���������ť����ĳ��ȣ��������Ǵ�����Ӧ������*/
    int keysLen=KEYS.length;//���ڴ��KEYS�����еİ�ť����
    int commandLen=COMMAND.length;//���ڴ��COMMAND�����еİ�ť����
    int functionLen=FUNCTION.length;//���ڴ��FUNCTION�����еİ�ť����
    int special_functionLen=SPECIAL_FUNTION.length;//���ڴ��SPECIAL_FUNTION �����еİ�ť����
    
    /*�򵥼������ϼ��İ�ť */  
    private JButton keys[] = new JButton[keysLen];  
    /* �������ϵĹ��ܼ��İ�ť */  
    private JButton commands[] = new JButton[commandLen];  
    /*�������Ϲ���ͼ����ʾ����*/  
    private JButton function[] = new JButton[functionLen]; 
    /*�������ϳ��ú����İ�ť*/
    private JButton special_function[] = new JButton[special_functionLen]; 
    /*�������ı��� */  
    private JTextField resultText = new JTextField("");     //ȥ����Ĭ�ϵ��� 
  
    /*****************************************************************************************************/
    // ��־�û������Ƿ�����������ʽ�ĵ�һ������,�������������ĵ�һ������  
    private boolean firstDigit = true;  
    // ������м�����  
    private double resultNum =0;  
    // ��ǰ����������  
    private String operator = "=";  
    // �����Ƿ�Ϸ�  
    private boolean operateValidFlag = true; 
    
   /** 
    ************ ���캯�� *************************************************************************
    *********************/  
    public blj() {  
        super("MyCalculator");  
        // ��ʼ��������  
        init();  
        // ���ü������ı�����ɫ  
        this.setBackground(Color.LIGHT_GRAY);  
        this.setTitle("������");  
        // ����Ļ(500, 300)���괦��ʾ������  
        this.setLocation(500, 300);  
        // �����޸ļ������Ĵ�С  
        this.setResizable(false);             //ע�⣺�����С���Ըı� 
        // ʹ�������и������С����  
        this.pack();  
    }  
  
    /** 
     *******��ʼ�������� ***************************************************************************************** 
     */  
    private void init() {   	
    	
        // �ı����е����ݲ����Ҷ��뷽ʽ  
        resultText.setHorizontalAlignment(JTextField.RIGHT);  
        // �����޸Ľ���ı���  
        resultText.setEditable(true);            //�����ı���ʵ��
        // �����ı��򱳾���ɫΪ��ɫ  
        resultText.setBackground(Color.white);  
  
        // ��ʼ�����������������֡�����ȼ��İ�ť����������һ��������  
        JPanel calckeysPanel = new JPanel();  
        // �����񲼾�����4�У�4�е���������֮���ˮƽ������Ϊ3�����أ���ֱ������Ϊ3������  
        calckeysPanel.setLayout(new GridLayout(4, 4, 3, 3));  
        for (int i = 0; i < keysLen; i++) {  
            keys[i] = new JButton(KEYS[i]);  
            calckeysPanel.add(keys[i]);  
            keys[i].setForeground(Color.blue);  
        }  
        // ��������ú�ɫ��ʾ������������ɫ��ʾ  
        keys[3].setForeground(Color.red);  
        keys[7].setForeground(Color.red);  
        keys[11].setForeground(Color.red);  
        keys[15].setForeground(Color.red);   
  
        // ��ʼ���������ɾ�ȹ��ܼ������ú�ɫ��ʾ�������ܼ�����һ��������  
        JPanel commandsPanel = new JPanel();  
        // �����񲼾�����1�У�3�е���������֮���ˮƽ������Ϊ3�����أ���ֱ������Ϊ3������  
        commandsPanel.setLayout(new GridLayout(1, 3, 3, 3));  
        for (int i = 0; i < commandLen; i++) {  
            commands[i] = new JButton(COMMAND[i]);  
            commandsPanel.add(commands[i]);  
            commands[i].setForeground(Color.red);  
        }  
  
        // ��ʼ����ѧ�������������ܼ����ú�ɫ��ʾ���������������ܼ�����һ��������  
        JPanel functionsPanel = new JPanel();  
        // �����񲼾ֹ�������5�У�3�е���������֮���ˮƽ������Ϊ3�����أ���ֱ������Ϊ3������  
        functionsPanel.setLayout(new GridLayout(5, 3, 3, 3));  
        for (int i = 0; i < functionLen; i++) {  
            function[i] = new JButton(FUNCTION[i]);  
            functionsPanel.add(function[i]);  
            function[i].setForeground(Color.red);  
        }  
        
        // ��ʼ�����������⺯�����ܼ����ú�ɫ��ʾ���������������ܼ�����һ��������  
        JPanel special_functionsPanel = new JPanel();  
        // �����񲼾ֹ�������5�У�3�е���������֮���ˮƽ������Ϊ3�����أ���ֱ������Ϊ3������  
        special_functionsPanel.setLayout(new GridLayout(5, 3, 3, 3));  
        for (int i = 0; i < special_functionLen; i++) {  
        	special_function[i] = new JButton(SPECIAL_FUNTION[i]);  
        	special_functionsPanel.add(special_function[i]);  
            function[i].setForeground(Color.red);  
        } 
  
        // ������м����������岼�֣���calckeys��commands������ڼ��������в���  
        // ���ı�����ڱ�������functions��special_functions������ڼ�������������  
  
        // �½�һ����Ļ��壬�����潨����command��calckeys������ڸû�����  
        JPanel panel1 = new JPanel();  
        // ������ñ߽粼�ֹ����������������֮���ˮƽ�ʹ�ֱ�����ϼ����Ϊ3����  
        panel1.setLayout(new BorderLayout(3, 3));  
        panel1.add("North", commandsPanel);  
        panel1.add("South", calckeysPanel);
          // ����һ��������ı���  
        JPanel top = new JPanel();  
        top.setLayout(new BorderLayout());  
        top.add("Center", resultText); 
        //�½�һ����Ļ��壬�����潨����functions��special_functions������ڸû�����  
        JPanel panel2 = new JPanel();  
        // ������ñ߽粼�ֹ����������������֮���ˮƽ�ʹ�ֱ�����ϼ����Ϊ3����  
        panel2.setLayout(new BorderLayout(3, 3));  
        panel2.add("East", functionsPanel);  
        panel2.add("West", special_functionsPanel);
 
  
        // ���岼��  
        getContentPane().setLayout(new BorderLayout(3, 5));  
        getContentPane().add("North", top);  
        getContentPane().add("Center", panel1);  
        getContentPane().add("West", panel2);  
        
        /***************Ϊ����ť�����¼�������***********************************/   
        // ��ʹ��ͬһ���¼����������������󡣱������������implemennts ActionListener  
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
     *********�����¼� *****************************************************************************************************
     */  
    public void actionPerformed(ActionEvent e) {  
        // ��ȡ�¼�Դ�ı�ǩ  
        String label = e.getActionCommand();  
        if (label.equals(COMMAND[0])) {  
            // �û�����"( )"��  ,�������Ž������
             
        }else if (label.equals(COMMAND[1])) {  
            // �û�����"AC"��������ı����ϵ�����
            resultText.setText("0");  
        }else if (label.equals(COMMAND[2])) {  
            // �û�����"C"��  ����ʼ��������
            handleC();
        }else if (label.equals(COMMAND[3])) {  
        	//�û����ˡ������˻ؼ������ı������һ���ַ�ȥ��
             handleBackspace();
        }else if ("0123456789.".indexOf(label) >= 0) {  
            // �û��������ּ�����С�����  
            handleNumber(label);  
            // handlezero(zero);  
        } else {  
            // �û������������   
            handleOperator(label);            
            } 
//        else{
//        	//ȱʡ�����������������ӹ���
//        } 
    }  
  
    /** 
     * ����Backspace�������µ��¼� 
     */  
    private void handleBackspace() {  

    }  
  
    /** 
     * �������ּ���С����������µ��¼� 
     *  
     * @param key 
     */  
    private void handleNumber(String key) {  
 
    }  
  
    /** 
     * ����C�������µ��¼� 
     */  
    private void handleC() {  
 
    }  
  
    /** 
     * ����������������µ��¼� 
     *  
     * @param key 
     */  
    private void handleOperator(String key) {  
 
    }  
    
    
    /**
     * ���㺯��************************************************************
     */



    
    /** 
     * �ӽ���ı����л�ȡ���� 
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
  /********���࣬����һ��blj����****************************************************************************************************/
    public static void main(String args[]) {  
        blj calculator = new blj();  
        calculator.setVisible(true);  
        calculator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
    }  
}  