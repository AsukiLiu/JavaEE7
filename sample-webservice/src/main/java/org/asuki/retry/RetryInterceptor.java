package org.asuki.retry;

import org.slf4j.Logger;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Interceptor
@RetryPolicy
public class RetryInterceptor {

    @Inject
    private Logger log;

    @AroundInvoke
    public Object invoke(final InvocationContext ic) {

        Object result = null;
        RetryPolicy policy = ic.getMethod().getAnnotation(RetryPolicy.class);
        int retryThreshold = policy.threshold();

        boolean failed = true;
        int retryCount = 0;

        do {
            try {
                if (retryCount > 0) {
                    log.info("'{}#{}' will be retried", ic.getTarget().getClass().getName(), ic.getMethod().getName());
                }
                result = ic.proceed();
                failed = false;
            } catch (Exception e) {
                log.error(e.getMessage());
                retryCount++;
            }
        } while (failed && retryCount <= retryThreshold);

        return result;
    }
}
