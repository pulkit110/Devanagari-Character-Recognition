import javax.swing.*;
import java.awt.*;
import java.util.*;
import Segmentation.*;
import java.awt.image.*;
import java.awt.image.PixelGrabber;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;


public class MainEntry extends JFrame implements Runnable {
 
  static final int DOWNSAMPLE_WIDTH = 15;
  static final int DOWNSAMPLE_HEIGHT = 20;

    private Segment Segmenter;
    private Image img;
    private int iw;
    private int ih;
    private int LineNo;
    private int WordNo;
    private int CharNo;
    private int pixels[];
    BufferedImage selectedImage;
    BufferedImage shownImage;
    JScrollPane pictureScrollPane;
    String RecognizedText;

    Vector Lines;
 
  Entry entry;
  
  Sample sample;    
 
  DefaultListModel letterListModel = new DefaultListModel();
  
  KohonenNetwork net;
  
  Thread trainThread = null;
  private Frame frame;
 
    @SuppressWarnings("deprecation")
  MainEntry()
  {
    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setResizable(false);

    RecognizedText = new String("");
    LineNo = -1;
    WordNo = 0;
    CharNo = 0;
    Lines = new Vector();
    Segmenter = new Segment();

    getContentPane().setLayout(null);

    entry = new Entry();
    entry.reshape(168,25,200,128);
    getContentPane().add(entry);

    sample = new Sample(DOWNSAMPLE_WIDTH,DOWNSAMPLE_HEIGHT);
    sample.reshape(228,210,DOWNSAMPLE_WIDTH*5,DOWNSAMPLE_HEIGHT*4);
    entry.setSample(sample);
    getContentPane().add(sample);   

    setTitle("Devanagari Character Recognition");
    getContentPane().setLayout(null);
    setSize(950,335);               //Set Window Width and Height
    setVisible(false);
    JLabel1.setText("Letters Known");
    getContentPane().add(JLabel1);
    JLabel1.setBounds(12,12,84,12);

    /*
     * Add Scrolling Pane to Display Picture
     */
    pictureScrollPane = new JScrollPane();
    getContentPane().add(pictureScrollPane);
    pictureScrollPane.setBounds(400, 12, 200, 200);
    pictureScrollPane.setAlignmentX(CENTER_ALIGNMENT);
    pictureScrollPane.setAlignmentY(CENTER_ALIGNMENT);
    pictureScrollPane.setViewportView(new JLabel("                   Select an Image"));

    getContentPane().add(JScrollPaneText);
    JScrollPaneText.setBounds(700, 12, 200, 200);
    //JTextBox.setBounds(700, 12, 200, 200);
    JTextBox.setBackground(new java.awt.Color(225, 225, 225));
    JTextBox.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Recognized Text", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Georgia", 0, 11))); // NOI18N
    JScrollPaneText.setViewportView (JTextBox);
    //JTextBox.setText("Click on Recognize Text");

    /*RecognizeImage.setText("Recognize Image");
    RecognizeImage.setActionCommand("RecognizeImage");
    getContentPane().add(RecognizeImage);
    RecognizeImage.setBounds(168,230,135,27);
    */
    RecognizeText.setText(">>");
    RecognizeText.setActionCommand("RecognizeText");
    getContentPane().add(RecognizeText);
    RecognizeText.setBounds(620,90,50,27);
    Browse.setText("Browse for Image");
    Browse.setActionCommand("Browse");
    getContentPane().add(Browse);
    Browse.setBounds(435,220,135,27);
    downSample.setText("Down Sample");
    downSample.setActionCommand("Down Sample");
    getContentPane().add(downSample);
    downSample.setBounds(252,180,120,24);
    add.setText("Add");
    add.setActionCommand("Add");
    getContentPane().add(add);
    add.setBounds(168,156,84,24);
    addImage.setText("Add from Image");
    addImage.setActionCommand("AddImage");
    getContentPane().add(addImage);
    addImage.setBounds(435,245,135,27);
    clear.setText("Clear");
    clear.setActionCommand("Clear");
    getContentPane().add(clear);
    clear.setBounds(168,180,84,24);
    recognize.setText("Recognize");
    recognize.setActionCommand("Recognize");
    getContentPane().add(recognize);
    recognize.setBounds(252,156,120,24);
    JScrollPane1.setVerticalScrollBarPolicy(
    javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    JScrollPane1.setOpaque(true);
    getContentPane().add(JScrollPane1);
    JScrollPane1.setBounds(12,24,144,132);
    JScrollPane1.getViewport().add(letters);
    letters.setBounds(0,0,126,129);
    del.setText("Delete");
    del.setActionCommand("Delete");
    getContentPane().add(del);
    del.setBounds(12,156,144,24);
    load.setText("Load");
    load.setActionCommand("Load");
    getContentPane().add(load);
    load.setBounds(12,180,72,24);
    save.setText("Save");
    save.setActionCommand("Save");
    getContentPane().add(save);
    save.setBounds(84,180,72,24);
    train.setText("Begin Training");
    train.setActionCommand("Begin Training");
    getContentPane().add(train);
    train.setBounds(12,204,144,24);
    JLabel8.setHorizontalTextPosition(
    javax.swing.SwingConstants.CENTER);
    JLabel8.setHorizontalAlignment(
    javax.swing.SwingConstants.CENTER);
    JLabel5.setText("Draw Letters Here");
    getContentPane().add(JLabel5);
    JLabel5.setBounds(204,12,144,12);

    //{{REGISTER_LISTENERS
    SymAction lSymAction = new SymAction();
    downSample.addActionListener(lSymAction);
    clear.addActionListener(lSymAction);
    add.addActionListener(lSymAction);
    addImage.addActionListener(lSymAction);
    imageButton.addActionListener(lSymAction);
    del.addActionListener(lSymAction);
    Browse.addActionListener(lSymAction);
    RecognizeImage.addActionListener(lSymAction);
    RecognizeText.addActionListener(lSymAction);
    SymListSelection lSymListSelection = new SymListSelection();
    letters.addListSelectionListener(lSymListSelection);
    load.addActionListener(lSymAction);
    save.addActionListener(lSymAction);
    train.addActionListener(lSymAction);
    recognize.addActionListener(lSymAction);
   
    letters.setModel(letterListModel);
   
  }  
    @SuppressWarnings("deprecation")
  public static void main(String args[])
  {
    (new MainEntry()).show();
  }
 
  javax.swing.JLabel JLabel1 = new javax.swing.JLabel();
  javax.swing.JLabel JLabel2 = new javax.swing.JLabel();

  javax.swing.JButton downSample = new javax.swing.JButton();
  javax.swing.JButton add = new javax.swing.JButton();
  javax.swing.JButton addImage = new javax.swing.JButton();
  javax.swing.JButton clear = new javax.swing.JButton();
  javax.swing.JButton imageButton = new javax.swing.JButton();
   javax.swing.JButton Browse = new javax.swing.JButton();
   javax.swing.JButton recognize = new javax.swing.JButton();
   javax.swing.JButton RecognizeImage = new javax.swing.JButton();
   javax.swing.JButton RecognizeText = new javax.swing.JButton();
   javax.swing.JScrollPane JScrollPane1 = new javax.swing.JScrollPane();
   javax.swing.JScrollPane JScrollPaneText = new javax.swing.JScrollPane();

   javax.swing.JTextArea JTextBox = new javax.swing.JTextArea();
   
  javax.swing.JList letters = new javax.swing.JList();

  File file;
 
  javax.swing.JButton del = new javax.swing.JButton();

  
  javax.swing.JButton load = new javax.swing.JButton();

 
  javax.swing.JButton save = new javax.swing.JButton();

 
  javax.swing.JButton train = new javax.swing.JButton();
  javax.swing.JLabel JLabel3 = new javax.swing.JLabel();
  javax.swing.JLabel JLabel4 = new javax.swing.JLabel();

  javax.swing.JLabel tries = new javax.swing.JLabel();

 
  javax.swing.JLabel lastError = new javax.swing.JLabel();

  
  javax.swing.JLabel bestError = new javax.swing.JLabel();
  javax.swing.JLabel JLabel8 = new javax.swing.JLabel();
  javax.swing.JLabel JLabel5 = new javax.swing.JLabel();

    final String[] choices = new String[] {"अ", "आ", "इ", "ई", "उ", "ऊ", "ऋ", "ए", "ऐ", "ओ", "औ", "क", "ख", "ग", "घ", "च", "छ", "ज", "झ", "ट", "ठ", "ड", "ढ", "त", "थ", "द", "ध", "न", "प", "फ", "ब", "भ", "म", "य", "र", "ल", "व", "श", "ष", "स", "ह", "०", "१", "२", "३", "४", "५", "६", "७", "८", "९"};
 
  class SymAction implements java.awt.event.ActionListener {
    public void actionPerformed(java.awt.event.ActionEvent event)
    {
      Object object = event.getSource();
      if ( object == downSample )
        downSample_actionPerformed(event);
      else if ( object == clear )
        clear_actionPerformed(event);
      else if ( object == add )
        add_actionPerformed(event);
      else if ( object == del )
        del_actionPerformed(event);
      else if ( object == load )
        load_actionPerformed(event);
      else if ( object == save )
        save_actionPerformed(event);
      else if ( object == train )
        train_actionPerformed(event);
      else if ( object == recognize )
        recognize_actionPerformed(event);
      else if ( object == Browse) 
          Browse_actionPerformed(event);          
      else if ( object == RecognizeImage)
          recognizeImage_actionPerformed(event);
      else if ( object == RecognizeText)
          recognizeText_actionPerformed(event);
      else if ( object == addImage)
          addImage_actionPerformed(event);
      else if (object == imageButton)
          imageButton_actionPerformed(event);
      }
  }

  void Browse_actionPerformed(java.awt.event.ActionEvent event)
  {   

/*      try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      } catch (Exception e) {
        e.printStackTrace();
      }
*/
      JFileChooser _fileChooser = new JFileChooser();
      int retval = _fileChooser.showOpenDialog(MainEntry.this);
/*      try {
          UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
      } catch (Exception e) {
          e.printStackTrace();
      }
 */
      final String[] okFileExtensions = new String[] {"jpg", "png", "gif", "bmp", "jpeg"};

      if (retval == JFileChooser.APPROVE_OPTION) {
            try {               
                file = _fileChooser.getSelectedFile();
                Boolean flag = false;
                for (String extension : okFileExtensions) {
                    if (file.getName().toLowerCase().endsWith(extension)) {
                        flag = true;
                    }
                }
                if (!flag) {
                    JOptionPane.showMessageDialog(this, "Please choose a jpg, jpeg, png, bmp or gif file only.","Error",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                LineNo = -1;
                WordNo = 0;
                CharNo = 0;
                //System.out.println(file.getCanonicalFile());
                selectedImage = ImageIO.read(file);
                shownImage = selectedImage;
                imageButton.setIcon (new ImageIcon(selectedImage));
                pictureScrollPane.setViewportView(imageButton);
            } catch (IOException ex) {
                Logger.getLogger(MainEntry.class.getName()).log(Level.SEVERE, null, ex);
            }
      }


  }

  void downSample_actionPerformed(java.awt.event.ActionEvent event)
  {
      entry.downSample();
  }


  void clear_actionPerformed(java.awt.event.ActionEvent event)
  {
    entry.clear();
    sample.getData().clear();
    sample.repaint();
  }
 
  void add_actionPerformed(java.awt.event.ActionEvent event)
  {
    int i;

    final JPanel panel = new JPanel();

    final JComboBox addLetters = new JComboBox(choices);
    addLetters.setEditable(true);
    panel.add(addLetters);

    String letter = new String();
    if (JOptionPane.showConfirmDialog (null, panel,"Add Letter", JOptionPane.OK_CANCEL_OPTION) ==JOptionPane.OK_OPTION) {
        letter = (String)addLetters.getSelectedItem();
    } else {
        return;
    }

    if ( letter==null || letter.length() == 0) {
        JOptionPane.showMessageDialog(this, "You didn't enter any letter.","Error",JOptionPane.ERROR_MESSAGE);
        return;
    }
      

    if ( letter.length()>1 ) {
      JOptionPane.showMessageDialog(this, "Please enter only a single letter.", "Error", JOptionPane.ERROR_MESSAGE);
      return;
    }

    entry.downSample();
    SampleData sampleData = (SampleData)sample.getData().clone();
    sampleData.setLetter(letter.charAt(0));

    for ( i=0;i<letterListModel.size();i++ ) {
      Comparable str = (Comparable)letterListModel.getElementAt(i);
      /*if ( str.equals(letter) ) {
        JOptionPane.showMessageDialog(this,
                                      "That letter is already defined, delete it first!","Error",
                                      JOptionPane.ERROR_MESSAGE);           
        return;
      }*/

      if ( str.compareTo(sampleData)>0 ) {
        letterListModel.add(i,sampleData);
        return;
      }
    }
    letterListModel.add(letterListModel.size(),sampleData);
    letters.setSelectedIndex(i);
    entry.clear();
    sample.repaint();

  }

void addImage_actionPerformed(java.awt.event.ActionEvent event)
  {
    if (selectedImage == null) {
        JOptionPane.showMessageDialog(this, "Select an Image first!", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    int i;

    final JPanel panel = new JPanel();

    final JComboBox addLetters = new JComboBox(choices);
    addLetters.setEditable(true);
    panel.add(addLetters);

    String letter = new String();
    if (JOptionPane.showConfirmDialog (null, panel,"Add Letter", JOptionPane.OK_CANCEL_OPTION) ==JOptionPane.OK_OPTION) {
        letter = (String)addLetters.getSelectedItem();
    } else {
        return;
    }

    if ( letter==null || letter.length() == 0) {
        JOptionPane.showMessageDialog(this, "You didn't enter any letter.","Error",JOptionPane.ERROR_MESSAGE);
        return;
    }

    if ( letter.length()>1 ) {
      JOptionPane.showMessageDialog(this,
                                    "Please enter only a single letter.","Error",
                                    JOptionPane.ERROR_MESSAGE);
      return;
    }

    if (LineNo == -1) {
            try {
            img = ImageIO.read(file);
            iw = img.getWidth(null);
            ih = img.getHeight(null);
            pixels = new int[iw*ih];
            PixelGrabber pg = new PixelGrabber(img, 0, 0, iw, ih, pixels, 0, iw);
            pg.grabPixels();
        } catch (InterruptedException e1) {
        } catch (IOException e2) {
        }
        Segmenter.setImage(file);
        Lines = Segmenter.makeDataSet(pixels, iw, ih);
        LineNo ++;
        shownImage = Segmenter.getSegmentedImage();
        imageButton.setIcon (new ImageIcon(shownImage));
        pictureScrollPane.setViewportView(imageButton);
        //pictureScrollPane.setViewportView(new JLabel(new ImageIcon(Segmenter.getSegmentedImage())));
    }
    CLine TempLine = (CLine) Lines.get(LineNo);
    CWord TempWord = (CWord) TempLine.getWords().get(WordNo);
    CCharacter TempCharacter = (CCharacter) TempWord.getCharacters().get(CharNo);
    Segmenter.boundCharacter(TempLine, TempWord, TempCharacter);
    shownImage = Segmenter.getSegmentedImage();
    imageButton.setIcon (new ImageIcon(shownImage));
    pictureScrollPane.setViewportView(imageButton);
    //pictureScrollPane.setViewportView(new JLabel(new ImageIcon(Segmenter.getSegmentedImage())));
    CharNo ++;
    if (CharNo >= TempWord.getCharacters().size()) {
        WordNo ++;
        CharNo = 0;
    }
    if (WordNo >= TempLine.getWords().size()) {
        LineNo ++;
        WordNo = 0;
    }
    entry.downSample(img, TempCharacter.getCharacterStart(), TempWord.getShirorekhaIndex(), TempCharacter.getCharacterEnd() - TempCharacter.getCharacterStart(), TempLine.getLineEnd() - TempWord.getShirorekhaIndex(), iw, ih);
    //entry.downSample();
    SampleData sampleData = (SampleData)sample.getData().clone();
    sampleData.setLetter(letter.charAt(0));
    
    for ( i=0;i<letterListModel.size();i++ ) {
      Comparable str = (Comparable)letterListModel.getElementAt(i);
      if ( str.equals(letter) ) {
        JOptionPane.showMessageDialog(this,
                                      "That letter is already defined, delete it first!","Error",
                                      JOptionPane.ERROR_MESSAGE);
        return;
      }

      if ( str.compareTo(sampleData)>0 ) {
        letterListModel.add(i,sampleData);
        return;
      }
    }
    letterListModel.add(letterListModel.size(),sampleData);
    letters.setSelectedIndex(i);
    entry.clear();
    sample.repaint();
    if (LineNo >= Lines.size()) {
        JOptionPane.showMessageDialog(this,
                                    "Reached the end of image.","Completed",
                                    JOptionPane.INFORMATION_MESSAGE);
        CharNo = 0;
        WordNo = 0;
        LineNo = 0;
      return;
    }

  }
  
  void del_actionPerformed(java.awt.event.ActionEvent event)
  {
    int i = letters.getSelectedIndex();

    if ( i==-1 ) {
      JOptionPane.showMessageDialog(this,
                                    "Please select a letter to delete.","Error",
                                    JOptionPane.ERROR_MESSAGE);            
      return;
    }

    letterListModel.remove(i);  
  }

  void imageButton_actionPerformed(java.awt.event.ActionEvent event)
  {
    JFrame ImageFrame = new JFrame("Image");
    JScrollPane ImageScrollPane = new JScrollPane();
    ImageScrollPane.setViewportView(new JLabel(new ImageIcon(shownImage)));
    ImageFrame.add(ImageScrollPane);
    ImageFrame.pack();
    ImageFrame.setVisible(true);
  }

  class SymListSelection implements javax.swing.event.ListSelectionListener {
    public void valueChanged(javax.swing.event.ListSelectionEvent event)
    {
      Object object = event.getSource();
      if ( object == letters )
        letters_valueChanged(event);
    }
  }


  void letters_valueChanged(javax.swing.event.ListSelectionEvent event)
  {
    if ( letters.getSelectedIndex()==-1 )
      return;
    SampleData selected = 
      (SampleData)letterListModel.getElementAt(letters.getSelectedIndex());
    sample.setData((SampleData)selected.clone());
    sample.repaint();
    entry.clear();

  }

 
  void load_actionPerformed(java.awt.event.ActionEvent event)
  {
    try {
      FileReader f;// the actual file stream
      BufferedReader r;// used to read the file line by line
      //f = new FileInputStream( new File("./sample.dat") );
      r = new BufferedReader(new InputStreamReader(new FileInputStream ("./sample.dat"), "UTF8"));
      String line;
      int i=0;

      letterListModel.clear();

      while ( (line=r.readLine()) !=null ) {
        SampleData ds = 
          new SampleData(line.charAt(0),MainEntry.DOWNSAMPLE_WIDTH,MainEntry.DOWNSAMPLE_HEIGHT);            
        letterListModel.add(i++,ds);
        int idx=2;
        for ( int y=0;y<ds.getHeight();y++ ) {
          for ( int x=0;x<ds.getWidth();x++ ) {
            ds.setData(x,y,line.charAt(idx++)=='1');
          }
        }
      }

      r.close();
      clear_actionPerformed(null);
      JOptionPane.showMessageDialog(this,
                                    "Loaded from 'sample.dat'.","Training",
                                    JOptionPane.PLAIN_MESSAGE);                     

    } catch ( Exception e ) {
      JOptionPane.showMessageDialog(this,
                                    "Error: " + e,"Training",
                                    JOptionPane.ERROR_MESSAGE);                             
    }

  }

 
  void save_actionPerformed(java.awt.event.ActionEvent event)
  {
    try {

      BufferedWriter out = new BufferedWriter (new OutputStreamWriter(new FileOutputStream("./sample.dat"), "UTF8"));
      //os = new FileOutputStream( "./sample.dat",false);
      //ps = new PrintStream(os);

      for ( int i=0;i<letterListModel.size();i++ ) {
        SampleData ds = (SampleData)letterListModel.elementAt(i);
        //ps.print( ds.getLetter() + ":" );
        out.write( ds.getLetter() + ":" );
        for ( int y=0;y<ds.getHeight();y++ ) {
          for ( int x=0;x<ds.getWidth();x++ ) {
            out.write( ds.getData(x,y)?"1":"0" );
          }
        }
        out.newLine();
      }

      out.close();
      //ps.close();
      //os.close();
      clear_actionPerformed(null);
      JOptionPane.showMessageDialog(this,
                                    "Saved to 'sample.dat'.",
                                    "Training",
                                    JOptionPane.PLAIN_MESSAGE);                      

    } catch ( Exception e ) {
      JOptionPane.showMessageDialog(this,"Error: " + 
                                    e,"Training",
                                    JOptionPane.ERROR_MESSAGE);                                     
    }

  }

 
  public void run()
  {
    try {
      int inputNeuron = MainEntry.DOWNSAMPLE_HEIGHT*
        MainEntry.DOWNSAMPLE_WIDTH;
      int outputNeuron = letterListModel.size();

      TrainingSet set = new TrainingSet(inputNeuron,outputNeuron);
      set.setTrainingSetCount(letterListModel.size());

      for ( int t=0;t<letterListModel.size();t++ ) {
        int idx=0;
        SampleData ds = (SampleData)letterListModel.getElementAt(t);
        for ( int y=0;y<ds.getHeight();y++ ) {
          for ( int x=0;x<ds.getWidth();x++ ) {
            set.setInput(t,idx++,ds.getData(x,y)?.5:-.5);
          }
        }
      }

      net = new KohonenNetwork(inputNeuron,outputNeuron,this);
      net.setTrainingSet(set);
      net.learn();
    } catch ( Exception e ) {
      JOptionPane.showMessageDialog(this,"Error: " + e,
                                    "Training",
                                    JOptionPane.ERROR_MESSAGE);                             
    }

  }

  
  void updateStats(long trial,double error,double best)
  {
    if ( (((trial%100)!=0) || (trial==10)) && !net.halt )
      return;

    if ( net.halt ) {
      trainThread = null;
      train.setText("Begin Training");
      JOptionPane.showMessageDialog(this,
                                    "Training has completed.","Training",
                                    JOptionPane.PLAIN_MESSAGE);                     
    }
    UpdateStats stats = new UpdateStats();
    stats._tries = trial;
    stats._lastError=error;
    stats._bestError=best;
    try {
      SwingUtilities.invokeAndWait(stats);
    } catch ( Exception e ) {
      JOptionPane.showMessageDialog(this,"Error: " + e,"Training",
                                    JOptionPane.ERROR_MESSAGE);                                       
    }
  }

 
  void train_actionPerformed(java.awt.event.ActionEvent event)
  {
    if ( trainThread==null ) {
      train.setText("Stop Training");
      train.repaint();
      trainThread = new Thread(this);
      trainThread.start();       
    } else {
      net.halt=true;
    }
  }

    public class UpdateStats implements Runnable {
    long _tries;
    double _lastError;
    double _bestError;

    public void run()
    {
      tries.setText(""+_tries);
      lastError.setText(""+_lastError);
      bestError.setText(""+_bestError);
    }
  }
    
  void recognize_actionPerformed(java.awt.event.ActionEvent event)
  {
    {
    if ( net==null ) {
      JOptionPane.showMessageDialog(this,
                                    "I need to be trained first!","Error",
                                    JOptionPane.ERROR_MESSAGE);  
      return;
    }
    entry.downSample();

    double input[] = new double[DOWNSAMPLE_WIDTH*DOWNSAMPLE_HEIGHT];
    int idx=0;
    SampleData ds = sample.getData();
    for ( int y=0;y<ds.getHeight();y++ ) {
      for ( int x=0;x<ds.getWidth();x++ ) {
        input[idx++] = ds.getData(x,y)?.5:-.5;
      }
    }

    double normfac[] = new double[1];
    double synth[] = new double[1];

    int best = net.winner ( input , normfac , synth ) ;
    char map[] = mapNeurons();
    JOptionPane.showMessageDialog(this,
                                  "  That letter is " + map[best], "Recognition Successful",
                                  JOptionPane.PLAIN_MESSAGE);            
    clear_actionPerformed(null);

    }
  }


    @SuppressWarnings("deprecation")
  void recognizeImage_actionPerformed(java.awt.event.ActionEvent event)
  {
    if ( file==null ) {
      JOptionPane.showMessageDialog(this,
                                    "Please Select an Image!","Error",
                                    JOptionPane.ERROR_MESSAGE);  
      return;
    } else if ( net==null ) {
      JOptionPane.showMessageDialog(this,
                                    "I need to be trained first!","Error",
                                    JOptionPane.ERROR_MESSAGE);  
      return;
    } 

    if (LineNo == -1) {
            try {
            img = ImageIO.read(file);
            iw = img.getWidth(null);
            ih = img.getHeight(null);
            pixels = new int[iw*ih];
            PixelGrabber pg = new PixelGrabber(img, 0, 0, iw, ih, pixels, 0, iw);
            pg.grabPixels();
        } catch (InterruptedException e1) {
        } catch (IOException e2) {
        }
        Segmenter.setImage(file);
        Lines = Segmenter.makeDataSet(pixels, iw, ih);
        LineNo ++;
        shownImage = Segmenter.getSegmentedImage();
        imageButton.setIcon (new ImageIcon(shownImage));
        pictureScrollPane.setViewportView(imageButton);
        //pictureScrollPane.setViewportView(new JLabel(new ImageIcon(Segmenter.getSegmentedImage())));
    }
    CLine TempLine = (CLine) Lines.get(LineNo);
    CWord TempWord = (CWord) TempLine.getWords().get(WordNo);
    CCharacter TempCharacter = (CCharacter) TempWord.getCharacters().get(CharNo);
    Segmenter.boundCharacter(TempLine, TempWord, TempCharacter);
    shownImage = Segmenter.getSegmentedImage();
    imageButton.setIcon (new ImageIcon(shownImage));
    pictureScrollPane.setViewportView(imageButton);
    //pictureScrollPane.setViewportView(new JLabel(new ImageIcon(Segmenter.getSegmentedImage())));
    CharNo ++;
    if (CharNo >= TempWord.getCharacters().size()) {
        WordNo ++;
        CharNo = 0;
    }
    if (WordNo >= TempLine.getWords().size()) {
        LineNo ++;
        WordNo = 0;
    }

    entry.downSample(img, TempCharacter.getCharacterStart(), TempWord.getShirorekhaIndex(), TempCharacter.getCharacterEnd() - TempCharacter.getCharacterStart(), TempLine.getLineEnd() - TempWord.getShirorekhaIndex(), iw, ih);

    double input[] = new double[DOWNSAMPLE_WIDTH*DOWNSAMPLE_HEIGHT];
    int idx=0;
    SampleData ds = sample.getData();
    for ( int y=0;y<ds.getHeight();y++ ) {
      for ( int x=0;x<ds.getWidth();x++ ) {
        input[idx++] = ds.getData(x,y)?.5:-.5;
      }
    }

    double normfac[] = new double[1];
    double synth[] = new double[1];

    int best = net.winner ( input , normfac , synth ) ;
    char map[] = mapNeurons();
    JOptionPane.showMessageDialog(this,
                                  "  That letter is " + map[best], "Recognition Successful",
                                  JOptionPane.PLAIN_MESSAGE);            
    clear_actionPerformed(null);

    if (LineNo >= Lines.size()) {
        JOptionPane.showMessageDialog(this,
                                    "Reached the end of image.","Completed",
                                    JOptionPane.INFORMATION_MESSAGE);
        CharNo = 0;
        WordNo = 0;
        LineNo = 0;
      return;
    }
  }

  
  char []mapNeurons()
  {
    char map[] = new char[letterListModel.size()];
    double normfac[] = new double[1];
    double synth[] = new double[1];

    for ( int i=0;i<map.length;i++ )
      map[i]='?';
    for ( int i=0;i<letterListModel.size();i++ ) {
      double input[] = new double[DOWNSAMPLE_WIDTH*DOWNSAMPLE_HEIGHT];
      int idx=0;
      SampleData ds = (SampleData)letterListModel.getElementAt(i);
      for ( int y=0;y<ds.getHeight();y++ ) {
        for ( int x=0;x<ds.getWidth();x++ ) {
          input[idx++] = ds.getData(x,y)?.5:-.5;
        }
      }

      int best = net.winner ( input , normfac , synth ) ;
     
      map[best] = ds.getLetter();
    }
    return map;
  }

  void recognizeText_actionPerformed(java.awt.event.ActionEvent event)
  {
    if ( file==null ) {
      JOptionPane.showMessageDialog(this,
                                    "Please Select an Image!","Error",
                                    JOptionPane.ERROR_MESSAGE);
      return;
    } else if ( net==null ) {
      JOptionPane.showMessageDialog(this,
                                    "I need to be trained first!","Error",
                                    JOptionPane.ERROR_MESSAGE);
      return;
    }
 
    RecognizedText = "";
    if (LineNo == -1) {
            try {
            img = ImageIO.read(file);
            iw = img.getWidth(null);
            ih = img.getHeight(null);
            pixels = new int[iw*ih];
            PixelGrabber pg = new PixelGrabber(img, 0, 0, iw, ih, pixels, 0, iw);
            pg.grabPixels();
        } catch (InterruptedException e1) {
        } catch (IOException e2) {
        }
        Segmenter.setImage(file);
        Lines = Segmenter.makeDataSet(pixels, iw, ih);
        LineNo ++;
        shownImage = Segmenter.getSegmentedImage();
        imageButton.setIcon (new ImageIcon(shownImage));
        pictureScrollPane.setViewportView(imageButton);
        //pictureScrollPane.setViewportView(new JLabel(new ImageIcon(Segmenter.getSegmentedImage())));
    }

    for (int i = 0; i < Lines.size(); i ++) {
        CLine TempLine = (CLine)(Lines.get(i));

        for (int j = 0; j < TempLine.getWords().size(); j ++) {
            Vector Words = TempLine.getWords();
            CWord TempWord = (CWord)Words.get(j);
            for (int k = 0; k < TempWord.getCharacters().size(); k++) {
                Vector Characters = TempWord.getCharacters();
                CCharacter TempCharacter = (CCharacter)Characters.get(k);
                Segmenter.boundCharacter(TempLine, TempWord, TempCharacter);
                imageButton.setIcon (new ImageIcon(Segmenter.getSegmentedImage()));
                pictureScrollPane.setViewportView(imageButton);
                //pictureScrollPane.setViewportView(new JLabel(new ImageIcon(Segmenter.getSegmentedImage())));
                entry.downSample(img, TempCharacter.getCharacterStart(), TempWord.getShirorekhaIndex(), TempCharacter.getCharacterEnd() - TempCharacter.getCharacterStart(), TempLine.getLineEnd() - TempWord.getShirorekhaIndex(), iw, ih);
                double input[] = new double[DOWNSAMPLE_WIDTH*DOWNSAMPLE_HEIGHT];
                int idx=0;
                SampleData ds = sample.getData();
                for ( int y=0;y<ds.getHeight();y++ ) {
                  for ( int x=0;x<ds.getWidth();x++ ) {
                    input[idx++] = ds.getData(x,y)?.5:-.5;
                  }
                }

                double normfac[] = new double[1];
                double synth[] = new double[1];

                int best = net.winner ( input , normfac , synth ) ;
                char map[] = mapNeurons();
                RecognizedText += map[best];
                clear_actionPerformed(null);
            }
            RecognizedText += " ";
        }
        RecognizedText += "\n";
    }
    JTextBox.setText(RecognizedText);
    System.out.println (RecognizedText);
  }

}