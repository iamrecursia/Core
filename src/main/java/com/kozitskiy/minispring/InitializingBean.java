package com.kozitskiy.minispring;

public interface InitializingBean {
    void afterPropertiesSet() throws Exception;
}
