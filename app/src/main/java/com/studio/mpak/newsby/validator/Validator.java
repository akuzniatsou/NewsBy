package com.studio.mpak.newsby.validator;

/**
 * @author Andrei Kuzniatsou
 */
public interface Validator<T> {

    boolean isValid(T domain);
}
