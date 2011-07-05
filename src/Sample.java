import javax.swing.*;
import java.awt.*;


public class Sample extends JPanel {

 
  SampleData data;

  
  Sample(int width,int height)
  {
    data = new SampleData(' ',width,height);
  }

  
  SampleData getData()
  {
    return data;
  }
  
  void setData(SampleData data)
  {
    this.data = data;
  }


    @Override
  public void paint(Graphics g)
  {
    if ( data==null )
      return;

    int x,y;
    int vcell = getHeight()/data.getHeight();
    int hcell = getWidth()/data.getWidth();

    g.setColor(Color.white);
    g.fillRect(0,0,getWidth(),getHeight());

    g.setColor(Color.black);
    for ( y=0;y<data.getHeight();y++ )
      g.drawLine(0,y*vcell,getWidth(),y*vcell);
    for ( x=0;x<data.getWidth();x++ )
      g.drawLine(x*hcell,0,x*hcell,getHeight());

    for ( y=0;y<data.getHeight();y++ ) {
      for ( x=0;x<data.getWidth();x++ ) {
        if ( data.getData(x,y) )
          g.fillRect(x*hcell,y*vcell,hcell,vcell);
      }
    }

    g.setColor(Color.black);
    g.drawRect(0,0,getWidth()-1,getHeight()-1);

  }
}