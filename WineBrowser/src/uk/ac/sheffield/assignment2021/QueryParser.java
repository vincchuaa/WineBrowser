package uk.ac.sheffield.assignment2021;

import uk.ac.sheffield.assignment2021.codeprovided.AbstractQueryParser;
import uk.ac.sheffield.assignment2021.codeprovided.Query;
import uk.ac.sheffield.assignment2021.codeprovided.SubQuery;
import uk.ac.sheffield.assignment2021.codeprovided.WineProperty;
import uk.ac.sheffield.assignment2021.codeprovided.WineType;
import uk.ac.sheffield.assignment2021.test.common.TestCommon;

import java.util.*;

public class QueryParser extends AbstractQueryParser {
	
	 /**
     * 1 - receives the List of Strings, each is a single token
     * 2 - assesses their content, creates the relevant Query & SubQuery objects
     * 3 - and then returns a List of the Query objects
     *
     * @param queryTokens The List of tokenized Strings from the readQueryFile method
     * @return List of all Query objects
     */
    @Override
    public List<Query> readQueries(List<String> queryTokens){

    	ArrayList<Query> queryList = new ArrayList<Query>();
    	ArrayList<WineType> wineTypeList = new ArrayList<WineType>(sortWineType(queryTokens));
    	List<List<SubQuery>> subQueryList = new ArrayList<>(sortSubQuery(queryTokens));
    	
    	for(int i=0;i<wineTypeList.size();i++) {
    		queryList.add(new Query(subQueryList.get(i), wineTypeList.get(i)));
    	}
    	
        return queryList;
    }
    
    /**
     * Split the tokenized strings into each query
     * @param queryTokens The List of tokenized Strings from the readQueryFile method
     * @return List of all Query statements
     * @throws IllegalArgumentException if the provided query tokens are invalid (e.g. invalid query
     * statements)
     */
    public static List<List<String>> splitQuery(List<String> queryTokens) throws IllegalArgumentException{
    	
    	List<List<String>> data = new ArrayList<>();
    	ArrayList<Integer> pointers = new ArrayList<Integer>();
    	
    	for(int i=0;i<queryTokens.size();i++) {
    		if(queryTokens.get(i).equals("select")) 
    			pointers.add(i);
    		}
    	pointers.add(queryTokens.size());
    	
    	//Split queries into each arraylist
    	for(int i=0;i<pointers.size()-1;i++) {
    		System.out.println(queryTokens.get(pointers.get(i)));
    		List<String> l = new ArrayList<>(queryTokens.subList(pointers.get(i), pointers.get(i+1)));
    		data.add(l);
    	}
    	
    	//Exception Handler Code
    	for(int i=0;i<data.size();i++) {
	    	if(!data.get(i).contains("where")) { 
	    		int counter = i+1;
	    		throw new IllegalArgumentException("Invalid statement, no "
	    				+ "subquery found in Query " + counter);
	    	}
    	}
    	return data;
    }
    
    /**
     * Stores wine type of each query and returns a list of Wine Type objects.
     * @param  queryTokens The List of tokenized Strings from the readQueryFile method
     * @return List of all WineType objects
     * @throws IllegalArgumentException if the provided query tokens are invalid (e.g. invalid wine type
     * or invalid wine type comparison)
     */
    public static List<WineType> sortWineType(List<String> queryTokens) throws IllegalArgumentException{
    	
    	List<List<String>> data = new ArrayList<>(splitQuery(queryTokens));
    	ArrayList<WineType> wineTypeList = new ArrayList<WineType>();
    	
    	for(int i=0;i<data.size();i++) {
    		if(data.get(i).contains("red"))
    			wineTypeList.add(WineType.RED);
    		if(data.get(i).contains("white"))
    			wineTypeList.add(WineType.WHITE);
    		if(data.get(i).contains("or"))
    			wineTypeList.add(WineType.ALL);
    	}
    	
    	//Exception handler code
    	//Throws exception if wrong wine type
    	for(int i=0;i<data.size();i++) {
			if(!data.get(i).get(1).equals("red") && !data.get(i).get(1).equals("white")) {
				int counter = i+1;
				throw new IllegalArgumentException("Invalid wine type found at Query " + counter);
			}
		}
    	
    	//Throws exception if compared with same wine type
    	for(int i=0;i<wineTypeList.size();i++) {
    		if(wineTypeList.get(i).equals(WineType.ALL)) {
    			if(wineTypeList.get(i-1).equals(WineType.RED) 
    					&& wineTypeList.get(i-2).equals(WineType.RED)) {
    				throw new IllegalArgumentException("Invalid query, can't compare red wine "
    						+ "with red wine");
    			}
    			else if(wineTypeList.get(i-1).equals(WineType.WHITE) 
    					&& wineTypeList.get(i-2).equals(WineType.WHITE)) {
    				throw new IllegalArgumentException("Invalid query, can't compare white wine "
    						+ "with white wine");
    			}
    		}
    	}
    	
    	ListIterator<WineType> listIterator = wineTypeList.listIterator();
    	while (listIterator.hasNext()) {
			WineType wineType = listIterator.next();
			
			//Remove the duplicated winetype before and after winetype.ALL
			if (wineType.equals(WineType.ALL)) {
				for(int i=0;i<2;i++) {
					listIterator.previous();
					listIterator.previous();
					listIterator.remove();
					listIterator.next();
				}
			}
		}	
		return wineTypeList;
    }
    
    /**
     * Stores sub queries of each query and returns a nested list of Sub Query objects.
     * @param queryTokens The List of tokenized Strings from the readQueryFile method
     * @return Nested List of all Sub Queries objects
     * @throws IllegalArgumentException if the provided query tokens are invalid(e.g. non-numbers
     * as boundary values, invalid operators, etc)
     */
    public static List<List<SubQuery>> sortSubQuery(List<String> queryTokens) throws IllegalArgumentException{
    	List<List<String>> data = new ArrayList<>(splitQuery(queryTokens));
    	List<List<SubQuery>> subQueryList = new ArrayList<>();
    	List<List<WineProperty>> winePropertyList = new ArrayList<>();
    	List<List<String>> operatorList = new ArrayList<>();
    	List<List<Double>> valueList = new ArrayList<>();
    	
    	//Add all sub queries parameters data into each seperated list
    	for(int i=0;i<data.size();i++) {
    		List<WineProperty> wp = new ArrayList<>();
    		List<String> str = new ArrayList<>();
    		List<Double> db = new ArrayList<>();
    		
    		for(int j=0;j<data.get(i).size();j++) {
    			String temp = data.get(i).get(j);
    			
    			if(temp.equals("where") || temp.equals("and")) {
    				WineProperty wineProperty = WineProperty.fromFileIdentifier(data.get(i).get(j+1));
    				wp.add(wineProperty);
    				str.add(data.get(i).get(j+2));
    				db.add(Double.parseDouble(data.get(i).get(j+3)));
    			}
    		}
    		
    		winePropertyList.add(wp);
    		operatorList.add(str);
    		valueList.add(db);
    	}
    	
    	//Instantiate sub queries and store them inside each seperated list
    	for(int i=0;i<winePropertyList.size();i++) {
    		List<SubQuery> sq = new ArrayList<>();
    		for(int j=0;j<winePropertyList.get(i).size();j++) {
    			sq.add(new SubQuery(winePropertyList.get(i).get(j), 
    					operatorList.get(i).get(j), valueList.get(i).get(j)));
    		}
    		subQueryList.add(sq);
    	}
    	
    	//Exception Handler Code
    	List<String> s = new ArrayList<>();
    	for(int i=0;i<operatorList.size();i++) {
    		s.addAll(operatorList.get(i));
    	}
    	
    	for(int i=0;i<s.size();i++) {
    		//Throw exception when found invalid operator
    		if(!SubQuery.isValidOperator(s.get(i))) {
    			throw new IllegalArgumentException("Invalid operator");
    		}
    	}
    	
    	return subQueryList;
    }
    
    public static void main(String[] args) {
    	List<String> queryTokens = new ArrayList<>(AbstractQueryParser.readQueryFile(TestCommon.QUERIES));
    	List<List<String>> queries = new ArrayList<>(QueryParser.splitQuery(queryTokens));
    	for(int i=0;i<queries.size();i++)
    		System.out.println(queries.get(i));
    	
    }
}
