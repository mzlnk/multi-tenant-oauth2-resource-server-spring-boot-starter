package pl.mzlnk.autoconfigure.oauth2.server.resource.context;

import org.springframework.util.Assert;

public class ThreadLocalAuthenticationTenantContextHolderStrategy implements AuthenticationTenantContextHolderStrategy {

    private ThreadLocal<AuthenticationTenantContext> contextHolder = new ThreadLocal<>();

    ThreadLocalAuthenticationTenantContextHolderStrategy() {

    }

    @Override
    public String getName() {
        return "STRATEGY_THREAD_LOCAL";
    }

    @Override
    public void clearContext() {
        this.contextHolder.remove();
    }

    @Override
    public AuthenticationTenantContext getContext() {
        AuthenticationTenantContext context = contextHolder.get();

        if(context == null) {
            context = this.createEmptyContext();
            contextHolder.set(context);
        }

        return context;
    }

    @Override
    public void setContext(AuthenticationTenantContext context) {
        Assert.notNull(context, "Only non-null AuthenticationContext instances are permitted");
        contextHolder.set(context);
    }

    @Override
    public AuthenticationTenantContext createEmptyContext() {
        return new AuthenticationTenantContextImpl();
    }
}
