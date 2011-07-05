package Segmentation;
import java.util.*;

public class CWord {
    int WordStart;
    int WordEnd;
    int ShirorekhaIndex;
    Vector Characters;
    Vector upperModifiers;
    final static int threshold = 175;
    
    public CWord() {
        WordStart = -1;
        WordEnd = -1;
        ShirorekhaIndex = -1;
        Characters = new Vector ();
        upperModifiers = new Vector();
    }
    
    public int getWordStart () {
        return WordStart; 
    }
    
    public int getWordEnd () {
        return WordEnd; 
    }
    
    public int getShirorekhaIndex () {
        return ShirorekhaIndex; 
    }
    
    public Vector getCharacters() {
        return Characters;
    }
    
    public Vector getUpperModifiers() {
        return upperModifiers;
    }
    
    public void setWordStart (int a_WordStart) {
        WordStart = a_WordStart; 
    }
    
    public void setWordEnd (int a_WordEnd) {
        WordEnd = a_WordEnd; 
    }
    
    public void setShirorekhaIndex (int a_ShirorekhaIndex) {
        ShirorekhaIndex = a_ShirorekhaIndex; 
    }
    
    public void setCharacters(Vector a_Characters) {
        Characters = a_Characters;
     }
    
     public void Shirorekha(int[] pixels, int LineStart, int LineEnd, int iw)
     {
        int count = 0;
        int[] hp = new int[LineEnd-LineStart+1];
        hp[0] = 0;
        for (int i = ((iw*LineStart)+WordStart ); i < ((iw*LineEnd)+WordEnd); i ++) {
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
                hp[count] ++;
            }
            if((i - WordEnd)%iw == 0) {
                i += iw - (WordEnd-WordStart);
                i --;
                count ++;
                hp[count] = 0;
             }            
        }
        
        int max = 0;
        int maxindex = -1;
        for (int i = 0; i < LineEnd-LineStart; i ++) {
            if (max < hp[i]) {
                max = hp[i];
                maxindex = i;
            }
        }     
        if (maxindex != -1) {
            ShirorekhaIndex = LineStart + maxindex;
        }     
    }
    
    @SuppressWarnings("unchecked")
    public void findCharacter(int[] pixels, int LineEnd, int iw) {
        int count = 0;
        int[] vp = new int[WordEnd-WordStart+1];
        for(int i = 0; i < (WordStart-WordEnd+1); i ++) {
            vp[i] = 0;
        }        
        for (int i = ((iw*(ShirorekhaIndex+1)+WordStart)); i < ((iw*LineEnd)+WordEnd); i ++) {
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
                vp[(i%iw)-WordStart] ++;
            }
            if((i - WordEnd)%iw == 0) {
                i += iw - (WordEnd-WordStart);
                i --;
             }
        }
        for (int i = 0; i < (WordEnd-WordStart+1); i ++) {
            if (vp[i] != 0) {
                CCharacter tempChar = new CCharacter();
                tempChar.setCharacterStart(i+WordStart);
                while (i < (WordEnd-WordStart+1) && vp[i] != 0) {
                    i ++;
                }
                tempChar.setCharacterEnd(i+WordStart);
                tempChar.setCharacterWidth(tempChar.getCharacterEnd()-tempChar.getCharacterStart());
                while (i < (WordEnd-WordStart+1) && vp[i] == 0) {
                    i ++;
                }
                i --;
                Characters.add(tempChar);
            }    
        }
    }
    
    @SuppressWarnings("unchecked")
    public void upperModifier(int[] pixels, int LineStart, int iw)
    {
        int count = 0;
        int[] vp = new int[WordEnd-WordStart+1];
        for(int i = 0; i < (WordStart-WordEnd+1); i ++) {
            vp[i] = 0;
        }        
        for (int i = ((iw*(LineStart+1)+WordStart)); i < ((iw*(ShirorekhaIndex-1))+WordEnd); i ++) {
            
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
                vp[(i%iw)-WordStart] ++;
            }
            if((i - WordEnd)%iw == 0) {
                i += iw - (WordEnd-WordStart);
                i --;
             }
        }
        for (int i = 0; i < (WordEnd-WordStart+1); i ++) {
           
            if (vp[i] != 0) {
                CUpperModifier tempUpperModifier = new CUpperModifier();
                tempUpperModifier.setUpperModifierStart(i+WordStart);
                while (i < (WordEnd-WordStart+1) && vp[i] != 0) {
                    i ++;
                }
                tempUpperModifier.setUpperModifierEnd(i+WordStart);
                while (i < (WordEnd-WordStart+1) && vp[i] == 0) {
                    i ++;
                }
                i --;
                upperModifiers.add(tempUpperModifier);
            }
            
        }
    }
}