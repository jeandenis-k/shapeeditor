package IHM;

import java.awt.Toolkit;

import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 *
 * @author Boris
 */
public class JNumTextField extends JTextField {

        public JNumTextField(int maxLength) {
            super();
            AbstractDocument doc = (AbstractDocument) getDocument();
            doc.setDocumentFilter(new TextLimiter(maxLength));
        }

        private class TextLimiter extends DocumentFilter {

            private int max;

            public TextLimiter(int max) {
                this.max = max;
            }

            @Override
            public void insertString(DocumentFilter.FilterBypass fb, int offset, String str, AttributeSet attr) throws BadLocationException {
                replace(fb, offset, 0, str, attr);
            }

            @Override
            public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String str, AttributeSet attrs) throws BadLocationException {
                int newLength = fb.getDocument().getLength() - length + str.length();

                if (newLength <= max && isNumeric(str)) {
                    fb.replace(offset, length, str, attrs);
                } else {
                    Toolkit.getDefaultToolkit().beep();
                }
            }

            public boolean isNumeric(String str) {
                try {
                    Integer.parseInt(str);
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
        }
    }
