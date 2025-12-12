package vip.luosu.additional.storage;

@FunctionalInterface
public interface BeforeStorageCleanCondition {

    boolean isContinue();
}
