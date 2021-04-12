/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package baseclass;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import prg.V;

/**
 *
 * @author Hit
 */
public class Treer extends JTree {

    public int X1 = getX(), X2 = getX() + getWidth(), Y1 = getY(), Y2 = getY() + getHeight(); //координаты по углам        public Formr THISFORM   ;
    public Formr THISFORM; //ссылка на родительскую форму
    public JScrollPane SCROLL;//
    ArrayList<ArrayList<String>> arr = new ArrayList<>();

    public void CLICK() {
        THISFORM.CLICK_ALL(getName());
    }

    public Treer(final Formr THISFORM, String name, String root, int width, int height) {//иницилизация имени поля и заголовка и размеров
        super.setName(name);
        super.setSize(width, height);
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(root);
        DefaultTreeModel model = (DefaultTreeModel) this.getModel();
        model.setRoot(top);
        SCROLL = new javax.swing.JScrollPane(this); // создаем скролингш для таблицы
        SCROLL.setSize(width, height); // координаты по скролингу а не по полю
        SCROLL.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        SCROLL.setViewportView(this); //не знаю 
        THISFORM.add(SCROLL); //добавляем на панель формы           
//        SCROLL.setBounds(1, 1, width, height); // координаты по скролингу а не по таблице

        addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                CLICK();
            }
        });
    }

    public void setLocation(int x, int y) {
//        System.out.println(x + "  " + y);
        if (SCROLL != null && x != 0 && y != -18) {
            SCROLL.setLocation(x, y);
        } else {
            super.setLocation(x, y);
        }
    }

//    public int getWidth() {
//        if (SCROLL != null) {
//            return SCROLL.getWidth();
//        } else {
//            return super.getWidth();
//        }
//    }
//
//    public int getHeight() {
//        if (SCROLL != null) {
//            return SCROLL.getHeight();
//        } else {
//            return super.getHeight();
//        }
//    }
//
//    public Rectangle getBounds() {
//        if (SCROLL != null) {
//            return SCROLL.getBounds();
//        } else {
//            return super.getBounds();
//        }
//    }
    public void addNewItem() {
        // ВАЖНО - работа с уже готовым деревом может производится только через модель дерева.
        // Только в этом случае гарантируется правильная работа и вызов событий
        // В противном случае новые узлы могут быть не прорисованы
        DefaultTreeModel model = (DefaultTreeModel) this.getModel();
        Object obj = this.getLastSelectedPathComponent();
        if (obj != null) {
            DefaultMutableTreeNode sel = (DefaultMutableTreeNode) obj;
            // Смотрим уровень вложенности и работаем в соответствии с ним
            DefaultMutableTreeNode tmp = new DefaultMutableTreeNode(sel.getLevel() + 1);
            model.insertNodeInto(tmp, sel, sel.getChildCount());
//            if (sel.getLevel() == 1) {
//                DefaultMutableTreeNode tmp = new DefaultMutableTreeNode("Deep");
//                model.insertNodeInto(tmp, sel, sel.getChildCount());
//            }
//            if (sel.isRoot()) {
//                DefaultMutableTreeNode tmp = new DefaultMutableTreeNode("Midle");
//                model.insertNodeInto(tmp, sel, sel.getChildCount());
//            }
            this.expandPath(new TreePath(sel.getPath()));
        }
    }

    public void removeItem() {
        // Смотри замечание в addNewItem()
        DefaultTreeModel model = (DefaultTreeModel) this.getModel();
        Object obj = this.getLastSelectedPathComponent();
        if (obj != null) {
            DefaultMutableTreeNode sel = (DefaultMutableTreeNode) obj;
            // Корень удалять нельзя
            if (!sel.isRoot()) {
                if (sel.getChildCount() == 0) {
                    model.removeNodeFromParent(sel);
                } else // Если есть "детишки" выведем сообщение
                {
                    JOptionPane.showMessageDialog(null, "Remove all subnodes");
                }
            }
        }
    }

    public void fillTree(ArrayList<ArrayList<String>> arr, int maxLevel) {
        this.arr = arr;
        if (arr.size()==0) {return;}
        ArrayList<DefaultMutableTreeNode> currentNodes = new ArrayList<>();
        for (int i = 0; i < maxLevel; i++) {
            currentNodes.add(new DefaultMutableTreeNode());
        }
        DefaultTreeModel model = (DefaultTreeModel) this.getModel();

        DefaultMutableTreeNode sel = (DefaultMutableTreeNode) model.getRoot();
        DefaultMutableTreeNode tmp = new DefaultMutableTreeNode(arr.get(0).get(0));
        model.insertNodeInto(tmp, sel, sel.getChildCount());
        currentNodes.set(0, tmp);

        for (int i = 1; i < arr.size(); i++) {
            if (Integer.parseInt(arr.get(i).get(1)) == 1) {
                sel = (DefaultMutableTreeNode) model.getRoot();
            } else {
                sel = currentNodes.get(Integer.parseInt(arr.get(i).get(1)) - 2);
            }
            tmp = new DefaultMutableTreeNode(arr.get(i).get(0));
            model.insertNodeInto(tmp, sel, sel.getChildCount());
            currentNodes.set(Integer.parseInt(arr.get(i).get(1)) - 1, tmp);
        }
        TreePath path = new TreePath(((DefaultMutableTreeNode) model.getRoot()).getPath());
        expandPath(path);
        RESTORE();
    }

    public void SAVE() {//сохранение раскрытых узлов
        Configs proper = new Configs(V.fileNameTreeconf);
        Iterator iter = proper.keySet().iterator();
        while (iter.hasNext()) {
            Object key = iter.next();
            if (key.toString().contains(THISFORM.getName() + "$" + getName())) {
                iter.remove();
            }
        }
        for (int i = 0; i < arr.size(); i++) {
            if (isExpanded(find((DefaultMutableTreeNode) ((DefaultTreeModel) getModel()).getRoot(), arr.get(i).get(0)))) {
                proper.setProperty(THISFORM.getName() + "$" + getName() + "$" + arr.get(i).get(0), arr.get(i).get(0));
            }
        }
        proper.saveProperties("Tree Configuration");
    }

    public void RESTORE() {//восстановление раскрытых узлов
        Configs proper = new Configs(V.fileNameTreeconf);
        Iterator iter = proper.keySet().iterator();
        try {
            while (iter.hasNext()) {
                Object key = iter.next();
                if (key.toString().contains(THISFORM.getName() + "$" + getName())) {
                    expandPath(find((DefaultMutableTreeNode) ((DefaultTreeModel) getModel()).getRoot(), proper.getProperty(key.toString())));
                }
                iter.remove();
            }
        } catch (Exception e) {
            System.out.println("Не найдены свойства дерева");
        }
    }

    public TreePath find(DefaultMutableTreeNode root, String s) {//поиск узла по наименованию
//        @SuppressWarnings("unchecked")
        Enumeration<DefaultMutableTreeNode> e = root.depthFirstEnumeration();
        while (e.hasMoreElements()) {
            DefaultMutableTreeNode node = e.nextElement();
            if (node.toString().equalsIgnoreCase(s)) {
                return new TreePath(node.getPath());
            }
        }
        return null;
    }
}
