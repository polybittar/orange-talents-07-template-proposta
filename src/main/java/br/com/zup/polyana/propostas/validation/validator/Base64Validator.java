package br.com.zup.polyana.propostas.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class Base64Validator implements ConstraintValidator<Base64Valid, Object> {



    @Override
    public void initialize(Base64Valid constraintAnnotation) {

    }

    @Override
    //entra em MethodArgumentNotValidException, status 400
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        return (value instanceof String &&
                ((String) value).length()>0 &&
                ((String) value).matches("^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{4})$"));
    }

}