

public class SampleData implements Comparable,Cloneable {


  protected boolean grid[][];


  protected char letter;

  public SampleData(char letter,int width,int height)
  {
    grid = new boolean[width][height];
    this.letter = letter;
  }


  public void setData(int x,int y,boolean v)
  {
    grid[x][y]=v;
  }


  public boolean getData(int x,int y)
  {
    return grid[x][y];
  }

  public void clear()
  {
    for ( int x=0;x<grid.length;x++ )
      for ( int y=0;y<grid[0].length;y++ )
        grid[x][y]=false;
  }


  public int getHeight()
  {
    return grid[0].length;
  }


  public int getWidth()
  {
    return grid.length;
  }


  public char getLetter()
  {
    return letter;
  }


  public void setLetter(char letter)
  {
    this.letter = letter;
  }


  public int compareTo(Object o)
  {
    SampleData obj = (SampleData)o;
    if ( this.getLetter()>obj.getLetter() )
      return 1;
    else
      return -1;
  }


    @Override
  public String toString()
  {
    return ""+letter;
  }


    @Override
  public Object clone()

  {

    SampleData obj = new SampleData(letter,getWidth(),getHeight());
    for ( int y=0;y<getHeight();y++ )
      for ( int x=0;x<getWidth();x++ )
        obj.setData(x,y,getData(x,y));
    return obj;    
  }
}