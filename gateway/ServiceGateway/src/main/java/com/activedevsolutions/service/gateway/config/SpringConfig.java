package com.activedevsolutions.service.gateway.config;

import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.web.ErrorPageFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Disabling the ErrorPageFilter since this is just pure REST.
 * 
 * @author techguy
 *
 */
@Configuration
public class SpringConfig {
	/**
	 * Creates an ErrorPageFilter.
	 * @return ErrorPageFilter holds a new instance
	 */
	@Bean
	public ErrorPageFilter errorPageFilter() {
	    return new ErrorPageFilter();
	}

	/**
	 * Disables the error filter.
	 * 
	 * @param filter the filter to disable
	 * @return FilterRegistrationBean
	 */
	@Bean
	public FilterRegistrationBean disableSpringBootErrorFilter(ErrorPageFilter filter) {
	    FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
	    filterRegistrationBean.setFilter(filter);
	    filterRegistrationBean.setEnabled(false);
	    return filterRegistrationBean;
	}
}
