package Segmentation;

import java.awt.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import java.applet.*;
import java.awt.image.*;
import java.util.*;

public class Segment extends Applet{
    Dimension d;
    Image img;
    BufferedImage image;
    WritableRaster raster;
    int iw, ih;
    int pixels[];
    int w, h;
    
    @SuppressWarnings("unchecked")
    @Override
    public void init() {
        d = getSize();
        w = d.width;
        h = d.height;
        
        try {
            img = ImageIO.read(new File("D:/1.jpg"));
            image = ImageIO.read(new File("D:/1.jpg"));
            raster = image.getRaster();
            MediaTracker t = new MediaTracker(this);
            t.addImage(img, 0);
            t.waitForID(0);
            iw = img.getWidth(null);
            ih = img.getHeight(null);
            pixels = new int[iw*ih];
            PixelGrabber pg = new PixelGrabber(img, 0, 0, iw, ih, pixels, 0, iw);
            pg.grabPixels();
            
        } catch (InterruptedException e1) {
        } catch (IOException e2) {
        }
    }
    
    public Vector makeDataSet(int pixels[], int iw, int ih) 
    {
        Vector Lines = new Vector();
        Lines = findLines(pixels, iw, ih);
        
        int maxCharHeight = 0;
        int maxCharWidth = 0;
        for (int i = 0; i < Lines.size(); i ++) {
            
            CLine TempLine = (CLine)(Lines.get(i));
                
            TempLine.findWords (pixels,iw, ih);
                       
            for (int j = 0; j < TempLine.getWords().size(); j ++) {
                Vector Words = TempLine.getWords();
                CWord TempWord = (CWord)Words.get(j);
                TempWord.Shirorekha(pixels, TempLine.getLineStart(), TempLine.getLineEnd(), iw);
                TempWord.findCharacter(pixels, TempLine.getLineEnd(), iw);
                for (int k = 0; k < TempWord.getCharacters().size(); k++) {
                    Vector Characters = TempWord.getCharacters();
                    CCharacter TempCharacter = (CCharacter)Characters.get(k);
                    TempCharacter.findCharacterHeight(pixels, iw, TempLine.getLineEnd(), TempWord.getShirorekhaIndex());
                    
                    if (maxCharHeight < TempCharacter.getCharacterHeight()) {
                        maxCharHeight = TempCharacter.getCharacterHeight();
                    }
                    if (maxCharWidth < TempCharacter.getCharacterWidth()) {
                        maxCharWidth = TempCharacter.getCharacterWidth();
                    }
                    int[] color = new int[3];
                    color[0] = 255;
                    color[1] = 0;
                    color[2] = 0;

                    for (int m = 0; m < TempCharacter.getCharacterHeight(); m++) {
                        raster.setPixel(TempCharacter.getCharacterStart(), TempWord.getShirorekhaIndex()+m, color);
                        raster.setPixel(TempCharacter.getCharacterEnd(), TempWord.getShirorekhaIndex()+m, color);
                    }

                    for (int l = TempCharacter.getCharacterStart()-1; l < TempCharacter.getCharacterEnd()+1; l++) {
                        raster.setPixel(l, TempWord.getShirorekhaIndex(), color);
                        raster.setPixel(l, TempWord.getShirorekhaIndex()+TempCharacter.getCharacterHeight(), color);
                    }
                }
               }
        }
        return Lines; 
    }

    public void setImage (File a_file) {
        try {
            img = ImageIO.read(a_file);
            image = ImageIO.read(a_file);
        } catch (IOException e2) {
            System.out.println("Error Reading Image");
        }
        raster = image.getRaster();
    }

    public BufferedImage getSegmentedImage() {
        return image;
    }

    public Image getImage() {
        return img;
    }
    @SuppressWarnings("unchecked")
    Vector findLines(int[] pixels, int iw, int ih) {
        Vector Lines = new Vector();
        int[] hp = new int[ih];
        for (int i = 0; i < iw*ih; i++) {
            int p = pixels[i];
            int r = 0xff & (p >> 16);
            int g = 0xff & (p >> 8);
            int b = 0xff & (p);
            int y = (r + g + b)/3;
            Boolean white = false;
            if (y > 150) {
                white = true;
            }
            if (i%iw == 0) {
                hp[i/iw] = 0;
            }
            if (!white) {
                hp[i/iw] ++;
            }
       }
                
       for (int i = 0; i < ih; i ++) {        
            if (hp[i] != 0) {
                CLine Line = new CLine();
                Line.setLineStart(i);
                while (i < ih && hp[i] != 0) {
                    i ++;
                }
                Line.setLineEnd(i);
                while (i < ih && hp[i] == 0) {
                    i ++;
                }
                i --;
                Lines.add(Line);
            }    
        }
        return Lines;
    }

    public void boundCharacter (CLine Line, CWord Word, CCharacter Character) {
        int[] color = new int[3];
        color[0] = 0;
        color[1] = 0;
        color[2] = 255;

        for (int m = 0; m < Character.getCharacterHeight(); m++) {
            raster.setPixel(Character.getCharacterStart(), Word.getShirorekhaIndex()+m, color);
            raster.setPixel(Character.getCharacterEnd(), Word.getShirorekhaIndex()+m, color);
        }

        for (int l = Character.getCharacterStart()-1; l < (int)Character.getCharacterEnd()+1; l++) {
            raster.setPixel(l, Word.getShirorekhaIndex(), color);
            raster.setPixel(l, Word.getShirorekhaIndex()+Character.getCharacterHeight(), color);
        }
    }
    public int findThresholdHeight (Vector Lines, int maxCharHeight)
    {
        int countRegion1 = 0;
        int countRegion2 = 0;
        int countRegion3 = 0;
        int sumOfHeight1 = 0;
        int sumOfHeight2 = 0;
        int sumOfHeight3 = 0;
        int thresholdHeight = 0;
                
        for (int i = 0; i < Lines.size(); i ++) {
            CLine TempLine = (CLine)(Lines.get(i));
            for (int j = 0; j < TempLine.getWords().size(); j ++) {
                Vector Words = TempLine.getWords();
                CWord TempWord = (CWord)Words.get(j);
                for (int k = 0; k < TempWord.getCharacters().size(); k++) {
                    Vector Characters = TempWord.getCharacters();
                    CCharacter TempCharacter = (CCharacter)Characters.get(k);
                    if (TempCharacter.getCharacterHeight() > 0.8*maxCharHeight) {
                        TempCharacter.setCharacterHeightRegion(1);
                        countRegion1 ++;
                        sumOfHeight1 += TempCharacter.getCharacterHeight();
                    } else if (TempCharacter.getCharacterHeight() > 0.64*maxCharHeight) {
                        TempCharacter.setCharacterHeightRegion(2);
                        countRegion2 ++;
                        sumOfHeight2 += TempCharacter.getCharacterHeight();
                    } else {
                        TempCharacter.setCharacterHeightRegion(3);
                        countRegion3 ++;
                        sumOfHeight3 += TempCharacter.getCharacterHeight();
                    }
                }
                
                TempWord.upperModifier(pixels, TempLine.getLineStart(), iw);
            }
        }
        int maxCount = countRegion1;
        int regionWithMaxCharacters = 1;
        if (maxCount < countRegion2) {
            maxCount = countRegion2;
            regionWithMaxCharacters = 2;
        }
        if (maxCount < countRegion3) {
            maxCount = countRegion3;
            regionWithMaxCharacters = 3;
        }
        
        if ( regionWithMaxCharacters == 1) {
            thresholdHeight = sumOfHeight1/countRegion1;
        } else if ( regionWithMaxCharacters == 2) {
            thresholdHeight = sumOfHeight2/countRegion2;
        } else {
            thresholdHeight = sumOfHeight3/countRegion3;
        }
        
        return thresholdHeight;
    }
    
    public int findThresholdWidth (Vector Lines, int maxCharWidth)
    {
        int countRegion1 = 0;
        int countRegion2 = 0;
        int countRegion3 = 0;
        int sumOfWidth1 = 0;
        int sumOfWidth2 = 0;
        int sumOfWidth3 = 0;
        int thresholdWidth = 0;
                
        for (int i = 0; i < Lines.size(); i ++) {
            CLine TempLine = (CLine)(Lines.get(i));
            for (int j = 0; j < TempLine.getWords().size(); j ++) {
                Vector Words = TempLine.getWords();
                CWord TempWord = (CWord)Words.get(j);
                for (int k = 0; k < TempWord.getCharacters().size(); k++) {
                    Vector Characters = TempWord.getCharacters();
                    CCharacter TempCharacter = (CCharacter)Characters.get(k);
                    if (TempCharacter.getCharacterWidth() > 0.8*maxCharWidth) {
                        TempCharacter.setCharacterWidthRegion(1);
                        countRegion1 ++;
                        sumOfWidth1 += TempCharacter.getCharacterWidth();
                    } else if (TempCharacter.getCharacterWidth() > 0.64*maxCharWidth) {
                        TempCharacter.setCharacterWidthRegion(2);
                        countRegion2 ++;
                        sumOfWidth2 += TempCharacter.getCharacterWidth();
                    } else {
                        TempCharacter.setCharacterWidthRegion(3);
                        countRegion3 ++;
                        sumOfWidth3 += TempCharacter.getCharacterWidth();
                    }
                }
            }
        }
        int maxCount = countRegion1;
        int regionWithMaxCharacters = 1;
        if (maxCount < countRegion2) {
            maxCount = countRegion2;
            regionWithMaxCharacters = 2;
        }
        if (maxCount < countRegion3) {
            maxCount = countRegion3;
            regionWithMaxCharacters = 3;
        }
        
        if ( regionWithMaxCharacters == 1) {
            thresholdWidth = sumOfWidth1/countRegion1;
        } else if ( regionWithMaxCharacters == 2) {
            thresholdWidth = sumOfWidth2/countRegion2;
        } else {
            thresholdWidth = sumOfWidth3/countRegion3;
        }
        
        return thresholdWidth;
    }
    
  
    public void update() {}
    
    @Override
    public void paint(Graphics g) {
        
        g.drawImage(image, 0, 0, null);
       
    }
}