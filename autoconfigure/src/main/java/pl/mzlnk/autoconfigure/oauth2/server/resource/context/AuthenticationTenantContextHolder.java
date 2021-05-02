package pl.mzlnk.autoconfigure.oauth2.server.resource.context;

public class AuthenticationTenantContextHolder {

    private static AuthenticationTenantContextHolderStrategy strategy;
    private static int initializeCount = 0;

    static {
        initialize();
    }

    public AuthenticationTenantContextHolder() {
    }

    private static void initialize() {
        strategy = new ThreadLocalAuthenticationTenantContextHolderStrategy();
        ++initializeCount;
    }

    public static void clearContext() {
        strategy.clearContext();
    }

    public static AuthenticationTenantContext getContext() {
        return strategy.getContext();
    }

    public static void setContext(AuthenticationTenantContext context) {
        strategy.setContext(context);
    }

    public static int getInitializeCount() {
        return initializeCount;
    }

    public static void setStrategyName(String strategyName) {
        throw new UnsupportedOperationException("This feature is not yet supported");
    }

    public static AuthenticationTenantContextHolderStrategy getContextHolderStrategy() {
        return strategy;
    }

    public static AuthenticationTenantContext createEmptyContext() {
        return strategy.createEmptyContext();
    }

    public String toString() {
        return "AuthenticationTenantContextHolder[strategy='" + strategy.getName() + "'; initializeCount=" + initializeCount + "]";
    }

}
