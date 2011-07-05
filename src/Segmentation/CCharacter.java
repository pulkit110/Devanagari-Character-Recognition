
package Segmentation;

import java.util.*;


public class CCharacter {
    int CharacterStart;
    int CharacterEnd;
    int CharacterHeight;
    int CharacterWidth;
    int CharacterHeightRegion;
    int CharacterWidthRegion;
    final static int threshold = 175;
    
    public CCharacter() {
        CharacterStart = -1;
        CharacterEnd = -1;
        CharacterHeight = -1;
        CharacterWidth = -1;
        CharacterWidthRegion = -1;
        CharacterHeightRegion = -1;
    }
    
    public int getCharacterStart () {
        return CharacterStart; 
    }
    
    public int getCharacterEnd () {
        return CharacterEnd; 
    }
    
    public int getCharacterHeight () {
        return CharacterHeight;
    }
    
    public int getCharacterWidth () {
        return CharacterWidth;
    }
    
    public int getCharacterHeightRegion () {
        return CharacterHeightRegion;
    }
    
    public int getCharacterWidthRegion () {
        return CharacterWidthRegion;
    }
    
    public void setCharacterStart (int a_CharacterStart) {
        CharacterStart = a_CharacterStart; 
    }
    
    public void setCharacterEnd (int a_CharacterEnd) {
        CharacterEnd = a_CharacterEnd; 
    }
    
    public void setCharacterHeight (int a_CharacterHeight) {
        CharacterHeight = a_CharacterHeight;
    }
    
    public void setCharacterWidth (int a_CharacterWidth) {
        CharacterWidth = a_CharacterWidth;
    }
    
    public void setCharacterHeightRegion (int a_CharacterHeightRegion) {
        CharacterHeightRegion = a_CharacterHeightRegion;
    }
    
    public void setCharacterWidthRegion (int a_CharacterWidthRegion) {
        CharacterWidthRegion = a_CharacterWidthRegion;
    }
    
    public void findCharacterHeight (int[] pixels, int iw, int LineEnd, int shiroRekhaIndex) {
        int count = 0;
        int[] hp = new int[LineEnd-shiroRekhaIndex+1];
        hp[0] = 0;
        for (int i = ((iw*shiroRekhaIndex) + CharacterStart); i < ((iw*LineEnd)+CharacterEnd); i ++) {
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
            if((i - CharacterEnd)%iw == 0) {
                if (hp[count] == 0) {
                    CharacterHeight = count;
                    return;
                }
                i += iw - (CharacterEnd-CharacterStart);
                i --;
                count ++;
                hp[count] = 0;
             }            
        }
        CharacterHeight = LineEnd - shiroRekhaIndex;
    }
}