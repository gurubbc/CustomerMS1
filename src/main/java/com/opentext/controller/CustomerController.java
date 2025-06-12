package com.opentext.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.opentext.model.Customer;
@RestController
public class CustomerController {

	ArrayList<Customer> allCustomers=new ArrayList<Customer>();
	@Autowired
	RestTemplate restTemplate;
	
	
	
	public CustomerController() {
		System.out.println("Preparing the customer details in CustomerController");
		int[] stocksList1= {1,2};
		int[] stocksList2= {2,3};
		int[] stocksList3= {3,4,5};
		int[] stocksList4= {1,2,4,5};
		int[] stocksList5= {5};
		
		Customer c1=new Customer(1, "Guru", "Murthy", "java.guru@yahoo.com",9731801675L,stocksList1) ;
		Customer c2=new Customer(2, "Rohit", "Sharma", "rohit@yahoo.com",3333301675L,stocksList2);
		Customer c3=new Customer(3, "Pradeep", "Patel", "pradeep.patel@gmail.com",456789L,stocksList3);
		Customer c4=new Customer(4, "Sarif", "Rana", "s.r@yahoo.com",987654L, stocksList4);
		Customer c5=new Customer(5, "Yamini", "Rajula", "yr@yahoo.com",456789L, stocksList5);
		allCustomers.add(c1);
		allCustomers.add(c2);
		allCustomers.add(c3);
		allCustomers.add(c4);
		allCustomers.add(c5);
	}
	
	
	// API 1
	@RequestMapping(value="/customers", method=RequestMethod.GET)
	public ArrayList<Customer> getAllcustomers()
	{
		return allCustomers;
	}
	
	// API 2
	@RequestMapping(value="/customers/id/{custId}", method=RequestMethod.GET)
	public ResponseEntity<Object> getACustomer(@PathVariable("custId") int custId) {
		for (Customer c:allCustomers) {
			if (c.getCustomerId()==custId) {
				// success condition
				return ResponseEntity.status(200).body(c);
			}
		}
		// failure condition
		return ResponseEntity.status(404).body("The customer id "+custId+" is not present");
	}
	
	// API 3
	// For a given customer id, it should return an arraylist of stock objects with details
	@RequestMapping(value="/customers/id/{custId}/stocks", method=RequestMethod.GET)
	public ArrayList<Object> getStockDetails(@PathVariable("custId") int custId) {
		ArrayList<Object> allStocksForAGivenCustomer=new ArrayList();
		int stockIds[]=null;
		Object stockTemp=null;
		// check if the customer id exists, if not return null straight away
		for (Customer c:allCustomers) {
			if (c.getCustomerId()==custId) {
				// the customer id is matching
				stockIds=c.getStockIds();// [1,2,3]
				break;
			}
		}
		
		if (stockIds == null) {
			return allStocksForAGivenCustomer; // at this point, this is empty array list
		} else {
			for (int i=0;i<stockIds.length;i++) {
				System.out.println("Stock id is "+stockIds[i]);
				stockTemp=getAStockObject(stockIds[i]);
				allStocksForAGivenCustomer.add(stockTemp);
			}
		}
		
		return allStocksForAGivenCustomer;
	}


	private Object getAStockObject(int stockId) {
		// From here I am going to call the other microservice's end point
		// The input is stock id (integer) and output is Stock object
		// to access other microservice, there is a pre-defined class called RestTemplate
		String stockServiceURL="http://STOCKMS/stocks/{stockId}";
		return restTemplate.getForObject(stockServiceURL, Object.class,stockId);
	}
	
}
