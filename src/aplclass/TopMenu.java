package aplclass;

import java.util.HashMap;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import prg.FF;
import prg.P;
import prg.V;

public abstract class TopMenu {
public  int i;
public String key;    
public String keymap;    
    
    
public abstract void  POPUP();    //Формирование массива для заполнения меню перекрывается в дочернем классе
public abstract void  ONPAD(Object obj);   //Обработка выбора меню перекрывается в дочернем классе
 
public  void ADDLIST (String str1,String str2,int nomer) {
        V.MENU_ITEMS1.add(str1);
        V.MENU_ITEMS2.add(str2);
//        V.MENU_ITEMS3.add(nomer);
   
}   
public  void ADDLIST (String str1,String str2) {
ADDLIST (str1, str2,0);
}
//формирование линейки меню основного окна
    /**
     * формирование линейки меню основного окна
     */
 public  TopMenu()  {
       POPUP();
 //    ONPAD(null)     ; //ПУНКТЫ МЕНЮ ДОБАВЛЯЕМ
               JMenuBar mbar = new javax.swing.JMenuBar(); //основная линейка
                mbar.setOpaque(true);        
                mbar.setBackground(V.COLORB_MENU);        
        V._SCREEN.add(mbar);
        V.MENU_ITEMS3=new HashMap<>(); //ВЛОЖЕННОСТЬ МЕНЮ 1.2
        V.MENU_ITEMS4=new HashMap<>(); // НАЗВАНИЕ ПУНКТА МЕНЮ
        
        String  nmenu;
        JMenu menu1;
        JMenuItem menu2,menu3;
        for(i=0;i<V.MENU_ITEMS1.size();i++){
            key=V.MENU_ITEMS1.get(i);
            if (FF.AT(".", key)==0) { //если верхний уровень 1 2 3 ...
                nmenu = V.MENU_ITEMS2.get(i); //получаем название пункта
               menu1=new JMenu(nmenu); menu1.setOpaque(true);menu1.setBackground(V.COLORB_MENU);   //создаем пункт меню
               V.MENU_ITEMS3.put(key, menu1); //добавляем в массив пунктов меню c ключем V.MENU_ITEMS1 
               mbar.add(menu1);    //добавляем в линейку
            }else {//если добавляется к существующему меню
                if (FF.OCCURS(".", key)==1){//если второй уровень
                nmenu = V.MENU_ITEMS2.get(i); //получаем название пункта
                menu2 = new JMenuItem(nmenu); 
                menu2.setName(key);
                keymap = FF.SUBSTR(key, 1,FF.RAT(".", key)-1);//ключ родительского меню
                menu1=(JMenu)V.MENU_ITEMS3.get(keymap);
                menu1.add(menu2); menu1.addSeparator();
                menu2.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt)  {
                    ONPAD(evt.getSource()); // ОБРАБАТЫВАЕМ НАЖАТИЕ НА ПУНКТ МЕНЮ
                }});                
               V.MENU_ITEMS4.put(V.MENU_ITEMS1.get(i), menu2); //добавляем в массив пунктов меню c ключем V.MENU_ITEMS1 
                                    
                }
                if (FF.OCCURS(".", V.MENU_ITEMS1.get(i))>1){//если больше второго уровня ПОКА НЕ РАБОТАЕТ НЕ ЗНАЮ КАК ВООБЩЕ СТРОИТСЯ ТРЕТИЙ УРОВЕНЬ
                nmenu = V.MENU_ITEMS2.get(i); //получаем название пункта
                menu3 = new JMenuItem(nmenu); 
                String key = FF.SUBSTR(V.MENU_ITEMS1.get(i), 1,FF.RAT(".", V.MENU_ITEMS1.get(i))-1);//ключ родительского меню
                menu2=(JMenuItem)V.MENU_ITEMS4.get(key);
                menu2.add(menu3); //menu2.addSeparator();
                }
                
            }
            
        }
         
      V._SCREEN.setJMenuBar(mbar);
      
      /*  JMenu Menu1 = new javax.swing.JMenu("Справочники");
        JMenu Menu2 = new javax.swing.JMenu("Сервис");
        
                Menu1.setOpaque(true);        
                Menu1.setBackground(V.COLORB_MENU);        
                Menu2.setOpaque(true);        
                Menu2.setBackground(V.COLORB_MENU);        

        mbar.add(Menu1);  mbar.add(Menu2); 
        
        JMenuItem jMenuItem11 = new javax.swing.JMenuItem("Справочник 1");
        JMenuItem jMenuItem12 = new javax.swing.JMenuItem("Справочник 2");
        JMenuItem jMenuItem13 = new javax.swing.JMenuItem("Справочник 2");
        Menu1.add(jMenuItem11); Menu1.addSeparator();
        Menu1.add(jMenuItem12); Menu1.addSeparator(); 
        Menu1.add(jMenuItem13); Menu1.addSeparator();

        JMenuItem jMenuItem21 = new javax.swing.JMenuItem("Системные настройки");
        JMenuItem jMenuItem22 = new javax.swing.JMenuItem("Настройка окружения ");
        JMenuItem jMenuItem23 = new javax.swing.JMenuItem("Настройка пользователей");
        JMenuItem jMenuItem24 = new javax.swing.JMenuItem("Соединение");        
        JMenuItem jMenuItem25 = new javax.swing.JMenuItem("Просмотр данных");                
        JMenuItem jMenuItem26 = new javax.swing.JMenuItem("Выход");
        jMenuItem26.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.ALT_MASK));        
        Menu2.add(jMenuItem21); Menu2.addSeparator();
        Menu2.add(jMenuItem22); Menu2.addSeparator();
        Menu2.add(jMenuItem23); Menu2.addSeparator();
        Menu2.add(jMenuItem24); Menu2.addSeparator();
        Menu2.add(jMenuItem25); Menu2.addSeparator();
        Menu2.add(jMenuItem26); Menu2.addSeparator();
        jMenuItem24.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt)  {
                    P.DOFORM("conn");
            }});
        
        jMenuItem26.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
//           System.out.print("  exit:"+evt.paramString()+"\n");                
           System.exit(0);
            }
            });
        jMenuItem23.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
               P.DOFORM("users");
            }
        });
        jMenuItem25.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
               V.PARAMTO = new String[2];                
               V.PARAMTO[0]="select * from users";
               V.PARAMTO[1]=V.SELECT;
               P.DOFORM("browse");               

            }
        });
        
         V._SCREEN.setJMenuBar(mbar);
    
    }
*/   








 }

}
