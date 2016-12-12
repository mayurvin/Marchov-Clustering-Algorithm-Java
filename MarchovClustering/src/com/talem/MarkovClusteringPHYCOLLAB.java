package com.talem;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MarkovClusteringPHYCOLLAB {
	private static final int powerCoeff = 2;
	private static final double inflationCoeff = 1.2;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MarkovClusteringPHYCOLLAB objPhyCollab = new MarkovClusteringPHYCOLLAB();
		CommonMethods objCommon = new CommonMethods();
		String cluFileName = "PajecFile_pyhics_collab_NEW.clu";
		String inputFileName = "physics_collaboration_net.txt";
		double[][] adjMatrix = objPhyCollab.createMatrix(inputFileName);
		for(int i=0;i<adjMatrix.length;i++){
			adjMatrix[i][i] = 1;
		}
		double[][] adjMatrixOld = new double[adjMatrix.length][adjMatrix.length];
		//int count = 0;
		while(!objPhyCollab.isEqual(adjMatrixOld, adjMatrix, adjMatrix.length)){
			adjMatrixOld = adjMatrix;
			adjMatrix = objCommon.NormalizedMatrix(adjMatrix, adjMatrix.length);
			adjMatrix = objCommon.ExpansionMatrix(adjMatrix, adjMatrix.length, powerCoeff);
			//Inflating the matrix with inflation coefficient of 1.3
			for(int i=0;i<adjMatrix.length;i++){
				for(int j=0;j<adjMatrix.length;j++){
					adjMatrix[i][j] = Math.pow(adjMatrix[i][j], inflationCoeff);
				}
			}
			//pruning the matrix
			for(int i=0;i<adjMatrix.length;i++){
				for(int j=0;j<adjMatrix.length;j++){
					if(adjMatrix[i][j] > 0.99){
						adjMatrix[i][j] = 1;
					}
					else if(adjMatrix[i][j] < 0.01){
						adjMatrix[i][j] = 0;
					}
				}
			}
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
		System.out.println("Pajek file (PajekFile.clu) for physics_collaboration.txt is generated");
		
	}

	private boolean isEqual(double[][] adjMatrixOld, double[][] adjMatrixNew, int size) {
		// TODO Auto-generated method stub
		for(int i=0;i<size;i++){
			for(int j=0;j<size;j++){
				if(adjMatrixOld[i][j] != adjMatrixNew[i][j]){
					return false;
				}
			}
		}   
		return true;
	}

	private double[][] createMatrix(String fileName) {
		// TODO Auto-generated method stub
		ArrayList<String> list = new ArrayList<String>();
		FileReader fr;
		double[][] adjMatrix = null ;
		try {
			String line;
			fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);
			while((line = br.readLine())!=null){
				String[] lineArray1 = line.split("\\s+");
				for(int i=0;i<lineArray1.length;i++){
					if (!list.contains(lineArray1[i].toString())) {
						list.add(lineArray1[i].toString());
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
					if(list.get(i).equals(lineArray2[0])){
						x = i;
						break;
					}
				}
				int y = -1;
				for(int i=0;i<list.size();i++){
					if(list.get(i).equals(lineArray2[1])){
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
		}catch(FileNotFoundException ex){
			System.out.println(ex.toString());
		} catch (NumberFormatException ex) {
			System.out.println(ex.toString());
		} catch (IOException ex) {
			System.out.println(ex.toString());
		}
		return adjMatrix;
	}
}
