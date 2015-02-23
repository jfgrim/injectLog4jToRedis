package inject.log4j.redis.rest.domain;


public class MonitoredSystemData {

    String service;
    StatutEnum status;
    String errorReason;

    public static MonitoredSystemData onInfo(@SuppressWarnings("SameParameterValue") String service, StatutEnum status) {
        return new MonitoredSystemData(service, status);
    }

    public static MonitoredSystemData onError(@SuppressWarnings("SameParameterValue") String service, String errorReason) {
        return new MonitoredSystemData(service, errorReason);
    }

    private MonitoredSystemData(@SuppressWarnings("SameParameterValue") String service, StatutEnum status) {
        this.service = service;
        this.status = status;
        this.errorReason = null;
    }

    private MonitoredSystemData(String service, String errorReason) {
        this.service = service;
        this.status = StatutEnum.KO;
        this.errorReason = errorReason;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public StatutEnum getStatus() {
        return status;
    }

    public void setStatus(StatutEnum status) {
        this.status = status;
    }

    public String getErrorReason() {
        return errorReason;
    }

    public void setErrorReason(String errorReason) {
        this.errorReason = errorReason;
    }

    @Override
    public String toString() {
        return "MonitoredSystemData{" +
                "service='" + service + '\'' +
                ", status=" + status +
                ", errorReason='" + errorReason + '\'' +
                '}';
    }
}
