package com.cyzest.findbooks.common;

import org.modelmapper.ModelMapper;

import java.lang.reflect.Type;

/**
 *
 * ModelMapper Wrapper 클래스
 * map() 매서드에 소스객체가 null이 들어가면 null이 리턴되도록 수정
 *
 */
public class BasedModelMapper extends ModelMapper {

    @Override
    public <D> D map(Object source, Class<D> destinationType) {
        if(source != null){
            return super.map(source, destinationType);
        }else{
            return null;
        }
    }

    @Override
    public <D> D map(Object source, Class<D> destinationType, String typeMapName) {
        if(source != null){
            return super.map(source, destinationType, typeMapName);
        }else{
            return null;
        }
    }

    @Override
    public <D> D map(Object source, Type destinationType) {
        if(source != null){
            return super.map(source, destinationType);
        }else{
            return null;
        }
    }

    @Override
    public <D> D map(Object source, Type destinationType, String typeMapName) {
        if(source != null){
            return super.map(source, destinationType, typeMapName);
        }else{
            return null;
        }
    }

}
