package baseclass;

import baseclass.Fieldr;
import com.toedter.calendar.JDateChooser;
import prg.P;
import prg.V;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;

public class DateChooser extends JDateChooser {

    public DateChooser(Fieldr parent) {
        settings(parent);
    }

    private void settings(final Fieldr fieldr) {
        this.setLocale(V.LOCRUS);
        this.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            @Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                // If the 'date' property was changed...
                if ("date".equals(evt.getPropertyName())) {
                    JDateChooser aDateChooser = (JDateChooser) evt.getSource();
                    boolean isDateSelectedByUser = false;
                    // Get the otherwise unaccessible JDateChooser's 'dateSelected' field.
                    try {
                        // Get the desired field using reflection
                        Field dateSelectedField = JDateChooser.class.getDeclaredField("dateSelected");
                        // This line makes the value accesible (can be read and/or modified)
                        dateSelectedField.setAccessible(true);
                        isDateSelectedByUser = dateSelectedField.getBoolean(aDateChooser);
                    } catch (Exception ignoreOrNot) {
                        System.out.println("Что то с датой!!!");
                    }

                    // Do some important stuff depending on wether value was changed by user
                    if (isDateSelectedByUser) {
                        if (fieldr.TYPE.equals(V.TYPE_DATE)) {
                            fieldr.textFr.setText(new SimpleDateFormat("dd.MM.yyyy").format(aDateChooser.getDate()));
                        } else if (fieldr.TYPE.equals(V.TYPE_DATETIME)) {
                            fieldr.textFr.setText(new SimpleDateFormat("dd.MM.yyyy").format(aDateChooser.getDate()) + fieldr.textFr.getText().substring(fieldr.textFr.getText().length() - 9, fieldr.textFr.getText().length()));
                        }
                        aDateChooser.getJCalendar().setVisible(false);
                    }

                    // Reset the value to false
                    try {
                        Field dateSelectedField = JDateChooser.class.getDeclaredField("dateSelected");
                        dateSelectedField.setAccessible(true);
                    } catch (Exception ignoreOrNot) {
                        System.out.println("Что то с датой!!!");
                    }
                }
            }
        });
        this.setPreferredSize(new Dimension(0, 0));
        this.setMaximumSize(new Dimension(0, 0));
        this.getDateEditor().setEnabled(false);
        fieldr.THISFORM.add(this, -1);
        this.setLocation(fieldr.textFr.getX(), fieldr.textFr.getY());
        this.setVisible(true);
        this.actionPerformed(null);

        try {
            P.STARTMODAL(this.getJCalendar());
        } catch (Exception ignored) {
        }

        if (fieldr.THISFORM.MODAL == 1) {
            P.STARTMODAL(fieldr.THISFORM);
        }
        try { //windows
            Field popupField = this.popup.getClass().getSuperclass().getDeclaredField("popup");
            boolean isAccessiblePopupField = popupField.isAccessible();
            popupField.setAccessible(true);
            Popup popup = (Popup) popupField.get(this.popup);
            popupField.setAccessible(isAccessiblePopupField);


            Field field = popup.getClass().getSuperclass().getDeclaredField("component");
            boolean isAccessible = field.isAccessible();
            field.setAccessible(true);
            Component component = (Component) field.get(popup);
            field.setAccessible(isAccessible);
            component.setVisible(false);
        } catch (Exception ignored) {
        }
        try { //linux
            super.popup.getParent().setVisible(false);
            super.popup.getComponent().setVisible(false);
        } catch (Exception ignored) {
        }
    }
}
