package Segmentation;

import java.util.*;

public class CLine {
    int LineStart;
    int LineEnd;
    Vector Words;
    final static int threshold = 175;
    
    public CLine() {
        LineStart = -1;
        LineEnd = -1;
        Words = new Vector ();
    }
    
    public int getLineStart () {
        return LineStart; 
    }
    
    public int getLineEnd () {
        return LineEnd; 
    }
    
    public Vector getWords() {
        return Words;
    }
    
    public void setLineStart (int a_LineStart) {
        LineStart = a_LineStart; 
    }
    
    public void setLineEnd (int a_LineEnd) {
        LineEnd = a_LineEnd; 
    }
    
    public void setWords(Vector a_Words) {
        Words = a_Words;
    }
    
    @SuppressWarnings("unchecked")
    public void findWords(int[] pixels, int iw, int ih) {
        int[] vp = new int[iw];
        for (int i = 0; i < iw; i ++) {
            vp[i] = 0;
        }
        for (int i = LineStart*iw; i < iw*LineEnd; i++) {
            int p = pixels[i];
            int r = 0xff & (p >> 16);
            int g = 0xff & (p >> 8);
            int b = 0xff & (p);
            int y = (r + g + b)/3;
            Boolean white = false;
            if (y > threshold) {
                white = true;
            }
            if (!white) {
                vp[i%iw] ++;
            }
        }
        
        for (int i = 0; i < iw; i ++) {
            if (vp[i] != 0) {
                CWord Word = new CWord();
                Word.setWordStart(i);
                while (i < iw && vp[i] != 0) {
                    i ++;
                }
                Word.setWordEnd(i);
                while (i < iw && vp[i] == 0) {
                    i ++;
                }
                i --;
                Words.add(Word);
            }    
        }
    }    
}