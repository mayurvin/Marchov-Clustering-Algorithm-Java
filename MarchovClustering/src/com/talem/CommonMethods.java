package com.talem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class CommonMethods {
	
	public double[][] NormalizedMatrix(double[][] adjMatrixNorm, int size) {
		double[] sums = new double[size];
		//double sum = 0;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				sums[i] = sums[i] + adjMatrixNorm[j][i];
				//sum += adjMatrixNorm[j][i];
			}
//			for (int j = 0; j < adjMatrixNorm[i].length; j++) {
//				adjMatrixNorm[j][i] = sum;
//			}
		}
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				adjMatrixNorm[i][j] = adjMatrixNorm[i][j] / sums[j];
			}
		}
		return adjMatrixNorm;
	}
	
	public  double[][] ExpansionMatrix(double[][] adjMatrixExp, int size, int powerCoeff){
		double[][] expansionMatrix=new double[size][size];
		double[][] adjMatrixExpNew = adjMatrixExp;
		int pow = 2;
		while(pow++ <= powerCoeff){
			for(int i=0;i<size;i++)
			{
				for(int j=0;j<size;j++)
				{
					double sum=0.0;
					for(int k=0;k<size;k++)
					{
						sum += adjMatrixExpNew[i][k] * adjMatrixExpNew[k][j];
					}
					expansionMatrix[i][j] = sum;
				}
			}
			adjMatrixExpNew = expansionMatrix;
		}	
		return expansionMatrix;
	}
	public boolean checkIfListExist(ArrayList<ArrayList<Integer>> clusterLists, ArrayList<Integer> list, int size){
		boolean flag = false;
		if(clusterLists != null && size > 0){
			for(int i=0;i<size;i++){
				ArrayList<Integer> newList = clusterLists.get(i);
				if(newList.size() == list.size()){
					flag = true;
					Collections.sort(list);
					Collections.sort(newList);
					for(int j=0;j<newList.size();j++){
						if(newList.get(j)!= list.get(j)){
							flag = false;
							break;
						}
					}
				}
			}
		}else{
			flag = false;
		}
		return flag;
	}
	public void generatePajekFile(int clusterSize, ArrayList<ArrayList<Integer>> finalClusters, String fileName){
		try {
			File file = new File(fileName);
			if(!file.exists())
				file.createNewFile();
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			int count = 0; 
			bw.write("*Vertices " + clusterSize);
			while(count<clusterSize){
				for(int i=0;i<finalClusters.size();i++){
					ArrayList<Integer> list = finalClusters.get(i);
					for(int j=0;j<list.size();j++){
						if(list.get(j) == count){
							bw.newLine();
							bw.write(new Integer(i).toString());
							count++;
						}
					}
				}
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
