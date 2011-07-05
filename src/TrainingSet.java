import java.io.*;
import java.util.*;

public class TrainingSet {

  protected int inputCount;
  protected int outputCount;
  protected double input[][];
  protected double output[][];
  protected double classify[];
  protected int trainingSetCount;

  
  TrainingSet ( int inputCount , int outputCount )
  {
    this.inputCount = inputCount;
    this.outputCount = outputCount;
    trainingSetCount = 0;
  }

  
  public int getInputCount()
  {
    return inputCount;
  }

  public int getOutputCount()
  {
    return outputCount;
  }

  public void setTrainingSetCount(int trainingSetCount)
  {
    this.trainingSetCount = trainingSetCount;
    input = new double[trainingSetCount][inputCount];
    output = new double[trainingSetCount][outputCount];
    classify = new double[trainingSetCount];
  }

  public int getTrainingSetCount()
  {
    return trainingSetCount;
  }

  void setInput(int set,int index,double value)
  throws RuntimeException
  {
    if ( (set<0) || (set>=trainingSetCount) )
      throw(new RuntimeException("Training set out of range:" + set ));
    if ( (index<0) || (index>=inputCount) )
      throw(new RuntimeException("Training input index out of range:" + index ));
    input[set][index] = value;
  }

  void setOutput(int set,int index,double value)
  throws RuntimeException
  {
    if ( (set<0) || (set>=trainingSetCount) )
      throw(new RuntimeException("Training set out of range:" + set ));
    if ( (index<0) || (set>=outputCount) )
      throw(new RuntimeException("Training input index out of range:" + index ));
    output[set][index] = value;
  }

  void setClassify(int set,double value)
  throws RuntimeException
  {
    if ( (set<0) || (set>=trainingSetCount) )
      throw(new RuntimeException("Training set out of range:" + set ));
    classify[set] = value;
  }



  double getInput(int set,int index)
  throws RuntimeException
  {
    if ( (set<0) || (set>=trainingSetCount) )
      throw(new RuntimeException("Training set out of range:" + set ));
    if ( (index<0) || (index>=inputCount) )
      throw(new RuntimeException("Training input index out of range:" + index ));
    return input[set][index];
  }

  
  double getOutput(int set,int index)
  throws RuntimeException
  {
    if ( (set<0) || (set>=trainingSetCount) )
      throw(new RuntimeException("Training set out of range:" + set ));
    if ( (index<0) || (set>=outputCount) )
      throw(new RuntimeException("Training input index out of range:" + index ));
    return output[set][index];
  }

  
  double getClassify(int set)
  throws RuntimeException
  {
    if ( (set<0) || (set>=trainingSetCount) )
      throw(new RuntimeException("Training set out of range:" + set ));
    return classify[set];
  }

  
  void CalculateClass(int c)
  {
    for ( int i=0;i<=trainingSetCount;i++ ) {
      classify[i] = c + 0.1;
    }
  }
  

  double []getOutputSet(int set)
  throws RuntimeException
  {
    if ( (set<0) || (set>=trainingSetCount) )
      throw(new RuntimeException("Training set out of range:" + set ));
    return output[set];
  }

  
  double []getInputSet(int set)
  throws RuntimeException
  {
    if ( (set<0) || (set>=trainingSetCount) )
      throw(new RuntimeException("Training set out of range:" + set ));
    return input[set];
  }
}
