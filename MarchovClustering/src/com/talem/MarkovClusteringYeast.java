package com.talem;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MarkovClusteringYeast {
	private static final int powerCoeff = 2;
	private static final double inflationCoeff = 1.22;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MarkovClusteringYeast objYeast = new MarkovClusteringYeast();
		CommonMethods objCommon = new CommonMethods();
		String cluFileName = "PajecFile_yeast_NEW.clu";
		String inputFileName = "yeast_undirected_metabolic.txt";
		double[][] adjMatrix = objYeast.createMatrix(inputFileName);
		for(int i=0;i<adjMatrix.length;i++){
			adjMatrix[i][i] = 1;
		}
		double[][] adjMatrixOld = new double[adjMatrix.length][adjMatrix.length];
		//int count = 0;
		while(!objYeast.isEqual(adjMatrixOld, adjMatrix, adjMatrix.length)){
			adjMatrixOld = adjMatrix;
			adjMatrix = objCommon.NormalizedMatrix(adjMatrix, adjMatrix.length);
			adjMatrix = objCommon.ExpansionMatrix(adjMatrix, adjMatrix.length, powerCoeff);
			//Inflating the matrix with inflation coefficient of 1.2
			for(int i=0;i<adjMatrix.length;i++){
				for(int j=0;j<adjMatrix.length;j++){
					adjMatrix[i][j] = Math.pow(adjMatrix[i][j], inflationCoeff);
				}
			}
			//pruning the matrix
//			for(int i=0;i<adjMatrix.length;i++){
//				for(int j=0;j<adjMatrix.length;j++){
//					if(adjMatrix[i][j] > 0.99){
//						adjMatrix[i][j] = 1;
//					}
//					else if(adjMatrix[i][j] < 0.01){
//						adjMatrix[i][j] = 0;
//					}
//				}
//			}
			//count++;
		}
		//System.out.println("# of Rotations: " + count);
		ArrayList<ArrayList<Integer>> clusterList = new ArrayList<ArrayList<Integer>>();
		for(int i =0;i<adjMatrix.length;i++){
			if(adjMatrix[i][i]>0){
				ArrayList<Integer> list = new ArrayList<Integer>();
				for(int j =0; j<adjMatrix[i].length;j++){
					if(adjMatrix[i][j] > 0){
						list.add(j);
					}
				}
				if(!objCommon.checkIfListExist(clusterList, list, clusterList.size())){
					clusterList.add(list);
				}
			}
		}
		System.out.println("# of Clusters: " + clusterList.size());
		objCommon.generatePajekFile(adjMatrix.length, clusterList, cluFileName);
		System.out.println("Pajek file (PajekFile.clu) for yeast_undirected_metabolic.txt is generated");
	}
	public double[][] createMatrix(String fileName){
		ArrayList<Integer> list = new ArrayList<Integer>();
		FileReader fr;
		double[][] adjMatrix = null ;
		try {
			String line;
			fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);
			
			while((line = br.readLine())!=null){
				String[] lineArray1 = line.split("\\s+");
				for(int i=0;i<lineArray1.length;i++){
					if (!list.contains(Integer.parseInt(lineArray1[i]))) {
						list.add(Integer.parseInt(lineArray1[i]));
					}
				}
			}	
			br.close();
			adjMatrix = new double[list.size()][list.size()];
			FileReader fReader = new FileReader(fileName);
			BufferedReader bReader = new BufferedReader(fReader);
			
			while((line = bReader.readLine())!=null){
				String[] lineArray2 = line.split("\\s+");
				int x = -1;
				for(int i=0;i<list.size();i++){
					if(list.get(i) == Integer.parseInt(lineArray2[0])){
						x = i;
						break;
					}
				}
				int y = -1;
				for(int i=0;i<list.size();i++){
					if(list.get(i) == Integer.parseInt(lineArray2[1])){
						y = i;
						break;
					}
				}
				if(x != -1 && y != -1){
					adjMatrix[x][y] = 1;
					adjMatrix[y][x] = 1;
				}
			}
			bReader.close();
		}catch(FileNotFoundException fne){
			System.out.println("File not found");
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return adjMatrix;
	}
	public boolean isEqual(double[][] adjMatrixOld, double[][] adjMatrixNew, int size){
		for(int i=0;i<size;i++){
			for(int j=0;j<size;j++){
				if(adjMatrixOld[i][j] != adjMatrixNew[i][j]){
					return false;
				}
			}
		}   
		return true;
	}
}
