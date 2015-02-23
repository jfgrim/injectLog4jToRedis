package inject.log4j.redis.rest.domain;

public class MaxIterationException extends Exception {

    public MaxIterationException(Object... objects) {
        super(String.format("Le maximum d'itération est dépassé ! ({}/{})", objects));
    }
}
