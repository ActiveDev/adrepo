package com.activedevsolutions.calcdemo;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.activedevsolutions.calcdemo.model.CalcError;
import com.activedevsolutions.calcdemo.model.CalcResult;

/**
 * Very simple demonstration of a REST controller that
 * returns a CalcResult. It also includes an exception handler 
 * that will return a CalcError.
 */
@ControllerAdvice
@RestController
public class CalcController {
	/**
	 * Very very very basic add method.
	 * 
	 * @param numberOne is the first value involved in the addition
	 * @param numberTwo is used to add to the first value
	 * @return CalcResult holding the results of the addition
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
    public CalcResult add(@RequestParam(value="number_one") int numberOne,
    		            @RequestParam(value="number_two") int numberTwo) {
		long result = numberOne + numberTwo;
		CalcResult calcResult = new CalcResult();
		calcResult.setResult(result);
		
        return calcResult;
    }
	
	/**
	 * Very very very basic subtraction method.
	 * 
	 * @param numberOne is the first value involved in the subtraction
	 * @param numberTwo is used to subtract from the first value
	 * @return CalcResult holding the results of the subtraction
	 */
	@RequestMapping(value = "/subtract", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
    public CalcResult subtract(@RequestParam(value="number_one") int numberOne,
    		            @RequestParam(value="number_two") int numberTwo) {
		int result = numberOne - numberTwo;
		CalcResult calcResult = new CalcResult();
		calcResult.setResult(result);
		
        return calcResult;
    }
	
	/**
	 * Exception handler that will handle anything that derives
	 * from the Exception class.
	 * 
	 * @param e is the Exception object containing the exception thrown
	 * @return CalcError containing the exception message
	 */
    @ResponseStatus(HttpStatus.BAD_REQUEST)  
    @ExceptionHandler(value = Exception.class)  
    public CalcError handleBaseException(Exception e) {
    	CalcError calcError = new CalcError();
    	calcError.setMessage(e.getMessage());
    	
        return calcError;  
    }  
}
