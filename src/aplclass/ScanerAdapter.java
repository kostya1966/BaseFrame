package aplclass;

import baseclass.Formr;
import forms.CipherForm;
import java.awt.event.KeyEvent;

/**
 * Прослушиватель формы на нажатие клавиш.
 * Служит для обработки отсканированного ручным сканером штрихкода.
 * @author Fatal_HiT
 */
public class ScanerAdapter extends ScanerListener {

    boolean numFlag = false;
    Formr form;

    public ScanerAdapter(Formr form) {
        this.form = form;
    }

    @Override
    public void keyTypedS(KeyEvent e) {
    }

    @Override
    public void keyPressedS(KeyEvent e) {
    }

    @Override
    public void keyReleasedS(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void scanComplete(String scan, int scanType) {
        switch (scanType) {
            case scanEO:
                break;
            case scanRKV:
                break;
            case scanDK:
                break;
            case scanPS:
                break;
            case scanEAN:
                break;
            case scanKupon:
                break;
            default:
                return;
        }
        //для CipherForm обрабатывает штрихкод, отсканированный ручным сканером
        if (form.getName().equals("CipherForm")) {
            ((CipherForm) form).F[5].setText(scan);
            ((CipherForm) form).VALID_ALL("F5");
        }
    }
}
