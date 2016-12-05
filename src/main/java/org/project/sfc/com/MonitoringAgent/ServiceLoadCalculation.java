package org.project.sfc.com.MonitoringAgent;

import Jama.Matrix;
import java.lang.Math.*;
/**
 * Created by mah on 11/16/16.
 */
public class ServiceLoadCalculation {
  public ServiceLoadCalculation() {

  }

  public double CalculateServiceLoads(double [][] InvolvedServicesArray,double [] TotalVNFLoadsArray, int ServicePositionNumber){

    //Creating Matrix Objects with arrays
    Matrix InvolvedServices = new Matrix(InvolvedServicesArray);
    Matrix TotalVNFLoads = new Matrix(TotalVNFLoadsArray, TotalVNFLoadsArray.length);

    //Calculate Solved Matrix
    Matrix ans = InvolvedServices.solve(TotalVNFLoads);

    return ans.get(ServicePositionNumber, 0);

  }
}
