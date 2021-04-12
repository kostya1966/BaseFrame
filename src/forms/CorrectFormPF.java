package forms;

import baseclass.Formr;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import prg.P;
import prg.V;

public class CorrectFormPF extends Formr {

    ArrayList<String> arr = new ArrayList<>();
    public Formr FormParent=V.FormParent;
   
    public CorrectFormPF() {
        super(V.FormCorrName, V.PARAMTO[V.PARAMTO.length - 1], 300, 400); //Вызов конструктора от базового класса 
        for (int i = 0; i < V.PARAMTO.length - 1; i++) {
            arr.add(V.PARAMTO[i]);
        }
        V.FormCorr=THISFORM;
    }

    @Override
    public void DESCPROP() {
        SETRESIZABLE(1); //1-Признак фиксированного размера 0- не фиксированный
        SETMODAL(1);    //1-Модальная форма 0-не модальная
//        setClosable(false);

    }

    @Override
    public void KEYPRESS(Component obj, KeyEvent e) {
        super.KEYPRESS(obj, e);
//        if (obj.getName().equals("g_view")) {
//            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
//                CLICK_ALL("bok");
//            }
//        }
    }

    @Override
    public void LOAD_OBJ() {
        int k = 0;
        for (int i = 0; i < V.PARAMTO.length / 4; i++) {
            L[i] = P.addobjL(this, "L" + i, V.PARAMTO[k]);
            PF[i] = P.addobjPF(this, "F" + i, V.PARAMTO[k + 3], 250, 30);
            PF[i].setEchoChar('*');
            if (!V.PARAMTO[k + 1].equals("")) {
                PF[i].SETTYPE(V.PARAMTO[k + 1]);
            }
            if (!V.PARAMTO[k + 2].equals("")) {
                PF[i].SETINPUTMASK(V.PARAMTO[k + 2]);
            }
            PF[i].setText(V.PARAMTO[k + 3]);
            if (V.PARAMTO[k + 2].contains(".") && !V.PARAMTO[k + 3].contains(".") && V.PARAMTO[k + 1].equals(V.TYPE_NUMERIC)) {		
                PF[i].setText(V.PARAMTO[k + 3] + ".0");		
            }
            k += 4;
        }
        B[0] = P.addobjB(this, "bok", "Подтвердить", "Подтвердить");
        B[1] = P.addobjB(this, "besc", "Отмена", "Отмена");
    }

//расположение объектов относительно друг друга выполняется также при изменении размеров формы 
    @Override
    public void LOC_ABOUT() {
        int maxL = 0, num = 0;
        for (int i = 0; i < arr.size() / 4; i++) {
            if (maxL < L[i].getWidth()) {
                maxL = L[i].getWidth();
                num = i;
            }
        }
        int maxF = 0, numF = 0;
        for (int i = 0; i < arr.size() / 4; i++) {
            if (maxF < PF[i].getWidth()) {
                maxF = PF[i].getWidth();
                numF = i;
            }
        }
        
        for (int i = 0; i < arr.size() / 4; i++) {
            if (i == 0) {
                locate(L[i], null, V.LOC_LEFT, 5, null, V.LOC_UP, 15);
                locate(PF[i], L[num], V.LOC_RIGHT, 5, null, V.LOC_UP, 10);
            } else {
                locate(L[i], null, V.LOC_LEFT, 5, L[i - 1], V.LOC_DOWN, 15);
                locate(PF[i], L[num], V.LOC_RIGHT, 5, L[i - 1], V.LOC_DOWN, 10);
            }
        }
        locate(B[0], null, V.LOC_LEFT, 5, null, V.LOC_DOWN, 10);
        locate(B[1], null, V.LOC_RIGHT, 5, null, V.LOC_DOWN, 10);
        setBounds(getX(), getY(), L[num].getWidth() + PF[numF].getWidth() + 30, arr.size() / 4 * 40 + 80);
    }

    @Override
    public void DESTROY() {
        super.DESTROY();
//        A.CLOSE(alias);
        V.PARAMOT = new String[arr.size() / 4 + 1];
        V.PARAMOT[0] = "F";
    }

    @Override
    public void OPEN() {
        super.OPEN();
        setBounds(V._SCREEN.getContentPane().getWidth() / 2 - getWidth() / 2, V._SCREEN.getContentPane().getHeight() / 2 - getHeight() / 2, getWidth(), getHeight());
    }

    @Override
    public void DBLCLICK_ALL(String name) {
      if (FormParent!=null){
            FormParent.DBLCLICK_ALL(name);
        }
       super.DBLCLICK_ALL(name); //To change body of generated methods, choose Tools | Templates.
    }

  
    @Override
    public void CLICK_ALL(String name) {
        if ("besc".equals(name)) {
            DESTROY();
        }
        if ("bok".equals(name)) {
            DESTROY();
            V.PARAMOT[0] = "T";
            for (int i = 0; i < arr.size() / 4; i++) {
                V.PARAMOT[i + 1] = PF[i].getText();
            }
        }
    }

    @Override
    public boolean VALID_ALL(String name) {
        if (FormParent!=null){
           return  FormParent.VALID_ALL(name);
        }
        
        return super.VALID_ALL(name); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean INIT() {
    if (FormParent!=null){
             FormParent.INIT_COR(THISFORM);
        }
     return super.INIT(); //To change body of generated methods, choose Tools | Templates.
    }
    
        
        
    
    
}
   
