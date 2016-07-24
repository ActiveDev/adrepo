package com.activedevsolutions.services.stock;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.activedevsolutions.services.stock.model.RestError;
import com.activedevsolutions.services.stock.model.Stock;

/**
 * Very simple demonstration of one way to implement a REST controller that
 * follows the REST conventions. This Stock controller will perform CRUD
 * operations on Stock objects.
 * 
 * At the moment, it uses one exception handler to return 400 Bad Request.
 * This is because each method will update the http status as it sees fit.
 * The alternative is for each validation to throw a specific exception and have
 * an exception handler that will return the appropriate http status based on the
 * exception class.
 */
@ControllerAdvice
@RestController
@RequestMapping("/v1.0")
public class StockController {
	// Our in-memory list keyed by the id (
	private Map<String, Stock> stocks = new HashMap<>();
	
	/**
	 * REST Conventions use a POST for adding an object.
	 * This method will add a stock object to our in-memory list.
	 * For simplicity, we won't be doing any unique constraints.
	 * 
	 * @param symbol is the symbol of the stock
	 * @param comapny is the name of the company who owns the stock
	 * @param exchange is the stock exchange where this stock is located
	 * @param price is the current price of the stock
	 * 
	 * @return Stock object containing the newly created item
	 */
	@RequestMapping(value = "/stocks", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	public Stock add(@RequestParam(value = "symbol") String symbol,
			@RequestParam(value = "company") String company,
			@RequestParam(value = "exchange") String exchange,
			@RequestParam(value = "price") String price) {
 		
		// Create the stock object with the values passed in.
		// NOTE, not unique constraints are enforced in this sample code
		final Stock stock = new Stock();
		stock.setSymbol(symbol);
		stock.setCompany(company);
		stock.setExchange(exchange);
		stock.setPrice(price);
		
		// Let's just fake a generated id so we can send something extra back
		// for demonstration purposes
		final String id = UUID.randomUUID().toString();
		stock.setId(id);
		
		// Demo purposes so there is no thread safety here
		stocks.put(id, stock);
		
		return stock;
	}

	/**
	 * REST Conventions use a PATCH for partially updating an object.
	 * In this case, we will be updating the price on a particular
	 * stock.
	 * 
	 * @param price is the value to update the stock with
	 * 
	 * @return Stock object containing the newly created item
	 */
	@RequestMapping(value = "/stocks/{id}", method = RequestMethod.PATCH, produces = "application/json")
	@ResponseBody
	public Stock update(HttpServletResponse response, @PathVariable("id") String id, 
			@RequestParam(value = "price") String price) {
 		
		Stock stock = null;
		
		// Demo purposes so there is no thread safety here
		if (stocks.containsKey(id)) {
			stock = stocks.get(id);
			stock.setPrice(price);
		}
		else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		} // end if
		
		return stock;
	}
	
	/**
	 * List all of the stocks in our list.
	 * 
	 * @param exchange is used to filter the list to only show ones belonging to a specific exchange
	 * @param sorting is used to indicate which fields to sort on
	 * @return Collection of Stock objects matching any filters and sorting passed in
	 */
	@RequestMapping(value = "/stocks", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Collection<Stock> list(@RequestParam(value = "exchange", required = false) String exchange,
			@RequestParam(value = "sorting", required = false) String sorting) {

		//TODO implement filtering
		//TODO implement sorting
		
		return stocks.values();
	}

	/**
	 * Gets a very specific Stock object our of the in-memory list
	 * based on the id passed in.
	 * 
	 * @param id of the stock to retrieve
	 * @return Stock representing the object retrieved by the id
	 */
	@RequestMapping(value = "/stocks/{id}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Stock getItem(HttpServletResponse response, @PathVariable("id") String id) {
		Stock stock = null;
		
		if (stocks.containsKey(id)) {
			stock = stocks.get(id);
		}
		else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		} // end if
		
		return stock;
	}

	/**
	 * Deletes a very specific Stock object our of the in-memory list
	 * based on the id passed in.
	 * 
	 * @param id of the stock to retrieve
	 * @return Stock representing the object retrieved by the id
	 */
	@RequestMapping(value = "/stocks/{id}", method = RequestMethod.DELETE, produces = "application/json")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteItem(HttpServletResponse response, @PathVariable("id") String id) {		
		// Demo purposes so there is no thread safety here
		if (stocks.containsKey(id)) {
			stocks.remove(id);
		}
		else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		} // end if
	}
	
	/**
	 * Exception handler that will handle anything that derives from the
	 * Exception class. It will return a HTTP 400 for all exceptions
	 * 
	 * @param e is the Exception object containing the exception thrown
	 * @return CalcError containing the exception message
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = Exception.class)
	public RestError handleBaseException(Exception e) {
		final RestError calcError = new RestError();
		calcError.setMessage(e.getMessage());

		return calcError;
	}
}