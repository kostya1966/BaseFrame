package aplclass;
//
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import prg.FF;
import prg.V;

public abstract class CreateMenu {

    public int i;
    public String key;
    public String keymap;
    public String[] MASK;
    public boolean STAT = false; //признак true - добавление пунктов меню      false - слушатель и обработка выбора
    public JMenuBar mbar;
    public abstract void DESCR(String key);   //Обработка выбора меню перекрывается в дочернем классе

   public CreateMenu(Font font ) {
       ADDMENU(font);
  }
   public CreateMenu() {    
       ADDMENU();
   }
    
    
    /**
     * Добавление пунктов в меню задачи
     *
     * @param str1 код пункта 1.2
     * @param str2 название
     * @param nomer 1- обязательный пункт
     */
    public void ADDLIST(String str1, String str2, int nomer) {
  //      ADDLIST("6.1", "Системные настройки");
  //      ADDLIST("6.2", "Настройка окружения");
  //      ADDLIST("6.3", "Настройка пользователей",3);//только одминистратору
        
        //# - доступ ко всем пунктам меню кроме nomer==3     V.USER_ADMIN = 1      иначе ,1.* - ко всем как ,1.1,1.2,1.3,.....
        //## - доступ ко всем пунктам меню и корректуре всех  кроме nomer==3   V.USER_ADMIN = 2   
        //### - администратор  V.USER_ADMIN = 3
        if (STAT = false) {
            return;
        } //если слушаетль 
        boolean tf = false;
        if (nomer == 1) {
            tf = true;
        }//если обязательный 
        if (V.USER_ADMIN > 0 ) { //если просмотр всего
            tf = true;
        }//если админ 
        if (V.USER_ADMIN < 3 && nomer==3) { //если только для админа
            tf = false;
        }//
        if (V.USER_ADMIN < 2 && nomer==2) { //если только для админа
            tf = false;
        }//
        
        
        if (FF.AT("," + str1+".", V.USER_MENU) > 0 ||FF.AT("," + str1+"-", V.USER_MENU) > 0 || FF.AT("," + str1+",", V.USER_MENU) > 0)
        { //если найдено ','+номер пункта в меню  ,2. или ,2.24,   в ,2.24, т.е. найдет "2" и "2.24" но не найдет "2.2"
            tf = true;
        }//если в списке разрешенных
        if (MASK.length > 0) {
            for (int i = 0; i < MASK.length; i++) {
                if (FF.AT(MASK[i], "," + str1) > 0) {
                    tf = true;
                }//если под текущей  маской                 
            }
        }
        if (tf == true) {
            V.MENU_ITEMS1.add(str1);
            V.MENU_ITEMS2.add(str2);
        }

    }

    public void ADDLIST(String str1, String str2) {
        ADDLIST(str1, str2, 0);
    }

//формирование линейки меню основного окна
    /**
     * формирование линейки меню основного окна
     */

public void ADDMENU() {
    ADDMENU(null);
}
    
public void ADDMENU(Font font) {
          //разложить V.USER_MENU на маски 1.*
        String str = V.USER_MENU;
        String val;
        int i;
        int kol = FF.OCCURS("*", str);
        MASK = new String[kol]; //набор масок типа ,1.*, ,3.1.*,
        for (int j = 0; j < kol; j++) {
            i = FF.AT("*", str);
            val = FF.SUBSTR(str, 1, i - 1);
            str = FF.SUBSTR(str, i + 1);
            i = FF.RAT(",", val);
            val = FF.SUBSTR(val, i);
            MASK[j] = val;
        }
        //

        STAT = true;  //   добавление пунктов меню
        DESCR(""); //ПУНКТЫ МЕНЮ ОРИСАНИЕ
        STAT = false; //слушатель и обработка выбора    
        mbar = new javax.swing.JMenuBar(); //основная линейка без элементов
        mbar.setOpaque(true);
        mbar.setBackground(V.COLORB_MENU);
        mbar.setFont(font);
        V._SCREEN.add(mbar);
        V.MENU_ITEMS3 = new HashMap<>(); //ВЛОЖЕННОСТЬ МЕНЮ 1.2
        V.MENU_ITEMS4 = new HashMap<>(); // НАЗВАНИЕ ПУНКТА МЕНЮ

        String nmenu;
        JMenu menu1, menu1_1;
        JMenuItem menu2, menu3;
        for (i = 0; i < V.MENU_ITEMS1.size(); i++) {
            key = V.MENU_ITEMS1.get(i);  // ключ 
            if (FF.AT(".", key) == 0) { //если верхний уровень 1 2 3 ... основная линейка
                if (FF.AT("-", key) == 0) {  // если доступен 
                    nmenu = V.MENU_ITEMS2.get(i); //получаем название пункта
                    menu1 = new JMenu(nmenu); // заносим элемент в основную линейку
                    menu1.setOpaque(true);
                    menu1.setBackground(V.COLORB_MENU);   //создаем пункт меню
                    menu1.setName(key);
                    // menu1.requestFocus();
                    if ("<>".equals(FF.SUBSTR(nmenu,1,2))){  //если первых два символа <> то пункт не доступен
                     menu1.setEnabled(false);
                     menu1.setText(FF.SUBSTR(nmenu,3));
                    }
                    V.MENU_ITEMS3.put(key, menu1); //добавляем в массив пунктов меню c ключем V.MENU_ITEMS1 
                    mbar.add(menu1);    //добавляем в линейку
            menu1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
               //           System.out.println("2:mouseClicked");
                            JMenu menu = (JMenu) e.getSource();
                            String key = menu.getName();
                            DESCR(key);
                              

            }
        });
/*           menu1.addActionListener(new java.awt.event.ActionListener() {
                        @Override
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            System.out.println("actionPerformed");
                            JMenu menu = (JMenu) evt.getSource();
                            String key = menu.getName();
                            DESCR(key);
                            //ONPAD(evt.getSource()); // ОБРАБАТЫВАЕМ НАЖАТИЕ НА ПУНКТ МЕНЮ
                        }
                    });
  */                
                    
                } else {
                    nmenu = V.MENU_ITEMS2.get(i); //получаем название пункта
                    menu1 = new JMenu(nmenu);
                    menu1.setOpaque(true);
                    menu1.setBackground(V.COLORB_MENU);   //создаем пункт меню
                    if ("<>".equals(FF.SUBSTR(nmenu,1,2))){  //если первых два символа <> то пункт не доступен
                     menu1.setEnabled(false);
                     menu1.setText(FF.SUBSTR(nmenu,3));
                    }
                    V.MENU_ITEMS3.put(key, menu1); //добавляем в массив пунктов меню c ключем V.MENU_ITEMS1 
                    keymap = FF.SUBSTR(key, 1, FF.RAT("-", key) - 1);//ключ родительского меню
                    menu1_1 = (JMenu) V.MENU_ITEMS3.get(keymap);
                    menu1_1.add(menu1);    //добавляем в линейку
                    menu1_1.addSeparator();
                }
                    if (font!=null) {
                     menu1.setFont(font);
                    }
                
            } else {//если добавляется к существующему меню
                if (FF.OCCURS(".", key) == 1 && FF.OCCURS("-", key) == 0) {//если второй уровень
                    nmenu = V.MENU_ITEMS2.get(i); //получаем название пункта
                    menu2 = new JMenuItem(nmenu);
                    menu2.setName(key);
                    if ("<>".equals(FF.SUBSTR(nmenu,1,2))){  //если первых два символа <> то пункт не доступен
                     menu2.setEnabled(false);
                     menu2.setText(FF.SUBSTR(nmenu,3));
                    }
                    keymap = FF.SUBSTR(key, 1, FF.RAT(".", key) - 1);//ключ родительского меню
                    menu1 = (JMenu) V.MENU_ITEMS3.get(keymap); //родительское меню 
                    if (font!=null) {
                     menu2.setFont(font);
                    }
                    menu1.add(menu2);
                    menu1.addSeparator();
                    menu2.addActionListener(new java.awt.event.ActionListener() {
                        @Override
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            JMenuItem menu = (JMenuItem) evt.getSource();
                            String key = menu.getName();
                            DESCR(key);
                            //ONPAD(evt.getSource()); // ОБРАБАТЫВАЕМ НАЖАТИЕ НА ПУНКТ МЕНЮ
                        }
                    });
                    V.MENU_ITEMS4.put(V.MENU_ITEMS1.get(i), menu2); //добавляем в массив пунктов меню c ключем V.MENU_ITEMS1 

                }
                if (FF.OCCURS(".", key) == 1 && FF.OCCURS("-", key) == 1) {//
                    nmenu = V.MENU_ITEMS2.get(i); //получаем название пункта
                    menu2 = new JMenuItem(nmenu);
                    menu2.setName(key);
                    if (font!=null) {
                     menu2.setFont(font);
                    }
                    if ("<>".equals(FF.SUBSTR(nmenu,1,2))){  //если первых два символа <> то пункт не доступен
                     menu2.setEnabled(false);
                     menu2.setText(FF.SUBSTR(nmenu,3));
                    }
                    
                    keymap = FF.SUBSTR(key, 1, FF.RAT(".", key) - 1);//ключ родительского меню
                    menu1 = (JMenu) V.MENU_ITEMS3.get(keymap);
                    menu1.add(menu2);
                    menu1.addSeparator();
                    menu2.addActionListener(new java.awt.event.ActionListener() {
                        @Override
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            JMenuItem menu = (JMenuItem) evt.getSource();
                            String key = menu.getName();
                            DESCR(key);
                            //ONPAD(evt.getSource()); // ОБРАБАТЫВАЕМ НАЖАТИЕ НА ПУНКТ МЕНЮ
                        }
                    });
                    V.MENU_ITEMS4.put(V.MENU_ITEMS1.get(i), menu2); //добавляем в массив пунктов меню c ключем V.MENU_ITEMS1  
                }
                if (FF.OCCURS(".", V.MENU_ITEMS1.get(i)) > 1) {//если больше второго уровня ПОКА НЕ РАБОТАЕТ НЕ ЗНАЮ КАК ВООБЩЕ СТРОИТСЯ ТРЕТИЙ УРОВЕНЬ
                    nmenu = V.MENU_ITEMS2.get(i); //получаем название пункта
                    menu3 = new JMenuItem(nmenu);
                    if (font!=null) {
                     menu3.setFont(font);
                    }
                    if ("<>".equals(FF.SUBSTR(nmenu,1,2))){  //если первых два символа <> то пункт не доступен
                     menu3.setEnabled(false);
                     menu3.setText(FF.SUBSTR(nmenu,3));
                    }
                    
                    String key = FF.SUBSTR(V.MENU_ITEMS1.get(i), 1, FF.RAT(".", V.MENU_ITEMS1.get(i)) - 1);//ключ родительского меню
                    menu2 = (JMenuItem) V.MENU_ITEMS4.get(key);
                    menu2.add(menu3); //menu2.addSeparator();
                }

            }

        }

        V._SCREEN.setJMenuBar(mbar);
        
  
}    
    
}
